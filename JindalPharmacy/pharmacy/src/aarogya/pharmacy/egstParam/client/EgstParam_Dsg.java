package aarogya.pharmacy.egstParam.client;

import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract public class EgstParam_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().EGST_PARAM.caption;
    public final static String modifiedByBlog = "Modified By & Date";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final Button btnSave = new Button();
    final Button btnCopy = new Button();
    final Button btnClose = new Button();
    final DmToolContainer toolContainer = new DmToolContainer();
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    //<editor-fold defaultstate="collapsed" desc="E-Way Var">
    final DmTextBox txtEwayUserName = new DmTextBox(50, true);
    final PasswordTextBox txtEwayUserPass = new PasswordTextBox();
    final PasswordTextBox txtEwayUserCPass = new PasswordTextBox();
    final PasswordTextBox txtEwayAspCPassword = new PasswordTextBox();
    final DmTextBox txtEwayBaseVersion = new DmTextBox(10, false);
    final DmTextBox txtEwayGenerateAction = new DmTextBox(50, false);
    final DmTextBox txtEwayCancelAction = new DmTextBox(50, false);
    final DmRemarkTextBox txtEwayBaseUrl = new DmRemarkTextBox(250, false);
    final DmTextBox txtEwayAuthVersion = new DmTextBox(10, false);
    final DmTextBox txtEwayAspId = new DmTextBox(10, true);
    final PasswordTextBox txtEwayAspPassword = new PasswordTextBox();
    final DmTextBox txtEwayGSPName = new DmTextBox(50, false);
    final DmRemarkTextBox txtEwayUrlFormat = new DmRemarkTextBox(250, false);
    final DmTextBox txtEwayBaseApiName = new DmTextBox(50, false);
    final DmTextBox txtEwayAuthApiName = new DmTextBox(50, false);
    final DmTextBox txtEwayAuthAction = new DmTextBox(50, false);
    final DmRemarkTextBox txtEwayAuthFormat = new DmRemarkTextBox(250, false);
    final DmTextBox txtEwayPrintlApiName = new DmTextBox(50, false);
    final DmTextBox txtEwayPrintAction = new DmTextBox(50, false);
    final DmTextBox txtEwayDetailedPrintAction = new DmTextBox(50, false);
    final DmRemarkTextBox txtEwayPrintFormat = new DmRemarkTextBox(250, false);
    final DmTextBox txtEwayAuthFolderName = new DmTextBox(50, false);
    final DmTextBox txtEwayBaseFolderName = new DmTextBox(50, false);
    final DmTextBox txtEwayPrintlVersion = new DmTextBox(10, false);
    final DmTextBox txtEwaygetDetailAction = new DmTextBox(50, false);
    final DmRemarkTextBox txtEwayExampleFormat = new DmRemarkTextBox(250, false);
    final DmLabel lblEwayBaseUrl = new DmLabel("IRP");

    //<editor-fold defaultstate="collapsed" desc="txtHEwayIRP">
    interface IrpEway_PrpAcs extends KeyFieldHashCode_T_PrpAcs<IrpInfoPopup_Mdl> {

        @Editor.Path("Url")
        ValueProvider<IrpInfoPopup_Mdl, String> url();

    }
    final DmTextHelp<IrpInfoPopup_Mdl, IrpEway_PrpAcs> txtHEwayIRP = new DmTextHelp(GWT.create(IrpEway_PrpAcs.class), 5);

    private void iniTxtHEwayBaseUrl() {
        txtHEwayIRP.getDmGrid().addTextColumn(txtHEwayIRP.getDmGrid().getPropertyAccess().name(), "Name", 80, "irpName");
        txtHEwayIRP.getDmGrid().addTextColumn(txtHEwayIRP.getDmGrid().getPropertyAccess().url(), "URL", 200, "url");
        txtHEwayIRP.getDmGrid().initializeGrid(txtHEwayIRP.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 200);
        txtHEwayIRP.getHelper().addSort("irpName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Text Help">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHUserGstin = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniHUserGstin() {
        txtHUserGstin.getDmGrid().addTextColumn(txtHUserGstin.getDmGrid().getPropertyAccess().name(), "GSTIN", 0, "gstin");
        txtHUserGstin.getDmGrid().initializeGrid(txtHUserGstin.getDmGrid().getPropertyAccess().code(), 500, 0, 1);
        txtHUserGstin.getHelper().addSort("gstin", DmEnum.DmSortType.ASC);
    }

    final DmHelpGrid<KeyFieldExtended, KeyField_PrpAcs> txtHUserGstinCopy = new DmHelpGrid(GWT.create(KeyField_PrpAcs.class), 3);

    private void iniHUserGstinCopy() {
        txtHUserGstinCopy.getDmGrid().addTextColumn(txtHUserGstinCopy.getDmGrid().getPropertyAccess().name(), "GSTIN", 0, "gstin");
        txtHUserGstinCopy.getDmGrid().initializeGrid(txtHUserGstinCopy.getDmGrid().getPropertyAccess().code(), 120, 0, 1);
        txtHUserGstinCopy.getHelper().addSort("gstin", DmEnum.DmSortType.ASC);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EInvoice">
    final DmTextBox txtEinvUserName = new DmTextBox(50, true);
    final PasswordTextBox txtEinvUserPass = new PasswordTextBox();
    final PasswordTextBox txtEinvUserCPass = new PasswordTextBox();
    final PasswordTextBox txtEinvAspCPassword = new PasswordTextBox();
    final DmTextBox txtEinvBaseVersion = new DmTextBox(10, true);
    final DmTextBox txtEinvGenerateAction = new DmTextBox(50, false);
    final DmTextBox txtEinvCancelAction = new DmTextBox(50, false);
    final DmRemarkTextBox txtEinvBaseUrl = new DmRemarkTextBox(250, false);
    final DmTextBox txtEinvAuthVersion = new DmTextBox(10, false);
    final DmTextBox txtEinvAspId = new DmTextBox(10, false);
    final PasswordTextBox txtEinvAspPassword = new PasswordTextBox();
    final DmTextBox txtEinvGSPName = new DmTextBox(50, false);
    final DmRemarkTextBox txtEinvUrlFormat = new DmRemarkTextBox(250, false);
    final DmTextBox txtEinvBaseApiName = new DmTextBox(50, false);
    final DmTextBox txtEinvAuthApiName = new DmTextBox(50, false);
    final DmTextBox txtEinvAuthAction = new DmTextBox(50, false);
    final DmRemarkTextBox txtEinvAuthFormat = new DmRemarkTextBox(250, false);
    final DmTextBox txtEinvPrintlApiName = new DmTextBox(50, false);
    final DmTextBox txtEinvPrintAction = new DmTextBox(50, false);
    final DmTextBox txtEinvDetailedPrintAction = new DmTextBox(50, false);
    final DmRemarkTextBox txtEinvPrintFormat = new DmRemarkTextBox(250, false);
    final DmTextBox txtEinvAuthFolderName = new DmTextBox(50, false);
    final DmTextBox txtEinvBaseFolderName = new DmTextBox(50, false);
    final DmTextBox txtEinvPrintlVersion = new DmTextBox(10, false);
    final DmTextBox txtEinvgetDetailAction = new DmTextBox(50, false);
    final DmRemarkTextBox txtEinvExampleFormat = new DmRemarkTextBox(250, false);
    final DmTextBoxNumeric txtQrCodeSize = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.NONE, false, 5, false);
    final DmTextBoxNumeric txtParseIrnResp = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.NONE, false, 5, false);
    final DmRemarkTextBox txtEinvCancelFormat = new DmRemarkTextBox(250, false);
    final DmTextBox txtEinv_Eway_ViaIRNApiName = new DmTextBox(50, false);
    final DmTextBox txtEinv_Eway_ViaIRNApiFolder = new DmTextBox(50, false);
    final DmTextBox txtEinv_Eway_ViaIRNApiVersion = new DmTextBox(20, false);
    final DmRemarkTextBox txtEinv_Eway_ViaIRNUrlFormat = new DmRemarkTextBox(250, false);
    final DmLabel lblEinvBaseUrl = new DmLabel("IRP");

    //<editor-fold defaultstate="collapsed" desc="txtHEinvIRP">
    interface IrpEinv_PrpAcs extends KeyFieldHashCode_T_PrpAcs<IrpInfoPopup_Mdl> {

        @Editor.Path("Url")
        ValueProvider<IrpInfoPopup_Mdl, String> url();

    }
    final DmTextHelp<IrpInfoPopup_Mdl, IrpEinv_PrpAcs> txtHEinvIRP = new DmTextHelp(GWT.create(IrpEinv_PrpAcs.class), 5);

    private void iniTxtHEinvBaseUrl() {
        txtHEinvIRP.getDmGrid().addTextColumn(txtHEinvIRP.getDmGrid().getPropertyAccess().name(), "Name", 80, "irpName");
        txtHEinvIRP.getDmGrid().addTextColumn(txtHEinvIRP.getDmGrid().getPropertyAccess().url(), "URL", 200, "url");
        txtHEinvIRP.getDmGrid().initializeGrid(txtHEinvIRP.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 200);
        txtHEinvIRP.getHelper().addSort("irpName", DmEnum.DmSortType.ASC);
    }

//</editor-fold>
//</editor-fold>
    public EgstParam_Dsg() {
        ini();
        iniHUserGstin();
        iniHUserGstinCopy();
        iniTxtHEwayBaseUrl();
        iniTxtHEinvBaseUrl();
    }

    private DmToolContainer iniToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.setTitle("Save: (Ctrl + S)");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Copy");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-copy");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCopy.setTitle("Copy");
        btnCopy.addStyleName("dm-top-control-btnStyle");
        btnCopy.getElement().appendChild(aBtn.getElement());
        btnCopy.getElement().appendChild(label.getElement());
        btnCopy.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(vl.add(btnSave));
        toolContainer.addWidget(vl.add(btnCopy));
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();
        return toolContainer;

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("400px");
        vrtInnerForm.add(vl.add(iniToolControl()));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        userControl.addGap(40);
        userControl.addTextBlog(modifiedByBlog, 200, true, true);
        vrtInnerForm.add(userControl);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmEnum.DmPosition.CENTER_HORIZONTAL, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("User GSTIN");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHUserGstin));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "350px", null, null, DmEnum.DmPosition.NONE, true);
        tabPanel.add(tabEway(), "E-Way");
        tabPanel.add(tabEinvoice(), "E-Invoice");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private IsWidget tabEway() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();//previous hight is 550<ak>
        DmClient.setStyle(flwTable, "95%", "560px", null, null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMargin(10, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(EwayWorkingArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

    private IsWidget tabEinvoice() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();//previous hight is 550<ak>
        DmClient.setStyle(flwTable, "95%", "560px", null, null, DmEnum.DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMargin(10, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(EinvWorkingArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel EwayWorkingArea() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmEnum.DmPosition.NONE, true);
        //================================<<<< 1st Row >>>=========================================//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("User Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayUserName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("User Password");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayUserPass));
        txtEwayUserPass.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Confirm Password");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayUserCPass));
        txtEwayUserCPass.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("ASP ID");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayAspId));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("ASP Password");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayAspPassword));
        txtEwayAspPassword.getElement().getStyle().setWidth(100, Style.Unit.PCT);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Confirm Password");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayAspCPassword));
        txtEwayAspCPassword.getElement().getStyle().setWidth(100, Style.Unit.PCT);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Auth Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayAuthFormat));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base URL Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayUrlFormat));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Print Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayPrintFormat));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Example Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayExampleFormat));
        txtEwayExampleFormat.setEnabled(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblEwayBaseUrl.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        lblEwayBaseUrl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblEwayBaseUrl.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblEwayBaseUrl.getElement().getStyle().setColor("blue");
        flwTd.add(lblEwayBaseUrl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEwayIRP));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base URL");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayBaseUrl));
        txtEwayBaseUrl.setEnabled(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("GSP Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayGSPName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Auth Folder Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayAuthFolderName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base Folder Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayBaseFolderName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Auth Version");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayAuthVersion));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base Version");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayBaseVersion));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Print Version");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayPrintlVersion));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Auth API Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayAuthApiName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base API Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayBaseApiName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Print API Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayPrintlApiName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Auth Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayAuthAction));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Genrate Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayGenerateAction));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Cancel Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayCancelAction));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Get Detail Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwaygetDetailAction));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Print Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayPrintAction));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Detailed Print Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayDetailedPrintAction));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel EinvWorkingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmEnum.DmPosition.NONE, true);
        //================================<<<< 1st Row >>>=========================================//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("User Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvUserName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("User Password");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvUserPass));
        txtEinvUserPass.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Confirm Password");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvUserCPass));
        txtEinvUserCPass.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("ASP ID");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvAspId));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("ASP Password");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvAspPassword));
        txtEinvAspPassword.getElement().getStyle().setWidth(100, Style.Unit.PCT);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Confirm Password");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvAspCPassword));
        txtEinvAspCPassword.getElement().getStyle().setWidth(100, Style.Unit.PCT);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Auth Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvAuthFormat));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base URL Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvUrlFormat));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Print Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvPrintFormat));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Example Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvExampleFormat));
        txtEinvExampleFormat.setEnabled(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblEinvBaseUrl.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        lblEinvBaseUrl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblEinvBaseUrl.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblEinvBaseUrl.getElement().getStyle().setColor("blue");
        flwTd.add(lblEinvBaseUrl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEinvIRP));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base URL");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvBaseUrl));
        txtEinvBaseUrl.setEnabled(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("GSP Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvGSPName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Auth Folder Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvAuthFolderName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base Folder Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvBaseFolderName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Auth Version");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvAuthVersion));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base Version");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvBaseVersion));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Print Version");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvPrintlVersion));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Auth API Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvAuthApiName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Base API Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvBaseApiName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Print API Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvPrintlApiName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Auth Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvAuthAction));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Genrate Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvGenerateAction));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Cancel Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvCancelAction));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Get Detail Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvgetDetailAction));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Print Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvPrintAction));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Detailed Print Action");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvDetailedPrintAction));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Qr Code Size");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtQrCodeSize));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Parse IRN Response");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtParseIrnResp));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Cancel Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinvCancelFormat));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 1nd Row >>>=========================================//
        //================================<<<< 2nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("E-Way Via IRN API Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinv_Eway_ViaIRNApiName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("E-Way Via IRN API Folder");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinv_Eway_ViaIRNApiFolder));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("E-Way Via IRN Version");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinv_Eway_ViaIRNApiVersion));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 2nd Row >>>=========================================//
        //================================<<<< 3nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("E-Way Via IRN URL Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEinv_Eway_ViaIRNUrlFormat));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 3nd Row >>>=========================================//

        return flwTable;

    }
}
