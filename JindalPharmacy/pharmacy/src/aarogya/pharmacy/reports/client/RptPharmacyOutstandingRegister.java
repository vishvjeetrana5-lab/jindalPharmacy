package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.reports.shared.RptPharmacyOutstandingRegister_Srv;
import aarogya.pharmacy.reports.shared.RptPharmacyOutstandingRegister_SrvAsync;
import aarogya.pharmacy.reports.shared.TxtHLMCode_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptPharmacyOutstandingRegister extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmPharmacy().PHARMACY_OUTSTANDING.caption;
    ServiceInvoker<RptPharmacyOutstandingRegister_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHType">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, false);

    private void iniTxtHType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLMCode">
    interface TxtHLMCode_PrpAcs extends KeyField_T_PrpAcs<TxtHLMCode_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHLMCode_Mdl, String> manualCode();

        @Editor.Path("City")
        ValueProvider<TxtHLMCode_Mdl, String> city();

        @Editor.Path("RegNo")
        ValueProvider<TxtHLMCode_Mdl, String> regNo();

        @Editor.Path("AcName")
        ValueProvider<TxtHLMCode_Mdl, String> acName();
    }
    DmTextHelpMulti<TxtHLMCode_Mdl, TxtHLMCode_PrpAcs> txtHLMCode = new DmTextHelpMulti(GWT.create(TxtHLMCode_PrpAcs.class), 6);

    private void iniTxtHLMCode() {
        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().city(), "City", 100, "cityName");
        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().regNo(), "Reg. No.", 100, "regNo");
        txtHLMCode.getDmGrid().addTextColumn(txtHLMCode.getDmGrid().getPropertyAccess().acName(), "Account Name", 100, "acName");
        txtHLMCode.getDmGrid().initializeGrid(txtHLMCode.getDmGrid().getPropertyAccess().code(), 600, 0, 2);
        txtHLMCode.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBalanceType">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHBalanceType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHBalanceType() {
        txtHBalanceType.getDmGrid().addTextColumn(txtHBalanceType.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHBalanceType.getDmGrid().initializeGrid(txtHBalanceType.getDmGrid().getPropertyAccess().code(), 150, 0, 2);
        txtHBalanceType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Site", 200, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSite.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOrganisation">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOrganisation = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHOrganisation() {
        txtHOrganisation.getDmGrid().addTextColumn(txtHOrganisation.getDmGrid().getPropertyAccess().name(), "Name", 200, "name");
        txtHOrganisation.getDmGrid().initializeGrid(txtHOrganisation.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHOrganisation.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGroup">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHGroup() {
        txtHGroup.getDmGrid().addTextColumn(txtHGroup.getDmGrid().getPropertyAccess().name(), "Group By", 200, "name");
        txtHGroup.getDmGrid().initializeGrid(txtHGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGroup">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIncludeOpening = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHIncludeOpening() {
        txtHIncludeOpening.getDmGrid().addTextColumn(txtHIncludeOpening.getDmGrid().getPropertyAccess().name(), "Include Opening", 200, "");
        txtHIncludeOpening.getDmGrid().initializeGrid(txtHIncludeOpening.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHIncludeOpening.getHelper().addSort("name", DmEnum.DmSortType.ASC);
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
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    private void txtHelpEvents() {

        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });

            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHOrganisation.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOrganization(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOrganisation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOrganisation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOrganisation).show();
                        }
                    }
                });

            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHLMCode.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHLMCode_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLMCode(DmShared.xNull(txtHType.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHLMCode_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLMCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHLMCode_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHLMCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLMCode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHBalanceType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBalanceType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBalanceType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBalanceType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBalanceType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getGroupName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIncludeOpening.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIncludeOpening).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIncludeOpening.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIncludeOpening).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    public RptPharmacyOutstandingRegister(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptPharmacyOutstandingRegister_Srv.class), RptPharmacyOutstandingRegister.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        setDefaultValue();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {

        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHSite();
        addControl("site", "Site", vl.add(txtHSite));

        iniTxtHType();
        addControl("type", "Type", vl.add(txtHType));

        iniTxtHOrganisation();
        addControl("organisation", "Organisation", vl.add(txtHOrganisation));

        iniTxtHLMCode();
        addControl("lmCode", "Pharmacy A/c", vl.add(txtHLMCode));

        iniTxtHBalanceType();
        addControl("balanceType", "Balance Type", vl.add(txtHBalanceType));

        iniTxtHGroup();
        addControl("group", "Group By", vl.add(txtHGroup));

        iniTxtHIncludeOpening();
        addControl("includeOpening", "Include Opening", vl.add(txtHIncludeOpening));
        txtHIncludeOpening.setText("Yes");
        txtHIncludeOpening.setTag("1");

        swi.setWidget(this);
    }

    private void setDefaultValue() {
        txtHBalanceType.setTag("N,P");
        txtHBalanceType.setText("Negative Balance,Positive Balance");
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended keyField = result.getData();
                    txtHSite.setTag(keyField.getSharedKeyFieldCode());
                    txtHSite.setText(keyField.getSharedKeyFieldName());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

}
