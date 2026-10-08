package dataman.utility.copyUserPermission.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.libraryMenu.self.shared.lmUtility;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.utility.copyUserPermission.shared.GrdCopyPermissionFromTo_Mdl;
import dataman.utility.copyUserPermission.shared.TxtHCompany_Mdl;

public abstract class CopyUserPermission_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().COPY_USER_PERMISSION.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();

    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtFinanceYearFrom = new DmTextBox(9, false);
    final DmTextBoxNumeric txtPrefixFrom = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBox txtFinanceYearTo = new DmTextBox(9, false);
    final DmTextBoxNumeric txtPrefixTo = new DmTextBoxNumeric(0, false, 2, false);

    final Button btnSave = new Button();
    final Button btnClose = new Button();
    final Button btnCopy = new Button();
    final InlineLabel chkAllFromGrd = new InlineLabel();
    final InlineLabel chkAllToGrd = new InlineLabel();

    //<editor-fold defaultstate="collapsed" desc="txtHFromCompany">
    interface TxtHFromCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHCompany_Mdl> {

        @Editor.Path("Prefix")
        ValueProvider<TxtHCompany_Mdl, String> prefix();

        @Editor.Path("StartDate")
        ValueProvider<TxtHCompany_Mdl, String> startDate();

        @Editor.Path("EndDate")
        ValueProvider<TxtHCompany_Mdl, String> endDate();
    }
    final DmTextHelp<TxtHCompany_Mdl, TxtHFromCompany_PrpAcs> txtHFromCompany = new DmTextHelp(GWT.create(TxtHFromCompany_PrpAcs.class), 7, true);

    public void iniTxtHFromCompany() {
        txtHFromCompany.getDmGrid().addTextColumn(txtHFromCompany.getDmGrid().getPropertyAccess().name(), "Name", 230, "Comp_Name");
        txtHFromCompany.getDmGrid().addTextColumn(txtHFromCompany.getDmGrid().getPropertyAccess().prefix(), "Vr. Prefix", 80, "V_Prefix");
        txtHFromCompany.getDmGrid().addTextColumn(txtHFromCompany.getDmGrid().getPropertyAccess().startDate(), "Start Date", 80, "Start_Dt");
        txtHFromCompany.getDmGrid().addTextColumn(txtHFromCompany.getDmGrid().getPropertyAccess().endDate(), "End Date", 80, "End_Dt");
        txtHFromCompany.getDmGrid().initializeGrid(txtHFromCompany.getDmGrid().getPropertyAccess().code(), 500, 0, 1); //244
        txtHFromCompany.getHelper().addSort("Comp_Name", DmSortType.ASC);
        txtHFromCompany.getHelper().addSort("sname", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHToCompany">
    interface TxtHToCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHCompany_Mdl> {

        @Editor.Path("Prefix")
        ValueProvider<TxtHCompany_Mdl, String> prefix();

        @Editor.Path("StartDate")
        ValueProvider<TxtHCompany_Mdl, String> startDate();

        @Editor.Path("EndDate")
        ValueProvider<TxtHCompany_Mdl, String> endDate();
    }
    final DmTextHelp<TxtHCompany_Mdl, TxtHToCompany_PrpAcs> txtHToCompany = new DmTextHelp(GWT.create(TxtHToCompany_PrpAcs.class), 7, true);

    public void iniTxtHToCompany() {
        txtHToCompany.getDmGrid().addTextColumn(txtHToCompany.getDmGrid().getPropertyAccess().name(), "Name", 230, "Comp_Name");
        txtHToCompany.getDmGrid().addTextColumn(txtHToCompany.getDmGrid().getPropertyAccess().prefix(), "Vr. Prefix", 80, "V_Prefix");
        txtHToCompany.getDmGrid().addTextColumn(txtHToCompany.getDmGrid().getPropertyAccess().startDate(), "Start Date", 80, "Start_Dt");
        txtHToCompany.getDmGrid().addTextColumn(txtHToCompany.getDmGrid().getPropertyAccess().endDate(), "End Date", 80, "End_Dt");
        txtHToCompany.getDmGrid().initializeGrid(txtHToCompany.getDmGrid().getPropertyAccess().code(), 500, 0, 1); //244
        txtHToCompany.getHelper().addSort("Comp_Name", DmSortType.ASC);
        txtHToCompany.getHelper().addSort("sname", DmSortType.ASC);
    }

    //</editor-fold>
    public CopyUserPermission_Dsg() {
        ini();
        iniTxtHFromCompany();
        iniTxtHToCompany();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("502px");
        vrtInnerForm.add(vl.add(iniToolControl()));
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        // aBtn.getElement().getStyle().setColor("black");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.setTitle("Save");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Close");
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        // aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        label = new InlineLabel("Copy");
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-files-o");
        // aBtn.getElement().getStyle().setColor("black");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCopy.addStyleName("dm-top-control-btnStyle");
        btnCopy.setTitle("Copy");
        btnCopy.getElement().appendChild(aBtn.getElement());
        btnCopy.getElement().appendChild(label.getElement());
        btnCopy.setWidth("70px");

        toolContainer.addWidget(vl.add(btnSave));
        toolContainer.addWidget(vl.add(btnCopy));
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();
        return toolContainer;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "475px", null, null, DmPosition.CENTER, true);
        FlowPanel flwTr;
        FlowPanel flwTd;

        ///////////////////////// TR1 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "210px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", "210px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(leftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", "210px", "left", null, DmPosition.NONE, false);
        DmLabel lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", "210px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(rightArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        ///////////////////////// TR2 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "265px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(createToGrid()));
        flwTd.add(chkAllToGrd);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        chkAllToGrd.addStyleName("fa fa-square-o");
        chkAllToGrd.getElement().setAttribute("style", "position:absolute; top:216px; left:31px; z-index:10;cursor:pointer;font-size:1.1em;");
        chkAllToGrd.setTitle("Delete all");

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "210px", null, null, DmPosition.CENTER, true);
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "10px", null, null, DmPosition.CENTER, false);
        lblTemp = new DmLabel("From Company");
        lblTemp.getElement().getStyle().setHeight(13, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        ///////////////////////// TR1 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("From Company");
        flwTd.add(lblTemp);
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHFromCompany));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR3 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(10, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Voucher Prefix");
        flwTd.add(lblTemp);
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPrefixFrom));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Financial Year");
        flwTd.add(lblTemp);
        lblTemp.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFinanceYearFrom));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        ////////////////////////////////////TR 5//////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "10px", null, null, DmPosition.CENTER, false);
        lblTemp = new DmLabel("To Company");
        lblTemp.getElement().getStyle().setHeight(13, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        ///////////////////////// TR1 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("To Company");
        flwTd.add(lblTemp);
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHToCompany));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR3 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Voucher Prefix");
        flwTd.add(lblTemp);
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPrefixTo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Financial Year");
        flwTd.add(lblTemp);
        lblTemp.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFinanceYearTo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
