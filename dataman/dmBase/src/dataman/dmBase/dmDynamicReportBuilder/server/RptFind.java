package dataman.dmBase.dmDynamicReportBuilder.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmDynamicReportBuilder.shared.DmMasterReport;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.xls.server.DmPrintFindExcel;
import dataman.dmBase.self.xls.server.DmXlsColumn;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import java.io.File;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import jxl.format.Alignment;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class RptFind {

    private DmMasterReport mReport;
    private DmPreparedStatement stmt;

    public RptFind() {

    }

    public DmMasterReport getMasterReport() {
        return mReport;
    }

    public void setMasterReport(DmMasterReport mReport) {
        this.mReport = mReport;
    }

    public DmPreparedStatement getPreparedStatement() {
        return stmt;
    }

    public void setPreparedStatement(DmPreparedStatement stmt) {
        this.stmt = stmt;
    }

    public KeyField printFind(DmRSS rssTemp) {
        KeyField keyField = new KeyField();
        ResultSet rsTemp = null;
        String sessionId = "", fileName = "";
        fileName = this.mReport.getTitle().trim().replace(" ", "");

        try {
            rsTemp = this.stmt.executeQuery();
            if (this.mReport == null) {
                throw new Exception("excel not found..");
            }

            if (this.stmt == null) {
                throw new Exception("datasource not found..");
            }
            sessionId = DmBaseServer.getSessionId(rssTemp.getLocalRequest());
            String directoryPath = DmBaseServer.Path.sessions + File.separatorChar + sessionId;
            String serverFileName = fileName + ".xls";
            String fullFilePath = directoryPath + File.separatorChar + serverFileName;

            File sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            File nFile = new File(fullFilePath);
            if (nFile.exists()) {
                nFile.delete();
            }
            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);
            DmPrintFindExcel xlsRsTemp = new DmPrintFindExcel(rsTemp, directoryPath, serverFileName)
                    .setDefaultFontName("Tahoma")
                    .setDefaultFontSize(11)
                    .setDefaultHorizontalAlignment(Alignment.LEFT)
                    .setDefaultVerticalAlignment(VerticalAlignment.TOP);

            xlsRsTemp.setWorkSheetCallback(new DmPrintFindExcel.IWorksheetCallback() {
                @Override
                public void onPreWorksheetWriting(DmXlsSheet workSheet) throws Exception {

                }

                @Override
                public void configureWorksheetColumns(List<DmXlsColumn> column) throws Exception {

                }

                @Override
                public void onPostWorksheetWritten(DmXlsSheet workSheet) throws Exception {
                    DmBaseServer.setXLSFileFooter(workSheet, rssTemp);

                }
            });
            xlsRsTemp.setRecordColumnCallback(new DmPrintFindExcel.IWorksheetRecordColumnCallback() {
                @Override
                public String configureNumericCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Number value) throws Exception {
                    cellFormat.setAlignment(Alignment.RIGHT);
                    return DmBaseServer.vNull(value, "00.00");
                }

                @Override
                public String configureTextCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, String text) throws Exception {
                    return text;
                }

                @Override
                public String configureDateCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception {
                    return DmBaseShared.dNull(value);
                }

                @Override
                public String configureTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception {
                    return DmBaseServer.vNull(value, "00.00");
                }

                @Override
                public String configureDateTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception {
                    return "";
                }
            });

//            xlsRsTemp.writeWorkBook(DmBaseServer.setXLSFileHeader(fileName, xlsRsTemp, this.mReport.getTitle(), PageOrientation.LANDSCAPE, PaperSize.LEGAL, new LinkedHashMap<String, FieldUDT_Mdl>(), rssTemp), 3, this.mReport);
            xlsRsTemp.writeWorkBook(this.mReport.getTitle(), PageOrientation.LANDSCAPE, PaperSize.LEGAL, mReport, rssTemp);
            xlsRsTemp.commitChanges();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                    rsTemp = null;
                }
            } catch (Exception ex) {
            }
        }
        return keyField;
    }
}
