package aarogya.selfToolkit.prescriptionCustomItem.client;

import aarogya.selfToolkit.prescriptionCustomItem.shared.*;
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
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

abstract class PrescriptionCustomItem_Dsg {

    public final static String widgetName = "Prescription Custom Item";
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

    final DmTextBox txtSearchFavriote = new DmTextBox(50, false);
    final DmTextBox txtSearchTemplate = new DmTextBox(50, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, false);
    final DmTextBoxNumeric txtSNo = new DmTextBoxNumeric(0, false, 2, false);
    final DmLabel lblName = new DmLabel("Name");
    final DmLabel lblTabCaption = new DmLabel("Name");

    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    protected int colArrowLeft, colArrowUp, colRemoveFav;

    //<editor-fold defaultstate="collapsed" desc="txtHGroupName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGroupName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHGroupName() {
        txtHGroupName.getDmGrid().addTextColumn(txtHGroupName.getDmGrid().getPropertyAccess().name(), "Group Name", 1, "name");
        txtHGroupName.getDmGrid().initializeGrid(txtHGroupName.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHGroupName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHName">
    interface TxtHName_PrpAcs extends KeyField_T_PrpAcs<TxtHName_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHName_Mdl, String> shortName();

        @Editor.Path("GroupName")
        ValueProvider<TxtHName_Mdl, String> groupName();
    }
    final DmTextHelp<TxtHName_Mdl, TxtHName_PrpAcs> txtHName = new DmTextHelp(GWT.create(TxtHName_PrpAcs.class), 7, false);

    private void iniTxtHName() {
        txtHName.getDmGrid().addTextColumn(txtHName.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHName.getDmGrid().addTextColumn(txtHName.getDmGrid().getPropertyAccess().shortName(), "Short Name", 70, "shortName");
        txtHName.getDmGrid().addTextColumn(txtHName.getDmGrid().getPropertyAccess().groupName(), "Group Name", 100, "groupName");
        txtHName.getDmGrid().initializeGrid(txtHName.getDmGrid().getPropertyAccess().code(), 600, 0, 1);
        txtHName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public PrescriptionCustomItem_Dsg() {
        ini();
        iniTxtHGroupName();
        iniTxtHName();
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

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "470px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        lblName.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblName.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblName.getElement().getStyle().setColor("blue");
        flwTr.add(lblName);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGroupName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(0.5, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "81.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "91%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("S. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "90%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
        hrtPnl.add(vl.add(txtSNo));
        hrtPnl.setCellWidth(txtSNo, "100%");
        DmClient.setStyle(flwAddToGrid, "100%", "20px", "left", null, DmPosition.NONE, false);
        btnAddToGrid.setWidth("20px");
        btnAddToGrid.setHeight("20px");
        btnAddToGrid.getElement().setAttribute("style", btnAddToGrid.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px; Color : green;");
        btnAddToGrid.addStyleName("fa fa-arrow-down");
        btnAddToGrid.setTitle("Add to Grid");
        flwAddToGrid.add(vl.add(btnAddToGrid));
        hrtPnl.add(vl.add(flwAddToGrid));
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "395px", "left", null, DmPosition.NONE, false);
        flwTr.add(createGrid());
        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "475px", null, null, DmPosition.NONE, true);

        tabPanel.add(favoriteTabArea(), lblTabCaption);
        tabPanel.add(templateTabArea(), "Template");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel favoriteTabArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "445px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtSearchFavriote));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "425px", "left", null, DmPosition.NONE, true);
        flwTr.add(grdFavorite);
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
        flwTr.add(vl.add(txtSearchTemplate));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "425px", "left", null, DmPosition.NONE, true);
        flwTr.add(grdFavoriteTemplate);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Favorite">
    interface GrdFavorite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdFavorite_Mdl> {

        @Editor.Path("GroupName")
        ValueProvider<GrdFavorite_Mdl, String> groupName();

        @Editor.Path("Image")
        ValueProvider<GrdFavorite_Mdl, String> image();
    }
    final DmGridPaging<GrdFavorite_Mdl, GrdFavorite_PrpAcs, SearchValue_Mdl> grdFavorite = new DmGridPaging(GWT.create(GrdFavorite_PrpAcs.class), 18);

    private void iniGrdFavorite() {
        int colIndex = 0;
        DmColumnConfig<GrdFavorite_Mdl, String> imageRemove;
        grdFavorite.getDmGrid().addTextColumn(grdFavorite.getDmGrid().getPropertyAccess().name(), "Favorites", 280, "name");
        colIndex++;
        grdFavorite.getDmGrid().addTextColumn(grdFavorite.getDmGrid().getPropertyAccess().groupName(), "Group", 83, "groupName");
        colIndex++;
        colRemoveFav = colIndex;
        imageRemove = grdFavorite.getDmGrid().addTextColumn(grdFavorite.getDmGrid().getPropertyAccess().code(), "", 22, "");
        grdFavorite.getDmGrid().initializeGrid(grdFavorite.getDmGrid().getPropertyAccess().fieldHashCode(), 390, 0);

        imageRemove.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        imageRemove.setCellPadding(false);
        imageRemove.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-times' style='font-size:1.1em; color:red;'></span>");
            }
        });

        grdFavorite.getHelper().addSort("groupName", DmSortType.ASC);
        grdFavorite.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Favorite Template">
    interface GrdFavoriteTemplate_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
    }
    final DmGridPaging<KeyFieldExtended, GrdFavoriteTemplate_PrpAcs, SearchValue_Mdl> grdFavoriteTemplate = new DmGridPaging(GWT.create(GrdFavoriteTemplate_PrpAcs.class), 18);

