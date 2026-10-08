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
import dataman.accounts.reports.shared.*;
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
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class AgeingAnalysisBillWise extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new lmAccounts().AGEING_AYALYSIS_BILL_WISE.caption;
    ServiceInvoker<AgeingAnalysisBillWise_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtAsOnDate = new DmTextBox();
    DmTextBoxNumeric txtInterval = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 10, false);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHAccountGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    interface TxtHAccountName_PrpAcs extends KeyField_T_PrpAcs<AgeingAnalysisBillWise_Model.TxtHAccountName> {

        @Editor.Path("ManualCode")
        ValueProvider<AgeingAnalysisBillWise_Model.TxtHAccountName, String> manualCode();

        @Editor.Path("City")
        ValueProvider<AgeingAnalysisBillWise_Model.TxtHAccountName, String> city();

        @Editor.Path("GroupName")
        ValueProvider<AgeingAnalysisBillWise_Model.TxtHAccountName, String> groupName();

    }
    DmTextHelpMulti<AgeingAnalysisBillWise_Model.TxtHAccountName, TxtHAccountName_PrpAcs> txtHAccountName = new DmTextHelpMulti(GWT.create(TxtHAccountName_PrpAcs.class), 10);

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 150, "ManualCode");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().city(), "City", 150, "City");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().groupName(), "Group Name", 150, "GroupName");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 600, 0, 2);
        txtHAccountName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("ManualCode", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("City", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2); //244
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "GroupName");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2); //244
        txtHAccountGroup.getHelper().addSort("GroupName", DmEnum.DmSortType.ASC);
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
        if (event.getSource() == txtAsOnDate) {
            txtAsOnDate.setText(DmClient.returnDate(txtAsOnDate.getText()));
        }
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

        txtHAccountName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<AgeingAnalysisBillWise_Model.TxtHAccountName>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<AgeingAnalysisBillWise_Model.TxtHAccountName>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AgeingAnalysisBillWise_Model.TxtHAccountName>> result) {
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

    public AgeingAnalysisBillWise(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<AgeingAnalysisBillWise_SrvAsync>(GWT.create(AgeingAnalysisBillWise_Srv.class), AgeingAnalysisBillWise.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtAsOnDate.addBlurHandler(this);
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

        txtInterval.setMandatory(true);
        addControl("interval", "Interval", vl.add(txtInterval));
        txtInterval.setText("180");
        txtInterval.setMaxLength(11);

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

        swi.setWidget(this);
    }

}
