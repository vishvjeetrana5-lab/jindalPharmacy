package aarogya.master.additionalUserPermission.client;

import aarogya.master.additionalUserPermission.shared.GrdAdditionalUserPermission_Mdl;
import aarogya.master.additionalUserPermission.shared.SearchValue_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

abstract class AdditionalUserPermission_Dsg {

    final String widgetName = "Additional User Permission";
    public final static String createUserBlog = "Create User";
    public final static String additionalPermissionBlog = "Additional Permission";
    public final static String userPermissionBlog = "User Permission";
    public final static String manageUserRoleBlog = "Manage User Role";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwManageUserRole = new FlowPanel();
    final Button btnManageUserRole = new Button();
    final FlowPanel flwCreateUser = new FlowPanel();
    final Button btnCreateUser = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="grdUserDepartment">
    interface GrdUserDepartmentMapping_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAdditionalUserPermission_Mdl> {

        @Editor.Path("UserDescription")
        ValueProvider<GrdAdditionalUserPermission_Mdl, String> userDescription();

        @Editor.Path("LinkedAcName")
        ValueProvider<GrdAdditionalUserPermission_Mdl, String> linkedAcName();

        @Editor.Path("NursingStationName")
        ValueProvider<GrdAdditionalUserPermission_Mdl, String> nursingStationName();

        @Editor.Path("DepartmentName")
        ValueProvider<GrdAdditionalUserPermission_Mdl, String> departmentName();

        @Editor.Path("AllowDepartmentName")
        ValueProvider<GrdAdditionalUserPermission_Mdl, String> departmentAllowedName();
        
        @Editor.Path("AllowLocationName")
        ValueProvider<GrdAdditionalUserPermission_Mdl, String> allowLocationName();

        @Editor.Path("ServiceName")
        ValueProvider<GrdAdditionalUserPermission_Mdl, String> serviceName();

        @Editor.Path("ServiceCatName")
        ValueProvider<GrdAdditionalUserPermission_Mdl, String> serviceCatName();

    }
    final DmGridPaging<GrdAdditionalUserPermission_Mdl, GrdUserDepartmentMapping_PrpAcs, SearchValue_Mdl> grdUserDepartment = new DmGridPaging(GWT.create(GrdUserDepartmentMapping_PrpAcs.class), 23, false);

    private void iniGrdUserDepartmeMapping() {

        grdUserDepartment.getDmGrid().addRowNumberColumn();
        grdUserDepartment.getDmGrid().addTextColumn(grdUserDepartment.getDmGrid().getPropertyAccess().name(), "User Name", 80, "user_Name");
        grdUserDepartment.getDmGrid().addTextColumn(grdUserDepartment.getDmGrid().getPropertyAccess().userDescription(), "User Description", 150, "description");
        grdUserDepartment.getDmGrid().addTextColumn(grdUserDepartment.getDmGrid().getPropertyAccess().linkedAcName(), "Linked A/c", 150, "linkedAcName");
        grdUserDepartment.getDmGrid().addTextColumn(grdUserDepartment.getDmGrid().getPropertyAccess().nursingStationName(), "Nursing Station", 150, "nursingStationName");
        grdUserDepartment.getDmGrid().addTextColumn(grdUserDepartment.getDmGrid().getPropertyAccess().departmentName(), "Department Name", 150, "departmentName");
        grdUserDepartment.getDmGrid().addTextColumn(grdUserDepartment.getDmGrid().getPropertyAccess().departmentAllowedName(), "Allowed Department", 150, "");
        grdUserDepartment.getDmGrid().addTextColumn(grdUserDepartment.getDmGrid().getPropertyAccess().allowLocationName(), "Allowed Location", 120, "");
        grdUserDepartment.getDmGrid().addTextColumn(grdUserDepartment.getDmGrid().getPropertyAccess().serviceCatName(), "Service Category", 150, "");
        grdUserDepartment.getDmGrid().addTextColumn(grdUserDepartment.getDmGrid().getPropertyAccess().serviceName(), "Service Name", 150, "");
        grdUserDepartment.getDmGrid().initializeGrid(grdUserDepartment.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 536);
        grdUserDepartment.getHelper().addSort("user_Name", DmSortType.ASC);

    }
    //</editor-fold>

    public AdditionalUserPermission_Dsg() {
        ini();
        iniGrdUserDepartmeMapping();
    }

    private void ini() {

        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("549px");
//        vrtInnerForm.add(toolContainer);
        vrtInnerForm.add(iniToolContorls());

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Create User");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-user");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCreateUser.setTitle("Create User");
        btnCreateUser.addStyleName("dm-top-control-btnStyle");
        btnCreateUser.getElement().appendChild(aBtn.getElement());
        btnCreateUser.getElement().appendChild(label.getElement());
        btnCreateUser.setWidth("90px");
        flwCreateUser.add(btnCreateUser);

        label = new InlineLabel("Manage User Role");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-group");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnManageUserRole.setTitle("Manage User Role");
        btnManageUserRole.addStyleName("dm-top-control-btnStyle");
        btnManageUserRole.getElement().appendChild(aBtn.getElement());
        btnManageUserRole.getElement().appendChild(label.getElement());
        btnManageUserRole.setWidth("140px");
        flwManageUserRole.add(btnManageUserRole);

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.setTitle("Refresh");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("75px");
        flwRefresh.add(btnRefresh);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close: (Escap)");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "522px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(topArea()));

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "480px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(grdUserDepartment));
        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        HorizontalPanel hrtPanel;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "25px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().getStyle().setBackgroundColor("lightgray");
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSearchValue));
        txtSearchValue.setHeight("21px");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "right", null, DmPosition.NONE, false);
        hrtPanel = new HorizontalPanel();
        hrtPanel.addStyleName("dm-StripLabel");
        hrtPanel.getElement().getStyle().setBackgroundColor("lightgray");
        hrtPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);

        flwTd.add(hrtPanel);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

}
