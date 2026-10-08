package dataman.accounts.reports.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.reports.shared.AgeingAnalysisFIFO_Srv;
import dataman.accounts.reports.shared.AgeingAnalysisFIFO_SrvAsync;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class AgeingAnalysisFIFO extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new lmAccounts().AGEING_AYALYSIS_FIFO.caption;
    ServiceInvoker<AgeingAnalysisFIFO_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtUpToDate = new DmTextBox(11, true);
    DmTextBoxNumeric txt1Interval = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 10, false);
    DmTextBoxNumeric txt2Interval = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 10, false);
    DmTextBoxNumeric txt3Interval = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 10, false);
    DmTextBoxNumeric txt4Interval = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 10, false);
    DmTextBoxNumeric txt5Interval = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 10, false);
    DmTextBoxNumeric txt6Interval = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 10, false);

    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHAccountType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10, false);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHShowRecords = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportOnChoice = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHAccountType() {
        txtHAccountType.getDmGrid().addTextColumn(txtHAccountType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHAccountType.getDmGrid().initializeGrid(txtHAccountType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHAccountType.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHShowRecords() {
        txtHShowRecords.getDmGrid().addTextColumn(txtHShowRecords.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHShowRecords.getDmGrid().initializeGrid(txtHShowRecords.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }

    private void iniTxtHSiteReportOnChoice() {
        txtHReportOnChoice.getDmGrid().addTextColumn(txtHReportOnChoice.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHReportOnChoice.getDmGrid().initializeGrid(txtHReportOnChoice.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
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
        if (event.getSource() == txtUpToDate) {
            txtUpToDate.setText(DmClient.returnDate(txtUpToDate.getText()));
        }
    }

    private void txtEvents() {

        txtHAccountType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAccountType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountType).show();
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

        txtHShowRecords.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHShowRecords(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHShowRecords).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHShowRecords.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHShowRecords).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
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

    }

    public AgeingAnalysisFIFO(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(AgeingAnalysisFIFO_Srv.class), AgeingAnalysisFIFO.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtUpToDate.addBlurHandler(this);
        txtEvents();
        initialize(swi);
        txtHReportOnChoice.setEnabled(false);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        addControl("upToDate", "Up To Date", vl.add(txtUpToDate));
        txtUpToDate.setText(DmClient.getLoginDate());

        iniTxtHAccountType();
        addControl("accountType", "Account Type", vl.add(txtHAccountType));

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

        txt1Interval.setMandatory(true);
        addControl("iInterval", "I Interval", vl.add(txt1Interval));
        txt1Interval.setMaxLength(11);

        txt2Interval.setMandatory(true);
        addControl("iiInterval", "II Interval", vl.add(txt2Interval));
        txt2Interval.setMaxLength(11);

        txt3Interval.setMandatory(true);
        addControl("iiiInterval", "III Interval", vl.add(txt3Interval));
        txt3Interval.setMaxLength(11);

        txt4Interval.setMandatory(true);
        addControl("ivInterval", "IV Interval", vl.add(txt4Interval));
        txt4Interval.setMaxLength(11);

        txt5Interval.setMandatory(true);
        addControl("vInterval", "V Interval", vl.add(txt5Interval));
        txt5Interval.setMaxLength(11);

        txt6Interval.setMandatory(true);
        addControl("viInterval", "VI Interval", vl.add(txt6Interval));
        txt6Interval.setMaxLength(11);

        iniTxtHShowRecords();
        txtHShowRecords.setMandatory(true);
        addControl("showRecords", "Show Records", vl.add(txtHShowRecords));

        iniTxtHSiteReportOnChoice();
        addControl("reportOnChoice", "Report on Choice", vl.add(txtHReportOnChoice));

        defaultValue();
        swi.setWidget(this);
    }

    private void defaultValue() {
        txtHReportOnChoice.setText("Account Name Wise");
        txtHReportOnChoice.setTag("AC");
        
        
        txtHShowRecords.setText("All");
        txtHShowRecords.setTag("A");

        txt6Interval.setText("180");
        txt5Interval.setText("150");
        txt4Interval.setText("120");
        txt3Interval.setText("90");
        txt2Interval.setText("60");
        txt1Interval.setText("30");
    }

}
