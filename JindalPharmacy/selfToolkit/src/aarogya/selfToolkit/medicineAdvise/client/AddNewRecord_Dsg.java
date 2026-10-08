package aarogya.selfToolkit.medicineAdvise.client;

import aarogya.selfToolkit.medicineAdvise.shared.TxtHCompany_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;

public class AddNewRecord_Dsg {

    public static final String widgetName = "Add New Medicine";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final PopupPanel flwPanel = new PopupPanel();

    final Button btnSave = new Button();
    final FlowPanel flwSave = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();

    final DmSuggestBox txtMedicineName = new DmSuggestBox(100, 7, true, "displayName", DmDataType.STRING);

    //<editor-fold defaultstate="collapsed" desc="txtHMedCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMedCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true, true);

    private void iniTxtHMedNature() {
        txtHMedCategory.getDmGrid().addTextColumn(txtHMedCategory.getDmGrid().getPropertyAccess().name(), "Nature", 0, "name");
        txtHMedCategory.getDmGrid().initializeGrid(txtHMedCategory.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHMedCategory.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHMedGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMedGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7);

    private void iniTxtHMedGroup() {
        txtHMedGroup.getDmGrid().addTextColumn(txtHMedGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHMedGroup.getDmGrid().initializeGrid(txtHMedGroup.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHMedGroup.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHCompany">
    interface TxtHCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHCompany_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHCompany_Mdl, String> shortName();
    }

    final DmTextHelp<TxtHCompany_Mdl, TxtHCompany_PrpAcs> txtHCompany = new DmTextHelp(GWT.create(TxtHCompany_PrpAcs.class), 7);

    private void iniTxtHCompany() {
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().name(), "Name", 0, "companyName");
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        txtHCompany.getDmGrid().initializeGrid(txtHCompany.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHCompany.getHelper().addSort("companyName", DmSortType.ASC);
    }
    //</editor-fold>

    public AddNewRecord_Dsg() {
        ini();
        iniTxtHMedNature();
        iniTxtHMedGroup();
        iniTxtHCompany();
    }

    private void ini() {
        VerticalPanel vrtInnerForm = new VerticalPanel();
        FlowPanel flw = new FlowPanel();

        flwPanel.setWidth("325px");
        flwPanel.setHeight("185px");
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
        DmClient.setStyle(flwTable, "98%", "150px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Name");
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtMedicineName));
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Category");
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtHMedCategory));
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Medicine Group");
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtHMedGroup));
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Medicine Company");
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtHCompany));
        flwTable.add(flwTr);

        return flwTable;
    }

}
