package aarogya.master.eod.client;

import aarogya.master.eod.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.ArrayList;
import java.util.List;

public class AcPosting extends AcPosting_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    public interface CallBack {

        public void onClose();
    }
    private CallBack callBack;
    private ServiceInvoker<EOD_SrvAsync> srvAsync;
    private String forDate = "";
    private String processCode;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void gridEvents() {
        grdPendingDate.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showPendingAP(DmShared.xNull(grdPendingDate.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName()).trim());
            }
        });
        grdPendingDate.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPendingAP(DmShared.xNull(grdPendingDate.getGrid().getStore().get(grdPendingDate.getGrid().getStore().indexOf(grdPendingDate.getGrid().getSelectionModel().getSelectedItem())).getSharedKeyFieldName()).trim());
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showPendingAP(String forDate) {
        this.forDate = forDate;
        txtForDate.setText(DmShared.dNull(forDate));
        getAcPostingDetail();
    }

    public void show(String forDate, boolean isDelete, String processCode) {
        this.forDate = forDate;
        this.processCode = processCode;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        toolContainer.setTitle(STEnum.ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()).getValue());

        txtForDate.setText(DmShared.dNull(forDate));
        clear();

        flwPosting.clear();
        flwDelete.clear();
        flwWorkingArea.clear();
        if (isDelete) {
            grdAcPostedDetail.getGrid().setHeight("350px");
            flwWorkingArea.add(deleteArea());
            flwPosting.add(vl.add(btnDelete));
            getAccountDeleteDetail();
        } else {
            grdAcPostedDetail.getGrid().setHeight("130px");
            flwWorkingArea.add(postingArea());
            flwPosting.add(vl.add(btnPosting));
            getPendingDates();
        }
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean isCallbackNeeded) {
        this.popupPanel.hide();
        if (isCallbackNeeded) {
            callBack.onClose();
        }
    }

    @Override
    public void onClick(ClickEvent event) {

        isPostDateWise = false;
        if (event.getSource() == btnClose) {
            hide();
        } else if (event.getSource() == btnPosting) {
            new DmConfirmMessageBox("Are you sure? You want to post.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        txtFromDate.setText("");
                        txtToDate.setText("");
                        forDates.clear();
                        getPendingEodDates();
                        saveRecord();
                    }
                }
            }).show();
        } else if (event.getSource() == btnDelete) {
            new DmConfirmMessageBox("Are you sure? You want to delete.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        delete();
                    }
                }
            }).show();
        } else if (event.getSource() == btnPostDateWise) {

            boolean isFromDateOk = false, isToDateOk = false;

            //==========================Validate Date========================
            if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("From Date is mandatory", txtFromDate).show();
                return;
            }

            if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("To Date is mandatory", txtToDate).show();
                return;
            }

            for (int i = 0; i < grdPendingDate.getGrid().getStore().getAll().size(); i++) {

                if (DmShared.convertDate(grdPendingDate.getGrid().getStore().get(i).getSharedKeyFieldName()).getTime() == DmShared.convertDate(txtFromDate.getText()).getTime()) {

                    isFromDateOk = true;

                }
                if (DmShared.convertDate(grdPendingDate.getGrid().getStore().get(i).getSharedKeyFieldName()).getTime() == DmShared.convertDate(txtToDate.getText()).getTime()) {

                    isToDateOk = true;

                }
            }

            if (!isFromDateOk) {
                new DmAlertMessageBox("From Date does not exist in pendig dates.", txtFromDate).show();
                return;
            }
            if (!isToDateOk) {
                new DmAlertMessageBox("To Date does not exist in pendig dates.", txtToDate).show();
                return;
            }
            if (DmShared.convertDate(txtFromDate.getText()).getTime() > DmShared.convertDate(txtToDate.getText()).getTime()) {
                new DmAlertMessageBox("From Date can not be greater than To Date.", txtToDate).show();
                return;
            }

            ////////////////////////////////////////////////////////////////
            new DmConfirmMessageBox("Are you sure? You want to post Date wise.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {

                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {

                        datewiseposting();
                    }
                }
            }).show();
        } else if (event.getSource() == btnUnPostDateWise) {

            //==========================Validate Date========================
            if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("From Date is mandatory", txtFromDate).show();
                return;
            }

            if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("To Date is mandatory", txtToDate).show();
                return;
            }

            if (DmShared.convertDate(txtFromDate.getText()).getTime() > DmShared.convertDate(txtToDate.getText()).getTime()) {
                new DmAlertMessageBox("From Date can not be greater than To Date.", txtToDate).show();
                return;
            }

            new DmConfirmMessageBox("Please Backup the Database before Run this Process ."
                    + " Are you sure? You want to Unpost from Date " + txtFromDate.getText() + " to Date " + txtToDate.getText(), new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {

                        datewiseUnposting();
                    }
                }
            }).show();
        }
    }
