package aarogya.inventory.itemReceive.client;

import aarogya.inventory.itemReceive.shared.ItemReceive_Vld;
import aarogya.inventory.itemReceive.shared.ItemReceive_Mdl;
import aarogya.inventory.itemReceive.shared.DefaultValue_Mdl;
import aarogya.inventory.itemReceive.shared.TxtHReceivedFrom_Mdl;
import aarogya.inventory.itemReceive.shared.Find_Mdl;
import aarogya.inventory.itemReceive.shared.GrdItemDetail_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
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
import aarogya.inventory.itemReceive.shared.ItemReceive_Srv;
import aarogya.inventory.itemReceive.shared.ItemReceive_SrvAsync;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;

public final class ItemReceive extends ItemReceive_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents {

    private final ServiceInvoker<ItemReceive_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    final ItemReceiveGridEdit<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> itemDetailGridEdit = new ItemReceiveGridEdit(gridItemDetail);
    private String deptCode = "";
    boolean departmentEditable = false, dateEditable = false;
    ItemReceive_Mdl previousModel = null;
    final ModifyReason modifyReason = new ModifyReason();
    private ModifyReason_Mdl modifyReasonMdl;

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

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().ITEM_RECEIVE})) {
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
                txtHReceivedFrom.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(true);
                gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
                txtHReceivedFrom.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ItemReceive.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

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
                if (blnStatus && t1Row != null) {
                    txtHVoucherType.setTag2nd(t1Row.getSharedPreparedBy());
                }
            }
        });

        txtHReceivedFrom.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHReceivedFrom_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHReceivedFrom(txtHVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHReceivedFrom_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReceivedFrom).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHReceivedFrom_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHReceivedFrom.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReceivedFrom).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHReceivedFrom_Mdl t1Row) {
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

    }

    private void gridEvents() {
        itemDetailGridEdit.addCallBack(new ItemReceiveGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemDetail_Mdl opdService) {
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

                showItemReceiveGridEdit(event.getRowIndex());

            }
        });

        gridItemDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showItemReceiveGridEdit(gridItemDetail.getGrid().getStore().indexOf(gridItemDetail.getGrid().getSelectionModel().getSelectedItem()));
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

    private void showItemReceiveGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please select Godown In.", txtHDepartment).show();
            return;
        }
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            itemDetailGridEdit.show(currentRowIndex, false, false, false, this,txtHVoucherType.getTag());
        } else {
            itemDetailGridEdit.show(currentRowIndex, true, true, true, this,txtHVoucherType.getTag());
        }
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Receive No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
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
    }

    public ItemReceive(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<ItemReceive_SrvAsync>(GWT.create(ItemReceive_Srv.class), ItemReceive.class.getName());
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
        srvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    txtVoucherDate.setText("");
                    swi.setWidget(ItemReceive.this);
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

    private ItemReceive_Mdl topControlSaveInitialize() {
        ItemReceive_Mdl mrTemp = new ItemReceive_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdItemReceive().setCounter(DmShared.iNull(txtVoucherNo.getTag()).longValue());
            mrTemp.getRecIdItemReceive().setPrefix(DmShared.xNull(txtVoucherNo.getTag2nd()));
            mrTemp.getRecIdItemReceive().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setBalancingAc(DmShared.xNull(txtHVoucherType.getTag2nd()));
        mrTemp.setReceivedByTag(DmShared.xNull(txtHReceivedFrom.getTag()));
        mrTemp.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setRefNo(DmShared.xNull(txtRefNo.getText()));
        mrTemp.setRefDate(DmShared.dNull(txtRefDate.getText()));
        mrTemp.setRemarkHeader(DmShared.xNull(txtRemark.getText()));
        mrTemp.setInventoryAccountingActive(defaultValue.isInventoryAccountingActive());
        
        mrTemp.getListItemDetail().clear();
        mrTemp.getListItemDetail().addAll(gridItemDetail.getGrid().getStore().getAll());

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
                    ItemReceive.this.modifyReasonMdl = modifyReason;
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
        ItemReceive_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ItemReceive_Vld.chkSave(mrTemp);

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
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        topControlSaveError(errorNo, "", errorMessage);
    }

    private void topControlSaveError(int errorNo, final String gridRowIndex, String errorMessage) {
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
            case 500:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) gridItemDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHReceivedFrom).show();
                break;
        }
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ItemReceive_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ItemReceive_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ItemReceive_Mdl> result) {
        clear();
        if (result.isStatus()) {
            ItemReceive_Mdl mrTemp = result.getData();
            previousModel = new ItemReceive_Mdl(mrTemp);
            topControl.setDocId(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtVoucherNo.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            txtVoucherNo.setTag(DmShared.iNull(mrTemp.getRecIdItemReceive().getCounter()).toString());
            txtVoucherNo.setTag2nd(DmShared.xNull(mrTemp.getRecIdItemReceive().getPrefix()));
            txtVoucherDate.setText(DmShared.dNull(mrTemp.getVoucherDate()));
            txtHVoucherType.setText(DmShared.xNull(mrTemp.getVoucherTypeName()));
            txtHVoucherType.setTag(DmShared.xNull(mrTemp.getVoucherTypeCode()));
            txtHVoucherType.setTag2nd(DmShared.xNull(mrTemp.getBalancingAc()));
            txtHReceivedFrom.setTag(DmShared.xNull(mrTemp.getReceivedByTag()));
            txtHReceivedFrom.setText(DmShared.xNull(mrTemp.getReceivedByText()));
            txtHDepartment.setTag(DmShared.xNull(mrTemp.getDepartmentTag()));
            txtHDepartment.setText(DmShared.xNull(mrTemp.getDepartmentText()));

            //Ref Challan
            txtRefDate.setText(DmShared.dNull(mrTemp.getRefDate()));
            txtRefNo.setText(DmShared.xNull(mrTemp.getRefNo()));
            txtRemark.setText(DmShared.xNull(mrTemp.getRemarkHeader()));

            userControl.setTextBlog(preparedByBlog, DmShared.xNull(mrTemp.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(mrTemp.getSharedModifiedBy()));
            gridItemDetail.getGrid().getStore().clear();
            gridItemDetail.getGrid().getStore().addAll(mrTemp.getListItemDetail());
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
        txtHDepartment.setTag(defaultValue.getDepartmentTag());
        txtHDepartment.setText(defaultValue.getDepartmentText());
        gridItemDetail.getGrid().getStore().add(new GrdItemDetail_Mdl());
    }

    private void clear() {
        previousModel = null;
        txtVoucherDate.setText("");
        txtVoucherDate.setTag("");
        txtVoucherDate.setTag2nd("");
        gridItemDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

}
