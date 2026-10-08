package dataman.utility.guiConfig.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.utility.guiConfig.shared.GUIColumn_Mdl;

public abstract class RowGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtRowNo = new DmTextBoxNumeric(0, false, 3, true);

    public RowGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "220px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "200px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdCol()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "20%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Row No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtRowNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Column">
    interface GrdColumn_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GUIColumn_Mdl> {

        @Editor.Path("FieldCaption")
        ValueProvider<GUIColumn_Mdl, String> fieldCaption();

        @Editor.Path("FieldDataTypeText")
        ValueProvider<GUIColumn_Mdl, String> fieldDataType();

        @Editor.Path("MandatoryText")
        ValueProvider<GUIColumn_Mdl, String> mandatory();

        @Editor.Path("MaxLength")
        ValueProvider<GUIColumn_Mdl, String> maxLength();

        @Editor.Path("CaptionWidth")
        ValueProvider<GUIColumn_Mdl, String> captionWidth();

        @Editor.Path("ControlWidth")
        ValueProvider<GUIColumn_Mdl, String> controlWidth();

        @Editor.Path("NegativeText")
        ValueProvider<GUIColumn_Mdl, String> negative();

        @Editor.Path("DecimalText")
        ValueProvider<GUIColumn_Mdl, String> decimal();

        @Editor.Path("DefaultText")
        ValueProvider<GUIColumn_Mdl, String> defaultText();

        @Editor.Path("DefaultCode")
        ValueProvider<GUIColumn_Mdl, String> defaultCode();

        @Editor.Path("ViewFieldCode")
        ValueProvider<GUIColumn_Mdl, String> viewFieldCode();

        @Editor.Path("ViewFieldText")
        ValueProvider<GUIColumn_Mdl, String> viewFieldText();
    }
    final DmGrid<GUIColumn_Mdl, GrdColumn_PrpAcs> grdColumn = new DmGrid(GWT.create(GrdColumn_PrpAcs.class));

    private Grid iniGrdCol() {
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().code(), "S.No.", 35, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().name(), "Save in Field", 100, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().fieldCaption(), "Field Caption", 120, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().fieldDataType(), "Data Type", 65, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().defaultText(), "Default Text", 75, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().defaultCode(), "Default Code", 75, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().captionWidth(), "Caption Width", 75, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().controlWidth(), "Control Width", 75, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().decimal(), "Allow Decimal", 50, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().negative(), "Allow Negative", 50, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().viewFieldCode(), "View Field Code", 75, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().viewFieldText(), "View Field Text", 75, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().maxLength(), "Max Length", 50, "");
        grdColumn.addTextColumn(grdColumn.getPropertyAccess().mandatory(), "Mandatory", 50, "");
        grdColumn.initializeGrid(grdColumn.getPropertyAccess().fieldHashCode(), 1120, 200, 2);

        return grdColumn.getGrid();
    }
    //</editor-fold>
}
