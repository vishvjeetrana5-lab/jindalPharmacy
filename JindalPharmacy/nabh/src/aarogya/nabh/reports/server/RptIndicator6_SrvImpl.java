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

public class RptIndicator6_SrvImpl extends DmRSSPrivate implements RptIndicator6_Srv {

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
            fileName = "Rate of re-dos";

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

            WritableSheet sheet = workbook.createSheet("Rate of re-dos", 0);
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
        sheet.mergeCells(0, rowIndex, 18, rowIndex);                              // (startColIndex,startRowIndex,endColIndex,endRowIndex)

        label = new Label(0, rowIndex, "6 CQI 3b.Rate of re-dos", cellFormatBold);
        sheet.addCell(label);
        // ================================= < END > ==========================================

        rowIndex++;
        return rowIndex;

    }

    private int getColumnData(WritableSheet sheet, int rowIndex, LinkedHashMap<String, FieldUDT_Mdl> filter) throws Exception {
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", fields = "";
        Label label;
        int colIndex = 0, colDischargeOdd = 2, colADREven = 1;
        WritableFont cellFontColumn = new WritableFont(WritableFont.createFont("Calibri"), 11, WritableFont.NO_BOLD);
        WritableCellFormat cellFormatColumn = new WritableCellFormat(cellFontColumn);
        cellFormatColumn.setAlignment(Alignment.CENTRE);
        cellFormatColumn.setVerticalAlignment(VerticalAlignment.CENTRE);
        cellFormatColumn.setBorder(Border.ALL, BorderLineStyle.THIN);
        cellFormatColumn.setWrap(true);
        LinkedHashMap<String, Integer> hmRoomADR = new LinkedHashMap<String, Integer>();
        LinkedHashMap<String, Integer> hmRoomDischarge = new LinkedHashMap<String, Integer>();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select * from patientServiceCatMast where name in ('PATHOLOGY','X-RAY','ULTRASOUND') order by name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                /*===================================<< New Row >>====================================*/

                sheet.mergeCells(0, rowIndex, 0, rowIndex);
                label = new Label(0, rowIndex, "", cellFormatColumn);
                sheet.addCell(label);

                sheet.mergeCells(colIndex + 1, rowIndex, colIndex + 2, rowIndex);
                label = new Label(colIndex + 1, rowIndex, DmShared.xNull(rsTemp.getString("name")), cellFormatColumn);
                sheet.addCell(label);

                sheet.mergeCells(0, rowIndex + 1, 0, rowIndex + 1);
                sheet.setRowView(rowIndex + 1, 50 * 26);
                label = new Label(0, rowIndex + 1, "Month", cellFormatColumn);
                sheet.addCell(label);

                sheet.mergeCells(colIndex + 1, rowIndex + 1, colIndex + 1, rowIndex + 1);
                sheet.setRowView(rowIndex + 1, 50 * 26);
                label = new Label(colIndex + 1, rowIndex + 1, "N. of re-dos", cellFormatColumn);
                sheet.addCell(label);

                sheet.mergeCells(colIndex + 2, rowIndex + 1, colIndex + 2, rowIndex + 1);
                sheet.setRowView(rowIndex + 1, 50 * 26);
                label = new Label(colIndex + 2, rowIndex + 1, "N. of tests performed", cellFormatColumn);
                sheet.addCell(label);
                colIndex += 2;
//                fields += " ,sum(case when cm.isOB=1 and rcm.code = " + DmShared.xNull(rsTemp.getString("code")) + " then 1 else 0 end) as [noOfADr" + DmShared.xNull(rsTemp.getString("name")) + "],"
//                        + " sum(case when pp.docId is not null and rcm.code = " + DmShared.xNull(rsTemp.getString("code")) + "  then 1 else 0 end) as [noOfDischarge" + DmShared.xNull(rsTemp.getString("name")) + "],"
//                        + " sum(case when cm.isOB=1 then 1 else 0 end) as noOfADRTotal,sum(case when pp.docId is not null then 1 else 0 end) as noOfDischargeTotal,"
//                        + " (sum(case when cm.isOB=1 then 1 else 0 end)*100/ sum(case when pp.docId is not null then 1 else 0 end)) as totalPercent";
//                hmRoomADR.put("noOfADr" + DmShared.xNull(rsTemp.getString("name")).trim(), 0);
//                hmRoomDischarge.put("noOfDischarge" + DmShared.xNull(rsTemp.getString("name")).trim(), 0);

            }

            psTemp.close();
            rsTemp.close();

            sheet.mergeCells(colIndex + 1, rowIndex, colIndex + 2, rowIndex);
            label = new Label(colIndex + 1, rowIndex, "Total	", cellFormatColumn);
            sheet.addCell(label);

            sheet.mergeCells(colIndex + 3, rowIndex, colIndex + 3, rowIndex);
            label = new Label(colIndex + 3, rowIndex, "	", cellFormatColumn);
            sheet.addCell(label);

            sheet.mergeCells(colIndex + 1, rowIndex + 1, colIndex + 1, rowIndex + 1);
            sheet.setRowView(rowIndex + 1, 50 * 26);
            label = new Label(colIndex + 1, rowIndex + 1, "N. of re-dos", cellFormatColumn);
            sheet.addCell(label);

            sheet.mergeCells(colIndex + 2, rowIndex + 1, colIndex + 2, rowIndex + 1);
            sheet.setRowView(rowIndex + 1, 50 * 26);
            label = new Label(colIndex + 2, rowIndex + 1, "N. of tests performed", cellFormatColumn);
            sheet.addCell(label);

            sheet.mergeCells(colIndex + 3, rowIndex + 1, colIndex + 3, rowIndex + 1);
            sheet.setRowView(rowIndex + 1, 50 * 26);
            sheet.setColumnView(colIndex + 3, 18);
            label = new Label(colIndex + 3, rowIndex + 1, "Rate of re-dos", cellFormatColumn);
            sheet.addCell(label);

            rowIndex += 1;
            condition += "  and pr.v_date between :fromDate and :toDate ";

