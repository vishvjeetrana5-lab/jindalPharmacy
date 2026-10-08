package dataman.dmBase.self.server;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DmCsvToExcelWriter {

    public interface WriteHandler {

        public void writeHeaders(WritableSheet workSheet, List<String> lstHeaders, String sheetName) throws Exception;

        public void writeContents(WritableSheet workSheet, TreeMap<String, DmCsvFileFormat_Mdl.CsvContentType> declaredColumns,
                List<String> lstHeaders, List<DmDynamicGridRow> rows, String sheetName) throws Exception;
    }

    private WriteHandler writeHandler;
    private File outFile;
    private List<DmCsvFileFormat_Mdl> csvFiles;
    private int headerFontSize = 10;
    private int contentFontSize = 10;

    public int getHeaderFontSize() {
        return headerFontSize;
    }

    public void setHeaderFontSize(int headerFontSize) {
        this.headerFontSize = headerFontSize;
    }

    public int getContentFontSize() {
        return contentFontSize;
    }

    public void setContentFontSize(int contentFontSize) {
        this.contentFontSize = contentFontSize;
    }

    public List<String> getCsvFiles() {
        List<String> filePaths = new ArrayList<>();
        for (int i = 0; i < csvFiles.size(); i++) {
            filePaths.add(csvFiles.get(i).getCsvFile().getAbsolutePath());
        }
        return filePaths;
    }

    public void addWriteHandler(WriteHandler writeHandler) {
        this.writeHandler = writeHandler;
    }

    public DmCsvToExcelWriter(String outputFilePath, List<DmCsvFileFormat_Mdl> csvFilesWithColumns) throws Exception {
        startUp(outputFilePath, null, csvFilesWithColumns);
    }

    public DmCsvToExcelWriter(String outputFilePath, String csvFolderPath) throws Exception {
        startUp(outputFilePath, csvFolderPath, null);
    }

    private void startUp(String outputFilePath, String csvFolderPath, List<DmCsvFileFormat_Mdl> csvFiles) throws Exception {

        this.csvFiles = new ArrayList<>();

        this.outFile = new File(outputFilePath);
        if (!outFile.getParentFile().exists()) {
            if (!outFile.getParentFile().mkdirs()) {
                throw new Exception("Unable to create output file directories.");
            }
        }

        if (DmBaseShared.xNull(csvFolderPath).equalsIgnoreCase("") && (csvFiles == null)) {
            throw new Exception("Neither csv folder path nor csv files are provided.");
        }

        if (DmBaseShared.xNull(csvFolderPath).equalsIgnoreCase("")) {
            this.csvFiles.addAll(csvFiles);
        } else {
            filterCsvFiles(csvFolderPath);
        }
    }

    private void filterCsvFiles(String csvFolderPath) throws Exception {
        File tempFileFolder = new File(csvFolderPath);
        String[] filteredCsvs = tempFileFolder.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".csv");
            }
        });
        for (int i = 0; i < filteredCsvs.length; i++) {
            DmCsvFileFormat_Mdl csvFile = new DmCsvFileFormat_Mdl(new File(csvFolderPath, filteredCsvs[i].trim()));
            csvFiles.add(csvFile);
        }
    }

    public void startWriting() throws Exception {
        WorkbookSettings workbookSettings = new WorkbookSettings();

        workbookSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(this.outFile, workbookSettings);

        for (int i = 0; i < this.csvFiles.size(); i++) {
            File csvFile = this.csvFiles.get(i).getCsvFile();
            TreeMap<String, DmCsvFileFormat_Mdl.CsvContentType> declaredColumns = this.csvFiles.get(i).getColumns();
            String sheetName = csvFile.getName().substring(0, csvFile.getName().lastIndexOf("."));
            WritableSheet workSheet = workbook.createSheet(sheetName, i);

            DmCsvReader reader = new DmCsvReader(csvFile);
            reader.readLines();
            List<String> headers = reader.getHeaders();

            if (writeHandler == null) {
                writeHeaders(workSheet, headers, 0, 0);
            } else {
                writeHandler.writeHeaders(workSheet, headers, sheetName);
            }

            List<DmDynamicGridRow> rows = reader.getRows();

            if (writeHandler == null) {
                writeContents(workSheet, declaredColumns, headers, rows, 0, 1);
            } else {
                writeHandler.writeContents(workSheet, declaredColumns, headers, rows, sheetName);
            }

        }

        workbook.write();
        workbook.close();
    }

    public void writeHeadersHandler(WritableSheet workSheet, List<String> lstHeaders, String sheetName) throws Exception {

        writeHeaders(workSheet, lstHeaders, 0, 0);
    }

    public void writeHeaders(WritableSheet workSheet, List<String> lstHeaders, int colIndexStartFrom, int rowIndexStartFrom) throws Exception {
        WritableFont headerFont = new WritableFont(WritableFont.TAHOMA, headerFontSize, WritableFont.BOLD);
        WritableCellFormat headerFormat = new WritableCellFormat(headerFont);
        CellView headerCellView = new CellView();
        headerCellView.setFormat(headerFormat);
        for (int i = 0; i < lstHeaders.size(); i++) {
            Label lblHeader = new Label(colIndexStartFrom, rowIndexStartFrom, lstHeaders.get(i).trim(), headerFormat);
            workSheet.addCell(lblHeader);
            colIndexStartFrom++;
        }
    }

    public void writeContentsHandler(WritableSheet workSheet, TreeMap<String, DmCsvFileFormat_Mdl.CsvContentType> declaredColumns,
            List<String> lstHeaders, List<DmDynamicGridRow> rows, String sheetName) throws Exception {

        writeContents(workSheet, declaredColumns, lstHeaders, rows, 0, 1);
    }

    public void writeContents(WritableSheet workSheet, TreeMap<String, DmCsvFileFormat_Mdl.CsvContentType> declaredColumns,
            List<String> lstHeaders, List<DmDynamicGridRow> rows, int colIndexStartFrom, int rowIndexStartFrom) throws Exception {

        WritableFont headerFont = new WritableFont(WritableFont.TAHOMA, contentFontSize, WritableFont.NO_BOLD);
        WritableCellFormat contentFormat = new WritableCellFormat(headerFont);
        CellView contentCellView = new CellView();
        contentCellView.setFormat(contentFormat);
        String headerColumnName;
        int colIndex;
        Object objValue;

        for (int r = 0; r < rows.size(); r++) {
            DmDynamicGridRow row = rows.get(r);
            colIndex = colIndexStartFrom;
            for (int i = 0; i < lstHeaders.size(); i++) {
                headerColumnName = lstHeaders.get(i).trim().toLowerCase();
                String content = "";
                objValue = row.getCells().get(headerColumnName);
                if (objValue != null) {
                    content = DmBaseShared.xNull((String) objValue);
                }

                WritableCell writableCell = null;
                if (declaredColumns.containsKey(headerColumnName)) {
                    if (declaredColumns.get(headerColumnName) == DmCsvFileFormat_Mdl.CsvContentType.Text) {
                        writableCell = new Label(colIndex, rowIndexStartFrom, content, contentFormat);
                    } else if (declaredColumns.get(headerColumnName) == DmCsvFileFormat_Mdl.CsvContentType.Numeric) {
                        writableCell = new jxl.write.Number(colIndex, rowIndexStartFrom, DmBaseShared.vNull(content), contentFormat);
                    }
                } else {
                    writableCell = new Label(colIndex, rowIndexStartFrom, content, contentFormat);
                }

                if (writableCell != null) {
                    workSheet.addCell(writableCell);
                }
                colIndex++;
            }
            rowIndexStartFrom++;
        }
    }

