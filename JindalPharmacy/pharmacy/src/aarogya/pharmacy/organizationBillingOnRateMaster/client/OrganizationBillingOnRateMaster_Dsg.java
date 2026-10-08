package aarogya.pharmacy.organizationBillingOnRateMaster.client;

import aarogya.pharmacy.organizationBillingOnRateMaster.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class OrganizationBillingOnRateMaster_Dsg extends IsWidgetExtended {

    public final static String widgetName = new pmPharmacy().ORGANIZATION_BILLING_ON_RATE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    //<editor-fold defaultstate="collapsed" desc="txtHOrganisation">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOrganisation = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHOrganisation() {
        txtHOrganisation.getDmGrid().addTextColumn(txtHOrganisation.getDmGrid().getPropertyAccess().name(), "Name", 200, "name");
        txtHOrganisation.getDmGrid().initializeGrid(txtHOrganisation.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHOrganisation.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineSaleOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineSaleOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHMedicineSaleOn() {
        txtHMedicineSaleOn.getDmGrid().addTextColumn(txtHMedicineSaleOn.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHMedicineSaleOn.getDmGrid().initializeGrid(txtHMedicineSaleOn.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("MedicineSaleOnText")
        ValueProvider<Find_Mdl, String> medicineSaleOnText();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Organization", 0, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().medicineSaleOnText(), "Medicine Sale On ", 100, "medicineSaleOn");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    public OrganizationBillingOnRateMaster_Dsg() {
        ini();
        iniTxtHOrganisation();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHMedicineSaleOn();

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
        DmClient.setStyle(flwTable, "70%", "60px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Orgnanization");
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOrganisation));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Sale On");
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineSaleOn));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }
}
