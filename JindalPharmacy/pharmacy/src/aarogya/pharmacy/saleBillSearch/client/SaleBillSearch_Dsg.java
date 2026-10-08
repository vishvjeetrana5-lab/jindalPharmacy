package aarogya.pharmacy.saleBillSearch.client;

import aarogya.pharmacy.saleBillSearch.shared.GrdSaleBillSearch_Mdl;
import aarogya.pharmacy.saleBillSearch.shared.SearchValueWise_Mdl;
import aarogya.pharmacy.saleBillSearch.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;

abstract class SaleBillSearch_Dsg {

    public final static String widgetName = "Sale Bill Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public SaleBillSearch_Dsg() {
        ini();
        iniPatientSearch();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
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
        DmClient.setStyle(flwTable, "100%", "355px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(topArea()));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "330px", "left", null, DmPosition.NONE, true);

        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top: 1px solid gray;");

        flwTr.add(vl.add(grdRoomSearch));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", "20px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        txtSearchValue.getElement().setPropertyString("placeholder", "Search Value");
        flwTr.add(vl.add(txtSearchValue));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdRoomSearch">
    interface DmPatientSearch_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdSaleBillSearch_Mdl> {

        @Editor.Path("BillNo")
        ValueProvider<GrdSaleBillSearch_Mdl, String> billNo();

        @Editor.Path("VoucherDate")
        ValueProvider<GrdSaleBillSearch_Mdl, String> voucherDate();

        @Editor.Path("PatientName")
        ValueProvider<GrdSaleBillSearch_Mdl, String> patientName();

        @Editor.Path("RegNo")
        ValueProvider<GrdSaleBillSearch_Mdl, String> regNo();

        @Editor.Path("UHID")
        ValueProvider<GrdSaleBillSearch_Mdl, String> UHID();

        @Editor.Path("ConsultantName")
        ValueProvider<GrdSaleBillSearch_Mdl, String> consultantName();
    }

    final DmGridPaging<GrdSaleBillSearch_Mdl, DmPatientSearch_PrpAcs, SearchValueWise_Mdl> grdRoomSearch = new DmGridPaging(GWT.create(DmPatientSearch_PrpAcs.class), 14);

    private void iniPatientSearch() {

        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().billNo(), "Bill No.", 150, "billNo");
        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().voucherDate(), "Voucher Date", 120, "v_Date");
        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 150, "patientName");
        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().regNo(), "Reg No.", 150, "regNo");
        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().UHID(), "UHID No.", 120, "UHIDNo");
        grdRoomSearch.getDmGrid().addTextColumn(grdRoomSearch.getDmGrid().getPropertyAccess().consultantName(), "Consultant Name", 150, "consultantName");
        grdRoomSearch.getDmGrid().initializeGrid(grdRoomSearch.getDmGrid().getPropertyAccess().fieldHashCode(), 950, 325, 3);

        grdRoomSearch.getHelper().addSort("v_Date", DmSortType.DESC);
        grdRoomSearch.getHelper().addSort("vType_ShortName", DmSortType.ASC);
        grdRoomSearch.getHelper().addSort("v_no", DmSortType.DESC);
        grdRoomSearch.getHelper().addSort("billNo", DmSortType.ASC);
    }
    //</editor-fold>
}
