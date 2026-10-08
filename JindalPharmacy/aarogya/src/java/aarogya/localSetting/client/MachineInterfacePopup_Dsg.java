package aarogya.localSetting.client;

import aarogya.localSetting.shared.LabMachineConfiguration_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class MachineInterfacePopup_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnSave = new Button();
    final Button btnDelete = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwDelete = new FlowPanel();
    public String popupTitle = "";
    final Button btnCancel = new Button();
    final Button btnAssign = new Button();
    final Button btnCreate = new Button();
    final FlowPanel flwAssign = new FlowPanel();
    final FlowPanel flwCreate = new FlowPanel();

    interface GrdLabMachineConfiguration_PrpAcs extends KeyFieldHashCode_T_PrpAcs<LabMachineConfiguration_Mdl> {
        //interface GrdLabMachineConfiguration_PrpAcs extends KeyField_T_PrpAcs<LabMachineConfiguration_Mdl> {

        @Editor.Path("MachineName")
        ValueProvider<LabMachineConfiguration_Mdl, String> machineName();

        @Editor.Path("MachineCode")
        ValueProvider<LabMachineConfiguration_Mdl, String> machineCode();

        @Editor.Path("ModelNo")
        ValueProvider<LabMachineConfiguration_Mdl, String> modelNo();

        @Editor.Path("CompanyName")
        ValueProvider<LabMachineConfiguration_Mdl, String> companyName();

        @Editor.Path("CommunicationMode")
        ValueProvider<LabMachineConfiguration_Mdl, String> communicationMode();

        @Editor.Path("TransmissionMode")
        ValueProvider<LabMachineConfiguration_Mdl, String> transmissionMode();

        @Editor.Path("Protocoll")
        ValueProvider<LabMachineConfiguration_Mdl, String> protocoll();

    }

    final DmGrid<LabMachineConfiguration_Mdl, GrdLabMachineConfiguration_PrpAcs> grdLabMachineConfiguration = new DmGrid(GWT.create(GrdLabMachineConfiguration_PrpAcs.class));

    private Grid iniGrdMachineInterface() {
        grdLabMachineConfiguration.addRowNumberColumn();

        grdLabMachineConfiguration.addTextColumn(grdLabMachineConfiguration.getPropertyAccess().machineName(), "Machine Name", 145, "");
        grdLabMachineConfiguration.addTextColumn(grdLabMachineConfiguration.getPropertyAccess().machineCode(), "Machine Code", 293, "");
        grdLabMachineConfiguration.addTextColumn(grdLabMachineConfiguration.getPropertyAccess().modelNo(), "Model No.", 100, "");
        grdLabMachineConfiguration.addTextColumn(grdLabMachineConfiguration.getPropertyAccess().companyName(), "Company Name", 135, "");
        grdLabMachineConfiguration.addTextColumn(grdLabMachineConfiguration.getPropertyAccess().communicationMode(), "Comn. Mode", 80, "");
        grdLabMachineConfiguration.addTextColumn(grdLabMachineConfiguration.getPropertyAccess().transmissionMode(), "Trans. Mode", 100, "");
        grdLabMachineConfiguration.addTextColumn(grdLabMachineConfiguration.getPropertyAccess().protocoll(), "Protocol", 70, "");

        grdLabMachineConfiguration.initializeGrid(grdLabMachineConfiguration.getPropertyAccess().fieldHashCode(), 950, 350);
//        grdLabMachineConfiguration.initializeGrid(grdLabMachineConfiguration.getPropertyAccess().code(), 950, 350);

        return grdLabMachineConfiguration.getGrid();
    }

    public MachineInterfacePopup_Dsg() {

        ini();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("950px");
        popupPanel.setHeight("350px");
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

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.setTitle("Save");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");
        flwOk.add(vl.add(btnSave));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        label = new InlineLabel("Assign");;
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnAssign.addStyleName("dm-top-control-btnStyle");
        btnAssign.setTitle("Assign");
        btnAssign.getElement().appendChild(aBtn.getElement());
        btnAssign.getElement().appendChild(label.getElement());
        btnAssign.setWidth("79px");
        flwAssign.add(btnAssign);

        label = new InlineLabel("Create");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plus");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCreate.addStyleName("dm-top-control-btnStyle");
        btnCreate.setTitle("Create");
        btnCreate.getElement().appendChild(aBtn.getElement());
        btnCreate.getElement().appendChild(label.getElement());
        btnCreate.setWidth("79px");
        flwCreate.add(btnCreate);

        label = new InlineLabel("Delete");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.setTitle("Delete");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.getElement().appendChild(label.getElement());
        btnDelete.setWidth("79px");
        flwDelete.add(btnDelete);

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwAssign);
        toolContainer.addWidget(flwCreate);
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(flwCancel);

        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {

        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "350px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTr.add(iniGrdMachineInterface());
        flwTable.add(flwTr);

        return flwTable;
    }

}
