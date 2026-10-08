package dataman.payroll.attendanceImport.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.payroll.attendanceImport.shared.*;
import java.util.HashMap;

public class AttendanceImport extends AttendanceImport_Dsg implements BlurHandler, ClickHandler {

    interface CallBack {

        public void onOk(ImportData_Mdl attendanceImport);

        public void onDelete();
    }
    private final ServiceInvoker<AttendanceImport_SrvAsync> SrvAsync;
    private DefaultModel_Mdl parentRow;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

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
//                flwForm.remove(vrtInnerForm);
//                flwForm.add(dmFind);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AttendanceImport.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void findEvents() {
    }

    public AttendanceImport(UserMenu menuPermission, final SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(AttendanceImport_Srv.class), AttendanceImport.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);

        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        btnImport.addClickHandler(this);
        findEvents();
        textHelpEvents();
        SrvAsync.invokeRPC().getDefaultMdl(new AsyncCallback<DmResult<DefaultModel_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnImport).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultModel_Mdl> result) {
                if (result.isStatus()) {
                    AttendanceImport.this.parentRow = result.getData();
                    swi.setWidget(AttendanceImport.this);
                } else {
                    new DmAlertMessageBox("Hi", btnImport).show();
                }

            }
        });
    }

    private AttendanceImport_Mdl topControlSaveInitialize() {
        AttendanceImport_Mdl mrTemp = new AttendanceImport_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtToDate.getText()));
        return mrTemp;
    }

    private void topControlSave() {
        AttendanceImport_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            AttendanceImport_Vld.chkSave(mrTemp, DmClient.getLoginDate());

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtFromDate).show();
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
                new DmAlertMessageBox(errorMessage, txtFromDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtToDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
        }
    }

    private void topControlCard() {

    }

    private void topControlMoveRec(String strKey) {

    }

    private void topControlMoveRecSuccess(DmResult<AttendanceImport_Mdl> result) {
//        AttendanceImport_Mdl data;
//     
//        if (result.isStatus()) {
//            data = result.getData();
//            topControl.setDocId(data.getSharedKeyFieldCode());
//            txtToDate.setText(data.getSharedKeyFieldName());
//            txtFromDate.setText(data.getSharedKeyFieldCode());
//        } else {
//            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
//        }
//        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
//        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<String> result) {
//                if (result.isStatus()) {
//                    clear();
//                    topControlCard();
//                } else {
//                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
//                }
//            }
//        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void onAdd() {
        clear();
        txtFromDate.setFocus(true);
    }

    private void clear() {
//        userControl.setTextBlog(modifiedByBlog, "");
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtFromDate)) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        }
        if (event.getSource().equals(txtToDate)) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    private void textHelpEvents() {
    }

    @Override
    public void notifyForUpload(String notifier) {

    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        if (file != null) {
            parentRow.getImportMdl().setDataFilePath(file.getServerDirectory() + "/" + file.getServerFileName());
            parentRow.getImportMdl().setImportFrom(file.getFileName());
            txtFile.setText(file.getFileName());
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        ImportData_Mdl importMdl;
        if (event.getSource() == btnImport) {
            if (!DmShared.xNull(txtFile.getText()).equalsIgnoreCase("")) {
//                getCSVFile();
                parentRow.getImportMdl().setStartDate(txtFromDate.getText());
                parentRow.getImportMdl().setEndDate(txtToDate.getText());
                getImport();
            } else {
                new DmAlertMessageBox("Please give the format to download file.", btnImport).show();
                return;
            }
        }
    }

    private void getCSVFile() {
        try {
            SrvAsync.invokeRPC().getCSVFile(parentRow.getImportMdl(), DmShared.xNull(txtFile.getText()).trim(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        startDownloading(result.getData(), result.getValue1());
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void getImport() {
        try {
            SrvAsync.invokeRPC().importData(parentRow.getImportMdl(), new AsyncCallback<DmResult<ImportData_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<ImportData_Mdl> result) {
                    if (result.isStatus()) {
                        ImportData_Mdl data = result.getData();
                        if (data.getImportStatus() == DmImportStatus.ERROR) {
                            startDownloading(parentRow.getLogFileName(), parentRow.getLogFilePath());
                        } else {
                            new DmAlertMessageBox("Import completed...", btnImport).show();
                        }
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), btnImport).show();
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void startDownloading(String downloadFileName, String downloadDirectoryPath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", downloadFileName);
            params.put("p", downloadDirectoryPath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }
}
