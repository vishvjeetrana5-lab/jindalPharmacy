package aarogya.selfToolkit.uhidDuplicacy.client;

import aarogya.selfToolkit.uhidDuplicacy.shared.GrdUHIDDetail_Mdl;
import aarogya.selfToolkit.uhidDuplicacy.shared.PatientDetail_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import aarogya.selfToolkit.uhidDuplicacy.shared.UHIDDuplicacy_Srv;
import aarogya.selfToolkit.uhidDuplicacy.shared.UHIDDuplicacy_SrvAsync;
import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Mdl;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class UHIDDuplicacy extends UHIDDuplicacy_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    public interface CallBack {

        public void ok(String uhidDocId, String uhidNo);

        public void close(UhidPopup_Mdl uhidPopupMdl);

    }

    private CallBack callBack;
    private final ServiceInvoker<UHIDDuplicacy_SrvAsync> SrvAsync;
    private UhidPopup_Mdl uhidPopup_Mdl;
    private PatientDetail_Mdl detail_Mdl;
    private Boolean flag = true;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(UhidPopup_Mdl uhidPopup_Mdl) {
        this.uhidPopup_Mdl = uhidPopup_Mdl;
        flag = true;
        txtSearchValue.setText("");
        fillPatientDetail(new PatientDetail_Mdl());
        getDetail(uhidPopup_Mdl);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean withCloseEvent) {
        this.popupPanel.hide();
        if (withCloseEvent) {
            callBack.close(this.uhidPopup_Mdl);
        }
    }

    private void gridEvents() {
        gridPatientDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (detail_Mdl != null) {
                    if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(gridPatientDetail.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()).longValue()) {
                        onSelection();
                    } else {
                        getPatientDetail(DmShared.xNull(gridPatientDetail.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
                    }
                } else {
                    getPatientDetail(DmShared.xNull(gridPatientDetail.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
                }
            }
        });

        gridPatientDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    if (detail_Mdl != null) {
                        if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(gridPatientDetail.getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode()).longValue()) {
                            onSelection();
                        } else {
                            getPatientDetail(DmShared.xNull(gridPatientDetail.getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode()));
                        }
                    } else {
                        getPatientDetail(DmShared.xNull(gridPatientDetail.getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode()));
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnChooseRecord) {
            onSelection();
        }
    }

    private void onSelection() {
        if (detail_Mdl != null) {
            new DmConfirmMessageBox("Are you sure? You want to choose this record.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        onOk();
                    }
                }
            }).show();
        }
    }

    private void onOk() {
        callBack.ok(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()), DmShared.xNull(detail_Mdl.getSharedKeyFieldName()));
        hide(false);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            txtSearchValue.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public UHIDDuplicacy() {
        super();
        SrvAsync = new ServiceInvoker<UHIDDuplicacy_SrvAsync>(GWT.create(UHIDDuplicacy_Srv.class), UHIDDuplicacy.class.getName());
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        gridEvents();
        manageEvents();
    }

    private void manageEvents() {
        btnCancel.addClickHandler(this);
        btnChooseRecord.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    private void getDetail(UhidPopup_Mdl uhidPopup_Mdl) {
        SrvAsync.invokeRPC().getDetail(uhidPopup_Mdl, new AsyncCallback<DmResult<List<GrdUHIDDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdUHIDDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.getData().size() > 0) {
                        detail_Mdl = null;
                        gridPatientDetail.getGrid().getStore().clear();
                        gridPatientDetail.getGrid().getStore().addAll(result.getData());
                        if (flag) {
                            flag = false;
                            popupPanel.show();
                            popupPanel.center();
                            popupPanel.setGlassEnabled(true);
                        }
                    } else {
                        hide(true);
                    }
                    ((CellSelectionModel) gridPatientDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void getPatientDetail(String docId) {
        SrvAsync.invokeRPC().getUHIDDetail(docId, new AsyncCallback<DmResult<PatientDetail_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtSearchValue).show();
            }

            @Override
            public void onSuccess(DmResult<PatientDetail_Mdl> result) {
                if (result.isStatus()) {
                    fillPatientDetail(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtSearchValue).show();
                }
            }
        });
    }

    private void fillPatientDetail(PatientDetail_Mdl detail_Mdl) {
        this.detail_Mdl = detail_Mdl;
        lblAddress1.setText(DmShared.xNull(detail_Mdl.getAddress1()) + " " + DmShared.xNull(detail_Mdl.getAddress2()));
        lblArea.setText(DmShared.xNull(detail_Mdl.getAreaName()));
        lblCity.setText(DmShared.xNull(detail_Mdl.getCityName()));
        lblMaritalStatus.setText(DmShared.xNull(detail_Mdl.getMaritalStatus()));
        lblPatientName.setText(DmShared.xNull(detail_Mdl.getPatientName()));
        lblGuardianName.setText(DmShared.xNull(detail_Mdl.getGuardianName()));
        lblUHIDNo.setText(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()));
        lblEmail.setText(DmShared.xNull(detail_Mdl.getEmail()));
        lblMobile.setText(DmShared.xNull(detail_Mdl.getMobile()));
        lblIDType.setText(DmShared.xNull(detail_Mdl.getIdType()) + " - " + DmShared.xNull(detail_Mdl.getIdNo()));
        lblVoucherDate.setText(DmShared.dNull(detail_Mdl.getVoucherDate()));
        snapshot.setSnapshot(detail_Mdl.getSnapshot_Mdl());
    }

}
