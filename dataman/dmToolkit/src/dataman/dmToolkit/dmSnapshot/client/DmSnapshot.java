package dataman.dmToolkit.dmSnapshot.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmToolkit.dmSnapshot.shared.DmSnapshot_Srv;
import dataman.dmToolkit.dmSnapshot.shared.DmSnapshot_SrvAsync;
import java.util.HashMap;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmSnapshot.shared.DmSnapshot_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSize;
import dataman.dmBase.self.shared.ServiceInvoker;

public class DmSnapshot extends Composite implements IFileUploadNotifier, ClickHandler {

    public interface CallBack {

        public void onCaptureImage();
    }

    private CallBack callBack;
    private final VerticalPanel vrtPnl = new VerticalPanel();
    private final FlowPanel flwCamWidget = new FlowPanel();
    private final FlowPanel flwForm = new FlowPanel();
    private Button cmdCaptureImage;
    private String imgWidth, imgHeight;
    private String tempURL = "";
    private Image imgView = new Image();
    private DmFileUploadButton btnUpload;
    private Button btnDelete, btnDownload;
    private Anchor camBtn, aBtn, dltBtn, dwnldBtn;
    ServiceInvoker<DmSnapshot_SrvAsync> SrvAsync;
    DmFileRuleType fileRuleType;
    DmSnapshot_Mdl snapshot_Mdl = null;
    private int btnPanelHeight = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public DmSnapshot(int width, int height, DmFileRuleType fileRuleType, boolean isToolBoxNeeded) {
        startUp(width, height, fileRuleType, isToolBoxNeeded);
    }

    public DmSnapshot(int width, int height, DmFileRuleType fileRuleType) {
        startUp(width, height, fileRuleType, true);
    }

    private void startUp(int width, int height, DmFileRuleType fileRuleType, boolean isToolBoxNeeded) {
        SrvAsync = new ServiceInvoker<>(GWT.create(DmSnapshot_Srv.class), DmSnapshot.class.getName());
        if (isToolBoxNeeded) {
            btnPanelHeight = 25;
        }
        this.fileRuleType = fileRuleType;
        this.flwForm.setSize(String.valueOf(width) + "px", String.valueOf(height) + "px");
        this.imgWidth = String.valueOf(width);
        this.imgHeight = String.valueOf(height - btnPanelHeight);
        this.flwCamWidget.setSize(String.valueOf(width) + "px", imgHeight + "px");
        ini();
        initWidget(flwForm);
    }

