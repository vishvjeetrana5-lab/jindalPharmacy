package aarogya.inventory.visitorGatePass.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmCallBack;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmSnapshot.shared.DmSnapshot_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;
import java.util.List;
import aarogya.inventory.visitorGatePass.shared.*;
import aarogya.inventory.visitorGatePassSearch.client.VisitorGatePassSearch;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;

public class VisitorGatePass extends VisitorGatePass_Dsg implements BlurHandler, ClickHandler, ChangeHandler, DmRootEvents, AttachEvent.Handler {

    private ServiceInvoker<VisitorGatePass_SrvAsync> srvAsync;
    private Default_Mdl defaultValue = new Default_Mdl();
    private Boolean dateTimeEditable = false;
    private boolean gateOutFlag = false, isGateOutPermission = false;
    final VisitorGatePassSearch visitorGatePassSearch = new VisitorGatePassSearch();
    ModifyReason_Mdl modifyReasonMdl;
    final ModifyReason modifyReason = new ModifyReason();
    private DmCallBack visitorgatePassCardCallBack = null;
    private String visitorActionDocId = "";

    public VisitorGatePass(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();

        this.asWidget().setTitle(widgetName);
        srvAsync = new ServiceInvoker<>(GWT.create(VisitorGatePass_Srv.class), VisitorGatePass.class.getName());

        textHelpEvents();
        topControlEvents(menuPermission);
        findEvents();
        manageEvents();
        showVisitorGatePassSearch();
        iniDefaultValues(swi);
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);

