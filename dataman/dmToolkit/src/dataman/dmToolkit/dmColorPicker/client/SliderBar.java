package dataman.dmToolkit.dmColorPicker.client;

import dataman.dmToolkit.dmColorPicker.client.images.ColorPickerImageResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * Implements the SliderBar control.
 */
public final class SliderBar extends HTML
{
    public static final int BarA = 1;
    public static final int BarB = 2;
    public static final int BarC = 3;
    public static final int BarD = 4;
    public static final int Blue = 6;
    public static final int Brightness = 2;
    public static final int Green = 5;
    public static final int Saturation = 1;
    public static final int Hue = 3;
    public static final int Red = 4;

    private Image colorA;
    private Image colorB;
    private Image colorC;
    private Image colorD;
    private Image slider;

    private ColorPickerImageResource cpImageBundle;
    private ColorPicker parent = null;

    private boolean capturedMouse = false;

    /***
     * Initialize the SliderMap -- default mode is Saturation.
     */
    public SliderBar(ColorPicker parent)
    {
        super();

        this.parent = parent;

        setWidth("40px");
        setHeight("256px");

        cpImageBundle = (ColorPickerImageResource) GWT.create(ColorPickerImageResource.class);

        colorA = new Image(cpImageBundle.bar_white());
        colorB = new Image(cpImageBundle.bar_white());
        colorC = new Image(cpImageBundle.bar_white());
        colorD = new Image(cpImageBundle.bar_saturation());
        slider = new Image(cpImageBundle.rangearrows());

        DOM.appendChild(getElement(), colorA.getElement());
        DOM.appendChild(getElement(), colorB.getElement());
        DOM.appendChild(getElement(), colorC.getElement());
        DOM.appendChild(getElement(), colorD.getElement());
        DOM.appendChild(getElement(), slider.getElement());

        DOM.setStyleAttribute(getElement(), "position", "absolute");
        DOM.setStyleAttribute(colorA.getElement(), "border", "1px solid black");
        DOM.setStyleAttribute(colorB.getElement(), "border", "1px solid black");
        DOM.setStyleAttribute(colorC.getElement(), "border", "1px solid black");
        DOM.setStyleAttribute(colorD.getElement(), "border", "1px solid black");
    }

    /***
     * This method is called when a widget is attached to the browser's document.
     */
    @Override
    public void onAttach()
    {
        super.onAttach();

        DOM.setStyleAttribute(colorA.getElement(), "position", "absolute");
        DOM.setStyleAttribute(colorA.getElement(), "left", "10px");
        DOM.setStyleAttribute(colorA.getElement(), "top", "0px");
        DOM.setStyleAttribute(colorB.getElement(), "position", "absolute");
        DOM.setStyleAttribute(colorB.getElement(), "left", "10px");
        DOM.setStyleAttribute(colorB.getElement(), "top", "0px");
        DOM.setStyleAttribute(colorC.getElement(), "position", "absolute");
        DOM.setStyleAttribute(colorC.getElement(), "left", "10px");
        DOM.setStyleAttribute(colorC.getElement(), "top", "0px");
        DOM.setStyleAttribute(colorD.getElement(), "position", "absolute");
        DOM.setStyleAttribute(colorD.getElement(), "left", "10px");
        DOM.setStyleAttribute(colorD.getElement(), "top", "0px");
        DOM.setStyleAttribute(slider.getElement(), "position", "absolute");
        DOM.setStyleAttribute(slider.getElement(), "left", "1px");
        DOM.setStyleAttribute(slider.getElement(), "top", "0px");
    }

