package aarogya.selfToolkit.wholeSaleBillSearchPopup.client;

import aarogya.selfToolkit.wholeSaleBillSearchPopup.shared.GrdWholeSaleBill_Mdl;
import aarogya.selfToolkit.wholeSaleBillSearchPopup.shared.SearchValue_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;

abstract class WholeSaleBillSearchPopup_Dsg {

    public final static String widgetName = "Whole Sale Bill Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public WholeSaleBillSearchPopup_Dsg() {
        ini();
        iniPatientSearch();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("940px");
        popupPanel.setHeight("383px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        btnCancel.setTitle("Close");
        flwCancel.add(btnCancel);

    }
    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "355px", null, null, DmEnum.DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(vl.add(topArea()));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "330px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top: 1px solid gray;");

        flwTr.add(vl.add(grdRoomSearch));
        flwTable.add(flwTr);

        return flwTable;
    }
     private FlowPanel topArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", "20px", null, null, DmEnum.DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        txtSearchValue.getElement().setPropertyString("placeholder", "Search Value");
        flwTr.add(vl.add(txtSearchValue));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }
    
     //<editor-fold defaultstate="collapsed" desc="grdRoomSearch">
    interface DmPatientSearch_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdWholeSaleBill_Mdl> {

        @Editor.Path("VendorName")
        ValueProvider<GrdWholeSaleBill_Mdl, String> vendorName();

        @Editor.Path("BillDate")
        ValueProvider<GrdWholeSaleBill_Mdl, String> billDate();
//
//        @Editor.Path("Mobile")
//        ValueProvider<GrdWholeSaleBill_Mdl, String> mobile();
//
//        @Editor.Path("RecIdIPD")
//        ValueProvider<GrdWholeSaleBill_Mdl, String> regRecId();
//
//        @Editor.Path("RecIdUHID")
//        ValueProvider<GrdWholeSaleBill_Mdl, String> recIdUHID();
//
//        @Editor.Path("OrganizationName")
//        ValueProvider<GrdWholeSaleBill_Mdl, String> organizationName();
//
//        @Editor.Path("DepartmentName")
//        ValueProvider<GrdWholeSaleBill_Mdl, String> departmentName();
    }

    final DmGridPaging<GrdWholeSaleBill_Mdl, DmPatientSearch_PrpAcs, SearchValue_Mdl> grdRoomSearch = new DmGridPaging(GWT.create(DmPatientSearch_PrpAcs.class), 14);

    private void iniPatientSearch() {

        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().name(), "Bill No.", 100, "roomNo");
        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().billDate(), "Bill Date ", 90, "recIdReg");
        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().vendorName(), "Vendor Name", 100, "recIdUHID");
//        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().patient(), "Patient Name", 150, "patient");
//        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().guardian(), "Guardian Name", 130, "guardian");
//        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().mobile(), "Mobile", 90, "mobile");
//        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().organizationName(), "Organization", 130, "organization");
//        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().departmentName(), "Department", 130, "department");
        grdRoomSearch.getDmGrid().initializeGrid(grdRoomSearch.getDmGrid().getPropertyAccess().fieldHashCode(), 950, 325);

        grdRoomSearch.getHelper().addSort("roomNo", DmSortType.ASC);
//        grdRoomSearch.getHelper().addSort("v_Date", DmSortType.DESC);
//        grdRoomSearch.getHelper().addSort("regTime", DmSortType.DESC);
//        grdRoomSearch.getHelper().addSort("short_Name", DmSortType.ASC);
//        grdRoomSearch.getHelper().addSort("V_No", DmSortType.DESC, "V_No");
//        grdRoomSearch.getHelper().addSort("recIdReg", DmSortType.ASC);
    }
    //</editor-fold>
    
}
