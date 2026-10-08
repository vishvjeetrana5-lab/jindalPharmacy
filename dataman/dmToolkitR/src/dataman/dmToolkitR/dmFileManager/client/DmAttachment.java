package dataman.dmToolkitR.dmFileManager.client;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkitR.dmFileManager.shared.DmFileManager_Srv;
import dataman.dmToolkitR.dmFileManager.shared.DmFileManager_SrvAsync;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkitR.dmAlertBox.client.DmAlertBox;
import dataman.dmToolkitR.dmConfirmBox.client.DmConfirmBox;
import dataman.dmToolkitR.dmImageCanvas.client.DmImageCanvas;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class DmAttachment extends DmAttachment_Dsg implements IsWidget, ClickHandler {
    
    interface AttachmentCallBack {
        
        public void refreshView(DmEnum.DmStatus status, DmAttachment_Mdl fileAttachment);
    }
    
    private final ServiceInvoker<DmFileManager_SrvAsync> SrvAsync;
    private DmAttachment_Mdl attachment_Mdl;
    private AttachmentCallBack fileCallBack;
    private DmImageCanvas canvasEditor;
    private HandlerRegistration imgClickHandler = null;
    private final DmFileType fileType;
    private DmMultiLineModel dmMultiLineModel = new DmMultiLineModel();
    
    public void addFileCallBack(AttachmentCallBack callBack) {
        this.fileCallBack = callBack;
    }
    
    private void showImageCanvas(DmSize imgSize) {
        try {
            if (this.canvasEditor != null) {
                this.canvasEditor.modal.hide();
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
            this.canvasEditor.modal.show();
        } catch (Exception ex) {
            new DmAlertBox(ex.getMessage(), (DmTextBox) null).show();
        }
    }
    
    private void saveEditedFile(DmFile_Mdl fileModel) {
        try {
            this.SrvAsync.invokeRPC().saveEditedFile(attachment_Mdl, fileModel, new AsyncCallback<DmResult<DmAttachment_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
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
            new DmAlertBox(ex.getMessage(), (DmTextBox) null).show();
        }
    }
    
    private void refreshAttachmentView() {
        try {
            if (this.fileCallBack != null) {
                this.fileCallBack.refreshView(DmEnum.DmStatus.OK, attachment_Mdl);
            }
        } catch (Exception ex) {
            new DmAlertBox(ex.getMessage(), (DmTextBox) null).show();
        }
    }
    
    public DmAttachment(DmAttachment_Mdl attachment_Mdl, DmFileType fileType) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(DmFileManager_Srv.class), DmFileManager.class.getName());
        this.attachment_Mdl = attachment_Mdl;
        this.fileType = fileType;
        if (DmClient.getScreenDisplay() == DmEnum.ScreenDisplay.MOBILE) {
            flwDownload.setVisible(false);
        }
        manageEvents();
        initialize();
        
    }
    
    private void manageEvents() {
        lblRemark.addClickHandler(this);
        fileActionAnchor.addClickHandler(this);
        flwRemark.addDomHandler(this, ClickEvent.getType());
        flwDelete.addDomHandler(this, ClickEvent.getType());
        flwDownload.addDomHandler(this, ClickEvent.getType());
        dmMultiLineModel.addCallBack(new DmMultiLineModel.CallBack() {
            
            @Override
            public void onOk(String textVal) {
                updateRemarks(textVal);
            }
            
        });
    }
    
    private void initialize() {
        resetControls();
        if (attachment_Mdl != null) {
            aDelete.setVisible(fileType.fileManager.canDelete);
            lblFileDateTime.setText(DmShared.xNull(attachment_Mdl.getModifyDateTime()));
            lblRemark.setText(DmShared.xNull(attachment_Mdl.getRemarks()));
            popover.setContent(DmShared.xNull(attachment_Mdl.getRemarks()));
            initiateView();
        }
    }
    
    private void setMenuData() {
        if (attachment_Mdl != null) {
            aDescription.setHTML("<div style='font-weight: bold; padding: 0px 8px; color: rgb(68, 68, 68);'>File Name</div><div style='font-size:12px; padding: 0px 8px; color: rgb(68, 68, 68);'>" + DmShared.xNull(attachment_Mdl.getFileName()) + "</div>");
            aDescription.getElement().setAttribute("style", "border-bottom: 1px solid #eee; display: block;");
            aFileSize.setHTML("<div style='font-weight: bold; padding: 0px 8px; color: rgb(68, 68, 68);'>File Size (kb)</div><div style='font-size:12px; padding: 0px 8px; color: rgb(68, 68, 68);'> " + DmShared.xNull(attachment_Mdl.getFileSize()) + "</div>");
            aFileSize.getElement().setAttribute("style", "border-bottom: 1px solid #eee; display: block;");
            aDateTime.setHTML("<div style='font-weight: bold; padding: 0px 8px; color: rgb(68, 68, 68);'>Date & Time</div><div style='font-size:12px; padding: 0px 8px; color: rgb(68, 68, 68);'> " + DmShared.xNull(attachment_Mdl.getModifyDateTime()) + "</div>");
            aDateTime.getElement().setAttribute("style", "border-bottom: 1px solid #eee; display: block;");
            aUserName.setHTML("<div style='font-weight: bold; padding: 0px 8px; color: rgb(68, 68, 68);'>User</div><div style='font-size:12px; padding: 0px 8px; color: rgb(68, 68, 68);'> " + DmShared.xNull(attachment_Mdl.getUserName()) + "</div>");
            aUserName.getElement().setAttribute("style", "border-bottom: 1px solid #eee; display: block;");
            aRemark.setText("Remark");
            aRemark.getElement().setAttribute("style", "font-weight: bold; padding: 0px 8px; color: rgb(68, 68, 68);");
            aDownload.setText("Download");
            aDownload.getElement().setAttribute("style", "font-weight: bold; padding: 0px 8px; color: rgb(68, 68, 68);");
            aDelete.setText("Delete");
            aDelete.getElement().setAttribute("style", "font-weight: bold; padding: 0px 8px; color: rgb(68, 68, 68);");
        }
        
    }
    
    @Override
    public Widget asWidget() {
        return fileDetailFlw;
    }
    
    private void resetControls() {
        lblFileDateTime.setText("");
        lblRemark.setText("");
        popover.setContent("");
        imgView.setUrl("");
    }
    
    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(lblRemark) || event.getSource().equals(flwRemark)) {
            dmMultiLineModel.show(lblRemark.getText(), "Remark", 50);
            
        } else if (event.getSource() == fileActionAnchor) {
            setMenuData();
            
        } else if (event.getSource() == flwDownload) {
            initiateDownload();
        } else if (event.getSource() == flwDelete) {
            confirmDelete();
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
                new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
            }
            
            @Override
            public void onSuccess(DmResult<List<DmAttachment_Mdl>> result) {
                if (result.isStatus()) {
                    attachment_Mdl = result.getData().get(0);
                    SrvAsync.invokeRPC().getActualImageSize(attachment_Mdl, new AsyncCallback<DmResult<DmSize>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
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
        
        urlString = DmClient.concatURL("dataman/dmToolkitR/dmFileServlets/DmImageFileThumbnail_Srvlt", params);
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
            Window.open(DmClient.concatURL("dataman/dmToolkitR/dmFileServlets/DmFileDownload_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }
    
    private void onDelete() {
        if (fileType.fileManager.canDelete) {
            SrvAsync.invokeRPC(true).deleteFile(attachment_Mdl, new AsyncCallback<DmResult<Boolean>>() {
                
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
                }
                
                @Override
                public void onSuccess(DmResult<Boolean> result) {
                    if (result.isStatus()) {
                        DmAttachment.this.fileDetailFlw.removeFromParent();
                        fileCallBack.refreshView(DmEnum.DmStatus.DELETE, attachment_Mdl);
                    } else {
                        new DmAlertBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                    }
                }
            });
        }
    }
    
    private void updateRemarks(String remarks) {
        SrvAsync.invokeRPC(true).updateRemarks(attachment_Mdl, remarks, new AsyncCallback<DmResult<String>>() {
            
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
            }
            
            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    lblRemark.setText(DmShared.xNull(result.getData()));
                    popover.setContent(DmShared.xNull(attachment_Mdl.getRemarks()));
                    attachment_Mdl.setRemarks(DmShared.xNull(result.getData()));
                } else {
                    new DmAlertBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
    
    private void confirmDelete() {
        if (fileType.fileManager.canDelete) {
            
            new DmConfirmBox("Are you sure? You want to delete this attachment.").addCallBack(new DmConfirmBox.CallBack() {
                @Override
                public void onAction(DmEnum.DmYesNo dmYesNo) {
                    if (dmYesNo == DmEnum.DmYesNo.YES) {
                        onDelete();
                    }
                }
            });
        }
    }
}
