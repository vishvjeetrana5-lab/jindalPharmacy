package aarogya.selfToolkit.patientSchedulePopup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class PatientSchedulePopup_Dsg {

    public final static String widgetName = "Create Schedule";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwTrUHID = new FlowPanel();

    final Button btnCancel = new Button();

    final DmTextBox txtUHID = new DmTextBox(20, true);
    final DmTextBox txtPatntName = new DmTextBox(20, false);
    final DmTextBox txtAge = new DmTextBox(5, false);
    final DmTextBox txtGender = new DmTextBox(10, false);

    final DmTextBox txtGuardianName = new DmTextBox(50, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);

    final DmTextBox txtScheduleDate = new DmTextBox(50, true);
    final DmTextBox txtVDate = new DmTextBox(50, true);

    final FlowPanel flwPatientSearchChild = new FlowPanel();
    final FlowPanel flwPatientSearchParent = new FlowPanel();
    final Button btnSearchPatient = new Button("...");
    final InlineLabel lblUHID = new InlineLabel();
    //<editor-fold defaultstate="collapsed" desc="TxtHSchedule">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSchedule = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHSchedule() {
        txtHSchedule.getDmGrid().addTextColumn(txtHSchedule.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSchedule.getDmGrid().initializeGrid(txtHSchedule.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHSchedule.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public PatientSchedulePopup_Dsg() {
        ini();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("600px");
        popupPanel.setHeight("125px");

        vrtInnerForm.add(iniToolContorls());

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);

        iniTxtHSchedule();
    }

    private FlowPanel workingArea() {

        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "95px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "95px", "left", null, DmPosition.NONE, true);
        flwTr.add(Uhid());
        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel Uhid() {
        FlowPanel flwTable, flwTr, flwTd;
        HorizontalPanel hrtPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "70px", null, null, DmPosition.CENTER, true);

        //=================================Row-1=============================================
        flwTr = new FlowPanel();

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lblUHID.setText("UHID No.");
        lblUHID.getElement().setAttribute("style", "color: Black;  cursor: pointer; font-size:12px;");
        flwTd.add(lblUHID);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(txtUHID);
        hrtPnl.setCellWidth(txtUHID, "100%");
        DmClient.setStyle(flwPatientSearchChild, "100%", "20px", "left", null, DmPosition.NONE, true);
        btnSearchPatient.setWidth("20px");
        btnSearchPatient.setHeight("20px");
        btnSearchPatient.addStyleName("dm-button-grad");
        btnSearchPatient.getElement().setAttribute("style", btnSearchPatient.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnSearchPatient.setTitle("Patient Search");
        flwPatientSearchChild.add(vl.add(btnSearchPatient));
        flwPatientSearchParent.add(flwPatientSearchChild);
        hrtPnl.add(flwPatientSearchParent);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //=================================PatientDetails=============================================   
        flwTr = new FlowPanel();

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPatntName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gender");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGender));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Age");
        lbl.getElement().getStyle().setMarginLeft(35, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAge));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Guardian's Name");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGuardianName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr = new FlowPanel();

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Schedule");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSchedule));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Schedule Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtScheduleDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
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
        btnOk.setTitle("New: (F2)");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        btnCancel.setTitle("Close: (Escape)");
        flwCancel.add(vl.add(btnCancel));

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

}
