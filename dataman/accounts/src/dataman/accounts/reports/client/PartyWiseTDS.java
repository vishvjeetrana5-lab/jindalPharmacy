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
import dataman.accounts.reports.shared.PartyWiseTDS_Model;
import dataman.accounts.reports.shared.PartyWiseTDS_Srv;
import dataman.accounts.reports.shared.PartyWiseTDS_SrvAsync;
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

public class PartyWiseTDS extends DmReportLayout implements IsWidget, BlurHandler {

    ServiceInvoker<PartyWiseTDS_SrvAsync> SrvAsync;
    final String widgetName = new lmAccounts().PARTY_WISE_TDS.caption; 
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(11, true);
    DmTextBox txtToDate = new DmTextBox(11, true);

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    interface TxtHAccountName_PrpAcs extends KeyField_T_PrpAcs<PartyWiseTDS_Model.TxtHAccountName> {

        @Editor.Path("ManualCode")
        ValueProvider<PartyWiseTDS_Model.TxtHAccountName, String> manualCode();

        @Editor.Path("City")
        ValueProvider<PartyWiseTDS_Model.TxtHAccountName, String> city();
    }
    DmTextHelpMulti<PartyWiseTDS_Model.TxtHAccountName, TxtHAccountName_PrpAcs> txtHAccountName = new DmTextHelpMulti(GWT.create(TxtHAccountName_PrpAcs.class), 10);

    interface TxtHTDSDeductFrom_PrpAcs extends KeyField_T_PrpAcs<PartyWiseTDS_Model.TxtHTDSDeductFrom> {

        @Editor.Path("ManualCode")
        ValueProvider<PartyWiseTDS_Model.TxtHTDSDeductFrom, String> manualCode();

        @Editor.Path("City")
        ValueProvider<PartyWiseTDS_Model.TxtHTDSDeductFrom, String> city();
    }
    DmTextHelpMulti<PartyWiseTDS_Model.TxtHTDSDeductFrom, TxtHTDSDeductFrom_PrpAcs> txtHTDSDeductFrom = new DmTextHelpMulti(GWT.create(TxtHTDSDeductFrom_PrpAcs.class), 10);

    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHCategoryName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSeperate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHWithNarration = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPartyType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 2);

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Code", 180, "ManualCode");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().city(), "City", 120, "City");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 500, 0, 1); //244
        txtHAccountName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("ManualCode", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHTDSDeductFrom() {
        txtHTDSDeductFrom.getDmGrid().addTextColumn(txtHTDSDeductFrom.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHTDSDeductFrom.getDmGrid().addTextColumn(txtHTDSDeductFrom.getDmGrid().getPropertyAccess().manualCode(), "Code", 180, "ManualCode");
        txtHTDSDeductFrom.getDmGrid().addTextColumn(txtHTDSDeductFrom.getDmGrid().getPropertyAccess().city(), "City", 120, "City");
        txtHTDSDeductFrom.getDmGrid().initializeGrid(txtHTDSDeductFrom.getDmGrid().getPropertyAccess().code(), 500, 0, 1); //244
        txtHTDSDeductFrom.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHTDSDeductFrom.getHelper().addSort("ManualCode", DmEnum.DmSortType.ASC);
    }

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

    private void iniTxtHWithNarration() {
        txtHWithNarration.getDmGrid().addTextColumn(txtHWithNarration.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHWithNarration.getDmGrid().initializeGrid(txtHWithNarration.getDmGrid().getPropertyAccess().code(), 200, 0, 1); 
        txtHWithNarration.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    private void iniTxtHPartyType() {
        txtHPartyType.getDmGrid().addTextColumn(txtHPartyType.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHPartyType.getDmGrid().initializeGrid(txtHPartyType.getDmGrid().getPropertyAccess().code(), 200, 0, 2); //244
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

        txtHAccountName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<PartyWiseTDS_Model.TxtHAccountName>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<PartyWiseTDS_Model.TxtHAccountName>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<PartyWiseTDS_Model.TxtHAccountName>> result) {
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

        txtHTDSDeductFrom.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<PartyWiseTDS_Model.TxtHTDSDeductFrom>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTDSDeductFrom(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<PartyWiseTDS_Model.TxtHTDSDeductFrom>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTDSDeductFrom).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<PartyWiseTDS_Model.TxtHTDSDeductFrom>> result) {
                        if (result.isStatus()) {
                            txtHTDSDeductFrom.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTDSDeductFrom).show();
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

    public PartyWiseTDS(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<PartyWiseTDS_SrvAsync>(GWT.create(PartyWiseTDS_Srv.class), PartyWiseTDS.class.getName());
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

        iniTxtHAccountName();
        addControl("accountName", "Account Name", vl.add(txtHAccountName));

        iniTxtHCategoryName();
        addControl("categoryName", "Category Name", vl.add(txtHCategoryName));

        iniTxtHSeperate();

        addControl("seperatePage", "Seperate Page", vl.add(txtHSeperate));

        iniTxtHTDSDeductFrom();
        addControl("tdsDeductFrom", "TDS Deduct From", vl.add(txtHTDSDeductFrom));

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
        txtHSeperate.setTag("Y");
        txtHSeperate.setText("Yes");

        txtHWithNarration.setTag("N");
        txtHWithNarration.setText("No");

    }

}
