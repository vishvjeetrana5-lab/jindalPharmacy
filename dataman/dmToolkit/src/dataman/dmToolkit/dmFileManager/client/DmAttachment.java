package dataman.dmToolkit.dmFileManager.client;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmFileManager.shared.DmFileManager_Srv;
import dataman.dmToolkit.dmFileManager.shared.DmFileManager_SrvAsync;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmToolkit.dmImageCanvas.client.DmImageCanvas;
import dataman.dmToolkit.dmInputBox.client.DmInputBox;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class DmAttachment extends DmAttachment_Dsg implements IsWidget, ClickHandler {

    interface AttachmentCallBack {

        public void refreshView(DmEnum.DmStatus status, DmAttachment_Mdl fileAttachment);
    }

    private final ServiceInvoker<DmFileManager_SrvAsync> SrvAsync;
    private DmAttachment_Mdl attachment_Mdl;
    private final DmInputBox inputBox = new DmInputBox();
    private AttachmentCallBack fileCallBack;
    private DmImageCanvas canvasEditor;
    private HandlerRegistration imgClickHandler = null;
    private final DmFileType fileType;

    public void addFileCallBack(AttachmentCallBack callBack) {
        this.fileCallBack = callBack;
    }

    private void showImageCanvas(DmSize imgSize) {
        try {
            if (this.canvasEditor != null) {
                this.canvasEditor.hide(true);
                this.canvasEditor = null;
            }

            String tempUrl = createViewUrl(imgSize.getOriginalWidth(), imgSize.getOriginalHeight());
            Image tempImage = new Image();
            tempImage.setUrl(tempUrl);

            this.canvasEditor = new DmImageCanvas(tempImage, attachment_Mdl.getRuleCode(), attachment_Mdl.getFileLink());
            this.canvasEditor.setFileSavedCallback(new DmImageCanvas.ImageCanvasFileSaveCallback() {
                @Override
                public void fileSaved(DmFile_Mdl fileModel) {
                    saveEditedFile(fileModel);
                }
            });
            this.canvasEditor.show();
            this.canvasEditor.center();
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    private void saveEditedFile(DmFile_Mdl fileModel) {
        try {
            this.SrvAsync.invokeRPC().saveEditedFile(attachment_Mdl, fileModel, new AsyncCallback<DmResult<DmAttachment_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
                }

                @Override
                public void onSuccess(DmResult<DmAttachment_Mdl> result) {
                    if (result.isStatus()) {
                        attachment_Mdl = result.getData();
                        refreshAttachmentView();
                    }
                }
            });
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    private void refreshAttachmentView() {
        try {
            if (this.fileCallBack != null) {
                this.fileCallBack.refreshView(DmEnum.DmStatus.OK, attachment_Mdl);
            }
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    private void textHelpEvents() {
        inputBox.addCallBack(new DmInputBox.CallBack() {

            @Override
            public void onOk(String inputValue, String parentName) {
                updateRemarks(inputValue);
            }
        });
    }

    public DmAttachment(DmAttachment_Mdl attachment_Mdl, DmFileType fileType) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(DmFileManager_Srv.class), DmFileManager.class.getName());
        this.attachment_Mdl = attachment_Mdl;
        this.fileType = fileType;
        textHelpEvents();
        manageEvents();
        initialize();
    }

    private void manageEvents() {
        fileActionAnchor.addClickHandler(this);
        lblRemark.addClickHandler(this);
        lblFileDateTime.addClickHandler(this);
    }

    private void initialize() {
        resetControls();
        if (attachment_Mdl != null) {
            mnuDelete.setVisible(fileType.fileManager.canDelete);
            lblFileDateTime.setText(DmShared.xNull(attachment_Mdl.getModifyDateTime()) + "\n" + DmShared.xNull(attachment_Mdl.getFileName()));
            lblRemark.setText(DmShared.xNull(attachment_Mdl.getRemarks()));
            initiateView();
        }
    }

    private void setMenuData() {
        if (attachment_Mdl != null) {
            mnuDescription.setText("File Name : " + DmShared.xNull(attachment_Mdl.getFileName()));
            mnuFileSize.setText("File Size (kb) : " + DmShared.xNull(attachment_Mdl.getFileSize()));
            mnuDateTime.setText("Date & Time : " + DmShared.xNull(attachment_Mdl.getModifyDateTime()));
            mnuUserName.setText("User : " + DmShared.xNull(attachment_Mdl.getUserName()));
        }
    }

    @Override
    public Widget asWidget() {
        return fileDetailFlw;
    }

    private void resetControls() {
        lblFileDateTime.setText("");
        lblRemark.setText("");
        imgView.setUrl("");
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(lblRemark)) {
            inputBox.show(lblRemark, "Remark", lblRemark.getText(), 250, "Please enter remark:", "380px");
        } else if (event.getSource() == fileActionAnchor || (event.getSource() == lblFileDateTime)) {
            setMenuData();
            menu.show(lblFileDateTime);
        }
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuDelete.getText())) {
            if (fileType.fileManager.canDelete) {
                new DmConfirmMessageBox("Are you sure? You want to delete this attachment.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            onDelete();
                        }
                    }
                }).show();
            }

        } else if (event.getSelectedItem().getText().equalsIgnoreCase(mnuDownload.getText())) {
            initiateDownload();
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(mnuRemark.getText())) {
            inputBox.show(lblRemark, "Remark", lblRemark.getText(), 250, "Please enter remark:", "380px");
        }
    }

    private void allocateResource(final boolean calledForDownload) {

        List<DmAttachment_Mdl> listAttachments = new ArrayList<>();
        listAttachments.add(attachment_Mdl);

        SrvAsync.invokeRPC().allocateResource(listAttachments, new AsyncCallback<DmResult<List<DmAttachment_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<List<DmAttachment_Mdl>> result) {
                if (result.isStatus()) {
                    attachment_Mdl = result.getData().get(0);

                    if (!calledForDownload) {
                        startViewing();
                    } else {
                        startDownloading();
                    }
                }
            }
        });
    }

    private void initiateView() {
        allocateResource(false);
    }

    private void startViewing() {
        try {
            flwImage.clear();
            flwImage.add(imgView);

            if (this.imgClickHandler != null) {
                this.imgClickHandler.removeHandler();
            }

            this.imgClickHandler = imgView.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    prepareImageCanvas();
                }
            });

            imgView.setUrl(createViewUrl(140, 125));

            if (attachment_Mdl != null) {
                DmAttachment_Mdl fileModel = attachment_Mdl;

                //String selExt = attachment_Mdl.getFileName().substring(attachment_Mdl.getFileName().lastIndexOf(".") + 1);
                String selExt = fileModel.getFileName().substring(fileModel.getFileName().lastIndexOf(".") + 1);
                flwImageDisplay.clear();
                if (DmShared.xNull(selExt).trim().equalsIgnoreCase("pdf")) {
                    flwImageDisplay.addStyleName("fa fa-file-pdf-o");
                    flwImageDisplay.getElement().setAttribute("style", flwImageDisplay.getElement().getAttribute("style")
                            + ";font-size:100px; margin-left:25px; margin-top:15px; color: #ddd;");
                    flwImage.clear();
                    flwImage.add(flwImageDisplay);
                } else if (DmShared.xNull(selExt).trim().equalsIgnoreCase("doc") || DmShared.xNull(selExt).trim().equalsIgnoreCase("docx")) {
                    flwImageDisplay.addStyleName("fa fa-file-word-o");
                    flwImageDisplay.getElement().setAttribute("style", flwImageDisplay.getElement().getAttribute("style")
                            + ";font-size:100px; margin-left:25px; margin-top:15px; color: #ddd;");
                    flwImage.clear();
                    flwImage.add(flwImageDisplay);
                } else if (DmShared.xNull(selExt).trim().equalsIgnoreCase("xlsx") || DmShared.xNull(selExt).trim().equalsIgnoreCase("xls")
                        || DmShared.xNull(selExt).trim().equalsIgnoreCase("csv")) {
                    flwImageDisplay.addStyleName("fa fa-file-excel-o");
                    flwImageDisplay.getElement().setAttribute("style", flwImageDisplay.getElement().getAttribute("style")
                            + ";font-size:100px; margin-left:25px; margin-top:15px; color: #ddd;");
                    flwImage.clear();
                    flwImage.add(flwImageDisplay);
                } else if (DmShared.xNull(selExt).trim().equalsIgnoreCase("xlsx") || DmShared.xNull(selExt).trim().equalsIgnoreCase("xls")
                        || DmShared.xNull(selExt).trim().equalsIgnoreCase("csv")) {
                    flwImageDisplay.addStyleName("fa fa-file-excel-o");
                    flwImageDisplay.getElement().setAttribute("style", flwImageDisplay.getElement().getAttribute("style")
                            + ";font-size:100px; margin-left:25px; margin-top:15px; color: #ddd;");
                    flwImage.clear();
                    flwImage.add(flwImageDisplay);
                } else if (DmShared.xNull(selExt).trim().equalsIgnoreCase("7z") || DmShared.xNull(selExt).trim().equalsIgnoreCase("rar")
                        || DmShared.xNull(selExt).trim().equalsIgnoreCase("zip") || DmShared.xNull(selExt).trim().equalsIgnoreCase("z")) {
                    flwImageDisplay.addStyleName("fa fa-file-archive-o");
                    flwImageDisplay.getElement().setAttribute("style", flwImageDisplay.getElement().getAttribute("style")
                            + ";font-size:100px; margin-left:25px; margin-top:15px; color: #ddd;");
                    flwImage.clear();
                    flwImage.add(flwImageDisplay);
                } else if (DmShared.xNull(selExt).trim().equalsIgnoreCase("txt")) {
                    flwImageDisplay.addStyleName("fa fa-file-text-o");
                    flwImageDisplay.getElement().setAttribute("style", flwImageDisplay.getElement().getAttribute("style")
                            + ";font-size:100px; margin-left:25px; margin-top:15px; color: #ddd;");
                    flwImage.clear();
                    flwImage.add(flwImageDisplay);
                }
            }
        } catch (Exception ex) {
        }
    }

    private void prepareImageCanvas() {

        final List<DmAttachment_Mdl> attachments = new ArrayList<>();
        attachments.add(attachment_Mdl);
        this.SrvAsync.invokeRPC().allocateResource(attachments, new AsyncCallback<DmResult<List<DmAttachment_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<DmAttachment_Mdl>> result) {
                if (result.isStatus()) {
                    attachment_Mdl = result.getData().get(0);
                    SrvAsync.invokeRPC().getActualImageSize(attachment_Mdl, new AsyncCallback<DmResult<DmSize>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
                        }

                        @Override
                        public void onSuccess(DmResult<DmSize> result) {
                            if (result.isStatus()) {
                                showImageCanvas(result.getData());
                            }
                        }
                    });
                }
            }
        });
    }

    private String createViewUrl(int width, int height) {
        String urlString = "";
        HashMap<String, String> params = new HashMap<>();
        params.put("u", DmRequestBuilder.getSessionId());
        params.put("r", attachment_Mdl.getResourceAlocationId());

        params.put("w", ((width <= 0) ? "100" : String.valueOf(width)));
        params.put("h", ((height <= 0) ? "100" : String.valueOf(height)));

        urlString = DmClient.concatURL("dataman/dmToolkit/dmFileServlets/DmImageFileThumbnail_Srvlt", params);
        return urlString.trim();
    }

    private void initiateDownload() {
        try {
            allocateResource(true);
        } catch (Exception ex) {
        }
    }

    private void startDownloading() {
        try {
            flwImage.clear();
            flwImage.add(imgView);

            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("r", attachment_Mdl.getResourceAlocationId());
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileServlets/DmFileDownload_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    private void onDelete() {
        if (fileType.fileManager.canDelete) {
            SrvAsync.invokeRPC(true).deleteFile(attachment_Mdl, new AsyncCallback<DmResult<Boolean>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<Boolean> result) {
                    if (result.isStatus()) {
                        DmAttachment.this.fileDetailFlw.removeFromParent();
                        fileCallBack.refreshView(DmEnum.DmStatus.DELETE, attachment_Mdl);
                    } else {
                        new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                    }
                }
            });
        }
    }

    private void updateRemarks(String remarks) {
        SrvAsync.invokeRPC(true).updateRemarks(attachment_Mdl, remarks, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    lblRemark.setText(DmShared.xNull(result.getData()));
                    attachment_Mdl.setRemarks(DmShared.xNull(result.getData()));
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
}
