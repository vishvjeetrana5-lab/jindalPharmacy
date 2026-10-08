package dataman.dmToolkit.dmImageCanvas.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmColorPicker.client.Color;
import dataman.dmToolkit.dmColorPicker.client.ColorPicker;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmToolkit.dmImageCanvas.shared.DmImageCanvas_Srv;
import dataman.dmToolkit.dmImageCanvas.shared.DmImageCanvas_SrvAsync;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSize;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abhishek
 */
public class DmImageCanvas extends PopupPanel implements MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseOutHandler {

    private enum DrawMode {
        FreeHand,
        Textual;
    }

    public interface ImageCanvasFileSaveCallback {

        public void fileSaved(DmFile_Mdl fileModel);
    }

    private Image img;

    private FlowPanel flwCanvas;
    private FlowPanel btnPanel;

    private Canvas canvasImage;

    private ImageTextPopup textBoxPopup;
    private static final String textFontFamilyName = "Tahoma";

    private HandlerRegistration mouseDownRegistration, mouseUpRegistration, mouseMoveRegistration, mouseOutRegistration;

    private boolean draggingFlag = false;
    private boolean dotFlag = false;

    private VerticalPanel mainPanel;

    private Button cmdSave, cmdCancel, cmdEraser, cmdColor, cmdBrush, cmdText, cmdUndo;
    private Anchor anSave, anCancel, anEraser, anColor, anBrush, anText, anUndo;

    private HandlerRegistration canvasClickHandler;
    private HandlerRegistration textBoxApplyClickHandler;
    private HandlerRegistration textBoxHideClickHandler;

    private List<String> imageHistoryList;

    private final int buttonHeight = 25;
    private int brushWidth = 2;
    private final int popupHeightInPCT = 95;
    private final int popupWidthInPCT = 95;
    private int thisWidth, thisHeight;

    private int imgWidth = -1;
    private int imgHeight = -1;

    private int canvasWidth, canvasHeight;

    private int prevClientX = -1;
    private int prevClientY = -1;
    private int currClientX = -1;
    private int currClientY = -1;

    private ColorPicker colorPicker;
    private PopupPanel popupColor;
    private Color strokeColor;

    private DmFile_Mdl fileModel;
    private String ruleCode;
    private String mongoFileLink;

    private DrawMode drawMode;

    private ImageCanvasFileSaveCallback fileSavedCallBack;

    private ServiceInvoker<DmImageCanvas_SrvAsync> srvInvoker;

