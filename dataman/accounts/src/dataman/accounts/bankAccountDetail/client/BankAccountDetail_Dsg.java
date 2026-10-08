package dataman.accounts.bankAccountDetail.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.bankAccountDetail.shared.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmAccounts;

abstract class BankAccountDetail_Dsg extends IsWidgetExtended {

    final String widgetName =  new lmAccounts().BANK_ACCOUNT_DETAIL.caption;
//
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtAccountNo = new DmTextBox(20, true);
    final DmTextBox txtBankName = new DmTextBox(35, true);
    final DmTextBox txtBankBranch = new DmTextBox(35, true);
    final DmRemarkTextBox txtBankBranchAddress = new DmRemarkTextBox(250, false);
    final DmTextBox txtSwiftCode = new DmTextBox(15, false);
    final DmTextBox txtIFSCCode = new DmTextBox(11, false);

    //<editor-fold defaultstate="collapsed" desc="txtHDefault">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDefault = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4,true, true);

    private void iniTxtHDefault() {
        txtHDefault.getDmGrid().addTextColumn(txtHDefault.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHDefault.getDmGrid().initializeGrid(txtHDefault.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHLinkWithAccount">
    interface txtHLinkWithAccount_PrpAcs extends KeyField_T_PrpAcs<TxtHLinkWithAccount_Mdl> {

        @Editor.Path("City")
        ValueProvider<TxtHLinkWithAccount_Mdl, String> city();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHLinkWithAccount_Mdl, String> manualCode();
    }
    final DmTextHelp<TxtHLinkWithAccount_Mdl, txtHLinkWithAccount_PrpAcs> txtHAccount = new DmTextHelp(GWT.create(txtHLinkWithAccount_PrpAcs.class), 7, true);

    private void iniLinkAccount() {
        txtHAccount.getDmGrid().addTextColumn(txtHAccount.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHAccount.getDmGrid().addTextColumn(txtHAccount.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 150, "manualCode");
        txtHAccount.getDmGrid().addTextColumn(txtHAccount.getDmGrid().getPropertyAccess().city(), "City", 150, "City");
        txtHAccount.getDmGrid().initializeGrid(txtHAccount.getDmGrid().getPropertyAccess().code(), 500, 0, 1);
        txtHAccount.getHelper().addSort("Name", DmSortType.ASC);
        txtHAccount.getHelper().addSort("manualCode", DmSortType.ASC);
        txtHAccount.getHelper().addSort("City", DmSortType.ASC);

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("AccountNo")
        ValueProvider<Find_Mdl, String> accountNo();

        @Editor.Path("BankName")
        ValueProvider<Find_Mdl, String> bankName();

        @Editor.Path("BankBranch")
        ValueProvider<Find_Mdl, String> bankBranch();

        @Editor.Path("BankBranchAddress")
        ValueProvider<Find_Mdl, String> bankBranchAddress();

        @Editor.Path("SwiftCode")
        ValueProvider<Find_Mdl, String> swiftCode();

        @Editor.Path("IFSCCode")
        ValueProvider<Find_Mdl, String> IFSCCode();

        @Editor.Path("DefaultText")
        ValueProvider<Find_Mdl, String> defaultText();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 8, true);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Link With A/c", 100, "accountName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().accountNo(), "Account No.", 130, "accountNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().bankName(), "Bank", 150, "bankName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().bankBranch(), "Branch", 150, "branch");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().bankBranchAddress(), "Bank Address", 300, "bankAddress");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().swiftCode(), "Swift No.", 100, "swiftCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().IFSCCode(), "IFSC Code", 90, "ifscCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().defaultText(), "Default", 70, "isDefault");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0);
        dmFind.getHelper().addSort("accountName", DmSortType.ASC);
        dmFind.getHelper().addSort("sNo", DmSortType.ASC, "sNo");
        dmFind.getHelper().addSort("accountNo", DmSortType.ASC);

    }
    //</editor-fold>

    public BankAccountDetail_Dsg() {
        ini();
        iniFind();
        iniLinkAccount();
        iniTxtHDefault();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "145px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Link With A/c");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Account No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAccountNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Bank");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBankName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Branch");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBankBranch));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Branch Address");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBankBranchAddress));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 5th Row >====================================*/

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Swift No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSwiftCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("IFSC Code");
        lblTemp.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIFSCCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Default");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHDefault);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
