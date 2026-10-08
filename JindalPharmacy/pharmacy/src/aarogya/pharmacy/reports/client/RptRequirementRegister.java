package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.medicineRequirement.shared.SearchValue_Mdl;
import aarogya.pharmacy.reports.shared.RptRequirementRegister_Srv;
import aarogya.pharmacy.reports.shared.RptRequirementRegister_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class RptRequirementRegister extends DmReportLayout implements IsWidget {

    final String widgetName = "Medicine Requirement Register";
    final ServiceInvoker<RptRequirementRegister_SrvAsync> srvAsync;
    final SearchValue_Mdl svm;
    final PopupPanel ppForm = new PopupPanel();
    final DmTextBox txtAsOnDate = new DmTextBox();
    final DmTextBox txtMovementFrom = new DmTextBox();
    final DmTextBox txtDepartment = new DmTextBox();
    final DmTextBox txtMedicineCompany = new DmTextBox();
    final DmTextBox txtMedicineGroup = new DmTextBox();
    final DmTextBox txtMedicineCategory = new DmTextBox();
    final DmTextBox txtMedicine = new DmTextBox();
    final DmTextBox txtGroupOn = new DmTextBox();
    boolean flagOpenAsPopup = false;

    @Override
    public void onClose() {
        if (flagOpenAsPopup) {
            ppForm.hide();
        } else {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
        }
    }

    @Override
    public void onView() {
        srvAsync.invokeRPC(true).execute(getFilter(), reportProperty, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    if (flagOpenAsPopup) {
                        getReport(result.getData(), flagOpenAsPopup);
                    } else {
                        getReport(result.getData());
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public RptRequirementRegister(SearchValue_Mdl svm, boolean flagOpenAsPopup, SetWidgetInvoker swi) {
        super();
        this.svm = svm;
        this.flagOpenAsPopup = flagOpenAsPopup;
        this.srvAsync = new ServiceInvoker<>(GWT.create(RptRequirementRegister_Srv.class), RptRequirementRegister.class.getName());
        setTitle(this.asWidget(), widgetName);
        manageDisplay();
        setDefaultValues();
        initialize();
        swi.setWidget(this);
    }

    private void manageDisplay() {
        txtAsOnDate.setEnabled(false);
        txtMovementFrom.setEnabled(false);
        txtDepartment.setEnabled(false);
        txtMedicineCompany.setEnabled(false);
        txtMedicineGroup.setEnabled(false);
        txtMedicineCategory.setEnabled(false);
        txtMedicine.setEnabled(false);
        txtGroupOn.setEnabled(false);
    }

    private void setDefaultValues() {
        txtAsOnDate.setText(DmShared.dNull(svm.getAsOnDate()));
        txtMovementFrom.setText(DmShared.dNull(svm.getMovementFrom()));
        txtDepartment.setTag(DmShared.xNull(svm.getDepartmentCode()));
        txtDepartment.setText(DmShared.xNull(svm.getDepartmentName()));
        txtMedicineCompany.setTag(DmShared.xNull(svm.getCompanyCode()));
        txtMedicineCompany.setText(DmShared.xNull(svm.getCompanyName()));
        txtMedicineGroup.setTag(DmShared.xNull(svm.getGroupCode()));
        txtMedicineGroup.setText(DmShared.xNull(svm.getGroupName()));
        txtMedicineCategory.setTag(DmShared.xNull(svm.getCategoryCode()));
        txtMedicineCategory.setText(DmShared.xNull(svm.getCategoryName()));
        txtMedicine.setTag(DmShared.xNull(svm.getMedicineCode()));
        txtMedicine.setText(DmShared.xNull(svm.getMedicineName()));
        txtGroupOn.setTag(DmShared.xNull(svm.getGroupOnCode()));
        txtGroupOn.setText(DmShared.xNull(svm.getGroupOnName()));
    }

    private void initialize() {
        addControl("asOnDate", "As On Date", txtAsOnDate);
        addControl("movementFrom", "Movement From", txtMovementFrom);
        addControl("department", "Department", txtDepartment);
        addControl("medMake", "Medicine Company", txtMedicineCompany);
        addControl("medGroup", "Medicine Group", txtMedicineGroup);
        addControl("medCat", "Medicine Category", txtMedicineCategory);
        addControl("medicine", "Medicine", txtMedicine);
        addControl("groupBy", "Group On", txtGroupOn);

        if (flagOpenAsPopup) {
            ppForm.clear();
            form.getElement().setAttribute("Style", "width:980px; height:500px;");

            Element e = form.getElement().getElementsByTagName("iframe").getItem(0);
            e.getStyle().setWidth(980, Style.Unit.PX);
            e.getStyle().setHeight(474, Style.Unit.PX);
            ppForm.add(form);
            ppForm.setGlassEnabled(true);
            ppForm.center();
        }

        onView();
    }

    @Override
    public Widget asWidget() {
        if (flagOpenAsPopup) {
            return ppForm;
        } else {
            return form;
        }
    }

}
