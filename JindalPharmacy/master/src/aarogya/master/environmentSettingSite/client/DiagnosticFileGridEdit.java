package aarogya.master.environmentSettingSite.client;

import aarogya.master.environmentSettingSite.shared.EnvironmentSettingSite_Srv;
import aarogya.master.environmentSettingSite.shared.EnvironmentSettingSite_SrvAsync;
import aarogya.master.environmentSettingSite.shared.FileUpload_Mdl;
import aarogya.master.environmentSettingSite.shared.GrdDiagnosticFile_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;

public class DiagnosticFileGridEdit<T1, T2> extends DiagnosticFileGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdDiagnosticFile_Mdl diagnosticFile_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<EnvironmentSettingSite_SrvAsync> SrvAsync;
    private CallBack callBack;
    private EnvironmentSettingSite envSettingSite;
    private FileUpload_Mdl file_Mdl;

    public DiagnosticFileGridEdit(DmGrid<T1, T2> grid, DmFileRuleType fileRuleTypeDocument) {
        super(fileRuleTypeDocument, grid, "Upload and Download Radiology Header Document", "550px", "100px");
        SrvAsync = new ServiceInvoker<>(GWT.create(EnvironmentSettingSite_Srv.class), EnvironmentSettingSite.class.getName());
        handelEvent();

    }

    public void addCallBack(DiagnosticFileGridEdit.CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, EnvironmentSettingSite envSettingSite) {
        super.showRelativeTo(currentRowIndex, 1, false, false, false);
        this.envSettingSite = envSettingSite;
        txtUploadedFileName.setEnabled(false);
        txtNature.setEnabled(false);

    }

    @Override
    public void onOk() {

    }

    @Override
    public void onNew() {

    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onClose() {

        hide();
    }

    @Override
    public void manageOk(T1 row) {

    }

    @Override
    public void manageDelete() {

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdDiagnosticFile_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdDiagnosticFile_Mdl ourRow = (GrdDiagnosticFile_Mdl) row;

        txtUploadedFileName.setText(DmShared.xNull(ourRow.getFileName()));
        txtNature.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtNature.setTag(DmShared.iNull(ourRow.getSharedKeyFieldCode()).toString());

        txtUploadedFileName.setFocus(true);
    }

    private void handelEvent() {
        btnUploadDocument.setNotifier(new IFileUploadNotifier() {
            @Override
            public void notifyForUpload(String notifier) {

            }

            @Override
            public void processAfterUploaded(DmIFile_Mdl file) {
                if (file != null) {
                    getFileUpload(file);
                }
            }
        });
        btnDownloadDocument.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!DmShared.xNull(txtUploadedFileName.getText()).equalsIgnoreCase("")) {
                    downloadDocument();
                } else {
                    new DmAlertMessageBox(" First Upload Documnets ", txtUploadedFileName).show();
                    return;
                }
            }
        });
    }

    private void downloadDocument() {
        SrvAsync.invokeRPC().downloadDocument(envSettingSite.txtHSite.getTag(), txtNature.getTag(), fileRuleTypeDocument.getRuleCode(), new AsyncCallback<DmResult<DmAttachment_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<DmAttachment_Mdl> result) {
                if (result.isStatus()) {
                    startDownloadingDoument(result.getData());
                }
            }
        });
    }

    private void startDownloadingDoument(DmAttachment_Mdl attachment_Mdl) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("r", attachment_Mdl.getResourceAlocationId());
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileServlets/DmFileDownload_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    private void getFileUpload(final DmIFile_Mdl file) {
        boolean extFound = false;
        try {

            String[] filters = DmShared.xNull(fileRuleTypeDocument.getFilters()).split(",");
            String selExt = file.getFileName().substring(file.getFileName().lastIndexOf(".") + 1);

            for (int i = 0; i < filters.length; i++) {
                if (selExt.equalsIgnoreCase(filters[i].trim())) {
                    extFound = true;
                    break;
                }
            }

            if (!extFound) {
                new DmAlertMessageBox("Invalid file type. File upload failed.", (DmTextBox) null).show();
                return;
            }

            if (DmShared.vNull(fileRuleTypeDocument.getFileSizeLimit()).doubleValue() < (DmShared.iNull(file.getFileSize()).longValue() / 1024)) {
                new DmAlertMessageBox("File is too big. File upload failed.", (DmTextBox) null).show();
                return;
            }
            file_Mdl = new FileUpload_Mdl();
            file_Mdl.setSessionId(DmRequestBuilder.getSessionId());
            file_Mdl.setDocId(envSettingSite.txtHSite.getTag());
            file_Mdl.setsNo(DmShared.iNull(txtNature.getTag()).toString());

            file_Mdl.setRuleCode(DmShared.xNull(fileRuleTypeDocument.getRuleCode()));
            file_Mdl.setFileTypeName(DmShared.xNull(fileRuleTypeDocument.getFileTypeName()));
            file_Mdl.setFileSizeLimit(fileRuleTypeDocument.getFileSizeLimit());
            file_Mdl.setFilters(fileRuleTypeDocument.getFilters());

            file_Mdl.setFileName(file.getFileName());
            file_Mdl.setDirectoryPath(file.getServerDirectory());
            file_Mdl.setServerFileName(file.getServerFileName());
            file_Mdl.setFileSize(String.valueOf((DmShared.iNull(file.getFileSize()).longValue() / 1024)));
            if (file_Mdl != null) {
                uploadDocument(file_Mdl);
            }

        } catch (Exception ex) {
        }

    }

    private void uploadDocument(FileUpload_Mdl uploadMdl) {
        try {
            SrvAsync.invokeRPC().uploadFile(uploadMdl, new AsyncCallback<DmResult<FileUpload_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(DmShared.xNull(caught.getMessage()), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<FileUpload_Mdl> result) {
                    if (result.isStatus()) {
                        txtUploadedFileName.setText(result.getData().getFileName());

                        GrdDiagnosticFile_Mdl ourRow = (GrdDiagnosticFile_Mdl) DiagnosticFileGridEdit.this.getDmGrid().getGrid().getStore().get(DiagnosticFileGridEdit.this.getCurrentRowIndex());
//                        GrdDiagnosticFile_Mdl ourRow = new GrdDiagnosticFile_Mdl() ;
                        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtNature.getTag()));
                        ourRow.setSharedKeyFieldName(DmShared.xNull(txtNature.getText()));
                        ourRow.setFileName(DmShared.xNull(txtUploadedFileName.getText()));
                        callBack.onOk(ourRow);
                    } else {
                        new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(DmShared.xNull(e.getMessage()), (DmTextBox) null).show();
        }
    }
}
