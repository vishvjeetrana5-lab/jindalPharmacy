package aarogya.selfToolkit.customFieldPopup.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class CustomFieldPopup_Dsg {

    public final static String widgetName = "Custom Fields";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    public CustomFieldPopup_Dsg() {
        ini();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");
        popupPanel.setHeight("380px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
        btnCancel.setTitle("Close");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "350px", null, null, DmPosition.CENTER, true);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "350px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "350px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdCustomField()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Custom Field">
    interface Grd_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmGrid<KeyFieldExtended, Grd_PrpAcs> gridCustomField = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid iniGrdCustomField() {
        DmColumnConfig<KeyFieldExtended, String> name;
        gridCustomField.addRowNumberColumn();
        name = gridCustomField.addTextColumn(gridCustomField.getPropertyAccess().name(), "Custom Field Name", 1, "");
        gridCustomField.initializeGrid(gridCustomField.getPropertyAccess().code(), 500, 350, 2);

        name.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word;'>" + DmShared.xNull(value) + "</div>");
            }
        });

        return gridCustomField.getGrid();
    }
    //</editor-fold>
}
