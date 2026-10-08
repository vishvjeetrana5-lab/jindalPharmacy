package aarogya.master.priceListMaster.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import aarogya.master.priceListMaster.shared.Find_Mdl;
import aarogya.master.priceListMaster.shared.GrdItemDetail_Mdl;
import aarogya.master.priceListMaster.shared.PriceList_Mdl;
import aarogya.master.priceListMaster.shared.SearchValue_Mdl;
import aarogya.master.self.shared.Master_Model;
import aarogya.projectMenu.self.shared.pmMaster;

abstract class PriceListMaster_Dsg extends IsWidgetExtended {

    public static String widgetName = new pmMaster().ITEM_PRICELIST_MASTER.caption;
    public final static String priceListBlog = "Price List";
    public final static String savePriceBlog = "Save Price";
    public final static String deletePriceBlog = "Delete Price";
    public final static String newRateListBlog = "Create New Price List";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();

    final DmTextBox txtPriceListType = new DmTextBox(10, true);
    final DmTextBox txtApplicableOn = new DmTextBox(10, true);
    final DmTextBox txtSearchValue = new DmTextBox(100, false);
    final DmTextBox txtApplicableDate = new DmTextBox(11, false);
    final FlowPanel flwFind = new FlowPanel();
    final FlowPanel flwOther = new FlowPanel();
    final Button btnFind = new Button();
    final Button btnOther = new Button();
    final Button btnClose = new Button();
    protected final DmToolContainer toolContainer = new DmToolContainer();
    final Button priceListBtn = new Button("Price List");
    final MenuItem mnuApplyDiscount = new MenuItem("Apply Discount");
    final MenuItem mnuImportApplicableRate = new MenuItem("Import PriceList");

    final DmLabel lblPriceList = new DmLabel("Price List");

    final Menu menu = new Menu();
    final PriceList_Mdl plmdl = new PriceList_Mdl();

    //<editor-fold defaultstate="collapsed" desc="txtHPriceList">
    interface RateList_PrpAcs extends KeyFieldHashCode_T_PrpAcs<PriceList_Mdl> {

        @Editor.Path("PriceListTypeName")
        ValueProvider<PriceList_Mdl, String> priceListTypeName();

    }
    final DmTextHelp<PriceList_Mdl, RateList_PrpAcs> txtHPriceList = new DmTextHelp(GWT.create(RateList_PrpAcs.class), 10);

    private void iniTxtHPriceList() {
        txtHPriceList.getDmGrid().addTextColumn(txtHPriceList.getDmGrid().getPropertyAccess().name(), "Name", 120, "Name");
        txtHPriceList.getDmGrid().addTextColumn(txtHPriceList.getDmGrid().getPropertyAccess().priceListTypeName(), "priceListTypeName", 70, "Name");
        txtHPriceList.getDmGrid().initializeGrid(txtHPriceList.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 1);
        txtHPriceList.getHelper().addSort("name", DmSortType.ASC);
        txtHPriceList.getHelper().addSort("priceListType", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("PriceListName")
        ValueProvider<Find_Mdl, String> priceListName();

        @Editor.Path("PriceListType")
        ValueProvider<Find_Mdl, String> priceListType();


    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 23);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().priceListName(), "Name", 200, "priceListName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().priceListType(), "Price List Type", 110, "priceListType");
//        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().applicableFrom(), "Applicable From", 100, "applicableFrom");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);

        dmFind.getHelper().addSort("priceListName", DmSortType.ASC);
        dmFind.getHelper().addSort("priceListTypeName", DmSortType.ASC);

    }
    //</editor-fold>

    public PriceListMaster_Dsg() {
        ini();
        iniGridServices();
        iniTxtHPriceList();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(iniToolContainer());
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        iniMenu();
        vrtInnerForm.add(flw);

        //Test Start
        userControl.addGap(50);
        priceListBtn.addStyleName("dm-button-grad");
        priceListBtn.setWidth("100px");
        userControl.addControlBlog(priceListBlog, priceListBtn);
        userControl.addGap(50);
        vrtInnerForm.add(userControl);
        //Test End

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContainer() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Find");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-search");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFind.addStyleName("dm-top-control-btnStyle");
        btnFind.setTitle("Find: (Ctrl + F)");
        btnFind.getElement().appendChild(aBtn.getElement());
        btnFind.getElement().appendChild(label.getElement());
        btnFind.setWidth("55px");
        flwFind.add(vl.add(btnFind));

        label = new InlineLabel("Other");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-share-alt");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOther.addStyleName("dm-top-control-btnStyle");
        btnOther.setTitle("Other: (Ctrl + O)");
        btnOther.getElement().appendChild(aBtn.getElement());
        btnOther.getElement().appendChild(label.getElement());
        btnOther.setWidth("60px");
        flwOther.add(btnOther);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("75px");

