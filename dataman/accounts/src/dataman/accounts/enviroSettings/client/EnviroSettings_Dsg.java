package dataman.accounts.enviroSettings.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.enviroSettings.shared.TxtHStockAdjustmentVType;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

abstract class EnviroSettings_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().ENVIRONMENT_SETTINGS.caption;
    public final static String modifiedByBlog = "Modified By & Date";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmToolContainer toolContainer = new DmToolContainer();
    final Button btnSave = new Button();
    final Button btnCopy = new Button();
    final Button btnClose = new Button();
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    //<editor-fold defaultstate="collapsed" desc="txtHSiteCopy">
    final DmHelpGrid<KeyFieldExtended, KeyField_PrpAcs> txtHSiteCopy = new DmHelpGrid(GWT.create(KeyField_PrpAcs.class), 3);

    private void iniSiteCopy() {
        txtHSiteCopy.getDmGrid().addTextColumn(txtHSiteCopy.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSiteCopy.getDmGrid().initializeGrid(txtHSiteCopy.getDmGrid().getPropertyAccess().code(), 120, 0, 1);

        txtHSiteCopy.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 500, 0, 1);

        txtHSite.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHStkAdjVoucherType_PrpAcs">
    interface TxtHStkAdjVoucherType_PrpAcs extends KeyField_T_PrpAcs<TxtHStockAdjustmentVType> {

        @Editor.Path("Decription")
        ValueProvider<TxtHStockAdjustmentVType, String> decription();

        @Editor.Path("Category")
        ValueProvider<TxtHStockAdjustmentVType, String> category();

    }

    final DmTextHelpMulti<TxtHStockAdjustmentVType, TxtHStkAdjVoucherType_PrpAcs> txtHStockAdjustmentVType = new DmTextHelpMulti(GWT.create(TxtHStkAdjVoucherType_PrpAcs.class), 7, false);

    private void iniTxtHStockAdjustmentVType() {
        txtHStockAdjustmentVType.getDmGrid().addTextColumn(txtHStockAdjustmentVType.getDmGrid().getPropertyAccess().name(), "Name", 60, "name");
        txtHStockAdjustmentVType.getDmGrid().addTextColumn(txtHStockAdjustmentVType.getDmGrid().getPropertyAccess().decription(), "Description", 100, "description");
        txtHStockAdjustmentVType.getDmGrid().addTextColumn(txtHStockAdjustmentVType.getDmGrid().getPropertyAccess().category(), "Category", 60, "category");
        txtHStockAdjustmentVType.getDmGrid().initializeGrid(txtHStockAdjustmentVType.getDmGrid().getPropertyAccess().code(), 400, 0, 3);
        txtHStockAdjustmentVType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TxtHVoucherExcludeInTurnover_PrpAcs">
    
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherExcludeInTurnover = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHVoucherExcludeInTurnover() {
        txtHVoucherExcludeInTurnover.getDmGrid().addTextColumn(txtHVoucherExcludeInTurnover.getDmGrid().getPropertyAccess().name(), "Type", 300, "Name");
        txtHVoucherExcludeInTurnover.getDmGrid().initializeGrid(txtHVoucherExcludeInTurnover.getDmGrid().getPropertyAccess().code(), 310, 0, 2);
        txtHVoucherExcludeInTurnover.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRoundOffTDS">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRoundOffTDS = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniRoundOffTDS() {
        txtHRoundOffTDS.getDmGrid().addTextColumn(txtHRoundOffTDS.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHRoundOffTDS.getDmGrid().initializeGrid(txtHRoundOffTDS.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHTDSOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTDSOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniTxtHTDSOn() {
        txtHTDSOn.getDmGrid().addTextColumn(txtHTDSOn.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHTDSOn.getDmGrid().initializeGrid(txtHTDSOn.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRoundOffGST">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRoundOffGST = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniRoundOffGST() {
        txtHRoundOffGST.getDmGrid().addTextColumn(txtHRoundOffGST.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHRoundOffGST.getDmGrid().initializeGrid(txtHRoundOffGST.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    public EnviroSettings_Dsg() {
        ini();
        iniSite();
        iniSiteCopy();
        iniRoundOffTDS();
        iniRoundOffGST();
        iniTxtHStockAdjustmentVType();
        iniTxtHTDSOn();
        iniTxtHVoucherExcludeInTurnover();
    }

    private DmToolContainer iniToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.setTitle("Back");
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
        btnClose.setTitle("Close");
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

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("600px");
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
        DmClient.setStyle(flwTable, "100%", "545px", null, null, DmPosition.CENTER, true);

        // Control Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        // Grid Tab
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "518px", "left", null, DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "35%", "20px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "90%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "518px", null, null, DmPosition.NONE, true);

        tabPanel.add(tabGeneral(), "General");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel tabGeneral() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "480px", null, null, DmPosition.CENTER, true);

        //================================< 1st Row >=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("TDS Round Off");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRoundOffTDS));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================< ******* >=========================================//
        //================================< 2nd Row >=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Round Off");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRoundOffGST));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("TDS On");
        lbl.getElement().getStyle().setMarginLeft(60, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(vl.add(txtHTDSOn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================< ******* >=========================================//
        //================================< 3rd Row >=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Stock Adjustment Voucher");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHStockAdjustmentVType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================< 4th Row >=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Vouchers Exclueds in Turnover");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherExcludeInTurnover));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================< ******* >=========================================//

        return flwTable;
    }
}
