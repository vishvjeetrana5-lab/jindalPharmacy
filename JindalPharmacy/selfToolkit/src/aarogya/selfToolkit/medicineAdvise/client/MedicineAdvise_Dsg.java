package aarogya.selfToolkit.medicineAdvise.client;

import aarogya.selfToolkit.medicineAdvise.shared.*;
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
import com.sencha.gxt.data.shared.ModelKeyProvider;
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
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

abstract class MedicineAdvise_Dsg {

    public final static String widgetName = "Medicine Advise";
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
    final DmTextBox txtDrugSearch = new DmTextBox(50, false);
    final DmLabel lblMedicine = new DmLabel("Medicine Name");
    final DmLabel lblFrequency = new DmLabel("Frequency");
    final DmLabel lblInstruction = new DmLabel("Instructions");
    final DmTextBox txtRemark = new DmTextBox(250, false);
    final DmTextBox txtDoseText = new DmTextBox(50, true);
    final DmTextBox txtComposition = new DmTextBox();
    final DmTextBoxNumeric txtSNo = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBoxNumeric txtDays = new DmTextBoxNumeric(0, false, 3, true);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    protected int colArrowLeft, colArrowUp, colRemoveFav;

    //<editor-fold defaultstate="collapsed" desc="txtHMedicine">
    interface TxtHMedicine_PrpAcs extends KeyField_T_PrpAcs<TxtHMedicine_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHMedicine_Mdl, String> manualCode();

        @Editor.Path("CompositionName")
        ValueProvider<TxtHMedicine_Mdl, String> compositionName();

        @Editor.Path("CategoryName")
        ValueProvider<TxtHMedicine_Mdl, String> categoryName();

        @Editor.Path("Stock")
        ValueProvider<TxtHMedicine_Mdl, String> stock();

