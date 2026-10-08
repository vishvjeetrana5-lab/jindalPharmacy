package aarogya.pharmacy.medicineMaster.client;

import aarogya.pharmacy.medicineMaster.shared.TxtHItemsName_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class SchmeGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    //<editor-fold defaultstate="collapsed" desc="TEXTBOX">
    final DmTextBox txtDescription = new DmTextBox(50, true);
    final DmTextBoxNumeric txtSchemeOnQty = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBoxNumeric txtSchemeFreeQty = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBox txtUnit = new DmTextBox(50, true);
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    //<editor-fold defaultstate="collapsed" desc="txtHMedicineName">

    public interface Medicine_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHItemsName_Mdl> {

        @Editor.Path("SaleUnitName")
        ValueProvider<TxtHItemsName_Mdl, String> saleUnitName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItemsName_Mdl, String> manualCode();

        @Editor.Path("CompositionName")
        ValueProvider<TxtHItemsName_Mdl, String> compositionName();

        @Editor.Path("ItemPacking")
        ValueProvider<TxtHItemsName_Mdl, String> itemPacking();

    }
    final DmTextHelp<TxtHItemsName_Mdl, Medicine_PrpAcs> txtHMedicineName = new DmTextHelp(GWT.create(Medicine_PrpAcs.class), 7, true);

    private void iniTxtHMedicineName() {
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().name(), "Medicine Name", 220, "name");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().saleUnitName(), "Sale Unit", 80, "saleUnitName");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 80, "manualCode");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().compositionName(), "Composition", 300, "compositionName");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().itemPacking(), "Pack Size", 150, "itemPacking");
        txtHMedicineName.getDmGrid().initializeGrid(txtHMedicineName.getDmGrid().getPropertyAccess().fieldHashCode(), 850, 0, 1);
        txtHMedicineName.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
//</editor-fold>

    public SchmeGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHMedicineName();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "110px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtUnit));
        txtUnit.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Scheme On Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Scheme Free Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSchemeOnQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSchemeFreeQty));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Description");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDescription));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
