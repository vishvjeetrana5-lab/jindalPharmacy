package dataman.dmToolkitR.dmFileManager.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkitR.dmFileManager.shared.*;
import dataman.dmToolkitR.dmFileUploader.client.DmFile;
import dataman.dmToolkitR.dmFileUploader.client.events.DmFileDialogCompleteEvent;
import dataman.dmToolkitR.dmFileUploader.client.events.DmFileDialogCompleteHandler;
import dataman.dmToolkitR.dmFileUploader.client.events.DmFileDialogStartEvent;
import dataman.dmToolkitR.dmFileUploader.client.events.DmFileDialogStartHandler;
import dataman.dmToolkitR.dmFileUploader.client.events.DmFileQueueErrorEvent;
import dataman.dmToolkitR.dmFileUploader.client.events.DmFileQueueErrorHandler;
import dataman.dmToolkitR.dmFileUploader.client.events.DmFileQueuedEvent;
import dataman.dmToolkitR.dmFileUploader.client.events.DmFileQueuedHandler;
import dataman.dmToolkitR.dmFileUploader.client.events.DmUploadCompleteEvent;
import dataman.dmToolkitR.dmFileUploader.client.events.DmUploadCompleteHandler;
import dataman.dmToolkitR.dmFileUploader.client.events.DmUploadErrorEvent;
import dataman.dmToolkitR.dmFileUploader.client.events.DmUploadErrorHandler;
import dataman.dmToolkitR.dmFileUploader.client.events.DmUploadProgressEvent;
import dataman.dmToolkitR.dmFileUploader.client.events.DmUploadProgressHandler;
import dataman.dmToolkitR.dmFileUploader.client.events.DmUploadStartEvent;
import dataman.dmToolkitR.dmFileUploader.client.events.DmUploadStartHandler;
import dataman.dmToolkitR.dmFileUploader.client.events.DmUploadSuccessEvent;
import dataman.dmToolkitR.dmFileUploader.client.events.DmUploadSuccessHandler;
import dataman.dmToolkitR.dmAlertBox.client.DmAlertBox;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DmFileType extends DmFileType_Dsg implements IsWidget, DmFileDialogStartHandler, DmFileDialogCompleteHandler, DmUploadProgressHandler,
        DmUploadSuccessHandler, DmFileQueuedHandler, DmFileQueueErrorHandler, DmUploadErrorHandler, DmUploadCompleteHandler, DmUploadStartHandler {

    private final ServiceInvoker<DmFileManager_SrvAsync> SrvAsync;
    protected final DmFileManager fileManager;
    private DmFileRuleType fileRuleTypeMdl;
    private List<DmAttachment_Mdl> listAttachments = new ArrayList<>();

    DmAttachment.AttachmentCallBack attachmentCallback = new DmAttachment.AttachmentCallBack() {
        @Override
        public void refreshView(DmEnum.DmStatus status, DmAttachment_Mdl fileAttachment) {
            if (status == DmEnum.DmStatus.OK) {
                getFileType();
            } else if (status == DmEnum.DmStatus.SAVE) {
                startUploading();
            }
        }
    };

    @Override
    public Widget asWidget() {
        return this.fileTypeFlowPanel;
    }

    public DmFileType(DmFileRuleType fileRuleTypeMdl, DmFileManager fileManager) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(DmFileManager_Srv.class), DmFileManager.class.getName());
        this.fileRuleTypeMdl = fileRuleTypeMdl;
        this.fileManager = fileManager;

        manageEvents();
        setValues();
    }

    private void getFileType() {

        SrvAsync.invokeRPC().getFileType(fileManager.getKeyCode(), fileManager.getTempKeyCode(),
                fileManager.getSNo(), fileManager.getApplicableFileRule(),
                fileRuleTypeMdl.getFileTypeName(), fileManager.getMultiDocIdWithRuleCode(),
                new AsyncCallback<DmResult<List<DmFileRuleType>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<DmFileRuleType>> result) {
                if (result.isStatus()) {
                    fileRuleTypeMdl = result.getData().get(0);
                    fillAttachedFiles();
                } else {
                    new DmAlertBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void manageEvents() {
        attachAnchor.setUploadProgressHandler(this);
        attachAnchor.setUploadSuccessHandler(this);
        attachAnchor.setUploadStartHandler(this);
        attachAnchor.setUploadCompleteHandler(this);
        attachAnchor.setUploadErrorHandler(this);
        attachAnchor.setFileDialogCompleteHandler(this);
        attachAnchor.setFileDialogStartHandler(this);
        attachAnchor.setFileQueuedHandler(this);
        attachAnchor.setFileQueueErrorHandler(this);
    }

    private void setValues() {
        if (fileRuleTypeMdl != null) {
            flwAttach.clear();
            attachAnchor.clear();
            if (fileManager.tabPaneHistory.isActive() == true ? false : fileManager.canAttach) {
                flwAttach.add(attachAnchor);
                attachAnchor.setButtonDisabled(false);

                if (fileRuleTypeMdl != null) {
                    boolean extFound = false;
                    String[] filters = DmShared.xNull(fileRuleTypeMdl.getFilters()).split(",");
                    for (int i = 0; i < filters.length; i++) {
                        if ("jpg".equalsIgnoreCase(filters[i].trim())) {
                            extFound = true;
                            break;
                        }
                    }
                    if (extFound) {

                    }
                }
            } else {
                flwAttach.add(attachAnchor);
                attachAnchor.setButtonDisabled(true);
            }
            attachAnchor.setButtonText(DmShared.xNull(fileRuleTypeMdl.getFileTypeName()));

            if (!fileManager.tabPaneHistory.isActive()) {
                lblFilters.setText(DmShared.xNull(fileRuleTypeMdl.getFilters()) + " (" + DmShared.xNull(fileRuleTypeMdl.getFileSizeLimit()) + " kb)");
            }

            fillAttachedFiles();
        }
    }

    private void fillAttachedFiles() {
        fileDetailsFlowPanel.clear();

        if (fileRuleTypeMdl.getListFiles() != null) {
            List<DmAttachment_Mdl> attachments = fileRuleTypeMdl.getListFiles();
            for (int i = 0; i < attachments.size(); i++) {
                DmAttachment_Mdl attachment = attachments.get(i);
                if ((DmShared.iNull(attachment.getDocId()).longValue() != 0) || (!DmShared.xNull(attachment.getTempDocId()).trim().equals(""))) {
                    addFile(attachment);
                }
            }
        }
    }

    @Override
    public boolean onFileDialogStartEvent(DmFileDialogStartEvent fileDialogStartEvent) {
        listAttachments.clear();
        return true;
    }

    @Override
    public boolean onFileDialogComplete(DmFileDialogCompleteEvent fileDialogCompleteEvent) {
        boolean rValue = false;
        if (fileDialogCompleteEvent.getNumberOfFilesQueued() != listAttachments.size()) {
            //new DmAlertBox("Files are not properly Queued for Upload.", (Button) null).show();
        } else {
            rValue = true;
            allocateResource();
        }
        return rValue;
    }

    @Override
    public boolean onUploadProgress(DmUploadProgressEvent uploadProgressEvent) {
        return true;
    }

    @Override
    public boolean onUploadSuccess(DmUploadSuccessEvent event) {
        if (event.getResponseReceived().equals("200")) {
            DmAttachment_Mdl fileAttachment = new DmAttachment_Mdl();
            fileAttachment.setFileLink(event.getClientFileLinkId());
            fileAttachment.setFileName(event.getFileName());
            fileAttachment.setRuleCode(event.getRuleCode());
            if (listAttachments.contains(fileAttachment)) {
                listAttachments.remove(fileAttachment);
            }
            if (listAttachments.isEmpty()) {
                attachmentCallback.refreshView(DmEnum.DmStatus.OK, null);
            }
        }

        return true;
    }

    @Override
    public boolean onFileQueued(DmFileQueuedEvent event) {
        DmFile selectedFile;
        boolean extFound = false;

        try {
            selectedFile = event.getFile();
            String[] filters = DmShared.xNull(fileRuleTypeMdl.getFilters()).split(",");
            String selExt = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);

            for (int i = 0; i < filters.length; i++) {
                if (selExt.equalsIgnoreCase(filters[i].trim())) {
                    extFound = true;
                    break;
                }
            }

            if (!extFound) {
                new DmAlertBox("Invalid file type. File upload failed.", (DmTextBox) null).show();
                return false;
            }

            if (DmShared.vNull(fileRuleTypeMdl.getFileSizeLimit()).doubleValue() < (selectedFile.getSize() / 1024)) {
                new DmAlertBox("File is too big. File upload failed.", (DmTextBox) null).show();
                return false;
            }

            DmAttachment_Mdl fileModel = new DmAttachment_Mdl();
            fileModel.setRuleCode(DmShared.xNull(fileRuleTypeMdl.getRuleCode()));
            fileModel.setFileName(selectedFile.getName());

            if (!listAttachments.contains(fileModel)) {
                fileModel.setFileLink(selectedFile.getId());
                fileModel.setTempDocId(fileManager.tempKeyCode);
                fileModel.setSessionId(fileRuleTypeMdl.getSessionId());
                fileModel.setDocId(fileManager.keyCode);
                fileModel.setsNo(fileManager.sNo);
                fileModel.setFileTypeName(fileRuleTypeMdl.getFileTypeName());
                fileModel.setFileSizeLimit(fileRuleTypeMdl.getFileSizeLimit());
                fileModel.setFilters(fileRuleTypeMdl.getFilters());
                fileModel.setFileTypeName(fileRuleTypeMdl.getFileTypeName());
                fileModel.setFileSize(String.valueOf(selectedFile.getSize() / 1024));

                listAttachments.add(fileModel);
            }
        } catch (Exception ex) {
        }

        return true;
    }

    @Override
    public boolean onFileQueueError(DmFileQueueErrorEvent event) {
        new DmAlertBox("File Queue Error:" + event.getMessage(), (DmTextBox) null).show();
        return true;
    }

    @Override
    public boolean onUploadError(DmUploadErrorEvent event) {
        new DmAlertBox("Upload Error:" + event.getMessage(), (DmTextBox) null).show();
        return true;
    }

    @Override
    public boolean onUploadComplete(DmUploadCompleteEvent uploadCompleteEvent) {
        return true;
    }

    @Override
    public boolean onUploadStart(DmUploadStartEvent uploadStartEvent) {
        return true;
    }

    private void allocateResource() {
        SrvAsync.invokeRPC().allocateResource(listAttachments, new AsyncCallback<DmResult<List<DmAttachment_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertBox(DmShared.xNull(caught.getMessage()), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<DmAttachment_Mdl>> result) {
                if (result.isStatus()) {
                    listAttachments.clear();
                    listAttachments.addAll(result.getData());
                    for (int i = 0; i < listAttachments.size(); i++) {
                        DmAttachment_Mdl attachment = listAttachments.get(i);
                        attachAnchor.setServerIdToFile(attachment.getResourceAlocationId(), attachment.getFileLink());
                    }
                    attachmentCallback.refreshView(DmEnum.DmStatus.SAVE, null);
                } else {
                    new DmAlertBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void startUploading() {
        HashMap<String, String> params = new HashMap<>();

        try {
            params.put("u", DmRequestBuilder.getSessionId());
            attachAnchor.setUploadURL(DmClient.concatURL("dataman/dmToolkitR/dmFileServlets/DmFileUpload_Srvlt", params));
            attachAnchor.startUpload();
        } catch (Exception ex) {
            new DmAlertBox(DmShared.xNull(ex.getMessage()), (DmTextBox) null).show();
        }
    }

    private void addFile(DmAttachment_Mdl attachmentMdl) {
        if (attachmentMdl != null) {
            DmAttachment dmAttachment = new DmAttachment(attachmentMdl, this);
            dmAttachment.addFileCallBack(attachmentCallback);
            fileDetailsFlowPanel.add(dmAttachment.asWidget());
        }
    }
}
