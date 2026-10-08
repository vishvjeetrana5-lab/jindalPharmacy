package aarogya.dashBoard.favorite.client;

import aarogya.dashBoard.favorite.shared.GrdModuleMenu_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;

abstract class SetPosition_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final Button btnDown = new Button();
    final Button btnUp = new Button();
    final VerticalPanel btnPnl = new VerticalPanel();

    public SetPosition_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("400px");
        popupPanel.setHeight("300px");
        iniToolControls();
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolControls() {
        InlineLabel label;
        Anchor aBtn;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("40px");
        btnOk.setTitle("Ok");
        flwOk.add(btnOk);

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
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "270px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "270px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", "270px", "left", null, DmPosition.NONE, false);
        flwTd.add(iniGrdAllModule());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", "270px", "left", null, DmPosition.NONE, false);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "60px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-arrow-up");
        aBtn.getElement().getStyle().setColor("green");
        btnUp.addStyleName("dm-button-grad");
        btnUp.getElement().appendChild(aBtn.getElement());
        btnUp.setWidth("35px");
        btnUp.setHeight("25px");
        btnUp.setTitle("Up");
        flwTr.add(btnUp);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-arrow-down");
        aBtn.getElement().getStyle().setColor("green");
        btnDown.addStyleName("dm-button-grad");
        btnDown.getElement().appendChild(aBtn.getElement());
        btnDown.setWidth("35px");
        btnDown.setHeight("25px");
        btnDown.setTitle("Down");
        flwTr.add(btnDown);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Menu">
    interface GrdAllModule_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdModuleMenu_Mdl> {

    }
    final DmGrid<GrdModuleMenu_Mdl, GrdAllModule_PrpAcs> grdMenu = new DmGrid(GWT.create(GrdAllModule_PrpAcs.class));

    private Grid iniGrdAllModule() {
        grdMenu.addRowNumberColumn();
        grdMenu.addTextColumn(grdMenu.getPropertyAccess().name(), "Module", 1, "");
        grdMenu.initializeGrid(grdMenu.getPropertyAccess().fieldHashCode(), 340, 270, 2);

        return grdMenu.getGrid();
    }
    //</editor-fold>

}
