package aarogya.pharmacy.stockSummary.client;

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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.*;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

abstract class SummarySetting_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final DmTextBox txtFromDate = new DmTextBox(12, true);
    final DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineCategory">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineCategory = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHMedicineCategory() {
        txtHMedicineCategory.getDmGrid().addTextColumn(txtHMedicineCategory.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMedicineCategory.getDmGrid().initializeGrid(txtHMedicineCategory.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHMedicineCategory.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineGroup">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineGroup = new DmTextHelpMulti<>(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHMedicineGroup() {
        txtHMedicineGroup.getDmGrid().addTextColumn(txtHMedicineGroup.getDmGrid().getPropertyAccess().name(), "Name", 125, "Name");
        txtHMedicineGroup.getDmGrid().initializeGrid(txtHMedicineGroup.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHMedicineGroup.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineMake">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineMake = new DmTextHelpMulti<>(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHMedicineMake() {
        txtHMedicineMake.getDmGrid().addTextColumn(txtHMedicineMake.getDmGrid().getPropertyAccess().name(), "Name", 125, "Name");
        txtHMedicineMake.getDmGrid().initializeGrid(txtHMedicineMake.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHMedicineMake.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineName">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineName = new DmTextHelpMulti<>(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHMedicineName() {
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().name(), "Name", 125, "Name");
        txtHMedicineName.getDmGrid().initializeGrid(txtHMedicineName.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHMedicineName.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpMulti<>(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 125, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHDepartment.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSiteName">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti<>(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 125, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHSiteName.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHStockStatus">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHStockStatus = new DmTextHelpMulti<>(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHStockStatus() {
        txtHStockStatus.getDmGrid().addTextColumn(txtHStockStatus.getDmGrid().getPropertyAccess().name(), "Name", 125, "Name");
        txtHStockStatus.getDmGrid().initializeGrid(txtHStockStatus.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSorting">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSorting = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHSorting() {
        txtHSorting.getDmGrid().addTextColumn(txtHSorting.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSorting.getDmGrid().initializeGrid(txtHSorting.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHValueOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHValueOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHValueOn() {
        txtHValueOn.getDmGrid().addTextColumn(txtHValueOn.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHValueOn.getDmGrid().initializeGrid(txtHValueOn.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGroupBy">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGroupBy = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, true);

    private void iniTxtHGroupBy() {
        txtHGroupBy.getDmGrid().addTextColumn(txtHGroupBy.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGroupBy.getDmGrid().initializeGrid(txtHGroupBy.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHNarcotics">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNarcotics = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHNarcotics() {
        txtHNarcotics.getDmGrid().addTextColumn(txtHNarcotics.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHNarcotics.getDmGrid().initializeGrid(txtHNarcotics.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHConsignmentItem">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsignmentItem = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHConsignmentItem() {
        txtHConsignmentItem.getDmGrid().addTextColumn(txtHConsignmentItem.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHConsignmentItem.getDmGrid().initializeGrid(txtHConsignmentItem.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHFlowThrough">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHFlowThrough = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHFlowThrough() {
        txtHFlowThrough.getDmGrid().addTextColumn(txtHFlowThrough.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHFlowThrough.getDmGrid().initializeGrid(txtHFlowThrough.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHHighValue">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHighValue = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHHighValue() {
        txtHHighValue.getDmGrid().addTextColumn(txtHHighValue.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHHighValue.getDmGrid().initializeGrid(txtHHighValue.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBilledItem">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBilledItem = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHBuildItem() {
        txtHBilledItem.getDmGrid().addTextColumn(txtHBilledItem.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHBilledItem.getDmGrid().initializeGrid(txtHBilledItem.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHLifeSaving">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLifeSaving = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHLifeSaving() {
        txtHLifeSaving.getDmGrid().addTextColumn(txtHLifeSaving.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHLifeSaving.getDmGrid().initializeGrid(txtHLifeSaving.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    public SummarySetting_Dsg() {
        ini();
        iniTxtHMedicineCategory();
        iniTxtHMedicineGroup();
        iniTxtHMedicineMake();
        iniTxtHMedicineName();
        iniTxtHDepartment();
        iniTxtHSiteName();
        iniTxtHValueOn();
        iniTxtHGroupBy();
        iniTxtHStockStatus();
        iniTxtHSorting();
        iniTxtHNarcotics();
        iniTxtHConsignmentItem();
        iniTxtHBuildItem();
        iniTxtHHighValue();
        iniTxtHFlowThrough();
        iniTxtHLifeSaving();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("425px");
        popupPanel.setHeight("258px");
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
        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
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
        DmClient.setStyle(flwTable, "98%", "284px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("From Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("To Date");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Medicine Cat.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineCategory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Medicine Group");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineGroup));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Medicine Comp.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineMake));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Medicine Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Department");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 7th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Site Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSiteName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Status");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHStockStatus));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 9th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Group By");
        lblTemp.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGroupBy));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Value On");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHValueOn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Sorting");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSorting));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 11th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Narcotics");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNarcotics));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Consignment Item");
        lblTemp.getElement().getStyle().setMarginLeft(23, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsignmentItem));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 13th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("High Value");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHighValue));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Billed Item");
        lblTemp.getElement().getStyle().setMarginLeft(23, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBilledItem));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 14th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Flow Through");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHFlowThrough));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Life Saving");
        lblTemp.getElement().getStyle().setMarginLeft(23, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLifeSaving));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

}
