package dataman.accounts.reports.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.reports.shared.BankBook_Srv;
import dataman.accounts.reports.shared.CashBook_Srv;
import dataman.accounts.reports.shared.CashBook_SrvAsync;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class CashBook extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new lmAccounts().CASH_BOOK.caption;
    ServiceInvoker<CashBook_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();

    DmTextBox txtFromDate = new DmTextBox();
    DmTextBox txtToDate = new DmTextBox();

    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    public void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2); //244
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

 
    
     final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAccountName = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 166, 0, 1);
        txtHAccountName.getHelper().addSort("Code", DmEnum.DmSortType.ASC);
    }
   

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {

        if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox(" From Date is mandatory ", txtFromDate).show();
            return;
        }
        if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("To Date is mandatory ", txtToDate).show();
            return;
        }

        if (DmShared.xNull(txtHAccountName.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Account name is mandatory ", txtHAccountName).show();
            return;
        }

        if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {

            if (DmShared.convertDate(DmShared.xNull(txtFromDate.getText())).getTime() > DmShared.convertDate(DmShared.xNull(txtToDate.getText())).getTime()) {
                new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtToDate).show();
                return;
            }

        }

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

        
          txtHAccountName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountName).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        
        

        
    }

    public CashBook(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(CashBook_Srv.class), CashBook.class.getName());
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

        txtToDate.setMandatory(true);
        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        
       
        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

        txtHAccountName.setMandatory(true);
        iniTxtHAccountName();
        addControl("accountName", "Account Name", vl.add(txtHAccountName));

        

        swi.setWidget(this);
      
    }



}
