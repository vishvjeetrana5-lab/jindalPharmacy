package dataman.payroll.reports.client;

import dataman.libraryMenu.self.shared.lmPayroll;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
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
import dataman.payroll.reports.shared.RptDeptWiseSalarySheet_Srv;
import dataman.payroll.reports.shared.RptDeptWiseSalarySheet_SrvAsync;
import java.util.HashMap;
import java.util.List;

public class RptDeptWiseSalarySheet extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new lmPayroll().DEPT_WISE_SALARY_SHEET_REPORT.caption;
    private final ServiceInvoker<RptDeptWiseSalarySheet_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();

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

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Department", 1, "departmentName");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 225, 0, 2);
        txtHDepartment.getHelper().addSort("departmentName", DmSortType.ASC);
    }
    //</editor-fold>

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSalaryMethod = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHSalaryMethod() {
        txtHSalaryMethod.getDmGrid().addTextColumn(txtHSalaryMethod.getDmGrid().getPropertyAccess().name(), "Salary Method", 180, "name");
        txtHSalaryMethod.getDmGrid().initializeGrid(txtHSalaryMethod.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHSalaryMethod.getHelper().addSort("name", DmSortType.ASC);
    }

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(), getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended kf = result.getData();
                    startDownloading(DmShared.xNull(kf.getSharedKeyFieldName()), DmShared.xNull(kf.getSharedKeyFieldCode()));
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
    }

    private void txtHelpEvents() {

        txtHEmployee.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<Employee_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEmployee(txtHDepartment.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Employee_Mdl>>>() {

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

        txtHSalaryMethod.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSalaryMethod(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSalaryMethod).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSalaryMethod.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSalaryMethod).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

    }

    public RptDeptWiseSalarySheet(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<RptDeptWiseSalarySheet_SrvAsync>(GWT.create(RptDeptWiseSalarySheet_Srv.class), RptDeptWiseSalarySheet.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtHelpEvents();
        setDefaultValue();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        removePDFButton();
        removeWordButton();

        iniTxtHSalaryMethod();
        addControl("salaryMethod", "Salary Method", vl.add(txtHSalaryMethod));

        iniTxtHDepartment();
        addControl("department", "Department ", vl.add(txtHDepartment));

        iniTxtHEmployee();
        addControl("employees", "Employees", vl.add(txtHEmployee));

        swi.setWidget(this);

    }

    private void setDefaultValue() {

    }

}