    private void ini() {
        FlowPanel flwBtn = new FlowPanel();
        DmClient.setStyle(vrtPnl, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwCamWidget.getElement().setAttribute("style", flwCamWidget.getElement().getAttribute("style")
                + ";border-bottom:1px solid gray;");
        vrtPnl.add(flwCamWidget);
        vrtPnl.setCellHeight(flwCamWidget, imgHeight);

        if (btnPanelHeight > 0) {
            DmClient.setStyle(flwBtn, "100%", btnPanelHeight + "px", "left", null, DmEnum.DmPosition.NONE, true);
            flwBtn.add(btnArea());
            vrtPnl.add(flwBtn);
        }

        vrtPnl.getElement().setAttribute("style", vrtPnl.getElement().getAttribute("style")
                + ";border-left:1px solid gray;border-right:1px solid gray;border-top:1px solid gray;");
        flwForm.add(vrtPnl);
    }

    private FlowPanel btnArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", btnPanelHeight + "px", "left", null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "100%", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", "100%", "left", null, DmEnum.DmPosition.NONE, true);
        this.cmdCaptureImage = new Button("");
        this.camBtn = new Anchor();
        this.camBtn.addStyleName("fa fa-camera");
        this.camBtn.getElement().getStyle().setColor("black");
        this.camBtn.setTitle("Camera");
        this.camBtn.getElement().setAttribute("style", camBtn.getElement().getAttribute("style")
                + "padding:0px;");
        this.cmdCaptureImage.getElement().appendChild(camBtn.getElement());
        this.cmdCaptureImage.getElement().setAttribute("style", cmdCaptureImage.getElement().getAttribute("style")
                + "width:100%;border-left:none;");
        this.cmdCaptureImage.addClickHandler(this);
        flwTd.add(this.cmdCaptureImage);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", "100%", "left", null, DmEnum.DmPosition.NONE, true);
        this.btnUpload = new DmFileUploadButton(this, fileRuleType);
        this.imgView.setWidth(imgWidth);
        this.imgView.setHeight(imgHeight);
        this.imgView.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        this.aBtn = new Anchor();
        this.aBtn.setTitle("Attachment");
        this.aBtn.addStyleName("fa fa-paperclip");
        this.aBtn.getElement().setAttribute("style", aBtn.getElement().getAttribute("style")
                + "padding:0px;");
        this.aBtn.getElement().getStyle().setColor("black");
        this.btnUpload.getButton().getElement().appendChild(aBtn.getElement());
        this.btnUpload.getButton().getElement().getStyle().setWidth(100, Style.Unit.PCT);
        flwTd.add(this.btnUpload);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", "100%", "left", null, DmEnum.DmPosition.NONE, true);
        this.btnDelete = new Button();
        this.dltBtn = new Anchor();
        this.dltBtn.addStyleName("fa fa-times");
        this.dltBtn.setTitle("remove");
        this.dltBtn.getElement().getStyle().setColor("red");
        this.dltBtn.getElement().setAttribute("style", dltBtn.getElement().getAttribute("style")
                + "padding:0px;");
        this.btnDelete.getElement().appendChild(dltBtn.getElement());
        this.btnDelete.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        this.btnDelete.addClickHandler(this);
        flwTd.add(this.btnDelete);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, true);
        this.btnDownload = new Button();
        this.dwnldBtn = new Anchor();
        this.dwnldBtn.setTitle("Download");
        this.dwnldBtn.addStyleName("fa fa-download");
        this.dwnldBtn.getElement().setAttribute("style", dwnldBtn.getElement().getAttribute("style")
                + "width:100%;border-right:none;position: relative;top: 1px;");
        this.dwnldBtn.getElement().getStyle().setColor("Green");
        this.aBtn.getElement().setAttribute("style", aBtn.getElement().getAttribute("style")
                + "padding:0px;");
        this.btnDownload.getElement().appendChild(dwnldBtn.getElement());
        this.btnDownload.getElement().setAttribute("style", btnDownload.getElement().getAttribute("style")
                + "width:100%;border-right:none;");
        this.btnDownload.addClickHandler(this);
        flwTd.add(this.btnDownload);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(cmdCaptureImage)) {
                showCamera();

            } else if (event.getSource().equals(btnDelete)) {
                this.snapshot_Mdl = null;
                viewImage();

            } else if (event.getSource().equals(btnDownload)) {
                startDownloading();
            }
        } catch (DmException e) {
        }
    }

    @Override
    public void notifyForUpload(String notifier) {
    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        snapshot_Mdl = new DmSnapshot_Mdl();
        if (DmShared.xNull(file.getFileName()).equalsIgnoreCase("")) {
            file.setFileName(file.getServerFileName());
        }
        snapshot_Mdl.setFile((DmFile_Mdl) file);
        if (callBack != null) {
            callBack.onCaptureImage();
        }
        viewImage();
    }

    public void setSnapshot(DmSnapshot_Mdl snapshot_Mdl) {
        this.snapshot_Mdl = snapshot_Mdl;
        viewImage();
    }

    public DmSnapshot_Mdl getSnapshot() {
        return this.snapshot_Mdl;
    }

    private void viewImage() {
        flwCamWidget.clear();

        this.SrvAsync.invokeRPC().getImageSize(Integer.parseInt(imgWidth), Integer.parseInt(imgHeight), fileRuleType.getRuleCode(), snapshot_Mdl, new AsyncCallback<DmResult<DmSize>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<DmSize> result) {
                if (result.isStatus()) {
                    resetImageSizeAndShow(result.getData());
                }
            }
        });
    }

    private void resetImageSizeAndShow(DmSize size) {
        int top = 0, left = 0;

        if (size.getChangedWidth() > size.getChangedHeight()) {
            top = (int) Math.floor((Double.parseDouble(imgHeight) - (double) size.getChangedHeight()) / 2d);
        } else if (size.getChangedWidth() <= size.getChangedHeight()) {
            left = (int) Math.floor((Double.parseDouble(imgWidth) - (double) size.getChangedWidth()) / 2d);
        }

        imgView.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        imgView.getElement().getStyle().setTop(top, Style.Unit.PX);
        imgView.getElement().getStyle().setLeft(left, Style.Unit.PX);
        imgView.getElement().getStyle().setHeight(size.getChangedHeight(), Style.Unit.PX);
        imgView.getElement().getStyle().setWidth(size.getChangedWidth(), Style.Unit.PX);
        flwCamWidget.add(imgView);
        imgView.setUrl("");

        if (snapshot_Mdl != null) {
            tempURL = tempURL + "A";
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            if (snapshot_Mdl.getFile() != null) {
                params.put("f", snapshot_Mdl.getFile().getServerFileName());
                params.put("p", snapshot_Mdl.getFile().getServerDirectory());
            }
            params.put("m", snapshot_Mdl.getMongoFileLink());
            params.put("r", fileRuleType.getRuleCode());
            params.put("temp", tempURL);
            params.put("w", imgWidth);
            params.put("h", imgHeight);
            imgView.setUrl(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileViewer_Srvlt", params));
        }
    }

    protected void getSaveCaptureImage(String image) {
        SrvAsync.invokeRPC().uploadSnapshot(image, new AsyncCallback<DmResult<DmIFile_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DmIFile_Mdl> result) {
                if (result.isStatus()) {
                    processAfterUploaded(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void startDownloading() {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());

            if (snapshot_Mdl.getFile() != null) {
                params.put("f", snapshot_Mdl.getFile().getServerFileName());
                params.put("p", snapshot_Mdl.getFile().getServerDirectory());
            }

            params.put("m", snapshot_Mdl.getMongoFileLink());
            params.put("r", fileRuleType.getRuleCode());
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    private void showCamera() {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess() {
                new DmShowCamera().showCamera(DmSnapshot.this);
            }
        });
    }

}
