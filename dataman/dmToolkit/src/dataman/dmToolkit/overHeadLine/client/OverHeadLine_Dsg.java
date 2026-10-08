package dataman.dmToolkit.overHeadLine.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;

abstract class OverHeadLine_Dsg {

    VerticalPanel vrtForm = new VerticalPanel();
    final HorizontalPanel hrtBanner = new HorizontalPanel();
    final Button btnMenu = new Button();
    final InlineLabel lblBannerCaption = new InlineLabel();
    final InlineLabel lblBannerAmount = new InlineLabel();
    final Menu menu = new Menu();
    MenuItem refreshOverHeadLine;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    public OverHeadLine_Dsg(int width, int height) {
        DmClient.setStyle(vrtForm, width + "px", height + "px", "left", null, DmPosition.NONE, true);
        vrtForm.add(createBody(width, height));

        vrtForm.add(createFooter());
    }

    private HorizontalPanel createFooter() {
        Anchor aBtn;

        hrtBanner.addStyleName("dm-StripLabel");
        hrtBanner.setHeight("18px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-bars");
        aBtn.getElement().getStyle().setColor("brown");
        btnMenu.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;float:left;padding-top: 0px;");
        btnMenu.getElement().appendChild(aBtn.getElement());
        hrtBanner.add(btnMenu);
        lblBannerCaption.getElement().setAttribute("style", "float:left;margin-top: 3px;");
        hrtBanner.add(lblBannerCaption);
        hrtBanner.setCellWidth(lblBannerCaption, "65%");
        lblBannerAmount.getElement().setAttribute("style", "float:right;margin-top: 3px;margin-right: 10px;");
        hrtBanner.add(lblBannerAmount);
        hrtBanner.setCellWidth(lblBannerAmount, "35%");
        createMenu();
        return hrtBanner;
    }

    private Menu createMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        refreshOverHeadLine = new MenuItem("Refresh", selectionHandler);
        menu.add(refreshOverHeadLine);

        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="OverHead Grid">
    interface OverHead_PrpAcs extends KeyField_T_PrpAcs<OverHeadLine_Mdl> {

        @Editor.Path("OverHeadAlias")
        ValueProvider<OverHeadLine_Mdl, String> overHeadAlias();

        @Editor.Path("AtRate")
        ValueProvider<OverHeadLine_Mdl, String> atRate();

        @Editor.Path("Amount")
        ValueProvider<OverHeadLine_Mdl, String> amount();
    }

    final DmGrid<OverHeadLine_Mdl, OverHead_PrpAcs> grid = new DmGrid(GWT.create(OverHead_PrpAcs.class));

    private Grid createBody(int width, int height) {
        grid.addTextColumn(grid.getPropertyAccess().overHeadAlias(), "Particulars", 0, "");
        grid.addTextColumn(grid.getPropertyAccess().atRate(), "At Rate", 63, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grid.addTextColumn(grid.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grid.initializeGrid(grid.getPropertyAccess().code(), width, height, 1);

        grid.getGrid().getView().setViewConfig(new GridViewConfig<OverHeadLine_Mdl>() {

            @Override
            public String getColStyle(OverHeadLine_Mdl model, ValueProvider<? super OverHeadLine_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";

                if (colIndex == 0) {
                    styleClass += "dm-GridCellDisable ";
                } else if (colIndex == 1) {

                    if (DmShared.vNull(model.getAtRateEditable()) == 0) {
                        styleClass += "dm-GridCellDisable ";
                    } else if (DmShared.vNull(model.getAtRateMandatory()) == 1) {
                        styleClass += "dm-GridCellMandatory ";
                    }

                    if (DmShared.vNull(model.getAtRate()) < 0) {
                        styleClass += "dm-GridCellNegative ";
                    }

                } else if (colIndex == 2) {
                    if (DmShared.vNull(model.getAmountEditable()) == 0) {
                        styleClass += "dm-GridCellDisable ";
                    } else if (DmShared.vNull(model.getAmountMandatory()) == 1) {
                        styleClass += "dm-GridCellMandatory ";
                    }

                    if (DmShared.vNull(model.getAmount()) < 0) {
                        styleClass += "dm-GridCellNegative ";
                    }
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(OverHeadLine_Mdl model, int rowIndex) {
                return "";
            }
        });
        return grid.getGrid();
    }
    //</editor-fold>
}
