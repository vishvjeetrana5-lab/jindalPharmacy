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
import dataman.accounts.reports.shared.AnnexureCC_Model;
import dataman.accounts.reports.shared.AnnexureCC_Srv;
import dataman.accounts.reports.shared.AnnexureCC_SrvAsync;
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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class AnnexureCC extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new lmAccounts().ANNEXURE_CC.caption;
    ServiceInvoker<AnnexureCC_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtUpToDate = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="TxtHSite">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSiteName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHSiteName() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().code(), 200, 0, 2); //244
        txtHSiteName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHLedgerGroup">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHLedgerGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHLedgerGroup() {
        txtHLedgerGroup.getDmGrid().addTextColumn(txtHLedgerGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "LegerName");
        txtHLedgerGroup.getDmGrid().initializeGrid(txtHLedgerGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2); //244
        txtHLedgerGroup.getHelper().addSort("LegerName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHCostCenter">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHCostCenter = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    public void iniTxtHCostCenter() {
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHCostCenter.getDmGrid().initializeGrid(txtHCostCenter.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHCostCenter.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHReportType">

    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);

    public void iniTxtHReportType() {
        txtHReportType.getDmGrid().addTextColumn(txtHReportType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHReportType.getDmGrid().initializeGrid(txtHReportType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHAccountGroup">
    interface TxtHAccountGroup_PrpAcs extends KeyField_T_PrpAcs<AnnexureCC_Model.TxtHAccountGroup> {

        @Editor.Path("MainGroup")
        ValueProvider<AnnexureCC_Model.TxtHAccountGroup, String> mainGroup();

    }
    DmTextHelpMulti<AnnexureCC_Model.TxtHAccountGroup, TxtHAccountGroup_PrpAcs> txtHAccountGroup = new DmTextHelpMulti(GWT.create(TxtHAccountGroup_PrpAcs.class), 10);

    private void iniTxtHAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().mainGroup(), "Main Group", 150, "MainGroup");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 325, 0, 2);

        txtHAccountGroup.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

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
        txtHLedgerGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {

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
        txtHAccountGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<AnnexureCC_Model.TxtHAccountGroup>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<AnnexureCC_Model.TxtHAccountGroup>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AnnexureCC_Model.TxtHAccountGroup>> result) {
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
        txtHCostCenter.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCostCenter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCostCenter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCostCenter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCostCenter).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHReportType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReportType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    public AnnexureCC(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(AnnexureCC_Srv.class), AnnexureCC.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtUpToDate.addBlurHandler(this);
        txtEvents();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        addControl("upToDate", "Up To Date", vl.add(txtUpToDate));
        txtUpToDate.setText(DmClient.getLoginDate());

        iniTxtHAccountGroup();
        addControl("accountGroup", "Account Group", vl.add(txtHAccountGroup));

        iniTxtHLedgerGroup();
        addControl("ledgerGroup", "Ledger Group", vl.add(txtHLedgerGroup));

        iniTxtHSiteName();
        addControl("siteName", "Site Name", vl.add(txtHSiteName));

        iniTxtHCostCenter();
        addControl("costCenter", "Cost Center", vl.add(txtHCostCenter));

        iniTxtHReportType();
        txtHReportType.setMandatory(true);
        txtHReportType.setTag("CC");
        txtHReportType.setText("Cost Center");
        addControl("reportType", "Report Type", vl.add(txtHReportType));
        swi.setWidget(this);
    }

}
