package dataman.utility.productLicensedTo.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.utility.productLicensedTo.shared.*;

abstract class ProductLicensedTo_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().PRODUCT_LICENSED_TO.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();

    final DmSuggestBox txtCompanyName = new DmSuggestBox(75, 10, true, "comp_name", DmDataType.STRING);
    final DmTextBox txtCompanyCode = new DmTextBox(3, false);
    final DmTextBox txtShortName = new DmTextBox(10, true);

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("LatestCompCode")
        ValueProvider<Find_Mdl, String> latestCompCode();

        @Editor.Path("Sname")
        ValueProvider<Find_Mdl, String> sname();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    public void iniFind() {

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Company Name", 250, "Comp_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().sname(), "Short Name", 250, "sname");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().latestCompCode(), "Company Code ", 250, "latestCompCode");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);//183
        dmFind.getHelper().addSort("Comp_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("sname", DmSortType.ASC);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLatCompany">
    interface DmCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHCompany_Mdl> {

        @Editor.Path("StartDt")
        ValueProvider<TxtHCompany_Mdl, String> startDt();

        @Editor.Path("EndDt")
        ValueProvider<TxtHCompany_Mdl, String> endDt();

        @Editor.Path("CentralDataPath")
        ValueProvider<TxtHCompany_Mdl, String> centralDataPath();

    }
    final DmTextHelp<TxtHCompany_Mdl, DmCompany_PrpAcs> txtHLatCompany = new DmTextHelp(GWT.create(DmCompany_PrpAcs.class), 4, false);

    private void iniTxtHCompany() {
        txtHLatCompany.getDmGrid().addTextColumn(txtHLatCompany.getDmGrid().getPropertyAccess().name(), "Current Year", 100, "cYear");
        txtHLatCompany.getDmGrid().addTextColumn(txtHLatCompany.getDmGrid().getPropertyAccess().startDt(), "Start Date", 80, "start_Dt");
        txtHLatCompany.getDmGrid().addTextColumn(txtHLatCompany.getDmGrid().getPropertyAccess().endDt(), "End Date", 80, "end_Dt");
        txtHLatCompany.getDmGrid().addTextColumn(txtHLatCompany.getDmGrid().getPropertyAccess().centralDataPath(), "Cen. Data Path", 100, "centralData_Path");
        txtHLatCompany.getDmGrid().initializeGrid(txtHLatCompany.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHLatCompany.getHelper().addSort("start_dt", DmSortType.DESC);

    }
    //</editor-fold>

    public ProductLicensedTo_Dsg() {
        fIni();
        iniTxtHCompany();
    }

    private void fIni() {
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
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
        iniFind();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "65px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Company Name");

        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCompanyName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ///////////////////////// TR2 //////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Company Code");

        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCompanyCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Short Name");
        lblTemp.getElement().getStyle().setMarginLeft(26, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR15 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Lat. Comp. Code");

        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLatCompany));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
