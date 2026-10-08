package dataman.accounts.reports.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.reports.shared.UnapprovedLedger_Mdl;
import dataman.accounts.reports.shared.UnapprovedLedger_Srv;
import dataman.accounts.reports.shared.UnapprovedLedger_SrvAsync;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class UnapprovedLedger extends DmReportLayout implements IsWidget, BlurHandler {

    ServiceInvoker<UnapprovedLedger_SrvAsync> SrvAsync;
    final String widgetName = new lmAccounts().UNAPPROVE_LEDGER.caption;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtAsOnDate = new DmTextBox();
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHAccountGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHContraAccount = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtAsOnDate) {
            txtAsOnDate.setText(DmClient.returnDate(txtAsOnDate.getText()));
        }
    }

    //<editor-fold defaultstate="collapsed" desc="TxtHAccountName">
    interface TxtHAccountName_PrpAcs extends KeyField_T_PrpAcs<UnapprovedLedger_Mdl.TxtHAccountName> {

        @Editor.Path("City")
        ValueProvider<UnapprovedLedger_Mdl.TxtHAccountName, String> city();

        @Editor.Path("GroupName")
        ValueProvider<UnapprovedLedger_Mdl.TxtHAccountName, String> groupName();

        @Editor.Path("ManualCode")
        ValueProvider<UnapprovedLedger_Mdl.TxtHAccountName, String> manualCode();
    }
    DmTextHelpMulti<UnapprovedLedger_Mdl.TxtHAccountName, TxtHAccountName_PrpAcs> txtHAccountName = new DmTextHelpMulti(GWT.create(TxtHAccountName_PrpAcs.class), 10);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHVocherType">
    interface TxtHVoucherType_PrpAcs extends KeyField_T_PrpAcs<UnapprovedLedger_Mdl.TxtHVoucherType> {

        @Editor.Path("Type")
        ValueProvider<UnapprovedLedger_Mdl.TxtHVoucherType, String> type();
    }
    DmTextHelpMulti<UnapprovedLedger_Mdl.TxtHVoucherType, TxtHVoucherType_PrpAcs> txtHVoucherType = new DmTextHelpMulti(GWT.create(TxtHVoucherType_PrpAcs.class), 10);
    //</editor-fold>

    private void iniTxtHAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "GroupName");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHAccountGroup.getHelper().addSort("GroupName", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().city(), "City", 100, "City");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().groupName(), "Group Name", 150, "GroupName");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 500, 0, 2);
        txtHAccountName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("City", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("ManualCode", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Description");
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().type(), "Type", 50, "V_Type");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHVoucherType.getHelper().addSort("Description", DmEnum.DmSortType.ASC);
    }
    
       private void iniTxtHContraAccount() {
        txtHContraAccount.getDmGrid().addTextColumn(txtHContraAccount.getDmGrid().getPropertyAccess().name(), "Name", 1, "NAME");
        txtHContraAccount.getDmGrid().initializeGrid(txtHContraAccount.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHContraAccount.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
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
        txtHAccountGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAccountGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHAccountName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<UnapprovedLedger_Mdl.TxtHAccountName>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<UnapprovedLedger_Mdl.TxtHAccountName>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<UnapprovedLedger_Mdl.TxtHAccountName>> result) {
                        if (result.isStatus()) {
                            txtHAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
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
        txtHVoucherType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<UnapprovedLedger_Mdl.TxtHVoucherType>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<UnapprovedLedger_Mdl.TxtHVoucherType>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<UnapprovedLedger_Mdl.TxtHVoucherType>> result) {
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
        
         txtHContraAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHContraAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHContraAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHContraAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHContraAccount).show();
                        }

                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        
    }

    public UnapprovedLedger(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(UnapprovedLedger_Srv.class), UnapprovedLedger.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtAsOnDate.addBlurHandler(this);
        txtEvents();
        initialize(swi);
        setDefaultValue();
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }
    
     private void setDefaultValue() {
        
        
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended keyField = result.getData();
                    txtHSiteName.setTag(keyField.getSharedKeyFieldCode());
                    txtHSiteName.setText(keyField.getSharedKeyFieldName());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void initialize(SetWidgetInvoker swi) {
        txtAsOnDate.setMandatory(true);
        addControl("asOnDate", "As On Date", vl.add(txtAsOnDate));
        txtAsOnDate.setText(DmClient.getLoginDate());

        iniTxtHAccountGroup();
        addControl("accountGroup", "Account Group", vl.add(txtHAccountGroup));

        iniTxtHAccountName();
        addControl("accountName", "Account Name", vl.add(txtHAccountName));

        iniTxtHVoucherType();
        addControl("voucherType", "Voucher Type", vl.add(txtHVoucherType));

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));
        swi.setWidget(this);
        
         iniTxtHContraAccount();
        txtHContraAccount.setMandatory(true);
        addControl("contraAccount", "Contra A/c Needed", vl.add(txtHContraAccount));
        
        
        
        txtHContraAccount.setTag("N");
        txtHContraAccount.setText("No");

    }

}
