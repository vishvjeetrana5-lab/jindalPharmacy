package aarogya.master.categoryPoolMaster.client;

import aarogya.master.categoryPoolMaster.shared.Find_Mdl;
import aarogya.master.categoryPoolMaster.shared.CategoryPoolMaster_Mdl;
import aarogya.master.categoryPoolMaster.shared.CategoryPoolMaster_Vld;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;
import aarogya.master.categoryPoolMaster.shared.CategoryPoolMaster_Srv;
import aarogya.master.categoryPoolMaster.shared.CategoryPoolMaster_SrvAsync;

public final class CategoryPoolMaster extends CategoryPoolMaster_Dsg {

    ServiceInvoker<CategoryPoolMaster_SrvAsync> SrvAsync;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clearBlogs();
                txtHCategory.setFocus(true);

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHCategory.setFocus(true);
                manageDisplay();

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
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(CategoryPoolMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

        txtHCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCategory).show();
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
                manageCategoryDisplay();
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
                manageGroupDisplay();
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
                manageServiceDisplay();
            }
        });
        txtHHeaderConsType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHeaderConsType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHeaderConsType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHeaderConsType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHHeaderRefType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHeaderRefType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHeaderRefType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHeaderRefType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHLineConsType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLineConsType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLineConsType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLineConsType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHLineRefType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLineRefType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLineRefType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLineRefType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    public CategoryPoolMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<CategoryPoolMaster_SrvAsync>(GWT.create(CategoryPoolMaster_Srv.class), CategoryPoolMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
//        manageDisplay(false);
        textHelpEvents();
        findEvents();
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private CategoryPoolMaster_Mdl topControlSaveInitialize() {
        CategoryPoolMaster_Mdl mrTemp = new CategoryPoolMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHCategory.getTag2nd()));
        mrTemp.setServiceCatCode(DmShared.xNull(txtHServiceCat.getTag()));
        mrTemp.setServiceCatName(DmShared.xNull(txtHServiceCat.getText()));
        mrTemp.setCategoryTag(DmShared.xNull(txtHCategory.getTag()));
        mrTemp.setCategoryText(DmShared.xNull(txtHCategory.getText()));
//        mrTemp.setAmount(DmShared.xNull(txtAmount.getText()));
        mrTemp.setModuleName(DmShared.xNull(txtHModule.getTag()));
        mrTemp.setGroupCode(DmShared.xNull(txtHServiceGroup.getTag()));
        mrTemp.setServiceCode(DmShared.xNull(txtHService.getTag()));
        mrTemp.setHeaderConTypeCode(DmShared.xNull(txtHHeaderConsType.getTag()));
        mrTemp.setHeaderRefTypeCode(DmShared.xNull(txtHHeaderRefType.getTag()));
        mrTemp.setLineConTypeCode(DmShared.xNull(txtHLineConsType.getTag()));
        mrTemp.setLineRefTypeCode(DmShared.xNull(txtHLineRefType.getTag()));
        mrTemp.setHeaderConAmount(DmShared.xNull(txtHeaderConsAmount.getText()));
        mrTemp.setHeaderRefAmount(DmShared.xNull(txtHeaderRefAmount.getText()));
        mrTemp.setLineConAmount(DmShared.xNull(txtLineConsAmount.getText()));
        mrTemp.setLineRefAmount(DmShared.xNull(txtLineRefAmount.getText()));
        mrTemp.setOrgCode(DmShared.xNull(txtHEmpanelment.getTag()));
        return mrTemp;
    }

    private void topControlSave() {
        CategoryPoolMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            CategoryPoolMaster_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHServiceCat).show();
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
//            case 100:
//                new DmAlertMessageBox(errorMessage, txtHServiceCat).show();
//                break;
//            case 200:
//                new DmAlertMessageBox(errorMessage, txtHCategory).show();
//                break;
//            case 300:
//                new DmAlertMessageBox(errorMessage, txtHEmpanelment).show();
//                break;
            case 100:
                new DmAlertMessageBox(errorMessage, txtHModule).show();
                break;
