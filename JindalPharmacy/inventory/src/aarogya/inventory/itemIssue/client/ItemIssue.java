package aarogya.inventory.itemIssue.client;

import aarogya.inventory.itemIssue.shared.ItemIssue_Vld;
import aarogya.inventory.itemIssue.shared.Find_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHRequisitionNo_Mdl;
import aarogya.inventory.itemIssue.shared.DefaultValue_Mdl;
import aarogya.inventory.itemIssue.shared.ItemIssue_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHIssueTo_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHProject_Mdl;
import aarogya.inventory.itemIssue.shared.GrdItemDetail_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHRegNo_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import aarogya.inventory.itemIssue.shared.ItemIssue_Srv;
import aarogya.inventory.itemIssue.shared.ItemIssue_SrvAsync;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmDayCare;
import aarogya.projectMenu.self.shared.pmOPD;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.opdSearch.client.OPDSearch;
import aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl;
import aarogya.selfToolkit.patientSearchRoomWise.client.PatientSearchRoomWise;
import aarogya.selfToolkit.patientSearchRoomWise.shared.SearchValueRoomWise_Mdl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.ArrayList;
import java.util.HashMap;

public final class ItemIssue extends ItemIssue_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents, ClickHandler, KeyDownHandler {

    private final ServiceInvoker<ItemIssue_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    final ItemIssueGridEdit<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> itemDetailGridEdit = new ItemIssueGridEdit(gridItemDetail);
    private String deptCode = "", voucherShortName = "";
    boolean departmentEditable = false, dateEditable = false;
    ItemIssue_Mdl previousModel = null;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;
    private final PatientSearchRoomWise roomSearchIPD = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_DISCHARGED, SearchValueRoomWise_Mdl.FilterType.IPD_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_PHARMACY);
    private final PatientSearchRoomWise roomSearchEMRG = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_DISCHARGED, SearchValueRoomWise_Mdl.FilterType.EMERGENCY_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_PHARMACY);
    private PatientSearchRoomWise.CallBack roomSearchCallBack;
    private final OPDSearch opdSearch = new OPDSearch();
    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
 boolean isExpiryItems = false;
    private void topControlEvents(UserMenu menuPermission) {

        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(departmentEditableBlog)) {
                departmentEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateEditableBlog)) {
                dateEditable = true;
            }

        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().ITEM_ISSUE})) {
            @Override
            public String getVoucherType() {
                return txtHVoucherType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtVoucherDate.getText();
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
                if (DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PRJIS") || DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PRSIS")) {
                    flwBlank.add(vl.add(projectDet()));
                }
                if (DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PASIS")) {
                    flwBlank.add(vl.add(patientDet()));
                }

                txtHRequisition.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(true);
                gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                txtHRequisition.setFocus(true);
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
//                new DmConfirmMessageBox("Do you want to send print to printer?", new DialogHideEvent.DialogHideHandler() {
//
//                    @Override
//                    public void onDialogHide(DialogHideEvent event) {
//                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
//                            prnRecord(true);
//                        } else {
                prnRecord(false);
//                        }
//                    }
//                }).show();
            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                save();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ItemIssue.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                if (blnStatus && t1Row != null) {
                    txtHVoucherType.setTag2nd(t1Row.getSharedPreparedBy());
                    voucherShortName = t1Row.getSharedModifiedBy();
                    flwBlank.clear();
                    if (DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PRJIS") || DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PRSIS")) {
                        flwBlank.add(vl.add(projectDet()));
                    }
                    if (DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PASIS")) {
                        flwBlank.add(vl.add(patientDet()));
                    }
                }
            }
        });

        txtHIssueTo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHIssueTo_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIssueTo(txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHIssueTo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIssueTo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHIssueTo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHIssueTo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIssueTo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHIssueTo_Mdl t1Row) {
                if (blnStatus) {
                }
            }
        });

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                deptCode = DmShared.xNull(txtHDepartment.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (DmShared.iNull(deptCode).longValue() != DmShared.iNull(txtHDepartment.getTag()).longValue()) {
                        gridItemDetail.getGrid().getStore().clear();
                        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                    }
                }
            }
        });

        txtHRequisition.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHRequisitionNo_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRequisitionNo(txtVoucherDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHRequisitionNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRequisition).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHRequisitionNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRequisition.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRequisition).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHRequisitionNo_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    fillRequisitionDetail(t1Row, null);
                }
            }
        });

        txtHProject.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHProject_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHProject(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHProject_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHProject).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHProject_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHProject.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHProject).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHProject_Mdl t1Row) {
                if (blnStatus) {
                    txtProjectAccount.setText(DmShared.xNull(t1Row.getProjectAcName()));
                    txtProjectAccount.setTag(DmShared.xNull(t1Row.getProjectAcCode()));

                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtHDepartment.setFocus(true);
                        }
                    });
                }
            }
        });
