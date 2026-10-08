package dataman.utility.exportProcedure.client;

import dataman.utility.exportProcedure.shared.GrdProcedure_Mdl;
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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmUtility;

abstract class ExportProcedure_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().OSR_EXPORT.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwExport = new FlowPanel();
    final Button btnExport = new Button();
    final InlineLabel chkCheckAll = new InlineLabel();

    public ExportProcedure_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("555px");
        vrtInnerForm.add(iniToolContorls());
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

        label = new InlineLabel("Export");
        //  label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-upload");
        // aBtn.getElement().getStyle().setColor("blue");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnExport.addStyleName("dm-top-control-btnStyle");
        btnExport.setTitle("Export");
        btnExport.getElement().appendChild(aBtn.getElement());
        btnExport.getElement().appendChild(label.getElement());
        btnExport.setWidth("70px");
        flwExport.add(btnExport);

        label = new InlineLabel("Close");
        //  label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        // aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        /*====================================< 3rd Row >====================================*/
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "980px", "529px", "left", null, DmPosition.NONE, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "529px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGridSampleDetail()));
        flwTd.add(chkCheckAll);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        chkCheckAll.addStyleName("fa fa-square-o");
        chkCheckAll.getElement().setAttribute("style", "position:absolute; top:30px; left:9px; z-index:10;cursor:pointer;");

        chkCheckAll.setTitle("Select all");

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridSampleDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdProcedure_Mdl> {

        @Editor.Path("ProcedureName")
        ValueProvider<GrdProcedure_Mdl, String> procedureName();

    }

    final DmGrid<GrdProcedure_Mdl, GrdPaging_PrpAcs> grdProcedure = new DmGrid(GWT.create(GrdPaging_PrpAcs.class));

    private Grid iniGridSampleDetail() {
        ColumnConfig<GrdProcedure_Mdl, String> selectCheckBox;
        selectCheckBox = grdProcedure.addTextColumn(grdProcedure.getPropertyAccess().procedureName(), "", 30, "");
        grdProcedure.addTextColumn(grdProcedure.getPropertyAccess().procedureName(), "Procedure", 920, "SPECIFIC_NAME");
        grdProcedure.initializeGrid(grdProcedure.getPropertyAccess().fieldHashCode(), 1280, 529);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                boolean val = grdProcedure.getGrid().getStore().get(context.getIndex()).isChecked();
                if (val) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        return grdProcedure.getGrid();
    }
    //</editor-fold>
}
