package dataman.utility.reports.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.libraryMenu.self.shared.lmUtility;
import dataman.utility.reports.shared.RptDepartmentWiseUserReport_Srv;
import dataman.utility.reports.shared.RptDepartmentWiseUserReport_SrvAsync;
import java.util.List;

public class RptDepartmentWiseUserReport extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new lmUtility().DEPARTMENT_WISE_USER_REPORT.caption;
    private final ServiceInvoker<RptDepartmentWiseUserReport_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHUser = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
    
    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Department", 1, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHDepartment.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHIsAdministrator">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsAdministrator = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHIsAdministrator() {
        txtHIsAdministrator.getDmGrid().addTextColumn(txtHIsAdministrator.getDmGrid().getPropertyAccess().name(), "Name", 100, "Name");
        txtHIsAdministrator.getDmGrid().initializeGrid(txtHIsAdministrator.getDmGrid().getPropertyAccess().code(), 130, 0);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHIsActive">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHIsActive() {
        txtHIsActive.getDmGrid().addTextColumn(txtHIsActive.getDmGrid().getPropertyAccess().name(), "Name", 100, "Name");
        txtHIsActive.getDmGrid().initializeGrid(txtHIsActive.getDmGrid().getPropertyAccess().code(), 130, 0);
    }
    //</editor-fold>

    private void iniTxtHUser() {
        txtHUser.getDmGrid().addTextColumn(txtHUser.getDmGrid().getPropertyAccess().name(), "User Name", 1, "Name");
        txtHUser.getDmGrid().initializeGrid(txtHUser.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHUser.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<String>>() {

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
       
    }
    
    private void txthelpEvents(){
        txtHUser.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHUser(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUser).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUser.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUser).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                
            }
        });
        
        txtHDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                
            }
        });
        
        txtHIsActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIsActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsActive).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsActive.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsActive).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        
        txtHIsAdministrator.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIsAdministrator(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsAdministrator).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsAdministrator.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsAdministrator).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    public RptDepartmentWiseUserReport(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptDepartmentWiseUserReport_Srv.class), RptDepartmentWiseUserReport.class.getName());
        setTitle(this.asWidget(), widgetName);
        txthelpEvents();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        
        iniTxtHDepartment();
        addControl("departmentCode", "Department", vl.add(txtHDepartment));

        iniTxtHUser();
        addControl("user", "Users", vl.add(txtHUser));
        
        iniTxtHIsAdministrator();
        addControl("isAdmin", "Administrator", vl.add(txtHIsAdministrator));
//        txtHIsAdministrator.setTag("N");
//        txtHIsAdministrator.setText("No");
        
        iniTxtHIsActive();
        addControl("isActive", "Is Active", vl.add(txtHIsActive));
//        txtHIsActive.setTag("Y");
//        txtHIsActive.setText("Yes");

        swi.setWidget(this);

    }

}