//    private void writeContents(WritableSheet workSheet, TreeMap<String, DmCsvFileFormat_Mdl.CsvContentType> declaredColumns, List<String> lstHeaders, List<DmDynamicGridRow> rows) throws Exception {
//        WritableFont headerFont = new WritableFont(WritableFont.TAHOMA, contentFontSize, WritableFont.NO_BOLD);
//        WritableCellFormat contentFormat = new WritableCellFormat(headerFont);
//        CellView contentCellView = new CellView();
//        contentCellView.setFormat(contentFormat);
//
//        for (int r = 0; r < rows.size(); r++) {
//            DmDynamicGridRow row = rows.get(r);
//            for (int i = 0; i < lstHeaders.size(); i++) {
//                String content = "";
//                if (row.getCells().get(lstHeaders.get(i).trim()) != null) {
//                    content = DmBaseShared.xNull((String) row.getCells().get(lstHeaders.get(i).trim()));
//                }
//
//                WritableCell writableCell = null;
//                if (declaredColumns.containsKey(lstHeaders.get(i).toLowerCase())) {
//                    if (declaredColumns.get(lstHeaders.get(i).toLowerCase()) == DmCsvFileFormat_Mdl.CsvContentType.Text) {
//                        writableCell = new Label(i, r + 1, content, contentFormat);
//                    } else if (declaredColumns.get(lstHeaders.get(i).toLowerCase()) == DmCsvFileFormat_Mdl.CsvContentType.Numeric) {
//                        writableCell = new jxl.write.Number(i, r + 1, DmBaseShared.vNull(content), contentFormat);
//                    }
//                } else {
//                    writableCell = new Label(i, r + 1, content, contentFormat);
//                }
//
//                if (writableCell != null) {
//                    workSheet.addCell(writableCell);
//                }
//            }
//        }
//    }
}
