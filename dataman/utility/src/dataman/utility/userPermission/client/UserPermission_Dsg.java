package dataman.utility.userPermission.client;

import dataman.utility.userPermission.shared.TxtHUsers_Mdl;
import dataman.utility.userPermission.shared.GrdCostCenter_Mdl;
import dataman.utility.userPermission.shared.GrdVoucher_Mdl;
import dataman.utility.userPermission.shared.GrdMenu_Mdl;
import dataman.utility.userPermission.shared.GrdSite_Mdl;
import dataman.libraryMenu.self.shared.lmUtility;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

public abstract class UserPermission_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().USER_PERMISSION.caption;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    final Button btnClose = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final FlowPanel flwCopyPermission = new FlowPanel();
    final Button btnCopyPermission = new Button("...");
    final FlowPanel flwLinkedAccount = new FlowPanel();
    final Button btnLinkedAccount = new Button("...");

    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    final DmTextBox txtUserID = new DmTextBox();
    final DmTextBox txtDescription = new DmTextBox(50, false);
    final DmTextBox txtEmail = new DmTextBox(100, false);
    final DmTextBox txtAdministratorYN = new DmTextBox();
    final DmTextBox txtRole = new DmTextBox();
    final DmTextBox txtLinkedAccount = new DmTextBox();
    final DmTextBox txtCopyPermission = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHUser">
    interface DmTxtHUser_PrpAcs extends KeyField_T_PrpAcs<TxtHUsers_Mdl> {

        @Editor.Path("Description")
        ValueProvider<TxtHUsers_Mdl, String> description();
    }
    final DmTextHelp<TxtHUsers_Mdl, DmTxtHUser_PrpAcs> txtHUser = new DmTextHelp(GWT.create(DmTxtHUser_PrpAcs.class), 10);

    private void initxtHUser() {
        txtHUser.getDmGrid().addTextColumn(txtHUser.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHUser.getDmGrid().addTextColumn(txtHUser.getDmGrid().getPropertyAccess().description(), "Description", 250, "description");
        txtHUser.getDmGrid().initializeGrid(txtHUser.getDmGrid().getPropertyAccess().code(), 450, 0, 2);
        txtHUser.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public UserPermission_Dsg() {
        ini();
        initxtHUser();
    }

    private void ini() {
        VerticalPanel vrtInnerForm = new VerticalPanel();
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("560px");

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

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("40px");
        btnClose.setTitle("Close");
        flwClose.add(btnClose);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "530px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "70px", "left", null, DmPosition.NONE, false);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "460px", "left", null, DmPosition.NONE, false);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "80%", "60px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("User Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHUser));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("User ID");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUserID));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Administrator");
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAdministratorYN));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Role");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRole));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Description");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDescription));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Linked Account");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtLinkedAccount));
        hrtPnl.setCellWidth(txtLinkedAccount, "100%");
        DmClient.setStyle(btnLinkedAccount, "20px", "20px", null, null, DmPosition.NONE, true);
        btnLinkedAccount.addStyleName("dm-button-grad");
        btnLinkedAccount.setTitle("Linked Account");
        flwLinkedAccount.add(btnLinkedAccount);
        hrtPnl.add(flwLinkedAccount);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmail));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Copy Permission");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtCopyPermission));
        hrtPnl.setCellWidth(txtCopyPermission, "100%");
        DmClient.setStyle(btnCopyPermission, "20px", "20px", null, null, DmPosition.NONE, true);
        btnCopyPermission.addStyleName("dm-button-grad");
        btnCopyPermission.setTitle("Copy Permission");
        flwCopyPermission.add(btnCopyPermission);
        hrtPnl.add(flwCopyPermission);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "460px", null, null, DmPosition.NONE, true);

        tabPanel.add(vl.add(generalTab()), "General");
        tabPanel.add(vl.add(menuTabArea()), "Menu");
        tabPanel.add(vl.add(voucherTabArea()), "Vouchers");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel generalTab() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "430px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "25px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site Details :");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "25px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cost Center Details :");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "405px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "405px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdSite()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "405px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdCostCenter()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel menuTabArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "430px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "430px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", "430px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniNavigationGrd()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", "430px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdMenu()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel voucherTabArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "430px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "430px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(iniGrdVoucher()));
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Site Detail">
    interface GetSiteData extends KeyFieldHashCode_T_PrpAcs<GrdSite_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<GrdSite_Mdl, String> ShortName();
    }

    final DmGrid<GrdSite_Mdl, GetSiteData> grdSite = new DmGrid(GWT.create(GetSiteData.class));

    private Grid iniGrdSite() {
        grdSite.addRowNumberColumn();
        grdSite.addTextColumn(grdSite.getPropertyAccess().ShortName(), "Code", 80, "");
        grdSite.addTextColumn(grdSite.getPropertyAccess().name(), "Site Name", 500, "");
        grdSite.initializeGrid(grdSite.getPropertyAccess().fieldHashCode(), 630, 405);
        return grdSite.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Cost Center Detail">
    interface GetCostCenterData extends KeyFieldHashCode_T_PrpAcs<GrdCostCenter_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<GrdCostCenter_Mdl, String> ShortName();
    }

    final DmGrid<GrdCostCenter_Mdl, GetCostCenterData> GrdCostCenter = new DmGrid(GWT.create(GetCostCenterData.class));

    private Grid iniGrdCostCenter() {
        GrdCostCenter.addRowNumberColumn();
        GrdCostCenter.addTextColumn(GrdCostCenter.getPropertyAccess().ShortName(), "Code", 80, "");
        GrdCostCenter.addTextColumn(GrdCostCenter.getPropertyAccess().name(), "Cost Center Name", 500, "");
        GrdCostCenter.initializeGrid(GrdCostCenter.getPropertyAccess().fieldHashCode(), 630, 405);
        return GrdCostCenter.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Menu/Module">
    interface GrdMenu_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMenu_Mdl> {

        @Editor.Path("CuMnuCount")
        ValueProvider<GrdMenu_Mdl, String> cuMnuCount();

        @Editor.Path("CuIsAdd")
        ValueProvider<GrdMenu_Mdl, String> cuIsAdd();

        @Editor.Path("CuIsEdit")
        ValueProvider<GrdMenu_Mdl, String> cuIsEdit();

        @Editor.Path("CuIsDelete")
        ValueProvider<GrdMenu_Mdl, String> cuIsDelete();

        @Editor.Path("CuIsPrint")
        ValueProvider<GrdMenu_Mdl, String> cuIsPrint();

        @Editor.Path("CuIsEnable")
        ValueProvider<GrdMenu_Mdl, String> cuIsEnable();

        @Editor.Path("Vp_Days")
        ValueProvider<GrdMenu_Mdl, String> vp_Days();

        @Editor.Path("Sp_days")
        ValueProvider<GrdMenu_Mdl, String> sp_days();

        @Editor.Path("Dp_Days")
        ValueProvider<GrdMenu_Mdl, String> dp_Days();

        @Editor.Path("AllowValueUpto")
        ValueProvider<GrdMenu_Mdl, String> allowValueUpto();
    }

    final DmGrid<GrdMenu_Mdl, GrdMenu_PrpAcs> grdMenu = new DmGrid(GWT.create(GrdMenu_PrpAcs.class));

    private Grid iniGrdMenu() {
        ColumnConfig<GrdMenu_Mdl, String> view, add, edit, delete, print, enable, viewDays, saveDays, deleteDays, allowUpto;

        grdMenu.addRowNumberColumn();
        grdMenu.addTextColumn(grdMenu.getPropertyAccess().name(), "Name", 390, "");
        view = grdMenu.addTextColumn(grdMenu.getPropertyAccess().cuMnuCount(), "View", 40, "");
        add = grdMenu.addTextColumn(grdMenu.getPropertyAccess().cuIsAdd(), "Add", 40, "");
        edit = grdMenu.addTextColumn(grdMenu.getPropertyAccess().cuIsEdit(), "Edit", 40, "");
        delete = grdMenu.addTextColumn(grdMenu.getPropertyAccess().cuIsDelete(), "Delete", 45, "");
        print = grdMenu.addTextColumn(grdMenu.getPropertyAccess().cuIsPrint(), "Print", 40, "");
        enable = grdMenu.addTextColumn(grdMenu.getPropertyAccess().cuIsEnable(), "Enable", 45, "");

        viewDays = grdMenu.addTextColumn(grdMenu.getPropertyAccess().vp_Days(), "View Days", 65, "");
        saveDays = grdMenu.addTextColumn(grdMenu.getPropertyAccess().sp_days(), "Save Days", 65, "");
        deleteDays = grdMenu.addTextColumn(grdMenu.getPropertyAccess().dp_Days(), "Delete Days", 68, "");
        allowUpto = grdMenu.addTextColumn(grdMenu.getPropertyAccess().allowValueUpto(), "Allow Upto", 65, "");
        grdMenu.initializeGrid(grdMenu.getPropertyAccess().fieldHashCode(), 950, 430);

        grdMenu.getGrid().getView().setViewConfig(new GridViewConfig<GrdMenu_Mdl>() {
            @Override
            public String getColStyle(GrdMenu_Mdl model, ValueProvider<? super GrdMenu_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (colIndex == 1) {
                    if (!((DmShared.iNull(model.getMnuLevel()).intValue() == 4)
                            || (DmShared.iNull(model.getMnuLevel()).intValue() == 3 && DmShared.iNull(model.getBuIsEnable()).intValue() == 0))) {
                        styleClass += "dm-FontBold ";
                    }
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdMenu_Mdl model, int rowIndex) {
                return "";
            }
        });

        view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        view.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int baseCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getBuMnuCount()).intValue();
                int currentCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getCuMnuCount()).intValue();

                if (DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() == 4) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em;color:#ccc;'></span>");
                } else if (currentCount == baseCount && baseCount != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (currentCount < baseCount && currentCount != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        add.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        add.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int isAdd = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getBuIsAdd()).intValue();
                int baseCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getBuMnuCount()).intValue();
                int currentCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getCuIsAdd()).intValue();

                if (isAdd == 0 || DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() == 4) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em;color:#ccc;'></span>");
                } else if (currentCount == baseCount && baseCount != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (currentCount < baseCount && currentCount != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        edit.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int isEdit = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getBuIsEdit()).intValue();
                int baseCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getBuMnuCount()).intValue();
                int currentCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getCuIsEdit()).intValue();

                if (isEdit == 0 || DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() == 4) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em;color:#ccc;'></span>");
                } else if (currentCount == baseCount && baseCount != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (currentCount < baseCount && currentCount != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        edit.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        delete.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int isDelete = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getBuIsDelete()).intValue();
                int baseCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getBuMnuCount()).intValue();
                int currentCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getCuIsDelete()).intValue();

                if (isDelete == 0 || DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() == 4) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em;color:#ccc;'></span>");
                } else if (currentCount == baseCount && baseCount != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (currentCount < baseCount && currentCount != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        delete.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        print.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int isPrint = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getBuIsPrint()).intValue();
                int baseCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getBuMnuCount()).intValue();
                int currentCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getCuIsPrint()).intValue();

                if (isPrint == 0 || DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() == 4) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em;color:#ccc;'></span>");
                } else if (currentCount == baseCount && baseCount != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (currentCount < baseCount && currentCount != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        print.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        enable.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double baseCount = DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getBuIsEnable()).intValue();
                double currentCount = DmShared.vNull(grdMenu.getGrid().getStore().get(context.getIndex()).getCuIsEnable()).doubleValue();

                if (baseCount == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em;color:#ccc;'></span>");
                } else {
                    if (currentCount == baseCount && baseCount != 0) {
                        sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                    } else if (currentCount < baseCount && currentCount != 0) {
                        sb.appendHtmlConstant("<span class = 'fa fa-check-square' style='font-size:1.3em'></span>");
                    } else {
                        sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                    }
                }
            }
        });
        enable.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        viewDays.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() == 4) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em;color:#ccc;'></span>");
                } else if (DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() != 3
                        && DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getVp_Days()).intValue() != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-info-circle' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant(DmShared.iNull(value).intValue() > 0 ? DmShared.iNull(value).toString() : "");
                }
            }
        });
        viewDays.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        saveDays.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() == 4) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em;color:#ccc;'></span>");
                } else if (DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() != 3
                        && DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getSp_days()).intValue() != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-info-circle' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant(DmShared.iNull(value).intValue() > 0 ? DmShared.iNull(value).toString() : "");
                }
            }
        });
        saveDays.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        deleteDays.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() == 4) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em;color:#ccc;'></span>");
                } else if (DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() != 3
                        && DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getDp_Days()).intValue() != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-info-circle' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant(DmShared.iNull(value).intValue() > 0 ? DmShared.iNull(value).toString() : "");
                }
            }
        });
        deleteDays.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        allowUpto.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() == 4) {
                    sb.appendHtmlConstant("<span class = 'fa fa-ban' style='font-size:1.3em;color:#ccc;'></span>");
                } else if (DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getMnuLevel()).intValue() != 3
                        && DmShared.iNull(grdMenu.getGrid().getStore().get(context.getIndex()).getAllowValueUpto()).intValue() != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-info-circle' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant(DmShared.vNull(value).doubleValue() > 0 ? DmClient.vNull(value, "0.00") : "");
                }
            }
        });
        allowUpto.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        return grdMenu.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Menu/Module Navigation">
    interface GetRootData_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMenu_Mdl> {
    }
    final DmGrid<GrdMenu_Mdl, GetRootData_PrpAcs> grdNavigation = new DmGrid(GWT.create(GetRootData_PrpAcs.class));

    private Grid iniNavigationGrd() {
        grdNavigation.addTextColumn(grdNavigation.getPropertyAccess().name(), "Navigation", 292, "");
        grdNavigation.initializeGrid(grdNavigation.getPropertyAccess().fieldHashCode(), 315, 430);
        grdNavigation.getGrid().getView().setViewConfig(new GridViewConfig<GrdMenu_Mdl>() {

            @Override
            public String getColStyle(GrdMenu_Mdl model, ValueProvider<? super GrdMenu_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (DmShared.xNull(model.getSharedKeyFieldCode()).equals("Root")) {
                    styleClass += "dm-FontBold ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdMenu_Mdl model, int rowIndex) {
                return "";
            }
        });
        return grdNavigation.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Voucher Detail">
    interface GetVouchersData_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdVoucher_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<GrdVoucher_Mdl, String> shortName();

        @Editor.Path("Category")
        ValueProvider<GrdVoucher_Mdl, String> category();

        @Editor.Path("ViewPrevious")
        ValueProvider<GrdVoucher_Mdl, String> viewPrevious();

        @Editor.Path("SavePrevious")
        ValueProvider<GrdVoucher_Mdl, String> savePrevious();

        @Editor.Path("DeletePrevious")
        ValueProvider<GrdVoucher_Mdl, String> deletePrevious();

        @Editor.Path("AllowValueUpto")
        ValueProvider<GrdVoucher_Mdl, String> allowValueUpto();

        @Editor.Path("AuditAllowName")
        ValueProvider<GrdVoucher_Mdl, String> auditAllowName();
    }

    final DmGrid<GrdVoucher_Mdl, GetVouchersData_PrpAcs> grdVouchersDetail = new DmGrid(GWT.create(GetVouchersData_PrpAcs.class));

    private Grid iniGrdVoucher() {
        grdVouchersDetail.addRowNumberColumn();
        grdVouchersDetail.addTextColumn(grdVouchersDetail.getPropertyAccess().name(), "Description", 515, "");
        grdVouchersDetail.addTextColumn(grdVouchersDetail.getPropertyAccess().shortName(), "Short Name", 100, "");
        grdVouchersDetail.addTextColumn(grdVouchersDetail.getPropertyAccess().category(), "Category", 150, "");
        grdVouchersDetail.addTextColumn(grdVouchersDetail.getPropertyAccess().viewPrevious(), "View Previous", 100, "");
        grdVouchersDetail.addTextColumn(grdVouchersDetail.getPropertyAccess().savePrevious(), "Save Previous", 100, "");
        grdVouchersDetail.addTextColumn(grdVouchersDetail.getPropertyAccess().deletePrevious(), "Delete Previous", 100, "");
        grdVouchersDetail.addTextColumn(grdVouchersDetail.getPropertyAccess().allowValueUpto(), "Allow Value Upto", 100, "");
        grdVouchersDetail.addTextColumn(grdVouchersDetail.getPropertyAccess().auditAllowName(), "Allow", 50, "");
        grdVouchersDetail.initializeGrid(grdVouchersDetail.getPropertyAccess().fieldHashCode(), 1265, 430);
        return grdVouchersDetail.getGrid();
    }
    //</editor-fold>
}
