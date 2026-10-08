package aarogya.mis.reports.server;

import aarogya.mis.reports.shared.RptDateWiseCollection_Srv;
import aarogya.mis.reports.shared.RptDateWiseCollection_Vld;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.xls.server.DmResultSetToExcelWriter;
import dataman.dmBase.self.xls.server.DmXlsColumn;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import jxl.CellReferenceHelper;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class RptDateWiseCollection_SrvImpl extends DmRSSPrivate implements RptDateWiseCollection_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    private final String vCatOPD = "'" + pvc.OPD_REGISTRATION_RECEIPT + "','" + pvc.OPD_SERVICE_RECEIPT + "','" + pvc.OPD_RECEIPT + "',"
            + "'" + pvc.OPD_REFUND_RECEIPT + "','" + pvc.UHID_REGISTRATION_RECEIPT + "','" + pvc.OPD_APPOINTMENT_RECEIPT + "'";

    private final String vCatIPD = "'" + pvc.IPD_RECEIPT + "','" + pvc.IPD_REFUND_RECEIPT + "','" + pvc.ADVANCE_BOOKING_RECEIPT + "'";

    private final String vCatEmergency = "'" + pvc.EMERGENCY_RECEIPT + "','" + pvc.EMERGENCY_REFUND_RECEIPT + "'";

    private final String vCatDAYC = "'" + pvc.DAY_CARE_RECEIPT + "','" + pvc.DAY_CARE_REFUND_RECEIPT + "'";

    private final String vCatLAB = "'" + pvc.LAB_REGISTRATION_RECEIPT + "','" + pvc.LAB_REFUND_RECEIPT + "','" + pvc.LAB_RECEIPT + "'";

    private final String vCatPharmacy = "'" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "',"
            + "'" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "'";

    private final String vCatTPA = "'" + pvc.TPA_PATIENT_REFUND_RECEIPT + "','" + pvc.TPA_PATIENT_RECEIPT + "','" + pvc.PATIENT_RECEIPT + "',"
            + "'" + pvc.PATIENT_REFUND_RECEIPT + "','" + pvc.ORGANIZATION_BILL_SETTLEMENT_RECEIPT + "',"
            + "'" + pvc.ORGANIZATION_BILL_SETTLEMENT_HOSPITAL_HOLD + "'";

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", additionalFields = "", fileName = widgetName.replaceAll(" ", "");
        try {
            RptDateWiseCollection_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            /*==========================CSV=================================================*/

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(widgetName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId;
            String serverFileName = STShared.getValidFileName(fileName) + ".xls";
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

            sqlQuery = "select  vt.description, vt.v_Type  "
                    + "		from voucher_Type vt   "
                    + "		where vt.category in (" + vCatLAB + ") and vt.isActive = 1   "
                    + "		order by vt.category, vt.description";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getQuery(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                additionalFields += ", sum(case when vt.v_Type in (" + rsTemp.getString("v_Type") + ") then prc.amount else 0 end)  "
                        + " as [" + DmShared.xNull(rsTemp.getString("description")).trim() + "]";

            }
            rsTemp.close();
            psTemp.close();
            sqlQuery = "select  prc.v_date [Date],  "
                    + "         sum(case when vt.category in (" + vCatOPD + ") then prc.amount else 0 end) as [OPD], "
                    + "         sum(case when vt.category in (" + vCatIPD + "," + vCatEmergency + ") then prc.amount else 0 end) as [IPD/Emergency], "
                    + "         sum(case when vt.category in (" + vCatPharmacy + ") then prc.amount else 0 end) as [Pharmacy], "
                    + "         sum(case when vt.category in (" + vCatTPA + ") then prc.amount else 0 end) as [TPA], "
                    + "         sum(case when vt.category in ( " + vCatDAYC + ") then prc.amount else 0 end) as [Day Care]"
                    + additionalFields + ", sum(prc.amount) [Total] "
                    + "         from patientReceipt prc  "
                    + "         left join voucher_type vt on vt.v_type = prc.v_type  "
                    + "         where prc.site_code = :siteCode and prc.v_Date between :fromDate and :toDate and prc.isCancelled = 0  "
                    + "         group by prc.v_date "
                    + "         order by prc.v_date ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getQuery(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            DmResultSetToExcelWriter xlsRsTemp = new DmResultSetToExcelWriter(rsTemp, directoryPath, serverFileName)
                    .setDefaultFontName("Tahoma")
                    .setDefaultFontSize(11)
                    .setDefaultHorizontalAlignment(Alignment.LEFT)
                    .setDefaultVerticalAlignment(VerticalAlignment.TOP);

            xlsRsTemp.setWorkSheetCallback(new DmResultSetToExcelWriter.IWorksheetCallback() {
                @Override
                public void onPreWorksheetWriting(DmXlsSheet workSheet) throws Exception {

                }

                @Override
                public void configureWorksheetColumns(List<DmXlsColumn> column) throws Exception {

                }

                @Override
                public void onPostWorksheetWritten(DmXlsSheet workSheet) throws Exception {
                    WritableFont cellFontBold;
                    WritableCellFormat cellFormat;
                    Label lbl;
                    Formula formula;
                    String columnName = "", sumValue = "";
                    int lastRowIndex = workSheet.getWorkSheet().getRows();
                    int lastColumnIndex = workSheet.getWorkSheet().getColumns();
                    for (int i = 0; i <= workSheet.getWorkSheet().getRows(); i++) {
                        cellFontBold = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD);
                        cellFormat = new WritableCellFormat(cellFontBold);
                        cellFormat.setAlignment(Alignment.LEFT);
                        cellFormat.setBorder(Border.NONE, BorderLineStyle.THIN);
                        cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
                        if (i == 0) {
                            lbl = new Label(0, lastRowIndex, "Total", cellFormat);
                            workSheet.getWorkSheet().addCell(lbl);
                        } else if (i <= (lastColumnIndex - 1)) {
                            cellFormat.setAlignment(Alignment.RIGHT);
                            columnName = CellReferenceHelper.getColumnReference(i);
                            sumValue = columnName + "5:" + columnName + DmShared.iNull(lastRowIndex).toString();
                            formula = new Formula(i, lastRowIndex, "SUM(" + sumValue + ")", cellFormat);
                            workSheet.getWorkSheet().addCell(formula);
                        }
                    }
                    STServer.setXLSFileFooter(workSheet, RptDateWiseCollection_SrvImpl.this);
                }
            });
            xlsRsTemp.setRecordColumnCallback(new DmResultSetToExcelWriter.IWorksheetRecordColumnCallback() {
                @Override
                public String configureNumericCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Number value) throws Exception {
                    cellFormat.setAlignment(Alignment.RIGHT);
                    return DmServer.vNull(value, "00");
                }

                @Override
                public String configureTextCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, String text) throws Exception {
                    return text;
                }

                @Override
                public String configureDateCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception {
                    return DmShared.dNull(value);
                }

                @Override
                public String configureTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception {
                    return DmServer.vNull(value, "00.00");
                }

                @Override
                public String configureDateTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Date value) throws Exception {
                    return "";
                }
            });

            xlsRsTemp.writeWorkBook(STServer.setXLSFileHeader(fileName, xlsRsTemp, widgetName, PageOrientation.LANDSCAPE, PaperSize.LEGAL, filter, this), 2);

            xlsRsTemp.commitChanges();

