package aarogya.selfToolkit.fifoPopUp.client;

import aarogya.selfToolkit.fifoPopUp.shared.ClsVar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class Settings_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    private String moduleType = "";

    //<editor-fold defaultstate="collapsed" desc="POPUP">
    final PopupPanel popupPanel = new PopupPanel();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="PANELS">
    final FlowPanel flwOk = new FlowPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="BUTTONS">
    final Button btnCancel = new Button();
    final Button btnOk = new Button();
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="TEXT HELP">
    //<editor-fold defaultstate="collapsed" desc="txtHItemGroup">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHGroup() {
        txtHGroup.getDmGrid().addTextColumn(txtHGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHGroup.getDmGrid().initializeGrid(txtHGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHItemCat">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHCat = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHCat() {
        txtHCat.getDmGrid().addTextColumn(txtHCat.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHCat.getDmGrid().initializeGrid(txtHCat.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHCat.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHItemName">

    interface KeyField_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();

    }
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHItemName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHItemName() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().code(), 450, 0, 2);
        txtHItemName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHModuleType">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModuleType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHModuleType() {
        txtHModuleType.getDmGrid().addTextColumn(txtHModuleType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHModuleType.getDmGrid().initializeGrid(txtHModuleType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHModuleType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
//</editor-fold>

    public Settings_Dsg(String moduleType) {
        this.moduleType = moduleType;
        ini();
        iniTxtHGroup();
        iniTxtHCat();
        iniTxtHItemName();
        iniTxtHDepartment();
        iniTxtHModuleType();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("400px");
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
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        //  aBtn.getElement().getStyle().setColor("green");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.setTitle("Ok");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        // aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.getElement().getStyle().setMarginBottom(1, Style.Unit.PCT);
        flwTd.add(vl.add(viewArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel viewArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl = new DmLabel("");

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        //////////////////////FIRST ROW///////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        if (moduleType.equalsIgnoreCase(ClsVar.moduleTypePharmacy_Code)) {
            lbl = new DmLabel("Medicine Name");
        } else if (moduleType.equalsIgnoreCase(ClsVar.moduleTypeInventory_Code)) {
            lbl = new DmLabel("Item Name");
        }
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHItemName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //////////////////////NEXT ROW///////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //////////////////////NEXT ROW///////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        if (moduleType.equalsIgnoreCase(ClsVar.moduleTypePharmacy_Code)) {
            lbl = new DmLabel("Medicine Category");
        } else if (moduleType.equalsIgnoreCase(ClsVar.moduleTypeInventory_Code)) {
            lbl = new DmLabel("Item Category");
        }
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCat));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //////////////////////NEXT ROW///////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        if (moduleType.equalsIgnoreCase(ClsVar.moduleTypePharmacy_Code)) {
            lbl = new DmLabel("Medicine Group");
        } else if (moduleType.equalsIgnoreCase(ClsVar.moduleTypeInventory_Code)) {
            lbl = new DmLabel("Item Group");
        }
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGroup));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
