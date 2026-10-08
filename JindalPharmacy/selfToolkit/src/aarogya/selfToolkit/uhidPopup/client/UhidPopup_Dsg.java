package aarogya.selfToolkit.uhidPopup.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;

abstract class UhidPopup_Dsg {

    public final static String widgetName = "Patient Detail";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwTrUHID = new FlowPanel();
    final FlowPanel flwTrRegistration = new FlowPanel();
    final FlowPanel flwTrPatient = new FlowPanel();
    final FlowPanel flwTrGuardian = new FlowPanel();
    final FlowPanel flwTrEmail = new FlowPanel();
    final FlowPanel flwTrMobile = new FlowPanel();
    final FlowPanel flwTrID = new FlowPanel();
    final FlowPanel flwTable = new FlowPanel();
    final FlowPanel flwWorkignArea = new FlowPanel();

    final Button btnCancel = new Button();
    final DmTextBox txtRegNo = new DmTextBox(20, false);
    final DmTextBox txtUHID = new DmTextBox(20, false);
    final DmTextBox txtFirstName = new DmTextBox(20, false);
    final DmTextBox txtLastName = new DmTextBox(14, false);
    final DmTextBox txtMiddleName = new DmTextBox(14, false);
    final DmTextBox txtIdNo = new DmTextBox(50, false);
    final DmTextBox txtGuardianName = new DmTextBox(50, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);
    final DmTextBox txtEmail = new DmTextBox(50, false);
    final FlowPanel flwPatientSearch = new FlowPanel();
    final Button searchPatient = new Button("...");

    //<editor-fold defaultstate="collapsed" desc="TxtHIdType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIdType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHIdType() {
        txtHIdType.getDmGrid().addTextColumn(txtHIdType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIdType.getDmGrid().initializeGrid(txtHIdType.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHIdType.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPrefixPatient">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrefixPatient = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);

    private void initxtHPrefixPatient() {
        txtHPrefixPatient.getDmGrid().addTextColumn(txtHPrefixPatient.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPrefixPatient.getDmGrid().initializeGrid(txtHPrefixPatient.getDmGrid().getPropertyAccess().code(), 75, 0, 1);

        txtHPrefixPatient.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPrefixGuardian">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrefixGuardian = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);

    private void initxtHPrefixGuardian() {
        txtHPrefixGuardian.getDmGrid().addTextColumn(txtHPrefixGuardian.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPrefixGuardian.getDmGrid().initializeGrid(txtHPrefixGuardian.getDmGrid().getPropertyAccess().code(), 75, 0, 1);

        txtHPrefixGuardian.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    public UhidPopup_Dsg() {
        ini();
        initxtHPrefixPatient();
        initxtHPrefixGuardian();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("550px");
        popupPanel.setHeight("165px");

        vrtInnerForm.add(iniToolContorls());

        flwWorkignArea.setStyleName("dm-FlwWorkingArea");
        vrtInnerForm.add(flwWorkignArea);
        vrtInnerForm.setCellHeight(flwWorkignArea, "100%");
        popupPanel.add(vrtInnerForm);

        iniTxtHIdType();
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        btnCancel.setTitle("Close");
        flwOk.add(btnOk);

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

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        //toolContainer.addWidget(flwCancel);
        //toolContainer.addGap();

        return toolContainer;
    }

}
