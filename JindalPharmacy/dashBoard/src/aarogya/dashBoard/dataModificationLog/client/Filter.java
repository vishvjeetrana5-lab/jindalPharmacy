package aarogya.dashBoard.dataModificationLog.client;

import aarogya.dashBoard.dataModificationLog.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class Filter extends Filter_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    interface CallBack {

        public void onOk(SearchValue_Mdl search_Mdl);
    }

    private CallBack callBack;
    private final ServiceInvoker<DataModificationLog_SrvAsync> srvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(SearchValue_Mdl searchMdl, DefaultValue_Mdl defaultValueMdl) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtFromDate.setFocus(true);

        if (searchMdl != null) {
            txtFromDate.setText(DmShared.dNull(searchMdl.getFromDate()));
            txtToDate.setText(DmShared.dNull(searchMdl.getToDate()));

            txtHUserName.setTag(DmShared.xNull(searchMdl.getUserNameCode()));
            txtHUserName.setText(DmShared.xNull(searchMdl.getUserName()));

            txtHFY.setTag(DmShared.xNull(searchMdl.getFinancialyearCode()));
            txtHFY.setText(DmShared.xNull(searchMdl.getFinancialyear()));

            txtHMode.setTag(DmShared.xNull(searchMdl.getModeCode()));
            txtHMode.setText(DmShared.xNull(searchMdl.getMode()));
            
            txtHSite.setTag(DmShared.xNull(searchMdl.getSiteCode()));
            txtHSite.setText(DmShared.xNull(searchMdl.getSiteName()));

            txtHVoucherType.setTag(DmShared.xNull(searchMdl.getVrTypeShortNameCode()));
            txtHVoucherType.setText(DmShared.xNull(searchMdl.getVrTypeShortName()));

            txtHModules.setTag(DmShared.xNull(searchMdl.getModuleCode()));
            txtHModules.setText(DmShared.xNull(searchMdl.getModule()));

            txtHMenus.setTag(DmShared.xNull(searchMdl.getMenuCode()));
            txtHMenus.setText(DmShared.xNull(searchMdl.getMenu()));

            txtHModifyReason.setTag(DmShared.xNull(searchMdl.getModifyReasonCode()));
            txtHModifyReason.setText(DmShared.xNull(searchMdl.getModifyReason()));

            txtRecordName_1.setText(DmShared.xNull(searchMdl.getRecordName1()));
            txtRecordName_2.setText(DmShared.xNull(searchMdl.getRecordName2()));

        } else {
            txtFromDate.setText(DmShared.dNull(defaultValueMdl.getFromDate()));
            txtToDate.setText(DmShared.dNull(defaultValueMdl.getToDate()));

        }

    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void textHelpEvents() {

        txtHUserName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHUserName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUserName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUserName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUserName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHFY.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHFy(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFY).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHFY.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFY).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHModules.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHModules(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModules).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModules.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModules).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHMenus.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMenus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMenus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMenus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMenus).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHVoucherType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHModifyReason.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHModifyReason(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModifyReason).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModifyReason.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModifyReason).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHMode.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        
         txtHSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });

            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {

            if (DmShared.xNull(txtFromDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("From Date is mandatory.", txtFromDate).show();
                return;
            } else if (DmShared.xNull(txtToDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("To Date is mandatory.", txtToDate).show();
                return;
            }

            onOk();
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

    public Filter() {
        super();
        srvAsync = new ServiceInvoker<DataModificationLog_SrvAsync>(GWT.create(DataModificationLog_Srv.class), DataModificationLog.class.getName());
        popupPanel.addAttachHandler(this);
        textHelpEvents();
        manageEvents();
    }

    private void manageEvents() {
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    private void onOk() {
        SearchValue_Mdl mrTemp = new SearchValue_Mdl();
        mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));

        mrTemp.setUserNameCode(DmShared.xNull(txtHUserName.getTag()));
        mrTemp.setUserName(DmShared.xNull(txtHUserName.getText()));

        mrTemp.setFinancialyearCode(DmShared.xNull(txtHFY.getTag()));
        mrTemp.setFinancialyear(DmShared.xNull(txtHFY.getText()));

        mrTemp.setModeCode(DmShared.xNull(txtHMode.getTag()));
        mrTemp.setMode(DmShared.xNull(txtHMode.getText()));
        
         mrTemp.setSiteCode(DmShared.xNull(txtHSite.getTag()));
        mrTemp.setSiteName(DmShared.xNull(txtHSite.getText()));

        mrTemp.setVrTypeShortNameCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVrTypeShortName(DmShared.xNull(txtHVoucherType.getText()));

        mrTemp.setModuleCode(DmShared.xNull(txtHModules.getTag()));
        mrTemp.setModule(DmShared.xNull(txtHModules.getText()));

        mrTemp.setMenuCode(DmShared.xNull(txtHMenus.getTag()));
        mrTemp.setMenu(DmShared.xNull(txtHMenus.getText()));

        mrTemp.setModifyReasonCode(DmShared.xNull(txtHModifyReason.getTag()));
        mrTemp.setModifyReason(DmShared.xNull(txtHModifyReason.getText()));

        mrTemp.setRecordName1(DmShared.xNull(txtRecordName_1.getText()));
        mrTemp.setRecordName2(DmShared.xNull(txtRecordName_2.getText()));

        callBack.onOk(mrTemp);
        hide();
    }
}
