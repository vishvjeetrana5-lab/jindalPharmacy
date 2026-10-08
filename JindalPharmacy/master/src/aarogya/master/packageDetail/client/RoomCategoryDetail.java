package aarogya.master.packageDetail.client;

import aarogya.master.packageDetail.shared.GrdRoomCategory_Mdl;
import aarogya.master.packageDetail.shared.PackageDetail_SrvAsync;
import aarogya.master.packageDetail.shared.PackageDetail_Srv;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class RoomCategoryDetail<T1, T2> extends RoomCategoryDetail_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    public interface CallBack {

        public void onClose();
    }

    private final ServiceInvoker<PackageDetail_SrvAsync> srvAsync;
    private CallBack callBack;
    private String code, packageCode, organizationCode, fromDate, toDate;
    private final PackageDetail packageDetail = new PackageDetail();

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void gridEvents() {
        grdRoomCategory.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showGridEdit(event.getRowIndex());
            }
        });
        grdRoomCategory.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showGridEdit(grdRoomCategory.getGrid().getStore().indexOf(grdRoomCategory.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showGridEdit(int currentRowIndex) {
        GrdRoomCategory_Mdl node = grdRoomCategory.getGrid().getStore().get(currentRowIndex);
        packageDetail.show(node.getPackageCode(), packageCode, organizationCode,
                node.getSharedKeyFieldCode(), code, true, false);
    }

    public void show(String code, String packageCode, String organizationCode, String fromDate, String toDate) {
        this.code = code;
        this.packageCode = packageCode;
        this.organizationCode = organizationCode;
        this.fromDate = fromDate;
        this.toDate = toDate;

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        getPackageDetail();
    }

    public void hide() {
        this.popupPanel.hide();
        callBack.onClose();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
            packageDetail.hide();
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

    public RoomCategoryDetail() {
        super();
        srvAsync = new ServiceInvoker<PackageDetail_SrvAsync>(GWT.create(PackageDetail_Srv.class), PackageDetail.class.getName());
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Room Category Wise Detail");
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        manageEvent();
        gridEvents();
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);

        packageDetail.addCallBack(new PackageDetail.CallBack() {
            @Override
            public void onClose() {
                getPackageDetail();
            }
        });
    }

    private void getPackageDetail() {
        srvAsync.invokeRPC().getRoomCategoryDetail(DmShared.xNull(packageCode), DmShared.xNull(organizationCode), fromDate, toDate, new AsyncCallback<DmResult<List<GrdRoomCategory_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdRoomCategory_Mdl>> result) {
                if (result.isStatus()) {
                    grdRoomCategory.getGrid().getStore().clear();
                    grdRoomCategory.getGrid().getStore().addAll(result.getData());
                    ((CellSelectionModel) grdRoomCategory.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

}
