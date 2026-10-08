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

public class RptIndicator61_SrvImpl extends DmRSSPrivate implements RptIndicator61_Srv {

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
            fileName = "Incidence of blood body fluid exposures ";

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

            WritableSheet sheet = workbook.createSheet("Incidence of blood body fluid exposures", 0);
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

        // =================================<  ROW Header - 1>========================================
        rowIndex += 0;
        sheet.mergeCells(0, rowIndex, 3, rowIndex);                              // (startColIndex,startRowIndex,endColIndex,endRowIndex)

        label = new Label(0, rowIndex, "61 a.CQI 4f Incidence of blood body fluid exposures (in IPD area)", cellFormatBold);
        sheet.addCell(label);

        // ================================= < END > ==========================================
        // =================================<  ROW Header - 2 >========================================
        rowIndex += 0;
        sheet.mergeCells(5, rowIndex, 8, rowIndex);                              // (startColIndex,startRowIndex,endColIndex,endRowIndex)

        label = new Label(5, rowIndex, "61 b.CQI 4f Incidence of blood body fluid exposures (in OPD area)", cellFormatBold);
        sheet.addCell(label);
        rowIndex++;
        // ================================= < END > ==========================================

        // =================================<  ROW Header - 1>========================================
        sheet.mergeCells(0, rowIndex, 0, rowIndex);                           // (startColIndex,startRowIndex,endColIndex,endRowIndex)
        sheet.setRowView(rowIndex, 32 * 26);
        label = new Label(0, rowIndex, "Month", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(1, rowIndex, 1, rowIndex);
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(1, 30);
        label = new Label(1, rowIndex, "Number of blood body fluid exposures", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(2, rowIndex, 2, rowIndex);
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(2, 20);
        label = new Label(2, rowIndex, "N. of in-patient days", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(3, rowIndex, 3, rowIndex);
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(3, 25);
        label = new Label(3, rowIndex, "Incidence of blood body fluid exposures (in IPD area)", cellFormatColHead);
        sheet.addCell(label);
        // ================================= < END > ==========================================

        // =================================<  ROW Header - 2>========================================
        sheet.mergeCells(5, rowIndex, 5, rowIndex);                           // (startColIndex,startRowIndex,endColIndex,endRowIndex)
        sheet.setRowView(rowIndex, 32 * 26);
        label = new Label(5, rowIndex, "Month", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(6, rowIndex, 6, rowIndex);
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(6, 30);
        label = new Label(6, rowIndex, "Number of blood body fluid exposures", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(7, rowIndex, 7, rowIndex);
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(7, 20);
        label = new Label(7, rowIndex, "N. of OPD patient visits", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(8, rowIndex, 8, rowIndex);
        sheet.setRowView(rowIndex, 32 * 26);
        sheet.setColumnView(8, 25);
        label = new Label(8, rowIndex, "Incidnece of blood body fluid exposures(in  OPD area)", cellFormatColHead);
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
        int j = 5;
        WritableFont cellFontColumn = new WritableFont(WritableFont.createFont("Calibri"), 11, WritableFont.NO_BOLD);
        WritableCellFormat cellFormatColumn = new WritableCellFormat(cellFontColumn);
        cellFormatColumn.setAlignment(Alignment.CENTRE);
        cellFormatColumn.setVerticalAlignment(VerticalAlignment.TOP);
        cellFormatColumn.setBorder(Border.ALL, BorderLineStyle.THIN);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            condition += "  and pir.v_Date between :fromDate and :toDate ";

            sqlQuery = "select max(concat(convert(char(3), pir.v_Date,0),'-', datePart(year, pir.v_Date))) as monthYearName, "
                    + "  count(pir.infectionType) as NofBloodBodyFluidExpos, sum(dbo.getAdmitDays(pr.regDocId)) as NofinPatientdays, "
                    + " (sum(dbo.getAdmitDays(pr.regDocId))/count(pir.infectionType))  as IncidenceOfBloodBodyFluidExposuresIPD "
                    + " from nabhPatientInfection as pir "
                    + "	left join patientRegistration as pr on pr.regDocId = pir.regDocId "
                    + " left join voucher_Type as vt on vt.v_type = pr.v_Type "
                    + " where infectionType = 'Incidence of blood body fluid exposures' " +condition
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
                label = new Label(i, rowIndex, DmShared.xNull(rsTemp.getString("NofBloodBodyFluidExpos")), cellFormatColumn);
                sheet.addCell(label);
                i++;
                sheet.mergeCells(i, rowIndex, i, rowIndex);
                label = new Label(i, rowIndex, DmShared.xNull(rsTemp.getString("NofinPatientdays")), cellFormatColumn);
                sheet.addCell(label);
                i++;
                sheet.mergeCells(i, rowIndex, i, rowIndex);
                label = new Label(i, rowIndex, DmShared.xNull(rsTemp.getString("IncidenceOfBloodBodyFluidExposuresIPD")), cellFormatColumn);
                sheet.addCell(label);

            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select max(concat(convert(char(3), pir.v_Date,0),'-', datePart(year, pir.v_Date))) as monthYearName, "
                    + " count(pir.infectionType) as NofBloodBodyFluidExpos, count(dbo.getAdmitDays(pr.regDocId)) as NofinPatientdays, "
                    + "	(count(dbo.getAdmitDays(pr.regDocId))/count(pir.infectionType))  as IncidenceOfBloodBodyFluidExposuresOPD "
                    + " from nabhPatientInfection as pir "
                    + "	left join patientRegistration as pr on pr.regDocId = pir.regDocId "
                    + "	left join voucher_Type as vt on vt.v_type = pr.v_Type "
                    + "  where infectionType = 'Incidence of blood body fluid exposures' " +condition
                    + "  group by datepart(mm, pir.v_Date) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                /*===================================<< New Row >>====================================*/
                sheet.mergeCells(5, rowIndex, j, rowIndex);
                label = new Label(5, rowIndex, DmShared.xNull(rsTemp.getString("monthYearName")), cellFormatColumn);
                sheet.addCell(label);

                j++;
                sheet.mergeCells(j, rowIndex, j, rowIndex);
                label = new Label(j, rowIndex, DmShared.xNull(rsTemp.getString("NofBloodBodyFluidExpos")), cellFormatColumn);
                sheet.addCell(label);
                j++;
                sheet.mergeCells(j, rowIndex, j, rowIndex);
                label = new Label(j, rowIndex, DmShared.xNull(rsTemp.getString("NofinPatientdays")), cellFormatColumn);
                sheet.addCell(label);
                j++;
                sheet.mergeCells(j, rowIndex, j, rowIndex);
                label = new Label(j, rowIndex, DmShared.xNull(rsTemp.getString("IncidenceOfBloodBodyFluidExposuresOPD")), cellFormatColumn);
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
