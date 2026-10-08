package aarogya.master.environmentSettingSite.client;

import aarogya.master.environmentSettingSite.shared.GrdQueueSequence_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.QueueAlgoType;
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
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class QueuePopup extends QueuePopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void onOk(List<GrdQueueSequence_Mdl> lstQueue);

        public void onClose();
    }
    private CallBack callBack;
    private final QueueGridEdit<GrdQueueSequence_Mdl, GrdQueueSequence_PrpAcs> queueGridEdit = new QueueGridEdit(grdQueueSequence);
    private String mode = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(Widget showRelativeWidget, List<GrdQueueSequence_Mdl> lstQueue) {
        flwOk.clear();
        flwOk.add(flwOkChild);
        popupPanel.show();
        popupPanel.showRelativeTo(showRelativeWidget);
        popupPanel.setGlassEnabled(true);
        if (lstQueue.isEmpty()) {
            int i = 0;
            for (QueueAlgoType algoType : QueueAlgoType.values()) {
                ++i;
                GrdQueueSequence_Mdl queue = new GrdQueueSequence_Mdl();
                queue.setSharedKeyFieldCode(DmShared.iNull(algoType.getId()).toString());
                queue.setSharedKeyFieldName(algoType.getValue());
                queue.setPrioritySerial(DmShared.iNull(i).toString());
                lstQueue.add(queue.clone());
            }
        }
        grdQueueSequence.getGrid().getStore().clear();
        grdQueueSequence.getGrid().getStore().addAll(lstQueue);
        ((CellSelectionModel) grdQueueSequence.getGrid().getSelectionModel()).selectCell(0, 1);
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    private void gridEvents() {
        queueGridEdit.addCallBack(new QueueGridEdit.CallBack() {

            @Override
            public void onOk(GrdQueueSequence_Mdl queueMdl) {
                grdQueueSequence.commitChanges();
            }

            @Override
            public void onDelete() {
                grdQueueSequence.commitChanges();
            }
        });
        grdQueueSequence.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridQueueEdit(event.getRowIndex());
            }
        });
        grdQueueSequence.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridQueueEdit(grdQueueSequence.getGrid().getStore().indexOf(grdQueueSequence.getGrid().getSelectionModel().getSelectedItem()));
                }

            }
        }, KeyDownEvent.getType());
    }

    private void gridQueueEdit(int currentRowIndex) {
        boolean flag = !mode.equalsIgnoreCase("Browse");
        queueGridEdit.show(currentRowIndex, flag, false, false);

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
            boolean flag = false;
            for (GrdQueueSequence_Mdl queue : grdQueueSequence.getGrid().getStore().getAll()) {
                if (DmShared.iNull(queue.getPrioritySerial()).intValue() == 0) {
                    flag = true;
                    break;
                }
                for (GrdQueueSequence_Mdl timing : grdQueueSequence.getGrid().getStore().getAll()) {
                    if (DmShared.iNull(queue.getPrioritySerial()).intValue() == DmShared.iNull(timing.getPrioritySerial()).intValue()) {
                        if (queue.getSharedKeyFieldHashCode() != timing.getSharedKeyFieldHashCode()) {
                            new DmAlertMessageBox("S.No.: " + timing.getPrioritySerial() + " is already exist.", (DmTextBox) null).show();;
                            return;
                        }
                    }
                }
            }

            if (flag) {
                new DmAlertMessageBox("Please fill S.No. of All OPD Type", (DmTextBox) null).show();
                return;
            }
            callBack.onOk(grdQueueSequence.getGrid().getStore().getAll());
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

    public QueuePopup() {
        super();
        manageEvent();
        gridEvents();
    }

}
