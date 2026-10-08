package aarogya.selfToolkit.taxSummary.client;

import aarogya.selfToolkit.taxSummary.shared.TaxSummary_Mdl;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.util.ArrayList;
import java.util.List;

public class TaxSummary extends TaxSummary_Dsg implements IsWidget {

    public TaxSummary(int width, int height, int autoExpandColumn) {
        super(width, height, autoExpandColumn);
    }

    public void initialize(boolean isGstApplicable) {
        manageGridColumns(isGstApplicable);
    }

    public void reset(List<List<OverHeadLine_Mdl>> ohlLists) {
        grdTaxSummary.getGrid().getStore().clear();

        List<TaxSummary_Mdl> tsmList = new ArrayList<>();
        TaxSummary_Mdl tsmNode;
        boolean isNewTaxRate = false;
        double rate, cgstAmt, sgstAmt, ugstAmt, igstAmt, taxableAmt, totalAmt;

        if (ohlLists == null) {
            return;
        }

        for (List<OverHeadLine_Mdl> ohlList : ohlLists) {

            if (ohlList == null) {
                return;
            }

            isNewTaxRate = true;
            rate = 0;
            cgstAmt = 0;
            sgstAmt = 0;
            ugstAmt = 0;
            igstAmt = 0;
            taxableAmt = 0;
            totalAmt = 0;

            for (OverHeadLine_Mdl ohl : ohlList) {
                if (ohl == null) {
                    continue;
                }
                switch (DmShared.xNull(ohl.getOhManualCode()).trim().toUpperCase()) {
                    case "CGST":
                        rate += DmShared.vNull(ohl.getAtRate());
                        cgstAmt += DmShared.vNull(ohl.getAmount());
                        totalAmt += DmShared.vNull(ohl.getAmount());
                        break;
                    case "SGST":
                        rate += DmShared.vNull(ohl.getAtRate());
                        sgstAmt += DmShared.vNull(ohl.getAmount());
                        totalAmt += DmShared.vNull(ohl.getAmount());
                        break;
                    case "UGST":
                        rate += DmShared.vNull(ohl.getAtRate());
                        ugstAmt += DmShared.vNull(ohl.getAmount());
                        totalAmt += DmShared.vNull(ohl.getAmount());
                        break;
                    case "IGST":
                        rate += DmShared.vNull(ohl.getAtRate());
                        igstAmt += DmShared.vNull(ohl.getAmount());
                        totalAmt += DmShared.vNull(ohl.getAmount());
                        break;
                    case "TV":
                        taxableAmt += DmShared.vNull(ohl.getAmount());
                        break;
                }
            }

            for (TaxSummary_Mdl tsm : tsmList) {
                if (DmShared.vNull(tsm.getRate()).doubleValue() == DmShared.vNull(rate).doubleValue()) {
                    tsm.setCgstAmt(DmClient.vNull(DmShared.vNull(tsm.getCgstAmt()) + cgstAmt, "#.#######"));
                    tsm.setSgstAmt(DmClient.vNull(DmShared.vNull(tsm.getSgstAmt()) + sgstAmt, "#.#######"));
                    tsm.setUgstAmt(DmClient.vNull(DmShared.vNull(tsm.getUgstAmt()) + ugstAmt, "#.#######"));
                    tsm.setSgstOrUgstAmt(DmClient.vNull(DmShared.vNull(tsm.getSgstOrUgstAmt()) + sgstAmt + ugstAmt, "#.#######"));
                    tsm.setIgstAmt(DmClient.vNull(DmShared.vNull(tsm.getIgstAmt()) + igstAmt, "#.#######"));
                    tsm.setTaxableAmt(DmClient.vNull(DmShared.vNull(tsm.getTaxableAmt()) + taxableAmt, "#.#######"));
                    tsm.setTotalAmt(DmClient.vNull(DmShared.vNull(tsm.getTotalAmt()) + totalAmt, "#.#######"));
                    isNewTaxRate = false;
                    break;
                }
            }

            if (isNewTaxRate && DmShared.vNull(rate).doubleValue() != 0) {
                tsmNode = new TaxSummary_Mdl();
                tsmNode.setRate(DmClient.vNull(rate, "#.#######"));
                tsmNode.setCgstAmt(DmClient.vNull(cgstAmt, "#.#######"));
                tsmNode.setSgstAmt(DmClient.vNull(sgstAmt, "#.#######"));
                tsmNode.setUgstAmt(DmClient.vNull(ugstAmt, "#.#######"));
                tsmNode.setSgstOrUgstAmt(DmClient.vNull(sgstAmt + ugstAmt, "#.#######"));
                tsmNode.setIgstAmt(DmClient.vNull(igstAmt, "#.#######"));
                tsmNode.setTaxableAmt(DmClient.vNull(taxableAmt, "#.#######"));
                tsmNode.setTotalAmt(DmClient.vNull(totalAmt, "#.#######"));
                tsmList.add(tsmNode);
            }

        }

        grdTaxSummary.getGrid().getStore().addAll(tsmList);
    }

    @Override
    public Widget asWidget() {
        return flwWgt;
    }

    private void manageGridColumns(boolean isGstApplicable) {
        DmColumnConfig colSGSTOn, colCGSTOn, colIGSTOn;
        List<TaxSummary_Mdl> grdDetails = new ArrayList<>();
        grdDetails.addAll(grdTaxSummary.getGrid().getStore().getAll());
        grdTaxSummary.getGrid().getStore().clear();
        colSGSTOn = ((DmColumnConfig) grdTaxSummary.getGrid().getColumnModel().getColumn(colIndexSGST_UGST));

        colCGSTOn = ((DmColumnConfig) grdTaxSummary.getGrid().getColumnModel().getColumn(colIndexCGST));

        colIGSTOn = ((DmColumnConfig) grdTaxSummary.getGrid().getColumnModel().getColumn(colIndexIGST));

        colSGSTOn.setHidden(!isGstApplicable);
        colCGSTOn.setHidden(!isGstApplicable);
        colIGSTOn.setHidden(!isGstApplicable);

        grdTaxSummary.getGrid().getStore().addAll(grdDetails);

    }
}
