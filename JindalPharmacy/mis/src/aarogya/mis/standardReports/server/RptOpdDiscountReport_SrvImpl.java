package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.xls.server.DmResultSetToExcelWriter;
import dataman.dmBase.self.xls.server.DmXlsColumn;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import java.io.File;
import java.sql.*;
import java.util.*;
import jxl.format.Alignment;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptOpdDiscountReport_SrvImpl extends DmRSSPrivate implements RptOpdDiscountReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCat = "'" + pvc.OPD + "','" + pvc.OPD_SERVICE + "'" + ",'" + pvc.OPD_DISCOUNT + "'";

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId,
            LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery;
        String condition;
        String fileName = widgetName.replaceAll(" ", "");
        try {
            RptLabDiscountReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = " and  (ps.v_Date between :fromDate and :toDate)";

            sqlQuery = "select  max(ps.v_Date) as v_Date, max(ps.recId) as recId, max(pr.recId) as regNo , max(pu.patientName) as patientName,"
                    + "                 sum(psd.discount) as discount,  "
                    + "			max(case when isnull(dis.initial, '') = '' then dis.name else concat(dis.initial, ' ', dis.name) end) as discountBy, "
                    + "			sum(psd.oh_Amt_Net) as oh_Amt_Net, max(ps.preparedBy) as preparedBy "
                    + "			from patientService ps  "
                    + "			left join voucher_Type vt on vt.v_Type = ps.v_Type  "
                    + "			left join patientServiceDetail psd on psd.DocId = ps.docId  "
                    + "			left join patientServiceMast psm on psm.code = psd.serviceCode  "
                    + "			left join subGroup dis on dis.subCode = psd.discountGivenBy  "
                    + "			left join patientRegistration pr on pr.regDocId = psd.regDocId  "
                    + "			left join patientUHID pu on pu.docId = pr.uhid  "
                    + "			where vt.category in ( " + vrCat + " )  " // ps.isCancelled = 0  and 
                    + "                 and isnull(psd.discount, '') <> 0  and ps.site_Code = :siteCode " + condition
                    + "			group by ps.DocId, psd.discountGivenBy  "
                    + "			order by max(ps.v_Date), max(ps.v_Time), max(vt.short_Name), max(ps.v_No), max(ps.recId) ";

//            sqlQuery = "select  max(ps.v_Date) as v_Date, max(ps.recId) as recId, max(pr.recId) as regNo , max(pu.patientName) as patientName, sum(psd.discount) as discount,  "
//                    + "			max(case when isnull(dis.initial, '') = '' then dis.name else concat(dis.initial, ' ', dis.name) end) as discountBy, "
//                    + "			 case when sum(psd.oh_Amt_Gross)> 0 then  sum(psd.oh_Amt_Gross) else sum(psdp.oh_Amt_Net	) end   as oh_Amt_Net , max(ps.preparedBy) as preparedBy "
//                    + "			from patientService ps  "
//                    + "			left join voucher_Type vt on vt.v_Type = ps.v_Type  "
//                    + "			left join patientServiceDetail psd on psd.DocId = ps.docId  "
//                    + "			left join patientServiceMast psm on psm.code = psd.serviceCode  "
//                    + "			left join subGroup dis on dis.subCode = psd.discountGivenBy  "
//                    + "			left join patientRegistration pr on pr.regDocId = psd.regDocId  "
//                    + "			left join patientUHID pu on pu.docId = pr.uhid  "
//                    + "                 left join patientServiceDetail psdp on psdp.regDocId = psd.regdocId  and  psdp.DocId <> psd.docId 	 "
//                    + "			where vt.category in ( " + vrCat + " )  "   // ps.isCancelled = 0  and 
//                    + "                 and isnull(psd.discount, '') <> 0  and ps.site_Code = :siteCode " + condition
//                    + "			group by ps.DocId, psd.discountGivenBy  "
//                    + "			order by max(ps.v_Date), max(ps.v_Time), max(vt.short_Name), max(ps.v_No), max(ps.recId) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            rsTemp = psTemp.executeQuery();
            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                LinkedHashMap hm = new LinkedHashMap();

                jasperReport = DmServer.compileJasper("MIS_OPDDiscountRepot");
                jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, hm, filter, rsTemp);
                keyField.setSharedKeyFieldName(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            }//======================================================<IF ExCEL>===============================
            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

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
                        Iterator<DmXlsColumn> i = column.iterator();

                        while (i.hasNext()) {
                            DmXlsColumn col = i.next();
                            if (col.getColumnName().equalsIgnoreCase("recId")
                                    ||col.getColumnName().equalsIgnoreCase("oh_Amt_Net")) {
                                i.remove();
                            }
                            switch (col.getDisplayName()) {
                                case "v_Date":
                                    col.setDisplayName("V.Date");
                                    break;
                                case "regNo":
                                    col.setDisplayName("V.No.");
                                    break;
                                case "patientName":
                                    col.setDisplayName("Patient Name");
                                    break;
                                case "discount":
                                    col.setDisplayName("Discount");
                                    break;
                                case "discountBy":
                                    col.setDisplayName("Consultant");
                                    break;
                                case "preparedBy":
                                    col.setDisplayName("User");
                                    break;
                            }

                        }

                    }

                    @Override
                    public void onPostWorksheetWritten(DmXlsSheet workSheet) throws Exception {

                        List<DmXlsColumn> lstCol = workSheet.getWorkSheetColumns();
                        Iterator<DmXlsColumn> i = lstCol.iterator();
                        int countCol = 0;
                        while (i.hasNext()) {
                            DmXlsColumn col = i.next();
                            workSheet.getWorkSheet().setColumnView(countCol++, 10 + col.getColumnName().length());
                        }
                    }
                });
                xlsRsTemp.setRecordColumnCallback(new DmResultSetToExcelWriter.IWorksheetRecordColumnCallback() {
                    @Override
                    public String configureNumericCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Number value) throws Exception {

                        return DmServer.vNull(value, "0.00");
                    }

                    @Override
                    public String configureTextCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, String text) throws Exception {
                        cellFormat.setWrap(true);
                        return text;
                    }

                    @Override
                    public String configureDateCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, java.util.Date value) throws Exception {
                        return DmShared.dNull(value);
                    }

                    @Override
                    public String configureTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, java.util.Date value) throws Exception {
                        return DmServer.vNull(value, "00.00");
                    }

                    @Override
                    public String configureDateTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, java.util.Date value) throws Exception {
                        return "";
                    }

                });

                xlsRsTemp.writeWorkBook(STServer.setXLSFileHeader(fileName, xlsRsTemp, widgetName, PageOrientation.LANDSCAPE, PaperSize.LEGAL, filter, this), 2);

                xlsRsTemp.commitChanges();

            }

            rstTemp.setData(keyField);
            rstTemp.setStatus(true);
            cnTemp.close();

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

}
