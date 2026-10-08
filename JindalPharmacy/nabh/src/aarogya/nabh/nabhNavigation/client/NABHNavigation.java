package aarogya.nabh.nabhNavigation.client;

import aarogya.nabh.nabhNavigation.shared.GrdNABHNavigation_Mdl;
import aarogya.nabh.nabhNavigation.shared.NABHNavigation_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import aarogya.nabh.nabhNavigation.shared.NABHNavigation_SrvAsync;
import aarogya.nabh.nabhNavigation.shared.NABHNavigation_Srv;
import aarogya.nabh.self.shared.NABH_Column;
import aarogya.nabh.self.shared.NABH_Shared;
import aarogya.projectMenu.self.shared.pmNABH;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;
import java.util.Map;

public final class NABHNavigation extends NABHNavigation_Dsg implements BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyPressHandler {

    private final ServiceInvoker<NABHNavigation_SrvAsync> srvAsync;
    private final UserMenu userMenuPermission;
    protected String voucherType = "";
    pmNABH nm = new pmNABH();

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(NABHNavigation.this);
                    }
                }
            }).show();
        }
    }

    private void textHelpEvents() {
        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    getUHIDDetails("", "", txtHType.getText());
                    clear();
                }
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtIpdNo.setFocus(true);
                    }
                });
            }
        });

        grdNabhIndicator.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                openWidget(event.getRowIndex(), event.getCellIndex());
            }
        });
    }

    private void openWidget(int rowIndex, int colIndex) {
        GrdNABHNavigation_Mdl grdNABHNavigation_Mdl;
        String menuLink = "";

//        if (rowIndex >= 1 && colIndex >= 1) {
//            grdNABHNavigation_Mdl = grdNabhIndicator.getGrid().getStore().get(rowIndex);
//            if (grdNABHNavigation_Mdl != null) {
//                menuLink = DmShared.xNull(grdNABHNavigation_Mdl.getMenuLink());
//
//                if (menuLink.equals(nm.Indicator_1.caption)
//                        || menuLink.equals(nm.Indicator_2.caption)
//                        || menuLink.equals(nm.Indicator_3.caption)
//                        || menuLink.equals(nm.Indicator_4.caption)
//                        || menuLink.equals(nm.Indicator_11.caption)
//                        || menuLink.equals(nm.Indicator_12.caption)
//                        || menuLink.equals(nm.Indicator_51.caption)
//                        || menuLink.equals(nm.Indicator_52.caption)) {
//
//                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(nm.moduleName, menuLink, null, null, new SetWidgetInvokerImpl() {
//                        @Override
//                        public void setWidget(final IsWidget wgtObject) {
//                            IsWidgetExtended widget = ((IsWidgetExtended) wgtObject);
//                            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
//                        }
//                    });
//                }
//            }
//        } else 
        if (rowIndex == 9 && colIndex == 5) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(nm.moduleName, new pmNABH().INDICATOR_10_REPORT.caption, null, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {

                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            });
        }
       else if (rowIndex == 12 && colIndex == 5) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(nm.moduleName, new pmNABH().INDICATOR_13_REPORT.caption, null, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {

                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            });
        }
    }

    public NABHNavigation(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(NABHNavigation_Srv.class), NABHNavigation.class.getName());
        widgetNameDisplay = menuPermission.getMenuName();
        this.asWidget().setTitle(widgetNameDisplay);
        toolContainer.setTitle(widgetNameDisplay);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        userMenuPermission = menuPermission;
        userPermission(userMenuPermission);
        onAdd();
        textHelpEvents();
        btnClose.addClickHandler(this);
        txtIpdNo.addKeyPressHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addBlurHandler(this);
        manageDisplay(false);
        grdNabhIndicator.getGrid().getStore().clear();
        fillGrid();
        swi.setWidget(this);
    }

    private void userPermission(UserMenu menuPermission) {
        boolean userParam = false;

        userParam = true;

        if (menuPermission.getMenuName().equals(widgetName)) {
            if (menuPermission.getUserPermission().contains("A")) {
            }
            userParam = true;
        }

    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(NABHNavigation.this);
                }
            }
        }).show();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
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

    public void manageDisplay(boolean isEnable) {
        txtIpdNo.setEnabled(true);
        txtUHIDNo.setEnabled(false);
        txtPatient.setEnabled(false);
        txtGuardian.setEnabled(false);
        txtMobile.setEnabled(false);
        txtGender.setEnabled(false);
        txtAge.setEnabled(false);
        txtAddress.setEnabled(false);
        txtOrganization.setEnabled(false);
        txtRegConsultant.setEnabled(false);
        txtAdmittingTime.setEnabled(false);
        txtAdmittingDate.setEnabled(false);
        txtAdmittingTime.setEnabled(false);
//        grdNabhIndicator.getGrid().getStore().clear();

    }

    private void getUHIDDetails(String regDocId, String recID, String voucherCat) {
        srvAsync.invokeRPC().getPatientDetail(regDocId, recID, voucherCat, new AsyncCallback<DmResult<NABHNavigation_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtIpdNo).show();
                txtIpdNo.setFocus(true);
            }

            @Override
            public void onSuccess(DmResult<NABHNavigation_Mdl> result) {
                if (result.isStatus() && result.getData() != null) {
                    fillHeader(result);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtIpdNo).show();
                    clear();
                }
            }
        });
    }

    private void fillHeader(DmResult<NABHNavigation_Mdl> result) {
        txtIpdNo.setFocus(true);
        NABHNavigation_Mdl data = result.getData();

        if (result.isStatus()) {
            data = result.getData();
            txtIpdNo.setTag(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtIpdNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtPatient.setText(DmShared.xNull(data.getPatient()));
            txtGuardian.setText(DmShared.xNull(data.getGuardian()));
            txtGender.setText(DmShared.xNull(data.getGender()));
            txtUHIDNo.setText(DmShared.xNull(data.getRecIdUHID()));
            txtAddress.setText(DmShared.xNull(data.getAddress()));
            txtOrganization.setText(DmShared.xNull(data.getOrganizationText()));
            txtRegConsultant.setText(DmShared.xNull(data.getConsultantText()));
            txtAge.setText(DmShared.xNull(data.getAge()));
            txtMobile.setText(DmShared.xNull(data.getMobile()));
            txtAdmittingDate.setText(DmShared.dNull(data.getRegDate()));
            txtAdmittingTime.setText(DmShared.xNull(data.getRegTime()));
            txtHType.setTag(DmShared.xNull(data.getTypeTag()));
            txtHType.setText(DmShared.xNull(data.getTypeText()));
        }

        txtIpdNo.setEnabled(true);
    }

    public void clear() {
        txtIpdNo.setText("");
        txtPatient.setText("");
        txtGuardian.setText("");
        txtGender.setText("");
        txtUHIDNo.setText("");
        txtAddress.setText("");
        txtOrganization.setText("");
        txtRegConsultant.setText("");
        txtAge.setText("");
        txtAdmittingDate.setText("");
        txtAdmittingTime.setText("");

    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtIpdNo)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtIpdNo.getText()).trim().equalsIgnoreCase("")) {
                    getUHIDDetails("", txtIpdNo.getText(), txtHType.getText());
                }
            }
        }

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue.getText())) {
            txtSearchValue.setText(txtSearchValue.getText());

        }
    }

    public void fillGrid() {
        try {
            NABH_Column c = new NABH_Column();
            GrdNABHNavigation_Mdl grd_Mdl = null;
//            for (NABHIndicators_Mdl node : NABH_Shared.getNABHIndicators().getRows()) {
            for (Map<String, String> map : NABH_Shared.getNABHIndicators().getRows()) {
                grd_Mdl = new GrdNABHNavigation_Mdl();
                grd_Mdl.setMenuLink(map.get(c.menu));
                grd_Mdl.setStd(map.get(c.std));
                grd_Mdl.setIndicatorDescription(map.get(c.bo));
                grd_Mdl.setStatusTag("0");
                grdNabhIndicator.getGrid().getStore().add(grd_Mdl);

            }
//            }
        } catch (Exception ex) {

        }
    }

    private void onAdd() {
        txtHType.setFocus(true);
        txtHType.setTag("OPD");
        txtHType.setText("OPD");
    }

}
