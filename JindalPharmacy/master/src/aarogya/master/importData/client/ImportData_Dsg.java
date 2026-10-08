package aarogya.master.importData.client;

import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;

abstract class ImportData_Dsg {

    final String widgetName = new pmMaster().IMPORT_DATA.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwImport = new FlowPanel();
    final Button btnImport = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final Button btnRemove = new Button();
    final FlowPanel flwAddImport = new FlowPanel();
    final Button btnAddImport = new Button();

    public ImportData_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("555px");
        vrtInnerForm.add(toolContainer);
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

        label = new InlineLabel("Import");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-upload");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnImport.setTitle("Import");
        btnImport.addStyleName("dm-top-control-btnStyle");
        btnImport.getElement().appendChild(aBtn.getElement());
        btnImport.getElement().appendChild(label.getElement());
        btnImport.setWidth("70px");
        flwImport.add(btnImport);

        label = new InlineLabel("Add To Import");

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plus");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnAddImport.setTitle("Add To Import");
        btnAddImport.addStyleName("dm-top-control-btnStyle");
        btnAddImport.getElement().appendChild(aBtn.getElement());
        btnAddImport.getElement().appendChild(label.getElement());
        btnAddImport.setWidth("100px");
        flwAddImport.add(btnAddImport);

        label = new InlineLabel("Close");
        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close: (Escap)");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        label = new InlineLabel("Remove All");
        label.setWidth("60px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRemove.setTitle("Remove All");
        btnRemove.addStyleName("dm-top-control-btnStyle");
        btnRemove.getElement().appendChild(aBtn.getElement());
        btnRemove.getElement().appendChild(label.getElement());
        btnRemove.setWidth("85px");
        flwRefresh.add(btnRemove);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwAddImport);
        toolContainer.addWidget(flwImport);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        /*====================================< 3rd Row >====================================*/
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "980px", "544px", "left", null, DmPosition.NONE, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "544px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGridSampleDetail()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);


        /*====================================< ******* >====================================*/
        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridSampleDetail">
    interface GrdTableMaster_PrpAcs extends KeyFieldHashCode_T_PrpAcs<ImportData_Mdl> {

        @Editor.Path("ImportTo")
        ValueProvider<ImportData_Mdl, String> importTo();

        @Editor.Path("ImportFrom")
        ValueProvider<ImportData_Mdl, String> importFrom();

        @Editor.Path("FileFormat")
        ValueProvider<ImportData_Mdl, String> fileFormat();

    }

    final DmGrid<ImportData_Mdl, GrdTableMaster_PrpAcs> grdImportData = new DmGrid(GWT.create(GrdTableMaster_PrpAcs.class));

    public Grid iniGridSampleDetail() {
        DmColumnConfig<ImportData_Mdl, String> colStatus;
        grdImportData.addRowNumberColumn();
        grdImportData.addTextColumn(grdImportData.getPropertyAccess().importTo(), "Import To", 350, "");
        grdImportData.addTextColumn(grdImportData.getPropertyAccess().fileFormat(), "File Format", 380, "");
        grdImportData.addTextColumn(grdImportData.getPropertyAccess().importFrom(), "Import From", 370, "");
        colStatus = grdImportData.addTextColumn(grdImportData.getPropertyAccess().code(), "Status", 120, "");
        grdImportData.initializeGrid(grdImportData.getPropertyAccess().fieldHashCode(), 1280, 544);

        colStatus.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ImportData_Mdl model;
                StringBuilder cellDesign = new StringBuilder("");
                try {
                    model = grdImportData.getGrid().getStore().get(context.getIndex());
                    cellDesign.append("<div style='width: 100%; text-align: center; float: left; text-decoration: underline; color: blue;'>");
                    cellDesign.append(model.getImportStatus().getText());
                    cellDesign.append("</div>");
                    sb.appendHtmlConstant(cellDesign.toString());
                } catch (Exception e) {
                }
            }
        });
        return grdImportData.getGrid();
    }

    //</editor-fold>
}
