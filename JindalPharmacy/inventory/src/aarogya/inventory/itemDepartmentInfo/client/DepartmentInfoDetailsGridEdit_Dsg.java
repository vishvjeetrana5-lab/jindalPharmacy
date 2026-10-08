package aarogya.inventory.itemDepartmentInfo.client;

import aarogya.inventory.itemDepartmentInfo.shared.TxtHItem_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class DepartmentInfoDetailsGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtMinLvl = new DmTextBoxNumeric(4, false, 10, false);
    final DmTextBoxNumeric txtMaxLvl = new DmTextBoxNumeric(4, false, 10, false);
    final DmTextBoxNumeric txtReOrder = new DmTextBoxNumeric(4, false, 10, false);
    final DmTextBox txtBin = new DmTextBox(20, false);
    final DmLabel lblDepartmentName = new DmLabel("");
    final DmTextBox txtUnit = new DmTextBox(50, false);

    interface TxtHItem_PrpAcs extends KeyField_T_PrpAcs<TxtHItem_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItem_Mdl, String> manualCode();

        @Editor.Path("SkuName")
        ValueProvider<TxtHItem_Mdl, String> skuName();

        @Editor.Path("ItemGroup")
        ValueProvider<TxtHItem_Mdl, String> itemGroup();

        @Editor.Path("ItemCategory")
        ValueProvider<TxtHItem_Mdl, String> itemCategory();
    }

    final DmTextHelp<TxtHItem_Mdl, TxtHItem_PrpAcs> txtHItemName = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHItem_PrpAcs.class), 10, true);

    public void iniTxtHItem() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Item Name", 360, "itemName");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().skuName(), "SKU", 70, "skuName");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().itemGroup(), "Item Group", 200, "itemGroup");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().itemCategory(), "Item Category", 100, "itemCategory");
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().code(), 850, 0, 1);
        txtHItemName.getHelper().addSort("itemName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSiteName">
    interface GridSite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }

    public DepartmentInfoDetailsGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHItem();

        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "84px", null, null, DmPosition.CENTER, true);

        //============================== 1st Row ========================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHItemName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SKU");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);

        flwTd.add(vl.add(txtUnit));
        txtUnit.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //============================== END =========================================
        //================================ 2nd Row ===================================
        //============================== END =========================================
        //================================ 3rd Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Min. Level");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMinLvl));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Max. Level");
        lbl.getElement().getStyle().setMarginLeft(122, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMaxLvl));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //============================== END =========================================

        //================================ 4th Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Re-Order Level");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReOrder));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bin");
        lbl.getElement().getStyle().setMarginLeft(122, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBin));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END =========================================

        return flwTable;
    }

}
