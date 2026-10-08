package aarogya.master.provisionalStatement.client;

import aarogya.master.provisionalStatement.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.self.shared.DmEnum.DmMonths;
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
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;
import java.util.List;

public class ProvisionalStatement extends ProvisionalStatement_Dsg implements ClickHandler {

    private final ServiceInvoker<ProvisionalStatement_SrvAsync> srvAsync;
    private DefaultData_Mdl defaultMdl;

    public ProvisionalStatement(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
//        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        srvAsync = new ServiceInvoker<ProvisionalStatement_SrvAsync>(GWT.create(ProvisionalStatement_Srv.class), ProvisionalStatement.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        attachHandlers();
        textHelpEvents();
        findEvents();
        iniDefaultValue(swi);
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefault(new AsyncCallback<DmResult<DefaultData_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHMonth).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultData_Mdl> result) {
                if (result.isStatus()) {
                    defaultMdl = result.getData();
                    swi.setWidget(ProvisionalStatement.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHMonth).show();
                }
            }
        });
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                onAdd();

                txtHMonth.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHMonth.setEnabled(false);
                txtHSite.setEnabled(false);
                txtHMonth.setFocus(true);
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
                prnRecord();
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ProvisionalStatement.this);
            }
        });

        topControl.setDisplay(true);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void prnRecord() {

        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("month", new FieldUDT_Mdl("Month", DmShared.xNull(txtHMonth.getText()), DmShared.xNull(topControl.getDocId())));
        hashMapFilter.put("year", new FieldUDT_Mdl("Year", DmShared.xNull(txtHMonth.getTag2nd()), DmShared.xNull(topControl.getDocId())));
        hashMapFilter.put("site", new FieldUDT_Mdl("Site", DmShared.xNull(txtHSite.getText()), DmShared.xNull(topControl.getDocId())));

        new OpenSourceReport("", "Master_ProvisionalStatement", "", "Provisional Statement", new pmMaster().PROVISIONAL_STATEMENT.caption, hashMapFilter, false, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, new pmMaster().moduleName, new pmMaster().PROVISIONAL_STATEMENT.caption, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

    }

    private void textHelpEvents() {
        txtHMonth.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMonth(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMonth).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMonth.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMonth).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    private ProvisionalStatement_Mdl topControlSaveInitialize() {
        ProvisionalStatement_Mdl mrTemp = new ProvisionalStatement_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setMonth(DmMonths.getValueFrom(DmShared.iNull(txtHMonth.getTag()).intValue()));
        mrTemp.setCurrentYear(DmShared.xNull(txtHMonth.getTag2nd()));
        mrTemp.setSite(DmShared.xNull(txtHSite.getText()));
        mrTemp.setSiteCode(DmShared.xNull(txtHSite.getTag()));
        mrTemp.setVoucherTypeCode(defaultMdl.getVrType());
        mrTemp.getLstService().addAll(grdProvStatmnt.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        ProvisionalStatement_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ProvisionalStatement_Vld.chkSave(mrTemp);
            String[] dateRange = defaultMdl.getHmMonths().get(DmMonths.getValueFrom(DmShared.iNull(txtHMonth.getTag()).intValue()));
            srvAsync.invokeRPC().saveRecord(mrTemp, dateRange, DmShared.iNull(txtHSite.getTag()).toString(), DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHMonth).show();
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
                new DmAlertMessageBox(errorMessage, txtHMonth).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHSite).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHMonth).show();
                break;
        }
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ProvisionalStatement_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ProvisionalStatement_Mdl> result) {
                topControlMoveRecSuccess(result);

            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ProvisionalStatement_Mdl> result) {
        ProvisionalStatement_Mdl data;

        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            if (data.getMonth() != null) {
                txtHMonth.setText(data.getMonth().getMonthName());
                txtHMonth.setTag(DmShared.iNull(data.getMonth().getMonthCalendarValue()).toString());
            }
            txtHMonth.setTag2nd(data.getCurrentYear());
            txtHSite.setText(data.getSite());
            txtHSite.setTag(data.getSiteCode());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            grdProvStatmnt.getGrid().getStore().clear();
            grdProvStatmnt.getGrid().getStore().addAll(data.getLstService());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
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

    private void getServiceDetail() {
        String[] dateRange = defaultMdl.getHmMonths().get(DmMonths.getValueFrom(DmShared.iNull(txtHMonth.getTag()).intValue()));
        srvAsync.invokeRPC().getServiceDetail(dateRange, txtHSite.getTag(), defaultMdl.getServiceCategoryCSV(), new AsyncCallback<DmResult<List<GrdProvisionalStatement_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdProvisionalStatement_Mdl>> result) {
                if (result.isStatus()) {
                    grdProvStatmnt.getGrid().getStore().clear();
                    grdProvStatmnt.getGrid().getStore().addAll(result.getData());
                    txtHMonth.setTag2nd(result.getValue1());
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {
        grdProvStatmnt.getGrid().getStore().clear();
        txtHSite.setTag(defaultMdl.getCurrentSiteCode());
        txtHSite.setText(defaultMdl.getCurrentSiteName());
    }

    private void clear() {

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        grdProvStatmnt.getGrid().getStore().clear();

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnGenerate)) {
            if (topControl.getMode().equalsIgnoreCase("New")) {
                if (DmMonths.getValueFrom(DmShared.iNull(txtHMonth.getTag()).intValue()) == null
                        || DmShared.iNull(txtHSite.getTag()).intValue() == 0) {
                    new DmAlertMessageBox("Please select Month and Site.", txtHMonth).show();
                } else {
                    getServiceDetail();
                }
            }

        }
    }

    private void attachHandlers() {
        btnGenerate.addClickHandler(this);
    }

}