//    boolean isDataSaved = false;
    boolean isPostDateWise = false;
    ArrayList<String> forDates = new ArrayList<String>();

    private void datewiseposting() {
        try {
            boolean isFromDateOk = false, isToDateOk = false;

            //==========================Validate Date========================
            if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("From Date is mandatory", txtFromDate).show();
                return;
            }

            if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("To Date is mandatory", txtToDate).show();
                return;
            }

            for (int i = 0; i < grdPendingDate.getGrid().getStore().getAll().size(); i++) {

                if (DmShared.convertDate(grdPendingDate.getGrid().getStore().get(i).getSharedKeyFieldName()).getTime() == DmShared.convertDate(txtFromDate.getText()).getTime()) {

                    isFromDateOk = true;

                }
                if (DmShared.convertDate(grdPendingDate.getGrid().getStore().get(i).getSharedKeyFieldName()).getTime() == DmShared.convertDate(txtToDate.getText()).getTime()) {

                    isToDateOk = true;

                }
            }

            if (!isFromDateOk) {
                new DmAlertMessageBox("From Date does not exist in pendig dates.", txtFromDate).show();
                return;
            }
            if (!isToDateOk) {
                new DmAlertMessageBox("To Date does not exist in pendig dates.", txtToDate).show();
                return;
            }
            if (DmShared.convertDate(txtFromDate.getText()).getTime() > DmShared.convertDate(txtToDate.getText()).getTime()) {
                new DmAlertMessageBox("From Date can not be greater than To Date.", txtToDate).show();
                return;
            }

            ////////////////////////////////////////////////////////////////
            isPostDateWise = true;
            forDates.clear();
            for (int i = 0; i < grdPendingDate.getGrid().getStore().getAll().size(); i++) {

                if (DmShared.convertDate(grdPendingDate.getGrid().getStore().get(i).getSharedKeyFieldName()).getTime() >= DmShared.convertDate(txtFromDate.getText()).getTime()
                        && DmShared.convertDate(grdPendingDate.getGrid().getStore().get(i).getSharedKeyFieldName()).getTime() <= DmShared.convertDate(txtToDate.getText()).getTime()) {

                    forDates.add(DmShared.xNull(grdPendingDate.getGrid().getStore().get(i).getSharedKeyFieldName()).trim());

                }
            }

            saveRecord();

        } catch (DmException e) {
            error(e.getErrorNo(), e.getMessage());
        }
    }

    private void datewiseUnposting() {
        try {

            //==========================Validate Date========================
            if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("From Date is mandatory", txtFromDate).show();
                return;
            }

            if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("To Date is mandatory", txtToDate).show();
                return;
            }

            if (DmShared.convertDate(txtFromDate.getText()).getTime() > DmShared.convertDate(txtToDate.getText()).getTime()) {
                new DmAlertMessageBox("From Date can not be greater than To Date.", txtToDate).show();
                return;
            }

            ////////////////////////////////////////////////////////////////
            isPostDateWise = true;
            forDates.clear();

            forDates.add(DmShared.xNull(txtFromDate.getText()).trim());
            forDates.add(DmShared.xNull(txtToDate.getText()).trim());

            delete();

        } catch (DmException e) {
            error(e.getErrorNo(), e.getMessage());
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.onClose();
            hide();
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

    public AcPosting() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(EOD_Srv.class), EOD.class.getName());
        handleEvent();
        gridEvents();
    }

    private void handleEvent() {
        popupPanel.addAttachHandler(this);
        btnClose.addClickHandler(this);
        btnPosting.addClickHandler(this);
        btnDelete.addClickHandler(this);
        btnPostDateWise.addClickHandler(this);
        btnUnPostDateWise.addClickHandler(this);

        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
    }

    private void getPendingDates() {
        try {
            srvAsync.invokeRPC().getAPPendingDates(forDate, DmClient.getVoucherPrefix(), processCode, new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtForDate).show();
                }

                @Override
                public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                    grdPendingDate.getGrid().getStore().clear();
                    if (result.isStatus()) {
                        grdPendingDate.getGrid().getStore().addAll(result.getData());
                        getAcPostingDetail();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtForDate).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void getAcPostingDetail() {
        try {
            srvAsync.invokeRPC().getAPDetails(forDate, DmClient.getVoucherPrefix(), processCode, new AsyncCallback<DmResult<AcPosting_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtForDate).show();
                }

                @Override
                public void onSuccess(DmResult<AcPosting_Mdl> result) {
                    if (result.isStatus()) {
                        grdAcPosting.getGrid().getStore().clear();
                        grdAcPosting.getGrid().getStore().addAll(result.getData().getListAcPostingDetail());

                        grdAcPostedDetail.getGrid().getStore().clear();
                        grdAcPostedDetail.getGrid().getStore().addAll(result.getData().getListAcPostedDetail());

                        btnPosting.setFocus(true);

                    } else {
                        grdAcPosting.getGrid().getStore().clear();
                        new DmAlertMessageBox(result.getErrorMessage(), txtForDate).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void getAccountDeleteDetail() {
        try {
            srvAsync.invokeRPC().getAPDeleteDetails(forDate, DmClient.getVoucherPrefix(), processCode, new AsyncCallback<DmResult<List<GrdAcPostedDetail_Mdl>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtForDate).show();
                }

                @Override
                public void onSuccess(DmResult<List<GrdAcPostedDetail_Mdl>> result) {
                    if (result.isStatus()) {
                        grdAcPostedDetail.getGrid().getStore().clear();
                        grdAcPostedDetail.getGrid().getStore().addAll(result.getData());
                        btnDelete.setFocus(true);
                    } else {
                        grdAcPostedDetail.getGrid().getStore().clear();
                        new DmAlertMessageBox(result.getErrorMessage(), txtForDate).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private AcPosting_Mdl saveInitialize() {
        AcPosting_Mdl mrTemp = new AcPosting_Mdl();

        mrTemp.setForDate(DmShared.dNull(txtForDate.getText()));
        getPendingEodDates();
        return mrTemp;
    }

    ArrayList<String> pendingEodDates = new ArrayList<String>();

    private void getPendingEodDates() {
        pendingEodDates.clear();
        if (grdPendingDate.getGrid().getStore().getAll().size() > 0) {
            for (int i = 0; i < grdPendingDate.getGrid().getStore().getAll().size(); i++) {

                if (!DmShared.xNull(grdPendingDate.getGrid().getStore().get(i).getSharedKeyFieldName()).trim().equalsIgnoreCase("")) {
                    pendingEodDates.add(DmShared.xNull(grdPendingDate.getGrid().getStore().get(i).getSharedKeyFieldName()).trim());
                }
            }
        }
    }

    private void saveRecord() {
        AcPosting_Mdl mrTemp;

        try {
            mrTemp = saveInitialize();
            if (!isPostDateWise) {
                AcPosting_Vld.chkSave(mrTemp);
                AcPosting_Vld.chkPostingData(grdAcPosting.getGrid().getStore().getAll());
            }
            srvAsync.invokeRPC().saveAPRecord(forDate, forDates, pendingEodDates,DmShared.xNull(txtFromDate.getText()), DmClient.getVoucherPrefix(), processCode, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        grdAcPosting.getGrid().getStore().clear();
                        getPendingDates();
                        forDates.clear();
                    } else {
                        error(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            error(e.getErrorNo(), e.getMessage());
        }
    }

    private void delete() {
        try {
            srvAsync.invokeRPC().deleteAP(forDate, forDates, isPostDateWise, processCode, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        grdAcPostedDetail.getGrid().getStore().clear();
                        hide();
                    } else {
                        error(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            error(e.getErrorNo(), e.getMessage());
        }
    }

    private void error(int errorNo, String errorMessage) {
        switch (errorNo) {
            default:
                new DmAlertMessageBox(errorMessage, txtForDate).show();
        }
    }

    private void clear() {
        grdAcPosting.getGrid().getStore().clear();
        grdAcPostedDetail.getGrid().getStore().clear();
        grdPendingDate.getGrid().getStore().clear();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

}
