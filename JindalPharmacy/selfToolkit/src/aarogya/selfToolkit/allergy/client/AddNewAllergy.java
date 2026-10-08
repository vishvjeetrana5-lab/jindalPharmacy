package aarogya.selfToolkit.allergy.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.allergy.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public class AddNewAllergy extends AddNewAllergy_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void onOk(GrdAllergy_Mdl grdAllergy_Mdl);

        public void onClose();
    }

    private final ServiceInvoker<Allergy_SrvAsync> srvAsync;
    private CallBack callBack;

    private void textHelpEvents() {
        txtHMedNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAllergyNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    protected void show() {
        flwPanel.center();
        flwPanel.setGlassEnabled(true);
        clear();
        txtName.setFocus(true);
    }

    private void handlerEvents() {
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        flwPanel.addAttachHandler(this);
    }

    protected void addCallBack(CallBack callBack) {
        if (callBack != null) {
            this.callBack = callBack;
        }
    }

    protected void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.flwPanel.hide();
        if (isCallEvent) {
            this.callBack.onClose();
        }
    }

    private void clear() {
        txtName.setText("");
        txtHMedNature.setTag("");
        txtHMedNature.setText("");
    }

    public AddNewAllergy() {
        super();
        srvAsync = new ServiceInvoker<Allergy_SrvAsync>(GWT.create(Allergy_Srv.class), Allergy.class.getName());
        handlerEvents();
        textHelpEvents();
        clear();
    }

    @Override
    public Widget asWidget() {
        return flwPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnClose)) {
            hide(true);
        } else if (event.getSource().equals(btnSave)) {
            if (DmShared.xNull(txtName.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Name is mandatory.", txtName).show();
                return;
            } else if (DmShared.xNull(txtHMedNature.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Nature is mandatory.", txtHMedNature).show();
                return;
            } else {
                topControlSave();
            }
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

    private GrdAllergy_Mdl topControlSaveInitialize() {
        GrdAllergy_Mdl mrTemp = new GrdAllergy_Mdl();

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()).trim());
        mrTemp.setNatureCode(DmShared.xNull(txtHMedNature.getTag()));
        mrTemp.setNatureName(DmShared.xNull(txtHMedNature.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        GrdAllergy_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            srvAsync.invokeRPC().saveAllergy(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        GrdAllergy_Mdl mrTemp = new GrdAllergy_Mdl();
                        mrTemp.setSharedKeyFieldCode(DmShared.xNull(result.getData()));
                        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
                        mrTemp.setNatureCode(DmShared.xNull(txtHMedNature.getTag()));
                        mrTemp.setNatureName(DmShared.xNull(txtHMedNature.getText()));
                        callBack.onOk(mrTemp);
                        hide(false);
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHMedNature).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
        }
    }

}
