package aarogya.inventory.physicalStockPosting.client;

import aarogya.inventory.physicalStockPosting.shared.TxtHDateOn_Mdl;
import aarogya.inventory.physicalStockPosting.shared.DefaultValue_Mdl;
import aarogya.inventory.physicalStockPosting.shared.Find_Mdl;
import aarogya.inventory.physicalStockPosting.shared.PhysicalStockPosting_Mdl;
import aarogya.inventory.physicalStockPosting.shared.PhysicalStockPosting_Vld;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;
import aarogya.inventory.physicalStockPosting.shared.PhysicalStockPosting_Srv;
import aarogya.inventory.physicalStockPosting.shared.PhysicalStockPosting_SrvAsync;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;

public final class PhysicalStockPosting extends PhysicalStockPosting_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents {

    private final ServiceInvoker<PhysicalStockPosting_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    private String uEntDt_LatestLine = "", selectedVoucherDate = "", balancingAc = "";

    public PhysicalStockPosting(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        srvAsync = new ServiceInvoker<PhysicalStockPosting_SrvAsync>(GWT.create(PhysicalStockPosting_Srv.class), PhysicalStockPosting.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        btnPostedBy.addClickHandler(this);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        iniDefaultValues(swi);
    }

    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PHYSICAL_STOCK_POSTING})) {
            @Override
            public String getVoucherType() {
                return txtHVoucherDate.getTag2nd();
            }

            @Override
            public String getVoucherDate() {
                return txtHVoucherDate.getText();
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
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                if (DmShared.xNull(userControl.getTextBlog(postedByBlog)).equalsIgnoreCase("")) {
                    new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {
                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                topControlDelete(topControl.getDocId());
                            }
                        }
                    }).show();
                } else {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is posted. You can't delete this record.", (DmTextBox) null).show();
                }
            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String fetchCode) {
                topControlMoveRec(fetchCode);
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
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PhysicalStockPosting.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHVoucherDate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHDateOn_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedVoucherDate = DmShared.dNull(txtHVoucherDate.getText());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDateOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHDateOn_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherDate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHDateOn_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherDate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherDate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHDateOn_Mdl t1Row) {
                if (blnStatus) {
                    if (!DmShared.dNull(selectedVoucherDate).trim().equalsIgnoreCase(DmShared.dNull(txtHVoucherDate.getText()).trim())) {
                        txtDepartment.setTag("");
                        txtDepartment.setText("");
                        txtFilterType.setTag("");
                        txtFilterType.setText("");
                        txtFilterValue.setTag("");
                        txtFilterValue.setText("");
                    }
                    if (t1Row != null) {
                        txtDepartment.setTag(t1Row.getDepartmentCode());
                        txtDepartment.setText(t1Row.getDepartmentName());
                        txtFilterType.setTag(t1Row.getFilterTypeCode());
                        txtFilterType.setText(t1Row.getFilterTypeName());
                        txtFilterValue.setTag(t1Row.getFilterValueCode());
                        txtFilterValue.setText(t1Row.getFilterValueName());
                    }
                }
            }
        });
    }

    private void setPosted() {
        PhysicalStockPosting_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            PhysicalStockPosting_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC(true).setPosted(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHVoucherDate).show();
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

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Issue No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHVoucherDate.getTag2nd(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    swi.setWidget(PhysicalStockPosting.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private PhysicalStockPosting_Mdl topControlSaveInitialize() {
        PhysicalStockPosting_Mdl mrTemp = new PhysicalStockPosting_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdObj().setCounter(DmShared.iNull(txtVoucherNo.getTag()).longValue());
            mrTemp.getRecIdObj().setPrefix(DmShared.xNull(txtVoucherNo.getTag2nd()));
            mrTemp.getRecIdObj().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtHVoucherDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherDate.getTag2nd()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtDepartment.getTag()));
        mrTemp.setDepartmentName(DmShared.xNull(txtDepartment.getText()));
        mrTemp.setFilterTypeCode(DmShared.xNull(txtFilterType.getTag()));
        mrTemp.setFilterTypeName(DmShared.xNull(txtFilterType.getText()));
        mrTemp.setFilterValueCode(DmShared.xNull(txtFilterValue.getTag()));
        mrTemp.setFilterValueName(DmShared.xNull(txtFilterValue.getText()));
        mrTemp.setU_EntDt_LatestLine(DmShared.xNull(uEntDt_LatestLine));
        mrTemp.setBalancingAc(balancingAc);
        mrTemp.setInventoryAccountingActive(defaultValue.isInventoryAccountingActive());
        return mrTemp;
    }

    private void topControlSave() {
        PhysicalStockPosting_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            PhysicalStockPosting_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHVoucherDate).show();
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

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVoucherDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHVoucherDate).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PhysicalStockPosting_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PhysicalStockPosting_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PhysicalStockPosting_Mdl> result) {
        clear();
        if (result.isStatus()) {
            PhysicalStockPosting_Mdl mrTemp = result.getData();
            topControl.setDocId(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtVoucherNo.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            txtVoucherNo.setTag(DmShared.iNull(mrTemp.getRecIdObj().getCounter()).toString());
            txtVoucherNo.setTag2nd(DmShared.xNull(mrTemp.getRecIdObj().getPrefix()));
            txtHVoucherDate.setTag2nd(DmShared.xNull(mrTemp.getVoucherTypeCode()));
            txtHVoucherDate.setText(DmShared.dNull(mrTemp.getVoucherDate()));
            balancingAc = DmShared.xNull(mrTemp.getBalancingAc());
            txtDepartment.setTag(DmShared.xNull(mrTemp.getDepartmentCode()));
            txtDepartment.setText(DmShared.xNull(mrTemp.getDepartmentName()));
            txtFilterType.setTag(DmShared.xNull(mrTemp.getFilterTypeCode()));
            txtFilterType.setText(DmShared.xNull(mrTemp.getFilterTypeName()));
            txtFilterValue.setTag(DmShared.xNull(mrTemp.getFilterValueCode()));
            txtFilterValue.setText(DmShared.xNull(mrTemp.getFilterValueName()));
            uEntDt_LatestLine = mrTemp.getU_EntDt_LatestLine();
            userControl.setTextBlog(preparedByBlog, DmShared.xNull(mrTemp.getSharedPreparedBy()));
            userControl.setTextBlog(postedByBlog, DmShared.xNull(mrTemp.getPostedBy()));
//            userControl.setTextBlog(preparedByBlog, mrTemp.getSharedPreparedBy());
//            userControl.setTextBlog(postedByBlog, mrTemp.getPostedBy());
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
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void manageDisplay(boolean flag) {
        txtVoucherNo.setEnabled(false);
        txtDepartment.setEnabled(false);
        txtFilterType.setEnabled(false);
        txtFilterValue.setEnabled(false);
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        txtHVoucherDate.setTag2nd(defaultValue.getVoucherTypeCode());
        balancingAc = DmShared.xNull(defaultValue.getBalancingAc());
        txtHVoucherDate.setFocus(true);
    }

    private void clear() {
        uEntDt_LatestLine = "";
        balancingAc = "";
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(postedByBlog, "");
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnPostedBy) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                setPosted();
            }
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

}
