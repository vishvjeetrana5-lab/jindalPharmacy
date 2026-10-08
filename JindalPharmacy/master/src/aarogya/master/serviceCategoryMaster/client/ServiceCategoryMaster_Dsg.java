package aarogya.master.serviceCategoryMaster.client;

import aarogya.master.serviceCategoryMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmControlBlog;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ServiceCategoryMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().SERVICE_CATEGORY_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    DmControlBlog addServiceApplTo;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final Button btnServiceApplicableTo = new Button();

    final DmSuggestBox txtCategoryName = new DmSuggestBox(50, 10, true, "Name", DmDataType.STRING);
    final DmTextBoxNumeric txtPrintSerialNo = new DmTextBoxNumeric(2, false, 4, false);

    final MenuItem mnuExportCard = new MenuItem("Export Category");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHITSOnSale">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnSale = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHITSOnSale() {
        txtHITSOnSale.getDmGrid().addTextColumn(txtHITSOnSale.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnSale.getDmGrid().initializeGrid(txtHITSOnSale.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnSale.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPurchaseAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHPurchaseAC() {
        txtHServiceAC.getDmGrid().addTextColumn(txtHServiceAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHServiceAC.getDmGrid().initializeGrid(txtHServiceAC.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHServiceAC.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHisCultantRequired">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHisCultantRequired = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHisConsultantRequired() {
        txtHisCultantRequired.getDmGrid().addTextColumn(txtHisCultantRequired.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHisCultantRequired.getDmGrid().initializeGrid(txtHisCultantRequired.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("PrintSerialNo")
        ValueProvider<Find_Mdl, String> printSerialNo();

        @Editor.Path("ItsOnSaleText")
        ValueProvider<Find_Mdl, String> itsOnSaleText();

        @Editor.Path("IsConsultantRequiredText")
        ValueProvider<Find_Mdl, String> isConsultantRequiredText();

        @Editor.Path("AcServiceText")
        ValueProvider<Find_Mdl, String> acServiceText();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Category", 0, "NAME");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().printSerialNo(), "Print S.No.", 120, "printSNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().acServiceText(), "Purchase / Service Ac", 350, "serviceAcName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().itsOnSaleText(), "ITS On Sale", 350, "itsSaleName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isConsultantRequiredText(), "Is Consultan tRequiredText", 350, "isConsultantRequiredText");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);

        dmFind.getHelper().addSort("NAME", DmSortType.ASC);
        dmFind.getHelper().addSort("printSNo", DmSortType.ASC);
    }
    //</editor-fold>

    public ServiceCategoryMaster_Dsg() {
        ini();
        iniFind();
        iniTxtHITSOnSale();
        iniTxtHPurchaseAC();
        iniMenu();
        iniTxtHisConsultantRequired();
    }

    private void ini() {
        Button button;
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);

        button = new Button("Service Applicable To");
        button.setWidth("130Px");
        button.addStyleName("dm-button-grad");

        addServiceApplTo = userControl.addGeneralBlog(button);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "476px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Category");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtCategoryName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        /*====================================< Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Print Serial No.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPrintSerialNo));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Is Consultant Required");
        flwTD.add(lbl);
        flwTD.getElement().getStyle().setMarginLeft(16, Style.Unit.PCT);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(txtHisCultantRequired);
        flwTR.add(flwTD);

        flwTable.add(flwTR);


        /*====================================< Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase / Service A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHServiceAC));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        /*====================================< 3rd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS On Sale");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHITSOnSale));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }

}
