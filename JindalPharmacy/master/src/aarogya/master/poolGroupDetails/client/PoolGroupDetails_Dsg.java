package aarogya.master.poolGroupDetails.client;

import aarogya.master.poolGroupDetails.shared.Find_Mdl;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetailsGridEdit_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract public class PoolGroupDetails_Dsg extends IsWidgetExtended {

    public final static String widgetName = new pmMaster().POOL_GROUP_DETAILS_ENTRY.caption;
    public final static String moduleName = new pmMaster().moduleName;

    public final static String preparedByBlog = "Prepared By ";
    public final static String modifiedByBlog = "Modified By ";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtFromDate = new DmTextBox(15, true);
    final DmTextBox txtToDate = new DmTextBox(15, true);

    //<editor-fold defaultstate="collapsed" desc="TxtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPoolGroupName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHPoolGroupName() {
        txtHPoolGroupName.getDmGrid().addTextColumn(txtHPoolGroupName.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPoolGroupName.getDmGrid().initializeGrid(txtHPoolGroupName.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHPoolGroupName.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("PoolGroupName")
        ValueProvider<Find_Mdl, String> poolGroupName();

        @Editor.Path("FromDate")
        ValueProvider<Find_Mdl, String> fromDate();

        @Editor.Path("ToDate")
        ValueProvider<Find_Mdl, String> toDate();
    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().poolGroupName(), "Pool Group Name", 500, "poolGroupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().fromDate(), "From Date", 300, "fromDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().toDate(), "To Date", 300, "toDate");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0);
        dmFind.getHelper().addSort("fromDate", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("toDate", DmEnum.DmSortType.DESC);

    }
    //</editor-fold>

    public PoolGroupDetails_Dsg() {
        ini();
        iniFind();
        iniTxtHPoolGroupName();

    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmEnum.DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.add(addUserControl());
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    //<editor-fold defaultstate="collapsed" desc="addUserControl">
    private DmUserControl addUserControl() {
        Anchor aBtn;
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);

        return userControl;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="workingArea">
    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.8%", "524px", "left", null, DmEnum.DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTable.getElement().getStyle().setMarginTop(4, Style.Unit.PX);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7%", "44px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51%", "85px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "17.5%", "85px", "right", null, DmEnum.DmPosition.NONE, true);
//        flwTd.add(vl.add(topRightArea()));
//        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "45%", "451px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(66, Style.Unit.PCT);
        flwTd.add(vl.add(iniGrdGateDetail()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
    //<editor-fold defaultstate="collapsed" desc="topRightArea">

    private FlowPanel topRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        //===============================< 2nd Row >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("To Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="GrdGate">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<PoolGroupDetailsGridEdit_Mdl> {

        @Editor.Path("ValueName")
        ValueProvider<PoolGroupDetailsGridEdit_Mdl, String> valueName();

        @Editor.Path("Amount")
        ValueProvider<PoolGroupDetailsGridEdit_Mdl, String> amount();

    }
    final DmGrid<PoolGroupDetailsGridEdit_Mdl, Grd_PrpAcs> grdGateDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid iniGrdGateDetail() {
        grdGateDetail.addRowNumberColumn();
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().name(), "Consultant Name", 250, "");
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().valueName(), "%/Fix", 130, "");
        grdGateDetail.addTextColumn(grdGateDetail.getPropertyAccess().amount(), "Amount", 130, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        grdGateDetail.initializeGrid(grdGateDetail.getPropertyAccess().fieldHashCode(), 540, 451);
        return grdGateDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="topLeftArea">
    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //===============================< 1st Row >===============================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Pool Group Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPoolGroupName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(4, Style.Unit.PCT);
        lbl = new DmLabel("To Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //===============================< ******* >===============================//

        return flwTable;
    }

}
