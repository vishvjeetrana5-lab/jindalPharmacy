package aarogya.selfToolkit.investigationAdvise.client;

import aarogya.selfToolkit.investigationAdvise.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

abstract class InvestigationAdvise_Dsg {

    public final static String widgetName = "Investigation Advise";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwAddToGrid = new FlowPanel();
    final Button btnAddToGrid = new Button();
    final DmTextBox txtTemplateSearch = new DmTextBox(50, false);
    final DmTextBox txtTestSearch = new DmTextBox(50, false);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    protected int colArrowLeft, colArrowUp, colRemoveFav;

    //<editor-fold defaultstate="collapsed" desc="txtHInvestigation">
    interface TxtHInvestigation_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdInvestigation_Mdl> {

        @Editor.Path("CategoryName")
        ValueProvider<GrdInvestigation_Mdl, String> categoryName();

        @Editor.Path("Type")
        ValueProvider<GrdInvestigation_Mdl, String> type();
    }
    final DmTextHelp<GrdInvestigation_Mdl, TxtHInvestigation_PrpAcs> txtHInvestigation = new DmTextHelp(GWT.create(TxtHInvestigation_PrpAcs.class), 7, true);

    private void iniTxtHInvestigation() {
        txtHInvestigation.getDmGrid().addTextColumn(txtHInvestigation.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHInvestigation.getDmGrid().addTextColumn(txtHInvestigation.getDmGrid().getPropertyAccess().categoryName(), "Category", 165, "categoryName");
        txtHInvestigation.getDmGrid().addTextColumn(txtHInvestigation.getDmGrid().getPropertyAccess().type(), "Type", 35, "type");
        txtHInvestigation.getDmGrid().initializeGrid(txtHInvestigation.getDmGrid().getPropertyAccess().fieldHashCode(), 600, 0, 1);

        txtHInvestigation.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public InvestigationAdvise_Dsg() {
        ini();
        iniTxtHInvestigation();
        iniGrdFavorite();
        iniGrdFavoriteTemplate();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1200px");
        popupPanel.setHeight("500px");

        vrtInnerForm.add(iniToolContorls());
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

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("50px");
        btnOk.setTitle("Ok");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("60px");
        btnCancel.setTitle("Close");
        flwCancel.add(vl.add(btnCancel));

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "470px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "470px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", "470px", "left", null, DmPosition.NONE, false);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", "470px", "left", null, DmPosition.NONE, false);
        flwTd.add(tabArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "470px", null, null, DmPosition.NONE, true);

        tabPanel.add(investigationTabArea(), "Investigation");
        tabPanel.add(templateTabArea(), "Template");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel investigationTabArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "445px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtTestSearch));
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "425px", "left", null, DmPosition.NONE, true);
        flwTr.add(grdFavoriteTest);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel templateTabArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "445px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtTemplateSearch));
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "425px", "left", null, DmPosition.NONE, true);
        flwTr.add(grdFavoriteTemplate);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "470px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Investigation / Package / Panel");
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
        hrtPnl.add(vl.add(txtHInvestigation));
        hrtPnl.setCellWidth(txtHInvestigation, "100%");
        DmClient.setStyle(flwAddToGrid, "100%", "20px", "left", null, DmPosition.NONE, false);
        btnAddToGrid.setWidth("20px");
        btnAddToGrid.setHeight("20px");
        btnAddToGrid.getElement().setAttribute("style", btnAddToGrid.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px; Color : green;");
        btnAddToGrid.addStyleName("fa fa-arrow-down");
        btnAddToGrid.setTitle("Add to Complaint");
        flwAddToGrid.add(vl.add(btnAddToGrid));
        hrtPnl.add(vl.add(flwAddToGrid));
        flwTr.add(hrtPnl);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "430px", "left", null, DmPosition.NONE, true);
        flwTr.add(createGrid());
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Favorite">
    interface GrdFavorite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdFavorite_Mdl> {

        @Editor.Path("Image")
        ValueProvider<GrdFavorite_Mdl, String> image();
    }
    final DmGridPaging<GrdFavorite_Mdl, GrdFavorite_PrpAcs, SearchValue_Mdl> grdFavoriteTest = new DmGridPaging(GWT.create(GrdFavorite_PrpAcs.class), 18);

    private void iniGrdFavorite() {
        int colIndex = 0;
        DmColumnConfig<GrdFavorite_Mdl, String> imageRemove;
        grdFavoriteTest.getDmGrid().addTextColumn(grdFavoriteTest.getDmGrid().getPropertyAccess().name(), "Favorites", 363, "name");
        colIndex++;
        colRemoveFav = colIndex;
        imageRemove = grdFavoriteTest.getDmGrid().addTextColumn(grdFavoriteTest.getDmGrid().getPropertyAccess().code(), "", 22, "");
        colIndex++;
        grdFavoriteTest.getDmGrid().initializeGrid(grdFavoriteTest.getDmGrid().getPropertyAccess().fieldHashCode(), 390, 0);

        grdFavoriteTest.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdFavorite_Mdl>() {
            @Override
            public String getColStyle(GrdFavorite_Mdl model, ValueProvider<? super GrdFavorite_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (model.isPkg()) {
                    styleClass += "dm-FontBold ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdFavorite_Mdl model, int rowIndex) {
                return "";
            }
        });

        imageRemove.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        imageRemove.setCellPadding(false);
        imageRemove.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-times' style='font-size:1.1em; color:red;'></span>");
            }
        });

        grdFavoriteTest.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Favorite Template">
    interface GrdFavoriteTemplate_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
    }
    final DmGridPaging<KeyFieldExtended, GrdFavoriteTemplate_PrpAcs, SearchValue_Mdl> grdFavoriteTemplate = new DmGridPaging(GWT.create(GrdFavoriteTemplate_PrpAcs.class), 18);

    private void iniGrdFavoriteTemplate() {
        ColumnConfig<KeyFieldExtended, String> editTemplate;
        grdFavoriteTemplate.getDmGrid().addTextColumn(grdFavoriteTemplate.getDmGrid().getPropertyAccess().name(), "Favorite Template", 363, "name");
        editTemplate = grdFavoriteTemplate.getDmGrid().addTextColumn(grdFavoriteTemplate.getDmGrid().getPropertyAccess().code(), "", 22, "name");
        grdFavoriteTemplate.getDmGrid().initializeGrid(grdFavoriteTemplate.getDmGrid().getPropertyAccess().fieldHashCode(), 390, 0);

        editTemplate.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-pencil' style='font-size:1.1em;'></span>");
            }
        });

        grdFavoriteTemplate.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Investigation">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdInvestigation_Mdl> {

        @Editor.Path("CategoryName")
        ValueProvider<GrdInvestigation_Mdl, String> categoryName();

        @Editor.Path("Image")
        ValueProvider<GrdInvestigation_Mdl, String> image();
    }
    final DmGrid<GrdInvestigation_Mdl, Grd_PrpAcs> grdDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid createGrid() {
        int colIndex = 0;
        DmColumnConfig<GrdInvestigation_Mdl, String> imageArrowLeft, imageArrowUp, colInvestigation;
        colArrowUp = colIndex;
        colIndex++;
        imageArrowUp = grdDetail.addTextColumn(grdDetail.getPropertyAccess().image(), "", 23, "");
        colIndex++;
        colInvestigation = grdDetail.addTextColumn(grdDetail.getPropertyAccess().name(), "Investigation / Package / Panel", 1, "");
        colIndex++;
        grdDetail.addTextColumn(grdDetail.getPropertyAccess().categoryName(), "Category Name", 275, "");
        colArrowLeft = colIndex;
        colIndex++;
        imageArrowLeft = grdDetail.addTextColumn(grdDetail.getPropertyAccess().code(), "", 23, "code");
        grdDetail.initializeGrid(grdDetail.getPropertyAccess().fieldHashCode(), 770, 430, 2);

        imageArrowLeft.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-star' style='font-size:1em; color:green;'></span>");
            }
        });
        imageArrowUp.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-arrow-up' style='font-size:1em; color:red;'></span>");
            }
        });
        colInvestigation.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word;'>" + DmShared.xNull(value) + "</div>");
            }
        });

        grdDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdInvestigation_Mdl>() {
            @Override
            public String getColStyle(GrdInvestigation_Mdl model, ValueProvider<? super GrdInvestigation_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (model.isPkg()) {
                    styleClass += "dm-FontBold ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdInvestigation_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdDetail.getGrid();
    }
    //</editor-fold>

}
