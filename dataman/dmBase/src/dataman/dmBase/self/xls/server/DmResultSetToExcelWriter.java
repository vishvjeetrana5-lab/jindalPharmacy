package dataman.dmBase.self.xls.server;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class DmResultSetToExcelWriter implements DmXlsWriter.IXlsWriterWorkSheetCreationCallback {

    public interface IWorksheetCallback {

        public void onPreWorksheetWriting(DmXlsSheet workSheet) throws Exception;

        public void configureWorksheetColumns(List<DmXlsColumn> column) throws Exception;

        public void onPostWorksheetWritten(DmXlsSheet workSheet) throws Exception;
    }

    public interface IWorksheetRecordColumnCallback {

        public String configureNumericCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Number value) throws Exception;

        public String configureTextCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, String text) throws Exception;

        public String configureDateCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception;

        public String configureTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception;

        public String configureDateTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception;
    }

    public interface IWorksheetRecordCallback {

        public void postRecordWritten(DmXlsSheet workSheet, TreeMap<String, Object> record, int rowIndex) throws Exception;
    }

    private ResultSet resultSet;
    private DmXlsWriter xlsWriter;

    private String defaultFontName;
    private int defaultFontSize = -1;
    private Alignment defaultHorizontalAlignment;
    private VerticalAlignment defaultVerticalAlignment;

    private IWorksheetCallback workSheetCallback;
    private IWorksheetRecordColumnCallback recordColumnCallback;
    private IWorksheetRecordCallback recordCallback;

    private TreeMap<String, Number> totalValues;

    public DmResultSetToExcelWriter(ResultSet rsTemp, String outputDirectory, String outputFileName) throws Exception {
        this.totalValues = new TreeMap<>();
        this.resultSet = rsTemp;
        this.xlsWriter = new DmXlsWriter(outputDirectory, outputFileName);
    }

    public DmResultSetToExcelWriter setDefaultFontName(String defaultFontName) {
        this.defaultFontName = DmBaseShared.xNull(defaultFontName).trim();
        return this;
    }

    public DmResultSetToExcelWriter setDefaultFontSize(int defaultFontSize) {
        this.defaultFontSize = defaultFontSize;
        return this;
    }

    public DmResultSetToExcelWriter setDefaultHorizontalAlignment(Alignment defaultHorizontalAlignment) {
        this.defaultHorizontalAlignment = defaultHorizontalAlignment;
        return this;
    }

    public DmResultSetToExcelWriter setDefaultVerticalAlignment(VerticalAlignment defaultVerticalAlignment) {
        this.defaultVerticalAlignment = defaultVerticalAlignment;
        return this;
    }

    public IWorksheetCallback getWorkSheetCallback() {
        return workSheetCallback;
    }

    public DmResultSetToExcelWriter setWorkSheetCallback(IWorksheetCallback workBookColumnsCallback) {
        this.workSheetCallback = workBookColumnsCallback;
        return this;
    }

    public IWorksheetRecordColumnCallback getRecordColumnCallback() {
        return recordColumnCallback;
    }

    public DmResultSetToExcelWriter setRecordColumnCallback(IWorksheetRecordColumnCallback recordColumnCallback) {
        this.recordColumnCallback = recordColumnCallback;
        return this;
    }

    public IWorksheetRecordCallback getRecordCallback() {
        return recordCallback;
    }

    public DmResultSetToExcelWriter setRecordCallback(IWorksheetRecordCallback recordCallback) {
        this.recordCallback = recordCallback;
        return this;
    }

    @Override
    public boolean onWorkSheetCreating(DmXlsSheet workSheet) {
        return true;
    }

    @Override
    public String getDefaultFontName() {
        return this.defaultFontName;
    }

    @Override
    public int getDefaultFontSize() {
        return this.defaultFontSize;
    }

    @Override
    public Alignment getDefaultHorizontalAlignment() {
        return defaultHorizontalAlignment;
    }

    @Override
    public VerticalAlignment getDefaultVerticalAlignment() {
        return defaultVerticalAlignment;
    }

    private void validateRequiredParameters() throws Exception {
        if (DmBaseShared.xNull(defaultFontName).trim().equals("")) {
            throw new Exception("Default Font Name is Mandatory to create Excel sheet...");
        }
        if (defaultFontSize == -1) {
            throw new Exception("Default Font Size is Mandatory to create Excel Sheet...");
        }
        if (defaultHorizontalAlignment == null) {
            throw new Exception("Default Horizontal Alignment is Mandatory to create Excel Sheet...");
        }
        if (defaultVerticalAlignment == null) {
            throw new Exception("Default Vertical Alignment is Mandatory to create Excel Sheet...");
        }
    }

    public int getResultSetColumnCount() throws Exception {
        return this.resultSet.getMetaData().getColumnCount();
    }

    public DmXlsSheet createWorkBook(String name) throws Exception {
        DmXlsSheet tempSheet;
        try {
            xlsWriter = xlsWriter.setWorkSheetCreationCallback(this).build(name);
            tempSheet = xlsWriter.getDefaultWorkSheet();

        } finally {

        }
        return tempSheet;
    }

    public void writeWorkBook(DmXlsSheet tempSheet, int startRowIndex) throws Exception {
        try {

            validateRequiredParameters();

            if (this.resultSet == null) {
                throw new Exception("ResultSet is not found to write Excel...");
            }

            parseResultSetColumns(tempSheet);
            if (this.workSheetCallback != null) {
                this.workSheetCallback.onPreWorksheetWriting(tempSheet);
                this.workSheetCallback.configureWorksheetColumns(tempSheet.getWorkSheetColumns());
            }

            createTotalColumnsVars(tempSheet);

            int rowIndex = startRowIndex + 1;

            for (int i = 0; i < tempSheet.getWorkSheetColumns().size(); i++) {
                DmXlsColumn col = tempSheet.getWorkSheetColumns().get(i);
                WritableFont writableFont = new WritableFont(WritableFont.createFont(col.getHeaderCellFontName()), col.getHeaderCellFontSize(), WritableFont.BOLD);
                WritableCell cellToAdd = null;
                WritableCellFormat cellFormat = new WritableCellFormat(writableFont);
                cellFormat.setAlignment(col.getHeaderCellHorizontalAlignment());
                cellFormat.setVerticalAlignment(col.getHeaderCellVerticalAlignment());
                String cellFormattedValue = DmBaseShared.xNull(col.getDisplayName());

                Label lblText = new Label(i, rowIndex, cellFormattedValue, cellFormat);
                cellToAdd = lblText;

                tempSheet.getWorkSheet().addCell(cellToAdd);
            }

            rowIndex += 1;

            while (resultSet.next()) {
                TreeMap<String, Object> record = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                for (int i = 0; i < tempSheet.getWorkSheetColumns().size(); i++) {
                    Object cellValue = null;
                    DmXlsColumn col = tempSheet.getWorkSheetColumns().get(i);
                    WritableFont writableFont = new WritableFont(WritableFont.createFont(col.getHeaderCellFontName()), col.getHeaderCellFontSize());
                    WritableCell cellToAdd = null;
                    String cellFormattedValue = col.getFormatString();
                    WritableCellFormat cellFormat = new WritableCellFormat(writableFont);
                    cellFormat.setAlignment(col.getHeaderCellHorizontalAlignment());
                    cellFormat.setVerticalAlignment(col.getHeaderCellVerticalAlignment());
                    if (recordColumnCallback != null) {
                        if (col.getSQLValueType() == java.sql.Types.BIGINT) {
                            cellValue = resultSet.getLong(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureNumericCell(col, cellFormat, writableFont, (Number) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.DECIMAL) {
                            cellValue = resultSet.getDouble(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureNumericCell(col, cellFormat, writableFont, (Number) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.DOUBLE) {
                            cellValue = resultSet.getDouble(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureNumericCell(col, cellFormat, writableFont, (Number) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.FLOAT) {
                            cellValue = resultSet.getDouble(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureNumericCell(col, cellFormat, writableFont, (Number) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.INTEGER) {
                            cellValue = resultSet.getInt(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureNumericCell(col, cellFormat, writableFont, (Number) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.NUMERIC) {
                            cellValue = resultSet.getDouble(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureNumericCell(col, cellFormat, writableFont, (Number) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.SMALLINT) {
                            cellValue = resultSet.getInt(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureNumericCell(col, cellFormat, writableFont, (Number) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.TINYINT) {
                            cellValue = resultSet.getInt(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureNumericCell(col, cellFormat, writableFont, (Number) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.CHAR) {
                            cellValue = DmBaseShared.xNull(resultSet.getString(col.getColumnName())).trim();
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureTextCell(col, cellFormat, writableFont, (String) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.LONGNVARCHAR) {
                            cellValue = DmBaseShared.xNull(resultSet.getString(col.getColumnName())).trim();
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureTextCell(col, cellFormat, writableFont, (String) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.LONGVARCHAR) {
                            cellValue = DmBaseShared.xNull(resultSet.getString(col.getColumnName())).trim();
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureTextCell(col, cellFormat, writableFont, (String) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.NCHAR) {
                            cellValue = DmBaseShared.xNull(resultSet.getString(col.getColumnName())).trim();
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureTextCell(col, cellFormat, writableFont, (String) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.NVARCHAR) {
                            cellValue = DmBaseShared.xNull(resultSet.getString(col.getColumnName())).trim();
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureTextCell(col, cellFormat, writableFont, (String) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.VARCHAR) {
                            cellValue = DmBaseShared.xNull(resultSet.getString(col.getColumnName())).trim();
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureTextCell(col, cellFormat, writableFont, (String) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.DATE) {
                            cellValue = resultSet.getDate(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureDateCell(col, cellFormat, writableFont, (Date) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.TIME) {
                            cellValue = resultSet.getTime(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureDateCell(col, cellFormat, writableFont, (Date) cellValue);
                        } else if (col.getSQLValueType() == java.sql.Types.TIMESTAMP) {
                            cellValue = resultSet.getTimestamp(col.getColumnName());
                            record.put(col.getColumnName(), cellValue);
                            cellFormattedValue = this.recordColumnCallback.configureDateCell(col, cellFormat, writableFont, (Date) cellValue);
                        }
                    }

                    if (!DmBaseShared.xNull(cellFormattedValue).trim().equals("")) {
                        if (col.getContentType() == DmXlsColumn.XlsContentType.Text) {
                            Label lblText = new Label(i, rowIndex, cellFormattedValue, cellFormat);
                            cellToAdd = lblText;
                        } else if (col.getContentType() == DmXlsColumn.XlsContentType.Numeric) {
                            Double tempValue = Double.parseDouble(cellFormattedValue);
                            if (totalValues.containsKey(col.getColumnName())) {
                                totalValues.put(col.getColumnName(), totalValues.get(col.getColumnName()).doubleValue() + tempValue);
                            }
                            jxl.write.Number numberLabel = new jxl.write.Number(i, rowIndex, tempValue, cellFormat);
                            cellToAdd = numberLabel;
                        } else if ((col.getContentType() == DmXlsColumn.XlsContentType.Date) || (col.getContentType() == DmXlsColumn.XlsContentType.Time) || (col.getContentType() == DmXlsColumn.XlsContentType.DateTime)) {
                            Label dateLabel = new Label(i, rowIndex, cellFormattedValue, cellFormat);
                            cellToAdd = dateLabel;
                        }
                    } else {
                        Label lblText = new Label(i, rowIndex, "", cellFormat);
                        cellToAdd = lblText;
                    }

                    tempSheet.getWorkSheet().addCell(cellToAdd);
                }

                if (this.recordCallback != null) {
                    this.recordCallback.postRecordWritten(tempSheet, record, rowIndex);
                }

                rowIndex += 1;
            }

            if (this.totalValues.size() > 0) {
                Iterator<String> keys = this.totalValues.keySet().iterator();
                while (keys.hasNext()) {
                    String colName = keys.next();
                    Number cellValue = null;
                    int colIndex = tempSheet.getWorkSheetColumns().indexOf(new DmXlsColumn(colName, ""));
                    DmXlsColumn col = tempSheet.getWorkSheetColumns().get(colIndex);
                    WritableFont writableFont = new WritableFont(WritableFont.createFont(col.getHeaderCellFontName()), col.getHeaderCellFontSize(), WritableFont.BOLD);
                    String cellFormattedValue = col.getFormatString();
                    WritableCellFormat cellFormat = new WritableCellFormat(writableFont);
                    cellFormat.setAlignment(col.getHeaderCellHorizontalAlignment());
                    cellFormat.setVerticalAlignment(col.getHeaderCellVerticalAlignment());
                    if (recordColumnCallback != null) {
                        cellValue = this.totalValues.get(colName);
                        cellFormattedValue = this.recordColumnCallback.configureNumericCell(col, cellFormat, writableFont, (Number) cellValue);
                        jxl.write.Number numberLabel = new jxl.write.Number(colIndex, rowIndex, cellValue.doubleValue(), cellFormat);
                        tempSheet.getWorkSheet().addCell(numberLabel);
                    }
                }
            }

            rowIndex += 1;

            if (this.workSheetCallback != null) {
                this.workSheetCallback.onPostWorksheetWritten(tempSheet);
            }
        } finally {

        }
    }

    public void commitChanges() throws Exception {
        if (this.xlsWriter != null) {
            this.xlsWriter.commit();
            this.xlsWriter.close();
        }
    }

    private void createTotalColumnsVars(DmXlsSheet tempSheet) throws Exception {
        this.totalValues.clear();
        List<DmXlsColumn> columns = tempSheet.getWorkSheetColumns().stream().filter(c -> c.isTotalColumn()).collect(Collectors.toList());
        for (int i = 0; i < columns.size(); i++) {
            DmXlsColumn column = columns.get(i);
            if (!totalValues.containsKey(column.getColumnName())) {
                totalValues.put(column.getColumnName(), 0d);
            }
        }
    }

    public TreeMap<String, Number> getTotalValues() {
        return totalValues;
    }

    private void parseResultSetColumns(DmXlsSheet tempSheet) throws Exception {
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            String columnName = DmBaseShared.xNull(metaData.getColumnName(i + 1)).trim();
            String displayName = DmBaseShared.xNull(metaData.getColumnLabel(i + 1)).trim();
            DmXlsColumn column = new DmXlsColumn(columnName, displayName);
            column.setHeaderCellFontName(defaultFontName);
            column.setHeaderCellFontSize(defaultFontSize);
            column.setHeaderCellHorizontalAlignment(defaultHorizontalAlignment);
            column.setHeaderCellVerticalAlignment(defaultVerticalAlignment);
            int columnType = metaData.getColumnType(i + 1);
            column.setSQLValueType(columnType);
            if ((columnType == java.sql.Types.BIGINT) || (columnType == java.sql.Types.DECIMAL) || (columnType == java.sql.Types.DOUBLE) || (columnType == java.sql.Types.FLOAT) || (columnType == java.sql.Types.INTEGER) || (columnType == java.sql.Types.NUMERIC) || (columnType == java.sql.Types.REAL) || (columnType == java.sql.Types.SMALLINT) || (columnType == java.sql.Types.TINYINT)) {
                column.setContentType(DmXlsColumn.XlsContentType.Numeric);
            } else if ((columnType == java.sql.Types.CHAR) || (columnType == java.sql.Types.LONGVARCHAR) || (columnType == java.sql.Types.LONGNVARCHAR) || (columnType == java.sql.Types.NCHAR) || (columnType == java.sql.Types.NVARCHAR) || (columnType == java.sql.Types.VARCHAR)) {
                column.setContentType(DmXlsColumn.XlsContentType.Text);
            } else if (columnType == java.sql.Types.DATE) {
                column.setContentType(DmXlsColumn.XlsContentType.Date);
            } else if (columnType == java.sql.Types.TIME) {
                column.setContentType(DmXlsColumn.XlsContentType.Time);
            } else if (columnType == java.sql.Types.TIMESTAMP) {
                column.setContentType(DmXlsColumn.XlsContentType.DateTime);
            }
            tempSheet.getWorkSheetColumns().add(column);
        }
    }

    public DmXlsWriter getXlsWriter() {
        return xlsWriter;
    }
}
