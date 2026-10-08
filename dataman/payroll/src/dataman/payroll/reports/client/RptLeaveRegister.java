package dataman.payroll.reports.client;


import dataman.payroll.reports.shared.RptLeaveRegister_Srv;
import dataman.payroll.reports.shared.RptLeaveRegister_SrvAsync;
import dataman.libraryMenu.self.shared.lmPayroll;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.payroll.reports.shared.Employee_Mdl;
import dataman.payroll.reports.shared.LeaveTextHelp_Mdl;
import java.util.List;

public class RptLeaveRegister extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new lmPayroll().LEAVE_REGISTER_REPORT.caption;
    private final ServiceInvoker<RptLeaveRegister_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(11, true);
    private final DmTextBox txtToDate = new DmTextBox(11, true);

   interface TxtHLeave_PrpAcs extends KeyField_T_PrpAcs<LeaveTextHelp_Mdl> {

        @Editor.Path("Leave")
        ValueProvider<LeaveTextHelp_Mdl, String> leave();

        @Editor.Path("ShortName")
        ValueProvider<LeaveTextHelp_Mdl, String> shortName();
    }

    final DmTextHelpMulti<LeaveTextHelp_Mdl, TxtHLeave_PrpAcs> txtHLeave = new DmTextHelpMulti(com.google.gwt.core.shared.GWT.create(TxtHLeave_PrpAcs.class), 5);

    private void iniTxtHLeave() {
        txtHLeave.getDmGrid().addTextColumn(txtHLeave.getDmGrid().getPropertyAccess().leave(), "Leave", 150, "name");
        txtHLeave.getDmGrid().addTextColumn(txtHLeave.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "manualCode");
        txtHLeave.getDmGrid().initializeGrid(txtHLeave.getDmGrid().getPropertyAccess().code(), 300, 0,1);
        txtHLeave.getHelper().addSort("name", DmSortType.ASC);
    }

 interface TxtHEmployee_PrpAcs extends KeyField_T_PrpAcs<Employee_Mdl> {

        @Editor.Path("Employee")
        ValueProvider<Employee_Mdl, String> employee();

        @Editor.Path("Id")
        ValueProvider<Employee_Mdl, String> id();

        @Editor.Path("FatherName")
        ValueProvider<Employee_Mdl, String> fatherName();

        @Editor.Path("Department")
        ValueProvider<Employee_Mdl, String> department();

        @Editor.Path("DOJ")
        ValueProvider<Employee_Mdl, String> DOJ();
    }

    final DmTextHelpMulti<Employee_Mdl, TxtHEmployee_PrpAcs> txtHEmployee = new DmTextHelpMulti(com.google.gwt.core.shared.GWT.create(TxtHEmployee_PrpAcs.class), 10);

    private void iniTxtHEmployee() {
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().employee(), "Employee", 150, "Employee");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().id(), "ID", 100, "");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().fatherName(), "Father Name", 100, "fatherName");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().department(), "Department", 100, "department");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().DOJ(), "DOJ", 100, "doj");
        txtHEmployee.getDmGrid().initializeGrid(txtHEmployee.getDmGrid().getPropertyAccess().code(), 570, 300);
        txtHEmployee.getHelper().addSort("Employee", DmSortType.ASC);
    }

    //<editor-fold defaultstate="collapsed" desc="txtHReportType">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHReportType() {
        txtHReportType.getDmGrid().addTextColumn(txtHReportType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHReportType.getDmGrid().initializeGrid(txtHReportType.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Department", 1, "departmentName");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 225, 0, 2);
        txtHDepartment.getHelper().addSort("departmentName", DmSortType.ASC);
    }
    //</editor-fold>


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
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    private void txtHelpEvents() {
        txtHLeave.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<LeaveTextHelp_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLeaves(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<LeaveTextHelp_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLeave).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<LeaveTextHelp_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHLeave.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLeave).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHEmployee.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<Employee_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEmployee(txtHDepartment.getTag(),helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Employee_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEmployee).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Employee_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEmployee.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEmployee).show();
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
                SrvAsync.invokeRPC(blnCallBackWait).getReportType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
       
      
        txtHDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

    }

    public RptLeaveRegister(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<RptLeaveRegister_SrvAsync>(GWT.create(RptLeaveRegister_Srv.class), RptLeaveRegister.class.getName());
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
        txtFromDate.setText(DmClient.getLoginDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHDepartment();
        addControl("department", "Department ", vl.add(txtHDepartment));

        iniTxtHEmployee();
        addControl("employees", "Employees", vl.add(txtHEmployee));

        iniTxtHLeave();
        addControl("leaves", "Leaves", vl.add(txtHLeave));

        iniTxtHReportType();
        addControl("reportType", "Report Type", vl.add(txtHReportType));

      
        swi.setWidget(this);

    }

    private void setDefaultValue() {
        txtHReportType.setTag("S");
        txtHReportType.setText("Summary");
    }

}
