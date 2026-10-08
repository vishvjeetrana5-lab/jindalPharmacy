package aarogya.selfToolkit.taxSummary.client;

import aarogya.selfToolkit.taxSummary.shared.TaxSummary_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;

class TaxSummary_Dsg {

    protected int colIndexSGST_UGST = 0, colIndexCGST = 0, colIndexIGST = 0;
    final FlowPanel flwWgt = new FlowPanel();

    public TaxSummary_Dsg(int width, int height, int autoExpandColumn) {
        flwWgt.getElement().setAttribute("style", "float:left;");
        flwWgt.add(getGrdTaxSummary(width, height, autoExpandColumn));
    }

    interface GrdTaxSummary_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TaxSummary_Mdl> {

        @Editor.Path("Rate")
        ValueProvider<TaxSummary_Mdl, String> rate();

        @Editor.Path("CgstAmt")
        ValueProvider<TaxSummary_Mdl, String> cgstAmt();

        @Editor.Path("SgstAmt")
        ValueProvider<TaxSummary_Mdl, String> sgstAmt();

        @Editor.Path("UgstAmt")
        ValueProvider<TaxSummary_Mdl, String> ugstAmt();

        @Editor.Path("SgstOrUgstAmt")
        ValueProvider<TaxSummary_Mdl, String> sgstOrUgstAmt();

        @Editor.Path("IgstAmt")
        ValueProvider<TaxSummary_Mdl, String> igstAmt();

        @Editor.Path("TaxableAmt")
        ValueProvider<TaxSummary_Mdl, String> taxableAmt();

        @Editor.Path("TotalAmt")
        ValueProvider<TaxSummary_Mdl, String> totalAmt();

    }

    final DmGrid<TaxSummary_Mdl, GrdTaxSummary_PrpAcs> grdTaxSummary = new DmGrid(GWT.create(GrdTaxSummary_PrpAcs.class));

    private Grid getGrdTaxSummary(int width, int height, int autoExpandColumn) {
        int colIndex = 0;

        colIndex = 0;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().rate(), "Tax %", 60, "");
        ++colIndex;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().taxableAmt(), "Taxable Amt.", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colIndexSGST_UGST = ++colIndex;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().sgstOrUgstAmt(), "SGST / UGST", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colIndexCGST = ++colIndex;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().cgstAmt(), "CGST", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colIndexIGST = ++colIndex;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().igstAmt(), "IGST", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        grdTaxSummary.addTextColumn(grdTaxSummary.getPropertyAccess().totalAmt(), "Total Tax Amt.", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdTaxSummary.initializeGrid(grdTaxSummary.getPropertyAccess().fieldHashCode(), width, height, autoExpandColumn);
        grdTaxSummary.getGrid().setTitle("Tax Summary");
        grdTaxSummary.getGrid().getView().setViewConfig(new GridViewConfig<TaxSummary_Mdl>() {
            @Override
            public String getColStyle(TaxSummary_Mdl model, ValueProvider<? super TaxSummary_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                return "dm-GridCellFormat dm-GridCellDisable";
            }

            @Override
            public String getRowStyle(TaxSummary_Mdl model, int rowIndex) {
                return "";
            }
        });
        return grdTaxSummary.getGrid();
    }
}
