package dataman.dmBase.dmDynamicReportBuilder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmMasterReportColumn implements Serializable, IsSerializable {

    String title;
    byte[] dbFieldName;
    int colWid;
    //String dataType;
    boolean fixedWid;
    String pattern;
    DmMasterReportStyle style = new DmMasterReportStyle("simple");

    public DmMasterReportColumn() {

    }

    public DmMasterReportColumn(String title, String dbFieldName) {
        this.title = title.trim();
        this.dbFieldName = dbFieldName.toLowerCase().getBytes();
        this.style.setFont(new DmMasterReportFont(DmMasterReportStyle.Fonts.TAHOMA, DmMasterReportStyle.PDFFontEncoding.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true, 10, false, false, false));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public byte[] getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(byte[] dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public int getColWidth() {
        return colWid;
    }

    public void setColWidth(int colWid) {
        this.colWid = colWid;
    }

//    public String getDataType() {
//        return dataType;
//    }
//
//    public void setDataType(String dataType) {
//        this.dataType = dataType.trim();
//    }
    public void setFixedColumnWidth(boolean fixedWid) {
        this.fixedWid = fixedWid;
    }

    public boolean isFixedColumnWidth() {
        return this.fixedWid;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern.trim();
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setStyle(DmMasterReportStyle style) {
        this.style = style;
    }

    public DmMasterReportStyle getStyle() {
        return this.style;
    }

}
