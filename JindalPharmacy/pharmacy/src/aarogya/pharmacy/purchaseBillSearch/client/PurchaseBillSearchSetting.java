package aarogya.pharmacy.purchaseBillSearch.client;

import aarogya.pharmacy.purchaseBillSearch.shared.GrdBillDetails_Mdl;
import aarogya.pharmacy.purchaseBillSearch.shared.PurchaseBillSearchValue_Mdl;
import aarogya.pharmacy.purchaseBillSearch.shared.PurchaseBillSearch_Srv;
import aarogya.pharmacy.purchaseBillSearch.shared.PurchaseBillSearch_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.*;
import java.util.List;

public class PurchaseBillSearchSetting<T1, T2> extends PurchaseBillSearchSetting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurEvent.BlurHandler {

    String docId = "", subCode = "", v_Date = "", vCat = "";

    interface CallBack {

        public void onOk(List<GrdBillDetails_Mdl> approvalEntry_Mdl);
    }

    CallBack callBack;
    ServiceInvoker<PurchaseBillSearch_SrvAsync> SrvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(boolean editable, String subcode, String docID, String vCat, String v_Date) {
        this.subCode = subcode;
        this.docId = docID;
        this.vCat = vCat;
        this.v_Date = v_Date;

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        txtHPBill.setFocus(true);
        flwOk.clear();
        flwOk.setVisible(false);

        if (editable) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void txtKeyEvents() {
    }

    private void textHelpEvents() {

        txtHPBill.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBillListSearch(docId, subCode, v_Date, vCat, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPBill).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPBill.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPBill).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.xNull(txtHPBill.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please select atleast one order.", txtHPBill).show();
                return;
            }
            fillRecords();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
//        if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
//                if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
//                    new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtFromDate).show();
//                    return;
//                }
//            }
    }

    private void fillRecords() {
        PurchaseBillSearchValue_Mdl mrTemp = new PurchaseBillSearchValue_Mdl();
        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHPBill.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHPBill.getText()));

        SrvAsync.invokeRPC().getAutoBillDetail(txtHPBill.getTag(), this.v_Date, new AsyncCallback<DmResult<List<GrdBillDetails_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(DmResult<List<GrdBillDetails_Mdl>> result) {
                if (result.isStatus()) {
                    callBack.onOk(result.getData());
                    popupPanel.hide();
                }
            }
        });

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
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

    public PurchaseBillSearchSetting() {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(PurchaseBillSearch_Srv.class), PurchaseBillSearch.class.getName());
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        textHelpEvents();
        txtKeyEvents();

        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        onAdd();
    }

    private void onAdd() {

    }

}
