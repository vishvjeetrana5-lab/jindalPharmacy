package aarogya.master.additionalUserPermission.client;

import aarogya.master.additionalUserPermission.shared.GrdService_Mdl;
import aarogya.master.additionalUserPermission.shared.GridPaymentMode_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAutoHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;

abstract class AdditionalUserPermissionGridEdit_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final FlowPanel flwSaveParent = new FlowPanel();
    final FlowPanel flwSaveChild = new FlowPanel();
    final Button btnSave = new Button();
    final FlowPanel flwAlterUserParent = new FlowPanel();
    final FlowPanel flwAlterUserChild = new FlowPanel();
    final Button btnAlterUser = new Button();
    final FlowPanel flwUserPermissionParent = new FlowPanel();
    final FlowPanel flwUserPermissionChild = new FlowPanel();
    final Button btnUserPermission = new Button();
    final Button btnClose = new Button();

    final DmTextBox txtUserName = new DmTextBox();
    final DmTextBoxNumeric txtAllowDiscountPercent = new DmTextBoxNumeric(2, false, 6, false);

    //<editor-fold defaultstate="collapsed" desc="txtHLinkedAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLinkedAc = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHLinkedAc() {
        txtHLinkedAc.getDmGrid().addTextColumn(txtHLinkedAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHLinkedAc.getDmGrid().initializeGrid(txtHLinkedAc.getDmGrid().getPropertyAccess().code(), 330, 0, 1);

        txtHLinkedAc.getHelper().addSort("NAME", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHNursingStation">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHNursingStation = new DmTextHelpMulti(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHNursingStation() {

        txtHNursingStation.getDmGrid().addTextColumn(txtHNursingStation.getDmGrid().getPropertyAccess().name(), "Namet", 1, "NAME");
        txtHNursingStation.getDmGrid().initializeGrid(txtHNursingStation.getDmGrid().getPropertyAccess().code(), 330, 0, 2);
        txtHNursingStation.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 330, 0, 1);

        txtHDepartment.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOtherDepartment">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOtherDepartment = new DmTextHelpMulti(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHOtherDepartment() {

        txtHOtherDepartment.getDmGrid().addTextColumn(txtHOtherDepartment.getDmGrid().getPropertyAccess().name(), "Other Department", 1, "name");
        txtHOtherDepartment.getDmGrid().initializeGrid(txtHOtherDepartment.getDmGrid().getPropertyAccess().code(), 330, 0, 2);
        txtHOtherDepartment.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLocationAllowed">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHLocationAllowed = new DmTextHelpMulti(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHLocationAllowed() {

        txtHLocationAllowed.getDmGrid().addTextColumn(txtHLocationAllowed.getDmGrid().getPropertyAccess().name(), "Location", 1, "name");
        txtHLocationAllowed.getDmGrid().initializeGrid(txtHLocationAllowed.getDmGrid().getPropertyAccess().code(), 330, 0, 2);
        txtHLocationAllowed.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDiscForNonDisSer = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHDiscForNDS() {
        txtHDiscForNonDisSer.getDmGrid().addTextColumn(txtHDiscForNonDisSer.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDiscForNonDisSer.getDmGrid().initializeGrid(txtHDiscForNonDisSer.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    public AdditionalUserPermissionGridEdit_Dsg() {
        ini();
        iniTxtHLinkedAc();
        iniTxtHDepartment();
        iniTxtHNursingStation();
        iniTxtHOtherDepartment();
        iniTxtHLocationAllowed();
        iniTxtHDiscForNDS();

    }

    private void ini() {
        FlowPanel flw;
        final VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");
        popupPanel.setHeight("500px");

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
        btnSave.setTitle("Save");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("55px");
        flwSaveChild.add(vl.add(btnSave));
        flwSaveParent.add(flwSaveChild);

        label = new InlineLabel("Alter User");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-user");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnAlterUser.setTitle("Alter User");
        btnAlterUser.addStyleName("dm-top-control-btnStyle");
        btnAlterUser.getElement().appendChild(aBtn.getElement());
        btnAlterUser.getElement().appendChild(label.getElement());
        btnAlterUser.setWidth("90px");
        flwAlterUserChild.add(vl.add(btnAlterUser));
        flwAlterUserParent.add(flwAlterUserChild);

        label = new InlineLabel("User Permission");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-user-plus");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnUserPermission.setTitle("User Permission");
        btnUserPermission.addStyleName("dm-top-control-btnStyle");
        btnUserPermission.getElement().appendChild(aBtn.getElement());
        btnUserPermission.getElement().appendChild(label.getElement());
        btnUserPermission.setWidth("119px");
        flwUserPermissionChild.add(vl.add(btnUserPermission));
        flwUserPermissionParent.add(flwUserPermissionChild);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.setTitle("Additional User Permission Grid Edit");
        toolContainer.addWidget(flwSaveParent);
        toolContainer.addWidget(flwAlterUserParent);
        toolContainer.addWidget(flwUserPermissionParent);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "200px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);
        /*====================================< 2Nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "215px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "110px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdService()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 3Nd Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "164px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdUser()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "160px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("User Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUserName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Linked A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLinkedAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 3nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nursing Station");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNursingStation));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 4rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allowed Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOtherDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allowed Location");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLocationAllowed));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allowed Discount %");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAllowDiscountPercent));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 7th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allow Discount (Non Discountable Services)");
        lbl.getElement().setAttribute("style", "white-space: width: 100%;");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDiscForNonDisSer));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdService">
    interface GrdService_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdService_Mdl> {

        @Editor.Path("ServiceName")
        ValueProvider<GrdService_Mdl, String> serviceName();

    }
    final DmGrid<GrdService_Mdl, GrdService_PrpAcs> grdService = new DmGrid(GWT.create(GrdService_PrpAcs.class));

    private Grid iniGrdService() {
        grdService.addRowNumberColumn();
        grdService.addTextColumn(grdService.getPropertyAccess().name(), "Service Category", 1, "");
        grdService.addTextColumn(grdService.getPropertyAccess().serviceName(), "Service Name", 250, "");
        grdService.initializeGrid(grdService.getPropertyAccess().fieldHashCode(), 522, 110, 2);

        return grdService.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd PayMode Detail">
    interface GetPaymentMode_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GridPaymentMode_Mdl> {

        @Editor.Path("VoucherTypeText")
        ValueProvider<GridPaymentMode_Mdl, String> voucherTypeText();

        @Editor.Path("IsDefaultTag")
        ValueProvider<GridPaymentMode_Mdl, String> isDefaultTag();

    }

    final DmGrid<GridPaymentMode_Mdl, GetPaymentMode_PrpAcs> grdPaymentMode = new DmGrid(GWT.create(GetPaymentMode_PrpAcs.class));

    private Grid iniGrdUser() {
        grdPaymentMode.addRowNumberColumn();
        grdPaymentMode.addTextColumn(grdPaymentMode.getPropertyAccess().voucherTypeText(), "Voucher Type", 230, "");
        grdPaymentMode.addTextColumn(grdPaymentMode.getPropertyAccess().name(), "Payment Mode", 200, "");
        ColumnConfig<GridPaymentMode_Mdl, String> selectCheckBox;
        selectCheckBox = grdPaymentMode.addTextColumn(grdPaymentMode.getPropertyAccess().isDefaultTag(), "Default", 70, "");
        grdPaymentMode.initializeGrid(grdPaymentMode.getPropertyAccess().fieldHashCode(), 522, 165, 2);
        
        selectCheckBox.setHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_CENTER);

        grdPaymentMode.getGrid().getView().setViewConfig(new GridViewConfig<GridPaymentMode_Mdl>() {

            @Override
            public String getColStyle(GridPaymentMode_Mdl model, ValueProvider<? super GridPaymentMode_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (colIndex == 2) {
                    if (DmShared.vNull(model.getIsDefaultTag()).doubleValue() > 1) {
                        styleClass += "dm-FontBold ";
                    }
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GridPaymentMode_Mdl model, int rowIndex) {
                return "";
            }
        });

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdPaymentMode.getGrid().getStore().get(context.getIndex()).getIsDefaultTag()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        return grdPaymentMode.getGrid();
    }
    //</editor-fold>


}
