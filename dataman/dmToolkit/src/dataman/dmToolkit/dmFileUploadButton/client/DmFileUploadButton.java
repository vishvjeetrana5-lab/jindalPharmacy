package dataman.dmToolkit.dmFileUploadButton.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmFileUploader.client.DmFile;
import dataman.dmToolkit.dmFileUploader.client.DmStats;
import dataman.dmToolkit.dmFileUploader.client.events.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.ArrayList;
import java.util.HashMap;

public class DmFileUploadButton extends Composite {

    public interface CallBack {

        public String getUploadURL(DmFile selectedFile);

        public void onUploadSuccess(DmUploadSuccessEvent uploadSuccessEvent);

        public void onUploadError(DmUploadErrorEvent uploadErrorEvent);
    }

    private CallBack callBack;
    private final int FILE_QUEUE_LIMIT = 1;
    private IFileUploadNotifier notifier;
    private boolean ajaxUploadEnabled = true;
    private String filePostName = null;
    private String uploadURL;
    private boolean buttonDisabled = false;
    private DmFile selectedFile;
    private long[] httpSuccess;
    private FlowPanel flwPanel = new FlowPanel();
    private Button button = new Button();
    private JSONObject postParams = null;
    private boolean requeueOnError = false;
    private ButtonAction buttonAction = ButtonAction.SELECT_FILE;
    private JavaScriptObject lastXMLHttpRequest = null;
    private DmStats stats;
    private ArrayList<JavaScriptObject> nativeFilesQueued = new ArrayList<JavaScriptObject>();
    private HashMap<String, JavaScriptObject> nativeFilesQueuedById = new HashMap<String, JavaScriptObject>();
    private long lastFileId = 0;
    private FileUpload fileUpload;
    private DmFileRuleType fileRuleType;

    private boolean onFileDialogStartEvent(DmFileDialogStartEvent fileDialogStartEvent) {
        return true;
    }

    private boolean onFileDialogComplete(DmFileDialogCompleteEvent fileDialogCompleteEvent) {
        return (fileDialogCompleteEvent.getNumberOfFilesQueued() == this.FILE_QUEUE_LIMIT);
    }

