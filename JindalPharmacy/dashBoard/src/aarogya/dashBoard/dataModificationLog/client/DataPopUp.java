package aarogya.dashBoard.dataModificationLog.client;

import aarogya.dashBoard.dataModificationLog.shared.DataModificationLog_Srv;
import aarogya.dashBoard.dataModificationLog.shared.DataModificationLog_SrvAsync;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class DataPopUp extends DataPopUp_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    private ServiceInvoker<DataModificationLog_SrvAsync> srvAsync;
//    interface CallBack {
//
//        public void onClose();
//    }
//    private CallBack callBack;
//    public void addCallBack(CallBack callBack) {
//        this.callBack = callBack;
//    }

    public DataPopUp() {
        super();
        srvAsync = new ServiceInvoker<DataModificationLog_SrvAsync>(GWT.create(DataModificationLog_Srv.class), DataModificationLog.class.getName());
        popupPanel.addAttachHandler(this);
        manageEvents();
    }

    public void show(String newData, String oldData, final String moduleName, final String menuName, final String recordName) {
        srvAsync.invokeRPC().getTxtData(newData, oldData, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    popupPanel.show();
                    popupPanel.center();
                    popupPanel.setGlassEnabled(true);

                    setLabel(moduleName, menuName, recordName);

                    txtData.setText(result.getData());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void setLabel(String moduleName, String menuName, String recordNameOne) {
        clear();
        if (!DmShared.xNull(moduleName).equalsIgnoreCase("")) {
            lblModuleName.setText(moduleName);
        }
        if (!DmShared.xNull(menuName).equalsIgnoreCase("")) {
            lblDashOne.setText(" - ");
            lblMenuName.setText(menuName);
        }
        if (!DmShared.xNull(recordNameOne).equalsIgnoreCase("")) {
            lblDashTwo.setText(" - ");
            lblRecordName1.setText(recordNameOne);
        }

    }

//        Gson g = new Gson();
//        Type mapType = new TypeToken<Map<String, Object>>() {
//        }.getType();
//        Map<String, Object> firstMap = g.fromJson(newData, mapType);
//        Map<String, Object> secondMap = g.fromJson(oldData, mapType);
//        txtData.setText(Maps.difference(firstMap, secondMap).toString());
    private void clear() {
//        txtData.setText(Maps.difference(firstMap, secondMap));

        lblMenuName.setText("");
        lblModuleName.setText("");
        lblRecordName1.setText("");
        lblDashOne.setText("");
        lblDashTwo.setText("");

    }

    private void setData(String Data) {
        clear();
        txtData.setText(Data);

    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
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

    private void manageEvents() {

        btnCancel.addClickHandler(this);

    }

}
