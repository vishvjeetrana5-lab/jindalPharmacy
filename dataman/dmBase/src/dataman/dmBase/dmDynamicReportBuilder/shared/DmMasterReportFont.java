package dataman.dmBase.dmDynamicReportBuilder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmMasterReportFont implements Serializable, IsSerializable {

    private static final long serialVersionUID = 1L;

    private DmMasterReportStyle.Fonts font = DmMasterReportStyle.Fonts.TAHOMA;

    private DmMasterReportStyle.PDFFontEncoding pdfEncoding = DmMasterReportStyle.PDFFontEncoding.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing;

    private boolean pdfEmbedded = true;

    private int fontSize = 10;

    private boolean bold = false;
    private boolean italic = false;
    private boolean underline = false;

    public DmMasterReportFont() {
        this.fontSize = 10;
        this.bold = false;
        this.italic = false;
        this.underline = false;
    }

    public DmMasterReportFont(DmMasterReportStyle.Fonts font, DmMasterReportStyle.PDFFontEncoding pdfEncoding, boolean pdfEmbedded, int size, boolean bold, boolean italic, boolean underline) {
        this.font = font;
        this.pdfEncoding = pdfEncoding;
        this.pdfEmbedded = pdfEmbedded;
        this.fontSize = size;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
    }

    public DmMasterReportStyle.Fonts getFont() {
        return font;
    }

    public void setFont(DmMasterReportStyle.Fonts font) {
        this.font = font;
    }

    public DmMasterReportStyle.PDFFontEncoding getPdfEncoding() {
        return pdfEncoding;
    }

    public void setPdfEncoding(DmMasterReportStyle.PDFFontEncoding pdfEncoding) {
        this.pdfEncoding = pdfEncoding;
    }

    public boolean isPdfEmbedded() {
        return pdfEmbedded;
    }

    public void setPdfEmbedded(boolean pdfEmbedded) {
        this.pdfEmbedded = pdfEmbedded;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

}
