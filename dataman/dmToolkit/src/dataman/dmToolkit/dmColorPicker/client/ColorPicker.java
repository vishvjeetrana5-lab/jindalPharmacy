package dataman.dmToolkit.dmColorPicker.client;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the implementation of the Colorpicker. It defines the user interface,
 * the glue and calculations necessary for colorpicker functionality.
 *
 * <h1>Example</h1>
 * <CODE>
 * public class ColorPickerExample implements EntryPoint {
 *
 *  public void onModuleLoad() {
 *    // Make a new colorpicker
 *    ColorPicker picker = new ColorPicker();
 *
 *    // Add it to the root panel.
 *    RootPanel.get().add(picker);
 *
 *    // Make a new button that does something when you click it.
 *    Button b = new Button("Pick!", new ClickListener() {
 *      public void onClick(Widget sender) {
 *        Window.alert("You chose " + picker.getHexColor());
 *      }
 *    });
 *
 *    // Add it to the root panel.
 *    RootPanel.get().add(b);
 *  }
 * }
 * </CODE>
 *
 * @author AurorisNET
 * @copyright (C) 2007 AurorisNET. All Rights Reserved.
 */
public class ColorPicker extends Composite implements KeyPressHandler, ClickHandler, ChangeHandler {

    public interface CloseCallback {

        public void close(int statusOfClosing);
    }

    private int blue;
    private int brightness;
    private int colorMode; // Which color picking mode we are in

    // Elements
    private HTML colorpreview;
    private int green;
    private int hue;
    private RadioButton rbBlue;
    private RadioButton rbBrightness;
    private RadioButton rbGreen;
    // Radiobuttons
    private RadioButton rbHue;

    private RadioButton rbRed;
    private RadioButton rbSaturation;
    private int red;
    private int saturation;
    private SliderBar sliderbar; // auxilliary color picking slider. Center UI component.
    private SliderMap slidermap; // main color picking slider. Leftmost UI component.

    private TextBox tbBlue;
    private TextBox tbBrightness;
    private TextBox tbGreen;
    private TextBox tbHexColor;
    // Textboxes
    private TextBox tbHue;
    private TextBox tbRed;
    private TextBox tbSaturation;

    private VerticalPanel mainPanel;
    //Buttons
    private Button cmdOk, cmdCancel;
    private Anchor anOk, anCancel;

    private CloseCallback callBack;

