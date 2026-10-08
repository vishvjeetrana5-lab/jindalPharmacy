package aarogya.pharmacy.saleBillReturnProvisional.client;

import aarogya.pharmacy.saleBillReturnProvisional.shared.GrdMedicine_Mdl;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import aarogya.pharmacy.saleBillReturnProvisional.shared.SaleBillReturnProvisional_SrvAsync;
import aarogya.pharmacy.saleBillReturnProvisional.shared.SaleBillReturnProvisional_Srv;
import aarogya.pharmacy.saleBillReturnProvisional.shared.TxtHInvoicedItem_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.ArrayList;
import java.util.List;

public class ReturnMedicineBatchDetail extends ReturnMedicineBatchDetail_Dsg implements BlurHandler, ClickHandler, KeyUpHandler, KeyDownHandler, DmRootEvents, AttachEvent.Handler {

    private ServiceInvoker<SaleBillReturnProvisional_SrvAsync> srvAsync;
    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();
    private String regDocID = "";
    private String departmentCode = "";
    private CallBack callBack;
    private List<KeyFieldExtended> prevMedDetail = new ArrayList<>();
    private TxtHInvoicedItem_Mdl txtHInvoicedItem_Mdl = new TxtHInvoicedItem_Mdl();

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtReturnQuantity) {
            if (DmShared.vNull(txtReturnQuantity.getText()) > DmShared.vNull(txtInvoicedQty.getText())) {
                new DmAlertMessageBox("Return Qty exeeds pending bill quantity", txtReturnQuantity).show();
                return;
            } else {
                getReturnedMedicineDet("A");
                callBack.ok(keyFields);
                getClear();
            }
        }
    }

    public void getClear() {
        txtHInvoicedItem.setText("");
        txtHInvoicedItem.setTag("");
        txtHInvoicedItem.setTag2nd("");
        txtExpiryDate.setText("");
        txtBatchNo.setText("");
        txtBatchNo.setTag("");
        txtReturnQuantity.setText("");
        txtReturnQuantity.setTag("");
        txtInvoicedQty.setText("");
        txtInvoicedQty.setTag("");
//        txtHInvoicedItem.setFocus(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHInvoicedItem.setFocus(true);
            }
        });
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            popupPanel.hide();
        }
    }

    public interface CallBack {

        public void ok(List<KeyFieldExtended> listKF);

        public void close();

        public void delete(List<KeyFieldExtended> listKF);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public ReturnMedicineBatchDetail(String regDocID) {
        super("Medicine Detail", "500px", "");
        srvAsync = new ServiceInvoker<SaleBillReturnProvisional_SrvAsync>(GWT.create(SaleBillReturnProvisional_Srv.class), SaleBillReturnProvisional.class.getName());
        textHelpEvents();
        manageEvents();
//        manageOk(regDocID);
    }

    private void manageEvents() {
        btnOK.addClickHandler(this);
        btnCancel.addClickHandler(this);
        btnDelete.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtReturnQuantity.addBlurHandler(this);
        txtBatchNo.setEnabled(false);
        txtExpiryDate.setEnabled(false);
        txtSKU.setEnabled(false);
        txtInvoicedQty.setEnabled(false);
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {

    }

    @Override
    public void onKeyDown(KeyDownEvent event) {

    }

    public void show(String regDocID, String departmentCode, List<KeyFieldExtended> lstPrevMedDet, GrdMedicine_Mdl grdMedicineMdl) {
        this.txtHInvoicedItem_Mdl = txtHInvoicedItem_Mdl;
        this.regDocID = regDocID;
        this.departmentCode = departmentCode;
        if (grdMedicineMdl != null) {
            txtHInvoicedItem.setTag(grdMedicineMdl.getSharedKeyFieldCode());
            txtHInvoicedItem.setText(grdMedicineMdl.getSharedKeyFieldName());
            int itemIndex = 0;
            if (lstPrevMedDet.size() != 0) {
                for (int i = 0; i < lstPrevMedDet.size(); i++) {
                    itemIndex = i;
                }
                txtInvoicedQty.setText(lstPrevMedDet.get(itemIndex).getSharedModifiedBy());
            }
            txtReturnQuantity.setText(grdMedicineMdl.getQuantity());
            txtExpiryDate.setText(DmShared.dNull(grdMedicineMdl.getExpDate()));
            txtBatchNo.setText(grdMedicineMdl.getBatchNo());
        }

        prevMedDetail = lstPrevMedDet;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHInvoicedItem.setFocus(true);
            }
        });
    }

    private void textHelpEvents() {

        txtHInvoicedItem.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHInvoicedItem_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHInvoicedItem(regDocID, departmentCode, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHInvoicedItem_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInvoicedItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHInvoicedItem_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHInvoicedItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInvoicedItem).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHInvoicedItem_Mdl t1Row) {
                if (blnStatus) {
                    txtHInvoicedItem.setTag(DmShared.xNull(t1Row.getMedicineCode()));
                    txtHInvoicedItem.setText(DmShared.xNull(t1Row.getMedicineName()));
                    txtBatchNo.setTag(DmShared.xNull(t1Row.getMedicineBatchCode()));
                    txtBatchNo.setText(DmShared.xNull(t1Row.getMedicineBatchNo()));
                    txtExpiryDate.setText(DmShared.dNull(t1Row.getExpiryDate()));
                    txtSKU.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtInvoicedQty.setTag(DmClient.vNull(t1Row.getQty().toString(), "0.00"));
                    txtInvoicedQty.setText(DmClient.vNull(t1Row.getQty().toString(), "0.00"));
                    txtReturnQuantity.setTag(DmClient.vNull(t1Row.getQty().toString(), "0.00"));
                    txtReturnQuantity.setText(DmClient.vNull(t1Row.getQty().toString(), "0.00"));
                    fillPrevReturnQty();
                }
            }
        });
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
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOK) {
            getReturnedMedicineDet("A");
            callBack.ok(keyFields);
            popupPanel.hide();
        } else if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnDelete) {
            getReturnedMedicineDet("D");
            callBack.delete(keyFields);

        }
    }

    private void getReturnedMedicineDet(String entryAction) {
        keyFields = new ArrayList<KeyFieldExtended>();
        final KeyFieldExtended field = new KeyFieldExtended();
        field.setSharedKeyFieldCode(txtHInvoicedItem.getTag());
        field.setSharedKeyFieldName(txtBatchNo.getTag());
        field.setSharedModifiedBy(txtReturnQuantity.getText());
        field.setSharedPreparedBy(entryAction);
        keyFields.add(field);
    }

    private void fillPrevReturnQty() {
        boolean isItemExist = false;
        int itemIndex = 0;
        if (prevMedDetail.size() != 0) {
            for (int i = 0; i < prevMedDetail.size(); i++) {
                KeyFieldExtended currRecord = prevMedDetail.get(i);
                if ((DmShared.xNull(currRecord.getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(txtHInvoicedItem.getTag()).trim()))
                        && (DmShared.xNull(currRecord.getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(txtBatchNo.getTag()).trim()))) {
                    itemIndex = i;
                    isItemExist = true;
                    break;
                }
            }
            if (isItemExist) {
                txtReturnQuantity.setTag(DmClient.vNull(prevMedDetail.get(itemIndex).getSharedModifiedBy().toString(), "0.00"));
                txtReturnQuantity.setText(DmClient.vNull(prevMedDetail.get(itemIndex).getSharedModifiedBy().toString(), "0.00"));
            }
        }
    }

}
