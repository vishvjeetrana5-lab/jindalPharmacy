package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.xls.server.DmResultSetToExcelWriter;
import dataman.dmBase.self.xls.server.DmXlsColumn;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmToolkit.dmStatic.shared.DmShared;
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

public class RptIPDDiscountReport_SrvImpl extends DmRSSPrivate implements RptIPDDiscountReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCat = "'" + pvc.IPD_BILL + "'";
    private final String vrCatIPDService = "'" + pvc.IPD_SERVICE + "','" + pvc.OT_SERVICES + "','" + pvc.DISCHARGE_IPD + "' ," + "'" + pvc.IPD_ROOM_POSTING + "'," + "'" + pvc.IPD_SERVICE_BILLABLE + "'  ";

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
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

            sqlQuery = "select max(sb.v_date) as billDate, max(sb.recid) as billNo,max(pr.recid) as regNo,max(pu.patientName) "
                    + "as patientName,sum(psd.discount) as discount ,\n"
                    + "max(case when isnull(dis.initial, '') = '' then dis.name else concat(dis.initial, ' ', dis.name) end) as discountBy ,\n"
                    + "sum(psd.oh_Amt_Net) as oh_Amt_Net, max(sb.preparedBy) as preparedBy \n"
                    + "\n"
                    + "from patientserviceDetail as psd \n"
                    + "left join voucher_Type vt on vt.v_Type=psd.v_Type\n"
                    + "left join saleBill sb on  sb.docid=psd.sbdocid\n"
                    + "left join patientregistration as pr on pr.regDocid=psd.regDocid\n"
                    + "left join patientUHID pu on pu.docId = pr.uhid \n"
                    + "left join subGroup dis on dis.subCode = psd.discountGivenBy \n"
                    + "\n"
                    + "where psd.sbDocId is not null and psd.isCancelled=0 and psd.site_Code= :siteCode \n"
                    + "and psd.v_SNo < 32000 and psd.discountGivenBy is not null\n"
                    + "and ( sb.v_Date between :fromDate and :toDate )\n"
                    + "and vt.category in (" + vrCatIPDService + ") and sb.regDocId<> sb.docId \n"
                    + "\n"
                    + "group by sb.docid,sb.v_Date\n  "
                    + "order by sb.docid,sb.v_Date  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            rsTemp = psTemp.executeQuery();
            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                LinkedHashMap hm = new LinkedHashMap();

                jasperReport = DmServer.compileJasper("MIS_IPDDiscountReport");
                jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, hm, filter, rsTemp);
                keyField.setSharedKeyFieldName(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            }
//======================================================<IF ExCEL>===============================
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
                            if (col.getColumnName().equalsIgnoreCase("oh_Amt_Net")) {
                                i.remove();
                            }
                            switch (col.getDisplayName()) {
                                case "billDate":
                                    col.setDisplayName("Bill Date");
                                    break;
                                case "billNo":
                                    col.setDisplayName("Bill No.");
                                    break;
                                case "regNo":
                                    col.setDisplayName("Reg. No.");
                                    break;
                                case "patientName":
                                    col.setDisplayName("Patient Name");
                                    break;
                                case "discount":
                                    col.setDisplayName("Discount");
                                    break;
                                case "discountBy":
                                    col.setDisplayName("Discount By");
                                    break;
                                case "preparedBy":
                                    col.setDisplayName("Prepared By");
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
                        cellFormat.setAlignment(Alignment.RIGHT);

                        return DmServer.vNull(value, "00.00");
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
