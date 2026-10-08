package dataman.dmToolkitR.dmColorPicker.client;


public class Color
{
    private float b; //[0-1] Blue
    private float g; //[0-1] Green
    private float h; //[0,360] Hue
    private String hex; // Hexadecimal notation of RGB
    private float r; //[0-1] Red
    private float s; //[0-1] Saturation
    private float v; //[0-1] Value

    /**
     * Returns the integer of the blue component of the RGB colorspace, or 0 if it hasn't been set.
     * @return blue component
     */
    public int getBlue()
    {
        return (int)(b * 255);
    }

    /**
     * Returns the integer of the green component of the RGB colorspace, or 0 if it hasn't been set.
     * @return green component
     */
    public int getGreen()
    {
        return (int)(g * 255);
    }

    /**
     * Returns the hexadecimal representation of the RGB colorspace.
     * @return hexadecimal representation
     */
    public String getHex()
    {
        return hex;
    }

    /**
     * Returns the integer of the hue component of the HSV colorspace, or 0 if it hasn't been set.
     * @return hue component
     */
    public int getHue()
    {
        return (int)h;
    }

    /**
     * Returns the integer of the red component of the RGB colorspace, or 0 if it hasn't been set.
     * @return red component
     */
    public int getRed()
    {
        return (int)(r * 255);
    }

    /**
     * Returns the integer of the saturation component of the HSV colorspace, or 0 if it hasn't been set.
     * @return saturation component
     */
    public int getSaturation()
    {
        return (int)(s * 100);
    }

    /**
     * Returns the integer of the value (brightness) component of the HSV colorspace, or 0 if it hasn't been set.
     * @return value component
     */
    public int getValue()
    {
        return (int)(v * 100);
    }

    /**
     * Converts the HSV colorspace into the RGB colorspace. This private method works exclusively with internal variables and does not return anything.
     * @param h Hue
     * @param s Saturation
     * @param v Value (Brightness)
     */
    private void HSVtoRGB(float h, float s, float v)
    {
        int i;
        float f;
        float p;
        float q;
        float t;
        if(s == 0)
        {
            // achromatic (grey)
            this.r = v;
            this.g = v;
            this.b = v;
            return;
        }
        h /= 60;            // sector 0 to 5
        i = (int)Math.floor(h);
        f = h - i;          // factorial part of h
        p = v * (1 - s);
        q = v * (1 - s * f);
        t = v * (1 - s * (1 - f));
        switch( i )
        {
            case 0:
                this.r = v;
                this.g = t;
                this.b = p;
                break;
            case 1:
                this.r = q;
                this.g = v;
                this.b = p;
                break;
            case 2:
                this.r = p;
                this.g = v;
                this.b = t;
                break;
            case 3:
                this.r = p;
                this.g = q;
                this.b = v;
                break;
            case 4:
                this.r = t;
                this.g = p;
                this.b = v;
                break;
            default:        // case 5:
                this.r = v;
                this.g = p;
                this.b = q;
                break;
        }
    }

    /**
     * A helper function to determine the largest value between the three inputs.
     * @param a First value
     * @param b Second value
     * @param c Third value
     * @return The value of the largest of the inputs.
     */
    private float MAX(float a, float b, float c)
    {
        float max = Integer.MIN_VALUE;
        if (a > max)
            max = a;
        if (b > max)
            max = b;
        if (c > max)
            max = c;
        return max;
    }

    /**
     * A helper function to determine the smallest value between the three inputs.
     * @param a First value
     * @param b Second value
     * @param c Third value
     * @return The value of the smallest of the inputs.
     */
    private float MIN(float a, float b, float c)
    {
        float min = Integer.MAX_VALUE;
        if (a < min)
            min = a;
        if (b < min)
            min = b;
        if (c < min)
            min = c;
        return min;
    }