        topControl.setRefreshRequired(false);

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateTimeEditableBlog)) {
                dateTimeEditable = true;

            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(gateOutByBlog)) {
                isGateOutPermission = true;
            }
        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().VISITOR_GATE_PASS})) {
            @Override
            public String getVoucherType() {
                return txtHVoucherType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtDate.getText();
            }

            @Override
            public String getVoucherValue() {
                return "";
            }
        });

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (DmShared.xNull(userControl.getTextBlog(gateOutByBlog)).equalsIgnoreCase("")) {
                    btnSearchByMobile.setEnabled(true);
                    getVisitorDetails(txtMobile.getText());
                    manageDisplay(true);

                    txtVisitorName.setFocus(true);
                } else {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is mark as. You can't modify this record.", (DmTextBox) null).show();
                }

            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(topControl.getDocId());
                        }
                    }
                }).show();
            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {
                prnRecord(false);
            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                if (topControl.getMode().equalsIgnoreCase("New")) {
                    topControlSave();
                } else {
                    showModifyReason();
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(VisitorGatePass.this);
            }
        });

        topControl.setDisplay(true);

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

    }

    private void textHelpEvents() {
        visitorGatePassSearch.addCallBack(new VisitorGatePassSearch.CallBack() {
            @Override
            public void ok(String strKey) {
                gateOutFlag = true;
                topControlMoveRec(strKey);
            }

            @Override
            public void close() {
                txtSlipNo.setFocus(true);
            }
        });
        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                modifyReasonMdl = modifyReason;
                topControlSave();
            }
        });
        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl tRow) {

            }

        });
        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHVoucherType.getTag()).equalsIgnoreCase(DmShared.xNull(txtHVoucherType.getTag2nd()))) {
                    txtHVoucherType.setTag2nd(txtHVoucherType.getTag());

                }
            }
        });
        txtHVehicleType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHVehicleType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVehicleType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVehicleType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVehicleType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHGateNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGateNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGateNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGateNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGateNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private void topControlSave() {
        VisitorGatePass_Mdl mrTemp;

        try {

            mrTemp = topControlSaveInitialize();
            VisitorGatePass_Vld.chkSave(mrTemp, false);

            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private VisitorGatePass_Mdl topControlSaveInitialize() {
        VisitorGatePass_Mdl mrTemp = new VisitorGatePass_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtEntryNo.getText()).trim());
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setInDate(DmShared.dNull(txtDate.getText()));

        mrTemp.setInTime(DmShared.vNull(txtTime.getText()).toString());
        mrTemp.setVisitorName(DmShared.removeSpace(DmShared.xNull(txtVisitorName.getText())));
        mrTemp.setAddress(DmShared.xNull(txtAddress.getText()));
        mrTemp.setCity_Code(DmShared.xNull(txtHCity.getTag()));
        mrTemp.setCity_Name(DmShared.xNull(txtHCity.getText()));
        mrTemp.setMobile(DmShared.xNull(txtMobile.getText()));
        mrTemp.setVehicleType(DmShared.xNull(txtHVehicleType.getText()));
        mrTemp.setVehicleNo(DmShared.xNull(txtVehicleNo.getText()));
        mrTemp.setPersonToMeet(DmShared.removeSpace(DmShared.xNull(txtPersonToMeet.getText())));
        mrTemp.setPurpose(DmShared.xNull(txtPurpose.getText()));
        mrTemp.setGateOutDate(DmShared.dNull(txtGateOutDate.getText()));
        mrTemp.setGateOutTime(DmShared.xNull(txtGateOutTime.getText()));
        mrTemp.setOrganizationName(DmShared.xNull(txtOrganizationName.getText()));
        mrTemp.setGateNo(DmShared.xNull(txtHGateNo.getText()));
        mrTemp.setSnapshot_Mdl(snapshot.getSnapshot());
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        //over write default voucher type
        defaultValue.setvTypeCode(txtHVoucherType.getTag());
        defaultValue.setvTypeName(txtHVoucherType.getText());

        return mrTemp;
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(topControl.getPermissions(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    topControl.setNavigator(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtTime).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtVisitorName).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtAddress).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHCity).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtMobile).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHVehicleType).show();
                break;
            case 750:
                new DmAlertMessageBox(errorMessage, txtVehicleNo).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtPersonToMeet).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtPurpose).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtGateOutDate).show();
                break;
            case 1100:
                new DmAlertMessageBox(errorMessage, txtGateOutTime).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtVisitorName).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<VisitorGatePass_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<VisitorGatePass_Mdl> result) {
                btnSearchByMobile.setEnabled(false);
                if (visitorgatePassCardCallBack != null) {
                    DmResult<Object> dmrTemp = new DmResult<>();
                    dmrTemp.setStatus(true);
                    visitorgatePassCardCallBack.result(dmrTemp);
                }
//        visitorgatePassCardCallBack = null;
//        weighmentCard_Mdl = null;
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<VisitorGatePass_Mdl> result) {
        VisitorGatePass_Mdl data;
        clear();
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtEntryNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtDate.setText(data.getInDate());
            txtTime.setText(data.getInTime());
            txtHVoucherType.setTag(data.getVoucherTypeCode());
            txtHVoucherType.setText(data.getVoucherTypeName());
            txtVisitorName.setText(data.getVisitorName());
            txtAddress.setText(data.getAddress());
            txtHCity.setTag(data.getCity_Code());
            txtHCity.setText(data.getCity_Name());
            txtMobile.setText(data.getMobile());
            txtHVehicleType.setTag(data.getVehicleType());
            txtHVehicleType.setText(data.getVehicleType());
            txtVehicleNo.setText(data.getVehicleNo());
            txtPersonToMeet.setText(data.getPersonToMeet());
            txtPurpose.setText(data.getPurpose());
            txtGateOutDate.setText(data.getGateOutDate());
            txtGateOutTime.setText(DmShared.xNull(data.getGateOutTime()));
            txtGateOutDateView.setText(data.getGateOutDate());
            txtGateOutTimeView.setText(DmShared.xNull(data.getGateOutTime()));
            txtOrganizationName.setText(DmShared.xNull(data.getOrganizationName()));
            txtHGateNo.setText(DmShared.xNull(data.getGateNo()));
            snapshot.setSnapshot(data.getSnapshot_Mdl());

            if (gateOutFlag) {
                txtSlipNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
                txtSlipNo.setTag2nd("");
            } else {
                txtSlipNo.setText("");
                txtSlipNo.setTag2nd("");
            }
            gateOutFlag = false;

            userControl.setTextBlog(preparedByBlog, DmShared.xNull(data.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));
            userControl.setTextBlog(gateOutByBlog, DmShared.xNull(data.getGateOutBy()));
            modifyReasonMdl = data.getModifyReasonMdl();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDate) {
            txtDate.setText(DmClient.returnDate(txtDate.getText()));
        } else if (event.getSource() == txtGateOutDate) {
            txtGateOutDate.setText(DmClient.returnDate(txtGateOutDate.getText()));
        } else if (event.getSource() == txtSlipNo) {
            getSlipDetails(txtSlipNo.getText());

        }

    }

    private void manageEvents() {
        txtDate.addBlurHandler(this);
        txtGateOutDate.addBlurHandler(this);

        txtSlipNo.addChangeHandler(this);

        btnGateOutBy.addClickHandler(this);
        btnSearchGatePass.addClickHandler(this);
        btnGateDateTime.addClickHandler(this);
        btnGateInDateTime.addClickHandler(this);
        btnSearchByMobile.addClickHandler(this);
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        setDefaultValues();
        txtMobile.setFocus(true);
        btnSearchByMobile.setEnabled(true);

    }

    private void clear() {
        userControl.setTextBlog(gateOutByBlog, "");
        modifyReasonMdl = new ModifyReason_Mdl();
        DmSnapshot_Mdl dss = new DmSnapshot_Mdl();
        dss.setMongoFileLink("");
        snapshot.setSnapshot(dss);

    }

    private void setDefaultValues() {

        txtHVoucherType.setTag(defaultValue.getvTypeCode());
        txtHVoucherType.setText(defaultValue.getvTypeName());
        txtDate.setText(DmShared.dNull(defaultValue.getDate()));
        txtTime.setText(DmShared.vNull(defaultValue.getTime()).toString());
        txtVisitorName.setFocus(true);

    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValues(new AsyncCallback<DmResult<Default_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtDate).show();
            }

            @Override
            public void onSuccess(DmResult<Default_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    manageDisplay(false);
                    swi.setWidget(VisitorGatePass.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtDate).show();
                }
            }
        });
    }

    private void setGateOut(boolean isGateOut) {
        VisitorGatePass_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            if (isGateOut) {
                VisitorGatePass_Vld.chkSave(mrTemp, true);
            }

            srvAsync.invokeRPC().setGateOut(mrTemp, isGateOut, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControlMoveRec(result.getData());
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnSearchGatePass) {
            txtSlipNo.setTag2nd("1");
            visitorGatePassSearch.show();
        } else if (event.getSource() == btnGateOutBy) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                if (DmShared.xNull(userControl.getTextBlog(gateOutByBlog)).equalsIgnoreCase("")) {
                    setGateOut(true);
                } else {
                    setGateOut(false);
                }
            }
        } else if (event.getSource() == btnGateDateTime) {
            if (!topControl.getDocId().equals("") && topControl.getMode().equalsIgnoreCase("Browse")) {
                getDefaultDateTime(true);

            }
        } else if (event.getSource() == btnGateInDateTime) {
            if (topControl.getDocId().equals("") && topControl.getMode().equalsIgnoreCase("New")) {
                getDefaultDateTime(false);
            }
        } else if (event.getSource() == btnSearchByMobile) {
            if (!txtMobile.getText().equals("")) {
                getVisitorDetails(txtMobile.getText());
            } else {
                new DmAlertMessageBox("Enter Mobile No.", txtMobile).show();
            }
        }
    }

    private void getVisitorDetails(String mobioleNo) {
        srvAsync.invokeRPC().getVisitorDetails(mobioleNo, new AsyncCallback<DmResult<VisitorGatePass_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<VisitorGatePass_Mdl> result) {
                VisitorGatePass_Mdl data;
                if (result.isStatus()) {
                    data = result.getData();
                    if (DmShared.iNull(data.getSharedKeyFieldCode()).longValue() > 0) {
                        txtVisitorName.setEnabled(false);
                        txtVisitorName.setText(DmShared.xNull(data.getVisitorName()));
                        txtOrganizationName.setText(DmShared.xNull(data.getOrganizationName()));
                        txtAddress.setText(DmShared.xNull(data.getAddress()));
                        txtHCity.setText(DmShared.xNull(data.getCity_Name()));
                        txtHCity.setTag(DmShared.xNull(data.getCity_Code()));
                        txtHVehicleType.setText(DmShared.xNull(data.getVehicleType()));
                        txtVehicleNo.setText(DmShared.xNull(data.getVehicleNo()));
                        txtPersonToMeet.setText(DmShared.xNull(data.getPersonToMeet()));
                        snapshot.setSnapshot(data.getSnapshot_Mdl());
                    } else {
                        new DmAlertMessageBox("No Record Found", txtMobile).show();
                    }

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }

            }
        });

    }

    private void getDefaultDateTime(final Boolean dateTimeFlag) {
        srvAsync.invokeRPC().getDefaultDateTime(new AsyncCallback<DmResult<Default_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Default_Mdl> result) {

                if (result.isStatus()) {
                    if (dateTimeFlag) {
                        txtGateOutDate.setText(result.getData().getDate());
                        txtGateOutTime.setText(result.getData().getTime());
                    } else {
                        txtDate.setText(result.getData().getDate());
                        txtTime.setText(result.getData().getTime());
                    }
                }
            }
        });

    }

    private void showVisitorGatePassSearch() {
        btnSearchGatePass.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                visitorGatePassSearch.show();
            }
        });
    }

    private void getSlipDetails(String slipNo) {

        srvAsync.invokeRPC().getSlipNoDetail(slipNo, new AsyncCallback<DmResult<VisitorGatePass_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<VisitorGatePass_Mdl> result) {
//                getSlipNoDetailSuccess(result);
                topControlMoveRecSuccess(result);
            }
        });
    }

    @Override
    public void onChange(ChangeEvent event) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            visitorGatePassSearch.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    private void manageDisplay(boolean flag) {
        txtEntryNo.setEnabled(false);
        flwInDateTimeContainer.clear();
        flwGateOutContainer.clear();
        if (flag) {
            if (!dateTimeEditable) {
                txtDate.setEnabled(false);
                txtTime.setEnabled(false);
            }

            if (topControl.getMode().equalsIgnoreCase("Edit")) {
                txtHVoucherType.setEnabled(false);
                if (dateTimeEditable) {
                    flwInDateTimeContainer.add(flwInDateTime);
                }
            } else {
                flwInDateTimeContainer.add(flwInDateTime);
            }
        } else {
            if (DmShared.xNull(userControl.getTextBlog(gateOutByBlog)).equalsIgnoreCase("")) {
                if (isGateOutPermission) {
                    flwGateOutContainer.add(flwGateOutEntry);
                    if (!DmShared.xNull(topControl.getDocId()).trim().equals("")) {
                        txtGateOutDate.setEnabled(false);
                        txtGateOutTime.setEnabled(false);
                        txtSlipNo.setEnabled(true);
                    }
                }
            } else {
                flwGateOutContainer.add(flwGateOutDetail);
            }
        }
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Gate Pass No.", DmShared.xNull(txtEntryNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHVoucherType.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADD_RECORD":
                    this.visitorActionDocId = (String) object;
                    this.visitorgatePassCardCallBack = callback;
                    topControlMoveRec(visitorActionDocId);
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtEntryNo.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

}
