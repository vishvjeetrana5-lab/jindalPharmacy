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
import dataman.utility.userPermission.shared.TxtHLinkedAccount_Mdl;

abstract class LinkedAccountEdit_Dsg {

    public final static String widgetName = "Linked Account";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHLinkedAccount">
    interface DmLinkedAccount_PrpAcs extends KeyField_T_PrpAcs<TxtHLinkedAccount_Mdl> {

        @Editor.Path("GroupName")
        ValueProvider<TxtHLinkedAccount_Mdl, String> groupName();

        @Editor.Path("City")
        ValueProvider<TxtHLinkedAccount_Mdl, String> city();

        @Editor.Path("CodeValue")
        ValueProvider<TxtHLinkedAccount_Mdl, String> codeValue();

    }
    final DmTextHelp<TxtHLinkedAccount_Mdl, DmLinkedAccount_PrpAcs> txtHLinkedAccount = new DmTextHelp(GWT.create(DmLinkedAccount_PrpAcs.class), 10);

    private void initxtHLinkedAccount() {
        txtHLinkedAccount.getDmGrid().addTextColumn(txtHLinkedAccount.getDmGrid().getPropertyAccess().name(), "Name", 200, "SGName");
        txtHLinkedAccount.getDmGrid().addTextColumn(txtHLinkedAccount.getDmGrid().getPropertyAccess().codeValue(), "Code", 120, "ManualCode");
        txtHLinkedAccount.getDmGrid().addTextColumn(txtHLinkedAccount.getDmGrid().getPropertyAccess().groupName(), "Group Name", 200, "GName");
        txtHLinkedAccount.getDmGrid().addTextColumn(txtHLinkedAccount.getDmGrid().getPropertyAccess().city(), "City", 150, "CityName");
        txtHLinkedAccount.getDmGrid().initializeGrid(txtHLinkedAccount.getDmGrid().getPropertyAccess().code(), 700, 0, 1); //244

        txtHLinkedAccount.getHelper().addSort("SGName", DmSortType.ASC);
    }
//</editor-fold>

    public LinkedAccountEdit_Dsg() {
        ini();
        initxtHLinkedAccount();
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
        lbl = new DmLabel("Linked Account");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLinkedAccount));
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
        flwCancel.add(vl.add(btnCancel));

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }
}
