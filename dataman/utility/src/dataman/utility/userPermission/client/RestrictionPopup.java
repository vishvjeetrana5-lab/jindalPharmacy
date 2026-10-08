package dataman.utility.userPermission.client;

import dataman.utility.userPermission.shared.GrdMenu_Mdl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class RestrictionPopup extends RestrictionPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onSave(GrdMenu_Mdl menuMdl);
    }

    private CallBack callBack;
    private GrdMenu_Mdl menuMdl;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(GrdMenu_Mdl menuMdl) {
        this.menuMdl = menuMdl;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        clear();
        setValues();
        txtViewEntryDays.setFocus(true);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void textHelpEvents() {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnSave) {
            saveInitialize();
            callBack.onSave(menuMdl);
            popupPanel.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
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

    public RestrictionPopup() {
        super();
        btnCancel.addClickHandler(this);
        btnSave.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        textHelpEvents();
    }

    private void setValues() {
        if (menuMdl != null) {
            if (DmShared.iNull(menuMdl.getMnuLevel()).intValue() == 3) {
                txtAllowValueUpto.setText(DmClient.vNull(menuMdl.getAllowValueUpto(), "0.00"));
                txtDeleteEntryDays.setText(DmClient.vNull(menuMdl.getDp_Days(), "0"));
                txtSaveEntryDays.setText(DmClient.vNull(menuMdl.getSp_days(), "0"));
                txtViewEntryDays.setText(DmClient.vNull(menuMdl.getVp_Days(), "0"));
            }
        }
    }

    private void saveInitialize() {
        if (menuMdl != null) {
            menuMdl.setAllowValueUpto(DmClient.vNull(txtAllowValueUpto.getText(), "0.00"));
            menuMdl.setDp_Days(DmClient.vNull(txtDeleteEntryDays.getText(), "0"));
            menuMdl.setSp_days(DmClient.vNull(txtSaveEntryDays.getText(), "0"));
            menuMdl.setVp_Days(DmClient.vNull(txtViewEntryDays.getText(), "0"));
        }
    }

    private void clear() {
        txtAllowValueUpto.setText("");
        txtDeleteEntryDays.setText("");
        txtSaveEntryDays.setText("");
        txtViewEntryDays.setText("");
    }
}
