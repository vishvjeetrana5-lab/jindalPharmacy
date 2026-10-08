package dataman.utility.voucherType.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.utility.voucherType.shared.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExportData extends ExportData_Dsg implements IsWidget, ClickHandler, AttachEvent.Handler, DmRootEvents {

    private final ServiceInvoker<VoucherType_SrvAsync> srvAsync;
    private List<String> lstVType;
    private boolean checkAll = false;

    public void show() {
        popupPanel.show();
        popupPanel.setGlassEnabled(true);
        popupPanel.center();
        lstVType = new ArrayList<String>();
        getVoucherType();
    }

    private void gridEvents() {
        grdVoucherType.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                if (event.getCellIndex() == 0) {
                    grdVoucherType.lockScroll();
                    manageGridCheck(event.getCellIndex(), event.getRowIndex());
                    grdVoucherType.releaseScroll();
                }
            }
        });
        grdVoucherType.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    grdVoucherType.lockScroll();
                    try {
                        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE && !event.getNativeEvent().getCtrlKey()) {
                            int colIndex = ((CellSelectionModel) grdVoucherType.getGrid().getSelectionModel()).getSelectCell().getCell();
                            int rowIndex = grdVoucherType.getGrid().getStore().indexOf(grdVoucherType.getGrid().getSelectionModel().getSelectedItem());
                            manageGridCheck(colIndex, rowIndex);
                        }
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                    } finally {
                        grdVoucherType.releaseScroll();
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    private void manageGridCheck(int cellIndex, int rowIndex) {
        if (cellIndex == 0) {
            boolean val = grdVoucherType.getGrid().getStore().get(rowIndex).isChecked();
            grdVoucherType.getGrid().getStore().get(rowIndex).setChecked(!val);
            grdVoucherType.commitChanges();
        }
    }

    private void getVoucherType() {
        srvAsync.invokeRPC(true).getVoucherType(new AsyncCallback<DmResult<List<GrdVoucherType_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdVoucherType_Mdl>> result) {
                if (result.isStatus()) {
                    grdVoucherType.getGrid().getStore().clear();
                    grdVoucherType.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void handlerEvents() {
        btnCancel.addClickHandler(this);
        btnExport.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        cbCheck.addClickHandler(this);
    }

    private void exportJson() {
        srvAsync.invokeRPC().exportJson(DmRequestBuilder.getSessionId(), lstVType, new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
                    hide();
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

    private void manageAllCheck() {
        if (!checkAll) {
            checkAll = true;
            cbCheck.setStyleName("fa fa-check-square-o");
        } else {
            checkAll = false;
            cbCheck.setStyleName("fa fa-square-o");
        }
        for (int i = 0; i < grdVoucherType.getGrid().getStore().size(); i++) {
            grdVoucherType.getGrid().getStore().get(i).setChecked(checkAll);
            grdVoucherType.commitChanges();
        }
    }

    public ExportData() {
        super();
        srvAsync = new ServiceInvoker<VoucherType_SrvAsync>(GWT.create(VoucherType_Srv.class), VoucherType.class.getName());
        handlerEvents();
        gridEvents();
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            hide();
        } else if (event.getSource().equals(btnExport)) {
            boolean flag = false;
            for (GrdVoucherType_Mdl vTypeMdl : grdVoucherType.getGrid().getStore().getAll()) {
                if (vTypeMdl != null) {
                    if (vTypeMdl.isChecked()) {
                        if (!flag) {
                            flag = true;
                        }
                        lstVType.add(vTypeMdl.getSharedKeyFieldCode());
                    }
                }
            }
            if (!checkAll && !flag) {
                new DmAlertMessageBox("Please select any voucher type to export.", (DmTextBox) null).show();
            } else {
                new DmConfirmMessageBox("Are you sure? You want to export selected records.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportJson();
                        }
                    }
                }).show();
            }
        } else if (event.getSource().equals(cbCheck)) {
            manageAllCheck();
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    private void hide() {
        this.popupPanel.hide();
    }
}
