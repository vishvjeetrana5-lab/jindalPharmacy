package aarogya.nabh.reports.server;

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
import aarogya.nabh.reports.shared.RptIndicator13_Srv;

public class RptIndicator13_SrvImpl extends DmRSSPrivate implements RptIndicator13_Srv {

    final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    //REPORT NAME->>=============  13.CQI 3d Percentage of Modification of Anaesthesia Plan(Indicator 13)

    @Override
    public DmResult<KeyFieldExtended> execute(String session, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sessionId = "", fileName = "";

        try {
            fileName = "Indicator13";

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

            WritableSheet sheet = workbook.createSheet("Percentage of Modification of AnaesthesiaPlan", 0);
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

        label = new Label(0, rowIndex, "13.CQI 3d Percentage of Modification of Anaesthesia Plan", cellFormatBold);
        sheet.addCell(label);
        // ================================= < END > ==========================================

        // =================================<  ROW >========================================
        rowIndex++;
        sheet.mergeCells(0, rowIndex, 0, rowIndex);                           // (startColIndex,startRowIndex,endColIndex,endRowIndex)
        sheet.setRowView(rowIndex, 34 * 26);
        label = new Label(0, rowIndex, "Month", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(1, rowIndex, 1, rowIndex);
        sheet.setRowView(rowIndex, 34 * 26);
        sheet.setColumnView(1, 30);
        label = new Label(1, rowIndex, "No. of patients in whom the anaesthesia plan was modified", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(2, rowIndex, 2, rowIndex);
        sheet.setRowView(rowIndex, 34 * 26);
        sheet.setColumnView(2, 20);
        label = new Label(2, rowIndex, "No. of patients who underwent anaesthesia", cellFormatColHead);
        sheet.addCell(label);

        sheet.mergeCells(3, rowIndex, 3, rowIndex);
        sheet.setRowView(rowIndex, 34 * 26);
        sheet.setColumnView(3, 25);
        label = new Label(3, rowIndex, "Percentage of modification of anaesthesia plan", cellFormatColHead);
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

        int colIndex = 0;
        WritableFont cellFontColumn = new WritableFont(WritableFont.createFont("Calibri"), 11, WritableFont.NO_BOLD);
        WritableCellFormat cellFormatColumn = new WritableCellFormat(cellFontColumn);
        cellFormatColumn.setAlignment(Alignment.CENTRE);
        cellFormatColumn.setVerticalAlignment(VerticalAlignment.TOP);
        cellFormatColumn.setBorder(Border.ALL, BorderLineStyle.THIN);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            condition += "  and ppr.wheelInDate between :fromDate and :toDate ";

            sqlQuery = "declare @totalOTBooking int "
                    + " set @totalOTBooking=(select count(ppr.docid) from patientPrescriptionRef as ppr "
                    + "	                     where  ppr.roomDiaryDocId <> 0 and ppr.wheelInDate between :fromDate and :toDate "
                    + "                     )  "
                    + "  Select  concat(CONVERT(varchar(3), max(mtb.wheelInDate), 100) , ' '  ,year(mtb.wheelInDate) )as monthYearName, "
                    + "  count(mtb.surgDocId) as  noOfModification ,  isnull(@totalOTBooking,0) as totalOTBooking ,"
                    + "  ((count(mtb.surgDocId))*100/ isnull(@totalOTBooking,0))	as percentOfModification	"
                    + "  from (   "
                    + "       select  ppr.docId AS surgDocId,ppr.wheelInDate "
                    + "	         from roomDiary rd   "
                    + "		 left join voucher_Type vt on vt.v_Type = rd.v_Type   "
                    + "		 left join patientPrescriptionRef ppr on ppr.roomDiaryDocId = rd.docId and ppr.roomDiaryV_Sno = rd.v_Sno   "
                    + "		 left join patientRegistration pr on pr.regDocId = ppr.regDocId   "
                    + "		 where vt.category in ('" + pvc.OT_BOOKING + "') and rd.anaesthesiaType <> ppr.anaesthesiaType       " + condition
                    + "	     ) as    mtb  group by month(mtb.wheelInDate),year(mtb.wheelInDate)   ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                /*===================================<< New Row >>====================================*/
                sheet.mergeCells(0, rowIndex, colIndex, rowIndex);
                label = new Label(0, rowIndex, DmShared.xNull(rsTemp.getString("monthYearName")), cellFormatColumn);
                sheet.addCell(label);

                colIndex++;
                sheet.mergeCells(colIndex, rowIndex, colIndex, rowIndex);
                label = new Label(colIndex, rowIndex, DmShared.xNull(rsTemp.getString("noOfModification")), cellFormatColumn);
                sheet.addCell(label);
                colIndex++;
                sheet.mergeCells(colIndex, rowIndex, colIndex, rowIndex);
                label = new Label(colIndex, rowIndex, DmShared.xNull(rsTemp.getString("totalOTBooking")), cellFormatColumn);
                sheet.addCell(label);
                colIndex++;
                sheet.mergeCells(colIndex, rowIndex, colIndex, rowIndex);
                label = new Label(colIndex, rowIndex, DmServer.vNull(rsTemp.getString("percentOfModification"), "0.00"), cellFormatColumn);
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
