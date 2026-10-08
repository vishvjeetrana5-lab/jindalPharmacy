package dataman.accounts.reports.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.reports.shared.TDSReport_Srv;
import dataman.accounts.reports.shared.TDSReport_SrvAsync;
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

public class TDSReport extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new lmAccounts().TDS_REPORT.caption;
    ServiceInvoker<TDSReport_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(11, true);
    DmTextBox txtToDate = new DmTextBox(11, true);

    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHCategoryName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSeperate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPartyWise = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHWithNarration = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPartyType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 2);

    private void iniTxtHCategoryName() {
        txtHCategoryName.getDmGrid().addTextColumn(txtHCategoryName.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHCategoryName.getDmGrid().initializeGrid(txtHCategoryName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHCategoryName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2); //244
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHSeperate() {
        txtHSeperate.getDmGrid().addTextColumn(txtHSeperate.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHSeperate.getDmGrid().initializeGrid(txtHSeperate.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHSeperate.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHPartyWise() {
        txtHPartyWise.getDmGrid().addTextColumn(txtHPartyWise.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHPartyWise.getDmGrid().initializeGrid(txtHPartyWise.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHPartyWise.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHWithNarration() {
        txtHWithNarration.getDmGrid().addTextColumn(txtHWithNarration.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHWithNarration.getDmGrid().initializeGrid(txtHWithNarration.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHWithNarration.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHPartyType() {
        txtHPartyType.getDmGrid().addTextColumn(txtHPartyType.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHPartyType.getDmGrid().initializeGrid(txtHPartyType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHPartyType.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
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
        txtHCategoryName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCategoryName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCategoryName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCategoryName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCategoryName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

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

        txtHSeperate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSeperate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSeperate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSeperate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSeperate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHPartyWise.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPartyWise(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPartyWise).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPartyWise.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPartyWise).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHWithNarration.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHWithNarration(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHWithNarration).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHWithNarration.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHWithNarration).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHPartyType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPartyType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPartyType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPartyType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPartyType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

    }

    public TDSReport(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<TDSReport_SrvAsync>(GWT.create(TDSReport_Srv.class), TDSReport.class.getName());
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

        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHCategoryName();
        addControl("categoryName", "Category Name", vl.add(txtHCategoryName));

        txtHSeperate.setMandatory(true);
        iniTxtHSeperate();
        addControl("seperatePage", "Seperate Page", vl.add(txtHSeperate));

        txtHPartyWise.setMandatory(true);
        iniTxtHPartyWise();
        addControl("partyWise", "Party Wise", vl.add(txtHPartyWise));

        txtHWithNarration.setMandatory(true);
        iniTxtHWithNarration();
        addControl("withNarration", "With Narration", vl.add(txtHWithNarration));

        iniTxtHPartyType();
        addControl("partyType", "Party Type", vl.add(txtHPartyType));

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

        defaultValue();
        swi.setWidget(this);
    }

    private void defaultValue() {
        txtHSeperate.setTag("N");
        txtHSeperate.setText("No");

        txtHWithNarration.setTag("N");
        txtHWithNarration.setText("No");

        txtHPartyWise.setTag("Y");
        txtHPartyWise.setText("Yes");
    }
}