        toolContainer.addWidget(flwFind);
        toolContainer.addWidget(flwOther);
        toolContainer.addWidget(vl.add(btnClose));

        toolContainer.addGap();
        toolContainer.setTitle(widgetName);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "495px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lblPriceList.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        lblPriceList.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblPriceList.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblPriceList.getElement().getStyle().setColor("blue");
        flwTd.add(lblPriceList);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHPriceList);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, true);//margin true liya h
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("Rate List Type");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);//10 to 15
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPriceListType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);//before 15%
        lbl = new DmLabel("Applicable From");
        lbl.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApplicableDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);//70to 73
        flwTd.add(txtSearchValue);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//        if (Master_Model.PRICE_LIST_IS_APPLICABLE_ON_ACTIVE) {
            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
            lbl = new DmLabel("Applicable On");
            lbl.getElement().getStyle().setMarginLeft(13, Style.Unit.PX);
            flwTd.add(lbl);
            flwTr.add(flwTd);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
            flwTd.add(txtApplicableOn);
            txtApplicableOn.setEnabled(false);
            flwTr.add(flwTd);
            flwTable.add(flwTr);
//        }

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(grdItemDetail));
        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuApplyDiscount);
        menu.add(mnuImportApplicableRate);
        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Item Detail">
    interface GrdServices_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("ItemName")
        ValueProvider<GrdItemDetail_Mdl, String> itemName();

        @Editor.Path("UnitName")
        ValueProvider<GrdItemDetail_Mdl, String> unitName();

        @Editor.Path("ItemCatName")
        ValueProvider<GrdItemDetail_Mdl, String> itemCatName();

        @Editor.Path("Rate")
        ValueProvider<GrdItemDetail_Mdl, String> rate();

        @Editor.Path("DiscountAmount")
        ValueProvider<GrdItemDetail_Mdl, String> discountAmount();

        @Editor.Path("DiscountPercentage")
        ValueProvider<GrdItemDetail_Mdl, String> discountPercentage();

        @Editor.Path("PrintSNo")
        ValueProvider<GrdItemDetail_Mdl, String> printSNo();

    }
    final DmGridPaging<GrdItemDetail_Mdl, GrdServices_PrpAcs, SearchValue_Mdl> grdItemDetail = new DmGridPaging(GWT.create(GrdServices_PrpAcs.class), 19);

    private void iniGridServices() {
        final int colRate, colDiscPer, colDisAmt;
        int index = 0;
        grdItemDetail.getDmGrid().addTextColumn(grdItemDetail.getDmGrid().getPropertyAccess().itemName(), "Item Name", 260, "itemName");
        index++;
        grdItemDetail.getDmGrid().addTextColumn(grdItemDetail.getDmGrid().getPropertyAccess().unitName(), "Unit", 100, "unitName");
        index++;
        grdItemDetail.getDmGrid().addTextColumn(grdItemDetail.getDmGrid().getPropertyAccess().itemCatName(), "Item Category", 260, "itemCatName");
        index++;
        colRate = index;
        grdItemDetail.getDmGrid().addTextColumn(grdItemDetail.getDmGrid().getPropertyAccess().rate(), "Rate", 115, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        
        colDiscPer = index;
        grdItemDetail.getDmGrid().addTextColumn(grdItemDetail.getDmGrid().getPropertyAccess().discountPercentage(), "Discount %", 115, "discountPer").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        colDisAmt = index;
        grdItemDetail.getDmGrid().addTextColumn(grdItemDetail.getDmGrid().getPropertyAccess().discountAmount(), "Discount", 115, "discount").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        grdItemDetail.getDmGrid().addTextColumn(grdItemDetail.getDmGrid().getPropertyAccess().printSNo(), "Print S.No.", 115, "printSNo").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        grdItemDetail.getDmGrid().initializeGrid(grdItemDetail.getDmGrid().getPropertyAccess().fieldHashCode(), 1227, 400, 1);

        grdItemDetail.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdItemDetail_Mdl>() {
            @Override
            public String getColStyle(GrdItemDetail_Mdl model, ValueProvider<? super GrdItemDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (DmShared.xNull(model.getRecordType()).equalsIgnoreCase("1")) {
                    styleClass += "dm-GridCellDisable";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdItemDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        grdItemDetail.getHelper().addSort("itemName", DmSortType.ASC);
        grdItemDetail.getHelper().addSort("itemCatName", DmSortType.ASC);

    }
    //</editor-fold>
}
