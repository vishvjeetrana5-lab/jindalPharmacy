package aarogya.mis.graphicalDashboard.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelpMulti.client.*;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;

abstract class FilterPopup_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final Button btnCancel = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHDiagnosticSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDiagnosticSite = new DmTextHelpMulti<>(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHDiagnosticSite() {
        txtHDiagnosticSite.getDmGrid().addTextColumn(txtHDiagnosticSite.getDmGrid().getPropertyAccess().name(), "Name", 125, "Name");
        txtHDiagnosticSite.getDmGrid().initializeGrid(txtHDiagnosticSite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHDiagnosticSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPharmacySite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPharmacySite = new DmTextHelpMulti<>(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHPharmacySite() {
        txtHPharmacySite.getDmGrid().addTextColumn(txtHPharmacySite.getDmGrid().getPropertyAccess().name(), "Name", 125, "Name");
        txtHPharmacySite.getDmGrid().initializeGrid(txtHPharmacySite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHPharmacySite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIPDSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHIPDSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHIPDSite() {
        txtHIPDSite.getDmGrid().addTextColumn(txtHIPDSite.getDmGrid().getPropertyAccess().name(), "Schedule", 0, "name");
        txtHIPDSite.getDmGrid().initializeGrid(txtHIPDSite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHIPDSite.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOPDSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOPDSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHOPDSite() {
        txtHOPDSite.getDmGrid().addTextColumn(txtHOPDSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHOPDSite.getDmGrid().initializeGrid(txtHOPDSite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHOPDSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    public FilterPopup_Dsg() {
        ini();
        iniTxtHDiagnosticSite();
        iniTxtHIPDSite();

        iniTxtHPharmacySite();
        iniTxtHOPDSite();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("450px");
        popupPanel.setHeight("135px");
        vrtInnerForm.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControls() {
        InlineLabel label;
        Anchor aBtn;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.setTitle("Ok");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");

        toolContainer.setTitle("Filter");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(btnCancel);
        toolContainer.addGap();
        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "94%", "95px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("OPD Site");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "68%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOPDSite));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("IPD Site");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "68%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIPDSite));
        flwTr.add(flwTd);

        flwTable.add(flwTr);


        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Diagnostic Site");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "68%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDiagnosticSite));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Pharmacy Site");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "68%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPharmacySite));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

}