    private boolean onUploadProgress(DmUploadProgressEvent uploadProgressEvent) {
        boolean bool = false;
        try {
            final String notifyStr = DmClient.vNull(((uploadProgressEvent.getBytesComplete() * 100l) / uploadProgressEvent.getBytesTotal()), "0") + " %";
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    DmFileUploadButton.this.notifier.notifyForUpload(notifyStr.trim());
                }
            });
            bool = true;
        } catch (Exception ex) {
            bool = false;
        }
        return bool;
    }

    private boolean checkForSelectedFileSizeLimit(long selectedFileSize) {
//        boolean bool = false;
//        try {
//            if ((this.fileSizeLimit * this.fileUnit.getSizeInBytes()) < selectedFileSize) {
//                new DmAlertMessageBox("Size of Selected File must not be greater than : " + this.fileSizeLimit + " " + this.fileUnit.getUnitName(), (TextArea) null).show();
//                bool = false;
//            } else {
//                bool = true;
//            }
//        } catch (Exception ex) {
//            bool = false;
//        }
//        return bool;

        boolean bool = false;
        try {
            if (fileRuleType != null) {
                if ((DmShared.iNull(this.fileRuleType.getFileSizeLimit()).longValue() * 1024) < selectedFileSize) {
                    new DmAlertMessageBox("Size of Selected File must not be greater than : " + DmShared.iNull(this.fileRuleType.getFileSizeLimit()).toString() + "kb ", (TextArea) null).show();
                    bool = false;
                } else {
                    bool = true;
                }
            }

        } catch (Exception ex) {
            bool = false;
        }
        return bool;
    }

    private boolean onUploadSuccess(DmUploadSuccessEvent uploadSuccessEvent) {
        boolean bool = false;
        try {
            if (callBack != null) {
                callBack.onUploadSuccess(uploadSuccessEvent);
            } else {
                String serverResponseData = uploadSuccessEvent.getServerData();
                String[] values = serverResponseData.split("\\|");
                if (uploadSuccessEvent.getResponseReceived().equals("200")) {
                    DmFile_Mdl file = new DmFile_Mdl();
                    file.setServerDirectory(values[0]);
                    file.setFileName(values[1]);
                    file.setServerFileName(values[2]);
                    this.notifier.processAfterUploaded(file);
                }
            }
        } catch (Exception ex) {
            bool = false;
        }
        return bool;
    }

    private boolean checkForPermittibleFileExtensions(String fileName) {
        boolean bool = false;

        try {
            if (fileRuleType != null) {
                String fileExt = DmShared.xNull(fileName.substring(fileName.lastIndexOf(".") + 1)).trim();
                String[] filterExtList = DmShared.xNull(fileRuleType.getFilters()).split(",");

                for (int i = 0; i < filterExtList.length; i++) {
                    String filterExt = DmShared.xNull(filterExtList[i]).trim();
//                    if (filterExt.indexOf(".") >= 0) {
//                        filterExt = filterExt.substring(filterExt.lastIndexOf(".") + 1);
//                    }

                    if (filterExt.equalsIgnoreCase(fileExt)) {
                        bool = true;
                        break;
                    }
                }

                if (!bool) {
                    new DmAlertMessageBox("Selected File Having Extension : " + fileExt + ". Which is prohibited.", (TextArea) null).show();
                }
            }

        } catch (Exception ex) {
            bool = false;
        }
        return bool;
    }

    private boolean onFileQueued(DmFileQueuedEvent fileQueuedEvent) {
        boolean bool = false;
        try {
            this.selectedFile = null;
            this.uploadURL = null;
            bool = checkForSelectedFileSizeLimit(fileQueuedEvent.getFile().getSize());
            if (!bool) {
                throw new Exception("Selected File Size limit is Exceeds from Declared File Size.");
            }
            bool = checkForPermittibleFileExtensions(fileQueuedEvent.getFile().getName());
            if (!bool) {
                throw new Exception("Selected file extension is not allowed.");
            }
            this.selectedFile = fileQueuedEvent.getFile();
            this.setUploadURL();
            this.startUpload();
        } catch (Exception ex) {
            this.selectedFile = null;
            this.uploadURL = null;
            bool = false;
        }
        return bool;
    }

    private boolean onFileQueueError(DmFileQueueErrorEvent fileQueueErrorEvent) {
        boolean bool = false;
        try {
            this.selectedFile = null;
            this.uploadURL = null;
            new DmAlertMessageBox(fileQueueErrorEvent.getMessage(), (TextArea) null).show();
            throw new Exception(fileQueueErrorEvent.getMessage());
        } catch (Exception ex) {
            this.selectedFile = null;
            this.uploadURL = null;
            bool = false;
        }
        return bool;
    }

    private boolean onUploadError(DmUploadErrorEvent uploadErrorEvent) {
        boolean bool = false;
        try {
            this.selectedFile = null;
            this.uploadURL = null;

            if (callBack != null) {
                callBack.onUploadError(uploadErrorEvent);
            } else {
                new DmAlertMessageBox(uploadErrorEvent.getMessage(), (TextArea) null).show();
                throw new Exception(uploadErrorEvent.getMessage());
            }
        } catch (Exception ex) {
            this.selectedFile = null;
            this.uploadURL = null;
            bool = false;
        }
        return bool;
    }

    private boolean onUploadComplete(DmUploadCompleteEvent uploadCompleteEvent) {
        return true;
    }

    private boolean onUploadStart(DmUploadStartEvent uploadStartEvent) {
        return true;
    }

    public enum ButtonAction {

        SELECT_FILE(-100),
        START_UPLOAD(-120),
        SELECT_FILES(-110);

        private ButtonAction(int optionValue) {
            this.optionValue = optionValue;
        }

        private final int optionValue;

        public int toInt() {
            return optionValue;
        }
    }

    public enum Cursor {

        ARROW(-1),
        HAND(-2);

        private Cursor(int optionValue) {
            this.optionValue = optionValue;
        }

        private final int optionValue;

        public int toInt() {
            return optionValue;
        }
    }

    public enum WindowMode {

        WINDOW("window"),
        TRANSPARENT("transparent"),
        OPAQUE("opaque");

        private WindowMode(String optionValue) {
            this.optionValue = optionValue;
        }

        private final String optionValue;

        public String toString() {
            return optionValue;
        }
    }

    public DmFileUploadButton() {
        this(null, null);
    }

    public DmFileUploadButton(IFileUploadNotifier notifier, DmFileRuleType fileRuleType) {
        this.notifier = notifier;
        this.fileRuleType = fileRuleType;
        initComponent();
    }

    public DmFileUploadButton(IFileUploadNotifier notifier, DmFileRuleType fileRuleType, CallBack callBack) {
        this.notifier = notifier;
        this.fileRuleType = fileRuleType;
        this.callBack = callBack;
        initComponent();
    }

    public void setNotifier(IFileUploadNotifier notifier) {
        this.notifier = notifier;
    }

    public IFileUploadNotifier getNotifier() {
        return this.notifier;
    }

    private void initComponent() {

        if (ajaxUploadEnabled && isAjaxUploadWithProgressEventsSupported()) {

            this.flwPanel.add(createFileUpload());

            this.button.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (buttonDisabled) {
                        return;
                    }
                    switch (buttonAction) {
                        case START_UPLOAD:
                            startUpload();
                            break;
                        case SELECT_FILES:
                            openFileDialog(fileUpload, true);
                            break;
                        case SELECT_FILE:
                        default:
                            openFileDialog(fileUpload, false);
                            break;
                    }
                }
            });