    private void iniGrdFavoriteTemplate() {
        DmColumnConfig<KeyFieldExtended, String> editTemplate;
        grdFavoriteTemplate.getDmGrid().addTextColumn(grdFavoriteTemplate.getDmGrid().getPropertyAccess().name(), "Favorite Template", 363, "name");
        editTemplate = grdFavoriteTemplate.getDmGrid().addTextColumn(grdFavoriteTemplate.getDmGrid().getPropertyAccess().code(), "", 22, "");
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

    //<editor-fold defaultstate="collapsed" desc="Grd Name">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPrescriptionCustomItem_Mdl> {

        @Editor.Path("GroupName")
        ValueProvider<GrdPrescriptionCustomItem_Mdl, String> groupName();

        @Editor.Path("Remark")
        ValueProvider<GrdPrescriptionCustomItem_Mdl, String> remark();

        @Editor.Path("Image")
        ValueProvider<GrdPrescriptionCustomItem_Mdl, String> image();
    }
    final DmGrid<GrdPrescriptionCustomItem_Mdl, Grd_PrpAcs> grid = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid createGrid() {
        int colIndex = 0;
        DmColumnConfig<GrdPrescriptionCustomItem_Mdl, String> imageArrowLeft, imageArrowUp, colName, colGroup;
        colIndex++;
        grid.addRowNumberColumn();

        colArrowUp = colIndex;
        colIndex++;
        imageArrowUp = grid.addTextColumn(grid.getPropertyAccess().image(), "", 23, "");
        colIndex++;
        colName = grid.addTextColumn(grid.getPropertyAccess().name(), "Name", 1, "");
        colIndex++;
        colGroup = grid.addTextColumn(grid.getPropertyAccess().groupName(), "Group", 150, "");
        colIndex++;
        grid.addTextColumn(grid.getPropertyAccess().remark(), "Remark", 150, "");
        colArrowLeft = colIndex;
        colIndex++;
        imageArrowLeft = grid.addTextColumn(grid.getPropertyAccess().code(), "", 23, "code");
        grid.initializeGrid(grid.getPropertyAccess().fieldHashCode(), 770, 395, 3);

        imageArrowLeft.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-star' style='font-size:1.1em; color:green;'></span>");
            }
        });
        imageArrowUp.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-arrow-up' style='font-size:1.1em; color:red;'></span>");
            }
        });
        colName.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word;'>" + DmShared.xNull(value) + "</div>");
            }
        });
        colGroup.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word;'>" + DmShared.xNull(value) + "</div>");
            }
        });

        return grid.getGrid();
    }
    //</editor-fold>

}
