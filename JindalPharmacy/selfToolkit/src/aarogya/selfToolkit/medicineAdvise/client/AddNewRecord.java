package aarogya.selfToolkit.medicineAdvise.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.medicineAdvise.shared.*;
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
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class AddNewRecord extends AddNewRecord_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(GrdMedicineDetail_Mdl node);

        public void onClose();
    }

    private final ServiceInvoker<MedicineAdvise_SrvAsync> srvAsync;
    private CallBack callBack;

    private void textHelpEvents() {
        txtMedicineName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestionMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtMedicineName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtMedicineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtMedicineName).show();
                        }
                    }
                });
            }
        });
        txtHMedCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHMedGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHMedGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHCompany.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCompany_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCompany_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCompany_Mdl t1Row) {
            }
        });
    }

    public void show() {
        flwPanel.center();
        flwPanel.setGlassEnabled(true);
        clear();

        txtMedicineName.setFocus(true);
    }

    public void addCallBack(CallBack callBack) {
        if (callBack != null) {
            this.callBack = callBack;
        }
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.flwPanel.hide();
        if (isCallEvent) {
            this.callBack.onClose();
        }
    }

    public AddNewRecord() {
        super();
        srvAsync = new ServiceInvoker<MedicineAdvise_SrvAsync>(GWT.create(MedicineAdvise_Srv.class), MedicineAdvise.class.getName());
        handlerEvents();
        textHelpEvents();
        clear();
    }

    private void handlerEvents() {
        flwPanel.addAttachHandler(this);
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
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
            if (DmShared.xNull(txtMedicineName.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Name is mandatory.", txtMedicineName).show();
                return;
            }
            if (DmShared.xNull(txtHMedCategory.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Category is mandatory.", txtHMedCategory).show();
                return;
            }

            topControlSave();
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

    private AddNewRecord_Mdl topControlSaveInitialize() {
        AddNewRecord_Mdl mrTemp = new AddNewRecord_Mdl();

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtMedicineName.getText()).trim());
        mrTemp.setCategoryCode(DmShared.xNull(txtHMedCategory.getTag()));
        mrTemp.setCategoryName(DmShared.xNull(txtHMedCategory.getText()));
        mrTemp.setGroupCode(DmShared.xNull(txtHMedGroup.getTag()));
        mrTemp.setGroupName(DmShared.xNull(txtHMedGroup.getText()));
        mrTemp.setCompanyCode(DmShared.xNull(txtHCompany.getTag()));
        mrTemp.setCompanyName(DmShared.xNull(txtHCompany.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        AddNewRecord_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            srvAsync.invokeRPC().saveNewRecord(mrTemp, new AsyncCallback<DmResult<AddNewRecord_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtMedicineName).show();
                }

                @Override
                public void onSuccess(DmResult<AddNewRecord_Mdl> result) {
                    if (result.isStatus()) {
                        GrdMedicineDetail_Mdl mrTemp = new GrdMedicineDetail_Mdl();
                        mrTemp.setSharedKeyFieldCode(DmShared.xNull(result.getData().getSharedKeyFieldCode()));

                        mrTemp.setSharedKeyFieldName(DmShared.xNull(result.getData().getSharedKeyFieldName()));
                        mrTemp.setDisplayName(DmShared.xNull(result.getData().getDisplayName()));

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
                new DmAlertMessageBox(errorMessage, txtMedicineName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHMedCategory).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtMedicineName).show();
                break;
        }
    }

    private void clear() {
        txtMedicineName.setText("");
        txtHMedCategory.setTag("");
        txtHMedCategory.setText("");
        txtHMedGroup.setTag("");
        txtHMedGroup.setText("");
        txtHCompany.setTag("");
        txtHCompany.setText("");
    }
}
