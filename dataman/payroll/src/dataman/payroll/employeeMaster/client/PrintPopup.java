package dataman.payroll.employeeMaster.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import java.util.HashMap;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.libraryMenu.self.shared.lmPayroll;

public class PrintPopup extends PrintPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public PrintPopup() {
        super();
        eventHandler();
    }

    public interface CallBack {

        public void hide(boolean isCancel);
    }

    private CallBack callBack;
    private EmployeeMaster employeeMaster;
    private String moduleName = new lmPayroll().moduleName;
    private String voucherTypeTag;
    SetWidgetInvoker swi;

    private void eventHandler() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        btnEmployeeCard.addClickHandler(this);
        btnSalaryCertificate.addClickHandler(this);
        btnExperienceCertificate.addClickHandler(this);
        cbEmployeeCardPrint.addClickHandler(this);
        cbExperienceCertificatePrint.addClickHandler(this);
        cbSalaryCertificatePrint.addClickHandler(this);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(EmployeeMaster employeeMaster) {
        this.employeeMaster = employeeMaster;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        btnOk.setFocus(true);
        flwExperienceCertificate.clear();
        flwcbExperienceCertificate.clear();

    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void hide(boolean isCancel) {
        hide();
        cbEmployeeCardPrint.setChecked(false);
        cbExperienceCertificatePrint.setChecked(false);
        cbSalaryCertificatePrint.setChecked(false);
        callBack.hide(isCancel);
        

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnEmployeeCard ) {
            print(false, btnEmployeeCard);
            hide(false);
        } else if (event.getSource() == btnSalaryCertificate) {
            manageDisplay();
            print(false, btnSalaryCertificate);
            hide(false);
        }else if (event.getSource() == cbSalaryCertificatePrint) {
                manageDisplay();             
            }
        
        else if (event.getSource() == btnExperienceCertificate) {
            print(false, btnExperienceCertificate);
            hide(false);
        } else if (event.getSource() == btnOk) {
            if (cbEmployeeCardPrint.getValue()) {
                print(false, btnEmployeeCard);
            }
            if (cbSalaryCertificatePrint.getValue()) {
                print(false, btnSalaryCertificate);
            }
            if (cbExperienceCertificatePrint.getValue()) {
                print(false, btnExperienceCertificate);
            }

            hide(true);
        }
    }

    private void manageDisplay() {
        flwExperienceCertificate.clear();
        flwcbExperienceCertificate.clear();
        if (cbSalaryCertificatePrint.getValue()) {
            flwExperienceCertificate.add(btnExperienceCertificate);
            flwcbExperienceCertificate.add(cbExperienceCertificatePrint);
        }

    }

    private void print(boolean printToPrinter, Button btn) {
        if (btn.equals(btnEmployeeCard)) {
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("docId", new FieldUDT_Mdl("docId", "", DmShared.xNull(employeeMaster.topControl.getDocId())));
            new OpenSourceReport("", "PY_EmployeeMaster_", btnEmployeeCard.getTitle(), "Employee Card", employeeMaster.widgetName, hashMapFilter, printToPrinter,
             new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, moduleName, employeeMaster.widgetName, btnSalaryCertificate.getText());
                    return reportProperty;
                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        } 
        else if (btn.equals(btnSalaryCertificate)) {
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("docId", new FieldUDT_Mdl("docId", "", DmShared.xNull(employeeMaster.topControl.getDocId())));
            new OpenSourceReport("", "PY_EmployeeMaster_", btnSalaryCertificate.getTitle(), "Salary Certificate", employeeMaster.widgetName, hashMapFilter, printToPrinter,
             new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, moduleName, employeeMaster.widgetName, btnSalaryCertificate.getText());
                    return reportProperty;
                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

        }

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(false);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

//    private void printCheque(final boolean printToPrinter) {
//        ChequePrint_Mdl mrTemp = fillMdl();
//        if (mrTemp != null) {
//            
//            tpaPaymentRefund.SrvAsync.invokeRPC().printRecords(mrTemp, new AsyncCallback<DmResult<String>>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//                }
//
//                @Override
//                public void onSuccess(DmResult<String> result) {
//                    if (result.isStatus()) {
//                      
//                        hide();
//                    } else {
//                        new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
//                    }
//                }
//            });
//        }
//    }
//    private ChequePrint_Mdl fillMdl() {
//        ChequePrint_Mdl mrTemp = new ChequePrint_Mdl();
//        mrTemp.setSharedKeyFieldCode(DmShared.iNull(tpaPaymentRefund.topControl.getDocId()).toString());
//        mrTemp.setVoucherTypeTag(DmShared.iNull(tpaPaymentRefund.txtReciptType.getTag()).toString());
//        mrTemp.setHeaderAccountCode(DmShared.iNull(tpaPaymentRefund.cardPayment.getGrdCardPaymentList().get(0).getAcNameCode()).toString());
//        mrTemp.setAccountCode(DmShared.iNull(tpaPaymentRefund.txtPatientName.getTag()).toString());
//
//        return mrTemp;
//
//    }
}