    public DmImageCanvas(Image imageWidget, String ruleCode, String mongoFileId) throws Exception {
        super(false, true);

        this.drawMode = DrawMode.FreeHand;

        this.imageHistoryList = new ArrayList<>();
        this.srvInvoker = new ServiceInvoker<>(GWT.create(DmImageCanvas_Srv.class), DmImageCanvas.class.getName());

        thisWidth = (int) Math.floor(Document.get().getClientWidth() * (popupWidthInPCT / 100d));
        thisHeight = (int) Math.floor(Document.get().getClientHeight() * (popupHeightInPCT / 100d));

        this.ruleCode = ruleCode.trim();
        this.mongoFileLink = mongoFileId.trim();
        this.img = imageWidget;

        this.srvInvoker.invokeRPC().getAspectImageSize(ruleCode, mongoFileLink, thisWidth, (thisHeight - (buttonHeight + 7)), new AsyncCallback<DmResult<DmSize>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<DmSize> result) {
                if (result.isStatus()) {
                    DmSize size = result.getData();
                    canvasWidth = size.getChangedWidth();
                    canvasHeight = size.getChangedHeight();
                    imgWidth = size.getOriginalWidth();
                    imgHeight = size.getOriginalHeight();

                    initializeControl();
                }
            }
        });

        this.getElement().getStyle().setWidth(thisWidth, Style.Unit.PX);
        this.getElement().getStyle().setHeight(thisHeight, Style.Unit.PX);
    }

    private void initializeControl() {
        if (!Canvas.isSupported()) {
            new DmAlertMessageBox("Canvas is not supported in the Host Browser.", (Button) null).show();
        } else {
            init();
            setWidget(this.mainPanel);
        }
    }

    public void setMongoFileLinkId(String mongoFileId) {
        this.mongoFileLink = mongoFileId.trim();
    }

    public void setMongoRuleCode(String ruleCode) {
        this.ruleCode = ruleCode.trim();
    }

    public String getMongoFileLinkId() {
        return this.mongoFileLink.trim();
    }

    public String getMongoRuleCode() {
        return this.ruleCode.trim();
    }

    private void createColorPickerPanel() {
        this.popupColor = new PopupPanel(false, true);
        this.colorPicker = new ColorPicker();
        colorPicker.addCloseCallback(new ColorPicker.CloseCallback() {
            @Override
            public void close(int statusOfClosing) {
                closeColorPopup(statusOfClosing);
            }
        });
        this.popupColor.setWidget(colorPicker);
    }

    private void init() {
        try {

            Color color = new Color();
            color.setRGB(0, 0, 0);
            this.strokeColor = color;
            createColorPickerPanel();
            this.colorPicker.setRGB(this.strokeColor.getRed(), this.strokeColor.getGreen(), this.strokeColor.getBlue());

            prepareCanvas();

            this.mainPanel = new VerticalPanel();

            this.btnPanel = createButtonPanel();
            this.img.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
            this.img.getElement().getStyle().setWidth(0, Style.Unit.PX);
            this.img.getElement().getStyle().setHeight(0, Style.Unit.PX);
            btnPanel.add(this.img);

            this.img.addLoadHandler(new LoadHandler() {
                @Override
                public void onLoad(LoadEvent event) {
                    clearCanvas();
                }
            });

            this.mainPanel.add(btnPanel);

            setMainPanelDisplay(btnPanel);

            this.getContainerElement().getStyle().setHeight(100, Style.Unit.PCT);

        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    public void setFileSavedCallback(ImageCanvasFileSaveCallback callback) {
        this.fileSavedCallBack = callback;
    }

    private void setMainPanelDisplay(FlowPanel btnPanel) throws Exception {
        this.mainPanel.add(this.flwCanvas);

        this.mainPanel.setCellHeight(btnPanel, buttonHeight + "px");
        this.mainPanel.setCellHeight(this.canvasImage, "100%");

        this.mainPanel.setCellVerticalAlignment(this.flwCanvas, HasVerticalAlignment.ALIGN_MIDDLE);
        this.mainPanel.setCellHorizontalAlignment(this.flwCanvas, HasHorizontalAlignment.ALIGN_CENTER);

        this.mainPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        this.mainPanel.getElement().getStyle().setHeight(100, Style.Unit.PCT);
    }

    private void changeCanvasCursor() {
        if (drawMode == DrawMode.FreeHand) {
            this.canvasImage.getElement().getStyle().setCursor(Style.Cursor.CROSSHAIR);
        } else if (drawMode == DrawMode.Textual) {
            this.canvasImage.getElement().getStyle().setCursor(Style.Cursor.TEXT);
        }
    }

    private void prepareCanvas() throws Exception {
        this.canvasImage = Canvas.createIfSupported();

        this.canvasImage.setCoordinateSpaceWidth(canvasWidth);
        this.canvasImage.setCoordinateSpaceHeight(canvasHeight);
        this.canvasImage.setPixelSize(canvasWidth, canvasHeight);
        changeCanvasCursor();
        this.canvasImage.getElement().getStyle().setCursor(Style.Cursor.CROSSHAIR);
        this.canvasImage.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        this.canvasImage.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        this.canvasImage.getElement().getStyle().setBorderColor("black");

        if (this.canvasClickHandler != null) {
            this.canvasClickHandler.removeHandler();
        }

        this.canvasClickHandler = this.canvasImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showTextBoxPopup(canvasImage);
            }
        });

        mouseDownRegistration = this.canvasImage.addMouseDownHandler(this);
        mouseUpRegistration = this.canvasImage.addMouseUpHandler(this);
        mouseMoveRegistration = this.canvasImage.addMouseMoveHandler(this);
        mouseOutRegistration = this.canvasImage.addMouseOutHandler(this);

        this.flwCanvas = new FlowPanel();
        flwCanvas.add(this.canvasImage);

        this.flwCanvas.getElement().getStyle().setWidth(canvasWidth, Style.Unit.PX);
        this.flwCanvas.getElement().getStyle().setHeight(canvasHeight, Style.Unit.PX);

    }

    private void showTextBoxPopup(Canvas canvas) {
        if (drawMode == DrawMode.Textual) {
            if (this.textBoxPopup == null) {
                this.textBoxPopup = new ImageTextPopup();
                this.textBoxPopup.addCallBack(new ImageTextPopup.CallBack() {
                    @Override
                    public void hide(boolean isCancel) {
                        if (!isCancel) {
                            showTextOnImage();
                        }
                    }
                });
            }
            this.textBoxPopup.show();
            this.textBoxPopup.center();
        }
    }

    private FlowPanel createButtonPanel() {

        FlowPanel btnPanel = new FlowPanel();

        this.cmdSave = new Button();
        this.anSave = new Anchor();
        this.anSave.addStyleName("fa fa-check");
        this.anSave.getElement().getStyle().setColor("green");
        this.cmdSave.getElement().appendChild(this.anSave.getElement());
        this.cmdSave.setTitle("Save");

        this.cmdSave.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                saveImage();
                closeButtonCalled();
            }
        });

        this.cmdCancel = new Button();
        this.anCancel = new Anchor();
        this.anCancel.addStyleName("fa fa-close");
        this.anCancel.getElement().getStyle().setColor("red");
        this.cmdCancel.getElement().appendChild(this.anCancel.getElement());
        this.cmdCancel.setTitle("Cancel");

        this.cmdCancel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                closeButtonCalled();
            }
        });

        this.cmdEraser = new Button();
        this.anEraser = new Anchor();
        this.anEraser.addStyleName("fa fa-eraser");
        this.anEraser.getElement().getStyle().setColor("tomato");
        this.cmdEraser.getElement().appendChild(this.anEraser.getElement());
        this.cmdEraser.setTitle("Rollback all Changes");

        this.cmdEraser.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                clearCanvas();
            }
        });

        this.cmdBrush = new Button();
        this.anBrush = new Anchor();
        this.anBrush.addStyleName("fa fa-pencil");
        this.anBrush.getElement().getStyle().setColor("orange");
        this.cmdBrush.getElement().appendChild(this.anBrush.getElement());
        this.cmdBrush.setTitle("Choose Brush Size");

        this.cmdBrush.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                drawMode = DrawMode.FreeHand;
                changeCanvasCursor();
                showBrushPopup();
            }
        });

        this.cmdColor = new Button();
        this.anColor = new Anchor();
        this.anColor.addStyleName("fa fa-paint-brush");
        this.anColor.getElement().getStyle().setColor("maroon");
        this.cmdColor.getElement().appendChild(this.anColor.getElement());
        this.cmdColor.setTitle("Choose Color");

        this.cmdColor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showColorPicker();
            }
        });

        this.cmdUndo = new Button();
        this.anUndo = new Anchor();
        this.anUndo.addStyleName("fa fa-undo");
        this.anUndo.getElement().getStyle().setColor("red");
        this.cmdUndo.getElement().appendChild(this.anUndo.getElement());
        this.cmdUndo.setTitle("Undo Change");

        this.cmdUndo.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String tempImageString = getLastHistory();
                drawImageFromStringInCanvas(tempImageString);
            }
        });

        this.cmdText = new Button();
        this.anText = new Anchor();
        this.anText.addStyleName("fa fa-font");
        this.anText.getElement().getStyle().setColor("orange");
        this.cmdText.getElement().appendChild(this.anText.getElement());
        this.cmdText.setTitle("Click to Write Text");

        this.cmdText.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                drawMode = DrawMode.Textual;
                changeCanvasCursor();
            }
        });

        btnPanel.add(this.cmdSave);

        btnPanel.add(this.cmdText);

        btnPanel.add(this.cmdBrush);

        btnPanel.add(this.cmdColor);

        btnPanel.add(this.cmdEraser);

        btnPanel.add(this.cmdUndo);

        btnPanel.add(this.cmdCancel);

        btnPanel.getElement().getStyle().setHeight(buttonHeight, Style.Unit.PX);

        return btnPanel;
    }

    private String getLastHistory() {
        String lastStoredImage = null;
        if (this.imageHistoryList.size() > 0) {
            lastStoredImage = this.imageHistoryList.remove(this.imageHistoryList.size() - 1);
        }
        return lastStoredImage;
    }

    private void showTextOnImage() {

        makeHistory();

        double left = (double) currClientX;
        double top = (double) currClientY;

        String strComment = this.textBoxPopup.getText();
        Integer fontSize = this.textBoxPopup.getFontSize();

        Context2d context = canvasImage.getContext2d();
        context.setFont(fontSize.toString() + "px " + textFontFamilyName);

        double textWidth = getTextWidthFromFont(textFontFamilyName, fontSize, strComment);
        if ((textWidth + left > canvasWidth)) {
            left = left - ((left + textWidth) - canvasWidth);
        }

        double textHeight = getTextHeightFromFont(textFontFamilyName, fontSize, strComment);
        if (textHeight > top) {
            if (textHeight <= canvasHeight) {
                top = textHeight;
            } else if (textHeight > canvasHeight) {
                top = canvasHeight;
            }
        }

        Double imageTextWidth = 0d;
        if (textWidth > canvasWidth) {
            left = 0d;
            imageTextWidth = (double) canvasWidth;
        } else if (textWidth <= canvasWidth) {
            imageTextWidth = textWidth;
        }

        canvasImage.getContext2d().setFillStyle(CssColor.make(this.strokeColor.getRed(), this.strokeColor.getGreen(), this.strokeColor.getBlue()));
        canvasImage.getContext2d().fillText(strComment, left, top, imageTextWidth);
    }

    private double getTextWidthFromFont(String fontName, double fontSize, String textToMeasure) {
        Double widthValue = 0d;
        SpanElement span = Document.get().createSpanElement();
        span.setAttribute("style", "font-family:" + fontName);
        span.getStyle().setFontSize(fontSize, Style.Unit.PX);
        span.setInnerText(textToMeasure);
        span = Document.get().getBody().appendChild(span);
        widthValue = (double) span.getOffsetWidth();
        Document.get().getBody().removeChild(span);
        return widthValue;
    }

    private double getTextHeightFromFont(String fontName, double fontSize, String textToMeasure) {
        Double heightValue = 0d;
        SpanElement span = Document.get().createSpanElement();
        span.setAttribute("style", "font-family:" + fontName);
        span.getStyle().setFontSize(fontSize, Style.Unit.PX);
        span.setInnerText(textToMeasure);
        span = Document.get().getBody().appendChild(span);
        heightValue = (double) span.getOffsetHeight();
        Document.get().getBody().removeChild(span);
        return heightValue;
    }

    private void showBrushPopup() {
        DmBrushSlider brushSlider = new DmBrushSlider(new DmBrushSlider.DmBrushSliderCloseCallback() {
            @Override
            public void brushSliderClosing(int sliderValue) {
                changeBrushWidth(sliderValue);
            }
        }, brushWidth);
        brushSlider.show();
        brushSlider.center();
    }

    private void changeBrushWidth(int brushWidth) {
        this.brushWidth = brushWidth;
    }

    private String getInstantImage() {
        String tempImageString = "";
        if (canvasImage != null) {
            tempImageString = this.canvasImage.toDataUrl("image/jpeg");
        }
        return tempImageString;
    }

    private void saveImage() {

        final String imageString = getInstantImage();

        this.srvInvoker.invokeRPC().saveImage(imageString, fileModel, new AsyncCallback<DmResult<DmFile_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), cmdSave).show();
            }

            @Override
            public void onSuccess(DmResult<DmFile_Mdl> result) {
                if (!result.isStatus()) {
                    new DmAlertMessageBox(result.getErrorMessage(), cmdSave).show();
                } else if (result.isStatus()) {
                    fileModel = result.getData();
                    if (fileSavedCallBack != null) {
                        fileSavedCallBack.fileSaved(fileModel);
                    }
                }
            }
        });
    }

    private void showColorPicker() {
        if (this.popupColor != null) {
            this.popupColor.hide(true);
        }

        this.popupColor.show();
        this.popupColor.center();
    }

    private void closeColorPopup(int statusOfClosing) {
        try {
            if (statusOfClosing == 1) {
                if (this.popupColor != null) {
                    ColorPicker cPicker = (ColorPicker) this.popupColor.getWidget();
                    this.strokeColor = cPicker.getRGBColor();
                }
            }
            this.popupColor.hide(true);
        } catch (Exception ex) {

        }
    }

    private void clearCanvas() {
        if (this.canvasImage != null) {
            this.canvasImage.getContext2d().drawImage(ImageElement.as(img.getElement()), 0, 0, canvasWidth, canvasHeight);
        }
    }

    private void drawImageFromStringInCanvas(final String tempImageString) {
        if (!DmShared.xNull(tempImageString).equals("")) {
            if (this.canvasImage != null) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        final Image tempImage = new Image();
                        tempImage.setUrl(tempImageString);
                        tempImage.setVisible(false);
                        btnPanel.add(tempImage);
                        tempImage.addLoadHandler(new LoadHandler() {
                            @Override
                            public void onLoad(LoadEvent event) {
                                Context2d context = canvasImage.getContext2d();
                                context.drawImage(ImageElement.as(tempImage.getElement()), 0, 0, canvasWidth, canvasHeight);
                                btnPanel.remove(tempImage);
                            }
                        });
                    }
                });
            }
        }
    }

    private void closeButtonCalled() {
        if (mouseDownRegistration != null) {
            mouseDownRegistration.removeHandler();
        }
        if (mouseUpRegistration != null) {
            mouseUpRegistration.removeHandler();
        }
        if (textBoxApplyClickHandler != null) {
            textBoxApplyClickHandler.removeHandler();
        }
        if (textBoxHideClickHandler != null) {
            textBoxHideClickHandler.removeHandler();
        }
        if (canvasClickHandler != null) {
            canvasClickHandler.removeHandler();
        }
        this.hide(true);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        findMousePointerLocation(0, event);
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        findMousePointerLocation(1, event);
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        findMousePointerLocation(3, event);
    }

    private void findMousePointerLocation(int eType, MouseEvent event) {
        if (eType == 0) {//mouse down

            prevClientX = currClientX;
            prevClientY = currClientY;
            currClientX = event.getRelativeX(canvasImage.getCanvasElement());
            currClientY = event.getRelativeY(canvasImage.getCanvasElement());

            if (drawMode == DrawMode.FreeHand) {
                draggingFlag = true;
                dotFlag = true;
                if (dotFlag) {
                    makeHistory();

                    Context2d context = canvasImage.getContext2d();
                    context.beginPath();
                    context.setFillStyle(CssColor.make(this.strokeColor.getRed(), this.strokeColor.getGreen(), this.strokeColor.getBlue()));
                    double tempX = (currClientX - (brushWidth / 2));
                    double tempY = (currClientY - (brushWidth / 2));
                    if (tempX < 0) {
                        tempX = 0;
                    }
                    if (tempY < 0) {
                        tempY = 0;
                    }
                    context.fillRect(tempX, tempY, brushWidth, brushWidth);
                    context.stroke();
                    context.closePath();
                    dotFlag = false;
                }
            }
        }

        if ((eType == 1) || (eType == 2)) {//mouse out or up
            draggingFlag = false;
        }

        if (eType == 3) {//mouse move
            if (draggingFlag) {
                prevClientX = currClientX;
                prevClientY = currClientY;
                currClientX = event.getRelativeX(canvasImage.getCanvasElement());
                currClientY = event.getRelativeY(canvasImage.getCanvasElement());

                draw();
            }
        }

    }

    private void makeHistory() {
        String tempImageString = getInstantImage();
        if (!DmShared.xNull(tempImageString).equals("")) {
            this.imageHistoryList.add(tempImageString);
        }
    }

    private void draw() {
        if (canvasImage != null) {
            if (drawMode == DrawMode.FreeHand) {
                makeHistory();

                Context2d context = canvasImage.getContext2d();
                context.beginPath();
                double tempX = (prevClientX - (brushWidth / 2));
                double tempY = (prevClientY - (brushWidth / 2));
                if (tempX < 0) {
                    tempX = 0;
                }
                if (tempY < 0) {
                    tempY = 0;
                }
                context.setStrokeStyle(CssColor.make(this.strokeColor.getRed(), this.strokeColor.getGreen(), this.strokeColor.getBlue()));
                context.setLineWidth(brushWidth);
                context.moveTo(tempX, tempY);
                context.lineTo(currClientX, currClientY);
                context.fillRect(tempX, tempY, brushWidth, brushWidth);
                context.closePath();
                context.stroke();
            }
        }
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        findMousePointerLocation(2, event);
    }
}
