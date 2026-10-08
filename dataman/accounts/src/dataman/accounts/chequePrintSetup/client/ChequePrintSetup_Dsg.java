package dataman.accounts.chequePrintSetup.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.accounts.chequePrintSetup.shared.BankName_Mdl;
import dataman.accounts.chequePrintSetup.shared.Find_Mdl;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ChequePrintSetup_Dsg extends IsWidgetExtended {

    final String widgetName = "Cheque Print Setup";
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtAuthSign = new DmTextBox(250, false);
    final DmTextBox txtCompanyName = new DmTextBox(250, false);
    final DmTextBoxNumeric txtChequeDateTop = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtChequeDateLeft = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtFavourOfTop = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtFavourOfLeft = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtAmtInWordsTop = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtAmtInWordsLeft = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtAmountTop = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtAmountLeft = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtCrossChequeTop = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtCrossChequeLeft = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtAuthSignTop = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtAuthSignLeft = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtCompanyNameTop = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtCompanyNameLeft = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtPaperWidth = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtPaperHeight = new DmTextBoxNumeric(2, false, 6, false);

    final DmTextBoxNumeric txtPaperWidthMM = new DmTextBoxNumeric(2, false, 6, false);
    final DmTextBoxNumeric txtPaperHeightMM = new DmTextBoxNumeric(2, false, 6, false);

    final Menu menu = new Menu();
    final MenuItem mnuCopy = new MenuItem("Copy");

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("BankName")
        ValueProvider<Find_Mdl, String> bankName();

        @Editor.Path("AuthSign")
        ValueProvider<Find_Mdl, String> authSign();

        @Editor.Path("CompanyName")
        ValueProvider<Find_Mdl, String> companyName();

        @Editor.Path("PaperWidth")
        ValueProvider<Find_Mdl, String> paperWidth();

        @Editor.Path("PaperHeight")
        ValueProvider<Find_Mdl, String> paperHeight();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 11);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().bankName(), "Bank Name", 0, "bankName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().authSign(), "Authorized Signatory", 170, "authSign");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().companyName(), "Company Name", 170, "companyName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().paperWidth(), "Paper Width", 110, "paperWidth", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().paperHeight(), "Paper Height", 110, "paperHeight", DmEnum.DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("bankName", DmSortType.ASC);

    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHBankName">

    interface BankName_PrpAcs extends KeyField_T_PrpAcs<BankName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<BankName_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<BankName_Mdl, String> cityName();

    }
    final DmTextHelp<BankName_Mdl, BankName_PrpAcs> txtHBankName = new DmTextHelp(com.google.gwt.core.shared.GWT.create(BankName_PrpAcs.class), 6, true);

    private void iniTxtHBank() {
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().name(), "Bank", 100, "bankName");
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().cityName(), "City", 100, "cityName");
        txtHBankName.getDmGrid().initializeGrid(txtHBankName.getDmGrid().getPropertyAccess().code(), 375, 0, 1);
        txtHBankName.getHelper().addSort("bankName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public ChequePrintSetup_Dsg() {
        ini();
        iniOtherMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHBank();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "55%", "", null, null, DmPosition.CENTER_HORIZONTAL, true);

//<editor-fold defaultstate="collapsed" desc="BANK NAME">
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bank Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHBankName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Cheque Date">
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cheque Date");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Top");
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtChequeDateTop));
        flwTR.add(flwTD);

        //Cheque Date LEFT
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Left");
        lbl.getElement().getStyle().setMarginLeft(60, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtChequeDateLeft));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Favour Of">
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Favour Of");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Top");
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtFavourOfTop));
        flwTR.add(flwTD);

        //Favour Of LEFT
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Left");
        lbl.getElement().getStyle().setMarginLeft(60, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtFavourOfLeft));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Amount In words">
        //Amount In Words TOP
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount In Words");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Top");
        lbl.getElement().getStyle().setMarginLeft(0, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtAmtInWordsTop));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Left");
        lbl.getElement().getStyle().setMarginLeft(60, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtAmtInWordsLeft));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Amount">
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Top");
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtAmountTop));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Left");
        lbl.getElement().getStyle().setMarginLeft(60, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtAmountLeft));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Ac Payee">
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cross Cheque");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Top");
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtCrossChequeTop));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Left");
        lbl.getElement().getStyle().setMarginLeft(60, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtCrossChequeLeft));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Authorized Signatory">
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Authorized Signatory");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtAuthSign));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Top");
        lbl.getElement().getStyle().setMarginLeft(80, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtAuthSignTop));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Left");
        lbl.getElement().getStyle().setMarginLeft(60, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtAuthSignLeft));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="COMPANY NAME">
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Company Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtCompanyName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Top");
        lbl.getElement().getStyle().setMarginLeft(80, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtCompanyNameTop));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Left");
        lbl.getElement().getStyle().setMarginLeft(60, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtCompanyNameLeft));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="PAPER WIDTH HEIGHT">
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Paper");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Width");
        lbl.getElement().getStyle().setMarginLeft(0, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPaperWidthMM));
        txtPaperWidthMM.getElement().setPropertyString("placeholder", "mm");
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPaperWidth));
        txtPaperWidth.getElement().setPropertyString("placeholder", "px");
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Height");
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPaperHeightMM));
        txtPaperHeightMM.getElement().setPropertyString("placeholder", "mm");
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPaperHeight));
        txtPaperHeight.getElement().setPropertyString("placeholder", "px");
        flwTR.add(flwTD);

        flwTable.add(flwTR);

//</editor-fold>
        return flwTable;
    }

    private DmUserControl createBottom() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

    private Menu iniOtherMenu() {
        menu.add(mnuCopy);
        return menu;
    }
}
