package aarogya.pharmacy.medicineRequirement.client;

import aarogya.pharmacy.medicineRequirement.shared.GrdMedicine_Mdl;
import aarogya.pharmacy.medicineRequirement.shared.SearchValue_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;

abstract class MedicineRequirement_Dsg {

    public final static String widgetName = new pmPharmacy().MEDICINE_REQUIREMENT.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final Button btnClose = new Button();
    final Button btnPrint = new Button();
    final Button btnSave = new Button();
    final Button btnSetting = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    protected int colIndexGroupOn = 0, colIndexMedicineName = 0;

    public MedicineRequirement_Dsg() {
        ini();
        iniGridMedicines();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("550px");
        vrtInnerForm.add(vl.add(iniToolContorls()));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.setTitle("Save");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Print");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnSave);
        toolContainer.addWidget(btnPrint);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "550px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(topArea()));

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "530px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(gridMedicines));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", "20px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Indent No.");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridMedicines">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicine_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<GrdMedicine_Mdl, String> departmentName();

        @Editor.Path("SkuName")
        ValueProvider<GrdMedicine_Mdl, String> skuName();

        @Editor.Path("MinLevel")
        ValueProvider<GrdMedicine_Mdl, String> minLevel();

        @Editor.Path("MaxLevel")
        ValueProvider<GrdMedicine_Mdl, String> maxLevel();

        @Editor.Path("ReorderLevel")
        ValueProvider<GrdMedicine_Mdl, String> reorderLevel();

        @Editor.Path("StockQty")
        ValueProvider<GrdMedicine_Mdl, String> stockQty();

        @Editor.Path("RequiredQty")
        ValueProvider<GrdMedicine_Mdl, String> requiredQty();

        @Editor.Path("IsChecked")
        ValueProvider<GrdMedicine_Mdl, String> isChecked();

        @Editor.Path("GroupName")
        ValueProvider<GrdMedicine_Mdl, String> groupName();

    }

    final DmGridPaging<GrdMedicine_Mdl, GrdPaging_PrpAcs, SearchValue_Mdl> gridMedicines = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 23, false);

    private void iniGridMedicines() {
        ColumnConfig<GrdMedicine_Mdl, String> selectionColumn;
        int colIndex = 0;
        selectionColumn = gridMedicines.getDmGrid().addTextColumn(gridMedicines.getDmGrid().getPropertyAccess().isChecked(), "Hold", 50, "");

        colIndexGroupOn = ++colIndex;
        gridMedicines.getDmGrid().addTextColumn(gridMedicines.getDmGrid().getPropertyAccess().groupName(), "Group On", 150, "groupByName");
        colIndexMedicineName = ++colIndex;
        gridMedicines.getDmGrid().addTextColumn(gridMedicines.getDmGrid().getPropertyAccess().name(), "Medicine", 1, "medicineName");
        ++colIndex;
        gridMedicines.getDmGrid().addTextColumn(gridMedicines.getDmGrid().getPropertyAccess().departmentName(), "Department", 100, "departmentName");
        ++colIndex;
        gridMedicines.getDmGrid().addTextColumn(gridMedicines.getDmGrid().getPropertyAccess().skuName(), "SKU", 50, "skuName");
        ++colIndex;
        gridMedicines.getDmGrid().addTextColumn(gridMedicines.getDmGrid().getPropertyAccess().minLevel(), "Min. Qty.", 80, "minLevel", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        gridMedicines.getDmGrid().addTextColumn(gridMedicines.getDmGrid().getPropertyAccess().maxLevel(), "Max. Qty.", 80, "maxLevel", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        gridMedicines.getDmGrid().addTextColumn(gridMedicines.getDmGrid().getPropertyAccess().reorderLevel(), "Reorder Level", 100, "reorderLevel", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        gridMedicines.getDmGrid().addTextColumn(gridMedicines.getDmGrid().getPropertyAccess().stockQty(), "Stock Qty.", 100, "stockQty", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        gridMedicines.getDmGrid().addTextColumn(gridMedicines.getDmGrid().getPropertyAccess().requiredQty(), "Required Qty.", 100, "requiredQty", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        gridMedicines.getDmGrid().initializeGrid(gridMedicines.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 3);

        selectionColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectionColumn.setCellPadding(false);

        selectionColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridMedicines.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        gridMedicines.getHelper().addSort("departmentName", DmEnum.DmSortType.ASC);
        gridMedicines.getHelper().addSort("groupByName", DmEnum.DmSortType.ASC);
        gridMedicines.getHelper().addSort("medicineName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

}
