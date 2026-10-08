package aarogya.dashBoard.favorite.client;

import aarogya.dashBoard.favorite.shared.GrdModuleMenu_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;

abstract class Manage_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnLeft = new Button();
    final Button btnRight = new Button();
    final VerticalPanel btnPnl = new VerticalPanel();
    final InlineLabel setPositionMenu = new InlineLabel();
    final InlineLabel setPositionModule = new InlineLabel();

    public Manage_Dsg() {
        ini();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("890px");
        popupPanel.setHeight("400px");
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
        DmClient.setStyle(flwTable, "100%", "370px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "370px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", "370px", "left", null, DmPosition.NONE, false);
        flwTd.add(iniGrdAllModule());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "29%", "370px", "left", null, DmPosition.NONE, false);
        flwTd.add(iniGrdAllMenu());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", "370px", "left", null, DmPosition.NONE, false);
        flwTd.add(middleArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "29%", "370px", "left", null, DmPosition.NONE, false);
        flwTd.add(fMenuGrd());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", "370px", "left", null, DmPosition.NONE, false);
        flwTd.add(fModuleGrd());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel middleArea() {
        FlowPanel flwTable, flwTr;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "60px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-arrow-right");
        aBtn.getElement().getStyle().setColor("green");
        btnRight.addStyleName("dm-button-grad");
        btnRight.getElement().appendChild(aBtn.getElement());
        btnRight.setWidth("35px");
        btnRight.setHeight("25px");
        btnRight.setTitle("Right");
        flwTr.add(btnRight);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-arrow-left");
        aBtn.getElement().getStyle().setColor("green");
        btnLeft.addStyleName("dm-button-grad");
        btnLeft.getElement().appendChild(aBtn.getElement());
        btnLeft.setWidth("35px");
        btnLeft.setHeight("25px");
        btnLeft.setTitle("Left");
        flwTr.add(btnLeft);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel fMenuGrd() {
        FlowPanel flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdFavouriteMenu()));
        flwTd.add(setPositionMenu);
        setPositionMenu.addStyleName("fa fa-sliders");
        setPositionMenu.getElement().setAttribute("style", "position:absolute; top:5px; left:480px; z-index:10;cursor:pointer;color:blue;");
        setPositionMenu.setTitle("Select Position");

        return flwTd;
    }

    private FlowPanel fModuleGrd() {
        FlowPanel flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdFavouriteModule()));
        flwTd.add(setPositionModule);
        setPositionModule.addStyleName("fa fa-sliders");
        setPositionModule.getElement().setAttribute("style", "position:absolute; top:5px; left:737px; z-index:10;cursor:pointer;color:blue;");
        setPositionModule.setTitle("Select Position");

        return flwTd;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd All Module">
    interface GrdAllModule_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdModuleMenu_Mdl> {

        @Editor.Path("MenuShortHand")
        ValueProvider<GrdModuleMenu_Mdl, String> menuCode();
    }
    final DmGrid<GrdModuleMenu_Mdl, GrdAllModule_PrpAcs> grdAllModule = new DmGrid(GWT.create(GrdAllModule_PrpAcs.class));

    private Grid iniGrdAllModule() {
        grdAllModule.addRowNumberColumn();
        grdAllModule.addTextColumn(grdAllModule.getPropertyAccess().name(), "Module", 1, "module");
        grdAllModule.initializeGrid(grdAllModule.getPropertyAccess().fieldHashCode(), 159, 370, 2);

        return grdAllModule.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Favorite Module">
    interface GrdFavouriteModule_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdModuleMenu_Mdl> {

    }

    final DmGrid<GrdModuleMenu_Mdl, GrdFavouriteModule_PrpAcs> grdFavouriteModule = new DmGrid(GWT.create(GrdFavouriteModule_PrpAcs.class));

    private Grid iniGrdFavouriteModule() {
        grdFavouriteModule.addRowNumberColumn();
        grdFavouriteModule.addTextColumn(grdFavouriteModule.getPropertyAccess().name(), "Module", 1, "module");
        grdFavouriteModule.initializeGrid(grdFavouriteModule.getPropertyAccess().fieldHashCode(), 159, 370, 2);

        return grdFavouriteModule.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd All Menu">
    interface GrdAllMenu_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdModuleMenu_Mdl> {

        @Editor.Path("MenuShortHand")
        ValueProvider<GrdModuleMenu_Mdl, String> menuCode();
    }

    final DmGrid<GrdModuleMenu_Mdl, GrdAllMenu_PrpAcs> grdAllMenu = new DmGrid(GWT.create(GrdAllMenu_PrpAcs.class));

    private Grid iniGrdAllMenu() {
        grdAllMenu.addRowNumberColumn();
        grdAllMenu.addTextColumn(grdAllMenu.getPropertyAccess().name(), "...", 180, "");
        grdAllMenu.addTextColumn(grdAllMenu.getPropertyAccess().menuCode(), "Menu Code", 80, "");
        grdAllMenu.initializeGrid(grdAllMenu.getPropertyAccess().fieldHashCode(), 255, 370, 2);

        return grdAllMenu.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Favourite Menu">
    interface GrdFavouriteMenu_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdModuleMenu_Mdl> {

        @Editor.Path("MenuShortHand")
        ValueProvider<GrdModuleMenu_Mdl, String> menuCode();
    }

    final DmGrid<GrdModuleMenu_Mdl, GrdFavouriteMenu_PrpAcs> grdFavouriteMenu = new DmGrid(GWT.create(GrdFavouriteMenu_PrpAcs.class));

    private Grid iniGrdFavouriteMenu() {
        grdFavouriteMenu.addRowNumberColumn();
        grdFavouriteMenu.addTextColumn(grdFavouriteMenu.getPropertyAccess().name(), "...", 180, "");
        grdFavouriteMenu.addTextColumn(grdFavouriteMenu.getPropertyAccess().menuCode(), "Menu Code", 80, "");
        grdFavouriteMenu.initializeGrid(grdFavouriteMenu.getPropertyAccess().fieldHashCode(), 257, 370, 2);

        return grdFavouriteMenu.getGrid();
    }
    //</editor-fold>
}
