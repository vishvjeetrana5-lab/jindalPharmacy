package dataman.dmWebCamera.self.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent.BeforeHideHandler;
import dataman.dmBase.self.shared.IWebCamera;

public class DmWebCamera extends Window implements UserMediaHolder.IWebcamAvailableCallback, IWebCamera {

    public interface CallBack {

        public void getImage(String imgData);
    }

    private CallBack callBack;

    private UserMediaHolder userMediaHolder;
    private final VerticalPanel flwPanel = new VerticalPanel();
    private int width = 100, height = 100;
    private HandlerRegistration resizeHandler = null;
    private final FlowPanel flwCapture = new FlowPanel();
    private final Button btnCapture = new Button("Capture");
    private final FlowPanel flwCaptureClose = new FlowPanel();
    private final Button btnCaptureClose = new Button("Capture & Close");
    private final FlowPanel flwClose = new FlowPanel();
    private final Button btnClose = new Button("Close");

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public DmWebCamera() {
        setHeadingText("Web Camera Window");
        add(this.flwPanel);
        startWebCamera();
        manageEvents();
    }

    private void manageEvents() {
        this.addBeforeHideHandler(new BeforeHideHandler() {

            @Override
            public void onBeforeHide(BeforeHideEvent event) {
                stopWebCamera();
            }
        });

        btnCapture.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (callBack != null) {
                    callBack.getImage(DmWebCamera.this.userMediaHolder.getInstantaneousImage());
                }
            }
        });
        btnCaptureClose.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (callBack != null) {
                    callBack.getImage(DmWebCamera.this.userMediaHolder.getInstantaneousImage());
                }
                DmWebCamera.this.hide();
            }
        });
        btnClose.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DmWebCamera.this.hide();
            }
        });
    }

    public void startWebCamera() {
        stopWebCamera();
        this.userMediaHolder = new UserMediaHolder(this);
        this.userMediaHolder.initialize();
    }

    private void stopWebCamera() {
        destroyMediaHolder();
        this.flwPanel.clear();
    }

    @Override
    public String captureImage() {
        String imgData = this.userMediaHolder.getInstantaneousImage();
        return imgData;
    }

    private void destroyMediaHolder() {
        if (this.userMediaHolder != null) {
            this.userMediaHolder.destroyResources();
        }
        this.userMediaHolder = null;
    }

    @Override
    public void onWebCamAvailability() {
        if (resizeHandler != null) {
            resizeHandler.removeHandler();
            resizeHandler = null;
        }
        this.flwPanel.clear();
        if (this.userMediaHolder != null) {
            this.userMediaHolder.getVideoWidget().getElement().getStyle().setWidth(width, Style.Unit.PX);
            this.userMediaHolder.getVideoWidget().getElement().getStyle().setHeight(height, Style.Unit.PX);
            this.userMediaHolder.getVideoWidget().getElement().getStyle().setPosition(Style.Position.RELATIVE);
            this.flwPanel.add(this.userMediaHolder.getVideoWidget());
            this.flwPanel.add(addButtonSection());
            this.flwPanel.setCellHeight(this.userMediaHolder.getVideoWidget(), String.valueOf(this.height));
            this.flwPanel.setCellWidth(this.userMediaHolder.getVideoWidget(), String.valueOf(this.width));
            resizeHandler = this.addResizeHandler(new ResizeHandler() {
                @Override
                public void onResize(ResizeEvent event) {
                    userMediaHolder.getVideoWidget().getElement().getStyle().setWidth(event.getWidth(), Style.Unit.PX);
                    userMediaHolder.getVideoWidget().getElement().getStyle().setHeight(event.getHeight(), Style.Unit.PX);
                }
            });
        }
    }

    @Override
    public void setSize(String width, String height) {
        if (width == null) {
            width = "200";
        }
        if (height == null) {
            height = "150";
        }
        width = width.trim().toLowerCase().replace("px", "");
        height = height.trim().toLowerCase().replace("px", "");

        super.setSize(width, height);
        if (this.flwPanel != null) {
            this.flwPanel.setSize(width + "px", height + "px");
        }
        this.width = Integer.parseInt(width);
        this.height = Integer.parseInt(height);
    }

    private FlowPanel addButtonSection() {
        FlowPanel flowPanel = new FlowPanel();

        flowPanel.getElement().setAttribute("style", "width:100%; position:absolute; bottom:0;");

        flwCapture.clear();
        flwCapture.setWidth("33.33%");
        flwCapture.getElement().getStyle().setFloat(Style.Float.LEFT);
        btnCapture.setTitle("Capture");
        btnCapture.addStyleName("dm-button-grad");
        btnCapture.setWidth("100%");
        btnCapture.setHeight("30px");
        btnCapture.getElement().setAttribute("style", btnCapture.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        flwCapture.add(btnCapture);
        flowPanel.add(flwCapture);

        flwCaptureClose.clear();
        flwCaptureClose.setWidth("33.33%");
        flwCaptureClose.getElement().getStyle().setFloat(Style.Float.LEFT);
        btnCaptureClose.setTitle("Capture & Close");
        btnCaptureClose.addStyleName("dm-button-grad");
        btnCaptureClose.setWidth("100%");
        btnCaptureClose.setHeight("30px");
        btnCaptureClose.getElement().setAttribute("style", btnCaptureClose.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        flwCaptureClose.add(btnCaptureClose);
        flowPanel.add(flwCaptureClose);

        flwClose.clear();
        flwClose.setWidth("33.33%");
        flwClose.getElement().getStyle().setFloat(Style.Float.LEFT);
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-button-grad");
        btnClose.setWidth("100%");
        btnClose.setHeight("30px");
        btnClose.getElement().setAttribute("style", btnClose.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        flwClose.add(btnClose);
        flowPanel.add(flwClose);

        return flowPanel;
    }
}
