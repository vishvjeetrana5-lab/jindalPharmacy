package aarogya.master.servicePriceList.client;

import aarogya.master.servicePriceList.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
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
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ServicePriceList_Dsg extends IsWidgetExtended {

    public static String widgetName = new pmMaster().SERVICE_PRICELIST_MASTER.caption;
    public final static String priceListBlog = "Price List";
    public final static String percentBlog = "Organization %";
    public final static String savePriceBlog = "Save Price";
    public final static String deletePriceBlog = "Delete Price";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();

    final DmTextBox txtName = new DmTextBox(50, true);
    final DmTextBox txtShortName = new DmTextBox(10, true);
    final DmTextBox txtSearchValue = new DmTextBox(100, false);
    final DmTextBox txtApplicableDate = new DmTextBox(11, false);
    final FlowPanel flwFind = new FlowPanel();
    final FlowPanel flwOther = new FlowPanel();
    final FlowPanel flwPercent = new FlowPanel();
    final Button btnFind = new Button();
    final Button btnOther = new Button();
    final Button btnClose = new Button();
    final Button btnDiscountPercent = new Button();
    protected final DmToolContainer toolContainer = new DmToolContainer();
    final Button priceListBtn = new Button("Price List");
    final MenuItem mnuApplyDiscount = new MenuItem("Apply Discount");
    final MenuItem mnuImportApplicableRate = new MenuItem("Import PriceList");

    final DmTextBox txtServiceDiscount = new DmTextBox(50, false);
    final DmTextBox txtInvestigationDiscount = new DmTextBox(50, false);
    final DmTextBox txtFeesDiscount = new DmTextBox(50, false);
    final DmTextBox txtPharmacyDiscount = new DmTextBox(50, false);
    final DmTextBox txtPharmacyDiscountAllow = new DmTextBox(50, false);
    final DmTextBox txtMedicineSaleOn = new DmTextBox(50, false);
    final DmTextBox txtNature = new DmTextBox(50, false);
    final DmTextBox txtActive = new DmTextBox(50, false);

    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHReportFor">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHServiceNature = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniTxtHReportFor() {
        txtHServiceNature.getDmGrid().addTextColumn(txtHServiceNature.getDmGrid().getPropertyAccess().name(), "Name", 120, "Name");
        txtHServiceNature.getDmGrid().initializeGrid(txtHServiceNature.getDmGrid().getPropertyAccess().code(), 150, 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("ApplicableFrom")
        ValueProvider<Find_Mdl, String> applicableFrom();

        @Editor.Path("ServiceDiscountPer")
        ValueProvider<Find_Mdl, String> serviceDiscountPer();

        @Editor.Path("TestDiscountPer")
        ValueProvider<Find_Mdl, String> testDiscountPer();

        @Editor.Path("FeesDiscountPer")
        ValueProvider<Find_Mdl, String> feesDiscountPer();

        @Editor.Path("Insurance")
        ValueProvider<Find_Mdl, String> insurance();

        @Editor.Path("IsActive")
        ValueProvider<Find_Mdl, String> isActive();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 200, "NAME");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 110, "shortName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().applicableFrom(), "Applicable From", 100, "applicableFrom");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().serviceDiscountPer(), "Service Discount %", 110, "serviceDiscountPer");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().testDiscountPer(), "Test Discount %", 110, "testDiscountPer");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().feesDiscountPer(), "Fees Discount %", 110, "feesDiscountPer");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().insurance(), "Insurance", 150, "insurance");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isActive(), "Active", 50, "isActive");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);

        dmFind.getHelper().addSort("NAME", DmSortType.ASC);
        dmFind.getHelper().addSort("shortName", DmSortType.ASC);
    }
    //</editor-fold>

    public ServicePriceList_Dsg() {
        ini();
        iniGridServices();
        iniTxtHReportFor();
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
        DmClient.setStyle(flwTable, "99%", "519px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Applicable From");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApplicableDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, true);
        flwTd.setStyleName("dm-StripLabel");
        flwTd.getElement().getStyle().setLineHeight(25, Style.Unit.PX);
        flwTr.getElement().getStyle().setLineHeight(25, Style.Unit.PX);
        lbl = new DmLabel("Discount %");
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pharmacy Discount Allow");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtPharmacyDiscountAllow);
        txtPharmacyDiscountAllow.getElement().getStyle().setMarginTop(5, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Sale On");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtMedicineSaleOn);
        txtMedicineSaleOn.getElement().getStyle().setMarginTop(5, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtServiceDiscount);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Investigation");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtInvestigationDiscount);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Fees");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtFeesDiscount);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pharmacy ");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtPharmacyDiscount);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNature));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtActive));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "right", null, DmPosition.NONE, false);
        DmClient.setStyle(btnDiscountPercent, "125px", "20px", "right", null, DmPosition.NONE, false);
        btnDiscountPercent.setText("Organization's %");
        btnDiscountPercent.addStyleName("dm-button-grad");
        flwPercent.add(btnDiscountPercent);
        flwTd.add(flwPercent);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtSearchValue);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Type");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceNature));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(grdService));
        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuApplyDiscount);
        menu.add(mnuImportApplicableRate);
        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Service">
    interface GrdServices_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdServices_Mdl> {

        @Editor.Path("CategoryText")
        ValueProvider<GrdServices_Mdl, String> category();

        @Editor.Path("ServiceType")
        ValueProvider<GrdServices_Mdl, String> serviceType();

        @Editor.Path("OrganizationServiceName")
        ValueProvider<GrdServices_Mdl, String> organizationServiceName();

        @Editor.Path("Rate")
        ValueProvider<GrdServices_Mdl, String> rate();

        @Editor.Path("DiscountAmount")
        ValueProvider<GrdServices_Mdl, String> discountAmount();

        @Editor.Path("DiscountPercentage")
        ValueProvider<GrdServices_Mdl, String> discountPercentage();

        @Editor.Path("PrintSNo")
        ValueProvider<GrdServices_Mdl, String> printSNo();

    }
    final DmGridPaging<GrdServices_Mdl, GrdServices_PrpAcs, SearchValue_Mdl> grdService = new DmGridPaging(GWT.create(GrdServices_PrpAcs.class), 17);

    private void iniGridServices() {
        final int colRate, colDiscPer, colDisAmt;
        int index = 0;
        grdService.getDmGrid().addTextColumn(grdService.getDmGrid().getPropertyAccess().name(), "Service", 280, "name");
        index++;

        grdService.getDmGrid().addTextColumn(grdService.getDmGrid().getPropertyAccess().organizationServiceName(), "Org. Service Name", 280, "organizationServiceName");
        index++;
        grdService.getDmGrid().addTextColumn(grdService.getDmGrid().getPropertyAccess().category(), "Service Category", 280, "category");
        index++;
        colRate = index;
        grdService.getDmGrid().addTextColumn(grdService.getDmGrid().getPropertyAccess().rate(), "Rate", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        colDiscPer = index;
        grdService.getDmGrid().addTextColumn(grdService.getDmGrid().getPropertyAccess().discountPercentage(), "Discount %", 80, "discountPer").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        colDisAmt = index;
        grdService.getDmGrid().addTextColumn(grdService.getDmGrid().getPropertyAccess().discountAmount(), "Discount", 85, "discount").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        grdService.getDmGrid().addTextColumn(grdService.getDmGrid().getPropertyAccess().printSNo(), "Print S.No.", 95, "printSNo").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        grdService.getDmGrid().addTextColumn(grdService.getDmGrid().getPropertyAccess().serviceType(), "Service Type", 100, "serviceType");
        index++;
        grdService.getDmGrid().initializeGrid(grdService.getDmGrid().getPropertyAccess().fieldHashCode(), 1250, 386, 1);

        grdService.getHelper().addSort("name", DmSortType.ASC);
        grdService.getHelper().addSort("serviceType", DmSortType.ASC);
        grdService.getHelper().addSort("organizationServiceName", DmSortType.ASC);

        grdService.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdServices_Mdl>() {
            @Override
            public String getColStyle(GrdServices_Mdl model, ValueProvider<? super GrdServices_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (model.isColorChange()) {
                    if ((colIndex == colDisAmt) || (colIndex == colDiscPer) || (colIndex == colRate)) {
                        styleClass += "dm-GridCellDisable ";
                    }
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdServices_Mdl model, int rowIndex) {
                return "";
            }
        });
    }
    //</editor-fold>
}
