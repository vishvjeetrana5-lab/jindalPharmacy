package dataman.utility.voucherType.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.utility.voucherType.shared.*;

public class ExportData_Dsg {

    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final Button btnExport = new Button();
    final Button btnCancel = new Button();
    final InlineLabel cbCheck = new InlineLabel();

    //<editor-fold defaultstate="collapsed" desc="grdVoucherType">
    interface GrdVoucherType_PrpAcs extends KeyField_T_PrpAcs<GrdVoucherType_Mdl> {

        @Editor.Path("SharedKeyFieldCode")
        ValueProvider<GrdVoucherType_Mdl, String> vType();

        @Editor.Path("ShortName")
        ValueProvider<GrdVoucherType_Mdl, String> shortName();

        @Editor.Path("Category")
        ValueProvider<GrdVoucherType_Mdl, String> category();
    }
    final DmGrid<GrdVoucherType_Mdl, GrdVoucherType_PrpAcs> grdVoucherType = new DmGrid(GWT.create(GrdVoucherType_PrpAcs.class));

    private Grid iniGrdVoucher() {
        DmColumnConfig<GrdVoucherType_Mdl, String> colCheck;
        colCheck = grdVoucherType.addTextColumn(grdVoucherType.getPropertyAccess().vType(), "", 25, "v_Type");
        grdVoucherType.addTextColumn(grdVoucherType.getPropertyAccess().name(), "Description", 100, "description");
        grdVoucherType.addTextColumn(grdVoucherType.getPropertyAccess().category(), "Short Name", 100, "short_Name");
        grdVoucherType.addTextColumn(grdVoucherType.getPropertyAccess().category(), "category", 100, "category");
        grdVoucherType.initializeGrid(grdVoucherType.getPropertyAccess().code(), 395, 245, 2);

        colCheck.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                boolean val = grdVoucherType.getGrid().getStore().get(context.getIndex()).isChecked();
                if (val) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (!val) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        return grdVoucherType.getGrid();
    }
    //</editor-fold>

    public ExportData_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("400px");
        popupPanel.setHeight("275px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(iniGrdVoucher());
        cbCheck.addStyleName("fa fa-square-o");
        cbCheck.getElement().setAttribute("style", "position:absolute; top:35px; left:9px; z-index:50;cursor:pointer;");
        cbCheck.setTitle("Select all");
        flw.add(cbCheck);
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Export");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-upload");
        // aBtn.getElement().getStyle().setColor("blue");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnExport.addStyleName("dm-top-control-btnStyle");
        btnExport.setTitle("Export");
        btnExport.getElement().appendChild(aBtn.getElement());
        btnExport.getElement().appendChild(label.getElement());
        btnExport.setWidth("70px");

        label = new InlineLabel("Close");
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        // aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");

        toolContainer.addWidget(btnExport);
        toolContainer.addWidget(btnCancel);
        toolContainer.addGap();
        toolContainer.setModeVisible(false);
        toolContainer.setTitle("Export Voucher Type");
        return toolContainer;
    }

}
