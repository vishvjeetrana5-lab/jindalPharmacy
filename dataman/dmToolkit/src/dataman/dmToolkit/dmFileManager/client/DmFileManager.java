package dataman.dmToolkit.dmFileManager.client;

import dataman.dmToolkit.dmFileManager.shared.DmFileManager_SrvAsync;
import dataman.dmToolkit.dmFileManager.shared.DmFileManager_Srv;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import java.util.HashMap;
import java.util.List;

public class DmFileManager extends DmFileManager_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void close();
    }

    private CallBack callBack;
    private final ServiceInvoker<DmFileManager_SrvAsync> SrvAsync;
    protected boolean canAttach, canDelete;
    protected String applicableFileRule = "", tempKeyCode, keyCode, sNo;
    protected HashMap<String, String> multiDocIdWithRuleCode;

    public String getKeyCode() {
        return this.keyCode;
    }

    public String getTempKeyCode() {
        return this.tempKeyCode;
    }

    public String getSNo() {
        return this.sNo;
    }

    public String getApplicableFileRule() {
        return this.applicableFileRule;
    }

    public HashMap<String, String> getMultiDocIdWithRuleCode() {
        return radioHistory.getValue() == true ? multiDocIdWithRuleCode : new HashMap<String, String>();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
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

    public void show(String keyCode, String sNo, boolean canAttach, boolean canDelete, HashMap<String, String> multiDocIdWithRuleCode) {
        onShow(keyCode, sNo, canAttach, canDelete, multiDocIdWithRuleCode);
    }

    public void show(String keyCode, String sNo, boolean canAttach, boolean canDelete) {
        onShow(keyCode, sNo, canAttach, canDelete, null);
    }

    private void onShow(String keyCode, String sNo, boolean canAttach, boolean canDelete, HashMap<String, String> multiDocIdWithRuleCode) {
        this.canAttach = canAttach;
        this.canDelete = canDelete;
        this.keyCode = keyCode;
        this.sNo = sNo;
        this.multiDocIdWithRuleCode = multiDocIdWithRuleCode;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        flwWorkingArea.clear();
        flwCurrentHistoryParent.clear();
        radioCurrent.setValue(true);
        radioHistory.setValue(false);

        if (multiDocIdWithRuleCode != null && multiDocIdWithRuleCode.size() > 0) {
            flwCurrentHistoryParent.add(flwCurrentHistory);
        }

        getFileType();
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean withCloseEvent) {
        this.popupPanel.hide();
        if (withCloseEvent) {
            if (callBack != null) {
                callBack.close();
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true);
        } else if (event.getSource() == radioCurrent || event.getSource() == radioHistory) {
            getFileType();
        }
    }

    private void getFileType() {
        SrvAsync.invokeRPC().getFileType(keyCode, tempKeyCode, sNo, applicableFileRule, null, getMultiDocIdWithRuleCode(), new AsyncCallback<DmResult<List<DmFileRuleType>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<DmFileRuleType>> result) {
                flwWorkingArea.clear();
                if (result.isStatus()) {
                    for (DmFileRuleType mrTemp : result.getData()) {
                        if (!DmShared.xNull(mrTemp.getFileTypeName()).trim().equalsIgnoreCase("")) {
                            DmFileType dmFileType = new DmFileType(mrTemp, DmFileManager.this);

                            flwWorkingArea.add(dmFileType.asWidget());
                        }
                    }
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public DmFileManager(String applicableFileRule) {
        super();
        SrvAsync = new ServiceInvoker<DmFileManager_SrvAsync>(GWT.create(DmFileManager_Srv.class), DmFileManager.class.getName());
        this.applicableFileRule = applicableFileRule;
        popupPanel.addAttachHandler(this);
        btnClose.addClickHandler(this);
        radioCurrent.addClickHandler(this);
        radioHistory.addClickHandler(this);
    }

    public void setTempKeyCode(String tempKeyCode) {
        this.tempKeyCode = tempKeyCode;
    }
}
