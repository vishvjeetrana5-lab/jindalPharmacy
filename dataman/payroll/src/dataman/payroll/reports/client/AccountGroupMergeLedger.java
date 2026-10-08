package dataman.payroll.reports.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.payroll.reports.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.libraryMenu.self.shared.lmPayroll;
import java.util.List;

public class AccountGroupMergeLedger extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = "";//new lmPayroll().ACCOUNT_GROUP_MERGE_LEDGER.caption;
    private ServiceInvoker<AccountGroupMergeLedger_SrvAsync> SrvAsync;
    private DmClient.ViaList vl = new DmClient.ViaList();
    private DmTextBox txtFromDate = new DmTextBox();
    private DmTextBox txtToDate = new DmTextBox();
    private DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    private DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHContraACNeeded = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);

    //<editor-fold defaultstate="collapsed" desc="TxtHAccountGroup">
    interface TxtHAccountGroup_PrpAcs extends KeyField_T_PrpAcs<AccountGroupMergeLedger_Model.TxtHAccountGroup> {

        @Editor.Path("GroupUnder")
        ValueProvider<AccountGroupMergeLedger_Model.TxtHAccountGroup, String> groupUnder();
    }
    private final DmTextHelpMulti<AccountGroupMergeLedger_Model.TxtHAccountGroup, TxtHAccountGroup_PrpAcs> txtHAccountGroup = new DmTextHelpMulti(GWT.create(TxtHAccountGroup_PrpAcs.class), 10);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHVocherType">
    interface TxtHVoucherType_PrpAcs extends KeyField_T_PrpAcs<AccountGroupMergeLedger_Model.TxtHVoucherType> {

        @Editor.Path("Type")
        ValueProvider<AccountGroupMergeLedger_Model.TxtHVoucherType, String> type();
    }
    private final DmTextHelpMulti<AccountGroupMergeLedger_Model.TxtHVoucherType, TxtHVoucherType_PrpAcs> txtHVoucherType = new DmTextHelpMulti(GWT.create(TxtHVoucherType_PrpAcs.class), 10);
    //</editor-fold>

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHContraNeeded() {
        txtHContraACNeeded.getDmGrid().addTextColumn(txtHContraACNeeded.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHContraACNeeded.getDmGrid().initializeGrid(txtHContraACNeeded.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }

    private void iniTxtHAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Group Name", 0, "GroupName");
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().groupUnder(), "Group Under", 200, "GrpUnder");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 400, 0, 2);
        txtHAccountGroup.getHelper().addSort("GroupName", DmEnum.DmSortType.ASC);
        txtHAccountGroup.getHelper().addSort("GrpUnder", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Group Name", 0, "Description");
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().type(), "Type", 75, "V_Type");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHVoucherType.getHelper().addSort("Description", DmEnum.DmSortType.ASC);
        txtHVoucherType.getHelper().addSort("V_Type", DmEnum.DmSortType.ASC);
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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    private void txtEvents() {
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
        txtHContraACNeeded.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHContraACNeeded(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHContraACNeeded).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHContraACNeeded.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHContraACNeeded).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHAccountGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<AccountGroupMergeLedger_Model.TxtHAccountGroup>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<AccountGroupMergeLedger_Model.TxtHAccountGroup>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AccountGroupMergeLedger_Model.TxtHAccountGroup>> result) {
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
        txtHVoucherType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<AccountGroupMergeLedger_Model.TxtHVoucherType>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherTyep(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<AccountGroupMergeLedger_Model.TxtHVoucherType>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AccountGroupMergeLedger_Model.TxtHVoucherType>> result) {
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

    public AccountGroupMergeLedger(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(AccountGroupMergeLedger_Srv.class), AccountGroupMergeLedger.class.getName());
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

        iniTxtHAccountGroup();
        addControl("accountGroup", "A/c Group", vl.add(txtHAccountGroup));

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

        txtHContraACNeeded.setMandatory(true);
        iniTxtHContraNeeded();
        addControl("contraAccount", "Contra A/C Needed", vl.add(txtHContraACNeeded));

        iniTxtHVoucherType();
        addControl("voucherType", "Voucher Type", vl.add(txtHVoucherType));

        defaultValue();
        swi.setWidget(this);

    }

    private void defaultValue() {
        txtHContraACNeeded.setTag("N");
        txtHContraACNeeded.setText("No");
    }

}
