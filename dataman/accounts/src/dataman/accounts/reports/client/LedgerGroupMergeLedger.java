package dataman.accounts.reports.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.reports.shared.LedgerGroupMergeLedger_Srv;
import dataman.accounts.reports.shared.LedgerGroupMergeLedger_SrvAsync;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class LedgerGroupMergeLedger extends DmReportLayout implements IsWidget, BlurHandler {

    ServiceInvoker<LedgerGroupMergeLedger_SrvAsync> SrvAsync;
    final String widgetName =new lmAccounts().LEDGER_GROUP_MERGE_LEDGER.caption;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox();
    DmTextBox txtToDate = new DmTextBox();
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHLedgerGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
   
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHContraACNeeded = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);

    public void iniTxtHLedgerGroup() {
        txtHLedgerGroup.getDmGrid().addTextColumn(txtHLedgerGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHLedgerGroup.getDmGrid().initializeGrid(txtHLedgerGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHLedgerGroup.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    public void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2); //244
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

  
    public void iniTxtHContraNeeded() {
        txtHContraACNeeded.getDmGrid().addTextColumn(txtHContraACNeeded.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHContraACNeeded.getDmGrid().initializeGrid(txtHContraACNeeded.getDmGrid().getPropertyAccess().code(), 200, 0, 1); //244
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
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

        txtHLedgerGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLedgerGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLedgerGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLedgerGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLedgerGroup).show();
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

        
        txtHContraACNeeded.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHContraNeeded(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

    }

    public LedgerGroupMergeLedger(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(LedgerGroupMergeLedger_Srv.class), LedgerGroupMergeLedger.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
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

        txtFromDate.setMandatory(true);
        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHLedgerGroup();
        addControl("ledgerGroup", "Ledger Group", vl.add(txtHLedgerGroup));

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

    
        txtHContraACNeeded.setMandatory(true);
        iniTxtHContraNeeded();
        addControl("contraAccount", "Contra A/C Needed", vl.add(txtHContraACNeeded));

        defaultValue();
        swi.setWidget(this);
    }

    private void defaultValue() {
     
        txtHContraACNeeded.setTag("N");
        txtHContraACNeeded.setText("No");
    }
}