    public ColorPicker() {
        // UI Drawing
        //------------------

        hue = 0;
        saturation = 100;
        brightness = 100;
        red = 255;
        green = 0;
        blue = 0;

        HorizontalPanel panel = new HorizontalPanel();
        FlexTable table = new FlexTable();

        mainPanel = new VerticalPanel();

        // Add the large slider map
        slidermap = new SliderMap(this);
        panel.add(slidermap);
        panel.setCellWidth(slidermap, "258px");
        panel.setCellHeight(slidermap, "258px");

        // Add the small slider bar
        sliderbar = new SliderBar(this);
        panel.add(sliderbar);
        panel.setCellWidth(sliderbar, "40px");
        panel.setCellHeight(sliderbar, "258px");

        // Define the Flextable's content
        // Color preview at the top
        colorpreview = new HTML("");
        colorpreview.setWidth("50px");
        colorpreview.setHeight("50px");
        DOM.setStyleAttribute(colorpreview.getElement(), "border", "1px solid black");

        // Radio buttons
        String rbgroup = "color" + this.hashCode();
        rbHue = new RadioButton(rbgroup, "H:");
        rbHue.addClickHandler(this);
        rbSaturation = new RadioButton(rbgroup, "S:");
        rbSaturation.addClickHandler(this);
        rbBrightness = new RadioButton(rbgroup, "V:");
        rbBrightness.addClickHandler(this);
        rbRed = new RadioButton(rbgroup, "R:");
        rbRed.addClickHandler(this);
        rbGreen = new RadioButton(rbgroup, "G:");
        rbGreen.addClickHandler(this);
        rbBlue = new RadioButton(rbgroup, "B:");
        rbBlue.addClickHandler(this);

        // Textboxes
        tbHue = new TextBox();
        tbHue.setText(new Integer(hue).toString());
        tbHue.setMaxLength(3);
        tbHue.setVisibleLength(4);
        tbHue.addKeyPressHandler(this);
        tbHue.addChangeHandler(this);
        tbSaturation = new TextBox();
        tbSaturation.setText(new Integer(saturation).toString());
        tbSaturation.setMaxLength(3);
        tbSaturation.setVisibleLength(4);
        tbSaturation.addKeyPressHandler(this);
        tbSaturation.addChangeHandler(this);
        tbBrightness = new TextBox();
        tbBrightness.setText(new Integer(brightness).toString());
        tbBrightness.setMaxLength(3);
        tbBrightness.setVisibleLength(4);
        tbBrightness.addKeyPressHandler(this);
        tbBrightness.addChangeHandler(this);
        tbRed = new TextBox();
        tbRed.setText(new Integer(red).toString());
        tbRed.setMaxLength(3);
        tbRed.setVisibleLength(4);
        tbRed.addKeyPressHandler(this);
        tbRed.addChangeHandler(this);
        tbGreen = new TextBox();
        tbGreen.setText(new Integer(green).toString());
        tbGreen.setMaxLength(3);
        tbGreen.setVisibleLength(4);
        tbGreen.addKeyPressHandler(this);
        tbGreen.addChangeHandler(this);
        tbBlue = new TextBox();
        tbBlue.setText(new Integer(blue).toString());
        tbBlue.setMaxLength(3);
        tbBlue.setVisibleLength(4);
        tbBlue.addKeyPressHandler(this);
        tbBlue.addChangeHandler(this);
        tbHexColor = new TextBox();
        tbHexColor.setText("ff0000");
        tbHexColor.setMaxLength(6);
        tbHexColor.setVisibleLength(6);
        tbHexColor.addKeyPressHandler(this);
        tbHexColor.addChangeHandler(this);

        // Put together the FlexTable
        table.setWidget(0, 0, colorpreview);
        table.getFlexCellFormatter().setColSpan(0, 0, 3);
        table.setWidget(1, 0, rbHue);
        table.setWidget(1, 1, tbHue);
        table.setWidget(1, 2, new HTML("&deg;"));
        table.setWidget(2, 0, rbSaturation);
        table.setWidget(2, 1, tbSaturation);
        table.setText(2, 2, "%");
        table.setWidget(3, 0, rbBrightness);
        table.setWidget(3, 1, tbBrightness);
        table.setText(3, 2, "%");
        table.setWidget(4, 0, rbRed);
        table.setWidget(4, 1, tbRed);
        table.setWidget(5, 0, rbGreen);
        table.setWidget(5, 1, tbGreen);
        table.setWidget(6, 0, rbBlue);
        table.setWidget(6, 1, tbBlue);
        table.setText(7, 0, "#:");
        table.setWidget(7, 1, tbHexColor);
        table.getFlexCellFormatter().setColSpan(7, 1, 2);

        // Final setup
        panel.add(table);
        mainPanel.add(initButtonBar());
        mainPanel.add(panel);

        rbSaturation.setValue(true);
        setPreview("ff0000");
        DOM.setStyleAttribute(colorpreview.getElement(), "cursor", "default");

        // First event
        onClick(rbSaturation);

        initWidget(mainPanel);
        //initWidget(panel);
    }

