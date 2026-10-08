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
import dataman.accounts.reports.shared.OutstandingDebtorsFIFO_Model;
import dataman.accounts.reports.shared.OutstandingDebtorsFIFO_Srv;
import dataman.accounts.reports.shared.OutstandingDebtorsFIFO_SrvAsync;
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
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class OutstandingDebtorsFIFO extends DmReportLayout implements IsWidget, BlurHandler {

    ServiceInvoker<OutstandingDebtorsFIFO_SrvAsync> SrvAsync;
    final String widgetName = new lmAccounts().OUTSTANDING_DEBTORS_FIFO.caption; 
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtAsOnDate = new DmTextBox();
    DmTextBoxNumeric txtInterval = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 11, true);

    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHAccountGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportFor = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 150, "ManualCode");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().city(), "City", 150, "City");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().groupName(), "Group Name", 150, "GroupName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().zone(), "Zone", 150, "Zone");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 850, 0, 2);
        txtHAccountName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("ManualCode", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("City", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 1); //244
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Name", 150, "GroupName");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 1); //244
        txtHAccountGroup.getHelper().addSort("GroupName", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHReportFor() {
        txtHReportFor.getDmGrid().addTextColumn(txtHReportFor.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHReportFor.getDmGrid().initializeGrid(txtHReportFor.getDmGrid().getPropertyAccess().code(), 200, 0, 1); //244
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtAsOnDate) {
            txtAsOnDate.setText(DmClient.returnDate(txtAsOnDate.getText()));
        }
    }

    interface TxtHAccountName_PrpAcs extends KeyField_T_PrpAcs<OutstandingDebtorsFIFO_Model.TxtHAccountName> {

        @Editor.Path("ManualCode")
        ValueProvider<OutstandingDebtorsFIFO_Model.TxtHAccountName, String> manualCode();

        @Editor.Path("City")
        ValueProvider<OutstandingDebtorsFIFO_Model.TxtHAccountName, String> city();

        @Editor.Path("GroupName")
        ValueProvider<OutstandingDebtorsFIFO_Model.TxtHAccountName, String> groupName();

        @Editor.Path("Zone")
        ValueProvider<OutstandingDebtorsFIFO_Model.TxtHAccountName, String> zone();

    }
    DmTextHelpMulti<OutstandingDebtorsFIFO_Model.TxtHAccountName, TxtHAccountName_PrpAcs> txtHAccountName = new DmTextHelpMulti(GWT.create(TxtHAccountName_PrpAcs.class), 10);

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

        txtHSiteName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {

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

        txtHReportFor.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReportFor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportFor).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportFor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportFor).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Object t1Row) {

            }
        });

        txtHAccountGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {

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

        txtHAccountName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<OutstandingDebtorsFIFO_Model.TxtHAccountName>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<OutstandingDebtorsFIFO_Model.TxtHAccountName>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<OutstandingDebtorsFIFO_Model.TxtHAccountName>> result) {
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

    }

    public OutstandingDebtorsFIFO(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<OutstandingDebtorsFIFO_SrvAsync>(GWT.create(OutstandingDebtorsFIFO_Srv.class), OutstandingDebtorsFIFO.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtEvents();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        txtAsOnDate.setMandatory(true);
        addControl("asOnDate", "As On Date", vl.add(txtAsOnDate));
        txtAsOnDate.setText(DmClient.getLoginDate());
        txtAsOnDate.setMaxLength(11);

        iniTxtHAccountGroup();
        addControl("accountGroup", "Account Group", vl.add(txtHAccountGroup));

        iniTxtHAccountName();
        addControl("accountName", "Account Name", vl.add(txtHAccountName));

        addControl("interval", "Interval", vl.add(txtInterval));

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

        iniTxtHReportFor();
        addControl("reportFor", "Report For", vl.add(txtHReportFor));

        defaultValue();
        swi.setWidget(this);
    }

    private void defaultValue() {
        txtInterval.setText("180");

        txtHReportFor.setText("Both");
        txtHReportFor.setTag("B");

    }

}
