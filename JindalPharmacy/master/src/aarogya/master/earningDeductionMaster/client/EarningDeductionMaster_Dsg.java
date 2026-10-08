package aarogya.master.earningDeductionMaster.client;

import aarogya.master.earningDeductionMaster.shared.Find_Mdl;
import dataman.dmBase.self.shared.DmEnum.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class EarningDeductionMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().EARNDEDUCTION_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtName = new DmSuggestBox(30, 10, true, "Name", DmDataType.STRING);
    final DmSuggestBox txtShortName = new DmSuggestBox(10, 10, true, "shortName", DmDataType.STRING);
    final DmTextBoxNumeric txtSerialNo = new DmTextBoxNumeric(2, false, 6, false);

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniTxtHNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Nature", 0, "Name");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 125, 0, 1);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHClaculateOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHClaculateOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniTxtHClaculateOn() {
        txtHClaculateOn.getDmGrid().addTextColumn(txtHClaculateOn.getDmGrid().getPropertyAccess().name(), "Calculate On", 0, "Name");
        txtHClaculateOn.getDmGrid().initializeGrid(txtHClaculateOn.getDmGrid().getPropertyAccess().code(), 125, 0, 1);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRoundOff">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRoundOff = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniTxtHRoundOff() {
        txtHRoundOff.getDmGrid().addTextColumn(txtHRoundOff.getDmGrid().getPropertyAccess().name(), "Round On", 0, "Name");
        txtHRoundOff.getDmGrid().initializeGrid(txtHRoundOff.getDmGrid().getPropertyAccess().code(), 125, 0, 1);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("Nature")
        ValueProvider<Find_Mdl, String> nature();

        @Editor.Path("RoundOff")
        ValueProvider<Find_Mdl, String> roundOff();

        @Editor.Path("CalculatedOn")
        ValueProvider<Find_Mdl, String> calculatedOn();

        @Editor.Path("SerialNo")
        ValueProvider<Find_Mdl, String> serialNo();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 350, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().nature(), "Nature", 200, "nature");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().calculatedOn(), "Calculated On", 200, "calculatedOn");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().serialNo(), "Serial No", 200, "serialNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().roundOff(), "Round Off", 200, "roundOff");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183);

        dmFind.getHelper().addSort("NAME", DmSortType.ASC);
        dmFind.getHelper().addSort("shortName", DmSortType.ASC);
        dmFind.getHelper().addSort("nature", DmSortType.ASC, "Nature");

    }
    //</editor-fold>

    public EarningDeductionMaster_Dsg() {
        ini();

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHNature();
        iniTxtHClaculateOn();
        iniTxtHRoundOff();
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
        DmClient.setStyle(flwTable, "62%", "476px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);

        //Department Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Short Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
//        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNature));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Serial No.");
//        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSerialNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //nature
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Calculate On");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHClaculateOn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //nature
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Round Off");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRoundOff));
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
