package dataman.dmToolkit.tdsCategoryPopup.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.tdsCategoryPopup.shared.TdsCategoryGrid_Mdl;
import dataman.dmToolkit.tdsCategoryPopup.shared.TxtHTdsDeduct_Mdl;

public class TdsCategoryPopup_Dsg {

    public final static String widgetName = "TDS";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    final DmLabel lblClosingBalance = new DmLabel("");
    final DmLabel lblYTDCR = new DmLabel("");
    final DmLabel lblYTDDR = new DmLabel("");
    final DmLabel lblInitialTurnover = new DmLabel("");
    final DmLabel lblYTDTDSonAmt = new DmLabel("");
    final DmLabel lblYTDTurnover = new DmLabel("");

    final DmTextBoxNumeric txtTdsAmountProposed = new DmTextBoxNumeric(2, false, 15, false);
    final DmTextBoxNumeric txtTdsOnAmount = new DmTextBoxNumeric(2, false, 15, true);
    final DmLabel lblTotalTds = new DmLabel("");

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHTdsCategory">
    interface TxtHTdsCategory extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();

    }
    final DmTextHelp<KeyFieldExtended, TxtHTdsCategory> txtHTdsCategory = new DmTextHelp(GWT.create(TxtHTdsCategory.class), 7, true);

    public void iniTxtHTdsCategory() {
        txtHTdsCategory.getDmGrid().addTextColumn(txtHTdsCategory.getDmGrid().getPropertyAccess().name(), "Name", 250, "Name");
        txtHTdsCategory.getDmGrid().addTextColumn(txtHTdsCategory.getDmGrid().getPropertyAccess().sharedModifiedBy(), "Tds For", 100, "tdsFor");
        txtHTdsCategory.getDmGrid().initializeGrid(txtHTdsCategory.getDmGrid().getPropertyAccess().code(), 400, 200, 1);
        txtHTdsCategory.getDmGrid().getGrid().setHeight(200);
        txtHTdsCategory.getHelper().addSort("Name", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHTdsDeduct">
    interface TxtHTdsDeduct extends KeyField_T_PrpAcs<TxtHTdsDeduct_Mdl> {

        @Editor.Path("City")
        ValueProvider<TxtHTdsDeduct_Mdl, String> City();

        @Editor.Path("SCode")
        ValueProvider<TxtHTdsDeduct_Mdl, String> SCode();

    }
    final DmTextHelp<TxtHTdsDeduct_Mdl, TxtHTdsDeduct> txtHTdsDeduct = new DmTextHelp(GWT.create(TxtHTdsDeduct.class), 7, true);

    public void iniTxtHTdsDeduct() {
        txtHTdsDeduct.getDmGrid().addTextColumn(txtHTdsDeduct.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHTdsDeduct.getDmGrid().addTextColumn(txtHTdsDeduct.getDmGrid().getPropertyAccess().City(), "City", 100, "CityName");
        txtHTdsDeduct.getDmGrid().addTextColumn(txtHTdsDeduct.getDmGrid().getPropertyAccess().SCode(), "Code", 100, "ManualCode");
        txtHTdsDeduct.getDmGrid().initializeGrid(txtHTdsDeduct.getDmGrid().getPropertyAccess().code(), 400, 200, 1);
        txtHTdsDeduct.getDmGrid().getGrid().setHeight(200);
        txtHTdsDeduct.getHelper().addSort("Name", DmSortType.ASC);
        txtHTdsDeduct.getHelper().addSort("CityName", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTdsFor">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTdsFor = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    public void iniTxtHTdsFor() {
        txtHTdsFor.getDmGrid().addTextColumn(txtHTdsFor.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHTdsFor.getDmGrid().initializeGrid(txtHTdsFor.getDmGrid().getPropertyAccess().code(), 150, 200, 1);
        txtHTdsFor.getDmGrid().getGrid().setHeight(150);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TdsCategoryGrid">
    interface TdsCategoryGrid extends KeyFieldHashCode_T_PrpAcs<TdsCategoryGrid_Mdl> {

        @Editor.Path("Description")
        ValueProvider<TdsCategoryGrid_Mdl, String> description();

        @Editor.Path("PostingAccount")
        ValueProvider<TdsCategoryGrid_Mdl, String> postingAccount();

        @Editor.Path("Percent")
        ValueProvider<TdsCategoryGrid_Mdl, String> percent();

        @Editor.Path("Amount")
        ValueProvider<TdsCategoryGrid_Mdl, String> amount();
    }

    final DmGrid<TdsCategoryGrid_Mdl, TdsCategoryGrid> tdsCategoryGrid = new DmGrid(GWT.create(TdsCategoryGrid.class));

    public Grid iniTdsCategoryGrid() {
        tdsCategoryGrid.addRowNumberColumn();
        tdsCategoryGrid.addTextColumn(tdsCategoryGrid.getPropertyAccess().description(), "Description", 1, null);
        tdsCategoryGrid.addTextColumn(tdsCategoryGrid.getPropertyAccess().postingAccount(), "Posting A/c", 200, null);
        tdsCategoryGrid.addTextColumn(tdsCategoryGrid.getPropertyAccess().percent(), "(%)", 50, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        tdsCategoryGrid.addTextColumn(tdsCategoryGrid.getPropertyAccess().amount(), "Amount", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        tdsCategoryGrid.initializeGrid(tdsCategoryGrid.getPropertyAccess().fieldHashCode(), 755, 250, 2);
        tdsCategoryGrid.getGrid().setHeight(200);
        return tdsCategoryGrid.getGrid();
    }
//</editor-fold>

    public TdsCategoryPopup_Dsg() {
        popupPanel.setWidth("770px");
        ini();
    }

    private void ini() {
        initialize();
        iniToolContorls();
        iniTxtHTdsDeduct();
        iniTxtHTdsCategory();
        iniTxtHTdsFor();

    }

    private void initialize() {

        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("770px");
        popupPanel.setHeight("350px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(fCreateTdsCategoryDetails());
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
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(vl.add(btnCancel));

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);

        return toolContainer;
    }

    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "40px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS Category");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTdsCategory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS Deduct Form");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTdsDeduct));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel topRightArea() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Amount");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtTdsAmountProposed));
        flwTd.add((txtTdsAmountProposed));
        txtTdsAmountProposed.setReadOnly(true);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS On Amount");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtTdsOnAmount));
        flwTd.add((txtTdsOnAmount));
//        txtTdsOnAmount.setReadOnly(true);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;

    }

    private FlowPanel iniTdsInfo() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "32px", null, null, DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Closing Balance :");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add((lblClosingBalance));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp = new DmLabel("Led. YTD Cr. :");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add((lblYTDCR));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp = new DmLabel("Led. YTD Dr. :");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add((lblYTDDR));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        lblTemp.getElement().getStyle().setMarginBottom(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS on Amount :");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add((lblYTDTDSonAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp = new DmLabel("Turnover :");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add((lblYTDTurnover));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp = new DmLabel("Initial Turnover:");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add((lblInitialTurnover));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;

    }

    private FlowPanel fCreateTdsCategoryDetails() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "310px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "40px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "40px", "left", null, DmPosition.NONE, false);
        flwTd.add(topLeftArea());
        flwTr.add(flwTd);
        /*====================================< ******* >====================================*/

        /*====================================< 2nd Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "40px", "left", null, DmPosition.NONE, false);
        flwTd.add(topRightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(iniTdsInfo());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        /*====================================< 4 Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(iniTdsCategoryGrid());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        /*====================================< 5 Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Total TDS");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(lblTotalTds));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS For");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTdsFor));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }
}
