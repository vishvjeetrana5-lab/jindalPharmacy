/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmBase.self.xls.server;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.WritableWorkbook;

/**
 *
 * @author abhishek
 */
public class DmXlsWriter {

    public interface IXlsWriterSettingsCallback {

        public void configureWorkbookSettings(WorkbookSettings settings);
    }

    public interface IXlsWriterWorkSheetCreationCallback {

        public boolean onWorkSheetCreating(DmXlsSheet workSheet);

        public String getDefaultFontName();

        public int getDefaultFontSize();

        public Alignment getDefaultHorizontalAlignment();

        public VerticalAlignment getDefaultVerticalAlignment();
    }

    private WritableWorkbook workBook;
    private List<DmXlsSheet> workSheets;
    private File filePath;
    private DmXlsSheet defaultWorkSheet;

    private IXlsWriterSettingsCallback settingsCallback;
    private IXlsWriterWorkSheetCreationCallback workSheetCreationCallback;

    public DmXlsWriter(String filePath, String fileName) throws Exception {
        this.workSheets = new ArrayList<>();
        this.filePath = new File(DmBaseShared.xNull(filePath).trim());
        if (!this.filePath.exists()) {
            this.filePath.mkdirs();
        }
        this.filePath = new File(this.filePath, DmBaseShared.xNull(fileName).trim());
    }

    public IXlsWriterSettingsCallback getSettingsCallback() {
        return settingsCallback;
    }

    public DmXlsWriter setSettingsCallback(IXlsWriterSettingsCallback settingsCallback) {
        this.settingsCallback = settingsCallback;
        return this;
    }

    public IXlsWriterWorkSheetCreationCallback getWorkSheetCreationCallback() {
        return workSheetCreationCallback;
    }

    public DmXlsWriter setWorkSheetCreationCallback(IXlsWriterWorkSheetCreationCallback workSheetCreationCallback) {
        this.workSheetCreationCallback = workSheetCreationCallback;
        return this;
    }

    public WritableWorkbook getWorkBook() {
        return workBook;
    }

    public File getFilePath() {
        return filePath;
    }

    public DmXlsWriter build(String defaultSheetName) throws Exception {
        return createWorkBook(defaultSheetName);
    }

    private DmXlsWriter createWorkBook(String defaultSheetName) throws Exception {
        WorkbookSettings settings = new WorkbookSettings();
        if (settingsCallback != null) {
            settingsCallback.configureWorkbookSettings(settings);
        }
        this.workBook = Workbook.createWorkbook(filePath, settings);
        createWorkSheet(defaultSheetName);
        if (workBook.getNumberOfSheets() > 0) {
            defaultWorkSheet = this.workSheets.get(0);
        }
        return this;
    }

    private void createWorkSheet(String workSheetName) throws Exception {
        boolean addWorkSheet = true;
        DmXlsSheet tempSheet = new DmXlsSheet(workSheetName);
        tempSheet.setWorkSheet(this.workBook.createSheet(tempSheet.getWorkSheetName(), workSheets.size()));
        if (this.workSheetCreationCallback != null) {
            addWorkSheet = this.workSheetCreationCallback.onWorkSheetCreating(tempSheet);
            if (addWorkSheet) {
                tempSheet.setDefaultFontName(this.workSheetCreationCallback.getDefaultFontName());
                tempSheet.setDefaultFontSize(this.workSheetCreationCallback.getDefaultFontSize());
                tempSheet.setDefaultHorizontalAlign(this.workSheetCreationCallback.getDefaultHorizontalAlignment());
                tempSheet.setDefaultVerticalAlign(this.workSheetCreationCallback.getDefaultVerticalAlignment());
            }
        }
        if (addWorkSheet) {
            this.workSheets.add(tempSheet);
        }
    }

    public DmXlsSheet getWorkSheetByName(String workSheetName, boolean createIfAbsent) throws Exception {
        DmXlsSheet tempSheet = new DmXlsSheet(workSheetName);
        if (!workSheets.contains(tempSheet)) {
            if (createIfAbsent) {
                createWorkSheet(workSheetName);
                tempSheet = this.workSheets.get(this.workSheets.indexOf(tempSheet));
            } else {
                tempSheet = null;
            }
        } else {
            tempSheet = workSheets.get(workSheets.indexOf(tempSheet));
        }
        return tempSheet;
    }

    public DmXlsSheet getWorkSheetByIndex(int workSheetIndex) throws Exception {
        return getWorkSheetByName(workSheets.get(workSheetIndex).getWorkSheetName(), false);
    }

    public DmXlsSheet getDefaultWorkSheet() {
        return defaultWorkSheet;
    }

    public void commit() throws Exception {
        workBook.write();
    }

    public void close() throws Exception {
        workBook.close();
    }
}
