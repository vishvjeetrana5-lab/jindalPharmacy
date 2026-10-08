package aarogya.selfToolkit.itemUpdatePopup.client;

import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdateField_Mdl;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class ItemUpdatePopup_Dsg {

    final String widgetName = "Medicine Update";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final Button btnOk = new Button();
    final Button btnClose = new Button();

    public ItemUpdatePopup_Dsg() {
        iniLedgerMastSearch();
        ini();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("520px");
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.getElement().setAttribute("Style", "float:left;");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        //   label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        //aBtn.getElement().getStyle().setColor("green");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.setTitle("Ok");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");

        label = new InlineLabel("Close");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        //aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(topArea());
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; border-top: 1px solid gray;");
        flwTr.add(vl.add(grid.getGrid()));
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(new Label("Following values has been changed. Please select your action - "));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grid">
    interface DmGrid_PrpAcs extends KeyFieldHashCode_T_PrpAcs<ItemUpdateField_Mdl> {

        @Editor.Path("MasterValue")
        ValueProvider<ItemUpdateField_Mdl, String> masterValue();

        @Editor.Path("CurrentValue")
        ValueProvider<ItemUpdateField_Mdl, String> currentValue();

        @Editor.Path("FinalValue")
        ValueProvider<ItemUpdateField_Mdl, String> finalValue();

        @Editor.Path("SaveToCompany")
        ValueProvider<ItemUpdateField_Mdl, String> saveToCompany();

        @Editor.Path("SaveToItem")
        ValueProvider<ItemUpdateField_Mdl, String> saveToItem();

    }

    final DmGrid<ItemUpdateField_Mdl, DmGrid_PrpAcs> grid = new DmGrid(GWT.create(DmGrid_PrpAcs.class));

    private void iniLedgerMastSearch() {
        ColumnConfig<ItemUpdateField_Mdl, String> saveToItem;
        ColumnConfig<ItemUpdateField_Mdl, String> saveToCompany;

        grid.addTextColumn(grid.getPropertyAccess().name(), "Field", 120, "");
        grid.addTextColumn(grid.getPropertyAccess().masterValue(), "Previous Value", 100, "");
        grid.addTextColumn(grid.getPropertyAccess().finalValue(), "Current Value", 98, "");

        saveToItem = grid.addTextColumn(grid.getPropertyAccess().saveToItem(), "Save To Item", 100, "");
        saveToItem.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        saveToItem.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.iNull(grid.getGrid().getStore().get(context.getIndex()).getSaveToItem()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        saveToCompany = grid.addTextColumn(grid.getPropertyAccess().saveToCompany(), "Save To Company", 100, "");
        saveToCompany.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        saveToCompany.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ItemUpdateField_Mdl.Field field = grid.getGrid().getStore().get(context.getIndex()).getField();
                if (field != null && field == ItemUpdateField_Mdl.Field.PURCHASE_DISCOUNT_PERCENTAGE) {
                    int val = DmShared.iNull(grid.getGrid().getStore().get(context.getIndex()).getSaveToCompany()).intValue();
                    if (val == 1) {
                        sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                    } else if (val == 0) {
                        sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                    }
                }
            }
        });

        grid.initializeGrid(grid.getPropertyAccess().fieldHashCode(), 520, 160);
    }
    //</editor-fold>
}
