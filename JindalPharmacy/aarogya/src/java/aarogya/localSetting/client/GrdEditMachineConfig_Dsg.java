package aarogya.localSetting.client;

import aarogya.localSetting.shared.GrdLabTestCodification_Mdl;
import aarogya.localSetting.shared.TxtHMachineSpecification_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.CaptionPanel;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class GrdEditMachineConfig_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //  final DmTextBox txtMachineName = new DmTextBox(50, false);
    final DmSuggestBox txtMachineName = new DmSuggestBox(50, 10, true, "Name", DmEnum.DmDataType.STRING);
    final DmTextBox txtMachineCode = new DmTextBox(100, false);

    final DmTextBox txtCompanyName = new DmTextBox(50, false);
    final DmTextBox txtCommunicationMode = new DmTextBox(50, false);
    final DmTextBox txtTransmissionMode = new DmTextBox(50, false);
    final DmTextBox txtProtocol = new DmTextBox(50, false);

    final FlowPanel flwDynamicMain = new FlowPanel();
    final FlowPanel flwFrameTCP = new FlowPanel();
    final FlowPanel flwFrameASTM = new FlowPanel();

    final DmTextBox txtIPAddress = new DmTextBox(20, true);
    final DmTextBox txtPortNo = new DmTextBox(10, true);

    final DmTextBox txtComPortNo = new DmTextBox(5, true);
    final DmTextBox txtBaudRate = new DmTextBox(10, true);
    final DmTextBox txtStopBit = new DmTextBox(5, true);
    final DmTextBox txtDataBit = new DmTextBox(5, true);
    final DmTextBox txtParityStatus = new DmTextBox(5, true);
    final DmTextBox txtHardwareControl = new DmTextBox(5, true);
    final DmClient.ViaList vl2 = new DmClient.ViaList();
    final FlowPanel flwDynamic = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="TxtHdietCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMachineName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniMachineName() {
        txtHMachineName.getDmGrid().addTextColumn(txtHMachineName.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHMachineName.getDmGrid().initializeGrid(txtHMachineName.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHMachineName.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    interface MachineSpecification_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHMachineSpecification_Mdl> {

        @Editor.Path("CompanyName")
        ValueProvider<TxtHMachineSpecification_Mdl, String> companyName();

        @Editor.Path("CommunicationMode")
        ValueProvider<TxtHMachineSpecification_Mdl, String> communicationMode();

        @Editor.Path("TransmissionMode")
        ValueProvider<TxtHMachineSpecification_Mdl, String> transmissionMode();

        @Editor.Path("Protocoll")
        ValueProvider<TxtHMachineSpecification_Mdl, String> protocoll();
    }

    final DmTextHelp<TxtHMachineSpecification_Mdl, MachineSpecification_PrpAcs> txtHModelNo = new DmTextHelp(GWT.create(MachineSpecification_PrpAcs.class), 10, true, false);

    private void initxtHMachineSpecification() {
        txtHModelNo.getDmGrid().addTextColumn(txtHModelNo.getDmGrid().getPropertyAccess().name(), "Model No.", 100, "");
        txtHModelNo.getDmGrid().addTextColumn(txtHModelNo.getDmGrid().getPropertyAccess().companyName(), "Company Name", 120, "");
        txtHModelNo.getDmGrid().addTextColumn(txtHModelNo.getDmGrid().getPropertyAccess().communicationMode(), "Comn. Mode", 80, "");
        txtHModelNo.getDmGrid().addTextColumn(txtHModelNo.getDmGrid().getPropertyAccess().transmissionMode(), "Trans. Mode", 100, "");
        txtHModelNo.getDmGrid().addTextColumn(txtHModelNo.getDmGrid().getPropertyAccess().protocoll(), "Protocol", 100, "");
        txtHModelNo.getDmGrid().initializeGrid(txtHModelNo.getDmGrid().getPropertyAccess().fieldHashCode(), 500, 0, 1);
    }

    interface GrdLabTestCodification_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLabTestCodification_Mdl> {

        @Editor.Path("MachineTestName")
        ValueProvider<GrdLabTestCodification_Mdl, String> machineTestName();

        @Editor.Path("ManualName")
        ValueProvider<GrdLabTestCodification_Mdl, String> manualName();

    }

    final DmGrid<GrdLabTestCodification_Mdl, GrdLabTestCodification_PrpAcs> grdLabTestCodification = new DmGrid(GWT.create(GrdLabTestCodification_PrpAcs.class));

    private Grid iniGrdPrinterMapping() {
        grdLabTestCodification.addRowNumberColumn();

        grdLabTestCodification.addTextColumn(grdLabTestCodification.getPropertyAccess().machineTestName(), "Machine Subtest Name", 260, "");
        grdLabTestCodification.addTextColumn(grdLabTestCodification.getPropertyAccess().manualName(), "Our Subtest Name", 280, "");
        grdLabTestCodification.initializeGrid(grdLabTestCodification.getPropertyAccess().fieldHashCode(), 570, 175);

        return grdLabTestCodification.getGrid();
    }

    public GrdEditMachineConfig_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        initxtHMachineSpecification();
        iniMachineName();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {

        FlowPanel flwTable, flwTr;
        CaptionPanel cPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "330px", null, null, DmPosition.CENTER, true);

        //=================================1st Row============================================    
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "80px", "left", null, DmPosition.NONE, true);

        cPnl = new CaptionPanel();
        cPnl.getElement().setAttribute("style", "border-right: none; border-left: none; border-top: none; width 100%; height: 72px");
        cPnl.add(entryArea());
        flwTr.add(cPnl);

        // flwTr.add(entryArea());
        flwTable.add(flwTr);

        //===================================2nd Row==========================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "61px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(12, Style.Unit.PX);

        cPnl = new CaptionPanel();
        cPnl.getElement().setAttribute("style", "border-right: none; border-left: none; border-top: none; width 100%; height: 52px");
        cPnl.add(configurationArea());
        flwTr.add(cPnl);

        // flwTr.add(configurationArea());
        flwTable.add(flwTr);

        //===================================3rd Row==========================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "170px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        flwTr.add(vl2.add(iniGrdPrinterMapping()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "80px", null, null, DmPosition.CENTER, true);

        //======================================1st Row ==================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Machine Name");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        //flwTd = new FlowPanel();
        DmClient.setStyle(flwDynamic, "27%", null, "left", null, DmPosition.NONE, false);
//        flwDynamic.add(vl.add(txtMachineName));
        flwTr.add(flwDynamic);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Model No.");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl2.add(txtHModelNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        // ======================================2nd Row ==================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Machine Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMachineCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Company Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtCompanyName);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Transmission Mode");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtTransmissionMode);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Communication Mode");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtCommunicationMode);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Protocol");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtProtocol);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel configurationArea() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "61px", null, null, DmPosition.CENTER, true);

        DmClient.setStyle(flwDynamicMain, "100%", null, "left", null, DmPosition.NONE, true);

        /*====================================< For TCP>====================================*/
        DmClient.setStyle(flwFrameTCP, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("IP Address");
        flwTd.add(lbl);
        flwFrameTCP.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl2.add(txtIPAddress));
        flwFrameTCP.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Port No.");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwFrameTCP.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl2.add(txtPortNo));
        flwFrameTCP.add(flwTd);

        // flwDynamicMain.add(flwFrameTCP);
        /*====================================< For ASTM>====================================*/
        DmClient.setStyle(flwFrameASTM, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("COM Port No.");
        flwTd.add(lbl);
        flwFrameASTM.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl2.add(txtComPortNo));
        flwFrameASTM.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Baud Rate");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwFrameASTM.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl2.add(txtBaudRate));

        flwFrameASTM.add(flwTd);

        //===============================< 2nd Row>======================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Stop Bit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl2.add(txtStopBit));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Data Bit");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl2.add(txtDataBit));
        flwTr.add(flwTd);

        flwFrameASTM.add(flwTr);
        //===============================< 3rd Row>======================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Parity");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl2.add(txtParityStatus));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Hardware Control");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl2.add(txtHardwareControl));
        flwTr.add(flwTd);

        flwFrameASTM.add(flwTr);

        //  flwDynamicMain.add(flwFrameASTM);
        flwTable.add(flwDynamicMain);

        return flwTable;
    }

}
