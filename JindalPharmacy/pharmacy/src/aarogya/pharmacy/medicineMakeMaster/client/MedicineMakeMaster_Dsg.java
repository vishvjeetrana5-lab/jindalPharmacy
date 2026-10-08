package aarogya.pharmacy.medicineMakeMaster.client;

import aarogya.pharmacy.medicineMakeMaster.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class MedicineMakeMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().MEDICINE_MAKE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmSuggestBox txtCompanyName = new DmSuggestBox(50, 10, true, "companyName", DmDataType.STRING);
    final DmSuggestBox txtShortName = new DmSuggestBox(20, 10, true, "shortName", DmDataType.STRING);
    final DmTextBox txtLclPerson = new DmTextBox(35, false);
    final DmTextBox txtLclAddress = new DmTextBox(100, false);
    final DmTextBox txtLclAddress1 = new DmTextBox(100, false);
    final DmTextBox txtLclMobile = new DmTextBox(30, false);
    final DmTextBox txtLclFax = new DmTextBox(30, false);
    final DmTextBox txtLclPhoneR = new DmTextBox(30, false);
    final DmTextBox txtLclPhoneO = new DmTextBox(30, false);
    final DmTextBox txtLclEmail = new DmTextBox(100, false);
    final DmTextBox txtRegPerson = new DmTextBox(35, false);
    final DmTextBox txtRegAddress = new DmTextBox(100, false);
    final DmTextBox txtRegAddress1 = new DmTextBox(100, false);
    final DmTextBox txtRegMobile = new DmTextBox(30, false);
    final DmTextBox txtRegFax = new DmTextBox(30, false);
    final DmTextBox txtRegPhoneR = new DmTextBox(30, false);
    final DmTextBox txtRegPhoneO = new DmTextBox(30, false);
    final DmTextBox txtRegEmail = new DmTextBox(100, false);
    final DmTextBox txtNatPerson = new DmTextBox(35, false);
    final DmTextBox txtNatAddress = new DmTextBox(100, false);
    final DmTextBox txtNatAddress1 = new DmTextBox(100, false);
    final DmTextBox txtNatMobile = new DmTextBox(30, false);
    final DmTextBox txtNatFax = new DmTextBox(30, false);
    final DmTextBox txtNatPhoneR = new DmTextBox(30, false);
    final DmTextBox txtNatPhoneO = new DmTextBox(30, false);
    final DmTextBox txtNatEmail = new DmTextBox(100, false);

    final DmTextBoxNumeric txtPurchaseDisPer = new DmTextBoxNumeric(2, false, 4, false);
    Style stl;

    final MenuItem mnuExportCard = new MenuItem("Export Data");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("PurchaseDiscount")
        ValueProvider<Find_Mdl, String> purchaseDiscount();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 18);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Medicine Company", 0, "companyName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().purchaseDiscount(), "Purchase Disc. %", 100, "purchaseDiscountPer", DmDataType.DOUBLE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);

        dmFind.getHelper().addSort("companyName", DmSortType.ASC);
        dmFind.getHelper().addSort("shortName", DmSortType.ASC);
    }

    //</editor-fold>
    public MedicineMakeMaster_Dsg() {
        ini();
        iniMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        CaptionPanel cPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "90%", "380px", null, null, DmPosition.CENTER, true);

        //Company & Short Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Company");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCompanyName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(4, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase Disc %");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPurchaseDisPer));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //Comapany Detail Rows Heading
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        stl = flwTr.getElement().getStyle();
        stl.setTextAlign(Style.TextAlign.CENTER);
        stl.setFontWeight(Style.FontWeight.BOLD);
        stl.setMarginTop(1, Style.Unit.PCT);
        flwTr.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Medicine Company Detail");
        lbl.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        lbl.setHeight("20px");
        flwTr.add(lbl);
        flwTable.add(flwTr);

        //Local Detail
        cPnl = new CaptionPanel("Local");
        DmClient.setStyle(cPnl, "98%", null, "left", null, DmPosition.NONE, true);
        cPnl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        cPnl.add(localPanel());
        flwTable.add(cPnl);

        //Local Detail
        cPnl = new CaptionPanel("Regional");
        DmClient.setStyle(cPnl, "98%", null, "left", null, DmPosition.NONE, true);
        cPnl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        cPnl.add(regionalPanel());
        flwTable.add(cPnl);

        //Local Detail
        cPnl = new CaptionPanel("National");
        DmClient.setStyle(cPnl, "98%", null, "left", null, DmPosition.NONE, true);
        cPnl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        cPnl.add(nationalPanel());
        flwTable.add(cPnl);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Local">
    private FlowPanel localPanel() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "100%", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "60px", "left", null, DmPosition.NONE, true);
        flwTd.add(lclLeftSection());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "60px", "left", null, DmPosition.NONE, true);
        flwTd.add(lclRightSection());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel lclLeftSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "65px", null, null, DmPosition.CENTER, true);

        //Contact Person
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Contact Person");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLclPerson));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Address1
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLclAddress));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Address2
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTd.add(vl.add(txtLclAddress1));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel lclRightSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "100%", null, null, DmPosition.CENTER, true);

        //Mobile & Fax
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLclMobile));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Fax");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLclFax));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Phone
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Phone (O)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLclPhoneO));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Phone (R)");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLclPhoneR));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Email
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLclEmail));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Regional">
    private FlowPanel regionalPanel() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "100%", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "60px", "left", null, DmPosition.NONE, true);
        flwTd.add(regLeftSection());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "60px", "left", null, DmPosition.NONE, true);
        flwTd.add(regRightSection());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel regLeftSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "65px", null, null, DmPosition.CENTER, true);

        //Contact Person
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Contact Person");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegPerson));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Address1
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegAddress));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Address2
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTd.add(vl.add(txtRegAddress1));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel regRightSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "100%", null, null, DmPosition.CENTER, true);

        //Mobile & Fax
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegMobile));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Fax");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegFax));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Phone
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Phone (O)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegPhoneO));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Phone (R)");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegPhoneR));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Email
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegEmail));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="National">
    private FlowPanel nationalPanel() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "100%", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "60px", "left", null, DmPosition.NONE, true);
        flwTd.add(natLeftSection());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "60px", "left", null, DmPosition.NONE, true);
        flwTd.add(natRightSection());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel natLeftSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "65px", null, null, DmPosition.CENTER, true);

        //Contact Person
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Contact Person");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNatPerson));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Address1
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNatAddress));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Address2
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTd.add(vl.add(txtNatAddress1));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel natRightSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "100%", null, null, DmPosition.CENTER, true);

        //Mobile & Fax
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNatMobile));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Fax");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNatFax));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Phone
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Phone (O)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNatPhoneO));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Phone (R)");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNatPhoneR));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Email
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNatEmail));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
    //</editor-fold>

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }

}