//        ///////////////////////// TR1 /////////////////////////////////////////
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(btnCopy);
//        btnCopy.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "210px", null, null, DmPosition.CENTER, true);
        FlowPanel flwTr;
        FlowPanel flwTd;

        ///////////////////////// TR1 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(createFromGrid());
        flwTd.add(chkAllFromGrd);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        chkAllFromGrd.addStyleName("fa fa-square-o");
        chkAllFromGrd.getElement().setAttribute("style", "position:absolute; top:7px; left:31px; z-index:10;cursor:pointer;font-size:1.1em");
        chkAllFromGrd.setTitle("Select all");

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdToCompany">
    interface GrdToCompany_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdCopyPermissionFromTo_Mdl> {

        @Editor.Path("IsCheckedOrDelete")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isChecked();

        @Editor.Path("UserDescription")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> userDescription();

        @Editor.Path("IsAdmin")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isAdmin();

        @Editor.Path("IsSite")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isSite();

        @Editor.Path("IsCostCenter")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isCostCenter();

        @Editor.Path("IsUserPermission")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isUserPermission();

        @Editor.Path("IsVoucherPermission")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isVoucherPermission();

        @Editor.Path("FromCompanyName")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> fromCompanyName();

        @Editor.Path("FromCompVrPrefix")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> fromCompVrPrefix();

    }

    final DmGrid<GrdCopyPermissionFromTo_Mdl, GrdToCompany_PrpAcs> grdToCompany = new DmGrid(GWT.create(GrdToCompany_PrpAcs.class));

    private Grid createToGrid() {

        ColumnConfig<GrdCopyPermissionFromTo_Mdl, String> site;
        ColumnConfig<GrdCopyPermissionFromTo_Mdl, String> costCenter;
        ColumnConfig<GrdCopyPermissionFromTo_Mdl, String> userPermission;
        ColumnConfig<GrdCopyPermissionFromTo_Mdl, String> vrPermission;
        ColumnConfig<GrdCopyPermissionFromTo_Mdl, String> deleteCheckBox;

        grdToCompany.addRowNumberColumn();
        deleteCheckBox = grdToCompany.addTextColumn(grdToCompany.getPropertyAccess().isChecked(), "", 30, "");
        grdToCompany.addTextColumn(grdToCompany.getPropertyAccess().name(), "User Name", 150, "");
        grdToCompany.addTextColumn(grdToCompany.getPropertyAccess().userDescription(), "User Description", 150, "");
        grdToCompany.addTextColumn(grdToCompany.getPropertyAccess().isAdmin(), "Admin", 80, "");
        site = grdToCompany.addTextColumn(grdToCompany.getPropertyAccess().isSite(), "ST", 40, "");
        site.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        costCenter = grdToCompany.addTextColumn(grdToCompany.getPropertyAccess().isCostCenter(), "CT", 40, "");
        costCenter.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        userPermission = grdToCompany.addTextColumn(grdToCompany.getPropertyAccess().isUserPermission(), "UP", 40, "");
        userPermission.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        vrPermission = grdToCompany.addTextColumn(grdToCompany.getPropertyAccess().isVoucherPermission(), "VP", 40, "");
        vrPermission.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        grdToCompany.addTextColumn(grdToCompany.getPropertyAccess().fromCompanyName(), "From Company", 250, "");
        grdToCompany.addTextColumn(grdToCompany.getPropertyAccess().fromCompVrPrefix(), "Prefix", 80, "");

        grdToCompany.initializeGrid(grdToCompany.getPropertyAccess().fieldHashCode(), 1280, 265, 3);

        site.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double val = DmShared.vNull(grdToCompany.getGrid().getStore().get(context.getIndex()).getIsSite()).doubleValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square' style='font-size:1.1em;color:green;'></span>");
                }
            }
        });
        costCenter.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double val = DmShared.vNull(grdToCompany.getGrid().getStore().get(context.getIndex()).getIsCostCenter()).doubleValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square' style='font-size:1.1em;color:green;'></span>");
                }
            }
        });
        userPermission.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double val = DmShared.vNull(grdToCompany.getGrid().getStore().get(context.getIndex()).getIsUserPermission()).doubleValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square' style='font-size:1.1em;color:green;'></span>");
                }

            }
        });
        vrPermission.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double val = DmShared.vNull(grdToCompany.getGrid().getStore().get(context.getIndex()).getIsVoucherPermission()).doubleValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square' style='font-size:1.1em;color:green;'></span>");
                }

            }
        });
        deleteCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdToCompany.getGrid().getStore().get(context.getIndex()).getIsCheckedOrDelete()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-times' style='font-size:1.3em;margin-left:12%;color:red;'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em;margin-left:12%;'></span>");
                }
            }
        });

        return grdToCompany.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdFromCompany">
    interface GrdFromCompany_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdCopyPermissionFromTo_Mdl> {

        @Editor.Path("IsCheckedOrDelete")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isChecked();

        @Editor.Path("UserDescription")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> userDescription();

        @Editor.Path("IsAdmin")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isAdmin();

        @Editor.Path("IsSite")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isSite();

        @Editor.Path("IsCostCenter")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isCostCenter();

        @Editor.Path("IsUserPermission")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isUserPermission();

        @Editor.Path("IsVoucherPermission")
        ValueProvider<GrdCopyPermissionFromTo_Mdl, String> isVoucherPermission();

    }

    final DmGrid<GrdCopyPermissionFromTo_Mdl, GrdFromCompany_PrpAcs> grdFromCompany = new DmGrid(GWT.create(GrdFromCompany_PrpAcs.class));

    private Grid createFromGrid() {
        ColumnConfig<GrdCopyPermissionFromTo_Mdl, String> site;
        ColumnConfig<GrdCopyPermissionFromTo_Mdl, String> costCenter;
        ColumnConfig<GrdCopyPermissionFromTo_Mdl, String> userPermission;
        ColumnConfig<GrdCopyPermissionFromTo_Mdl, String> vrPermission;
        ColumnConfig<GrdCopyPermissionFromTo_Mdl, String> selectCheckBox;

        grdFromCompany.addRowNumberColumn();
        selectCheckBox = grdFromCompany.addTextColumn(grdFromCompany.getPropertyAccess().isChecked(), "", 30, "");
        grdFromCompany.addTextColumn(grdFromCompany.getPropertyAccess().name(), "User Name", 110, "");
        grdFromCompany.addTextColumn(grdFromCompany.getPropertyAccess().userDescription(), "User Description", 110, "");
        grdFromCompany.addTextColumn(grdFromCompany.getPropertyAccess().isAdmin(), "Admin", 40, "");
        site = grdFromCompany.addTextColumn(grdFromCompany.getPropertyAccess().isSite(), "ST", 40, "");
        site.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        costCenter = grdFromCompany.addTextColumn(grdFromCompany.getPropertyAccess().isCostCenter(), "CT", 40, "");
        costCenter.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        userPermission = grdFromCompany.addTextColumn(grdFromCompany.getPropertyAccess().isUserPermission(), "UP", 40, "");
        userPermission.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        vrPermission = grdFromCompany.addTextColumn(grdFromCompany.getPropertyAccess().isVoucherPermission(), "VP", 40, "");
        vrPermission.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        grdFromCompany.initializeGrid(grdFromCompany.getPropertyAccess().fieldHashCode(), 485, 210, 3);

        site.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double val = DmShared.vNull(grdFromCompany.getGrid().getStore().get(context.getIndex()).getIsSite()).doubleValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square' style='font-size:1.1em,;color:green;'></span>");
                }
            }
        });
        costCenter.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double val = DmShared.vNull(grdFromCompany.getGrid().getStore().get(context.getIndex()).getIsCostCenter()).doubleValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square' style='font-size:1.1em;color:green;'></span>");
                }
            }
        });
        userPermission.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double val = DmShared.vNull(grdFromCompany.getGrid().getStore().get(context.getIndex()).getIsUserPermission()).doubleValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square' style='font-size:1.1em;color:green;'></span>");
                }

            }
        });
        vrPermission.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                double val = DmShared.vNull(grdFromCompany.getGrid().getStore().get(context.getIndex()).getIsVoucherPermission()).doubleValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square' style='font-size:1.1em;color:green;'></span>");
                }

            }
        });
        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdFromCompany.getGrid().getStore().get(context.getIndex()).getIsCheckedOrDelete()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em;margin-left:12%;'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em;margin-left:12%;'></span>");
                }
            }
        });

        return grdFromCompany.getGrid();
    }
    //</editor-fold>
}
