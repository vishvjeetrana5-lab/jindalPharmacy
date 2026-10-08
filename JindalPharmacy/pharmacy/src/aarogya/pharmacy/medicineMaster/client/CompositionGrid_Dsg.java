/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.pharmacy.medicineMaster.client;

import aarogya.pharmacy.medicineMaster.shared.Sku_Mdl;
import aarogya.pharmacy.medicineMaster.shared.TxtHComposition_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class CompositionGrid_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    //<editor-fold defaultstate="collapsed" desc="TxtHComposition">
    interface TxtHComposition_PrpAcs extends KeyField_T_PrpAcs<TxtHComposition_Mdl> {

        @Editor.Path("RestrictGroupName")
        ValueProvider<TxtHComposition_Mdl, String> restrictGroupName();
    }
    final DmTextHelp<TxtHComposition_Mdl, TxtHComposition_PrpAcs> txtHComposition = new DmTextHelp(GWT.create(TxtHComposition_PrpAcs.class), 4, true);

    private void iniTxtHComposition() {
        txtHComposition.getDmGrid().addTextColumn(txtHComposition.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHComposition.getDmGrid().addTextColumn(txtHComposition.getDmGrid().getPropertyAccess().restrictGroupName(), "Restricted Group", 100, "restrictGroupName");
        txtHComposition.getDmGrid().initializeGrid(txtHComposition.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHComposition.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsBase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHIsBase() {
        txtHIsBase.getDmGrid().addTextColumn(txtHIsBase.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHIsBase.getDmGrid().initializeGrid(txtHIsBase.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHSKU">
    interface SKU_PrpAcs extends KeyField_T_PrpAcs<Sku_Mdl> {
    }
    final DmTextHelp<Sku_Mdl, SKU_PrpAcs> txtHSKU = new DmTextHelp(GWT.create(SKU_PrpAcs.class), 4, false);

    private void iniSKU() {
        txtHSKU.getDmGrid().addTextColumn(txtHSKU.getDmGrid().getPropertyAccess().name(), "Name", 0, "unit");
        txtHSKU.getDmGrid().initializeGrid(txtHSKU.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHSKU.getHelper().addSort("unit", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    final DmTextBoxNumeric txtPower = new DmTextBoxNumeric(2, false, 10, false);

    public CompositionGrid_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHComposition();
        iniTxtHIsBase();
        iniSKU();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "", "left", null, DmEnum.DmPosition.NONE, true);
//////////////////////////////////////////////////1st ROw///////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Composition");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHComposition));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
//////////////////////////////////////////////////2nd ROw///////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Power");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPower));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        lbl = new DmLabel("Unit");
        lbl.getElement().getStyle().setMarginLeft(45, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSKU));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
//////////////////////////////////////////////////3rd ROw///////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base Composition");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsBase));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
