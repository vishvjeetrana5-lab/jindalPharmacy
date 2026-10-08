package aarogya.nabh.roomHelp.client;

import aarogya.nabh.roomHelp.shared.RoomHelp_Mdl;
import aarogya.nabh.roomHelp.shared.GrdRoomHelp_Mdl;
import aarogya.selfToolkit.patientSearchRoomWise.client.PatientSearchRoomWise;
import aarogya.selfToolkit.patientSearchRoomWise.shared.SearchValueRoomWise_Mdl.FilterType;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import aarogya.nabh.roomHelp.shared.PatientHistorySearch_Mdl;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;
import aarogya.nabh.roomHelp.shared.RoomHelp_Srv;
import aarogya.nabh.roomHelp.shared.RoomHelp_SrvAsync;
import aarogya.selfToolkit.stStatic.shared.STEnum.MappingNature;
import java.util.HashMap;

public final class RoomHelp extends RoomHelp_Dsg implements BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyPressHandler, KeyDownHandler {

    private final ServiceInvoker<RoomHelp_SrvAsync> srvAsync;
    private final PatientSearchRoomWise patientSearch = new PatientSearchRoomWise(FilterType.NOT_BILLED_APPROVED);
    private final Rd guiPopup = new Rd();
    protected boolean addPrm, editPrm, deletePrm, printPrm, manageFilePrm, isCancel = false;
    private final UserMenu userMenuPermission;
    private String searchDbFieldName;
    protected String voucherType = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private MappingNature mappingNature;
//    private int searchColumnIndex, rowIndex = 0;
    PatientHistorySearch_Mdl searchMdl = new PatientHistorySearch_Mdl();

    private void textHelpEvents() {
        guiPopup.addCallBack(new Rd.CallBack() {

            @Override
            public void save(KeyFieldExtended keyField) {
                RoomHelp.this.flwForm.clear();
                RoomHelp.this.flwForm.add(RoomHelp.this.vrtInnerForm);
                getUHIDDetails(DmShared.xNull(keyField.getSharedKeyFieldCode()), "");
            }

            @Override
            public void close() {
                RoomHelp.this.flwForm.clear();
                RoomHelp.this.flwForm.add(RoomHelp.this.vrtInnerForm);
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        ((CellSelectionModel) grdGuiEntry.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                    }
                });

            }
        });
        patientSearch.addCallBack(new PatientSearchRoomWise.CallBack() {

            @Override
            public void ok(String regDocid, String regRecid, String roomCode) {
                getUHIDDetails(DmShared.iNull(regDocid).toString(), DmShared.xNull(regRecid));
                txtIpdNo.setFocus(true);
            }

            @Override
            public void close() {
                txtIpdNo.setFocus(true);
            }
        });
        txtHGUI.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTemplateName(helper, blnToEndOfFile, mappingNature, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtIpdNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGUI.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtIpdNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    fillPrecriptionMdl(txtIpdNo.getTag(), "", DmShared.iNull(t1Row.getSharedKeyFieldCode()).toString());

                }

            }
        });
    }

    private void fillPrecriptionMdl(String regDocId, String docId, String templateCode) {
        flwForm.clear();
        flwForm.add(guiPopup.show(mappingNature, regDocId, docId, templateCode, isCancel));
    }

    private void gridEvents() {
//        setHeaderValue(0);
//        grdGuiEntry.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
//            @Override
//            public void onHeaderClick(HeaderClickEvent event) {
//                setHeaderValue(event.getColumnIndex());
//            }
//        });

        grdGuiEntry.addCallBack(new DmGridPaging.CallBack<PatientHistorySearch_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, PatientHistorySearch_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getPatientHistory(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdRoomHelp_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdRoomHelp_Mdl>> result) {
                        if (result.isStatus()) {
                            grdGuiEntry.setData(result.getData(), result.getPaging());
                            txtSearchValue.setFocus(true);
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public PatientHistorySearch_Mdl getParam() {
                return valueIntialize();
            }

        });

        grdGuiEntry.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                fillPrecriptionMdl("", DmShared.xNull(grdGuiEntry.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()), DmShared.xNull(grdGuiEntry.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getTemplateCode()));
            }
        });
        grdGuiEntry.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    fillPrecriptionMdl("", grdGuiEntry.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode(), grdGuiEntry.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getTemplateCode());
                }
            }
        }, KeyDownEvent.getType());
    }

    private PatientHistorySearch_Mdl valueIntialize() {
        PatientHistorySearch_Mdl value_Mdl = new PatientHistorySearch_Mdl();
        value_Mdl = searchMdl;
        value_Mdl.setFieldName(searchDbFieldName);
        value_Mdl.setFieldValue(DmShared.xNull(txtSearchValue.getText()));
        value_Mdl.setRegDocId(DmShared.xNull(txtIpdNo.getTag()));
        return value_Mdl;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnPatientSearch) {
            patientSearch.show();
        } else if (event.getSource() == btnNew) {
            if (DmShared.xNull(txtIpdNo.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please select IPD No.", txtIpdNo).show();
                return;
            }
            txtHGUI.show(btnNew, "");
            if (txtHGUI.getHelper().equals("")) {
                fillPrecriptionMdl(null, null, null);
            }
            txtHGUI.reQuery("");
        } else if (event.getSource() == btnClose) {
            new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(RoomHelp.this);
                    }
                }
            }).show();
        }
    }