    /**
     * Fired whenever a browser event is received.
     * @param event Event to process
     */
    @Override
    public void onBrowserEvent(Event event)
    {
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

                    int y = DOM.eventGetClientY(event) - getAbsoluteTop() + Window.getScrollTop();

                    if (y < 0) y = 0;
                    if (y > 256) y = 256;

                    DOM.setStyleAttribute(slider.getElement(), "top", y - 4 + "px");

                    if (parent != null) { parent.onBarSelected(y); }
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

    /**
     * Sets the color selection mode
     * @param mode Can be one of: ColorBar.Saturation, ColorBar.Hue, ColorBar.Brightness, ColorBar.Red, ColorBar.Green, ColorBar.Blue, ColorBar.Red.
     */
    public void setColorSelectMode(int mode)
    {
        if (!isAttached()) { return; }

        switch (mode)
        {
            case Saturation:
                colorA.setResource(cpImageBundle.bar_white());
                colorB.setResource(cpImageBundle.bar_white());
                colorC.setResource(cpImageBundle.bar_white());
                colorD.setResource(cpImageBundle.bar_saturation());
            break;

            case Brightness:
                colorA.setResource(cpImageBundle.bar_white());
                colorB.setResource(cpImageBundle.bar_white());
                colorC.setResource(cpImageBundle.bar_white());
                colorD.setResource(cpImageBundle.bar_brightness());
            break;

            case Hue:
            	colorA.setResource(cpImageBundle.bar_white());
            	colorB.setResource(cpImageBundle.bar_white());
            	colorC.setResource(cpImageBundle.bar_white());
            	colorD.setResource(cpImageBundle.bar_hue());
            break;

            case Red:
            	colorA.setResource(cpImageBundle.bar_red_tl());
            	colorB.setResource(cpImageBundle.bar_red_tr());
            	colorC.setResource(cpImageBundle.bar_red_br());
            	colorD.setResource(cpImageBundle.bar_red_bl());
            break;

            case Green:
            	colorA.setResource(cpImageBundle.bar_green_tl());
            	colorB.setResource(cpImageBundle.bar_green_tr());
            	colorC.setResource(cpImageBundle.bar_green_br());
            	colorD.setResource(cpImageBundle.bar_green_bl());
            break;

            case Blue:
            	colorA.setResource(cpImageBundle.bar_blue_tl());
            	colorB.setResource(cpImageBundle.bar_blue_tr());
            	colorC.setResource(cpImageBundle.bar_blue_br());
            	colorD.setResource(cpImageBundle.bar_blue_bl());
            break;
        }
    }

    /**
     * Sets the color of a particular layer
     * @param color Hexadecimal notation of RGB to change the layer's color
     * @param layer Which layer to affect
     */
    public void setLayerColor(String color, int layer)
    {
        Element colorbar;

        switch (layer)
        {
            case BarA:
                colorbar = colorA.getElement();
                break;
            case BarB:
                colorbar = colorB.getElement();
                break;
            case BarC:
                colorbar = colorC.getElement();
                break;
            case BarD:
                colorbar = colorD.getElement();
                break;
            default:
                return;
        }

        TransparencyImpl.setBackgroundColor(colorbar, color);
    }

    /**
     * Set overlay's opacity.
     * @param alpha An opacity percentage, between 100 (fully opaque) and 0 (invisible).
     * @param layer which bar to change opacity for, 1-4
     */
    public void setLayerOpacity(int alpha, int layer)
    {
        if (alpha >= 0 && alpha <= 100 && isAttached())
        {
            Element colorbar;

            switch (layer)
            {
                case BarA:
                    colorbar = colorA.getElement();
                    break;
                case BarB:
                    colorbar = colorB.getElement();
                    break;
                case BarC:
                    colorbar = colorC.getElement();
                    break;
                case BarD:
                    colorbar = colorD.getElement();
                    break;
                default:
                    return;
            }

            TransparencyImpl.setTransparency(colorbar, alpha);
        }
    }

    /**
     * Sets the slider's position on the y-axis.
     * @param y Position along the y-axis to set the slider's position to.
     */
    public void setSliderPosition(int y)
    {
        if (y < 0) y = 0;
        if (y > 256) y = 256;
        DOM.setStyleAttribute(slider.getElement(), "top", y - 4 + "px");
    }
}