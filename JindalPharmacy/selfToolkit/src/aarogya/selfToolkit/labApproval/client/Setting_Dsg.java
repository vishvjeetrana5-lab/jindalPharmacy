package aarogya.selfToolkit.labApproval.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import aarogya.selfToolkit.labApproval.shared.TxtHItem_Mdl;
import aarogya.selfToolkit.labApproval.shared.TxtHSampleNo_Mdl;
import aarogya.selfToolkit.labApproval.shared.TxtHLabNo_Mdl;
import aarogya.selfToolkit.labApproval.shared.TxtHTechnician_Mdl;

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

    //<editor-fold defaultstate="collapsed" desc="txtHLabNo">
    interface TxtHLabNo_PrpAcs extends KeyField_T_PrpAcs<TxtHLabNo_Mdl> {

        @Editor.Path("LabDate")
        ValueProvider<TxtHLabNo_Mdl, String> labDate();

        @Editor.Path("LabTime")
        ValueProvider<TxtHLabNo_Mdl, String> labTime();

        @Editor.Path("ItemName")
        ValueProvider<TxtHLabNo_Mdl, String> itemName();

        @Editor.Path("TestTemplate")
        ValueProvider<TxtHLabNo_Mdl, String> testTemplate();

    }
    final DmTextHelpMulti<TxtHLabNo_Mdl, TxtHLabNo_PrpAcs> txtHLabNo = new DmTextHelpMulti(GWT.create(TxtHLabNo_PrpAcs.class), 5);

    public void iniTxtHLabNo() {
        txtHLabNo.getDmGrid().addTextColumn(txtHLabNo.getDmGrid().getPropertyAccess().name(), "Lab No.", 120, "labRecId");
        txtHLabNo.getDmGrid().addTextColumn(txtHLabNo.getDmGrid().getPropertyAccess().labDate(), "Date", 80, "labDate");
        txtHLabNo.getDmGrid().addTextColumn(txtHLabNo.getDmGrid().getPropertyAccess().labTime(), "Time", 40, "labTime");
        txtHLabNo.getDmGrid().addTextColumn(txtHLabNo.getDmGrid().getPropertyAccess().itemName(), "Item Name", 220, "itemName");
        txtHLabNo.getDmGrid().addTextColumn(txtHLabNo.getDmGrid().getPropertyAccess().testTemplate(), "Test Template", 80, "testTemplateName");
        txtHLabNo.getDmGrid().initializeGrid(txtHLabNo.getDmGrid().getPropertyAccess().code(), 580, 0);
        txtHLabNo.getHelper().addSort("docId", DmSortType.ASC);
        txtHLabNo.getHelper().addSort("labDate", DmSortType.ASC);
        txtHLabNo.getHelper().addSort("labRecId", DmSortType.ASC);
        txtHLabNo.getHelper().addSort("itemName", DmSortType.ASC);
        txtHLabNo.getHelper().addSort("testTemplateName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHStatus">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHStatus() {
        txtHStatus.getDmGrid().addTextColumn(txtHStatus.getDmGrid().getPropertyAccess().name(), "Status", 150, "Name");
        txtHStatus.getDmGrid().initializeGrid(txtHStatus.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHStatus.getHelper().addSort("Name", DmSortType.ASC);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHTechnician">

    interface TxtHTechnician_PrpAcs extends KeyField_T_PrpAcs<TxtHTechnician_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHTechnician_Mdl, String> manualCode();
    }
    final DmTextHelpMulti<TxtHTechnician_Mdl, TxtHTechnician_PrpAcs> txtHTechnician = new DmTextHelpMulti(GWT.create(TxtHTechnician_PrpAcs.class), 5);

    public void iniTxtHTechnician() {
        txtHTechnician.getDmGrid().addTextColumn(txtHTechnician.getDmGrid().getPropertyAccess().name(), "Name", 200, "name");
        txtHTechnician.getDmGrid().addTextColumn(txtHTechnician.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "name");
        txtHTechnician.getDmGrid().initializeGrid(txtHTechnician.getDmGrid().getPropertyAccess().code(), 330, 0);
        txtHTechnician.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSampleNo">
    interface TxtHSampleNo_PrpAcs extends KeyField_T_PrpAcs<TxtHSampleNo_Mdl> {

        @Editor.Path("SampleDate")
        ValueProvider<TxtHSampleNo_Mdl, String> sampleDate();

        @Editor.Path("SampleTime")
        ValueProvider<TxtHSampleNo_Mdl, String> sampleTime();

        @Editor.Path("ItemName")
        ValueProvider<TxtHSampleNo_Mdl, String> itemName();

        @Editor.Path("TestTemplate")
        ValueProvider<TxtHSampleNo_Mdl, String> testTemplate();

    }
    final DmTextHelpMulti<TxtHSampleNo_Mdl, TxtHSampleNo_PrpAcs> txtHSampleNo = new DmTextHelpMulti(GWT.create(TxtHSampleNo_PrpAcs.class), 5);

    public void iniTxtHSampleNo() {
        txtHSampleNo.getDmGrid().addTextColumn(txtHSampleNo.getDmGrid().getPropertyAccess().name(), "Sample No.", 120, "recId");
        txtHSampleNo.getDmGrid().addTextColumn(txtHSampleNo.getDmGrid().getPropertyAccess().sampleDate(), "Date", 80, "v_Date");
        txtHSampleNo.getDmGrid().addTextColumn(txtHSampleNo.getDmGrid().getPropertyAccess().sampleTime(), "Time", 40, "v_Time");
        txtHSampleNo.getDmGrid().addTextColumn(txtHSampleNo.getDmGrid().getPropertyAccess().itemName(), "Item Name", 220, "itemName");
        txtHSampleNo.getDmGrid().addTextColumn(txtHSampleNo.getDmGrid().getPropertyAccess().testTemplate(), "Test Template", 80, "testTemplateName");
        txtHSampleNo.getDmGrid().initializeGrid(txtHSampleNo.getDmGrid().getPropertyAccess().code(),580, 0);
        txtHSampleNo.getHelper().addSort("docId", DmSortType.ASC);
        txtHSampleNo.getHelper().addSort("v_Date", DmSortType.ASC);
        txtHSampleNo.getHelper().addSort("recId", DmSortType.ASC);
        txtHSampleNo.getHelper().addSort("itemName", DmSortType.ASC);
        txtHSampleNo.getHelper().addSort("testTemplateName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHTestTemplate">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHTestTemplate = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHTestTemplate() {
        txtHTestTemplate.getDmGrid().addTextColumn(txtHTestTemplate.getDmGrid().getPropertyAccess().name(), "Name", 180, "Description");
        txtHTestTemplate.getDmGrid().initializeGrid(txtHTestTemplate.getDmGrid().getPropertyAccess().code(), 210, 0);

        txtHTestTemplate.getHelper().addSort("Description", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHItem">
    interface TxtHItem_PrpAcs extends KeyField_T_PrpAcs<TxtHItem_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItem_Mdl, String> manualCode();
    }

    final DmTextHelpMulti<TxtHItem_Mdl, TxtHItem_PrpAcs> txtHItem = new DmTextHelpMulti(GWT.create(TxtHItem_PrpAcs.class), 5);

    public void iniTxtHItem() {
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().name(), "Item Name", 200, "itemName");
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHItem.getDmGrid().initializeGrid(txtHItem.getDmGrid().getPropertyAccess().code(), 335, 0, 1);
        txtHItem.getHelper().addSort("itemName", DmSortType.ASC);
    }

    //</editor-fold>
    public Setting_Dsg() {
        ini();
        iniTxtHStatus();
        iniTxtHLabNo();
        iniTxtHSampleNo();
        iniTxtHTechnician();
        iniTxtHTestTemplate();
        iniTxtHItem();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("550px");
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
        btnCancel.setTitle("Cancel");
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
        DmClient.setStyle(flwTable, "98%", null, null, null, DmPosition.NONE, true);
        flwTable.getElement().getStyle().setPadding(5, Style.Unit.PX);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.5%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblTemp = new DmLabel("From Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblTemp = new DmLabel("To Date");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//   
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.5%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Lab No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLabNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Technician");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTechnician));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.5%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Test Template");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTestTemplate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Sample No.");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSampleNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.5%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Item Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHItem));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Status");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHStatus));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

}
