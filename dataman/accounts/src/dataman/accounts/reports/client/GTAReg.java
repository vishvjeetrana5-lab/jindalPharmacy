package dataman.accounts.reports.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.reports.shared.GTAReg_Model;
import dataman.accounts.reports.shared.GTAReg_Srv;
import dataman.accounts.reports.shared.GTAReg_SrvAsync;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class GTAReg extends DmReportLayout implements IsWidget, BlurHandler {

    ServiceInvoker<GTAReg_SrvAsync> SrvAsync;
    final String widgetName = new lmAccounts().GTA_NON_GTA_REGISTER.caption; 
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox();
    DmTextBox txtToDate = new DmTextBox();
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportOnChoice = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIncludeOpening = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    //<editor-fold defaultstate="collapsed" desc="TxtHPartyName">
    interface TxtHPartyName_PrpAcs extends KeyField_T_PrpAcs<GTAReg_Model.TxtHPartyName> {

        @Editor.Path("ManualCode")
        ValueProvider<GTAReg_Model.TxtHPartyName, String> manualCode();
    }
    DmTextHelpMulti<GTAReg_Model.TxtHPartyName, TxtHPartyName_PrpAcs> txtHPartyName = new DmTextHelpMulti(GWT.create(TxtHPartyName_PrpAcs.class), 10);

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHConsignor">
    interface TxtHConsignor_PrpAcs extends KeyField_T_PrpAcs<GTAReg_Model.TxtHConsignor> {

        @Editor.Path("ManualCode")
        ValueProvider<GTAReg_Model.TxtHConsignor, String> manualCode();
    }
    DmTextHelpMulti<GTAReg_Model.TxtHConsignor, TxtHConsignor_PrpAcs> txtHConsignor = new DmTextHelpMulti(GWT.create(TxtHConsignor_PrpAcs.class), 10);

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHConsignee">
    interface TxtHConsignee_PrpAcs extends KeyField_T_PrpAcs<GTAReg_Model.TxtHConsignee> {

        @Editor.Path("ManualCode")
        ValueProvider<GTAReg_Model.TxtHConsignee, String> manualCode();
    }
    DmTextHelpMulti<GTAReg_Model.TxtHConsignee, TxtHConsignee_PrpAcs> txtHConsignee = new DmTextHelpMulti(GWT.create(TxtHConsignee_PrpAcs.class), 10);

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHDrAccount">
    interface TxtHDrAccount_PrpAcs extends KeyField_T_PrpAcs<GTAReg_Model.TxtHDrAccount> {

        @Editor.Path("ManualCode")
        ValueProvider<GTAReg_Model.TxtHDrAccount, String> manualCode();
    }
    DmTextHelpMulti<GTAReg_Model.TxtHDrAccount, TxtHDrAccount_PrpAcs> txtHDrAccount = new DmTextHelpMulti(GWT.create(TxtHDrAccount_PrpAcs.class), 10);

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHCrAccount">
    interface TxtHCrAccount_PrpAcs extends KeyField_T_PrpAcs<GTAReg_Model.TxtHCrAccount> {

        @Editor.Path("ManualCode")
        ValueProvider<GTAReg_Model.TxtHCrAccount, String> manualCode();
    }
    DmTextHelpMulti<GTAReg_Model.TxtHCrAccount, TxtHCrAccount_PrpAcs> txtHCrAccount = new DmTextHelpMulti(GWT.create(TxtHCrAccount_PrpAcs.class), 10);

    //</editor-fold>
    private void iniTxtHConsignee() {
        txtHConsignee.getDmGrid().addTextColumn(txtHConsignee.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHConsignee.getDmGrid().addTextColumn(txtHConsignee.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        txtHConsignee.getDmGrid().initializeGrid(txtHConsignee.getDmGrid().getPropertyAccess().code(), 225, 0, 2);
        txtHConsignee.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHConsignor() {
        txtHConsignor.getDmGrid().addTextColumn(txtHConsignor.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHConsignor.getDmGrid().addTextColumn(txtHConsignor.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        txtHConsignor.getDmGrid().initializeGrid(txtHConsignor.getDmGrid().getPropertyAccess().code(), 225, 0, 2);
        txtHConsignor.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHCreditAccount() {
        txtHCrAccount.getDmGrid().addTextColumn(txtHCrAccount.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHCrAccount.getDmGrid().addTextColumn(txtHCrAccount.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        txtHCrAccount.getDmGrid().initializeGrid(txtHCrAccount.getDmGrid().getPropertyAccess().code(), 225, 0, 2);
        txtHCrAccount.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHDebitAccount() {
        txtHDrAccount.getDmGrid().addTextColumn(txtHDrAccount.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDrAccount.getDmGrid().addTextColumn(txtHDrAccount.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        txtHDrAccount.getDmGrid().initializeGrid(txtHDrAccount.getDmGrid().getPropertyAccess().code(), 225, 0, 2);
        txtHDrAccount.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHIncludeOpening() {
        txtHIncludeOpening.getDmGrid().addTextColumn(txtHIncludeOpening.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHIncludeOpening.getDmGrid().initializeGrid(txtHIncludeOpening.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }

    private void iniTxtHPartyName() {
        txtHPartyName.getDmGrid().addTextColumn(txtHPartyName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPartyName.getDmGrid().addTextColumn(txtHPartyName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        txtHPartyName.getDmGrid().initializeGrid(txtHPartyName.getDmGrid().getPropertyAccess().code(), 225, 0, 2);
        txtHPartyName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHReportOnChoice() {
        txtHReportOnChoice.getDmGrid().addTextColumn(txtHReportOnChoice.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHReportOnChoice.getDmGrid().initializeGrid(txtHReportOnChoice.getDmGrid().getPropertyAccess().code(), 225, 0, 1);
    }

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Description");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHVoucherType.getHelper().addSort("Description", DmEnum.DmSortType.ASC);
    }

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    getReport(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void txtEvents() {
        txtHConsignee.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<GTAReg_Model.TxtHConsignee>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsignee(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GTAReg_Model.TxtHConsignee>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsignee).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GTAReg_Model.TxtHConsignee>> result) {
                        if (result.isStatus()) {
                            txtHConsignee.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsignee).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHConsignor.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<GTAReg_Model.TxtHConsignor>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsignor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GTAReg_Model.TxtHConsignor>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsignor).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GTAReg_Model.TxtHConsignor>> result) {
                        if (result.isStatus()) {
                            txtHConsignor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsignor).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHCrAccount.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<GTAReg_Model.TxtHCrAccount>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCreditAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GTAReg_Model.TxtHCrAccount>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCrAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GTAReg_Model.TxtHCrAccount>> result) {
                        if (result.isStatus()) {
                            txtHCrAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCrAccount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHDrAccount.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<GTAReg_Model.TxtHDrAccount>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDebitAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GTAReg_Model.TxtHDrAccount>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDrAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GTAReg_Model.TxtHDrAccount>> result) {
                        if (result.isStatus()) {
                            txtHDrAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDrAccount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHIncludeOpening.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIncludeOpening(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIncludeOpening).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIncludeOpening.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIncludeOpening).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPartyName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<GTAReg_Model.TxtHPartyName>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPartyName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GTAReg_Model.TxtHPartyName>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPartyName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GTAReg_Model.TxtHPartyName>> result) {
                        if (result.isStatus()) {
                            txtHPartyName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPartyName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHReportOnChoice.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReportOnChoice(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportOnChoice).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportOnChoice.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportOnChoice).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSiteName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSiteName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSiteName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSiteName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHVoucherType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }

                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    public GTAReg(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(GTAReg_Srv.class), GTAReg.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtEvents();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        txtFromDate.setMandatory(true);
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());
        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHPartyName();
        addControl("partyName", "Party Name", vl.add(txtHPartyName));

        iniTxtHConsignor();
        addControl("consignor", "Consignor Name", vl.add(txtHConsignor));

        iniTxtHConsignee();
        addControl("consignee", "Consignee Name", vl.add(txtHConsignee));

        iniTxtHReportOnChoice();
        txtHReportOnChoice.setMandatory(true);
        addControl("reportOnChoice", "Report On Choice", vl.add(txtHReportOnChoice));

        iniTxtHVoucherType();
        addControl("voucherType", "Voucher Type", vl.add(txtHVoucherType));

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

        iniTxtHIncludeOpening();
        txtHIncludeOpening.setMandatory(true);
        addControl("includeOpening", "Include Opening", vl.add(txtHIncludeOpening));

        iniTxtHDebitAccount();
        addControl("debitAccount", "Debit A/c", vl.add(txtHDrAccount));

        iniTxtHCreditAccount();
        addControl("creditAccount", "Credit A/c", vl.add(txtHCrAccount));

        defaultValue();
        swi.setWidget(this);
    }

    private void defaultValue() {
        txtHReportOnChoice.setTag("G");
        txtHReportOnChoice.setText("G.T.A.");

        txtHIncludeOpening.setTag("Y");
        txtHIncludeOpening.setText("Yes");
    }

}
