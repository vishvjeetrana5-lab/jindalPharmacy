package dataman.dmToolkitR.dmFileManager.client;

import dataman.dmToolkitR.dmFileManager.shared.DmFileManager_SrvAsync;
import dataman.dmToolkitR.dmFileManager.shared.DmFileManager_Srv;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkitR.dmAlertBox.client.DmAlertBox;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import java.util.HashMap;
import java.util.List;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class DmFileManager extends DmFileManager_Dsg implements IsWidget, ClickHandler {

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
        return tabPaneHistory.isActive() == true ? multiDocIdWithRuleCode : new HashMap<String, String>();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public FlowPanel show(String keyCode, String sNo, boolean canAttach, boolean canDelete, HashMap<String, String> multiDocIdWithRuleCode) {
        onShow(keyCode, sNo, canAttach, canDelete, multiDocIdWithRuleCode);
        return flwFileManager;
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
        tabPaneCurrent.setActive(true);
        tabPaneCurrent.setIn(true);
        tabCurrent.setActive(true);

        getFileType();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == tabCurrent.getWidget(0)) {
            setTabActive(true);
            getFileType();
        } else if (event.getSource() == tabHistory.getWidget(0)) {
            setTabActive(false);
            getFileType();
        }
    }

    private void setTabActive(boolean flag) {
        if (flag) {

            tabCurrent.setActive(true);
            tabPaneCurrent.setActive(true);
            tabPaneCurrent.setIn(true);

            tabHistory.setActive(false);
            tabPaneHistory.setActive(false);
            tabPaneHistory.setIn(false);

        } else {
            tabCurrent.setActive(false);
            tabPaneCurrent.setActive(false);
            tabPaneCurrent.setIn(false);
            tabPaneCurrent.setFade(false);

            tabHistory.setActive(true);
            tabPaneHistory.setActive(true);
            tabPaneHistory.setIn(true);
            tabPaneHistory.setFade(true);

        }
    }

    private void getFileType() {
        SrvAsync.invokeRPC().getFileType(keyCode, tempKeyCode, sNo, applicableFileRule, null, getMultiDocIdWithRuleCode(), new AsyncCallback<DmResult<List<DmFileRuleType>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<DmFileRuleType>> result) {
                flwFileCurrent.clear();
                flwFileHistory.clear();
                if (result.isStatus()) {
                    for (DmFileRuleType mrTemp : result.getData()) {
                        if (!DmShared.xNull(mrTemp.getFileTypeName()).trim().equalsIgnoreCase("")) {
                            DmFileType dmFileType = new DmFileType(mrTemp, DmFileManager.this);
                            if (tabPaneCurrent.isActive()) {
                                flwFileCurrent.add(dmFileType.asWidget());
                            } else {
                                flwFileHistory.add(dmFileType.asWidget());
                            }

                        }
                    }
                } else {
                    new DmAlertBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return this.flwFileManager;
    }

    public DmFileManager(String applicableFileRule) {
        super();
        SrvAsync = new ServiceInvoker<DmFileManager_SrvAsync>(GWT.create(DmFileManager_Srv.class), DmFileManager.class.getName());
        this.applicableFileRule = applicableFileRule;
        tabCurrent.addClickHandler(this);
        tabHistory.addClickHandler(this);
    }

    public void setTempKeyCode(String tempKeyCode) {
        this.tempKeyCode = tempKeyCode;
    }
}