    // r,g,b values are from 0 to 1
    // h = [0,360], s = [0,1], v = [0,1]
    //      if s == 0, then h = (undefined)
    /**
     * Converts the RGB colorspace into the HSV colorspace.  This private method works exclusively with internal variables and does not return anything.
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    private void RGBtoHSV(float r, float g, float b)
    {
        float min = 0;
        float max = 0;
        float delta = 0;

        min = MIN(r, g, b);
        max = MAX(r, g, b);

        this.v = max;               // v

        delta = max - min;

        if(max != 0)
        {
            this.s = delta / max;       // s
        }
        else
        {
            // r = g = b = 0        // s = 0, v is undefined
            this.s = 0;
            this.h = 0;
            return;
        }

        if (delta == 0)
        {
            h = 0;
            return;
        }

        if(r == max)
        {
            this.h = (g - b) / delta;       // between yellow & magenta
        }
        else if(g == max)
        {
            this.h = 2 + (b - r) / delta;   // between cyan & yellow
        }
        else
        {
            this.h = 4 + (r - g) / delta;   // between magenta & cyan
        }

        this.h *= 60;               // degrees

        if(this.h < 0)
            this.h += 360;
    }

    /**
     * Converts from RGB to Hexadecimal notation.
     */
    private void setHex()
    {
        String hRed = Integer.toHexString(getRed());
        String hGreen = Integer.toHexString(getGreen());
        String hBlue = Integer.toHexString(getBlue());

        if (hRed.length() == 0) { hRed = "00"; }
        if (hRed.length() == 1) { hRed = "0" + hRed; }
        if (hGreen.length() == 0) { hGreen = "00"; }
        if (hGreen.length() == 1) { hGreen = "0" + hGreen; }
        if (hBlue.length() == 0) { hBlue = "00"; }
        if (hBlue.length() == 1) { hBlue = "0" + hBlue; }

        this.hex = hRed + hGreen + hBlue;
    }

    /**
     * Sets the hexadecimal representation of Red, Green and Blue.
     * @param hex The hexadecimal string notation. It must be 6 letters long and consist of the characters 0-9 and A-F.
     * @throws java.lang.Exception Exception if the hexadecimal string cannot be parsed into its Red, Green, and Blue components.
     */
    public void setHex(String hex) throws Exception
    {
        if (hex.length() == 6)
        {
            setRGB(Integer.parseInt(hex.substring(0,2), 16),
                Integer.parseInt(hex.substring(2,4), 16),
                Integer.parseInt(hex.substring(4,6), 16));
        }
        else
        {
            throw new Exception();
        }
    }

    /**
     * Set the Hue, Saturation and Value (Brightness) variables. This will automatically populate the Red, Green, Blue, and Hexadecimal fields, too.
     *
     * It represents points in the RGB color space, which attempt to describe perceptual color relationships more accurately than RGB. HSV describes colors as points in a cylinder whose central axis ranges from black at the bottom to white at the top with neutral colors between them, where angle around the axis corresponds to �hue�, distance from the axis corresponds to �saturation�, and distance along the axis corresponds to �lightness�, �value�, or �brightness�.
     * @param h Hue - valid range is 0-359
     * @param s Saturation - valid range is 0-100
     * @param v Value (Brightness) - valid range is 0-100
     * @throws java.lang.Exception A general exception if the Hue, Saturation, or Value variables are out of range.
     */
    public void setHSV(int h, int s, int v) throws Exception
    {
        if (h < 0 || h > 360) throw new Exception();
        if (s < 0 || s > 100) throw new Exception();
        if (v < 0 || v > 100) throw new Exception();

        this.h = h;
        this.s = (float)s / 100;
        this.v = (float)v / 100;

        HSVtoRGB(this.h, this.s, this.v);

        setHex();
    }

    /**
     * Sets the Red, Green, and Blue color variables. This will automatically convert to Hue, Saturation and Brightness and Hexadecimal.
     *
     * The RGB color model is an additive color model in which red, green, and blue light are added together in various ways to reproduce a broad array of colors. The name of the model comes from the initials of the three additive primary colors, red, green, and blue.
     * @param r Red - valid range is 0-255
     * @param g Green - valid range is 0-255
     * @param b Blue - valid range is 0-255
     * @throws java.lang.Exception Exception if the Red, Green or Blue variables are out of range.
     */
    public void setRGB(int r, int g, int b) throws Exception
    {
        if (r < 0 || r > 255) throw new Exception();
        if (g < 0 || g > 255) throw new Exception();
        if (b < 0 || b > 255) throw new Exception();

        this.r = (float)r / 255;
        this.g = (float)g / 255;
        this.b = (float)b / 255;

        RGBtoHSV(this.r, this.g, this.b);

        setHex();
    }
}