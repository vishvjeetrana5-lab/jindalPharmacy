package dataman.dmBase.dmDynamicReportBuilder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmMasterReportStyle implements Serializable, IsSerializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Vertical Alignment">
    public enum VerticalAlignment implements Serializable, IsSerializable{

        TOP((byte) 1), MIDDLE((byte) 2), BOTTOM((byte) 3), JUSTIFIED((byte) 4);

        private byte value;

        public byte getValue() {
            return this.value;
        }

        private VerticalAlignment(byte type) {
            this.value = type;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Horizontal Alignment">
    public enum HorizontalAlignment implements Serializable, IsSerializable{

        LEFT((byte) 1), RIGHT((byte) 3), CENTER((byte) 2), JUSTIFY((byte) 4);

        private byte value;

        public byte getValue() {
            return this.value;
        }

        private HorizontalAlignment(byte type) {
            this.value = type;
        }
    }
//</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="Fonts">
    public enum Fonts implements Serializable, IsSerializable{

        TAHOMA("Tahoma", "Tahoma.ttf");

        private String fontName;
        private String fontPath;

        public String getName() {
            return this.fontName;
        }

        public String getPath() {
            return this.fontPath;
        }

        private Fonts(String fName, String fPath) {
            this.fontName = fName.trim();
            this.fontPath = fPath.trim();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PDF Font Encoding">
    public enum PDFFontEncoding implements Serializable, IsSerializable{

        PDF_ENCODING_CP1250_Central_European("CP1250"),
        PDF_ENCODING_CP1251_Cyrillic("CP1251"),
        PDF_ENCODING_CP1252_Western_European_ANSI("CP1252"),
        PDF_ENCODING_CP1253_Greek("CP1253"),
        PDF_ENCODING_CP1254_Turkish("CP1254"),
        PDF_ENCODING_CP1255_Hebrew("CP1255"),
        PDF_ENCODING_CP1256_Arabic("CP1256"),
        PDF_ENCODING_CP1257_Baltic("CP1257"),
        PDF_ENCODING_CP1258_Vietnamese("CP1258"),
        PDF_ENCODING_UniGB_UCS2_H_Chinese_Simplified("UniGB-UCS2-H"),
        PDF_ENCODING_UniGB_UCS2_V_Chinese_Simplified("UniGB-UCS2-V"),
        PDF_ENCODING_UniCNS_UCS2_H_Chinese_traditional("UniCNS-UCS2-H"),
        PDF_ENCODING_UniCNS_UCS2_V_Chinese_traditional("UniCNS-UCS2-V"),
        PDF_ENCODING_UniJIS_UCS2_H_Japanese("UniJIS-UCS2-H"),
        PDF_ENCODING_UniJIS_UCS2_V_Japanese("UniJIS-UCS2-V"),
        PDF_ENCODING_UniJIS_UCS2_HW_H_Japanese("UniJIS-UCS2-HW-H"),
        PDF_ENCODING_UniJIS_UCS2_HW_V_Japanese("UniJIS-UCS2-HW-V"),
        PDF_ENCODING_UniKS_UCS2_H_Korean("UniKS-UCS2-H"),
        PDF_ENCODING_UniKS_UCS2_V_Korean("UniKS-UCS2-V"),
        PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing("Identity-H"),
        PDF_ENCODING_Identity_V_Unicode_with_horizontal_writing("Identity-V");

        private String value;

        public String getValue() {
            return this.value;
        }

        private PDFFontEncoding(String fontEncode) {
            this.value = fontEncode.trim();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Rotation">
    public enum Rotation implements Serializable, IsSerializable{

        NONE((byte) 0),
        LEFT((byte) 1),
        RIGHT((byte) 2);

        private byte value;

        public byte getValue() {
            return this.value;
        }

        private Rotation(byte rtype) {
            this.value = rtype;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Colors">
    public enum Colors implements Serializable, IsSerializable{

        GREY(142, 142, 147),
        RED(255, 59, 48),
        GREEN(76, 217, 100),
        PURPLE(88, 86, 214),
        LIGHTBLUE(52, 170, 220),
        BLACK(0, 0, 0),
        LIGHTGRAY(160, 160, 160),
        DARKGRAY(80, 80, 80),
        WHITE(255, 255, 255);

        private final int r;
        private final int g;
        private final int b;
        private final String rgb;

        private Colors(final int r, final int g, final int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.rgb = r + ", " + g + ", " + b;
        }

        public String getRGB() {
            return rgb;
        }

        public int getRed() {
            return r;
        }

        public int getGreen() {
            return g;
        }

        public int getBlue() {
            return r;
        }

        public int getARGB() {
            return 0xFF000000 | ((r << 16) & 0x00FF0000) | ((g << 8) & 0x0000FF00) | b;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Transparency">
    public enum Transparency implements Serializable, IsSerializable{

        TRANSPARENT((byte) 2),
        OPAQUE((byte) 1);

        private byte value;

        public byte getValue() {
            return this.value;
        }

        private Transparency(byte tvalue) {
            this.value = tvalue;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Stretching">
    public enum Stretching implements Serializable, IsSerializable{

        NO_STRETCH((byte) 0),
        RELATIVE_TO_TALLEST_OBJECT((byte) 1),
        RELATIVE_TO_BAND_HEIGHT((byte) 2);

        private byte value;

        public byte getValue() {
            return this.value;
        }

        private Stretching(byte svalue) {
            this.value = svalue;
        }
    }
//</editor-fold>    

    public DmMasterReportStyle() {

    }

    private String name;
    private String parentStyleName;
    private Colors backgroundColor = Colors.WHITE;
    private Colors textColor = Colors.BLACK;

    private DmMasterReportFont font = new DmMasterReportFont(Fonts.TAHOMA, DmMasterReportStyle.PDFFontEncoding.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true, 10, false, false, false);

    private DmMasterReportBorder border = new DmMasterReportBorder();

    private DmMasterReportBorder borderTop;
    private DmMasterReportBorder borderBottom;
    private DmMasterReportBorder borderLeft;
    private DmMasterReportBorder borderRight;

    private int paddingBottom, paddingTop, paddingLeft, paddingRight;

    private int padding = 2;
    private int radius = 0;

    private Transparency transparency = Transparency.TRANSPARENT;

    private VerticalAlignment valign = VerticalAlignment.MIDDLE;
    private HorizontalAlignment halign = HorizontalAlignment.LEFT;

    private Rotation rotation = Rotation.NONE;

    private Stretching stretching = Stretching.RELATIVE_TO_TALLEST_OBJECT;

    private boolean stretchWithOverflow = true;
    private boolean blankWhenNull = true;

    private boolean transparent = false;

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public DmMasterReportStyle(String name) {
        this.name = name.trim();
    }

    public DmMasterReportStyle(String name, String parentStyleName) {
        this.name = name.trim();
        this.parentStyleName = parentStyleName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentStyleName() {
        return parentStyleName;
    }

    public void setParentStyleName(String parentStyleName) {
        this.parentStyleName = parentStyleName;
    }

    public Colors getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Colors backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Colors getTextColor() {
        return textColor;
    }

    public void setTextColor(Colors textColor) {
        this.textColor = textColor;
    }

    public DmMasterReportFont getFont() {
        return font;
    }

    public void setFont(DmMasterReportFont font) {
        this.font = font;
    }

    public DmMasterReportBorder getBorder() {
        return border;
    }

    public void setBorder(DmMasterReportBorder border) {
        this.border = border;
    }

    public DmMasterReportBorder getBorderTop() {
        return borderTop;
    }

    public void setBorderTop(DmMasterReportBorder borderTop) {
        this.borderTop = borderTop;
    }

    public DmMasterReportBorder getBorderBottom() {
        return borderBottom;
    }

    public void setBorderBottom(DmMasterReportBorder borderBottom) {
        this.borderBottom = borderBottom;
    }

    public DmMasterReportBorder getBorderLeft() {
        return borderLeft;
    }

    public void setBorderLeft(DmMasterReportBorder borderLeft) {
        this.borderLeft = borderLeft;
    }

    public DmMasterReportBorder getBorderRight() {
        return borderRight;
    }

    public void setBorderRight(DmMasterReportBorder borderRight) {
        this.borderRight = borderRight;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Transparency getTransparency() {
        return transparency;
    }

    public void setTransparency(Transparency transparency) {
        this.transparency = transparency;
    }

    public VerticalAlignment getVerticalAlign() {
        return valign;
    }

    public void setVerticalAlign(VerticalAlignment valign) {
        this.valign = valign;
    }

    public HorizontalAlignment getHorizontalAlign() {
        return halign;
    }

    public void setHorizontalAlign(HorizontalAlignment halign) {
        this.halign = halign;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public Stretching getStretching() {
        return stretching;
    }

    public void setStretching(Stretching stretching) {
        this.stretching = stretching;
    }

    public boolean isStretchWithOverflow() {
        return stretchWithOverflow;
    }

    public void setStretchWithOverflow(boolean stretchWithOverflow) {
        this.stretchWithOverflow = stretchWithOverflow;
    }

    public boolean isBlankWhenNull() {
        return blankWhenNull;
    }

    public void setBlankWhenNull(boolean blankWhenNull) {
        this.blankWhenNull = blankWhenNull;
    }

}
