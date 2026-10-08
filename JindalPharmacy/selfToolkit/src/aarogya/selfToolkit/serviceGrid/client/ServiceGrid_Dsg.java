package aarogya.selfToolkit.serviceGrid.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;

class ServiceGrid_Dsg {

    final FlowPanel flwWgt = new FlowPanel();
    protected int colIndex = 0;
    protected String sgstUgstCaption = "SGST/UGST";

    public ServiceGrid_Dsg(int width, int height) {
        flwWgt.getElement().setAttribute("style", "float:left;");
        flwWgt.add(createSummaryGrid(width, height));
    }

    //<editor-fold defaultstate="collapsed" desc="ServiceGrd">
    interface Service_Praps extends KeyFieldHashCode_T_PrpAcs<ServiceDetail_Mdl> {

        @Editor.Path("Amount")
        ValueProvider<ServiceDetail_Mdl, String> rate();

        @Editor.Path("GstRate")
        ValueProvider<ServiceDetail_Mdl, String> gstRate();

        @Editor.Path("CgstAmt")
        ValueProvider<ServiceDetail_Mdl, String> cgstAmt();

        @Editor.Path("SgstOrUgstAmt")
        ValueProvider<ServiceDetail_Mdl, String> sgstOrUgstAmt();

        @Editor.Path("IgstAmt")
        ValueProvider<ServiceDetail_Mdl, String> igstAmt();

        @Editor.Path("Amount")
        ValueProvider<ServiceDetail_Mdl, String> taxableAmt();

        @Editor.Path("Hsn")
        ValueProvider<ServiceDetail_Mdl, String> hsn();

    }
    final DmGrid<ServiceDetail_Mdl, Service_Praps> grdServiceDetail = new DmGrid(GWT.create(Service_Praps.class));

    private Grid createSummaryGrid(int width, int height) {
        int colIndex = 0;

        grdServiceDetail.addRowNumberColumn();
        grdServiceDetail.addTextColumn(grdServiceDetail.getPropertyAccess().name(), "Service", 150, "");
        ++colIndex;
        grdServiceDetail.addTextColumn(grdServiceDetail.getPropertyAccess().hsn(), "SAC", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        grdServiceDetail.addTextColumn(grdServiceDetail.getPropertyAccess().taxableAmt(), "Amount", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        grdServiceDetail.addTextColumn(grdServiceDetail.getPropertyAccess().gstRate(), "At Rate", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++colIndex;
        grdServiceDetail.addTextColumn(grdServiceDetail.getPropertyAccess().sgstOrUgstAmt(), sgstUgstCaption, 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.colIndex = ++colIndex;
        grdServiceDetail.addTextColumn(grdServiceDetail.getPropertyAccess().cgstAmt(), "CGST", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdServiceDetail.addTextColumn(grdServiceDetail.getPropertyAccess().igstAmt(), "IGST", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdServiceDetail.initializeGrid(grdServiceDetail.getPropertyAccess().fieldHashCode(), width, height);
        return grdServiceDetail.getGrid();
    }

    //</editor-fold>
}
