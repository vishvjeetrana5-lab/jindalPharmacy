package aarogya.mis.reports.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.mis.reports.shared.RptConsultantSharing_Srv;
import aarogya.mis.reports.shared.RptConsultantSharing_SrvAsync;
import aarogya.projectMenu.self.shared.pmMIS;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptConsultantSharing extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new pmMIS().CONSULTANT_SHARING.caption;
    private final ServiceInvoker<RptConsultantSharing_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, false);
    private final DmTextBox txtToDate = new DmTextBox(12, false);

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 3);

    private void iniTxtHNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Nature", 1, "");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 175, 0, 2);
        txtHNature.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, true);

    private void iniTxtHType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReportFor">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHReportFor = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHReportFor() {
        txtHReportFor.getDmGrid().addTextColumn(txtHReportFor.getDmGrid().getPropertyAccess().name(), "Report For", 1, "");
        txtHReportFor.getDmGrid().initializeGrid(txtHReportFor.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHReportFor.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHShareOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHShareOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHShare() {
        txtHShareOn.getDmGrid().addTextColumn(txtHShareOn.getDmGrid().getPropertyAccess().name(), "Share", 100, "");
        txtHShareOn.getDmGrid().initializeGrid(txtHShareOn.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAmountNeeded">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAmountNeeded = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHAmountNeeded() {
        txtHAmountNeeded.getDmGrid().addTextColumn(txtHAmountNeeded.getDmGrid().getPropertyAccess().name(), "Needed", 100, "");
        txtHAmountNeeded.getDmGrid().initializeGrid(txtHAmountNeeded.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHPaymentStatus">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPaymentStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHPaymentStatus() {
        txtHPaymentStatus.getDmGrid().addTextColumn(txtHPaymentStatus.getDmGrid().getPropertyAccess().name(), "Payment Status", 150, "");
        txtHPaymentStatus.getDmGrid().initializeGrid(txtHPaymentStatus.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOrganization">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOrganization = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHOrganization() {
        txtHOrganization.getDmGrid().addTextColumn(txtHOrganization.getDmGrid().getPropertyAccess().name(), "Organization", 1, "name");
        txtHOrganization.getDmGrid().initializeGrid(txtHOrganization.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHOrganization.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRoomCategory">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHRoomCategory = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHRoomCategory() {
        txtHRoomCategory.getDmGrid().addTextColumn(txtHRoomCategory.getDmGrid().getPropertyAccess().name(), "Room Category", 1, "name");
        txtHRoomCategory.getDmGrid().initializeGrid(txtHRoomCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHRoomCategory.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceCategory">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHServiceCategory = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHServiceCategory() {
        txtHServiceCategory.getDmGrid().addTextColumn(txtHServiceCategory.getDmGrid().getPropertyAccess().name(), "Service Category", 1, "name");
        txtHServiceCategory.getDmGrid().initializeGrid(txtHServiceCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHServiceCategory.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceGroup">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHServiceGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHServiceGroup() {
        txtHServiceGroup.getDmGrid().addTextColumn(txtHServiceGroup.getDmGrid().getPropertyAccess().name(), "Service Group", 1, "name");
        txtHServiceGroup.getDmGrid().initializeGrid(txtHServiceGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHServiceGroup.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceName">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHServiceName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHServiceName() {
        txtHServiceName.getDmGrid().addTextColumn(txtHServiceName.getDmGrid().getPropertyAccess().name(), "Service Name", 1, "name");
        txtHServiceName.getDmGrid().initializeGrid(txtHServiceName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHServiceName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
    interface TxtHConsultant_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("sharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> nature();
    }
    final DmTextHelpMulti<KeyFieldExtended, TxtHConsultant_PrpAcs> txtHConsultant = new DmTextHelpMulti(GWT.create(TxtHConsultant_PrpAcs.class), 10);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().nature(), "Nature", 70, "nature");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
        txtHConsultant.getHelper().addSort("name", DmSortType.ASC);
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

    private void txtEvents() {
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
        txtHNature.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {
                    txtHNature.setTag2nd(keyCode);
                }
            }
        });
        txtHReportFor.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
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
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHShareOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSharing(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHShareOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHShareOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHShareOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHAmountNeeded.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAmountNeeded).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAmountNeeded.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAmountNeeded).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHPaymentStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getPaymentStatus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPaymentStatus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPaymentStatus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPaymentStatus).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHOrganization.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOrganization(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOrganization).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOrganization.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOrganization).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHConsultant.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHRoomCategory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRoomCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoomCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoomCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRoomCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHServiceCategory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHServiceGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHServiceName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

    }

    public RptConsultantSharing(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptConsultantSharing_Srv.class), RptConsultantSharing.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtEvents();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        addControl("fromDate", "From Date", vl.add(txtFromDate));

        addControl("toDate", "To Date", vl.add(txtToDate));

        iniTxtHType();
        addControl("type", "Type", vl.add(txtHType));

        iniTxtHNature();
        addControl("nature", "Nature", vl.add(txtHNature));

        iniTxtHConsultant();
        addControl("consultant", "Consultant", vl.add(txtHConsultant));

        iniTxtHReportFor();
        addControl("reportFor", "Report For", vl.add(txtHReportFor));

        iniTxtHShare();
        addControl("share", "Sharing On", vl.add(txtHShareOn));

        iniTxtHAmountNeeded();
        addControl("amountNeeded", "Amount Needed", vl.add(txtHAmountNeeded));
        
        iniTxtHPaymentStatus();
        addControl("paymentStatus", "Payment Status", vl.add(txtHPaymentStatus));

        iniTxtHOrganization();
        addControl("organization", "Organization", vl.add(txtHOrganization));

        iniTxtHRoomCategory();
        addControl("roomCategory", "Room Category", vl.add(txtHRoomCategory));

        iniTxtHServiceCategory();
        addControl("serviceCategory", "Service Category", vl.add(txtHServiceCategory));

        iniTxtHServiceGroup();
        addControl("serviceGroup", "Service Group", vl.add(txtHServiceGroup));

        iniTxtHServiceName();
        addControl("serviceName", "Service Name", vl.add(txtHServiceName));

        swi.setWidget(this);
        setDefaultValue();
    }

    private void setDefaultValue() {
        txtFromDate.setText(DmClient.getStartDate());
        txtToDate.setText(DmClient.getLoginDate());
        txtHType.setTag("1");
        txtHType.setText("Category Wise Summary");
        txtHShareOn.setTag("1");
        txtHShareOn.setText("After Discount");
        txtHAmountNeeded.setTag("1");
        txtHAmountNeeded.setText("Yes");
        txtHPaymentStatus.setTag("0");
        txtHPaymentStatus.setText("Not Applicable");
    }

}