        @Editor.Path("Unit")
        ValueProvider<TxtHMedicine_Mdl, String> unit();
    }
    final DmTextHelp<TxtHMedicine_Mdl, TxtHMedicine_PrpAcs> txtHMedicine = new DmTextHelp(GWT.create(TxtHMedicine_PrpAcs.class), 7, true);

    private void iniTxtHMedicine() {
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().name(), "Medicine Name", 1, "name");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().categoryName(), "Category", 70, "isNull(itemCategoryName,'')");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().compositionName(), "Composition", 230, "isNull(compositionName,'')");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().manualCode(), "Short Name", 70, "isNull(manualCode,'')");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().stock(), "Stock", 90, "stockQty", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().unit(), "SKU", 50, "unit");
        txtHMedicine.getDmGrid().initializeGrid(txtHMedicine.getDmGrid().getPropertyAccess().code(), 750, 0, 1);

        txtHMedicine.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHFrequency">
    interface TxtHFrequency_PrpAcs extends KeyField_T_PrpAcs<TxtHFrequency_Mdl> {

        @Editor.Path("Time")
        ValueProvider<TxtHFrequency_Mdl, String> time();

        @Editor.Path("Presentation")
        ValueProvider<TxtHFrequency_Mdl, String> presentation();
    }
    final DmTextHelp<TxtHFrequency_Mdl, TxtHFrequency_PrpAcs> txtHFrequency = new DmTextHelp(GWT.create(TxtHFrequency_PrpAcs.class), 7, true);

    private void iniTxtHFrequency() {
        txtHFrequency.getDmGrid().addTextColumn(txtHFrequency.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHFrequency.getDmGrid().addTextColumn(txtHFrequency.getDmGrid().getPropertyAccess().time(), "Time", 125, "frequencyTime");
        txtHFrequency.getDmGrid().addTextColumn(txtHFrequency.getDmGrid().getPropertyAccess().presentation(), "Presentation", 125, "presentation");
        txtHFrequency.getDmGrid().initializeGrid(txtHFrequency.getDmGrid().getPropertyAccess().code(), 600, 0, 1);

        txtHFrequency.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHInstruction">
    interface TxtHName_PrpAcs extends KeyField_T_PrpAcs<TxtHInstruction_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHInstruction_Mdl, String> shortName();

        @Editor.Path("GroupName")
        ValueProvider<TxtHInstruction_Mdl, String> groupName();
    }
    final DmTextHelp<TxtHInstruction_Mdl, TxtHName_PrpAcs> txtHInstruction = new DmTextHelp(GWT.create(TxtHName_PrpAcs.class), 7, false);

    private void iniTxtHInstruction() {
        txtHInstruction.getDmGrid().addTextColumn(txtHInstruction.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHInstruction.getDmGrid().addTextColumn(txtHInstruction.getDmGrid().getPropertyAccess().shortName(), "Short Name", 70, "shortName");
        txtHInstruction.getDmGrid().addTextColumn(txtHInstruction.getDmGrid().getPropertyAccess().groupName(), "Group Name", 100, "groupName");
        txtHInstruction.getDmGrid().initializeGrid(txtHInstruction.getDmGrid().getPropertyAccess().code(), 575, 0, 1);
        txtHInstruction.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDuration">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDuration = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniTxtHDuration() {
        txtHDuration.getDmGrid().addTextColumn(txtHDuration.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDuration.getDmGrid().initializeGrid(txtHDuration.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    public MedicineAdvise_Dsg() {
        ini();
        iniTxtHMedicine();
        iniTxtHFrequency();
        iniTxtHDuration();
        iniTxtHInstruction();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1200px");
        popupPanel.setHeight("500px");
        iniGrdFavoriteMedicine();
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
        btnOk.setWidth("50px");
        btnOk.setTitle("Ok");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        // label.setWidth("35px");
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

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "470px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        lblMedicine.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblMedicine.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblMedicine.getElement().getStyle().setColor("blue");
        flwTr.add(lblMedicine);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicine));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtComposition));
        txtComposition.getElement().setPropertyString("placeholder", "Composition");
        txtComposition.setEnabled(false);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "37%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(lblFrequency);
        lblFrequency.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblFrequency.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblFrequency.getElement().getStyle().setColor("blue");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Dose");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Duration");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, true);
        lblInstruction.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblInstruction.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblInstruction.getElement().getStyle().setColor("blue");
        flwTd.add(lblInstruction);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtHFrequency));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDoseText));
        txtDoseText.getElement().setAttribute("onpaste", "return false;");
        txtDoseText.getElement().setAttribute("ondrop", "return false;");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDays));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDuration));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtHInstruction));
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
        DmClient.setStyle(flwTr, "100%", "338px", "left", null, DmPosition.NONE, true);
        flwTr.add(createGrid());
        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "470px", null, null, DmPosition.NONE, true);

        tabPanel.add(medicineTabArea(), "Medicine");
        tabPanel.add(templateTabArea(), "Template");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel medicineTabArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "445px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtDrugSearch));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "425px", "left", null, DmPosition.NONE, true);
        flwTr.add(grdFavoriteMedicine);
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


        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "425px", "left", null, DmPosition.NONE, true);
        flwTr.add(grdFavoriteTemplate);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Favorite Medicine">
    interface GrdFavorite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdFavorite_Mdl> {

        @Editor.Path("DoseDisplayText")
        ValueProvider<GrdFavorite_Mdl, String> doseDisplayText();

        @Editor.Path("FrequencyName")
        ValueProvider<GrdFavorite_Mdl, String> frequencyName();
    }
    final DmGridPaging<GrdFavorite_Mdl, GrdFavorite_PrpAcs, SearchValue_Mdl> grdFavoriteMedicine = new DmGridPaging(GWT.create(GrdFavorite_PrpAcs.class), 18);

    private void iniGrdFavoriteMedicine() {
        int colIndex = 0;
        DmColumnConfig<GrdFavorite_Mdl, String> imageRemove;
        grdFavoriteMedicine.getDmGrid().addTextColumn(grdFavoriteMedicine.getDmGrid().getPropertyAccess().name(), "Favorite Medicine", 223, "name");
        colIndex++;
        grdFavoriteMedicine.getDmGrid().addTextColumn(grdFavoriteMedicine.getDmGrid().getPropertyAccess().frequencyName(), "Frequency", 80, "frequencyName");
        colIndex++;
        grdFavoriteMedicine.getDmGrid().addTextColumn(grdFavoriteMedicine.getDmGrid().getPropertyAccess().doseDisplayText(), "Dose", 60, "doseDisplay");
        colIndex++;
        colRemoveFav = colIndex;
        imageRemove = grdFavoriteMedicine.getDmGrid().addTextColumn(grdFavoriteMedicine.getDmGrid().getPropertyAccess().code(), "", 22, "");
        colIndex++;
        grdFavoriteMedicine.getDmGrid().initializeGrid(grdFavoriteMedicine.getDmGrid().getPropertyAccess().fieldHashCode(), 390, 0);

        imageRemove.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        imageRemove.setCellPadding(false);
        imageRemove.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-times' style='font-size:1.1em; color:red;'></span>");
            }
        });

        grdFavoriteMedicine.getHelper().addSort("name", DmSortType.ASC);
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

    //<editor-fold defaultstate="collapsed" desc="Grd Medicine">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicineDetail_Mdl> {

        @Editor.Path("SharedKeyFieldHashCode")
        ModelKeyProvider<GrdMedicineDetail_Mdl> fieldUniqueCode();

        @Editor.Path("DoseDisplayText")
        ValueProvider<GrdMedicineDetail_Mdl, String> doseDisplayText();

        @Editor.Path("FrequencyName")
        ValueProvider<GrdMedicineDetail_Mdl, String> frequencyName();

        @Editor.Path("Duration")
        ValueProvider<GrdMedicineDetail_Mdl, String> duration();

        @Editor.Path("Remark")
        ValueProvider<GrdMedicineDetail_Mdl, String> remark();

        @Editor.Path("InstructionName")
        ValueProvider<GrdMedicineDetail_Mdl, String> instruction();

        @Editor.Path("Image")
        ValueProvider<GrdMedicineDetail_Mdl, String> image();
    }
    final DmGrid<GrdMedicineDetail_Mdl, Grd_PrpAcs> grdDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid createGrid() {
        int colIndex = 0;
        DmColumnConfig<GrdMedicineDetail_Mdl, String> imageArrowLeft, imageArrowUp, medicineName, freqName, dose;
        colIndex++;
        grdDetail.addRowNumberColumn();
        colArrowUp = colIndex;
        colIndex++;
        imageArrowUp = grdDetail.addTextColumn(grdDetail.getPropertyAccess().image(), "", 23, "");
        colIndex++;
        medicineName = grdDetail.addTextColumn(grdDetail.getPropertyAccess().name(), "Medicine Name", 1, "");
        colIndex++;
        freqName = grdDetail.addTextColumn(grdDetail.getPropertyAccess().frequencyName(), "Frequency", 120, "");
        colIndex++;
        dose = grdDetail.addTextColumn(grdDetail.getPropertyAccess().doseDisplayText(), "Dose", 130, "");
        colIndex++;
        grdDetail.addTextColumn(grdDetail.getPropertyAccess().duration(), "Duration", 50, "");
        colIndex++;
        grdDetail.addTextColumn(grdDetail.getPropertyAccess().instruction(), "Instructions", 70, "");
        colIndex++;
        grdDetail.addTextColumn(grdDetail.getPropertyAccess().remark(), "Remark", 50, "");
        colArrowLeft = colIndex;
        colIndex++;
        imageArrowLeft = grdDetail.addTextColumn(grdDetail.getPropertyAccess().code(), "", 23, "code");
        grdDetail.initializeGrid(grdDetail.getPropertyAccess().fieldUniqueCode(), 770, 338, 3);

        imageArrowLeft.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class ='fa fa-star' style='font-size:1.1em; color:green;'></span>");
            }
        });
        imageArrowUp.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-arrow-up' style='font-size:1.1em; color:red;'></span>");
            }
        });
        medicineName.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word;'>" + DmShared.xNull(value) + "</div>");
            }
        });
        freqName.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word;'>" + DmShared.xNull(value) + "</div>");
            }
        });
        dose.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word; font-size : 14px; '>" + DmShared.xNull(value) + "</div>");
            }
        });

        return grdDetail.getGrid();
    }
    //</editor-fold>

}