//            String FILE_HEADER = " Date, OPD, IPD/Emergency, Pharmacy, TPA, DAYCARE ";
//            for (String head : grandTotal.keySet()) {
//                FILE_HEADER += "," + "\"" + head + "\"";
//            }
//            FILE_HEADER += ",Total";
//
//            fileWriter.append(FILE_HEADER);
//            fileWriter.append(NEW_LINE_SEPARATOR);
//
//            while (rsTemp.next()) {
//
//                lineData = "\"" + DmShared.xNull(rsTemp.getString("vDate")) + "\"" + COMMA_DELIMITER;
//                lineData += "\"" + DmServer.vNull(rsTemp.getString("OPDCollection"), "0.00") + "\"" + COMMA_DELIMITER;
//                lineData += "\"" + DmServer.vNull(rsTemp.getString("IPDCollection"), "0.00") + "\"" + COMMA_DELIMITER;
//                lineData += "\"" + DmServer.vNull(rsTemp.getString("PharmacyCollection"), "0.00") + "\"" + COMMA_DELIMITER;
//                lineData += "\"" + DmServer.vNull(rsTemp.getString("TPACollection"), "0.00") + "\"" + COMMA_DELIMITER;
//                lineData += "\"" + DmServer.vNull(rsTemp.getString("DayCareCollection"), "0.00") + "\"" + COMMA_DELIMITER;
//                for (String head : grandTotal.keySet()) {
//                    lineData += "\"" + DmServer.vNull(rsTemp.getString(head), "0.00") + "\"" + COMMA_DELIMITER;
//                    grandTotal.put(head, DmShared.vNull(DmServer.vNull(grandTotal.get(head), "0.00")).doubleValue() + DmShared.vNull(DmServer.vNull(rsTemp.getString(head), "0.00")).doubleValue());
//                }
//                lineData += "\"" + DmServer.vNull(rsTemp.getString("RowTotal"), "0.00") + "\"" + COMMA_DELIMITER;
//
//                totalOPD += DmShared.vNull(DmServer.vNull(rsTemp.getString("OPDCollection"), "0.00"));
//                totalIPD += DmShared.vNull(DmServer.vNull(rsTemp.getString("IPDCollection"), "0.00"));
//                totalPharmacy += DmShared.vNull(DmServer.vNull(rsTemp.getString("PharmacyCollection"), "0.00"));
//                totalTPA += DmShared.vNull(DmServer.vNull(rsTemp.getString("TPACollection"), "0.00"));
//                totalDAYCARE += DmShared.vNull(DmServer.vNull(rsTemp.getString("DayCareCollection"), "0.00"));
//                fileWriter.append(lineData);
//                fileWriter.append(NEW_LINE_SEPARATOR);
//            }
//            rsTemp.close();
//            psTemp.close();
//
//            //append grand total
//            lineData = "\"" + "Total" + "\"" + COMMA_DELIMITER;
//            lineData += "\"" + DmServer.vNull(totalOPD, "0.00") + "\"" + COMMA_DELIMITER;
//            lineData += "\"" + DmServer.vNull(totalIPD, "0.00") + "\"" + COMMA_DELIMITER;
//            lineData += "\"" + DmServer.vNull(totalPharmacy, "0.00") + "\"" + COMMA_DELIMITER;
//            lineData += "\"" + DmServer.vNull(totalTPA, "0.00") + "\"" + COMMA_DELIMITER;
//            lineData += "\"" + DmServer.vNull(totalDAYCARE, "0.00") + "\"" + COMMA_DELIMITER;
//
//            for (String head : grandTotal.keySet()) {
//                lineData += "\"" + DmServer.vNull(grandTotal.get(head), "0.00") + "\"" + COMMA_DELIMITER;
//                lastTotal += DmShared.vNull(DmServer.vNull(grandTotal.get(head), "0.00"));
//            }
//            lastTotal = totalOPD + totalIPD + totalPharmacy + totalTPA + totalDAYCARE + totalLab;
//            lineData += "\"" + DmServer.vNull(lastTotal, "0.00") + "\"" + COMMA_DELIMITER;
//
//            fileWriter.append(lineData);
//            fileWriter.append(NEW_LINE_SEPARATOR);
//
//            fileWriter.flush();
//            fileWriter.close();
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

    public DmPreparedStatement getQuery(HashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
        } catch (Exception e) {
        }
        return psTemp;
    }

}