    private FlowPanel initButtonBar() {

        FlowPanel btnPanel = new FlowPanel();

        this.cmdOk = new Button();
        this.anOk = new Anchor();
        this.anOk.addStyleName("fa fa-check");
        this.anOk.getElement().getStyle().setColor("green");
        this.cmdOk.getElement().appendChild(this.anOk.getElement());
        this.cmdOk.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                callCloseCallBack(1);
            }
        });

        this.cmdCancel = new Button();
        this.anCancel = new Anchor();
        this.anCancel.addStyleName("fa fa-close");
        this.anCancel.getElement().getStyle().setColor("red");
        this.cmdCancel.getElement().appendChild(this.anCancel.getElement());
        this.cmdCancel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                callCloseCallBack(2);
            }
        });

        btnPanel.add(this.cmdOk);
        btnPanel.add(this.cmdCancel);

        return btnPanel;
    }

    public void addCloseCallback(CloseCallback callback) {
        this.callBack = callback;
    }

    private void callCloseCallBack(int closingStatus) {
        if (this.callBack != null) {
            this.callBack.close(closingStatus);
        }
    }

    /**
     * Returns the hexadecimal notation of the current selected color.
     *
     * @return Hexadecimal in the range of 000000-FFFFFF
     */
    public String getHexColor() {
        return tbHexColor.getText();
    }

    
    public Color getRGBColor() throws Exception {
        Color color = new Color();
        color.setHex(tbHexColor.getText());
        return color;
    }
    /**
     * This method is called when a widget is attached to the browser's
     * document. To receive notification after a Widget has been added to the
     * document, override the Widget.onLoad() method.
     *
     * Subclasses that override this method must call <tt>super.onAttach()</tt>
     * before doing anything else to ensure that the Widget has been properly
     * attached to its underlying Element.
     */
    @Override
    public void onAttach() {
        // Called when we are shown (from being hidden)
        super.onAttach();
        colorMode = -1;
        updateSliders();
    }

    /**
     * Fires whenever the user generates picking events along the color picker
     * bar.
     *
     * Subclasses that override this method must call
     * <tt>super.onBarSelected(y)</tt> to ensure that the Widget recieves its
     * events.
     *
     * @param y the distance along the y-axis of the user's selection, between 0
     * and 255, inclusive.
     */
    public void onBarSelected(int y) {
        switch (colorMode) {
            case SliderMap.Hue:
                hue = 360 - percentOf(y, 360);
                tbHue.setText(Integer.toString(hue));
                onChange(tbHue);
                break;
            case SliderMap.Saturation:
                saturation = 100 - percentOf(y, 100);
                tbSaturation.setText(Integer.toString(saturation));
                onChange(tbSaturation);
                break;
            case SliderMap.Brightness:
                brightness = 100 - percentOf(y, 100);
                tbBrightness.setText(Integer.toString(brightness));
                onChange(tbBrightness);
                break;
            case SliderMap.Red:
                red = 255 - y;
                tbRed.setText(Integer.toString(red));
                onChange(tbRed);
                break;
            case SliderMap.Green:
                green = 255 - y;
                tbGreen.setText(Integer.toString(green));
                onChange(tbGreen);
                break;
            case SliderMap.Blue:
                blue = 255 - y;
                tbBlue.setText(Integer.toString(blue));
                onChange(tbBlue);
                break;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.event.dom.client.ChangeHandler#onChange(com.google.gwt.event.dom.client.ChangeEvent)
     */
    public void onChange(ChangeEvent event) {
        onChange((Widget) event.getSource());
    }

    /**
     * Fired whenever something in this widget changes.
     *
     * Subclasses that override this method must call
     * <tt>super.onChange(sender)</tt> to ensure that the Widget recieves its
     * events.
     *
     * @param sender the widget that has changed.
     */
    public void onChange(Widget sender) {
        if (sender == tbHexColor) {
            // Figure out colors
            // Color class will do bounds check on hex input
            try {
                Color color = new Color();
                color.setHex(tbHexColor.getText());
                tbHue.setText(Integer.toString(color.getHue()));
                tbSaturation.setText(Integer.toString(color.getSaturation()));
                tbBrightness.setText(Integer.toString(color.getValue()));
                tbRed.setText(Integer.toString(color.getRed()));
                tbGreen.setText(Integer.toString(color.getGreen()));
                tbBlue.setText(Integer.toString(color.getBlue()));
                tbHexColor.setText(color.getHex());
                setPreview(color.getHex());
            } catch (Exception e) {
            }
        }

        if (sender == tbRed || sender == tbGreen || sender == tbBlue) {
            // Don't allow this value to overflow or underflow
            try {
                if (Integer.parseInt(((TextBox) sender).getText()) > 255) {
                    ((TextBox) sender).setText("255");
                }
                if (Integer.parseInt(((TextBox) sender).getText()) < 0) {
                    ((TextBox) sender).setText("0");
                }
            } catch (Exception e) {
            }

            red = Integer.parseInt(tbRed.getText());
            green = Integer.parseInt(tbGreen.getText());
            blue = Integer.parseInt(tbBlue.getText());
            hue = Integer.parseInt(tbHue.getText());
            saturation = Integer.parseInt(tbSaturation.getText());
            brightness = Integer.parseInt(tbBrightness.getText());

            // Figure out the colors
            try {
                Color color = new Color();
                color.setRGB(red, green, blue);
                tbHue.setText(Integer.toString(color.getHue()));
                tbSaturation.setText(Integer.toString(color.getSaturation()));
                tbBrightness.setText(Integer.toString(color.getValue()));
                tbHexColor.setText(color.getHex());
                setPreview(color.getHex());
            } catch (Exception e) {
            }
        } else if (sender == tbHue || sender == tbSaturation || sender == tbBrightness) {
            // Don't allow this value to overflow
            try {
                if (Integer.parseInt(tbHue.getText()) > 359) {
                    tbHue.setText("359");
                }

                if (Integer.parseInt(tbSaturation.getText()) > 100) {
                    tbSaturation.setText("100");
                }

                if (Integer.parseInt(tbBrightness.getText()) > 100) {
                    tbBrightness.setText("100");
                }
            } catch (Exception e) {
            }

            red = Integer.parseInt(tbRed.getText());
            green = Integer.parseInt(tbGreen.getText());
            blue = Integer.parseInt(tbBlue.getText());
            hue = Integer.parseInt(tbHue.getText());
            saturation = Integer.parseInt(tbSaturation.getText());
            brightness = Integer.parseInt(tbBrightness.getText());

            // Figure out colors
            try {
                Color color = new Color();
                color.setHSV(hue, saturation, brightness);
                tbRed.setText(Integer.toString(color.getRed()));
                tbGreen.setText(Integer.toString(color.getGreen()));
                tbBlue.setText(Integer.toString(color.getBlue()));
                tbHexColor.setText(color.getHex());
                setPreview(color.getHex());
            } catch (Exception e) {
            }
        }

        // Let the sliders know something's changed
        updateSliders();
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    public void onClick(ClickEvent event) {
        onClick((Widget) event.getSource());
    }

    /**
     * Fired when the user clicks on a widget.
     *
     * Subclasses that override this method must call
     * <tt>super.onClick(sender)</tt> to ensure that the Widget recieves its
     * events.
     *
     * @param sender the widget sending the event.
     */
    public void onClick(Widget sender) {
        if (sender == rbHue) {
            if (colorMode != SliderMap.Hue) {
                colorMode = SliderMap.Hue;
                slidermap.setColorSelectMode(SliderMap.Hue);
                sliderbar.setColorSelectMode(SliderBar.Hue);
                slidermap.setOverlayOpacity(100);
                sliderbar.setLayerOpacity(100, SliderBar.BarD);
            }

            try {
                Color color = new Color();
                color.setHSV(hue, 100, 100);
                slidermap.setOverlayColor("#" + color.getHex());
            } catch (Exception e) {
            }

            sliderbar.setSliderPosition(256 - (int) ((new Integer(hue).floatValue() / 360) * 256));
            slidermap.setSliderPosition((int) ((new Integer(saturation).floatValue() / 100) * 256),
                    256 - (int) ((new Integer(brightness).floatValue() / 100) * 256));
        } else if (sender == rbSaturation) {
            if (colorMode != SliderMap.Saturation) {
                colorMode = SliderMap.Saturation;
                slidermap.setColorSelectMode(SliderMap.Saturation);
                sliderbar.setColorSelectMode(SliderBar.Saturation);
                slidermap.setOverlayColor("transparent");
                sliderbar.setLayerOpacity(100, SliderBar.BarD);
            }

            try {
                Color color = new Color();
                color.setHSV(hue, 100, brightness);
                sliderbar.setLayerColor("#" + color.getHex(), SliderBar.BarD);
            } catch (Exception e) {
            }

            slidermap.setOverlayOpacity(100 - saturation);

            sliderbar.setSliderPosition(256 - (int) ((new Integer(saturation).floatValue() / 100) * 256));
            slidermap.setSliderPosition((int) ((new Integer(hue).floatValue() / 360) * 256),
                    256 - (int) ((new Integer(brightness).floatValue() / 100) * 256));
        } else if (sender == rbBrightness) {
            if (colorMode != SliderMap.Brightness) {
                colorMode = SliderMap.Brightness;
                slidermap.setColorSelectMode(SliderMap.Brightness);
                sliderbar.setColorSelectMode(SliderBar.Brightness);
                slidermap.setUnderlayColor("#000000");
                slidermap.setOverlayColor("transparent");
                sliderbar.setLayerOpacity(100, SliderBar.BarD);
            }

            try {
                Color color = new Color();
                color.setHSV(hue, saturation, 100);
                sliderbar.setLayerColor("#" + color.getHex(), SliderBar.BarD);
            } catch (Exception e) {
            }

            slidermap.setOverlayOpacity(brightness);

            sliderbar.setSliderPosition(256 - (int) ((new Integer(brightness).floatValue() / 100) * 256));
            slidermap.setSliderPosition((int) ((new Integer(hue).floatValue() / 360) * 256),
                    256 - (int) ((new Integer(saturation).floatValue() / 100) * 256));
        } else if (sender == rbRed) {
            if (colorMode != SliderMap.Red) {
                colorMode = SliderMap.Red;
                slidermap.setColorSelectMode(SliderMap.Red);
                sliderbar.setColorSelectMode(SliderBar.Red);
            }
            slidermap.setOverlayOpacity(percentOf(red, 100));

            sliderbar.setSliderPosition(256 - red);
            slidermap.setSliderPosition(blue, 256 - green);
        } else if (sender == rbGreen) {
            if (colorMode != SliderMap.Green) {
                colorMode = SliderMap.Green;
                slidermap.setColorSelectMode(SliderMap.Green);
                sliderbar.setColorSelectMode(SliderBar.Green);
            }

            slidermap.setOverlayOpacity(percentOf(green, 100));

            sliderbar.setSliderPosition(256 - green);
            slidermap.setSliderPosition(blue, 256 - red);
        } else if (sender == rbBlue) {
            if (colorMode != SliderMap.Blue) {
                colorMode = SliderMap.Blue;
                slidermap.setColorSelectMode(SliderMap.Blue);
                sliderbar.setColorSelectMode(SliderBar.Blue);
            }

            slidermap.setOverlayOpacity(percentOf(blue, 100));

            sliderbar.setSliderPosition(256 - blue);
            slidermap.setSliderPosition(red, 256 - green);
        }

        if (colorMode == SliderMap.Red || colorMode == SliderMap.Green || colorMode == SliderMap.Blue) {
            int x = 0;
            int y = 0;

            if (colorMode == SliderMap.Red) {
                x = blue;
                y = green;
            }

            if (colorMode == SliderMap.Green) {
                x = blue;
                y = red;
            }

            if (colorMode == SliderMap.Blue) {
                x = red;
                y = green;
            }

            int horzPer = (int) ((new Float(x).floatValue() / 256) * 100);
            int vertPer = (int) ((new Float(y).floatValue() / 256) * 100);
            int horzPerRev = (int) (((256 - new Float(x).floatValue()) / 256) * 100);
            int vertPerRev = (int) (((256 - new Float(y).floatValue()) / 256) * 100);

            if (vertPerRev > horzPerRev) {
                sliderbar.setLayerOpacity(horzPerRev, SliderBar.BarD);
            } else {
                sliderbar.setLayerOpacity(vertPerRev, SliderBar.BarD);
            }
            if (vertPerRev > horzPer) {
                sliderbar.setLayerOpacity(horzPer, SliderBar.BarC);
            } else {
                sliderbar.setLayerOpacity(vertPerRev, SliderBar.BarC);
            }
            if (vertPer > horzPer) {
                sliderbar.setLayerOpacity(horzPer, SliderBar.BarB);
            } else {
                sliderbar.setLayerOpacity(vertPer, SliderBar.BarB);
            }
            if (vertPer > horzPerRev) {
                sliderbar.setLayerOpacity(horzPerRev, SliderBar.BarA);
            } else {
                sliderbar.setLayerOpacity(vertPer, SliderBar.BarA);
            }
        }
    }

    /**
     * Fired when a keyboard action generates a character. This occurs after
     * onKeyDown and onKeyUp are fired for the physical key that was pressed.
     *
     * It should be noted that many browsers do not generate keypress events for
     * non-printing keyCode values, such as KEY_ENTER or arrow keys. These
     * keyCodes can be reliably captured either with onKeyDown(Widget, char,
     * int) or onKeyUp(Widget, char, int).
     *
     * Subclasses that override this method must call
     * <tt>super.onKeyPress(sender, keyCode, modifiers)</tt> to ensure that the
     * Widget recieves its events.
     *
     * @param sender the widget that was focused when the event occurred.
     * @see com.google.gwt.user.client.ui.KeyboardListener
     */
    public void onKeyPress(KeyPressEvent event) {
        Widget sender = (Widget) event.getSource();
        char keyCode = event.getCharCode();

        if (sender == tbHexColor) {
            // Disallow non-hex in hexadecimal boxes
            if ((!Character.isDigit(keyCode))
                    && (keyCode != 'A') && (keyCode != 'a')
                    && (keyCode != 'B') && (keyCode != 'b')
                    && (keyCode != 'C') && (keyCode != 'c')
                    && (keyCode != 'D') && (keyCode != 'd')
                    && (keyCode != 'E') && (keyCode != 'e')
                    && (keyCode != 'F') && (keyCode != 'f')
                    && (keyCode != (char) KeyCodes.KEY_TAB)
                    && (keyCode != (char) KeyCodes.KEY_BACKSPACE)
                    && (keyCode != (char) KeyCodes.KEY_DELETE) && (keyCode != (char) KeyCodes.KEY_ENTER)
                    && (keyCode != (char) KeyCodes.KEY_HOME) && (keyCode != (char) KeyCodes.KEY_END)
                    && (keyCode != (char) KeyCodes.KEY_LEFT) && (keyCode != (char) KeyCodes.KEY_UP)
                    && (keyCode != (char) KeyCodes.KEY_RIGHT) && (keyCode != (char) KeyCodes.KEY_DOWN)) {
                ((TextBox) sender).cancelKey();
            }
        } else // Disallow non-numerics in numeric boxes
        {
            if ((!Character.isDigit(keyCode))
                    && (keyCode != (char) KeyCodes.KEY_TAB)
                    && (keyCode != (char) KeyCodes.KEY_BACKSPACE)
                    && (keyCode != (char) KeyCodes.KEY_DELETE) && (keyCode != (char) KeyCodes.KEY_ENTER)
                    && (keyCode != (char) KeyCodes.KEY_HOME) && (keyCode != (char) KeyCodes.KEY_END)
                    && (keyCode != (char) KeyCodes.KEY_LEFT) && (keyCode != (char) KeyCodes.KEY_UP)
                    && (keyCode != (char) KeyCodes.KEY_RIGHT) && (keyCode != (char) KeyCodes.KEY_DOWN)) {
                ((TextBox) sender).cancelKey();
            }
        }
    }

    /**
     * Fires whenever the user generates picking events on the color picker map.
     *
     * Subclasses that override this method must call
     * <tt>super.onMapSelected(x,y)</tt> to ensure that the Widget recieves its
     * events.
     *
     * @param x the distance along the x-axis of the user's selection, between 0
     * and 255, inclusive.
     * @param y the distance along the y-axis of the user's selection, between 0
     * and 255, inclusive.
     */
    public void onMapSelected(int x, int y) {
        switch (colorMode) {
            case SliderMap.Hue:
                saturation = percentOf(x, 100);
                brightness = 100 - percentOf(y, 100);
                tbSaturation.setText(Integer.toString(saturation));
                tbBrightness.setText(Integer.toString(brightness));
                onChange(tbHue);
                break;
            case SliderMap.Saturation:
                hue = percentOf(x, 360);
                brightness = 100 - percentOf(y, 100);
                tbHue.setText(Integer.toString(hue));
                tbBrightness.setText(Integer.toString(brightness));
                onChange(tbSaturation);
                break;
            case SliderMap.Brightness:
                hue = percentOf(x, 360);
                saturation = 100 - percentOf(y, 100);
                tbHue.setText(Integer.toString(hue));
                tbSaturation.setText(Integer.toString(saturation));
                onChange(tbBrightness);
                break;
            case SliderMap.Red:
                blue = x;
                green = 256 - y;
                tbBlue.setText(Integer.toString(blue));
                tbGreen.setText(Integer.toString(green));
                onChange(tbRed);
                break;
            case SliderMap.Green:
                blue = x;
                red = 256 - y;
                tbBlue.setText(Integer.toString(blue));
                tbRed.setText(Integer.toString(red));
                onChange(tbGreen);
                break;
            case SliderMap.Blue:
                red = x;
                green = 256 - y;
                tbRed.setText(Integer.toString(red));
                tbGreen.setText(Integer.toString(green));
                onChange(tbBlue);
                break;
        }
    }

    /*Helper functions -- for common calculations
     */
    /**
     * Divides the first value by 256, then multiplies it by the second value.
     *
     * @param val1 first value.
     * @param val2 second value.
     * @return result.
     */
    private int percentOf(int val1, int val2) {
        return (int) (new Float(val1).floatValue() / 256 * val2);
    }

    /**
     * Sets the hexadecimal notation for Red, Green, and Blue. This will
     * automatically populate all the other fields, too.
     *
     * @param hex Hexadecimal notation of Red, Green and Blue in the range of
     * 000000-FFFFFF
     * @throws java.lang.Exception A generic exception if the hexadecimal
     * notation is bad.
     */
    public void setHex(String hex) throws Exception {
        Color color = new Color();
        color.setHex(hex);

        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.hue = color.getHue();
        this.saturation = color.getSaturation();
        this.brightness = color.getValue();

        tbRed.setText(Integer.toString(this.red));
        tbGreen.setText(Integer.toString(this.green));
        tbBlue.setText(Integer.toString(this.blue));
        tbHue.setText(Integer.toString(this.hue));
        tbSaturation.setText(Integer.toString(this.saturation));
        tbBrightness.setText(Integer.toString(this.brightness));
        tbHexColor.setText(color.getHex());
        setPreview(color.getHex());

        updateSliders();
    }

    /**
     * Set the Hue, Saturation and Value (Brightness) variables. This will
     * automatically populate the Red, Green, Blue, and Hexadecimal fields, too.
     *
     * HSV represents points in the RGB color space, which attempt to describe
     * perceptual color relationships more accurately than RGB. HSV describes
     * colors as points in a cylinder whose central axis ranges from black at
     * the bottom to white at the top with neutral colors between them, where
     * angle around the axis corresponds to �hue�, distance from the axis
     * corresponds to �saturation�, and distance along the axis corresponds to
     * �lightness�, �value�, or �brightness�.
     *
     * @param hue angle - valid range is 0-359
     * @param saturation percent - valid range is 0-100
     * @param value percent (Brightness) - valid range is 0-100
     * @throws java.lang.Exception A general exception if the Hue, Saturation,
     * or Value variables are out of range.
     */
    public void setHSV(int hue, int saturation, int value) throws Exception {
        Color color = new Color();
        color.setHSV(hue, saturation, value);

        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = value;

        tbRed.setText(Integer.toString(this.red));
        tbGreen.setText(Integer.toString(this.green));
        tbBlue.setText(Integer.toString(this.blue));
        tbHue.setText(Integer.toString(this.hue));
        tbSaturation.setText(Integer.toString(this.saturation));
        tbBrightness.setText(Integer.toString(this.brightness));
        tbHexColor.setText(color.getHex());
        setPreview(color.getHex());

        updateSliders();
    }

    /**
     * Called when the widget wants to update the preview color sample box in
     * the top-right corner of the UI.
     *
     * @param hex Hexadecimal notation of RGB
     */
    private void setPreview(String hex) {
        DOM.setStyleAttribute(colorpreview.getElement(), "backgroundColor", "#" + hex);
    }

    /**
     * Sets the Red, Green, and Blue color variables. This will automatically
     * populate the Hue, Saturation and Brightness and Hexadecimal fields, too.
     *
     * The RGB color model is an additive color model in which red, green, and
     * blue light are added together in various ways to reproduce a broad array
     * of colors. The name of the model comes from the initials of the three
     * additive primary colors, red, green, and blue.
     *
     * @param red strength - valid range is 0-255
     * @param green strength - valid range is 0-255
     * @param blue strength - valid range is 0-255
     * @throws java.lang.Exception Exception if the Red, Green or Blue variables
     * are out of range.
     */
    public void setRGB(int red, int green, int blue) throws Exception {
        Color color = new Color();
        color.setRGB(red, green, blue);

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.hue = color.getHue();
        this.saturation = color.getSaturation();
        this.brightness = color.getValue();

        tbRed.setText(Integer.toString(this.red));
        tbGreen.setText(Integer.toString(this.green));
        tbBlue.setText(Integer.toString(this.blue));
        tbHue.setText(Integer.toString(this.hue));
        tbSaturation.setText(Integer.toString(this.saturation));
        tbBrightness.setText(Integer.toString(this.brightness));
        tbHexColor.setText(color.getHex());
        setPreview(color.getHex());

        updateSliders();
    }

    /**
     * Called whenever the internal state has been changed and needs to
     * synchronize the other components.
     */
    private void updateSliders() {
        // Let the sliders know something's changed
        if (rbHue.getValue()) {
            onClick(rbHue);
        }
        if (rbSaturation.getValue()) {
            onClick(rbSaturation);
        }
        if (rbBrightness.getValue()) {
            onClick(rbBrightness);
        }
        if (rbRed.getValue()) {
            onClick(rbRed);
        }
        if (rbGreen.getValue()) {
            onClick(rbGreen);
        }
        if (rbBlue.getValue()) {
            onClick(rbBlue);
        }
    }
}
