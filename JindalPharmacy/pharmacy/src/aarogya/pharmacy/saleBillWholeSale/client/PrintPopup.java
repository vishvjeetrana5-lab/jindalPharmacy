package aarogya.pharmacy.saleBillWholeSale.client;

import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.pmTPA;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;

public class PrintPopup extends PrintPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void hide(boolean isCancel);
    }

    private CallBack callBack;
    private SaleBillWholeSale saleBillWholeSale;
    private String moduleName = new pmTPA().moduleName;
    private boolean printActualBill, isAllowPrintForAnotherUser;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(SaleBillWholeSale saleBillWholeSale, boolean printActualBill, boolean isAllowPrintForAnotherUser, String callFrom) {
        this.saleBillWholeSale = saleBillWholeSale;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        btnOk.setFocus(true);
        this.printActualBill = printActualBill;
        this.isAllowPrintForAnotherUser = isAllowPrintForAnotherUser;

        if (DmShared.xNull(callFrom).equalsIgnoreCase(new pmPharmacy().WHOLE_SALE_GATE_PASS_OUT.caption)) {
            flwMain.add(flwCbSaleBill);
            flwMain.add(flwBtnSaleBill);

        } else {
            flwMain.add(flwCbGatePass);
            flwMain.add(flwBtnGatePass);
        }

    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void hide(boolean isCancel) {
        hide();
        callBack.hide(isCancel);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnSaleBill) {
            print(false, btnSaleBill);
            hide(false);
        } else if (event.getSource() == btnEinvoice) {
            print(false, btnEinvoice);
            hide(false);
        } else if (event.getSource() == btnEway) {
            print(false, btnEway);
            hide(false);
        } else if (event.getSource() == btnGatePass) {
            print(false, btnGatePass);
            hide(false);
        } else if (event.getSource() == btnPartyLedger) {
            print(false, btnPartyLedger);
            hide(false);
        } else if (event.getSource() == btnOk) {
            if (cbSaleBill.getValue()) {
                print(true, btnSaleBill);
            }
            if (cbEinvoice.getValue()) {
                print(true, btnEinvoice);
            }
            if (cbEway.getValue()) {
                print(true, btnEway);
            }
            if (cbGatePass.getValue()) {
                print(true, btnGatePass);
            }
            if (cbPartyLedger.getValue()) {
                print(true, btnPartyLedger);
            }
            hide(false);
        }
    }

    private void print(boolean printToPrinter, Button btn) {
        String isZeroValueTag = "0", isZeroValueText = "No";
        String isPackageDetailTag = "0", isPackageDetailText = "No";
        String isDisplayTestDetailsTag = "0", isDisplayTestDetailsText = "No";
        String isDisplayMedicineDetailsTag = "0", isDisplayMedicineDetailsText = "No";

        if (btn.equals(btnSaleBill)) {
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("mongoLinkId", new FieldUDT_Mdl("mongo Link Id", DmShared.xNull(saleBillWholeSale.txtBillNo.getText()), DmShared.xNull(saleBillWholeSale.mongoLinkId)));
            hashMapFilter.put("docId", new FieldUDT_Mdl("Bill No.", DmShared.xNull(saleBillWholeSale.txtBillNo.getText()), DmShared.xNull(saleBillWholeSale.topControl.getDocId())));

            new OpenSourceReport(saleBillWholeSale.txtHVType.getTag(), "", "Sale Bill Whole Sale", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                    return reportProperty;
                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

        } else if (btn.equals(btnEinvoice)) {
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("mongoLinkId", new FieldUDT_Mdl("mongo Link Id", DmShared.xNull(saleBillWholeSale.txtBillNo.getText()), DmShared.xNull(saleBillWholeSale.mongoLinkId)));
            hashMapFilter.put("docId", new FieldUDT_Mdl("Bill No.", DmShared.xNull(saleBillWholeSale.txtBillNo.getText()), DmShared.xNull(saleBillWholeSale.topControl.getDocId())));

            new OpenSourceReport(saleBillWholeSale.txtHVType.getTag(), "", "Sale Bill Whole Sale", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                    return reportProperty;
                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

        } else if (btn.equals(btnEway)) {
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("docId", new FieldUDT_Mdl("Bill No.", DmShared.xNull(saleBillWholeSale.txtBillNo.getText()), DmShared.xNull(saleBillWholeSale.topControl.getDocId())));
            hashMapFilter.put("isZeroValue", new FieldUDT_Mdl("Show Zero Value", isZeroValueText, isZeroValueTag));
            hashMapFilter.put("type", new FieldUDT_Mdl("Type", "Category Wise", "C"));
            hashMapFilter.put("isDisplayTestDetails", new FieldUDT_Mdl("isDisplayTestDetails", isDisplayTestDetailsText, isDisplayTestDetailsTag));

            hashMapFilter.put("isDisplayMedicineDetails", new FieldUDT_Mdl("isDisplayMedicineDetails", isDisplayMedicineDetailsText, isDisplayMedicineDetailsTag));
            hashMapFilter.put("printActualBill", new FieldUDT_Mdl("Print Actual Bill", "Actual/MaskBill", printActualBill + ""));

//            new OpenSourceReport(isAllowPrintForAnotherUser,"saleBill", ipdBill.topControl.getDocId(), "docId", ipdBill.txtBillDate.getTag(), "_NonTPA", ipdBill.widgetName, ipdBill.widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
//                @Override
//                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
//                    DmClient.localSettingPrintMap(reportProperty, moduleName, ipdBill.widgetName, btnEway.getText());
//                    return reportProperty;
//                }
//            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        } else if (btn.equals(btnGatePass)) {
   
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("docId", new FieldUDT_Mdl("Bill No.", DmShared.xNull(saleBillWholeSale.txtBillNo.getText()), DmShared.xNull(saleBillWholeSale.topControl.getDocId())));

            new OpenSourceReport(saleBillWholeSale.txtHVType.getTag(), "Pharmacy_WholeSaleGatePass_main","","Gate Pass", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                    return reportProperty;
                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

        } else if (btn.equals(btnPartyLedger)) {

            isPackageDetailTag = "1";
            isPackageDetailText = "Yes";

            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("docId", new FieldUDT_Mdl("Bill No.", DmShared.xNull(saleBillWholeSale.txtBillNo.getText()), DmShared.xNull(saleBillWholeSale.topControl.getDocId())));
            hashMapFilter.put("isZeroValue", new FieldUDT_Mdl("Show Zero Value", isZeroValueText, isZeroValueTag));
            hashMapFilter.put("isPackageDetail", new FieldUDT_Mdl("Show Package Detail", isPackageDetailText, isPackageDetailTag));
            hashMapFilter.put("type", new FieldUDT_Mdl("Type", "Package Wise", "P"));
            hashMapFilter.put("isDisplayTestDetails", new FieldUDT_Mdl("isDisplayTestDetails", isDisplayTestDetailsText, isDisplayTestDetailsTag));

            hashMapFilter.put("isDisplayMedicineDetails", new FieldUDT_Mdl("isDisplayMedicineDetails", isDisplayMedicineDetailsText, isDisplayMedicineDetailsTag));
            hashMapFilter.put("printActualBill", new FieldUDT_Mdl("Print Actual Bill", "Actual/MaskBill", printActualBill + ""));

//            new OpenSourceReport(isAllowPrintForAnotherUser,"saleBill", ipdBill.topControl.getDocId(), "docId", ipdBill.txtBillDate.getTag(), "_NonTPA", ipdBill.widgetName, ipdBill.widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
//                @Override
//                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
//                    DmClient.localSettingPrintMap(reportProperty, moduleName, ipdBill.widgetName, btnPartyLedger.getText());
//                    return reportProperty;
//                }
//            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
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

    public PrintPopup() {
        super();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        btnSaleBill.addClickHandler(this);
        btnEinvoice.addClickHandler(this);
        btnEway.addClickHandler(this);
        btnGatePass.addClickHandler(this);
        btnPartyLedger.addClickHandler(this);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

    }

}
