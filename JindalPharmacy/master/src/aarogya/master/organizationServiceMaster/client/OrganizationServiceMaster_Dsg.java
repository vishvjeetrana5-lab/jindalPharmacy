package aarogya.master.organizationServiceMaster.client;

import aarogya.master.organizationServiceMaster.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract public class OrganizationServiceMaster_Dsg extends IsWidgetExtended {

    public final static String widgetName = new pmMaster().ORGANIZATION_SERVICE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String priceListBlog = "Price List";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final Button btnApplicable = new Button("Price List");
    final DmSuggestBox txtOrgServiceName = new DmSuggestBox(35, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtServiceCode = new DmTextBox(10, true);
    final DmTextBoxNumeric txtPrintSno = new DmTextBoxNumeric(2, false, 4, false);

    final MenuItem mnuExportData = new MenuItem("Export Service");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHOrganization">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOrganization = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHOrganization() {

        txtHOrganization.getDmGrid().addTextColumn(txtHOrganization.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHOrganization.getDmGrid().initializeGrid(txtHOrganization.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHOrganization.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHServiceGroup() {
        txtHServiceGroup.setAppendable(true);
        txtHServiceGroup.getDmGrid().addTextColumn(txtHServiceGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHServiceGroup.getDmGrid().initializeGrid(txtHServiceGroup.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHServiceGroup.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Organization")
        ValueProvider<Find_Mdl, String> organization();

        @Editor.Path("ServiceName")
        ValueProvider<Find_Mdl, String> serviceName();

        @Editor.Path("ServiceCode")
        ValueProvider<Find_Mdl, String> serviceCode();

        @Editor.Path("ServiceGroup")
        ValueProvider<Find_Mdl, String> serviceGroup();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().serviceName(), "Org. Service Name", 280, "serviceName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().serviceCode(), "Service Code", 150, "serviceCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().serviceGroup(), "Service Group", 250, "serviceGroup");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().organization(), "organization", 1, "organization");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 400, 4);

        dmFind.getHelper().addSort("organization", DmSortType.ASC);
        dmFind.getHelper().addSort("serviceName", DmSortType.ASC);
    }
    //</editor-fold>

    public OrganizationServiceMaster_Dsg() {
        ini();

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        iniFind();
        iniTxtHOrganization();
        iniTxtHServiceGroup();
        iniMenu();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "62%", "519px", null, null, DmPosition.CENTER, true);

        //1st Row
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Organization");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOrganization));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        // 4th row
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceGroup));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //2nd Row
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Org. Service Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOrgServiceName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //3rd Row
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtServiceCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Print SNo.");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPrintSno));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(50);

        btnApplicable.setWidth("100px");
        userControl.addControlBlog(priceListBlog, btnApplicable);
        userControl.addGap(50);

        userControl.addTextBlog(modifiedByBlog, 100, true, false);

        return userControl;
    }

    private Menu iniMenu() {
        menu.add(mnuExportData);
        return menu;
    }
}