//            case 500:
//                new DmAlertMessageBox(errorMessage, txtHServiceGroup).show();
//                break;
//            case 600:
//                new DmAlertMessageBox(errorMessage, txtHService).show();
//                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHHeaderConsType).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHHeaderRefType).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHLineConsType).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHLineRefType).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHServiceCat).show();
                break;
        }

    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<CategoryPoolMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<CategoryPoolMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<CategoryPoolMaster_Mdl> result) {
        CategoryPoolMaster_Mdl data;

        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHServiceCat.setTag(data.getServiceCatCode());
            txtHServiceCat.setText(data.getServiceCatName());
            txtHCategory.setTag(data.getCategoryTag());
            txtHCategory.setText(data.getCategoryText());
            txtHEmpanelment.setTag(DmShared.xNull(data.getOrgCode()));
            txtHEmpanelment.setText(DmShared.xNull(data.getOrgName()));
            txtHModule.setTag(DmShared.xNull(data.getModuleName()));
            txtHModule.setText(DmShared.xNull(data.getModuleName()));
            txtHServiceGroup.setTag(DmShared.xNull(data.getGroupCode()));
            txtHServiceGroup.setText(DmShared.xNull(data.getGroupName()));
            txtHService.setTag(DmShared.xNull(data.getServiceCode()));
            txtHService.setText(DmShared.xNull(data.getServiceName()));
            txtHHeaderConsType.setTag(DmShared.xNull(data.getHeaderConTypeCode()));
            txtHHeaderConsType.setText(DmShared.xNull(data.getHeaderConTypeName()));
            txtHHeaderRefType.setTag(DmShared.xNull(data.getHeaderRefTypeCode()));
            txtHHeaderRefType.setText(DmShared.xNull(data.getHeaderRefTypeName()));
            txtHLineConsType.setTag(DmShared.xNull(data.getLineConTypeCode()));
            txtHLineConsType.setText(DmShared.xNull(data.getLineConTypeName()));
            txtHLineRefType.setTag(DmShared.xNull(data.getLineRefTypeCode()));
            txtHLineRefType.setText(DmShared.xNull(data.getLineRefTypeName()));
            txtHeaderRefAmount.setText(DmClient.vNull(data.getHeaderRefAmount(), "0.00"));
            txtHeaderConsAmount.setText(DmClient.vNull(data.getHeaderConAmount(), "0.00"));
            txtLineRefAmount.setText(DmClient.vNull(data.getLineRefAmount(), "0.00"));
            txtLineConsAmount.setText(DmClient.vNull(data.getLineConAmount(), "0.00"));
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
//        manageDisplay(false);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clearBlogs();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    public void clearBlogs() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");

    }

    private void manageServiceDisplay() {
        if (DmShared.iNull(txtHService.getTag()).longValue() != 0) {
            txtHServiceCat.setTag("");
            txtHServiceCat.setText("");
            txtHServiceCat.setEnabled(false);
            txtHServiceGroup.setTag("");
            txtHServiceGroup.setText("");
            txtHServiceGroup.setEnabled(false);
            txtHService.setEnabled(true);
        } else {
            txtHServiceCat.setEnabled(true);
            txtHServiceGroup.setEnabled(true);
        }
    }

    private void manageCategoryDisplay() {
        if (DmShared.iNull(txtHServiceCat.getTag()).longValue() != 0) {
            txtHService.setTag("");
            txtHService.setText("");
            txtHService.setEnabled(false);
            txtHServiceGroup.setTag("");
            txtHServiceGroup.setText("");
            txtHServiceGroup.setEnabled(false);
            txtHServiceCat.setEnabled(true);
        } else {
            txtHService.setEnabled(true);
            txtHServiceGroup.setEnabled(true);
        }
    }

    private void manageGroupDisplay() {
        if (DmShared.iNull(txtHServiceGroup.getTag()).longValue() != 0) {
            txtHService.setTag("");
            txtHService.setText("");
            txtHService.setEnabled(false);
            txtHServiceCat.setTag("");
            txtHServiceCat.setText("");
            txtHServiceCat.setEnabled(false);
            txtHServiceGroup.setEnabled(true);
        } else {
            txtHService.setEnabled(true);
            txtHServiceCat.setEnabled(true);
        }
    }

    private void manageDisplay() {
//        txtHEnquiryType.setEnabled(flag);
//        if (!flag) {
//            btnAddInd.removeFromParent();
//        } else {
//            flwIndentBtn.clear();
//            flwIndentBtn.add(btnAddInd);
//        }
//
//        txtEnquiryNo.setEnabled(false);
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            if (DmShared.iNull(txtHService.getTag()).longValue() != 0) {
                manageServiceDisplay();
            } else if (DmShared.iNull(txtHServiceCat.getTag()).longValue() != 0) {
                manageCategoryDisplay();
            } else if (DmShared.iNull(txtHServiceGroup.getTag()).longValue() != 0) {
                manageGroupDisplay();
            } else {
                txtHService.setEnabled(true);
                txtHServiceCat.setEnabled(true);
                txtHServiceGroup.setEnabled(true);
            }
        }
    }

}
