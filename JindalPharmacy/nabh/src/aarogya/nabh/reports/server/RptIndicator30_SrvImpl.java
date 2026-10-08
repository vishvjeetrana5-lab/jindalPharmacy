package aarogya.nabh.reports.server;

import aarogya.nabh.reports.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class RptIndicator30_SrvImpl extends DmRSSPrivate implements RptIndicator30_Srv {

    final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> execute(String session, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sessionId = "", fileName = "";

        try {
            fileName = "Surgical site infection rate";

            /*==========================CSV=================================================*/
            sessionId = session;

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + "/" + sessionId;
            String serverFileName = fileName + ".xls";
            String fullFilePath = directoryPath + "/" + serverFileName;

            File nFile = new File(fullFilePath);
            if (nFile.exists()) {
                nFile.delete();
            }

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            WritableWorkbook workbook = Workbook.createWorkbook(nFile);

            WritableSheet sheet = workbook.createSheet("Surgical site infection rate", 0);
            int rowindex = 0;
            rowindex = getHeader(sheet, rowindex);
            rowindex = getColumnData(sheet, rowindex, filter);

            workbook.write();
            workbook.close();

            rstTemp.setData(keyField);
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private int getHeader(WritableSheet sheet, int rowIndex) throws Exception {
        Label label;
        // ============================== < Header Title >===============================
        WritableFont cellFontHeader = new WritableFont(WritableFont.createFont("Calibri"), 14, WritableFont.BOLD);

        WritableCellFormat cellFormatBold = new WritableCellFormat(cellFontHeader);
        cellFormatBold.setAlignment(Alignment.CENTRE);
        cellFormatBold.setVerticalAlignment(VerticalAlignment.TOP);
        cellFormatBold.setBorder(Border.ALL, BorderLineStyle.THIN);

        // ============================== < Column Header  >===============================
        WritableFont cellFontColumnHeader = new WritableFont(WritableFont.createFont("Calibri"), 11, WritableFont.BOLD);
        WritableCellFormat cellFormatColHead = new WritableCellFormat(cellFontColumnHeader);
        cellFormatColHead.setAlignment(Alignment.CENTRE);
        cellFormatColHead.setVerticalAlignment(VerticalAlignment.TOP);
        cellFormatColHead.setBorder(Border.ALL, BorderLineStyle.THIN);
        cellFormatColHead.setWrap(true);

        // =================================<  ROW >========================================
        rowIndex += 0;
        sheet.mergeCells(0, rowIndex, 3, rowIndex);                              // (startColIndex,startRowIndex,endColIndex,endRowIndex)

        label = new Label(0, rowIndex, "30 CQI 3g Surgical site infection rate", cellFormatBold);
        sheet.addCell(label);
        rowIndex++;
        // ================================= < END > ==========================================

        sheet.mergeCells(0, rowIndex, 0, rowIndex);                           // (startColIndex,startRowIndex,endColIndex,endRowIndex)
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(0, 25);
        label = new Label(0, rowIndex, "", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(1, rowIndex, 0, rowIndex);                           // (startColIndex,startRowIndex,endColIndex,endRowIndex)
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(1, 25);
        label = new Label(1, rowIndex, "", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(2, rowIndex, 0, rowIndex);                           // (startColIndex,startRowIndex,endColIndex,endRowIndex)
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(2, 25);
        label = new Label(2, rowIndex, "", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(3, rowIndex, 3, rowIndex);
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(3, 25);
        label = new Label(3, rowIndex, "", cellFormatColHead);
        sheet.addCell(label);
        rowIndex++;
        // =================================<  ROW >========================================

        sheet.mergeCells(0, rowIndex, 0, rowIndex);                           // (startColIndex,startRowIndex,endColIndex,endRowIndex)
        sheet.setRowView(rowIndex, 32 * 26);
        label = new Label(0, rowIndex, "Month", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(1, rowIndex, 1, rowIndex);
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(1, 30);
        label = new Label(1, rowIndex, "N.of SSI", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(2, rowIndex, 2, rowIndex);
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(2, 20);
        label = new Label(2, rowIndex, "N. of Surgeries", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(3, rowIndex, 3, rowIndex);
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(3, 25);
        label = new Label(3, rowIndex, "SSI rate", cellFormatColHead);
        sheet.addCell(label);
        // ================================= < END > ==========================================

        rowIndex++;
        return rowIndex;

    }

    private int getColumnData(WritableSheet sheet, int rowIndex, LinkedHashMap<String, FieldUDT_Mdl> filter) throws Exception {
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        Label label;

        int i = 0;
        WritableFont cellFontColumn = new WritableFont(WritableFont.createFont("Calibri"), 11, WritableFont.NO_BOLD);
        WritableCellFormat cellFormatColumn = new WritableCellFormat(cellFontColumn);
        cellFormatColumn.setAlignment(Alignment.CENTRE);
        cellFormatColumn.setVerticalAlignment(VerticalAlignment.TOP);
        cellFormatColumn.setBorder(Border.ALL, BorderLineStyle.THIN);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            condition += "  and pir.v_Date between :fromDate and :toDate ";

            sqlQuery = "select max(concat(convert(char(3), pir.v_Date,0),'-', datePart(year, pir.v_Date))) as monthYearName, "
                    + " count(pir.infectionType) as NofSSI,(count(pir.infectionType)/1) as SSIrate "
                    + " from nabhPatientInfection as pir "
                    + " where infectionType = 'Surgical site infection' " + condition
                    + " group by datepart(mm, pir.v_Date) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                /*===================================<< New Row >>====================================*/
                sheet.mergeCells(0, rowIndex, i, rowIndex);
                label = new Label(0, rowIndex, DmShared.xNull(rsTemp.getString("monthYearName")), cellFormatColumn);
                sheet.addCell(label);

                i++;
                sheet.mergeCells(i, rowIndex, i, rowIndex);
                label = new Label(i, rowIndex, DmShared.xNull(rsTemp.getString("NofSSI")), cellFormatColumn);
                sheet.addCell(label);
                i++;
                sheet.mergeCells(i, rowIndex, i, rowIndex);
                label = new Label(i, rowIndex, DmShared.xNull("1"), cellFormatColumn);
                sheet.addCell(label);
                i++;
                sheet.mergeCells(i, rowIndex, i, rowIndex);
                label = new Label(i, rowIndex, DmShared.xNull(rsTemp.getString("SSIrate")), cellFormatColumn);
                sheet.addCell(label);

            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rowIndex;
    }

}
