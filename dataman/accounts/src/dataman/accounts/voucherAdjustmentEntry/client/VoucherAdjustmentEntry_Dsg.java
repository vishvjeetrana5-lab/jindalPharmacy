package dataman.accounts.voucherAdjustmentEntry.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.accounts.voucherAdjustmentEntry.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.itemAdjustment.shared.GrdItemAdjustment_Mdl;
import dataman.libraryMenu.self.shared.lmAccounts;

public class VoucherAdjustmentEntry_Dsg {

    final String widgetName = new lmAccounts().VOUCHER_ADJUSTMENT_ENTRY.caption;
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnSave = new Button();
    final Button btnFill = new Button();
    final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="Ledger Grid">
    interface GrdLedger_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLedger_Mdl> {

        @Editor.Path("AcName")
        ValueProvider<GrdLedger_Mdl, String> acName();

        @Editor.Path("AcManualCode")
        ValueProvider<GrdLedger_Mdl, String> acManualCode();

        @Editor.Path("CityName")
        ValueProvider<GrdLedger_Mdl, String> cityName();

        @Editor.Path("Narration")
        ValueProvider<GrdLedger_Mdl, String> narration();

        @Editor.Path("Debit")
        ValueProvider<GrdLedger_Mdl, String> debit();

        @Editor.Path("Credit")
        ValueProvider<GrdLedger_Mdl, String> credit();

        @Editor.Path("Adjustment")
        ValueProvider<GrdLedger_Mdl, String> adjustment();

    }

    final DmGrid<GrdLedger_Mdl, GrdLedger_PrpAcs> grdAdjustment = new DmGrid(GWT.create(GrdLedger_PrpAcs.class));

    private void iniGrdLedger() {
        ColumnConfig<GrdLedger_Mdl, String> adjustmentColumn, itemAdjustmentColumn;

        grdAdjustment.addRowNumberColumn();
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().acManualCode(), "A/c Code", 113, null);
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().acName(), "A/c Name", 270, null);
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().cityName(), "City", 174, null);
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().narration(), "Narration", 290, null);
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().debit(), "Debit", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().credit(), "Credit", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        adjustmentColumn = grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().adjustment(), "Account Adj.", 100, null);
        adjustmentColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        adjustmentColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double totalAdjustment = 0;

                GrdLedger_Mdl glMdl = grdAdjustment.getGrid().getStore().get(context.getIndex());
                for (GrdAdjustment_Mdl gaMdl : glMdl.getLstAdjustment()) {
                    totalAdjustment += DmShared.vNull(gaMdl.getAdjustment());
                }

                if (totalAdjustment > 0) {
                    sb.appendHtmlConstant(DmClient.vNull(totalAdjustment, "0.00"));
                }
            }
        });

        itemAdjustmentColumn = grdAdjustment.addTextColumn(grdAdjustment.getPropertyAccess().adjustment(), "Item Adj.", 100, null);
        itemAdjustmentColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        itemAdjustmentColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double totalItemAdjustment = 0;

                GrdLedger_Mdl glMdl = grdAdjustment.getGrid().getStore().get(context.getIndex());
                for (GrdItemAdjustment_Mdl gaMdl : glMdl.getLstItemAdjustment()) {
                    totalItemAdjustment += DmShared.vNull(gaMdl.getAdjustment());
                }

                if (totalItemAdjustment > 0) {
                    sb.appendHtmlConstant(DmClient.vNull(totalItemAdjustment, "0.00"));
                }
            }
        });

        grdAdjustment.initializeGrid(grdAdjustment.getPropertyAccess().fieldHashCode(), 1280, 500);
    }
    //</editor-fold>

    public VoucherAdjustmentEntry_Dsg() {
        iniGrdLedger();
        ini();
    }

    private DmToolContainer iniToolControls() {
        Anchor aBtn;

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        btnSave.setTitle("Save: (Ctrl + S)");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plus");
        btnFill.setTitle("Fill: (Alt + F)");
        btnFill.addStyleName("dm-top-control-btnStyle");
        btnFill.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        btnClose.setTitle("Close: (Esc)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(btnSave);
        toolContainer.addWidget(btnFill);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.CENTER_HORIZONTAL, true);
        flwForm.addStyleName("dm-FlwForm");
        vrtInnerForm.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTable.add(vl.add(grdAdjustment.getGrid()));
        //====================================< ******* >====================================//
        return flwTable;
    }

}