//            sqlQuery = "select  concat(CONVERT(varchar(3), max(pp.v_date), 100),' ',year(pp.v_date)) as monthYear "
//                    + fields
//                    + " from patientPrescription as pp"
//                    + " left join voucher_Type as vt on pp.v_Type=vt.v_Type"
//                    + " left join patientRegistration as pr on pp.regDocId=pr.regDocId"
//                    + " left join roomMast rm on rm.code= pr.roomCode"
//                    + " left join roomCatMast rcm on rcm.code=rm.roomCategoryCode"
//                    + " left join caseMast as cm on pr.caseCode=cm.code"
//                    + " where vt.category='" + pvc.DISCHARGE_CARD_IPD + "'" + condition
//                    + " group by month(pp.v_Date),year(pp.v_Date)   ";

            sqlQuery = "select  max(concat(convert(char(3), pr.v_Date,0),'-', datePart(year, pr.v_Date))) as regMonth, "
                    + "		count(pr.regDocId) as regCount, sum(psd.testCount) as testCount "
                    + "		from patientRegistration pr "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "		left join ("
                    + "			select  psd.regDocId, count(psd.serviceCode) as testCount "
                    + "					from patientServiceDetail psd "
                    + "					left join voucher_Type vt on vt.v_Type = psd.v_Type "
                    + "					where psd.isCancelled = 0 and psd.site_Code = 1 and vt.category in ('LABRE') "
                    + "					group by psd.regDocId "
                    + "		) psd on psd.regDocId = pr.regDocId "
                    + "		where vt.category in ('LABRE') and pr.site_Code = 1 and pr.isCancelled = 0 " + condition
                    + "		group by datepart(mm, pr.v_Date) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                rowIndex++;
                sheet.mergeCells(0, rowIndex, 0, rowIndex);
                label = new Label(0, rowIndex, DmShared.xNull(rsTemp.getString("regMonth")), cellFormatColumn);
                sheet.addCell(label);
                int indexADR = 0, indexDis = 0;
                for (String colADR : hmRoomADR.keySet()) {
                    // sheet.mergeCells(colADREven + indexADR, rowIndex, colADREven + indexADR, rowIndex);
                    label = new Label(colADREven + indexADR, rowIndex, DmShared.xNull(rsTemp.getString(colADR)), cellFormatColumn);
                    sheet.addCell(label);
                    indexADR += 2;
                }
                for (String colDis : hmRoomDischarge.keySet()) {
                    //sheet.mergeCells(colDischargeOdd + indexDis, rowIndex, colDischargeOdd + indexDis, rowIndex);
                    label = new Label(colDischargeOdd + indexDis, rowIndex, DmShared.xNull(rsTemp.getString(colDis)), cellFormatColumn);
                    sheet.addCell(label);
                    indexDis += 2;
                }
                // sheet.mergeCells(colADREven + indexADR, rowIndex, colADREven + indexADR, rowIndex);
                label = new Label(colADREven + indexADR, rowIndex, DmShared.iNull(rsTemp.getString("regCount")).toString(), cellFormatColumn);
                sheet.addCell(label);

                // sheet.mergeCells(colDischargeOdd + indexDis, rowIndex, colDischargeOdd + indexDis, rowIndex);
                label = new Label(colDischargeOdd + indexDis, rowIndex, DmShared.iNull(rsTemp.getString("testCount")).toString(), cellFormatColumn);
                sheet.addCell(label);

                //sheet.mergeCells(colDischargeOdd + indexDis + 1, rowIndex, colDischargeOdd + indexDis + 1, rowIndex);
//                label = new Label(colDischargeOdd + indexDis + 1, rowIndex, DmServer.vNull(rsTemp.getString("testCount")), cellFormatColumn);
//                sheet.addCell(label);

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
