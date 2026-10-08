package dataman.utility.userMaster.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.utility.userMaster.shared.GrdLinkedUser_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.utility.userMaster.shared.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

public abstract class UserMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().USER_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmSuggestBox txtUserName = new DmSuggestBox(10, 10, true, "User_Name", DmDataType.STRING);
    final DmTextBox txtUserID = new DmTextBox();
    final DmTextBox txtDescription = new DmTextBox(100, false);
    final DmTextBoxNumeric txtLockTimeOut = new DmTextBoxNumeric(0, false, 7, false);
    final DmTextBox txtEmail = new DmTextBox(100, true);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);
    final Button btnUserPermission = new Button("User Permission");

    final FlowPanel flwBottomArea = new FlowPanel();
    final FlowPanel flwLinkedUser = new FlowPanel();

    final Menu menu = new Menu();
    MenuItem mnuResetPassword;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="txtHAdministratorYN">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAdministratorYN = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3);

    private void initxtHAdministratorYN() {
        txtHAdministratorYN.getDmGrid().addTextColumn(txtHAdministratorYN.getDmGrid().getPropertyAccess().name(), "Name", 100, "Name");
        txtHAdministratorYN.getDmGrid().initializeGrid(txtHAdministratorYN.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3);

    private void initxtHNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Nature", 100, "name");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHNature.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3);

    private void initxtHActiveYN() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 100, "Name");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRole">
    interface txtHRole_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> description();
    }
    final DmTextHelp<KeyFieldExtended, txtHRole_PrpAcs> txtHRole = new DmTextHelp(GWT.create(txtHRole_PrpAcs.class), 10);

    private void initxtHRole() {
        txtHRole.getDmGrid().addTextColumn(txtHRole.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHRole.getDmGrid().addTextColumn(txtHRole.getDmGrid().getPropertyAccess().description(), "Description", 100, "description");
        txtHRole.getDmGrid().initializeGrid(txtHRole.getDmGrid().getPropertyAccess().code(), 250, 0, 1); //244
        txtHRole.getHelper().addSort("Name", DmSortType.ASC, "User_Name");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
     final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Department", 100, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHDepartment.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Role")
        ValueProvider<Find_Mdl, String> role();

        @Editor.Path("Email")
        ValueProvider<Find_Mdl, String> email();

        @Editor.Path("Description")
        ValueProvider<Find_Mdl, String> description();
        
        @Editor.Path("DepartmentName")
        ValueProvider<Find_Mdl, String> departmentName();

        @Editor.Path("IsActive")
        ValueProvider<Find_Mdl, String> isActive();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 23);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "User Name", 200, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().description(), "Description", 300, "description");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().role(), "Role", 200, "User_Role");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().email(), "Email", 300, "eMail");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().departmentName(), "Department", 200, "departmentName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isActive(), "Active", 45, "isActive");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 200, 1);

        dmFind.getHelper().addSort("Name", DmSortType.ASC);
        dmFind.getHelper().addSort("User_Role", DmSortType.ASC);
        dmFind.getHelper().addSort("eMail", DmSortType.ASC);
    }
    //</editor-fold>

    public UserMaster_Dsg() {
        ini();
        iniMenu();
        initxtHActiveYN();
        initxtHAdministratorYN();
        initxtHRole();
        iniFind();
        initxtHNature();
        iniTxtHDepartment();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private Menu iniMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        mnuResetPassword = new MenuItem("Reset Password", selectionHandler);
        menu.add(mnuResetPassword);

        return menu;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "85%", "500px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "85px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        DmLabel lblTemp = new DmLabel("Linked User");
        lblTemp.setHeight("15px");
        lblTemp.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTr.add(lblTemp);
        flwLinkedUser.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "380px", "left", null, DmPosition.NONE, true);
        flwLinkedUser.add(vl.add(createLinkedUserGrid()));
        flwBottomArea.add(flwLinkedUser);
        flwTr.add(flwBottomArea);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "85px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("User Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUserName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("User ID");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUserID));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Administrator");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAdministratorYN));
        txtHAdministratorYN.setMandatory(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Role");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRole));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Description");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDescription));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Lock Time Out");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLockTimeOut));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Nature");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNature));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Email");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmail));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Mobile");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Department");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        txtHDepartment.setMandatory(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Is Active");
//        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHActive));
        txtHActive.setMandatory(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(63, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnUserPermission);
        btnUserPermission.setHeight("20px");
        btnUserPermission.setWidth("100%");
        btnUserPermission.addStyleName("dm-button-grad");
        btnUserPermission.setVisible(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Linked User">
    interface GridLinkedUserData extends KeyFieldHashCode_T_PrpAcs<GrdLinkedUser_Mdl> {

        @Editor.Path("Description")
        ValueProvider<GrdLinkedUser_Mdl, String> Description();
    }

    final DmGrid<GrdLinkedUser_Mdl, GridLinkedUserData> GrdLinkedUser = new DmGrid(com.google.gwt.core.shared.GWT.create(GridLinkedUserData.class));

    private Grid createLinkedUserGrid() {
        GrdLinkedUser.addRowNumberColumn();
        GrdLinkedUser.addTextColumn(GrdLinkedUser.getPropertyAccess().name(), "Name", 225, "");
        GrdLinkedUser.addTextColumn(GrdLinkedUser.getPropertyAccess().Description(), "Description", 800, "");
        GrdLinkedUser.initializeGrid(GrdLinkedUser.getPropertyAccess().fieldHashCode(), 1085, 390);
        return GrdLinkedUser.getGrid();
    }
    //</editor-fold>
}
