package aarogya.selfToolkit.labApproval.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import java.util.List;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import aarogya.selfToolkit.labApproval.shared.TxtHSampleNo_Mdl;
import aarogya.selfToolkit.labApproval.shared.LabApproval_Mdl;
import aarogya.selfToolkit.labApproval.shared.TxtHLabNo_Mdl;
import aarogya.selfToolkit.labApproval.shared.LabApproval_Srv;
import aarogya.selfToolkit.labApproval.shared.LabApproval_SrvAsync;
import aarogya.selfToolkit.labApproval.shared.TxtHItem_Mdl;
import aarogya.selfToolkit.labApproval.shared.TxtHTechnician_Mdl;

public class Setting<T1, T2> extends Setting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurEvent.BlurHandler {

    interface CallBack {

        public void onOk(LabApproval_Mdl sampleEntryApproval_Mdl);
    }

    CallBack callBack;
    ServiceInvoker<LabApproval_SrvAsync> srvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(boolean editable, LabApproval_Mdl mrTemp) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtFromDate.setFocus(true);
        flwOk.clear();
        flwOk.setVisible(false);

        if (editable) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
        displayData(mrTemp);
    }

    private void displayData(LabApproval_Mdl mrTemp) {

        txtFromDate.setText(DmShared.dNull(mrTemp.getFromDate()));
        txtToDate.setText(DmShared.dNull(mrTemp.getToDate()));
        txtHLabNo.setTag(DmShared.xNull(mrTemp.getLabCode()));
        txtHLabNo.setText(DmShared.xNull(mrTemp.getLabRecId()));
        txtHTechnician.setTag(DmShared.xNull(mrTemp.getTechnicianCode()));
        txtHTechnician.setText(DmShared.xNull(mrTemp.getTechnicianName()));
        txtHTestTemplate.setTag(DmShared.xNull(mrTemp.getTestTemplateCode()));
        txtHTestTemplate.setText(DmShared.xNull(mrTemp.getTestTemplateName()));
        txtHSampleNo.setTag(DmShared.xNull(mrTemp.getSampleCode()));
        txtHSampleNo.setText(DmShared.xNull(mrTemp.getSampleRecId()));
        txtHItem.setTag(DmShared.xNull(mrTemp.getItemCode()));
        txtHItem.setText(DmShared.xNull(mrTemp.getItemName()));

        txtHStatus.setTag(DmShared.xNull(mrTemp.getStatusCode()));
        txtHStatus.setText(DmShared.xNull(mrTemp.getStatusName()));
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void txtKeyEvents() {
        txtFromDate.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
            }
        });

        txtToDate.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
            }
        });
    }

    private void textHelpEvents() {

        txtHLabNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHLabNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLabNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHLabNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLabNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHLabNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHLabNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLabNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {

                }
            }
        });

        txtHStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHStatus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHStatus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHStatus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHStatus).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHTechnician.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHTechnician_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTechnician(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTechnician_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTechnician).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTechnician_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTechnician.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTechnician).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {

                }
            }
        });

        txtHSampleNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHSampleNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSampleNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSampleNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSampleNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSampleNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSampleNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSampleNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {

                }
            }
        });
        txtHItem.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHItem_Mdl>() {

            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItem(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItem_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItem).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {

                }
            }
        });

        txtHTestTemplate.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTestTemplate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTestTemplate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTestTemplate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTestTemplate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {

                }
            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
                    new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtFromDate).show();
                    return;
                }
            }
            fillRecords();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    private void fillRecords() {
        LabApproval_Mdl mrTemp = new LabApproval_Mdl();

        mrTemp.setFromDate(DmShared.dNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.dNull(txtToDate.getText()));
        mrTemp.setLabCode(DmShared.xNull(txtHLabNo.getTag()));
        mrTemp.setLabRecId(DmShared.xNull(txtHLabNo.getText()));
        mrTemp.setTechnicianCode(DmShared.xNull(txtHTechnician.getTag()));
        mrTemp.setTechnicianName(DmShared.xNull(txtHTechnician.getText()));
        mrTemp.setTestTemplateCode(DmShared.xNull(txtHTestTemplate.getTag()));
        mrTemp.setTestTemplateName(DmShared.xNull(txtHTestTemplate.getText()));
        mrTemp.setSampleCode(DmShared.xNull(txtHSampleNo.getTag()));
        mrTemp.setSampleRecId(DmShared.xNull(txtHSampleNo.getText()));
        mrTemp.setItemCode(DmShared.xNull(txtHItem.getTag()));
        mrTemp.setItemName(DmShared.xNull(txtHItem.getText()));
        mrTemp.setStatusCode(DmShared.xNull(txtHStatus.getTag()));
        mrTemp.setStatusName(DmShared.xNull(txtHStatus.getText()));

        callBack.onOk(mrTemp);
        popupPanel.hide();
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

    public Setting() {
        super();
        srvAsync = new ServiceInvoker<LabApproval_SrvAsync>(GWT.create(LabApproval_Srv.class), LabApproval.class.getName());
        textHelpEvents();
        txtKeyEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

}
