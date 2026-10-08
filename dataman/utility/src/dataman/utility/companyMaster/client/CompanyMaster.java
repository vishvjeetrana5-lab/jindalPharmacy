package dataman.utility.companyMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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
import dataman.utility.companyMaster.shared.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyMaster extends CompanyMaster_Dsg implements BlurHandler, KeyPressHandler {

    ServiceInvoker<CompanyMaster_SrvAsync> SrvAsync;
    List<TxtHProductLicenseKey_Mdl> txtHPLK_Mdl = new ArrayList<TxtHProductLicenseKey_Mdl>();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtHProductLicenseKey.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHProductLicenseKey.setEnabled(false);
                txtCompanyCode.setEnabled(false);
                txtShortName.setEnabled(false);
                txtCurrentYear.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(CompanyMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHProductLicenseKey.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHProductLicenseKey_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHProductLicensekey(helper, blnToEndOfFile, "", new AsyncCallback<DmResultPageing<List<TxtHProductLicenseKey_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {

                        new DmAlertMessageBox(caught.getMessage(), txtHProductLicenseKey).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHProductLicenseKey_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHProductLicenseKey.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHProductLicenseKey).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHProductLicenseKey_Mdl tRow) {
                if (blnStatus && tRow != null) {
//                    if (!DmShared.xNull(tRow.getLatestCompCode()).equalsIgnoreCase("")) {
//                        txtHLatFinancial.setText("Yes");
//                        txtHLatFinancial.setTag("1");
//                    } else {
//                        txtHLatFinancial.setText("No");
//                        txtHLatFinancial.setTag("0");
//                    }

                    txtShortName.setText(tRow.getShortName());
                    fillRecords(tRow.getSharedKeyFieldCode());
                }
            }
        });
        txtHLatFinancial.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLatFinancial).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLatFinancial.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLatFinancial).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

    }

    private void fillRecords(String siteCode) {
        SrvAsync.invokeRPC().fillGrid(siteCode, new AsyncCallback<DmResult<List<GrdCompanyMaster_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdCompanyMaster_Mdl>> result) {
                if (result.isStatus()) {
                    dmGrd.getGrid().getStore().clear();
                    dmGrd.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHProductLicenseKey).show();
                }
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
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), dmFind.getTextBox()).show();
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

    public CompanyMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<CompanyMaster_SrvAsync>(GWT.create(CompanyMaster_Srv.class), CompanyMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        txtVoucherPrefix.addBlurHandler(this);
        txtYearStartDate.addBlurHandler(this);
        txtYearEndDate.addBlurHandler(this);
        txtCurrentYear.addKeyPressHandler(this);
        txtPreviousYear.addKeyPressHandler(this);
        textHelpEvents();
        findEvents();
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private CompanyMaster_Mdl topControlSaveInitialize() {
        CompanyMaster_Mdl mrTemp = new CompanyMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setProductLicenseKyCode(DmShared.xNull(txtHProductLicenseKey.getTag()));
        mrTemp.setProductLicenseKyName(DmShared.xNull(txtHProductLicenseKey.getText()));
        mrTemp.setCompanyCode(DmShared.xNull(txtCompanyCode.getText()));
        mrTemp.setShortName(DmShared.xNull(txtShortName.getText()));
        mrTemp.setCurrentYear(DmShared.xNull(txtCurrentYear.getText()));
        mrTemp.setPreviousYear(DmShared.xNull(txtPreviousYear.getText()));
        mrTemp.setYearStartDate(DmShared.dNull(txtYearStartDate.getText()));
        mrTemp.setYearEndDate(DmShared.dNull(txtYearEndDate.getText()));
        mrTemp.setVoucherPrefix(DmShared.xNull(txtVoucherPrefix.getText()));
        mrTemp.setDataBaseName(DmShared.xNull(txtDatabaseName.getText().trim()));
        mrTemp.setFileDataBaseName(DmShared.xNull(txtFileDatabaseName.getText().trim()));
        mrTemp.setFinacialYearTag(DmShared.xNull(txtHLatFinancial.getTag()));
        mrTemp.setMajorCurrency(DmShared.xNull(txtMajorCurrency.getText()));
        mrTemp.setMinorCurrency(DmShared.xNull(txtMinorCurrency.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        CompanyMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            CompanyMaster_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
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
                        topControlSaveError(result.getErrorNumber(), DmShared.xNull(result.getErrorMessage()));
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
                new DmAlertMessageBox(errorMessage, txtHProductLicenseKey).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtCurrentYear).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtPreviousYear).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtYearStartDate).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtYearEndDate).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtYearEndDate).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtVoucherPrefix).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtDatabaseName).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtFileDatabaseName).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtHLatFinancial).show();
                break;
            case 1100:
                new DmAlertMessageBox(errorMessage, txtMajorCurrency).show();
                break;
            case 1200:
                new DmAlertMessageBox(errorMessage, txtMinorCurrency).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHProductLicenseKey).show();
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
                        topControlSaveError(result.getErrorNumber(), DmShared.xNull(result.getErrorMessage()));
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<CompanyMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<CompanyMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<CompanyMaster_Mdl> result) {
        CompanyMaster_Mdl data;

        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHProductLicenseKey.setText(data.getProductLicenseKyName());
            txtHProductLicenseKey.setTag(data.getProductLicenseKyCode());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

            txtCompanyCode.setText(data.getCompanyCode());
            txtShortName.setText(data.getShortName());

            txtCurrentYear.setText(data.getCurrentYear());
            txtPreviousYear.setText(data.getPreviousYear());

            txtYearStartDate.setText(data.getYearStartDate());
            txtYearEndDate.setText(data.getYearEndDate());

            txtVoucherPrefix.setText(data.getVoucherPrefix());
            txtDatabaseName.setText(data.getDataBaseName());
            txtFileDatabaseName.setText(data.getFileDataBaseName());

            txtHLatFinancial.setTag(data.getFinacialYearTag());
            txtHLatFinancial.setText(data.getFinacialYearText());
            txtMajorCurrency.setText(data.getMajorCurrency());
            txtMinorCurrency.setText(data.getMinorCurrency());

            fillRecords(txtHProductLicenseKey.getTag());
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
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if ((DmShared.vNull(txtVoucherPrefix.getText()).doubleValue() > 32767)) {
            new DmAlertMessageBox("Voucher Prefix should be less than 32768", txtVoucherPrefix).show();
        }
        if (event.getSource().equals(txtYearStartDate)) {
            txtYearStartDate.setText(DmClient.returnDate(txtYearStartDate.getText()));
        }
        if (event.getSource().equals(txtYearEndDate)) {
            txtYearEndDate.setText(DmClient.returnDate(txtYearEndDate.getText()));
        }
        if (event.getSource().equals(txtCurrentYear)) {
            if (DmShared.xNull(txtCurrentYear.getText()).equalsIgnoreCase("")) {
                txtCurrentYear.setText(txtCurrentYear.getText());
            }
        }
        if (event.getSource().equals(txtPreviousYear)) {
            if (DmShared.xNull(txtPreviousYear.getText()).equalsIgnoreCase("")) {
                txtPreviousYear.setText(txtPreviousYear.getText());
            }
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if (event.getSource().equals(txtCurrentYear)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45))) {
                txtCurrentYear.cancelKey();
            }
        }
        if (event.getSource().equals(txtPreviousYear)) {
            if ((charCode > 31) && ((charCode < 48 || charCode > 57) && (charCode != 45))) {
                txtPreviousYear.cancelKey();
            }
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    public void clear() {
        dmGrd.getGrid().getStore().clear();
        txtCompanyCode.setEnabled(false);
        txtShortName.setEnabled(false);
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }
}
