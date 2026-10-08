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
import dataman.accounts.reports.shared.BillWiseOutstandingCreditors_Model;
import dataman.accounts.reports.shared.BillWiseOutstandingCreditors_Srv;
import dataman.accounts.reports.shared.BillWiseOutstandingCreditors_SrvAsync;
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
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.HashMap;
import java.util.List;

public class BillWiseOutstandingCreditors extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new lmAccounts().BILL_WISE_OUTSTANDING_CREDITORS.caption;
    ServiceInvoker<BillWiseOutstandingCreditors_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtAsOnDate = new DmTextBox();
    DmTextBox txtBillAfterOn = new DmTextBox();
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHAccountGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHZoneName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportOnChoice = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBalance = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHavingBal = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHShowAdjustmentOf = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    private String strReportFor = "L";

    //<editor-fold defaultstate="collapsed" desc="TxtHAccountName">
    interface TxtHAccountName_PrpAcs extends KeyField_T_PrpAcs<BillWiseOutstandingCreditors_Model.TxtHAccountName> {

        @Editor.Path("GroupName")
        ValueProvider<BillWiseOutstandingCreditors_Model.TxtHAccountName, String> groupName();

        @Editor.Path("ManualCode")
        ValueProvider<BillWiseOutstandingCreditors_Model.TxtHAccountName, String> manualCode();

        @Editor.Path("City")
        ValueProvider<BillWiseOutstandingCreditors_Model.TxtHAccountName, String> city();
    }
    DmTextHelpMulti<BillWiseOutstandingCreditors_Model.TxtHAccountName, TxtHAccountName_PrpAcs> txtHAccountName = new DmTextHelpMulti(GWT.create(TxtHAccountName_PrpAcs.class), 10);
    //</editor-fold>

    private void iniTxtHAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Group Name", 0, "GroupName");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHAccountGroup.getHelper().addSort("GroupName", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().city(), "City", 100, "city");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().groupName(), "Group Name", 150, "GroupName");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 600, 0, 2);
        txtHAccountName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("ManualCode", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHZoneName() {
        txtHZoneName.getDmGrid().addTextColumn(txtHZoneName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHZoneName.getDmGrid().initializeGrid(txtHZoneName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHZoneName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHReportOnChoice() {
        txtHReportOnChoice.getDmGrid().addTextColumn(txtHReportOnChoice.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHReportOnChoice.getDmGrid().initializeGrid(txtHReportOnChoice.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHReportOn() {
        txtHReportOn.getDmGrid().addTextColumn(txtHReportOn.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHReportOn.getDmGrid().initializeGrid(txtHReportOn.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }

    private void iniTxtHBalance() {
        txtHBalance.getDmGrid().addTextColumn(txtHBalance.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHBalance.getDmGrid().initializeGrid(txtHBalance.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }

    private void iniTxtHHavingBalance() {
        txtHHavingBal.getDmGrid().addTextColumn(txtHHavingBal.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHHavingBal.getDmGrid().initializeGrid(txtHHavingBal.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }

    private void iniTxtHShowAdjustmentOf() {
        txtHShowAdjustmentOf.getDmGrid().addTextColumn(txtHShowAdjustmentOf.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHShowAdjustmentOf.getDmGrid().initializeGrid(txtHShowAdjustmentOf.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, strReportFor, new AsyncCallback<DmResult<String>>() {

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
        if (event.getSource() == txtAsOnDate) {
            txtAsOnDate.setText(DmClient.returnDate(txtAsOnDate.getText()));
        } else if (event.getSource() == txtBillAfterOn) {
            txtBillAfterOn.setText(DmClient.returnDate(txtBillAfterOn.getText()));
        }
    }

    private void txtEvents() {
        txtHAccountGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountGroup(helper, blnToEndOfFile, strReportFor, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHAccountName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<BillWiseOutstandingCreditors_Model.TxtHAccountName>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, strReportFor, new AsyncCallback<DmResultPageing<List<BillWiseOutstandingCreditors_Model.TxtHAccountName>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<BillWiseOutstandingCreditors_Model.TxtHAccountName>> result) {
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
        txtHZoneName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHZoneName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHZoneName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHZoneName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHZoneName).show();
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
        txtHBalance.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBalance(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBalance).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBalance.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBalance).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHReportOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReportOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHHavingBal.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHHavingBalance(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHavingBal).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHavingBal.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHavingBal).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHShowAdjustmentOf.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHShowAdjustmentOf(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHShowAdjustmentOf).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHShowAdjustmentOf.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHShowAdjustmentOf).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    public BillWiseOutstandingCreditors(HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(BillWiseOutstandingCreditors_Srv.class), BillWiseOutstandingCreditors.class.getName());
        if (paramHashMap != null) {
            strReportFor = (String) paramHashMap.get("strReportFor");
        }
        setTitle(this.asWidget(), strReportFor.equalsIgnoreCase("A")?"Bill Wise Outstanding (Debtors)":"Bill Wise Outstanding (Creditors)");
        txtAsOnDate.addBlurHandler(this);
        txtBillAfterOn.addBlurHandler(this);
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
        txtAsOnDate.setMaxLength(11);
        txtBillAfterOn.setMandatory(true);
        addControl("billAfterOn", "Bill After On", vl.add(txtBillAfterOn));
        txtBillAfterOn.setText(DmClient.getStartDate());
        txtBillAfterOn.setMaxLength(11);

        iniTxtHAccountGroup();
        addControl("accountGroup", "Account Group", vl.add(txtHAccountGroup));

        iniTxtHAccountName();
        addControl("accountName", "Account Name", vl.add(txtHAccountName));

        iniTxtHZoneName();
        addControl("zoneName", "Zone Name", vl.add(txtHZoneName));

        iniTxtHReportOnChoice();
        txtHReportOnChoice.setMandatory(true);
        addControl("reportOnChoice", "Report On Choice", vl.add(txtHReportOnChoice));

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

        iniTxtHBalance();
        txtHBalance.setMandatory(true);
        addControl("balance", "Balance", vl.add(txtHBalance));

        iniTxtHReportOn();
        txtHReportOn.setMandatory(true);
        addControl("reportOn", "Report On", vl.add(txtHReportOn));

        iniTxtHHavingBalance();
        txtHHavingBal.setMandatory(true);
        addControl("havingBalance", "Having Balance", vl.add(txtHHavingBal));

        iniTxtHShowAdjustmentOf();
        txtHShowAdjustmentOf.setMandatory(true);
        addControl("showAdjustmentOf", "Show Adjustment Of", vl.add(txtHShowAdjustmentOf));

        defaultValue();
        swi.setWidget(this);
    }

    private void defaultValue() {
        txtHBalance.setText("Cummulative Balance");
        txtHBalance.setTag("C");

        txtHHavingBal.setText("Yes");
        txtHHavingBal.setTag("Y");

        if (DmShared.xNull(strReportFor).equalsIgnoreCase("L")) {
            txtHShowAdjustmentOf.setText("Credit Entry");
            txtHShowAdjustmentOf.setTag("CE");
        } else {
            txtHShowAdjustmentOf.setText("Debit Entry");
            txtHShowAdjustmentOf.setTag("DE");
        }

        txtHReportOn.setText("Vr Date");
        txtHReportOn.setTag("V");

        txtHReportOnChoice.setText("Details");
        txtHReportOnChoice.setTag("D");

    }

}
