package aarogya.master.consultantSharingMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.consultantSharingMaster.shared.ConsultantSharingMaster_Srv;
import aarogya.master.consultantSharingMaster.shared.ConsultantSharingMaster_SrvAsync;
import aarogya.master.consultantSharingMaster.shared.SearchValue_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class SearchPopup extends SearchPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(SearchValue_Mdl key_Mdl);

        public void onCancel();
    }

    CallBack callBack;
    private final ServiceInvoker<ConsultantSharingMaster_SrvAsync> SrvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHSearchType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSearchType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSearchType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSearchType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSearchType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("RC")) {
                        flwTR.clear();
                        flwTR.add(flwRoomCategory);
                    } else if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("ORG")) {
                        flwTR.clear();
                        flwTR.add(flwEmpanelment);
                    } else if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("MN")) {
                        flwTR.clear();
                        flwTR.add(flwModuleName);
                    } else if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("SC")) {
                        flwTR.clear();
                        flwTR.add(flwServiceCategory);
                    } else if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("SG")) {
                        flwTR.clear();
                        flwTR.add(flwServiceGroup);
                    } else if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("SRV")) {

                        flwTR.clear();
                        flwTR.add(flwService);
                    } else {
                        flwTR.clear();
                    }
                }
            }
        });
        txtHEmpanelment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEmpanelment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEmpanelment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEmpanelment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEmpanelment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHModule.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHModule(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModule).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModule.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModule).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHServiceCat.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceCat(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceCat).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceCat).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        txtHServiceGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHService.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHService(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHService).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHService.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHService).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        txtHRoomCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRoomCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoomCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoomCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRoomCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtHSearchType.setTag("SRV");
        txtHSearchType.setText("Service");
        flwTR.clear();
        flwTR.add(flwService);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHSearchType.setFocus(true);
            }
        });

    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            callBack.onCancel();
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            SearchValue_Mdl mdl = new SearchValue_Mdl();
            if (DmShared.xNull(txtHSearchType.getTag()).equalsIgnoreCase("RC")) {
                mdl.setRoomCategoryCode(DmShared.xNull(txtHRoomCategory.getTag()));
            } else if (DmShared.xNull(txtHSearchType.getTag()).equalsIgnoreCase("ORG")) {
                mdl.setOrganizationCode(DmShared.xNull(txtHEmpanelment.getTag()));
            } else if (DmShared.xNull(txtHSearchType.getTag()).equalsIgnoreCase("MN")) {
                mdl.setModuleName(DmShared.xNull(txtHModule.getTag()));
            } else if (DmShared.xNull(txtHSearchType.getTag()).equalsIgnoreCase("SC")) {
                mdl.setServiceCategoryCode(DmShared.xNull(txtHServiceCat.getTag()));
            } else if (DmShared.xNull(txtHSearchType.getTag()).equalsIgnoreCase("SG")) {
                mdl.setServiceGroupCode(DmShared.xNull(txtHServiceGroup.getTag()));
            } else if (DmShared.xNull(txtHSearchType.getTag()).equalsIgnoreCase("SRV")) {
                mdl.setServiceCode(DmShared.xNull(txtHService.getTag()));
            }
            callBack.onOk(mdl);
            popupPanel.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.onCancel();
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

    public SearchPopup() {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(ConsultantSharingMaster_Srv.class), ConsultantSharingMaster.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        textHelpEvents();
    }

}
