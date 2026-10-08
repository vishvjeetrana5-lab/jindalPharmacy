package aarogya.pharmacy.orderSearch.client;

import aarogya.pharmacy.orderSearch.shared.GrdOrdertDetails_Mdl;
import aarogya.pharmacy.orderSearch.shared.OrderSearch_Srv;
import aarogya.pharmacy.orderSearch.shared.OrderSearch_SrvAsync;
import aarogya.pharmacy.orderSearch.shared.OrderSearchValue_Mdl;
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

public class OrderSearchSetting<T1, T2> extends OrderSearchSetting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurEvent.BlurHandler {

    String docId = "", subCode = "", v_Date = "", vCat = "";

    interface CallBack {

        public void onOk(List<GrdOrdertDetails_Mdl> approvalEntry_Mdl);
    }

    CallBack callBack;
    ServiceInvoker<OrderSearch_SrvAsync> SrvAsync;

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

        txtHPOrder.setFocus(true);
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

        txtHPOrder.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOrderListSearch(docId, subCode, v_Date, vCat, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPOrder).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPOrder.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPOrder).show();
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
            if (DmShared.xNull(txtHPOrder.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please select atleast one order.", txtHPOrder).show();
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
        OrderSearchValue_Mdl mrTemp = new OrderSearchValue_Mdl();
        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHPOrder.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHPOrder.getText()));

        SrvAsync.invokeRPC().getAutoOrderDetail(txtHPOrder.getTag(), this.v_Date, new AsyncCallback<DmResult<List<GrdOrdertDetails_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(DmResult<List<GrdOrdertDetails_Mdl>> result) {
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

    public OrderSearchSetting() {
        super();
        SrvAsync = new ServiceInvoker<OrderSearch_SrvAsync>(GWT.create(OrderSearch_Srv.class), OrderSearch.class.getName());
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