//    private void setHeaderValue(int columnIndex) {
//        if ((grdGuiEntry.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
//            searchColumnIndex = 0;
//        } else if (columnIndex < 0) {
//            searchColumnIndex = grdGuiEntry.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
//        } else {
//            searchColumnIndex = columnIndex;
//        }
//
//        DmColumnConfig col = ((DmColumnConfig) grdGuiEntry.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
//        searchDbFieldName = col.getDbFieldName();
//        searchDbFieldDataType = col.getDbFieldDataType();
//        grdGuiEntry.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
//        grdGuiEntry.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
//        txtSearchValue.setText("");
//        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
//        for (ColumnConfig col1 : grdGuiEntry.getDmGrid().getGrid().getColumnModel().getColumns()) {
//            col1.setColumnHeaderClassName("dm-Garbage");
//        }
//        col.setColumnHeaderClassName("dm-ColActiveForFilter");
//        grdGuiEntry.getDmGrid().getGrid().getView().refresh(true);
//        txtSearchValue.setFocus(true);
//    }
    public RoomHelp(UserMenu menuPermission, HashMap<String, Object> paramHashMap, SetWidgetInvoker swi) {
        super();
        if (paramHashMap != null && paramHashMap.containsKey("indicator")) {
            mappingNature = (MappingNature) paramHashMap.get("indicator");
        }
        srvAsync = new ServiceInvoker<>(GWT.create(RoomHelp_Srv.class), RoomHelp.class.getName());
        widgetNameDisplay = mappingNature.getValue();
        this.asWidget().setTitle(widgetNameDisplay);
        toolContainer.setTitle(widgetNameDisplay);
        toolContainer.addWidget(flwNew);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        userMenuPermission = menuPermission;
        userPermission(userMenuPermission);
        textHelpEvents();
        btnPatientSearch.addClickHandler(this);
        btnClose.addClickHandler(this);
        btnNew.addClickHandler(this);
        txtIpdNo.addKeyPressHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addKeyDownHandler(this);
        manageDisplay(false);
        grdGuiEntry.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        gridEvents();
        swi.setWidget(this);
        btnPatientSearch.setFocus(true);
    }

    private void userPermission(UserMenu menuPermission) {
        boolean userParam = false;

        addPrm = false;
        editPrm = false;
        deletePrm = false;
        printPrm = false;
        manageFilePrm = false;

        userParam = true;

        if (menuPermission.getMenuName().equals(widgetNameDisplay)) {
            if (menuPermission.getUserPermission().contains("A")) {
            }
            userParam = true;
        }

        if (userParam) {
            if (menuPermission.getUserPermission().contains("E")) {
                editPrm = true;
            }
            if (menuPermission.getUserPermission().contains("D")) {
                deletePrm = true;
            }
            if (menuPermission.getUserPermission().contains("P")) {
                printPrm = true;
            }

        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(RoomHelp.this);
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
        txtRoomNo.setEnabled(false);
        txtPatient.setEnabled(false);
        txtGuardian.setEnabled(false);
        txtMobile.setEnabled(false);
        txtGender.setEnabled(false);
        txtAge.setEnabled(false);
        txtAddress.setEnabled(false);
        txtOrganization.setEnabled(false);
        txtRegConsultant.setEnabled(false);
        txtCase.setEnabled(false);
        txtDaysOPD.setEnabled(false);
        txtDtOPD.setEnabled(false);
        txtAdmittingTime.setEnabled(false);
        txtAdmittingDate.setEnabled(false);
        txtAdmittingTime.setEnabled(false);
        flwPatientSearch.clear();
        flwPatientSearch.add(btnPatientSearch);
        btnPatientSearch.setFocus(true);

    }

    private void getUHIDDetails(String regDocId, String recID) {
        srvAsync.invokeRPC().getPatientDetail(regDocId, recID, new AsyncCallback<DmResult<RoomHelp_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtIpdNo).show();
                txtIpdNo.setFocus(true);
            }

            @Override
            public void onSuccess(DmResult<RoomHelp_Mdl> result) {
                if (result.isStatus() && result.getData() != null) {
                    fillHeader(result);
                }
            }
        });
    }

    private void fillHeader(DmResult<RoomHelp_Mdl> result) {
        RoomHelp_Mdl data = result.getData();

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
            txtCase.setText(DmShared.xNull(data.getCaseText()));
            txtRegConsultant.setText(DmShared.xNull(data.getConsultantText()));
            txtAge.setText(DmShared.xNull(data.getAge()));
            txtMobile.setText(DmShared.xNull(data.getMobile()));
            txtDaysOPD.setText(DmShared.xNull(data.getDaysOfOPD()));
            txtDtOPD.setText(DmShared.dNull(data.getRegDate()));
            txtRoomNo.setText(DmShared.xNull(data.getRoomNo()));
            txtAdmittingDate.setText(DmShared.dNull(data.getRegDate()));
            txtAdmittingTime.setText(DmShared.xNull(data.getRegTime()));
            grdGuiEntry.getDmGrid().getGrid().getStore().clear();
            grdGuiEntry.getDmGrid().getGrid().getStore().addAll(data.getGrdGUIEntry_Mdl());

            voucherType = DmShared.iNull(data.getVoucherTypeTag()).toString();
            isCancel = false;
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), btnPatientSearch).show();
            voucherType = "";
        }

        txtIpdNo.setEnabled(true);
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtIpdNo)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtIpdNo.getText()).trim().equalsIgnoreCase("")) {
                    getUHIDDetails("", txtIpdNo.getText());
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

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdGuiEntry.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdGuiEntry.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            }
        }
    }

    private void reQuery() {
        grdGuiEntry.getHelper().setRowsStartFrom(0);
        grdGuiEntry.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdGuiEntry.reQuery();
    }

}
