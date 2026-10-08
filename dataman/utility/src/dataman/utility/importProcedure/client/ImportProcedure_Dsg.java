package dataman.utility.importProcedure.client;

import dataman.utility.importProcedure.shared.GrdProcedure_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.libraryMenu.self.shared.lmUtility;

abstract class ImportProcedure_Dsg implements IFileUploadNotifier {

    final String widgetName = new lmUtility().OSR_IMPORT.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwImport = new FlowPanel();
    final Button btnImport = new Button();
    final InlineLabel chkCheckAll = new InlineLabel();

    final DmTextBox txtFilePath = new DmTextBox(100, false);

    final FlowPanel flwUpload = new FlowPanel();
    DmFileUploadButton btnUpload;
    DmFileRuleType fileRuleType;

    public ImportProcedure_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("542px");
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
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-download");
        //aBtn.getElement().getStyle().setColor("blue");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnImport.addStyleName("dm-top-control-btnStyle");
        btnImport.setTitle("Import");
        btnImport.getElement().appendChild(aBtn.getElement());
        btnImport.getElement().appendChild(label.getElement());
        btnImport.setWidth("70px");
        flwImport.add(btnImport);

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
        DmLabel lbl;
        HorizontalPanel hrtPnl;
        Anchor aBtn;

        /*====================================< 3rd Row >====================================*/
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "980px", "517px", "left", null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", "30px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("File Name");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(78, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", "30px", "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtFilePath));
        hrtPnl.setCellWidth(txtFilePath, "100%");
        DmClient.setStyle(flwUpload, "100%", "20px", "left", null, DmPosition.NONE, true);
        fileRuleType = new DmFileRuleType();
        fileRuleType.setFilters("dat");
        fileRuleType.setFileSizeLimit("10240");
        btnUpload = new DmFileUploadButton(this, fileRuleType);
        aBtn = new Anchor();
        aBtn.addStyleName("fa  fa-paperclip");
        aBtn.getElement().getStyle().setColor("black");
        btnUpload.getButton().getElement().appendChild(aBtn.getElement());
        btnUpload.getButton().setSize("25px", "20px");
        flwUpload.add(vl.add(btnUpload));
        btnUpload.addStyleName("dm-button-grad");
        hrtPnl.add(flwUpload);
        hrtPnl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "486px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "486px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGridSampleDetail()));
        flwTd.add(chkCheckAll);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        chkCheckAll.addStyleName("fa fa-square-o");
        chkCheckAll.getElement().setAttribute("style", "position:absolute; top:36px; left:9px; z-index:10;cursor:pointer;");
        chkCheckAll.setTitle("Select all");

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridSampleDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdProcedure_Mdl> {

        @Editor.Path("IsChecked")
        ValueProvider<GrdProcedure_Mdl, String> isChecked();

        @Editor.Path("DisProcedureName")
        ValueProvider<GrdProcedure_Mdl, String> disProcedureName();
    }

    final DmGrid<GrdProcedure_Mdl, GrdPaging_PrpAcs> grdProcedure = new DmGrid(GWT.create(GrdPaging_PrpAcs.class));

    public Grid iniGridSampleDetail() {
        ColumnConfig<GrdProcedure_Mdl, String> selectCheckBox;
        selectCheckBox = grdProcedure.addTextColumn(grdProcedure.getPropertyAccess().isChecked(), "", 30, "");
        grdProcedure.addTextColumn(grdProcedure.getPropertyAccess().disProcedureName(), "Procedure", 920, "");
        grdProcedure.initializeGrid(grdProcedure.getPropertyAccess().fieldHashCode(), 1280, 485);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdProcedure.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        return grdProcedure.getGrid();

    }

    //</editor-fold>
}
