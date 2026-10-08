package dataman.dmToolkitR.dmColorPicker.client;



import dataman.dmToolkitR.dmColorPicker.client.images.ColorPickerImageResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * Implements the SliderMap control.
 */
public final class SliderMap extends HTML
{
    public static final int Blue = 6;
    public static final int Brightness = 2;
    public static final int Green = 5;
    public static final int Hue = 3;
    public static final int Red = 4;
    public static final int Saturation = 1;

    private Image colorOverlay;
    private Image colorUnderlay;
    private Image slider;

    private ColorPickerImageResource cpImageBundle;
    private ColorPicker parent = null;

    private boolean capturedMouse = false;

    /***
     * Initialize the SliderMap -- default mode is Saturation.
     */
    public SliderMap(ColorPicker parent)
    {
        super();

        this.parent = parent;

//        setWidth("256px");
        setHeight("256px");

        cpImageBundle = (ColorPickerImageResource) GWT.create(ColorPickerImageResource.class);

        colorUnderlay = new Image(cpImageBundle.map_saturation());
        colorOverlay = new Image(cpImageBundle.map_saturation_overlay());
        slider = new Image(cpImageBundle.mappoint());

        DOM.appendChild(getElement(), colorUnderlay.getElement());
        DOM.appendChild(getElement(), colorOverlay.getElement());
        DOM.appendChild(getElement(), slider.getElement());

//        DOM.setStyleAttribute(getElement(), "position", "absolute");
        DOM.setStyleAttribute(colorUnderlay.getElement(), "border", "1px solid black");
        DOM.setStyleAttribute(colorOverlay.getElement(), "border", "1px solid black");
    }

    /***
     * This method is called when a widget is attached to the browser's document.
     */
    @Override
    public void onAttach()
    {
        super.onAttach();

//        DOM.setStyleAttribute(colorUnderlay.getElement(), "position", "absolute");
        DOM.setStyleAttribute(colorUnderlay.getElement(), "left", "0px");
        DOM.setStyleAttribute(colorUnderlay.getElement(), "top", "0px");
//        DOM.setStyleAttribute(colorOverlay.getElement(), "position", "absolute");
        DOM.setStyleAttribute(colorOverlay.getElement(), "left", "0px");
        DOM.setStyleAttribute(colorOverlay.getElement(), "top", "0px");
//        DOM.setStyleAttribute(slider.getElement(), "position", "absolute");
        DOM.setStyleAttribute(slider.getElement(), "left", "0px");
        DOM.setStyleAttribute(slider.getElement(), "top", "0px");
        setOverlayOpacity(100);
    }

    /**
     * Fired whenever a browser event is received.
     * @param event Event to process
     */
    @Override
    public void onBrowserEvent(Event event)
    {
        super.onBrowserEvent(event);

        switch (DOM.eventGetType(event))
        {
            case Event.ONMOUSEUP:
                Event.releaseCapture(this.getElement());
                capturedMouse = false;
                break;
            case Event.ONMOUSEDOWN:
                Event.setCapture(this.getElement());
                capturedMouse = true;
            case Event.ONMOUSEMOVE:
                if (capturedMouse)
                {
                    DOM.eventPreventDefault(event);

                    int x = DOM.eventGetClientX(event) - colorUnderlay.getAbsoluteLeft() + 1 + Window.getScrollLeft();
                    int y = DOM.eventGetClientY(event) - colorUnderlay.getAbsoluteTop() + 1 + Window.getScrollTop();

                    if (x < 0) x = 0;
                    if (x > 256) x = 256;
                    if (y < 0) y = 0;
                    if (y > 256) y = 256;

                    DOM.setStyleAttribute(slider.getElement(), "left", x - 7 + "px");
                    DOM.setStyleAttribute(slider.getElement(), "top", y - 7 + "px");

//                    if (parent != null) { parent.onMapSelected(x,y); }
                }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Widget#onLoad()
     */
    @Override
    public void onLoad()
    {
        this.sinkEvents(Event.MOUSEEVENTS);
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Widget#onUnload()
     */
    @Override
    public void onUnload()
    {
        this.unsinkEvents(Event.MOUSEEVENTS);
    }

    /***
     * Sets the color selection mode
     * @param mode Saturation
     */
    public void setColorSelectMode(int mode)
    {
        if (!isAttached()) { return; }

        switch (mode)
        {
            case Saturation:
            	colorUnderlay.setResource(cpImageBundle.map_saturation());
            	colorOverlay.setResource(cpImageBundle.map_saturation_overlay());
            break;

            case Brightness:
                colorUnderlay.setResource(cpImageBundle.map_white());
                colorOverlay.setResource(cpImageBundle.map_brightness());
            break;

            case Hue:
                colorUnderlay.setResource(cpImageBundle.map_white());
                colorOverlay.setResource(cpImageBundle.map_hue());
                setOverlayOpacity(100);
            break;

            case Red:
                colorOverlay.setResource(cpImageBundle.map_red_max());
                colorUnderlay.setResource(cpImageBundle.map_red_min());
            break;

            case Green:
                colorOverlay.setResource(cpImageBundle.map_green_max());
                colorUnderlay.setResource(cpImageBundle.map_green_min());
            break;

            case Blue:
            	colorOverlay.setResource(cpImageBundle.map_blue_max());
                colorUnderlay.setResource(cpImageBundle.map_blue_min());
            break;
        }
    }

    /**
     * Sets the overlay layer's color.
     * @param color Hexadecimal representation of RGB.
     */
    public void setOverlayColor(String color)
    {
        DOM.setStyleAttribute(colorOverlay.getElement(), "backgroundColor", color);
    }

    /**
     * Set overlay layer's opacity.
     * @param alpha An opacity percentage, between 100 (fully opaque) and 0 (invisible).
     */
    public void setOverlayOpacity(int alpha)
    {
        if (alpha >= 0 && alpha <= 100 && isAttached())
        {
            TransparencyImpl.setTransparency(colorOverlay.getElement(), alpha);
        }
    }

    /**
     * Sets the slider's position along the x-axis and y-axis.
     * @param x position along the x-axis [0-256]
     * @param y position along the y-axis [0-256]
     */
    public void setSliderPosition(int x, int y)
    {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > 256) x = 256;
        if (y > 256) y = 256;
        DOM.setStyleAttribute(slider.getElement(), "left", x - 7 + "px");
        DOM.setStyleAttribute(slider.getElement(), "top", y - 7 + "px");
    }

    /**
     * Sets the underlay's layer color.
     * @param color Hexadecimal representation of RGB.
     */
    public void setUnderlayColor(String color)
    {
        DOM.setStyleAttribute(colorUnderlay.getElement(), "backgroundColor", color);
    }
}