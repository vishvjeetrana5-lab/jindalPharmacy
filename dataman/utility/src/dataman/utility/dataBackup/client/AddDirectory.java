package dataman.utility.dataBackup.client;

import dataman.utility.dataBackup.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class AddDirectory extends AddDirectory_Dsg implements ClickHandler, AttachEvent.Handler, DmRootEvents {

    interface CallBack {

        public void onOk(GrdDataBackup_Mdl grdMdl);
    }

    private final ServiceInvoker<DataBackup_SrvAsync> srvAsync;
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void clear() {
        txtBackupLocation.setText("");
        txtBackupLocation.setTag("");
        txtBackupLocation.setTag2nd("");
        txtFileLocation.setText("");
        txtFileLocation.setTag("");
        txtFileLocation.setTag2nd("");
        txtFileName.setText("");
        txtFileName.setTag("");
        txtFileName.setTag2nd("");
        txtHElement.setText("");
        txtHElement.setTag("");
        txtHElement.setTag2nd("");
        txtHDatabase.setText("");
        txtHDatabase.setTag("");
        txtHDatabase.setTag2nd("");
    }

    public void show(Widget wdg) {
        clear();
        flwElementName.clear();
        flwElementName.add(flwTextElement);
        popupPanel.showRelativeTo(wdg);
        popupPanel.setGlassEnabled(true);
        txtHElement.setFocus(true);
        displayRow();
    }

    public void displayRow() {
    }

    public void onOk() {
        if (DmShared.xNull(txtHElement.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Type is mandatory.", txtHElement).show();
            return;
        }

        if (DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_COMPANY.getId()
                || DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_TRANSACTION.getId()
                || DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_MONGO.getId()) {
            if (DmShared.xNull(txtHDatabase.getTag()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Name is mandatory.", txtHDatabase).show();
                return;
            }
        } else {
            if (DmShared.xNull(txtFileName.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Name is mandatory.", txtFileName).show();
                return;
            }
        }

        if (DmShared.xNull(txtFileLocation.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Element Location is mandatory.", txtFileLocation).show();
            return;
        }

        if (DmShared.xNull(txtBackupLocation.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Backup Location is mandatory.", txtFileLocation).show();
            return;
        }

        GrdDataBackup_Mdl row = new GrdDataBackup_Mdl();
        row.setChecked(true);
        row.setBackupLocation(DmShared.xNull(txtBackupLocation.getText()).trim());
        row.setElementLocation(DmShared.xNull(txtFileLocation.getText()).trim());
        if (DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_COMPANY.getId()
                || DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_TRANSACTION.getId()
                || DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_MONGO.getId()) {
            row.setElementName(DmShared.removeSpace(DmShared.xNull(txtHDatabase.getTag())));
        } else {
            row.setElementName(DmShared.removeSpace(DmShared.xNull(txtFileName.getText())));
        }

        row.setElementTypeCode(DmShared.xNull(txtHElement.getTag()));
        row.setElementType(DmShared.xNull(txtHElement.getText()));
        manageOk(row);
    }

    public void manageOk(final GrdDataBackup_Mdl row) {
        srvAsync.invokeRPC().saveRecord(row, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnOk).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    callBack.onOk(row);
                    hide(false);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnOk).show();
                }
            }
        });
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            callBack.onOk(null);
        }
    }

    private void textHelpEvents() {
        txtHElement.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getElementType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHElement).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHElement.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHElement).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHDatabase.setText("");
                    txtHDatabase.setTag2nd("");
                    txtHDatabase.setTag("");
                    txtFileName.setText("");
                    txtFileName.setTag2nd("");
                    txtFileName.setTag("");
                    txtFileLocation.setText("");
                    txtFileLocation.setTag2nd("");
                    txtFileLocation.setTag("");
                    txtBackupLocation.setText("");
                    txtBackupLocation.setTag2nd("");
                    txtBackupLocation.setTag("");
                    txtFileLocation.setReadOnly(false);
                    flwElementName.clear();
                    if (DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_COMPANY.getId()
                            || DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_TRANSACTION.getId()
                            || DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_MONGO.getId()) {
                        flwElementName.add(flwHelpElement);
                    } else {
                        flwElementName.add(flwTextElement);
                    }
                }
            }
        });
        txtHDatabase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getDatabaseName(helper, blnToEndOfFile, DmShared.xNull(txtHElement.getTag()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDatabase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDatabase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDatabase).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtFileLocation.setText("");
                    txtFileLocation.setTag("");
                    txtFileLocation.setTag2nd("");
                    txtFileLocation.setText(DmShared.xNull(t1Row.getSharedModifiedBy()));
                    txtFileLocation.setReadOnly((DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_COMPANY.getId()
                            || DmShared.iNull(txtHElement.getTag()).intValue() == DmEnum.DataBackUp.DB_TRANSACTION.getId()));
                }
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            onOk();
        } else if (event.getSource().equals(btnCancel)) {
            hide(true);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        }
    }

    public AddDirectory() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(DataBackup_Srv.class), DataBackup.class.getName());
        textHelpEvents();
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

}
