package dataman.webJobs.scheduler.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.webJobs.scheduler.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;

public final class Scheduler extends Scheduler_Dsg implements ClickHandler, BlurHandler {

    private final ServiceInvoker<Scheduler_SrvAsync> SrvAsync;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
                manageDisplay(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                onEdit();
                manageDisplay(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(Scheduler.this);
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {

            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtSchedulerName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtSchedulerName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtSchedulerName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtSchedulerName).show();
                        }
                    }
                });
            }
        });

        txtHPLT.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<DataBase_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPLT(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<DataBase_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPLT).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<DataBase_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPLT.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPLT).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, DataBase_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtDBLookUp.setText(DmShared.xNull(t1Row.getDatabasename()));
                } else {
                    txtDBLookUp.setText("");
                    txtHTemplate.setTag("");
                    txtHTemplate.setText("");
                }
            }
        });

        txtHSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPLTSite(txtHPLT.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHRespUser.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHResponsibleUser(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRespUser).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRespUser.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRespUser).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHJobType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                SrvAsync.invokeRPC(blnCallBackWait).getTxtHJobType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHJobType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHJobType.setData(result.getData(), result.getPaging());
                            txtHTemplate.setEnabled(true);
                            txtHTemplate.setTag("");
                            txtHTemplate.setText("");
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHJobType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHTemplate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                if (DmShared.xNull(txtDBLookUp.getText()).equalsIgnoreCase("") || DmShared.xNull(txtHJobType.getText()).equalsIgnoreCase("")) {
                } else {
                    SrvAsync.invokeRPC(blnCallBackWait).getTxtHTemplate(txtDBLookUp.getText(), txtHJobType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                        @Override

                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHTemplate).show();
                        }

                        @Override
                        public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                            if (result.isStatus()) {
                                txtHTemplate.setData(result.getData(), result.getPaging());
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), txtHTemplate).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHActive.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });

        txtHActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHActive).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHActive.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHActive).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHMonthsName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMonthsName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMonthsName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMonthsName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMonthsName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHMonthDate.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMonthsNumber(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMonthDate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMonthDate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMonthDate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHMonthsOnWeek.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMonthsWeekNumber(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMonthsOnWeek).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMonthsOnWeek.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMonthsOnWeek).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHMonthsOnDays.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMonthsWeekName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMonthsOnDays).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMonthsOnDays.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMonthsOnDays).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

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

    public Scheduler(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(Scheduler_Srv.class), Scheduler.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        radioOnes.setValue(Boolean.TRUE);
        txtDBLookUp.setEnabled(false);
        textHelpEvents();
        findEvents();
        handlerEvents();
        manageExpireDiv(false);
        manageDisplay(false);
        swi.setWidget(this);
    }

    private Scheduler_Mdl topControlSaveInitialize() {

        Scheduler_Mdl mrTemp = new Scheduler_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtSchedulerName.getText()));
        mrTemp.setPltCode(DmShared.xNull(txtHPLT.getTag()));
        mrTemp.setDbLookUp(DmShared.xNull(txtDBLookUp.getText()));
        mrTemp.setSiteCode(DmShared.xNull(txtHSite.getTag()));
        mrTemp.setJobTypeCode(DmShared.xNull(txtHJobType.getTag()));
        mrTemp.setTemplateCode(DmShared.xNull(txtHTemplate.getTag()));
        mrTemp.setTemplateName(DmShared.xNull(txtHTemplate.getText()));

        mrTemp.setIsActiveCode(DmClient.vNull(txtHActive.getTag(), "0"));
        mrTemp.setResponsibleUser(DmShared.xNull(txtHRespUser.getText()));

        mrTemp.setStartDate(DmShared.dNull(txtStartDate.getText()));
        mrTemp.setStartTime(DmShared.xNull((txtStartTime.getText())));

        if (radioOnes.getValue()) {
            mrTemp.setRbTaskScheduler("1");
        } else if (radioDaily.getValue()) {
            mrTemp.setRbTaskScheduler("2");
            mrTemp.setRecurrenceQty(DmClient.vNull(txtRepeatDay.getText(), "0"));
            mrTemp.setRecurrenceUnit(String.valueOf(DmEnum.DmRecurrenceUnit.DAYS.getUnitValue()));
            mrTemp.setExpiryDate(DmShared.dNull(txtExpiryDate.getText()));
            mrTemp.setExpiryTime(DmShared.xNull(txtExpiryTime.getText()));
        } else if (radioWeekly.getValue()) {
            mrTemp.setRbTaskScheduler("3");
//            mrTemp.setRepeatWeek(DmClient.vNull(txtRepeatWeek.getText(), "0"));
            mrTemp.setRecurrenceQty(DmClient.vNull(txtRepeatWeek.getText(), "0"));
            mrTemp.setRecurrenceUnit(String.valueOf(DmEnum.DmRecurrenceUnit.WEEKS.getUnitValue()));
            mrTemp.setExpiryDate(DmShared.dNull(txtExpiryDate.getText()));
            mrTemp.setExpiryTime(DmShared.xNull(txtExpiryTime.getText()));

            String days = "";
            if (cbSunday.isChecked()) {
                days += "1|";
            }
            if (cbMonday.isChecked()) {
                days += "2|";
            }
            if (cbTuesday.isChecked()) {
                days += "3|";
            }
            if (cbWednesday.isChecked()) {
                days += "4|";
            }
            if (cbThursday.isChecked()) {
                days += "5|";
            }
            if (cbFriday.isChecked()) {
                days += "6|";
            }
            if (cbSaturday.isChecked()) {
                days += "7|";
            }
            mrTemp.setCbDays(DmShared.xNull(removeLastChar(days)));
        } else if (radioMonthly.getValue()) {
            mrTemp.setRbTaskScheduler("4");
            mrTemp.setExpiryDate(DmShared.dNull(txtExpiryDate.getText()));
            mrTemp.setExpiryTime(DmShared.xNull(txtExpiryTime.getText()));

            mrTemp.setRecurrenceQty("0");
            mrTemp.setRecurrenceUnit(String.valueOf(DmEnum.DmRecurrenceUnit.MONTHS.getUnitValue()));

            mrTemp.setMonthsCode(DmShared.xNull(txtHMonthsName.getTag()));
            if (radioMonthsDay.getValue()) {
                mrTemp.setRbDayOrMonth("d");
                mrTemp.setDateCode(DmShared.xNull(txtHMonthDate.getTag()));
            } else if (radioMonthsOn.getValue()) {
                mrTemp.setRbDayOrMonth("m");
                mrTemp.setWeekCode(DmShared.xNull(txtHMonthsOnWeek.getTag()));
                mrTemp.setDaysCode(DmShared.xNull(txtHMonthsOnDays.getTag()));
            }
        }

        return mrTemp;
    }

    private String removeLastChar(String str) {
        if (str.length() >= 1) {
            return str.substring(0, str.length() - 1);
        } else {
            return "";
        }
    }

    private void topControlSave() {
        Scheduler_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            Scheduler_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtSchedulerName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");

                        } else {
                            reset();
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
                new DmAlertMessageBox(errorMessage, txtSchedulerName).show();
                break;
            case 150:
                new DmAlertMessageBox(errorMessage, txtHPLT).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHJobType).show();
                break;
            case 250:
                new DmAlertMessageBox(errorMessage, txtHTemplate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHRespUser).show();
                break;
            case 350:
                new DmAlertMessageBox(errorMessage, txtHActive).show();
                break;
            case 450:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtStartDate).show();
                break;
            case 500:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtStartTime).show();
                break;
            case 550:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtRepeatDay).show();
                break;
            case 600:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtRepeatWeek).show();
                break;
            case 700:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtHMonthsName).show();
                break;
            case 800:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtHMonthDate).show();
                break;
            case 850:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtHMonthsOnWeek).show();
                break;
            case 900:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtHMonthsOnDays).show();
                break;
            case 1000:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtExpiryDate).show();
                break;
            case 1100:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtExpiryTime).show();
                break;
            default:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtSchedulerName).show();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<Scheduler_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Scheduler_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<Scheduler_Mdl> result) {
        Scheduler_Mdl data;
        if (result.isStatus()) {
            reset();
            manageDisplay(false);
            tabPanel.selectTab(0);
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtSchedulerName.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtHPLT.setTag(DmShared.xNull(data.getPltCode()));
            txtHPLT.setText(DmShared.xNull(data.getPltName()));
            txtHSite.setTag(DmShared.xNull(data.getSiteCode()));
            txtHSite.setText(DmShared.xNull(data.getSiteName()));
            txtDBLookUp.setText(DmShared.xNull(data.getDbLookUp()));
            txtHJobType.setTag(DmShared.xNull(data.getJobTypeCode()));
            txtHJobType.setText(DmShared.xNull(data.getJobTypeName()));
            txtHTemplate.setTag(DmShared.xNull(data.getTemplateCode()));
            txtHTemplate.setText(DmShared.xNull(data.getTemplateName()));
            txtHActive.setTag(DmClient.vNull(data.getIsActiveCode(), "0"));
            txtHRespUser.setTag(DmShared.xNull(data.getResponsibleUser()));
            txtHRespUser.setText(DmShared.xNull(data.getResponsibleUser()));
            txtHActive.setText(DmShared.xNull(data.getIsActiveName()));
            txtStartDate.setText(DmShared.dNull(data.getStartDate()));
            txtStartTime.setText(DmShared.xNull(data.getStartTime()));
            txtExpiryDate.setText(DmShared.dNull(data.getExpiryDate()));
            txtExpiryTime.setText(DmShared.xNull(data.getExpiryTime()));
            if (DmShared.iNull(data.getRecurrenceUnit()).intValue() == DmEnum.DmRecurrenceUnit.DAYS.getUnitValue()) {
                txtRepeatDay.setText(DmShared.xNull(data.getRecurrenceQty()));
            } else if (DmShared.iNull(data.getRecurrenceUnit()).intValue() == DmEnum.DmRecurrenceUnit.WEEKS.getUnitValue()) {
                txtRepeatWeek.setText(DmShared.xNull(data.getRecurrenceQty()));
            }
            txtHMonthsName.setTag(DmShared.xNull(data.getMonthsCode()));
            setMonthsName(DmShared.xNull(data.getMonthsCode()));
            txtHMonthDate.setTag(DmShared.xNull(data.getDateCode()));
            txtHMonthDate.setText(DmShared.xNull(data.getDateCode()).replace("-1", "Last"));
            txtHMonthsOnWeek.setTag(DmShared.xNull(data.getWeekCode()));
            setWeekName(DmShared.xNull(data.getWeekCode()));
            txtHMonthsOnDays.setTag(DmShared.xNull(data.getDaysCode()));
            setDaysCheckBox(DmShared.xNull(data.getCbDays()));
            setDaysName(DmShared.xNull(data.getDaysCode()));
            if (DmShared.xNull(data.getRbTaskScheduler()).equalsIgnoreCase("1")) {
                manageFlwRBtnDisplay(1, true);
                radioOnes.setValue(Boolean.TRUE);
            } else if (DmShared.xNull(data.getRbTaskScheduler()).equalsIgnoreCase("2")) {
                manageFlwRBtnDisplay(2, true);
                radioDaily.setValue(Boolean.TRUE);
            } else if (DmShared.xNull(data.getRbTaskScheduler()).equalsIgnoreCase("3")) {
                manageFlwRBtnDisplay(3, true);
                radioWeekly.setValue(Boolean.TRUE);
            } else if (DmShared.xNull(data.getRbTaskScheduler()).equalsIgnoreCase("4")) {
                manageFlwRBtnDisplay(4, true);
                radioMonthly.setValue(Boolean.TRUE);
                if (DmShared.xNull(data.getRbDayOrMonth()).equalsIgnoreCase("d")) {
                    mangageRBtnMonth(1);
                    radioMonthsDay.setValue(Boolean.TRUE);
                    txtHMonthsOnDays.setEnabled(false);
                    txtHMonthsOnWeek.setEnabled(false);
                } else if (DmShared.xNull(data.getRbDayOrMonth()).equalsIgnoreCase("m")) {
                    mangageRBtnMonth(2);
                    radioMonthsOn.setValue(Boolean.TRUE);
                    txtHMonthDate.setEnabled(false);
                }
            }
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
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
                    clear();
                    reset();
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

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void manageFlwRBtnDisplay(int rBtnSelection, boolean isDisplay) {
        flwRBSchChanger.clear();
        flwExpiry.clear();
        switch (rBtnSelection) {
            case 1:
                manageExpireDiv(false);
                radioOnes.setValue(Boolean.TRUE);
                txtStartDate.setFocus(true);
                break;
            case 2:
                manageExpireDiv(true);
                flwRBSchChanger.add(flwDaily());
                radioDaily.setValue(Boolean.TRUE);
                txtRepeatDay.setFocus(true);
                break;
            case 3:
                manageExpireDiv(true);
                flwRBSchChanger.add(flwWeekly());
                txtRepeatWeek.setFocus(true);
                break;
            case 4:
                manageExpireDiv(true);
                flwRBSchChanger.add(flwMonthly());
                if (isDisplay) {
                    manageMonthsRadio(radioMonthsDay);
                }
                txtHMonthsName.setFocus(true);
                break;
            default:
                manageExpireDiv(false);
                txtStartDate.setFocus(true);
                break;
        }
    }

    private void mangageRBtnMonth(int selection) {
        switch (selection) {
            case 1:
                manageMonthsRadio(radioMonthsDay);
                break;
            case 2:
                manageMonthsRadio(radioMonthsOn);
                break;
            default:
                manageMonthsRadio(radioMonthsDay);
                break;
        }
    }

    private void manageMonthsRadio(RadioButton rbtn) {
        if (rbtn.equals(radioMonthsDay)) {
            radioMonthsDay.setValue(Boolean.TRUE);
            txtHMonthDate.setEnabled(true);
            txtHMonthDate.setFocus(true);
            txtHMonthsOnDays.setEnabled(false);
            txtHMonthsOnWeek.setEnabled(false);
        } else if (rbtn.equals(radioMonthsOn)) {
            radioMonthsOn.setValue(Boolean.TRUE);
            txtHMonthDate.setEnabled(false);
            txtHMonthsOnDays.setEnabled(true);
            txtHMonthsOnWeek.setEnabled(true);
            txtHMonthsOnWeek.setFocus(true);
        }
    }

    private void handlerEvents() {
        radioOnes.addClickHandler(this);
        radioDaily.addClickHandler(this);
        radioWeekly.addClickHandler(this);
        radioMonthly.addClickHandler(this);
        radioMonthsDay.addClickHandler(this);
        radioMonthsOn.addClickHandler(this);
        txtStartDate.addBlurHandler(this);
        txtExpiryDate.addBlurHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        if ((topControl.getMode().equalsIgnoreCase("New")) || (topControl.getMode().equalsIgnoreCase("Edit"))) {
            if (event.getSource().equals(radioOnes)) {
                manageFlwRBtnDisplay(1, true);
            } else if (event.getSource().equals(radioDaily)) {
                manageFlwRBtnDisplay(2, true);
            } else if (event.getSource().equals(radioWeekly)) {
                manageFlwRBtnDisplay(3, true);
            } else if (event.getSource().equals(radioMonthly)) {
                manageFlwRBtnDisplay(4, true);
            } else if (event.getSource().equals(radioMonthsDay)) {
                mangageRBtnMonth(1);
            } else if (event.getSource().equals(radioMonthsOn)) {
                mangageRBtnMonth(2);
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtStartDate)) {
            if (!DmShared.xNull(txtStartDate.getText()).trim().equalsIgnoreCase("")) {
                txtStartDate.setText(DmClient.returnDate(txtStartDate.getText()));
            }
        } else if (event.getSource().equals(txtExpiryDate)) {
            if (!DmShared.xNull(txtExpiryDate.getText()).trim().equalsIgnoreCase("")) {
                txtExpiryDate.setText(DmClient.returnDate(txtExpiryDate.getText()));
            }
        }
    }

    private void reset() {
        radioDaily.setValue(Boolean.FALSE);
        radioMonthly.setValue(Boolean.FALSE);
        radioMonthsDay.setValue(Boolean.FALSE);
        radioMonthsOn.setValue(Boolean.FALSE);
        radioOnes.setValue(Boolean.FALSE);
        radioWeekly.setValue(Boolean.FALSE);
        cbFriday.setValue(Boolean.FALSE);
        cbMonday.setValue(Boolean.FALSE);
        cbSunday.setValue(Boolean.FALSE);
        cbThursday.setValue(Boolean.FALSE);
        cbTuesday.setValue(Boolean.FALSE);
        cbWednesday.setValue(Boolean.FALSE);
        cbSaturday.setValue(Boolean.FALSE);
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        tabPanel.selectTab(0);
    }

    private void onAdd() {
        clear();
        manageFlwRBtnDisplay(1, true);
        txtDBLookUp.setEnabled(false);
        txtHActive.setTag(DmShared.xNull("1"));
        txtHActive.setText(DmShared.xNull("Yes"));
        txtSchedulerName.setFocus(true);
    }

    private void manageExpireDiv(boolean flag) {
        flwExpiry.clear();
        if (flag == true) {
            flwExpiry.add(expiryDiv());
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                txtExpiryDate.setEnabled(false);
                txtExpiryTime.setEnabled(false);
            } else {
                txtExpiryDate.setEnabled(true);
                txtExpiryTime.setEnabled(true);
            }
        }
    }

    private void setDaysCheckBox(String weekCode) {
        if (weekCode.matches("(.*)1(.*)")) {
            cbSunday.setValue(true);
        }
        if (weekCode.matches("(.*)2(.*)")) {
            cbMonday.setValue(true);
        }
        if (weekCode.matches("(.*)3(.*)")) {
            cbTuesday.setValue(true);
        }
        if (weekCode.matches("(.*)4(.*)")) {
            cbWednesday.setValue(true);
        }
        if (weekCode.matches("(.*)5(.*)")) {
            cbThursday.setValue(true);
        }
        if (weekCode.matches("(.*)6(.*)")) {
            cbFriday.setValue(true);
        }
        if (weekCode.matches("(.*)7(.*)")) {
            cbSaturday.setValue(true);
        }
    }

    private void setMonthsName(String monthCode) {
        String name = "";
        if (monthCode.matches("(.*)1(.*)")) {
            name = name + String.valueOf(DmMonths.JANUARY.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)2(.*)")) {
            name = name + String.valueOf(DmMonths.FEBRUARY.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)3(.*)")) {
            name = name + String.valueOf(DmMonths.MARCH.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)4(.*)")) {
            name = name + String.valueOf(DmMonths.APRIL.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)5(.*)")) {
            name = name + String.valueOf(DmMonths.MAY.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)6(.*)")) {
            name = name + String.valueOf(DmMonths.JUNE.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)7(.*)")) {
            name = name + String.valueOf(DmMonths.JULY.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)8(.*)")) {
            name = name + String.valueOf(DmMonths.AUGUST.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)9(.*)")) {
            name = name + String.valueOf(DmMonths.SEPTEMBER.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)10(.*)")) {
            name = name + String.valueOf(DmMonths.OCTOBER.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)11(.*)")) {
            name = name + String.valueOf(DmMonths.NOVEMBER.getMonthName()) + ",";
        }
        if (monthCode.matches("(.*)12(.*)")) {
            name = name + String.valueOf(DmMonths.DECEMBER.getMonthName()) + ",";
        }
        txtHMonthsName.setText(DmShared.xNull(removeLastChar(name)));
    }

    private void setWeekName(String weekCode) {
        String name = "";
        if (weekCode.matches("(.*)1(.*)")) {
            name = name + String.valueOf(DmWeeks.FIRST.getWeekName()) + ",";
        }
        if (weekCode.matches("(.*)2(.*)")) {
            name = name + String.valueOf(DmWeeks.SECOND.getWeekName()) + ",";
        }
        if (weekCode.matches("(.*)3(.*)")) {
            name = name + String.valueOf(DmWeeks.THIRD.getWeekName()) + ",";
        }
        if (weekCode.matches("(.*)4(.*)")) {
            name = name + String.valueOf(DmWeeks.FOURTH.getWeekName()) + ",";
        }
        if (weekCode.matches("(.*)5(.*)")) {
            name = name + String.valueOf(DmWeeks.FIFTH.getWeekName()) + ",";
        }
        if (weekCode.matches("(.*)6(.*)")) {
            name = name + String.valueOf(DmWeeks.SIXTH.getWeekName()) + ",";
        }
        if (weekCode.matches("(.*)-1(.*)")) {
            name = name + String.valueOf(DmWeeks.LAST.getWeekName()) + ",";
        }
        txtHMonthsOnWeek.setText(DmShared.xNull(removeLastChar(name)));
    }

    private void setDaysName(String daysCode) {
        String name = "";
        if (daysCode.matches("(.*)1(.*)")) {
            name = name + String.valueOf(DmWeekdays.SUNDAY.getWeekDayName()) + ",";
        }
        if (daysCode.matches("(.*)2(.*)")) {
            name = name + String.valueOf(DmWeekdays.MONDAY.getWeekDayName()) + ",";
        }
        if (daysCode.matches("(.*)3(.*)")) {
            name = name + String.valueOf(DmWeekdays.TUESDAY.getWeekDayName()) + ",";
        }
        if (daysCode.matches("(.*)4(.*)")) {
            name = name + String.valueOf(DmWeekdays.WEDNESDAY.getWeekDayName()) + ",";
        }
        if (daysCode.matches("(.*)5(.*)")) {
            name = name + String.valueOf(DmWeekdays.THURSDAY.getWeekDayName()) + ",";
        }
        if (daysCode.matches("(.*)6(.*)")) {
            name = name + String.valueOf(DmWeekdays.FRIDAY.getWeekDayName()) + ",";
        }
        if (daysCode.matches("(.*)7(.*)")) {
            name = name + String.valueOf(DmWeekdays.SATURDAY.getWeekDayName()) + ",";
        }
        txtHMonthsOnDays.setText(DmShared.xNull(removeLastChar(name)));
    }

    private void onEdit() {
        if (radioMonthsDay.getValue()) {
            mangageRBtnMonth(1);
        } else if (radioMonthsOn.getValue()) {
            mangageRBtnMonth(2);
        }
        if (radioOnes.getValue()) {
            manageFlwRBtnDisplay(1, true);
        } else if (radioDaily.getValue()) {
            manageFlwRBtnDisplay(2, true);
        } else if (radioWeekly.getValue()) {
            manageFlwRBtnDisplay(3, true);
        } else if (radioMonthly.getValue()) {
            manageFlwRBtnDisplay(4, false);
        }
        txtSchedulerName.setFocus(true);
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(log.getText())) {
            if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {

            }
        }
    }

    private void manageDisplay(boolean flag) {
        txtHActive.setEnabled(flag);
        txtDBLookUp.setEnabled(false);
    }
}
