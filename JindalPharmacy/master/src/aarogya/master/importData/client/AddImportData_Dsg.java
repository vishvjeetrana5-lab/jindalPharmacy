package aarogya.master.importData.client;

import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import aarogya.master.importData.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public abstract class AddImportData_Dsg {

    public final static String widgetName = "Add To Import";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final InlineLabel chkCheckAll = new InlineLabel();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public AddImportData_Dsg() {
        ini();
        iniGridImport();
    }

    private void ini() {
        FlowPanel flw, flwTr, flwTd;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("725px");
        popupPanel.setHeight("466px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        DmClient.setStyle(flw, "100%", "440px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);
        flw.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "422px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "422px", "left", null, DmPosition.NONE, false);
        flwTd.add(BottomArea());
        flwTr.add(flwTd);
        flw.add(flwTr);

        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private FlowPanel BottomArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "422px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "422px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "422px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(grdImport));
        flwTd.add(chkCheckAll);
        flwTr.add(flwTd);

        chkCheckAll.addStyleName("fa fa-square-o");
        chkCheckAll.getElement().setAttribute("style", "position:absolute; top:5px; left:9px; z-index:10;cursor:pointer;");
        chkCheckAll.setTitle("Select all");

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
        flwClose.add(vl.add(btnClose));

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();

        return toolContainer;
    }

    //<editor-fold defaultstate="collapsed" desc="grdImport">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<ImportData_Mdl> {

        @Editor.Path("IsChecked")
        ValueProvider<ImportData_Mdl, String> isChecked();

        @Editor.Path("ImportTo")
        ValueProvider<ImportData_Mdl, String> importTo();

    }

    final DmGridPaging<ImportData_Mdl, GrdPaging_PrpAcs, SearchValue_Mdl> grdImport = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 18, false);

    private void iniGridImport() {
        ColumnConfig<ImportData_Mdl, String> selectCheckBox;
        selectCheckBox = grdImport.getDmGrid().addTextColumn(grdImport.getDmGrid().getPropertyAccess().isChecked(), "", 30, "");
        grdImport.getDmGrid().addTextColumn(grdImport.getDmGrid().getPropertyAccess().importTo(), "Import To", 670, "Name");
        grdImport.getDmGrid().initializeGrid(grdImport.getDmGrid().getPropertyAccess().fieldHashCode(), 720, 0);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdImport.getDmGrid().getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
    }
    //</editor-fold>
}
