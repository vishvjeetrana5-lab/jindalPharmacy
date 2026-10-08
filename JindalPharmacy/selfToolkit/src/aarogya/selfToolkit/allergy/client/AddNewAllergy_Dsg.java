package aarogya.selfToolkit.allergy.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;

public class AddNewAllergy_Dsg {

    public static final String widgetName = "Add New Allergy";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final PopupPanel flwPanel = new PopupPanel();

    final Button btnSave = new Button();
    final FlowPanel flwSave = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();

    final DmTextBox txtName = new DmTextBox(100, true);

    //<editor-fold defaultstate="collapsed" desc="txtHMedNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMedNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true, true);

    private void iniTxtHMedNature() {
        txtHMedNature.getDmGrid().addTextColumn(txtHMedNature.getDmGrid().getPropertyAccess().name(), "Nature", 0, "medicineNature");
        txtHMedNature.getDmGrid().initializeGrid(txtHMedNature.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHMedNature.getHelper().addSort("medicineNature", DmSortType.ASC);
    }
    //</editor-fold>

    public AddNewAllergy_Dsg() {
        ini();
        iniTxtHMedNature();
    }

    private void ini() {
        VerticalPanel vrtInnerForm = new VerticalPanel();
        FlowPanel flw = new FlowPanel();

        flwPanel.setWidth("325px");
        flwPanel.setHeight("110px");
        flwPanel.setGlassEnabled(true);
        vrtInnerForm.add(iniToolControl());

        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("60px");
        btnClose.setTitle("Close");
        flwClose.add(btnClose);

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("60px");
        btnSave.setTitle("Save");
        flwSave.add(vl.add(btnSave));

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwSave);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "75px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Name");
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtName));
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Nature");
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtHMedNature));
        flwTable.add(flwTr);

        return flwTable;
    }

}
