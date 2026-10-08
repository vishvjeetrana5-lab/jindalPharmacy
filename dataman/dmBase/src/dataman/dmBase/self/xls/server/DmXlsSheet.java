/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmBase.self.xls.server;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jxl.Range;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.WritableSheet;

/**
 *
 * @author abhishek
 */
public class DmXlsSheet {

    private String workSheetName;
    private WritableSheet workSheet;
    private List<DmXlsColumn> workSheetColumns;
    private String defaultFontName;
    private int defaultFontSize;
    private Alignment defaultHAlign;
    private VerticalAlignment defaultVAlign;

    public DmXlsSheet(String workSheetName) {
        this.workSheetColumns = new ArrayList<>();
        this.workSheetName = DmBaseShared.xNull(workSheetName).trim();
    }

    public WritableSheet getWorkSheet() {
        return workSheet;
    }

    public void setWorkSheet(WritableSheet workSheet) {
        this.workSheet = workSheet;
    }

    public List<DmXlsColumn> getWorkSheetColumns() {
        return workSheetColumns;
    }

    public String getWorkSheetName() {
        return workSheetName;
    }

    public String getDefaultFontName() {
        return defaultFontName;
    }

    public void setDefaultFontName(String defaultFontName) {
        this.defaultFontName = defaultFontName;
    }

    public int getDefaultFontSize() {
        return defaultFontSize;
    }

    public void setDefaultFontSize(int defaultFontSize) {
        this.defaultFontSize = defaultFontSize;
    }

    public Alignment getDefaultHorizontalAlign() {
        return defaultHAlign;
    }

    public void setDefaultHorizontalAlign(Alignment defaultHAlign) {
        this.defaultHAlign = defaultHAlign;
    }

    public VerticalAlignment getDefaultVerticalAlign() {
        return defaultVAlign;
    }

    public void setDefaultVerticalAlign(VerticalAlignment defaultVAlign) {
        this.defaultVAlign = defaultVAlign;
    }

    public Range mergeCells(int startColIndex, int startRowIndex, int endColIndex, int endRowIndex) throws Exception {
        return this.workSheet.mergeCells(startColIndex, startRowIndex, endColIndex, endRowIndex);        
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.workSheetName);
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
        final DmXlsSheet other = (DmXlsSheet) obj;
        if (!Objects.equals(this.workSheetName, other.workSheetName)) {
            return false;
        }
        return true;
    }
}
