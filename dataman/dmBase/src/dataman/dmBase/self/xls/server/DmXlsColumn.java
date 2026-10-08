/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmBase.self.xls.server;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import java.sql.Types;
import java.util.Objects;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;

/**
 *
 * @author abhishek
 */
public class DmXlsColumn {

    public enum XlsContentType {
        Text,
        Numeric,
        Date,
        DateTime,
        Time;
    }

    private String columnName;
    private String displayName;
    private XlsContentType contentType;

    private String strFormat;
    private String headerCellFontName;
    private int headerCellFontSize;

    private Alignment headerCellHorizontalAlignment;
    private VerticalAlignment headerCellVerticalAlignment;

    private int valueType;

    private boolean boolTotalColumn = false;

    public DmXlsColumn(String name, String displayName) {
        this(name, displayName, XlsContentType.Text);
    }

    public DmXlsColumn(String name, String displayName, XlsContentType contentType) {
        this.contentType = contentType;
        this.columnName = DmBaseShared.xNull(name).trim();
        this.displayName = DmBaseShared.xNull(displayName).trim();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public XlsContentType getContentType() {
        return contentType;
    }

    public void setContentType(XlsContentType contentType) {
        this.contentType = contentType;
    }

    public String getFormatString() {
        return strFormat;
    }

    public void setFormatString(String strFormat) {
        this.strFormat = strFormat;
    }

    public String getHeaderCellFontName() {
        return headerCellFontName;
    }

    public void setHeaderCellFontName(String headerCellFontName) {
        this.headerCellFontName = headerCellFontName;
    }

    public int getHeaderCellFontSize() {
        return headerCellFontSize;
    }

    public void setHeaderCellFontSize(int headerCellFontSize) {
        this.headerCellFontSize = headerCellFontSize;
    }

    public Alignment getHeaderCellHorizontalAlignment() {
        return headerCellHorizontalAlignment;
    }

    public void setHeaderCellHorizontalAlignment(Alignment headerCellHorizontalAlignment) {
        this.headerCellHorizontalAlignment = headerCellHorizontalAlignment;
    }

    public VerticalAlignment getHeaderCellVerticalAlignment() {
        return headerCellVerticalAlignment;
    }

    public void setHeaderCellVerticalAlignment(VerticalAlignment headerCellVerticalAlignment) {
        this.headerCellVerticalAlignment = headerCellVerticalAlignment;
    }

    public int getSQLValueType() {
        return valueType;
    }

    public void setSQLValueType(int valueType) {
        this.valueType = valueType;
    }

    public boolean isTotalColumn() {
        return boolTotalColumn;
    }

    public void setTotalColumn(boolean boolTotalColumn) {
        this.boolTotalColumn = boolTotalColumn;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.columnName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DmXlsColumn other = (DmXlsColumn) obj;
        if (!Objects.equals(this.columnName, other.columnName)) {
            return false;
        }
        return true;
    }

}
