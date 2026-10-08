package aarogya.inventory.visitorGatePassSearch.client;

import aarogya.inventory.visitorGatePassSearch.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;

abstract public class VisitorGatePassSearch_Dsg {

    public final static String widgetName = "Visitor Gate Pass Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final FlowPanel flwOK = new FlowPanel();
    final Button btnOK = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public VisitorGatePassSearch_Dsg() {
        ini();
        iniGridVisitorGatePassDetail();
    }

    //<editor-fold defaultstate="collapsed" desc="GridVisitorGatePassDetail">
    interface GrdPaging_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdVisitorGatePassDetailsSearch_Mdl> {

        @Editor.Path("Date")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> date();

        @Editor.Path("Time")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> time();

        @Editor.Path("VisitorName")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> visitorName();

        @Editor.Path("Address")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> address();

        @Editor.Path("CityName")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> cityName();

        @Editor.Path("Mobile")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> mobile();

        @Editor.Path("VechileType")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> vechileType();

        @Editor.Path("VechileNo")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> vechileNo();

        @Editor.Path("Purpose")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> purpose();

        @Editor.Path("PersonToMeet")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> personToMeet();

        @Editor.Path("GateOutDate")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> gateOutDate();

        @Editor.Path("GateOutTime")
        ValueProvider<GrdVisitorGatePassDetailsSearch_Mdl, String> gateOutTime();

    }

    final DmGridPaging<GrdVisitorGatePassDetailsSearch_Mdl, GrdPaging_PrpAcs, VisitorGateSearchValue_Mdl> GridVisitorGatePassDetail = new DmGridPaging(GWT.create(GrdPaging_PrpAcs.class), 13, false);

    private void iniGridVisitorGatePassDetail() {
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().name(), "Gate Pass No", 80, "recId");
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().date(), "Date", 80, "v_Date", DmEnum.DmDataType.DATE);
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().time(), " Time", 50, "v_Time", DmEnum.DmDataType.DOUBLE);
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().visitorName(), "Visitor Name", 80, "name");
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().address(), "Address", 100, "address");
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().cityName(), "City", 50, "cityName");
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().mobile(), "Mobile", 80, "mobile");
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().vechileType(), "Vechile Type ", 80, "vehicleType");
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().vechileNo(), "Vechile No", 80, "vehicleNo");
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().personToMeet(), "Person To Meet", 80, "personToMeet");
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().purpose(), "Purpose", 80, "purpose");
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().gateOutDate(), "Out Date", 80, "gateOutDate", DmEnum.DmDataType.DATE);
        GridVisitorGatePassDetail.getDmGrid().addTextColumn(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().gateOutTime(), "Out Time", 80, "gateOutTime", DmEnum.DmDataType.DOUBLE);
        GridVisitorGatePassDetail.getDmGrid().initializeGrid(GridVisitorGatePassDetail.getDmGrid().getPropertyAccess().fieldHashCode(), 1000, 320, 13);
        GridVisitorGatePassDetail.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        GridVisitorGatePassDetail.getHelper().addSort("recId", DmEnum.DmSortType.DESC);
    }
    //</editor-fold>

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("900px");
        popupPanel.setHeight("360px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("OK");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOK.setTitle("Ok");
        btnOK.addStyleName("dm-top-control-btnStyle");
        btnOK.getElement().appendChild(aBtn.getElement());
        btnOK.getElement().appendChild(label.getElement());
        btnOK.setWidth("70px");
        flwOK.add(btnOK);

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh ");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.setTitle("Refresh");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("75px");
        flwRefresh.add(btnRefresh);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOK);

        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", "20px", null, null, DmEnum.DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Indent No.");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "332px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(vl.add(topArea()));

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "312px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(vl.add(GridVisitorGatePassDetail));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

}
