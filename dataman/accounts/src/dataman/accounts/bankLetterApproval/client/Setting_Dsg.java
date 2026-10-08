package dataman.accounts.bankLetterApproval.client;


import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;

abstract class Setting_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final DmTextBox txtFromDate = new DmTextBox(15, false);
    final DmTextBox txtToDate = new DmTextBox(15, false);

        //    //<editor-fold defaultstate="collapsed" desc="txtHRequisitionNo">
//    interface TxtHRequisitionNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHRequisitionNo_Mdl> {
//
//        @Editor.Path("ReqType")
//        ValueProvider<TxtHRequisitionNo_Mdl, String> reqType();
//
//        @Editor.Path("ReqDate")
//        ValueProvider<TxtHRequisitionNo_Mdl, String> reqDate();
//    }
//
//    final DmTextHelpMulti<TxtHRequisitionNo_Mdl, TxtHRequisitionNo_PrpAcs> txtHRequisitionNo = new DmTextHelpMulti(GWT.create(TxtHRequisitionNo_PrpAcs.class), 7);
//
//    private void iniTxtHRequisitionNo() {
//        txtHRequisitionNo.getDmGrid().addTextColumn(txtHRequisitionNo.getDmGrid().getPropertyAccess().name(), "Requisition No.", 90, "requisitionNo");
//        txtHRequisitionNo.getDmGrid().addTextColumn(txtHRequisitionNo.getDmGrid().getPropertyAccess().reqType(), "Req. Type", 80, "category");
//        txtHRequisitionNo.getDmGrid().addTextColumn(txtHRequisitionNo.getDmGrid().getPropertyAccess().reqDate(), "Req. Date", 80, "requisitionDate", DmDataType.DATE);
//        txtHRequisitionNo.getDmGrid().initializeGrid(txtHRequisitionNo.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 2);
//
//        txtHRequisitionNo.getHelper().addSort("requisitionDate", DmSortType.DESC);
//        txtHRequisitionNo.getHelper().addSort("requisitionNo", DmSortType.ASC);
//    }
//    //</editor-fold>
    
//      //<editor-fold defaultstate="collapsed" desc="txtHFromLocation">
//    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHFromLocation = new DmTextHelpMulti(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, false);
//
//    public void iniTxtHLocation() {
//        txtHFromLocation.getDmGrid().addTextColumn(txtHFromLocation.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
//        txtHFromLocation.getDmGrid().initializeGrid(txtHFromLocation.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
//
//        txtHFromLocation.getHelper().addSort("NAME", DmSortType.ASC);
//    }
//
//    //</editor-fold>

   
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHStatus() {
        txtHStatus.getDmGrid().addTextColumn(txtHStatus.getDmGrid().getPropertyAccess().name(), "Status", 150, "Name");
        txtHStatus.getDmGrid().initializeGrid(txtHStatus.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHStatus.getHelper().addSort("Name", DmSortType.ASC);
    }

    public Setting_Dsg() {
        ini();
        iniTxtHStatus();
//        iniTxtHLocation();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("575px");
        popupPanel.setHeight("100px");
        iniToolControls();
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolControls() {
        InlineLabel label;
        Anchor aBtn;
        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

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
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "58Px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("From Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15.5%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("To Date");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
//        lblTemp = new DmLabel("To Location");
//        flwTd.add(lblTemp);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "34.5%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHFromLocation));
//        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15.5%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Status");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHStatus));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
      
        return flwTable;
    }

}
