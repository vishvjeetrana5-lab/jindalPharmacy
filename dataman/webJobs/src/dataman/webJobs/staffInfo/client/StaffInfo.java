package dataman.webJobs.staffInfo.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class StaffInfo extends StaffInfo_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(List<KeyFieldExtended> lstQueue);

        public void onClose();
    }
    private CallBack callBack;
    private final StaffInfoGridEdit<KeyFieldExtended, GrdStaffInfo_PrpAcs> infoGridEdit = new StaffInfoGridEdit(grdStaffInfo);
    private String mode = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(Widget showRelativeWidget, String mode, List<KeyFieldExtended> lstQueue) {
        this.mode = mode;
        flwOk.clear();
        if (!mode.equalsIgnoreCase("Browse")) {
            flwOk.add(flwOkChild);
        }
        popupPanel.show();
        popupPanel.showRelativeTo(showRelativeWidget);
        popupPanel.setGlassEnabled(true);
        grdStaffInfo.getGrid().getStore().clear();
        grdStaffInfo.getGrid().getStore().addAll(lstQueue);
        grdStaffInfo.getGrid().getStore().add(new KeyFieldExtended());
        ((CellSelectionModel) grdStaffInfo.getGrid().getSelectionModel()).selectCell(0, 1);
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    private void gridEvents() {
        infoGridEdit.addCallBack(new StaffInfoGridEdit.CallBack() {

            @Override
            public void onOk(KeyFieldExtended queueMdl) {
                grdStaffInfo.commitChanges();
            }

            @Override
            public void onDelete() {
                grdStaffInfo.commitChanges();
            }
        });
        grdStaffInfo.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridQueueEdit(event.getRowIndex());
            }
        });
        grdStaffInfo.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridQueueEdit(grdStaffInfo.getGrid().getStore().indexOf(grdStaffInfo.getGrid().getSelectionModel().getSelectedItem()));
                }

            }
        }, KeyDownEvent.getType());
    }

    private void gridQueueEdit(int currentRowIndex) {
        boolean flag = !mode.equalsIgnoreCase("Browse");
        infoGridEdit.show(currentRowIndex, flag, flag, flag);

    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            this.callBack.onClose();
        }
    }

    public void hide() {
        hide(false);
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            callBack.onOk(grdStaffInfo.getGrid().getStore().getAll());
            hide(false);
        }
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
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public StaffInfo() {
        super();
        manageEvent();
        gridEvents();
    }

}