//            button.addSelectHandler(new SelectEvent.SelectHandler() {
//                @Override
//                public void onSelect(SelectEvent event) {
//                    if (buttonDisabled) {
//                        return;
//                    }
//                    switch (buttonAction) {
//                        case START_UPLOAD:
//                            startUpload();
//                            break;
//                        case SELECT_FILE:
//                        default:
//                            openFileDialog(fileUpload, false);
//                            break;
//                    }
//                }
//            });
//            setDefaultOptions();
            this.flwPanel.add(button);

            initWidget(this.flwPanel);
        }
    }

    @Override
    public void setSize(String width, String height) {
        this.button.setSize(width, height);
        super.setSize(width, height);
    }

    private void setUploadURL() {
        if (callBack != null) {
//            HashMap<String, String> params = new HashMap<>();
//            params.put("u", DmRequestBuilder.getSessionId());
//            params.put("f", this.selectedFile.getName());
//            this.uploadURL = DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileUploadButton_Srvlt", params);
            this.uploadURL = callBack.getUploadURL(this.selectedFile);
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", this.selectedFile.getName());
            this.uploadURL = DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileUploadButton_Srvlt", params);
        }

    }

    public DmStats getStats() {
        if (stats == null) {
            stats = (DmStats) JavaScriptObject.createObject();
        }
        return stats;
    }

    public void startUpload() {

        if (uploadURL == null) {
            throw new IllegalStateException("The 'startUpload()' method was invoked before the uploader URL was provided.  Please call the 'setUploadURL' first.");
        }

        if (nativeFilesQueued.size() > 0) {
            JavaScriptObject nativeFile = nativeFilesQueued.get(0);

            nativeSetProperty(nativeFile, "startTime", System.currentTimeMillis());
            nativeSetProperty(nativeFile, "timeSinceLastEvent", System.currentTimeMillis());

            nativeSetProperty(getStats(), "in_progress", 1);

            nativeUpdateFileProperties(nativeFile, DmFile.Status.IN_PROGRESS.toInt());
            onUploadStart(new DmUploadStartEvent(nativeFile.<DmFile>cast()));

            uploadProgressEventCallback(nativeFile.<DmFile>cast(), 0.0, (double) nativeFile.<DmFile>cast().getSize());

            lastXMLHttpRequest = nativeStartAjaxUpload(nativeFile,
                    uploadURL,
                    filePostName != null ? filePostName : "Filedata",
                    postParams != null ? postParams.getJavaScriptObject() : null
            );

        }
    }

    private native JavaScriptObject nativeStartAjaxUpload(JavaScriptObject file, String url, String filePostName, JavaScriptObject postParams) /*-{

     var self = this;
     var xhr = new XMLHttpRequest();

     // Setup the event handlers we'll need to let the consuming application know what's going on
     xhr.upload.addEventListener('progress', function(e) {
     return self.@dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton::uploadProgressEventCallback(Ldataman/dmToolkit/dmFileUploader/client/DmFile;DD)(file, e.loaded, e.total);
     }, false);
     xhr.addEventListener('load', function() {
     return self.@dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton::uploadSuccessEventCallback(Ldataman/dmToolkit/dmFileUploader/client/DmFile;Ljava/lang/String;Ljava/lang/String;)(
     @dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton::nativeUpdateFileProperties(Lcom/google/gwt/core/client/JavaScriptObject;I)(
     file,
     -4      // DmFile.Status.COMPLETE
     ),
     xhr.status + "",
     xhr.responseText
     )
     }, false);
     xhr.addEventListener('error', function() {
     return self.@dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton::uploadErrorEventCallback(Ldataman/dmToolkit/dmFileUploader/client/DmFile;ILjava/lang/String;)(
     @dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton::nativeUpdateFileProperties(Lcom/google/gwt/core/client/JavaScriptObject;I)(
     file,
     -3      // DmFile.Status.ERROR
     ),
     -250, // UploadErrorEvent.ErrorCode.UPLOAD_FAILED
     xhr.responseText
     )
     }, false);
     xhr.addEventListener('abort', function() {
     return self.@dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton::uploadErrorEventCallback(Ldataman/dmToolkit/dmFileUploader/client/DmFile;ILjava/lang/String;)(
     @dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton::nativeUpdateFileProperties(Lcom/google/gwt/core/client/JavaScriptObject;I)(
     file,
     -5      // DmFile.Status.CANCELLED
     ),
     -280, // UploadErrorEvent.ErrorCode.FILE_CANCELLED
     "Upload Aborted"
     )
     }, false);

     xhr.open('POST', url, true);
     var formData = new FormData();
     formData.append(filePostName, file);

     // Append on any post params
     if(postParams != null) {
     for (var key in postParams) {
     formData.append(key, postParams[key]);
     }
     }

     // Kick off the multipart/form-data upload
     xhr.send(formData);

     return xhr;
     }-*/;

    public void cancelUpload(String fileId, boolean triggerErrorEvent) {
        JavaScriptObject nativeFile = nativeFilesQueuedById.get(fileId);
        if (nativeFile != null) {

            boolean cancelledActiveUpload = false;

            if (nativeFilesQueued.size() > 0 && lastXMLHttpRequest != null) {
                final DmFile currentFile = nativeFilesQueued.get(0).cast();
                if (nativeFile.<DmFile>cast().getId().equals(currentFile.getId()) && currentFile.getStatus() == DmFile.Status.IN_PROGRESS) {
                    try {
                        cancelledActiveUpload = true;
                        nativeAbortXMLHttpRequest(lastXMLHttpRequest);
                    } catch (Throwable t) {
                    }
                }
            }

            nativeUpdateFileProperties(nativeFile, DmFile.Status.CANCELLED.toInt());

            nativeSetProperty(getStats(), "upload_cancelled", getStats().getUploadsCancelled() + 1);

            if (triggerErrorEvent) {
                nativeSetProperty(getStats(), "upload_errors", getStats().getUploadErrors() + 1);
                onUploadError(new DmUploadErrorEvent(nativeFile.<DmFile>cast(), DmUploadErrorEvent.ErrorCode.FILE_CANCELLED.toInt(), "File Cancelled"));
            }

            if (cancelledActiveUpload) {
                uploadCompleteEventCallback(nativeFile.<DmFile>cast());
            } else {
                nativeFilesQueued.remove(nativeFile);
                nativeFilesQueuedById.remove(nativeFile.<DmFile>cast().getId());
                nativeSetProperty(getStats(), "files_queued", nativeFilesQueued.size());
                nativeSetProperty(getStats(), "in_progress", 0);
            }
        }
    }

    private static native void nativeAbortXMLHttpRequest(JavaScriptObject xmlHttpRequest) /*-{
     xmlHttpRequest.abort();
     }-*/;

    public void cancelUpload() {
        if (nativeFilesQueued.size() > 0) {
            cancelUpload(nativeFilesQueued.get(0).<DmFile>cast().getId());
        }
    }

    public Button getButton() {
        return this.button;
    }

    public void cancelUpload(String fileId) {
        cancelUpload(fileId, true);
    }

    public void cancelUpload(boolean triggerErrorEvent) {
        if (nativeFilesQueued.size() > 0) {
            cancelUpload(nativeFilesQueued.get(0).<DmFile>cast().getId(), triggerErrorEvent);
        }
    }

    public static boolean isAjaxUploadWithProgressEventsSupported() {
        return isFileAPISupported() && isAjaxUploadProgressEventsSupported() && isFormDataSupported();
    }

    private static native boolean isFileAPISupported() /*-{
     var inputElement = document.createElement('INPUT');
     inputElement.type = 'file';
     return 'files' in inputElement;
     }-*/;

    private static native boolean isAjaxUploadProgressEventsSupported() /*-{
     var xhr = new XMLHttpRequest();
     return !! (xhr && ('upload' in xhr) && ('onprogress' in xhr.upload));
     }-*/;

    private static native boolean isFormDataSupported() /*-{
     return !! window.FormData;
     }-*/;

    private FileUpload createFileUpload() {
        fileUpload = new FileUpload();
        fileUpload.getElement().getStyle().setDisplay(Style.Display.NONE);
        fileUpload.getElement().setAttribute("multiple", "true");

        fileUpload.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {

                JsArray selectedFiles = nativeGetSelectedFiles(fileUpload.getElement());

                DmFileUploadButton.this.flwPanel.remove(fileUpload);
                if (buttonAction == ButtonAction.SELECT_FILE) {
                    nativeFilesQueued.clear();
                    nativeFilesQueuedById.clear();
                }
                fileUpload = null;

                DmFileUploadButton.this.flwPanel.add(createFileUpload());

                addFilesToQueue(selectedFiles);

            }
        });
        return fileUpload;
    }

    private String getNextFileId() {
        lastFileId++;
        return "Uploader_" + lastFileId;
    }

    public void addFilesToQueue(JsArray files) {

        int filesQueued = 0;

        if (files != null) {
            for (int i = 0; i < files.length(); i++) {
                JavaScriptObject nativeFile = files.get(i);

                if (nativeFile.<DmFile>cast().getSize() <= 0) {
                    fileQueueErrorEventCallback(nativeFile.<DmFile>cast(), DmFileQueueErrorEvent.ErrorCode.ZERO_BYTE_FILE.toInt(), "File is zero bytes and cannot be uploaded.");
                    continue;
                }

                if (exceedsFileSizeLimit(nativeFile)) {
                    fileQueueErrorEventCallback(nativeFile.<DmFile>cast(), DmFileQueueErrorEvent.ErrorCode.FILE_EXCEEDS_SIZE_LIMIT.toInt(), "File size exceeds allowed limit.");
                    continue;
                }

                nativeSetProperty(nativeFile, "id", getNextFileId());

                filesQueued++;

                addFileToQueue(nativeFile);

            }
        }

        onFileDialogComplete(new DmFileDialogCompleteEvent(files != null ? files.length() : 0, filesQueued, nativeFilesQueued.size()));

    }

    public static native JsArray getDroppedFiles(NativeEvent event) /*-{
     return event.target.files || event.dataTransfer.files;
     }-*/;

    private void addFileToQueue(JavaScriptObject nativeFile) {
        nativeFilesQueued.add(nativeFile);
        nativeFilesQueuedById.put(nativeFile.<DmFile>cast().getId(), nativeFile);

        nativeSetProperty(getStats(), "files_queued", nativeFilesQueued.size());

        onFileQueued(new DmFileQueuedEvent(nativeUpdateFileProperties(nativeFile, DmFile.Status.QUEUED.toInt()).<DmFile>cast()));

    }

    private boolean exceedsFileSizeLimit(JavaScriptObject nativeFile) {
        long fileSize = nativeFile.<DmFile>cast().getSize();
        boolean rtn = false;

        if (fileRuleType != null) {
            if ((DmShared.iNull(this.fileRuleType.getFileSizeLimit()).longValue() * 1024) < fileSize) {
                rtn = true;
            }
        }

        return rtn;

//        return this.fileSizeLimit != 0 && fileSize > (this.fileSizeLimit * this.fileUnit.getSizeInBytes());
    }

    private void openFileDialog(FileUpload fileUpload, boolean multipleFiles) {

        if (multipleFiles) {
            fileUpload.getElement().setAttribute("multiple", "true");
        } else {
            fileUpload.getElement().removeAttribute("multiple");
        }

        onFileDialogStartEvent(new DmFileDialogStartEvent());

        InputElement.as(fileUpload.getElement()).click();
    }

    private boolean fileQueueErrorEventCallback(DmFile file, int errorCode, String message) {
        nativeSetProperty(getStats(), "queue_errors", getStats().getQueueErrors() + 1);

        return onFileQueueError(new DmFileQueueErrorEvent(file, errorCode, message));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private boolean uploadProgressEventCallback(DmFile file, double bytesComplete, double bytesTotal) {

        long now = System.currentTimeMillis();
        long bytesSinceLastEvent = (long) (bytesComplete - (nativeGetPropertyAsDouble(file, "previousBytesComplete")));
        double timeSinceLastEvent = (now - nativeGetPropertyAsDouble(file, "timeSinceLastEvent")) / 1000.0;
        double elapsedTime = (now - nativeGetPropertyAsDouble(file, "startTime")) / 1000.0;
        double currentSpeed = (bytesSinceLastEvent * 8) / timeSinceLastEvent;
        if (!Double.isInfinite(currentSpeed) && !Double.isNaN(currentSpeed)) {
            nativeAddValueToArray(file, "movingAverageHistory", currentSpeed);
        }
        nativeSetProperty(file, "bytesSinceLastEvent", bytesSinceLastEvent);
        nativeSetProperty(file, "previousBytesComplete", bytesComplete);
        nativeSetProperty(file, "timeSinceLastEvent", now);

        nativeSetProperty(file, "averageSpeed", (bytesComplete * 8) / elapsedTime); // Bits per seconds
        nativeSetProperty(file, "currentSpeed", currentSpeed); // Bytes per seconds since last event
        nativeSetProperty(file, "movingAverageSpeed", calculateMovingAverage(nativeGetPropertyAsArray(file, "movingAverageHistory"))); // Bytes per second
        nativeSetProperty(file, "percentUploaded", (bytesComplete / bytesTotal * 100)); // Bytes per seconds
        nativeSetProperty(file, "sizeUploaded", bytesComplete); // Bytes uploaded so far
        nativeSetProperty(file, "timeElapsed", elapsedTime); // Time since upload started
        nativeSetProperty(file, "timeRemaining", (bytesTotal - bytesComplete) / (bytesComplete / elapsedTime));

        return onUploadProgress(new DmUploadProgressEvent(file, (long) bytesComplete, (long) bytesTotal));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private boolean uploadErrorEventCallback(DmFile file, int errorCode, String message) {

        if (file.getStatus() == DmFile.Status.CANCELLED) {
            return true;
        }

        nativeSetProperty(getStats(), "upload_errors", getStats().getUploadErrors() + 1);

        boolean response;
        try {
            response = onUploadError(new DmUploadErrorEvent(file, errorCode, message));
        } finally {

            try {
                uploadCompleteEventCallback(file);
            } finally {
                if (requeueOnError && errorCode != DmFile.Status.CANCELLED.toInt()) {
                    addFileToQueue(file);
                }
            }
        }

        return response;
    }

    private boolean uploadSuccessEventCallback(DmFile file, String responseReceived, String serverData) {
        boolean response = false;

        if (httpSuccess == null) {
            httpSuccess = new long[]{200};
        }
        boolean success = false;
        for (long code : httpSuccess) {
            if ((code + "").equals(responseReceived)) {
                success = true;
            }
        }

        if (success) {
            try {
                if (file.getStatus() != DmFile.Status.CANCELLED) {
                    uploadProgressEventCallback(file, file.getSize(), file.getSize());

                    nativeSetProperty(getStats(), "successful_uploads", getStats().getSuccessfulUploads() + 1);

                    response = onUploadSuccess(new DmUploadSuccessEvent(file, serverData, responseReceived));
                }
            } finally {
                uploadCompleteEventCallback(file);
            }
        } else {
            uploadErrorEventCallback(file, DmUploadErrorEvent.ErrorCode.HTTP_ERROR.toInt(), (DmShared.xNull(serverData).trim().equalsIgnoreCase("") ? "Unsuccessful server response code of: " + responseReceived : serverData));
        }
        return response;
    }

    private boolean uploadCompleteEventCallback(DmFile file) {
        if (nativeFilesQueued.size() > 0) {
            final JavaScriptObject nativeFile = nativeFilesQueued.get(0);
            nativeFilesQueued.remove(nativeFile);
            nativeFilesQueuedById.remove(nativeFile.<DmFile>cast().getId());
        }

        nativeSetProperty(getStats(), "files_queued", nativeFilesQueued.size());
        nativeSetProperty(getStats(), "in_progress", 0);

        return onUploadComplete(new DmUploadCompleteEvent(file));
    }

    private static native JsArray nativeGetSelectedFiles(Element fileInputElement) /*-{
     return fileInputElement.files;
     }-*/;

    private static native JavaScriptObject nativeUpdateFileProperties(JavaScriptObject file, int fileStatus) /*-{
     file.modificationdate = file.lastModifiedDate;
     file.filestatus = fileStatus;
     return file;
     }-*/;

    private static native void nativeSetProperty(JavaScriptObject obj, String key, double value) /*-{
     obj[key] = value;
     }-*/;

    private static native void nativeSetProperty(JavaScriptObject obj, String key, String value) /*-{
     obj[key] = value;
     }-*/;

    private static native void nativeAddValueToArray(JavaScriptObject obj, String key, double value) /*-{
     if (!obj[key]) {
     obj[key] = new Array();
     }
     obj[key].push(value);
     }-*/;

    private static native double nativeGetPropertyAsDouble(JavaScriptObject obj, String key) /*-{
     return obj[key] ? obj[key] : 0.0;
     }-*/;

    private static native JsArrayNumber nativeGetPropertyAsArray(JavaScriptObject obj, String key) /*-{
     return obj[key] ? obj[key] : new Array();
     }-*/;

    private static double calculateMovingAverage(JsArrayNumber history) {
        double[] vals = new double[history.length()];
        long size = history.length();
        double sum = 0.0;
        double mSum = 0.0;
        long mCount = 0;

        if (size >= 8) {
            for (int i = 0; i < size; i++) {
                vals[i] = history.get(i);
                sum += vals[i];
            }
            double mean = sum / size;

            double varianceTemp = 0.0;
            for (int i = 0; i < size; i++) {
                varianceTemp += Math.pow((vals[i] - mean), 2);
            }
            double variance = varianceTemp / size;
            double standardDev = Math.sqrt(variance);

            for (int i = 0; i < size; i++) {
                vals[i] = (vals[i] - mean) / standardDev;
            }

            double deviationRange = 2.0;

            for (int i = 0; i < size; i++) {
                if (vals[i] <= deviationRange && vals[i] >= -deviationRange) {
                    mCount++;
                    mSum += history.get(i);
                }
            }

        } else {
            mCount = size;
            for (int i = 0; i < size; i++) {
                mSum += history.get(i);
            }
        }

        return mSum / mCount;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private native void consoleDebug(String msg) /*-{
     $wnd.console.debug(msg);
     }-*/;

}
