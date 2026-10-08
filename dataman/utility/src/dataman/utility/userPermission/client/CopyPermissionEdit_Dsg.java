package dataman.utility.userPermission.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.utility.userPermission.shared.TxtHUsers_Mdl;

abstract class CopyPermissionEdit_Dsg {

    public final static String widgetName = "Copy Permission";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHUser">
    interface txtHCopyUser_PrpAcs extends KeyField_T_PrpAcs<TxtHUsers_Mdl> {

        @Editor.Path("Description")
        ValueProvider<TxtHUsers_Mdl, String> description();

    }
    final DmTextHelp<TxtHUsers_Mdl, txtHCopyUser_PrpAcs> txtHUserCP = new DmTextHelp(GWT.create(txtHCopyUser_PrpAcs.class), 10, true);

    private void iniTxtHCopyUser() {
        txtHUserCP.getDmGrid().addTextColumn(txtHUserCP.getDmGrid().getPropertyAccess().name(), "Name", 200, "Name");
        txtHUserCP.getDmGrid().addTextColumn(txtHUserCP.getDmGrid().getPropertyAccess().description(), "Description", 250, "Description");
        txtHUserCP.getDmGrid().initializeGrid(txtHUserCP.getDmGrid().getPropertyAccess().code(), 450, 0, 1); //244

        txtHUserCP.getHelper().addSort("Name", DmSortType.ASC, "User_Name");
        txtHUserCP.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>

    public CopyPermissionEdit_Dsg() {
        ini();
        iniTxtHCopyUser();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("300px");
        popupPanel.setHeight("60px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);

    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.setTitle("Ok");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("40px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Copy Permission");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHUserCP));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
