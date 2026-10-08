package aarogya.master.additionalUserPermission.client;

import aarogya.master.additionalUserPermission.shared.GrdManageUserRole_Mdl;
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
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

abstract class ManageUserRoleGridEdit_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHRoleName">
    interface txtHRole_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> description();
    }
    final DmTextHelp<KeyFieldExtended, txtHRole_PrpAcs> txtHRoleName = new DmTextHelp(GWT.create(txtHRole_PrpAcs.class), 10);

    private void iniTxtHRoleName() {
        txtHRoleName.getDmGrid().addTextColumn(txtHRoleName.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHRoleName.getDmGrid().addTextColumn(txtHRoleName.getDmGrid().getPropertyAccess().description(), "Description", 100, "description");
        txtHRoleName.getDmGrid().initializeGrid(txtHRoleName.getDmGrid().getPropertyAccess().code(), 330, 0, 1);

        txtHRoleName.getHelper().addSort("NAME", DmSortType.ASC);
    }
//</editor-fold>

    public ManageUserRoleGridEdit_Dsg() {
        ini();
        iniTxtHRoleName();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");
        popupPanel.setHeight("306px");
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

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
        flwClose.add(btnClose);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "275px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(entryArea()));
        flwTable.add(flwTr);
        /*====================================< 2Nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "250px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdUser()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Role Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRoleName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdUser">
    interface GrdUser_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdManageUserRole_Mdl> {

        @Editor.Path("Description")
        ValueProvider<GrdManageUserRole_Mdl, String> description();

    }
    final DmGrid<GrdManageUserRole_Mdl, GrdUser_PrpAcs> grdUserDetail = new DmGrid(GWT.create(GrdUser_PrpAcs.class));

    private Grid iniGrdUser() {
        grdUserDetail.addRowNumberColumn();
        grdUserDetail.addTextColumn(grdUserDetail.getPropertyAccess().name(), "User Name", 1, "");
        grdUserDetail.addTextColumn(grdUserDetail.getPropertyAccess().description(), "Description", 250, "");
        grdUserDetail.initializeGrid(grdUserDetail.getPropertyAccess().fieldHashCode(), 522, 250, 2);

        return grdUserDetail.getGrid();
    }
    //</editor-fold>

}
