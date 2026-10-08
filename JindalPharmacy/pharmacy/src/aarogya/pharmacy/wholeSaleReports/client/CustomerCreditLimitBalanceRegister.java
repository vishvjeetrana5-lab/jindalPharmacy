package aarogya.pharmacy.wholeSaleReports.client;

import aarogya.pharmacy.wholeSaleReports.shared.CustomerCreditLimitBalanceRegister_Srv;
import aarogya.pharmacy.wholeSaleReports.shared.CustomerCreditLimitBalanceRegister_SrvAsync;
import aarogya.pharmacy.wholeSaleReports.shared.TxtHAccountName;
import aarogya.pharmacy.wholeSaleReports.shared.TxtHParty_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class CustomerCreditLimitBalanceRegister extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmPharmacy().CUSTOMER_CREDIT_LIMIT_BALANCE_REGISTER.caption;
    ServiceInvoker<CustomerCreditLimitBalanceRegister_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtAsOnDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHCustomer">
    interface TxtHSupplierName_PrpAcs extends KeyField_T_PrpAcs<TxtHParty_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHParty_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHParty_Mdl, String> cityName();

    }
    final DmTextHelpMulti<TxtHParty_Mdl, TxtHSupplierName_PrpAcs> txtHCustomer = new DmTextHelpMulti(GWT.create(TxtHSupplierName_PrpAcs.class), 7);

    private void iniTxtHCustomer() {
        txtHCustomer.getDmGrid().addTextColumn(txtHCustomer.getDmGrid().getPropertyAccess().name(), "Name", 220, "customerName");
        txtHCustomer.getDmGrid().addTextColumn(txtHCustomer.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 80, "manualCode");
        txtHCustomer.getDmGrid().addTextColumn(txtHCustomer.getDmGrid().getPropertyAccess().cityName(), "City Name", 80, "cityName");
        txtHCustomer.getDmGrid().initializeGrid(txtHCustomer.getDmGrid().getPropertyAccess().code(), 380, 0, 2);
        txtHCustomer.getHelper().addSort("customerName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHAccountGroup">
    interface TxtHAccountName_PrpAcs extends KeyField_T_PrpAcs<TxtHAccountName> {

        @Editor.Path("GroupName")
        ValueProvider<TxtHAccountName, String> groupName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHAccountName, String> manualCode();

        @Editor.Path("City")
        ValueProvider<TxtHAccountName, String> city();
    }
    DmTextHelpMulti<TxtHAccountName, TxtHAccountName_PrpAcs> txtHAccountName = new DmTextHelpMulti(GWT.create(TxtHAccountName_PrpAcs.class), 10);

    private void iniTxtHAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Group Name", 0, "GroupName");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHAccountGroup.getHelper().addSort("GroupName", DmEnum.DmSortType.ASC);
    }
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHAccountGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().city(), "City", 100, "city");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().groupName(), "Group Name", 150, "GroupName");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 600, 0, 2);
        txtHAccountName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("ManualCode", DmEnum.DmSortType.ASC);
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHCreditLimitExist.">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCreditLimitExist = new DmTextHelp<>(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHCreditLimitExist() {
        txtHCreditLimitExist.getDmGrid().addTextColumn(txtHCreditLimitExist.getDmGrid().getPropertyAccess().name(), "Credit Limit Exist", 200, "name");
        txtHCreditLimitExist.getDmGrid().initializeGrid(txtHCreditLimitExist.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHCreditLimitExist.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtAsOnDate) {
            txtAsOnDate.setText(DmClient.returnDate(txtAsOnDate.getText()));
        }
    }

    private void textHelpEvents() {

        txtHCustomer.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHParty_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCustomer(DmShared.xNull(txtHCreditLimitExist.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHParty_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCustomer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHParty_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCustomer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCustomer).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

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

        txtHCreditLimitExist.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCreditLimitExist).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCreditLimitExist.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCreditLimitExist).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void initialize(SetWidgetInvoker swi) {

        addControl("asOnDate", "As On Date", vl.add(txtAsOnDate));
        txtAsOnDate.setText(DmClient.getLoginDate());
        txtAsOnDate.setTag(DmClient.getEndDate());

        iniTxtHAccountGroup();
        addControl("accountGroup", "Account Group", vl.add(txtHAccountGroup));

        iniTxtHCustomer();
        addControl("customerName", "Customer Name", vl.add(txtHCustomer));

        iniTxtHCreditLimitExist();
        addControl("creditLimitExist", "Credit Limit Exist", vl.add(txtHCreditLimitExist));

        swi.setWidget(this);
    }

    public CustomerCreditLimitBalanceRegister(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(CustomerCreditLimitBalanceRegister_Srv.class), CustomerCreditLimitBalanceRegister.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtAsOnDate.addBlurHandler(this);
        txtHCreditLimitExist.setTag("1");
        txtHCreditLimitExist.setText("Yes");
        textHelpEvents();
        initialize(swi);
        removeExcelButton();
        removeWordButton();
        

    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

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
}
