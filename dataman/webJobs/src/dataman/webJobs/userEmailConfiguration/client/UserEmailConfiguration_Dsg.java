package dataman.webJobs.userEmailConfiguration.client;

import dataman.webJobs.userEmailConfiguration.shared.TxtHUsers_Mdl;
import dataman.webJobs.userEmailConfiguration.shared.GrdUserEmaiEdit_Mdl;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmWebJobs;

public abstract class UserEmailConfiguration_Dsg extends IsWidgetExtended {

    final String widgetName = new lmWebJobs().USER_EMAIL_CONFIGURATION.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String userEditableBlog = "User Editable";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final Button btnClose = new Button();

    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();

    final Button btnRepairDb = new Button("Repair Db");

    //<editor-fold defaultstate="collapsed" desc="UserHelp">
    interface DmTxtHUser_PrpAcs extends KeyField_T_PrpAcs<TxtHUsers_Mdl> {

        @Editor.Path("Description")
        ValueProvider<TxtHUsers_Mdl, String> description();

    }
    final DmTextHelp<TxtHUsers_Mdl, DmTxtHUser_PrpAcs> txtHUser = new DmTextHelp(GWT.create(DmTxtHUser_PrpAcs.class), 10);

    public void initxtHUser() {
        txtHUser.getDmGrid().addTextColumn(txtHUser.getDmGrid().getPropertyAccess().name(), "Name", 175, "Name");
        txtHUser.getDmGrid().addTextColumn(txtHUser.getDmGrid().getPropertyAccess().description(), "Description", 175, "Description");
        txtHUser.getDmGrid().initializeGrid(txtHUser.getDmGrid().getPropertyAccess().code(), 350, 0, 1); //244

        txtHUser.getHelper().addSort("Name", DmSortType.ASC, "User_Name");
        txtHUser.getHelper().addSort("Description", DmSortType.ASC);
    }
//</editor-fold>

    public UserEmailConfiguration_Dsg() {
        ini();
        initxtHUser();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("390px");

        vrtInnerForm.add(iniToolControl());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "360px", null, null, DmPosition.CENTER, true);
        FlowPanel flwTr;

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "330px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(inigrdDetail()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "30%", "20px", null, null, DmPosition.CENTER, true);
        FlowPanel flwTr, flwTd;
        DmLabel lbl;

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("User Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHUser));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="GrdDetail">
    interface Grd_PrpAcs extends KeyField_T_PrpAcs<GrdUserEmaiEdit_Mdl> {

        @Editor.Path("Email")
        ValueProvider<GrdUserEmaiEdit_Mdl, String> email();

        @Editor.Path("Cc")
        ValueProvider<GrdUserEmaiEdit_Mdl, String> cc();

        @Editor.Path("Bcc")
        ValueProvider<GrdUserEmaiEdit_Mdl, String> bcc();

    }

    final DmGrid<GrdUserEmaiEdit_Mdl, Grd_PrpAcs> grdEmail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid inigrdDetail() {
        grdEmail.addRowNumberColumn();
        grdEmail.addTextColumn(grdEmail.getPropertyAccess().name(), "Product License To", 350, "");
        grdEmail.addTextColumn(grdEmail.getPropertyAccess().email(), "Email", 200, "");
        grdEmail.addTextColumn(grdEmail.getPropertyAccess().cc(), "CC", 200, "");
        grdEmail.addTextColumn(grdEmail.getPropertyAccess().bcc(), "BCC", 200, "");
        grdEmail.initializeGrid(grdEmail.getPropertyAccess().code(), 1280, 332, 2);

        return grdEmail.getGrid();
    }
    //</editor-fold>

    private DmToolContainer iniToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(btnClose);
        toolContainer.setTitle(widgetName);
        toolContainer.addGap();
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

}