//        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        txtHRoomType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCustomerType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoomType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoomType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRoomType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (t1Row != null && blnStatus) {
                    if (!DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("Walk-In")) {
                        txtHRegNo.setEnabled(true);
                    } else if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("Walk-In")) {
                        txtHRegNo.setEnabled(false);
                        flwPatientSearchTd.add(flwPatientSearch);
                        txtHRegNo.setFocus(true);
                    }
                } else {
                    flwSearchRoom.clear();
                }

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (!DmShared.xNull(txtHRoomType.getTag()).trim().equalsIgnoreCase("")) {
                            if (!DmShared.xNull(txtHRoomType.getTag()).equalsIgnoreCase("Walk-In")) {
                                txtHRegNo.setFocus(true);
                            }
                        } else {
                            txtHRegNo.setEnabled(false);
                        }
                    }
                });
            }
        });
        txtHRegNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHRegNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRegNo(txtHRoomType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHRegNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRegNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHRegNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRegNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRegNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHRegNo_Mdl t1Row) {
                if (blnStatus) {
                    txtPatientName.setText(DmShared.xNull(t1Row.getPatientName()));

                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtHDepartment.setFocus(true);
                        }
                    });
                }
            }
        });
//        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++        
//opdSearch.addCallBack(new OPDSearch.CallBack() {
//
//            @Override
//            public void ok(String recIdReg, String docIdReg) {
//                
//            }
//
//            @Override
//            public void close() {
//                txtRegNo.setFocus(true);
//            }
//        });
//        roomSearchCallBack = new PatientSearchRoomWise.CallBack() {
//            @Override
//            public void ok(String regDocid, String regRecid, String roomCode) {
//                getUHIDDetail("", "", "", regDocid, null);
//            }
//
//            @Override
//            public void close() {
//                txtRegNo.setFocus(true);
//            }
//        };
//        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++        
//        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++        

    }

    private void fillRequisitionDetail(TxtHRequisitionNo_Mdl t1Row, DmCallBack callback) {
        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            if (t1Row.getDepartmentNature().equalsIgnoreCase("CM")) {
                txtHDepartment.setTag(DmShared.xNull(t1Row.getDepartmentToCode()));
                txtHDepartment.setText(DmShared.xNull(t1Row.getDepartmentToName()));
            }
        }
        txtHDepartment.setTag2nd(DmShared.xNull(t1Row.getDepartmentCode()));
        txtHIssueTo.setText(DmShared.xNull(t1Row.getRequisitionBy()));
        txtHIssueTo.setTag(DmShared.xNull(t1Row.getRequisitionByCode()));
        fillItemDetails(t1Row.getSharedKeyFieldCode(), txtHDepartment.getTag(), callback);
    }

    private void getRequisitionDetail(String reqDocid, final DmCallBack callback) {
        DmHelper dmHelper = new DmHelper();

        srvAsync.invokeRPC().getRequisitionDetail(reqDocid, txtVoucherDate.getText(), dmHelper, false, new AsyncCallback<DmResult<List<TxtHRequisitionNo_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<TxtHRequisitionNo_Mdl>> result) {
                if (result.isStatus()) {
                    txtHRequisition.setTag(result.getData().get(0).getSharedKeyFieldCode());
                    txtHRequisition.setText(result.getData().get(0).getSharedKeyFieldName());
                    fillRequisitionDetail(result.getData().get(0), callback);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillItemDetails(String reqDocid, String departmentCode, final DmCallBack callback) {
        srvAsync.invokeRPC().getItemDetail(reqDocid, topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), departmentCode, new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdItemDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        fillGridAuto(result.getData(), callback);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void fillGridAuto(List<GrdItemDetail_Mdl> grdItemDetailList, DmCallBack callback) {
        gridItemDetail.getGrid().getStore().clear();
        if (grdItemDetailList != null) {
            gridItemDetail.getGrid().getStore().addAll(grdItemDetailList);
            gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
            ((CellSelectionModel) gridItemDetail.getGrid().getSelectionModel()).selectCell(0, 0);
            if (callback != null) {
                DmResult<Object> dmrTemp = new DmResult<>();
                dmrTemp.setStatus(true);
                callback.result(dmrTemp);
            }
        }
    }

    private void gridEvents() {
        itemDetailGridEdit.addCallBack(new ItemIssueGridEdit.CallBack() {

            @Override
            public void onOk(List<GrdItemDetail_Mdl> itemLst) {
                List<GrdItemDetail_Mdl> tmpLst = new ArrayList<>();

                tmpLst.addAll(gridItemDetail.getGrid().getStore().getAll());
                tmpLst.addAll(itemLst);
                gridItemDetail.getGrid().getStore().clear();
                gridItemDetail.getGrid().getStore().addAll(tmpLst);

                int gridSize = gridItemDetail.getGrid().getStore().getAll().size();
                for (int i = 0; i < tmpLst.size(); i++) {
                    GrdItemDetail_Mdl node = tmpLst.get(i);
                    if (DmShared.xNull(node.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                        gridItemDetail.getGrid().getStore().remove(node);
                    }
                }
                gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                gridItemDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                gridItemDetail.commitChanges();
            }
        });

        gridItemDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {

                showItemIssueGridEdit(event.getRowIndex());

            }
        });

        gridItemDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showItemIssueGridEdit(gridItemDetail.getGrid().getStore().indexOf(gridItemDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
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

    private void showItemIssueGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Godown From.", txtHDepartment).show();
            return;
        }
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            itemDetailGridEdit.show(currentRowIndex, false, false, false, this, txtHVoucherType.getTag());
        } else {
            itemDetailGridEdit.show(currentRowIndex, true, true, true, this, txtHVoucherType.getTag());
        }
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Issue No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(txtHVoucherType.getTag(), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
        } else if (event.getSource() == txtRefDate) {
            txtRefDate.setText(DmClient.returnDate(txtRefDate.getText()));
            if (DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PASIS")) {
                txtHRoomType.setFocus(true);
            } else if (DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PRJIS")
                    || DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PRSIS")) {
                txtHProject.setFocus(true);
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            itemDetailGridEdit.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    private void manageEvent() {
        txtVoucherDate.addBlurHandler(this);
        txtRefDate.addBlurHandler(this);
        btnSearchRoom.addClickHandler(this);
        roomSearchEMRG.addCallBack(roomSearchCallBack);
        roomSearchIPD.addCallBack(roomSearchCallBack);
        txtPatientName.setEnabled(false);
    }

    public ItemIssue(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<ItemIssue_SrvAsync>(GWT.create(ItemIssue_Srv.class), ItemIssue.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        manageEvent();
        textHelpEvents();
        findEvents();
        gridEvents();
        manageDisplay(false);
        iniDefaultValues(swi);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC(true).getDefaultValues(topControl.getPermissions(), new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    txtVoucherDate.setText("");
                    swi.setWidget(ItemIssue.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
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

    private ItemIssue_Mdl topControlSaveInitialize() {
        ItemIssue_Mdl mrTemp = new ItemIssue_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdItemIssue().setCounter(DmShared.iNull(txtVoucherNo.getTag()).longValue());
            mrTemp.getRecIdItemIssue().setPrefix(DmShared.xNull(txtVoucherNo.getTag2nd()));
            mrTemp.getRecIdItemIssue().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setBalancingAc(DmShared.xNull(txtHVoucherType.getTag2nd()));
        mrTemp.setVoucherTypeShortName(DmShared.xNull(voucherShortName));
        mrTemp.setReceivedByTag(DmShared.xNull(txtHIssueTo.getTag()));
        mrTemp.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setProjectAcCode(DmShared.xNull(txtProjectAccount.getTag()));
        mrTemp.setProjectCode(DmShared.xNull(txtHProject.getTag()));
        mrTemp.setPatientRegDocID(DmShared.xNull(txtHRegNo.getTag()));

        mrTemp.getListItemDetail().clear();
        mrTemp.getListItemDetail().addAll(gridItemDetail.getGrid().getStore().getAll());

        mrTemp.setRefNo(DmShared.xNull(txtRefNo.getText()));
        mrTemp.setRefDate(DmShared.dNull(txtRefDate.getText()));
        mrTemp.setRemarkHeader(DmShared.xNull(txtRemark.getText()));
        mrTemp.setInventoryAccountingActive(defaultValue.isInventoryAccountingActive());

        // Set default Department Name
        defaultValue.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
        defaultValue.setDepartmentText(DmShared.xNull(txtHDepartment.getText()));

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }

        return mrTemp;
    }

    public void save() {
        if (topControl.getMode().equalsIgnoreCase("New")) {
            topControlSave();
        } else {
            showModifyReason();
        }

    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                try {
                    ItemIssue.this.modifyReasonMdl = modifyReason;
                    if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                        topControlDelete(topControl.getDocId());
                    } else {
                        topControlSave();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        ItemIssue_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ItemIssue_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, previousModel, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
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
                        topControlSaveError(result.getErrorNumber(), result.getValue1(), result.getValue2(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorSource, String errorSourceCode, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtRefDate).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHProject).show();
                break;
            case 500:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
                    List<GrdItemDetail_Mdl> listTemp = gridItemDetail.getGrid().getStore().getAll();
                    for (GrdItemDetail_Mdl node : listTemp) {
                        if (DmShared.xNull(node.getRequisitionDocId()).equalsIgnoreCase(errorSource)
                                && DmShared.xNull(node.getRequisitionV_SNo()).equalsIgnoreCase(errorSourceCode)) {
                            indexTemp = DmShared.iNull(listTemp.indexOf(node)).intValue();
                            if (errorMessage != null && !errorMessage.isEmpty()) {
                                if (Character.isDigit(errorMessage.charAt(errorMessage.length() - 1))) {
                                    errorMessage += " " + DmShared.xNull(node.getSkuName());
                                }
                                errorMessage += " in line no. " + (indexTemp + 1);
                            }
                            break;
                        }
                    }
                }
                final int gridRowIndex = indexTemp;
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) gridItemDetail.getGrid().getSelectionModel()).selectCell(gridRowIndex, 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ItemIssue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ItemIssue_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ItemIssue_Mdl> result) {
        clear();
        if (result.isStatus()) {

            previousModel = new ItemIssue_Mdl();
            result.getData().copy(previousModel);

            ItemIssue_Mdl mrTemp = result.getData();
            topControl.setDocId(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtVoucherNo.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            txtVoucherNo.setTag(DmShared.iNull(mrTemp.getRecIdItemIssue().getCounter()).toString());
            txtVoucherNo.setTag2nd(DmShared.xNull(mrTemp.getRecIdItemIssue().getPrefix()));
            txtVoucherDate.setText(DmShared.dNull(mrTemp.getVoucherDate()));
            txtHVoucherType.setText(DmShared.xNull(mrTemp.getVoucherTypeName()));
            txtHVoucherType.setTag(DmShared.xNull(mrTemp.getVoucherTypeCode()));
            txtHVoucherType.setTag2nd(DmShared.xNull(mrTemp.getBalancingAc()));
            voucherShortName = DmShared.xNull(mrTemp.getVoucherTypeShortName());
            txtHIssueTo.setTag(DmShared.xNull(mrTemp.getReceivedByTag()));
            txtHIssueTo.setText(DmShared.xNull(mrTemp.getReceivedByText()));
            txtHDepartment.setTag(DmShared.xNull(mrTemp.getDepartmentTag()));
            txtHDepartment.setText(DmShared.xNull(mrTemp.getDepartmentText()));
            txtHDepartment.setTag2nd(DmShared.xNull(mrTemp.getDepartmentToTag()));

            txtHProject.setTag(DmShared.xNull(mrTemp.getProjectCode()));
            txtHProject.setText(DmShared.xNull(mrTemp.getProjectName()));
            txtProjectAccount.setTag(DmShared.xNull(mrTemp.getProjectAcCode()));
            txtProjectAccount.setText(DmShared.xNull(mrTemp.getProjectAcName()));

            txtHRoomType.setTag(DmShared.xNull(mrTemp.getRoomSearchType()));
            txtHRoomType.setText(DmShared.xNull(mrTemp.getRoomSearchTypeName()));
            txtHRegNo.setTag(DmShared.xNull(mrTemp.getPatientRegDocID()));
            txtHRegNo.setText(DmShared.xNull(mrTemp.getPatientRegRecID()));
            txtPatientName.setText(DmShared.xNull(mrTemp.getPatientName()));

            txtHRequisition.setTag(DmShared.xNull(mrTemp.getRequisitionDocId()));
            txtHRequisition.setText(DmShared.xNull(mrTemp.getRequisitionNo()));

            txtRefDate.setText(DmShared.dNull(mrTemp.getRefDate()));
            txtRefNo.setText(DmShared.xNull(mrTemp.getRefNo()));
            txtRemark.setText(DmShared.xNull(mrTemp.getRemarkHeader()));

            userControl.setTextBlog(preparedByBlog, DmShared.xNull(mrTemp.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(mrTemp.getSharedModifiedBy()));

            gridItemDetail.getGrid().getStore().clear();
            gridItemDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());

            flwBlank.clear();
            if (DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PRJIS") || DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PRSIS")) {
                flwBlank.add(vl.add(projectDet()));
            }
            if (DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PASIS")) {
                flwBlank.add(vl.add(patientDet()));
            }
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
        txtVoucherDate.setEnabled(flag);
        if (flag) {
            if (dateEditable) {
                txtVoucherDate.setEnabled(flag);
            } else {
                txtVoucherDate.setEnabled(false);
            }
        }

        if (!departmentEditable) {
            txtHDepartment.setEnabled(false);
        }

        if (topControl.getMode().equals("Edit")) {
            txtHVoucherType.setEnabled(false);
            txtHDepartment.setEnabled(false);
        }
    }

    private void onAdd() {
        manageDisplay(true);
        clear();
        txtVoucherDate.setText(DmShared.dNull(defaultValue.getCurrentDate()));
        txtHVoucherType.setText(defaultValue.getVoucherTypeName());
        txtHVoucherType.setTag(defaultValue.getVoucherTypeCode());
        txtHVoucherType.setTag2nd(defaultValue.getBalancingAc());
        voucherShortName = defaultValue.getVoucherTypeShortName();
        txtHDepartment.setTag(defaultValue.getDepartmentTag());
        txtHDepartment.setText(defaultValue.getDepartmentText());
        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
        flwBlank.clear();
        if (DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PRJIS") || DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PRSIS")) {
            flwBlank.add(projectDet());
        }
        if (DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PASIS")) {
            flwBlank.add(patientDet());
        }
    }

    private void clear() {
        txtVoucherDate.setText("");
        txtVoucherDate.setTag("");
        txtVoucherDate.setTag2nd("");
        txtHProject.setText("");
        txtHProject.setTag("");
        txtHProject.setTag2nd("");
        txtProjectAccount.setText("");
        txtProjectAccount.setTag("");
        txtProjectAccount.setTag2nd("");
        txtHRegNo.setText("");
        txtHRegNo.setTag("");
        txtHRegNo.setTag2nd("");
        gridItemDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADD_CONSUMPTION":
                    topControl.btnClick("New");
                    getRequisitionDetail((String) object, callback);
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtRefNo.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void btnClick(Button button) {
        if (!button.isAttached()) {
            return;
        }
        if (button == btnSearchRoom) {
            if (DmShared.xNull(txtHRoomType.getTag()).equalsIgnoreCase(pvc.IPD)) {
                roomSearchIPD.show();
            } else if (DmShared.xNull(txtHRoomType.getTag()).equalsIgnoreCase(pvc.EMERGENCY)) {
                roomSearchEMRG.show();
            } else if (DmShared.xNull(txtHRoomType.getTag()).equalsIgnoreCase(pvc.OPD)) {
                opdSearch.show(new pmOPD().moduleName, SearchValue_Mdl.OPDFilterType.SERVICE_DAYS,
                        SearchValue_Mdl.OPDFilterType.OPD_ONLY, SearchValue_Mdl.OPDFilterType.ALLOW_SITE_FOR_PHARMACY);
            } else if (DmShared.xNull(txtHRoomType.getTag()).equalsIgnoreCase(pvc.DAY_CARE)) {
                opdSearch.show(new pmDayCare().moduleName, SearchValue_Mdl.OPDFilterType.CANCEL_DAYS,
                        SearchValue_Mdl.OPDFilterType.DAYCARE_ONLY, SearchValue_Mdl.OPDFilterType.ALLOW_SITE_FOR_PHARMACY);
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource() == txtHRegNo) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                txtHDepartment.setFocus(true);
            }
        }
    }
}
