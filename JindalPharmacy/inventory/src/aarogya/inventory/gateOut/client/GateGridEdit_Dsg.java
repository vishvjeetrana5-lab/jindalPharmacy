package aarogya.inventory.gateOut.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;


public abstract class GateGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmUserControl userControl = new DmUserControl();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBoxNumeric txtQty = new DmTextBoxNumeric(2, false, 8, false);
    final DmTextBoxNumeric txtEstmValue = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBox txtItem = new DmTextBox(100, false);
    final DmRemarkTextBox txtSpecification = new DmRemarkTextBox(250, false);
    final DmTextBox txtInDate = new DmTextBox(11, false);
    final DmTextBox txtInTime = new DmTextBox(11, false);
    
    //<editor-fold defaultstate="collapsed" desc="txtHUnit">
    interface TxtHPartyName_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        
    }

    final DmTextHelp<KeyFieldExtended, TxtHPartyName_PrpAcs> txtHUnit = new DmTextHelp(GWT.create(TxtHPartyName_PrpAcs.class), 6, false);

    private void iniTxtHUnit() {
        txtHUnit.getDmGrid().addTextColumn(txtHUnit.getDmGrid().getPropertyAccess().name(), "Unit", 0, "unit");
        txtHUnit.getDmGrid().initializeGrid(txtHUnit.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHUnit.getHelper().addSort("unit", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHAsset">
    interface TxtHAsset_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {
        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();
        
    }

    final DmTextHelp<KeyFieldExtended, TxtHAsset_PrpAcs> txtHAsset = new DmTextHelp(GWT.create(TxtHAsset_PrpAcs.class), 6, false);

    private void iniTxtHAsset() {
        txtHAsset.getDmGrid().addTextColumn(txtHAsset.getDmGrid().getPropertyAccess().name(), "Manual Code", 100, "assetManualCode");
        txtHAsset.getDmGrid().addTextColumn(txtHAsset.getDmGrid().getPropertyAccess().sharedModifiedBy(), "Asset Name", 200, "assetName");
        txtHAsset.getDmGrid().initializeGrid(txtHAsset.getDmGrid().getPropertyAccess().code(), 330, 0, 1);
        txtHAsset.getHelper().addSort("assetName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public GateGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        ini();

    }

    private void ini() {
        iniTxtHUnit();
        iniTxtHAsset();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        //===============================< 1st Row >===============================//
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("item");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "97%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtItem));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //===============================< 2nd Row >===============================//
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Qty");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Unit");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("In Date/Time");
        lbl.getElement().getStyle().setMarginLeft(56, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtQty));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtHUnit));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtInDate));
        flwTD.getElement().getStyle().setMarginLeft(17, Style.Unit.PCT);
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtInTime));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        
        //===============================< 5th Row >===============================//
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Specification");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "97%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtSpecification));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //===============================< 4th Row >===============================//
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Estm. Value");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtEstmValue));
        flwTR.add(flwTD);
        
        flwTable.add(flwTR);
        
        
        
        //===============================< 3rd Row >===============================//
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Asset");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "97%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtHAsset));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

}
