package aarogya.master.consultantSharingMaster.client;

import com.google.gwt.core.shared.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class SearchPopup_Dsg {

    public final static String widgetName = "Search Popup";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    //   final FlowPanel flwHelp = new FlowPanel();
    final FlowPanel flwTR = new FlowPanel();
    final FlowPanel flwService = new FlowPanel();
    final FlowPanel flwEmpanelment = new FlowPanel();
    final FlowPanel flwRoomCategory = new FlowPanel();
    final FlowPanel flwModuleName = new FlowPanel();
    final FlowPanel flwServiceCategory = new FlowPanel();
    final FlowPanel flwServiceGroup = new FlowPanel();

    // final DmLabel lblCaption = new DmLabel("Service");
    //<editor-fold defaultstate="collapsed" desc="Search Type">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSearchType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, false);

    private void iniTxtHSearchType() {
        txtHSearchType.getDmGrid().addTextColumn(txtHSearchType.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHSearchType.getDmGrid().initializeGrid(txtHSearchType.getDmGrid().getPropertyAccess().code(), 280, 0, 1);
        txtHSearchType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHEmpanelment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEmpanelment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHEmpanelment() {
        txtHEmpanelment.getDmGrid().addTextColumn(txtHEmpanelment.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHEmpanelment.getDmGrid().initializeGrid(txtHEmpanelment.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHEmpanelment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceCat">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceCat = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHServiceCat() {
        txtHServiceCat.getDmGrid().addTextColumn(txtHServiceCat.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHServiceCat.getDmGrid().initializeGrid(txtHServiceCat.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHServiceCat.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHServiceGroup() {
        txtHServiceGroup.getDmGrid().addTextColumn(txtHServiceGroup.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHServiceGroup.getDmGrid().initializeGrid(txtHServiceGroup.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHServiceGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHService">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHService = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);

    private void iniTxtHService() {
        txtHService.getDmGrid().addTextColumn(txtHService.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHService.getDmGrid().initializeGrid(txtHService.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHService.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHModule">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModule = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, false);

    private void iniTxtHModule() {
        txtHModule.getDmGrid().addTextColumn(txtHModule.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHModule.getDmGrid().initializeGrid(txtHModule.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHModule.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRoomCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRoomCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHRoomCategory() {
        txtHRoomCategory.getDmGrid().addTextColumn(txtHRoomCategory.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHRoomCategory.getDmGrid().initializeGrid(txtHRoomCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHRoomCategory.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    public SearchPopup_Dsg() {
        ini();
        iniToolContorls();
        iniTxtHEmpanelment();
        iniTxtHModule();
        iniTxtHServiceCat();
        iniTxtHServiceGroup();
        iniTxtHService();
        iniTxtHRoomCategory();
        iniTxtHSearchType();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("400px");
        popupPanel.setHeight("80px");

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
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Search Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSearchType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< Row >====================================*/

        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        DmClient.setStyle(flwService, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(lblCaption);
//        flwTR.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service");
        flwTd.add(lbl);
        flwService.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHService));
        flwService.add(flwTd);
        flwTR.add(flwService);
        /*====================================< Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Category");
        flwTd.add(lbl);
        flwServiceCategory.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceCat));
        flwServiceCategory.add(flwTd);
        /*====================================< Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Empanelment");
        flwTd.add(lbl);
        flwEmpanelment.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmpanelment));
        flwEmpanelment.add(flwTd);
        /*====================================< Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Module Name");
        flwTd.add(lbl);
        flwModuleName.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHModule));
        flwModuleName.add(flwTd);
        /*====================================< Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Group");
        flwTd.add(lbl);
        flwServiceGroup.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceGroup));
        flwServiceGroup.add(flwTd);
        /*====================================< Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Room Category");
        flwTd.add(lbl);
        flwRoomCategory.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRoomCategory));
        flwRoomCategory.add(flwTd);

        flwTable.add(flwTR);

        return flwTable;
    }

}
