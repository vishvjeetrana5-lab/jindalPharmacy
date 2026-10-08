package aarogya.master.prescriptionGuiMapping.client;

import aarogya.master.prescriptionGuiMapping.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import java.util.List;

public class PrescriptionGuiMapping extends PrescriptionGuiMapping_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    ServiceInvoker<PrescriptionGuiMapping_SrvAsync> SrvAsync;
    final PrescriptionGuiMappingGridEdit<GridDescription_Mdl, GridDescription_PrpAcs> gridEdit = new PrescriptionGuiMappingGridEdit(grdDescription);
    private final ExportPopup popup = new ExportPopup();

    public PrescriptionGuiMapping(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(PrescriptionGuiMapping_Srv.class), PrescriptionGuiMapping.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        textHelpEvents();
        handlerEvent();
        gridEvents();
        onLoad();
        findEvents();
        swi.setWidget(this);
        txtHEntry.setFocus(true);
    }

    private void textHelpEvents() {
        txtHEntry.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEntry(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEntry).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEntry.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEntry).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

                if (blnStatus && t1Row != null) {
                    grdDescription.getGrid().getStore().add(new GridDescription_Mdl());
                    topControlMoveRec(t1Row.getSharedKeyFieldCode());
                } else {

                }

            }
        });
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PrescriptionGuiMapping.this);
                }
            }
        }).show();
    }

    private PrescriptionGuiMapping_Mdl topControlSaveInitialize() {
        PrescriptionGuiMapping_Mdl mrTemp = new PrescriptionGuiMapping_Mdl();
        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHEntry.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHEntry.getText()));
        grdDescription.commitChanges();
        mrTemp.getDescrip_Mdl().addAll(grdDescription.getGrid().getStore().getAll());
        return mrTemp;

    }

    private void topControlSave() {
        PrescriptionGuiMapping_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            PrescriptionGuiMapping_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHEntry).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {

                        topControlMoveRec(result.getData());
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {

            case 100:
                new DmAlertMessageBox(errorMessage, txtHEntry).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHEntry).show();
        }

    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PrescriptionGuiMapping_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PrescriptionGuiMapping_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PrescriptionGuiMapping_Mdl> result) {
        PrescriptionGuiMapping_Mdl data;
        if (result.isStatus()) {
            data = result.getData();

            if (!DmShared.xNull(data.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                txtHEntry.setTag(DmShared.xNull(data.getSharedKeyFieldCode()));
                txtHEntry.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            }

            grdDescription.getGrid().getStore().clear();
            grdDescription.getGrid().getStore().addAll(data.getDescrip_Mdl());
            grdDescription.getGrid().getStore().add(new GridDescription_Mdl());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
//        topControl.setDisplay(true);
        txtHEntry.setFocus(true);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());
        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                }
                );
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl t1Row) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);
                topControlMoveRec(t1Row.getSharedKeyFieldCode());
            }
        });

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void gridEvents() {
        grdDescription.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showGrid(event.getRowIndex());

            }
        });
        grdDescription.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    int row = 0;

                    row = grdDescription.getGrid().getStore().indexOf(grdDescription.getGrid().getSelectionModel().getSelectedItem());
                    showGrid(row);
                }
            }
        }, KeyDownEvent.getType());
        grdDescription.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                //  gridEduEdit(event.getRowIndex());
            }
        });
        gridEdit.addCallBack(new PrescriptionGuiMappingGridEdit.CallBack() {

            @Override
            public void onOk(GridDescription_Mdl eduquali) {
                grdDescription.commitChanges();
            }

            @Override
            public void onDelete() {
                grdDescription.commitChanges();
            }
        });
    }

    private void showGrid(int index) {

        gridEdit.show(index, true, true, true);

    }

    private void onLoad() {

        grdDescription.getGrid().getStore().clear();

        grdDescription.getGrid().getStore().add(new GridDescription_Mdl());
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource() == btnClose) {
                onClose();
            } else if (event.getSource() == btnFind) {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            } else if (event.getSource() == btnSave) {
                topControlSave();
            } else if (event.getSource() == btnOther) {
                menu.show(btnOther);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            topControlSave();
        }
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(manageExport.getText())) {
            popup.show();
        }
    }

    private void handlerEvent() {
        btnFind.addClickHandler(this);
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnOther.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

}
