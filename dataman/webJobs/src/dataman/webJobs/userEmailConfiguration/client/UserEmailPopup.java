package dataman.webJobs.userEmailConfiguration.client;

import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_SrvAsync;
import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_Srv;
import com.google.gwt.core.client.GWT;
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
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import java.util.ArrayList;
import java.util.List;

public class UserEmailPopup extends UserEmailPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    CallBack callBack;
    ServiceInvoker<UserEmailConfiguration_SrvAsync> SrvAsync;
    List<KeyFieldExtended> parentRow;
    final UserEmailPopupGridEdit<KeyFieldExtended, dmGrd_PrpAcs> emailId = new UserEmailPopupGridEdit(dmGrdEmail);
    Boolean isEditable = false;
    private DmTextBox txtBox;

    public interface CallBack {

        public void onOk(List<KeyFieldExtended> keyFields, DmTextBox txtBox);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(List<KeyFieldExtended> keyFields, Boolean isEditable, DmTextBox txtBox) {
        this.txtBox = txtBox;
        this.isEditable = isEditable;
        flwOk.clear();
        if (isEditable) {
            flwOk.add(btnOk);
        }
        this.parentRow = keyFields;
        dmGrdEmail.getGrid().getStore().clear();
        dmGrdEmail.getGrid().getStore().addAll(parentRow);
        dmGrdEmail.getGrid().getStore().add(new KeyFieldExtended());
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOk) {
            dmGrdEmail.commitChanges();
            parentRow = new ArrayList<KeyFieldExtended>();
            for (KeyFieldExtended field : dmGrdEmail.getGrid().getStore().getAll()) {
                if (!DmShared.xNull(field.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    parentRow.add(field);
                }
            }
            callBack.onOk(parentRow, txtBox);
            hide();
        } else if (event.getSource().equals(btnCancel)) {
            hide();
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

    public UserEmailPopup() {
        super();
        SrvAsync = new ServiceInvoker<UserEmailConfiguration_SrvAsync>(GWT.create(UserEmailConfiguration_Srv.class), UserEmailConfiguration.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        gridEventsComposition();
    }

    private void gridEventsComposition() {

        emailId.addCallBack(new UserEmailPopupGridEdit.CallBack() {
            @Override
            public void onOk(KeyFieldExtended keyField) {
                dmGrdEmail.commitChanges();
            }

            @Override
            public void onDelete() {
                dmGrdEmail.commitChanges();
            }
        });

        dmGrdEmail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showGridEditComposition(event.getRowIndex());
            }
        });

        dmGrdEmail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showGridEditComposition(dmGrdEmail.getGrid().getStore().indexOf(dmGrdEmail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showGridEditComposition(int currentRowIndex) {
        if (!isEditable) {
            emailId.show(currentRowIndex, false, false, false);
        } else {
            emailId.show(currentRowIndex, true, true, true);
        }
    }

}
