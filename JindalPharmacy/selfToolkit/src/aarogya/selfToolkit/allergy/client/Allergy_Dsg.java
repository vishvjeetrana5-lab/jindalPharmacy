package aarogya.selfToolkit.allergy.client;

import aarogya.selfToolkit.allergy.shared.*;
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
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

abstract class Allergy_Dsg {

    public final static String widgetName = "Allergy";
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

    final DmTextBox txtSearchAllergy = new DmTextBox(50, false);
    final DmTextBox txtSearchTemplate = new DmTextBox(50, false);
    final DmLabel lblAllergy = new DmLabel("Allergy");

    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    protected int colArrowLeft, colArrowUp, colRemoveFav;

    //<editor-fold defaultstate="collapsed" desc="txtHAllergyNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAllergyNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, false);

    private void iniTxtHAllergyNature() {
        txtHAllergyNature.getDmGrid().addTextColumn(txtHAllergyNature.getDmGrid().getPropertyAccess().name(), "Nature", 0, "Name");
        txtHAllergyNature.getDmGrid().initializeGrid(txtHAllergyNature.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHAllergyNature.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAllergy">
    interface TxtHAllergy_PrpAcs extends KeyField_T_PrpAcs<TxtHAllergy_Mdl> {

        @Editor.Path("NatureName")
        ValueProvider<TxtHAllergy_Mdl, String> natureName();
    }
    final DmTextHelp<TxtHAllergy_Mdl, TxtHAllergy_PrpAcs> txtHAllergy = new DmTextHelp(GWT.create(TxtHAllergy_PrpAcs.class), 7, true);

    private void iniTxtHAllergy() {
        txtHAllergy.getDmGrid().addTextColumn(txtHAllergy.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHAllergy.getDmGrid().addTextColumn(txtHAllergy.getDmGrid().getPropertyAccess().natureName(), "Nature", 100, "natureName");
        txtHAllergy.getDmGrid().initializeGrid(txtHAllergy.getDmGrid().getPropertyAccess().code(), 600, 0, 1);

        txtHAllergy.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRemark">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRemark = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHRemark() {
        txtHRemark.setAppendable(true);
        txtHRemark.getDmGrid().addTextColumn(txtHRemark.getDmGrid().getPropertyAccess().name(), "Remark", 685, "remark");
        txtHRemark.getDmGrid().initializeGrid(txtHRemark.getDmGrid().getPropertyAccess().code(), 700, 0);

        txtHRemark.getHelper().addSort("remark", DmSortType.ASC);
    }
    //</editor-fold>

    public Allergy_Dsg() {
        ini();
        iniTxtHAllergy();
        iniTxtHRemark();
        iniTxtHAllergyNature();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1200px");
        popupPanel.setHeight("500px");
        iniGrdFavoriteAllergy();
        iniGrdFavoriteTemplate();
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
        btnOk.setWidth("40px");
        btnOk.setTitle("Ok");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
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

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "470px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        lblAllergy.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblAllergy.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblAllergy.getElement().getStyle().setColor("blue");
        flwTr.add(lblAllergy);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAllergyNature));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(0.5, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "81.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAllergy));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
        hrtPnl.add(vl.add(txtHRemark));
        hrtPnl.setCellWidth(txtHRemark, "100%");
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
        DmClient.setStyle(flwTr, "100%", "395px", "left", null, DmPosition.NONE, false);
        flwTr.add(iniGrdDetail());
        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "470px", null, null, DmPosition.NONE, true);

        tabPanel.add(allergyTabArea(), "Allergy");
        tabPanel.add(templateTabArea(), "Template");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel allergyTabArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "445px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtSearchAllergy));
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "425px", "left", null, DmPosition.NONE, true);
        flwTr.add(grdFavoriteAllergy);
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

    //<editor-fold defaultstate="collapsed" desc="Grd Favorite Medicine">
    interface GrdFavorite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdFavorite_Mdl> {

        @Editor.Path("NatureName")
        ValueProvider<GrdFavorite_Mdl, String> natureName();

        @Editor.Path("Image")
        ValueProvider<GrdFavorite_Mdl, String> image();
    }
    final DmGridPaging<GrdFavorite_Mdl, GrdFavorite_PrpAcs, SearchValue_Mdl> grdFavoriteAllergy = new DmGridPaging(GWT.create(GrdFavorite_PrpAcs.class), 18);

    private void iniGrdFavoriteAllergy() {
        int colIndex = 0;
        DmColumnConfig<GrdFavorite_Mdl, String> imageRemove;
        grdFavoriteAllergy.getDmGrid().addTextColumn(grdFavoriteAllergy.getDmGrid().getPropertyAccess().name(), "Favorites", 300, "name");
        colIndex++;
        grdFavoriteAllergy.getDmGrid().addTextColumn(grdFavoriteAllergy.getDmGrid().getPropertyAccess().natureName(), "Nature", 63, "natureName");
        colIndex++;
        colRemoveFav = colIndex;
        imageRemove = grdFavoriteAllergy.getDmGrid().addTextColumn(grdFavoriteAllergy.getDmGrid().getPropertyAccess().code(), "", 22, "");
        colIndex++;
        grdFavoriteAllergy.getDmGrid().initializeGrid(grdFavoriteAllergy.getDmGrid().getPropertyAccess().fieldHashCode(), 390, 0);

        imageRemove.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        imageRemove.setCellPadding(false);
        imageRemove.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-times' style='font-size:1.1em; color:red;'></span>");
            }
        });

        grdFavoriteAllergy.getHelper().addSort("name", DmSortType.ASC);
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

    //<editor-fold defaultstate="collapsed" desc="Grd Allergy">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAllergy_Mdl> {

        @Editor.Path("NatureName")
        ValueProvider<GrdAllergy_Mdl, String> natureName();

        @Editor.Path("Remark")
        ValueProvider<GrdAllergy_Mdl, String> remark();

        @Editor.Path("Image")
        ValueProvider<GrdAllergy_Mdl, String> image();
    }
    final DmGrid<GrdAllergy_Mdl, Grd_PrpAcs> grdDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid iniGrdDetail() {
        int colIndex = 0;
        DmColumnConfig<GrdAllergy_Mdl, String> imageArrowLeft, imageArrowUp, colAllergy;
        colArrowUp = colIndex;
        colIndex++;
        imageArrowUp = grdDetail.addTextColumn(grdDetail.getPropertyAccess().image(), "", 23, "");
        colIndex++;
        colAllergy = grdDetail.addTextColumn(grdDetail.getPropertyAccess().name(), "Allergy", 1, "");
        colIndex++;
        grdDetail.addTextColumn(grdDetail.getPropertyAccess().natureName(), "Nature", 75, "");
        colIndex++;
        grdDetail.addTextColumn(grdDetail.getPropertyAccess().remark(), "Remark", 225, "");
        colArrowLeft = colIndex;
        colIndex++;
        imageArrowLeft = grdDetail.addTextColumn(grdDetail.getPropertyAccess().code(), "", 23, "code");
        grdDetail.initializeGrid(grdDetail.getPropertyAccess().fieldHashCode(), 770, 395, 2);

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
        colAllergy.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word;'>" + DmShared.xNull(value) + "</div>");
            }
        });

        return grdDetail.getGrid();
    }
    //</editor-fold>

}
