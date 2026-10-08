package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptOPDServicesReport_Srv;
import aarogya.mis.standardReports.shared.RptOPDServicesReport_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.xls.server.DmResultSetToExcelWriter;
import dataman.dmBase.self.xls.server.DmXlsColumn;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import jxl.format.Alignment;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptOPDServicesReport_SrvImpl extends DmRSSPrivate implements RptOPDServicesReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String filterCategory = "'" + pvc.OPD_SERVICE + "'";

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint;
        String mainQuery, condition = "", groupCode = "", groupName = "";
        String fileName = widgetName.replaceAll(" ", "");
        try {
            RptOPDServicesReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition += " and (psd.v_Date between :fromDate and :toDate) ";

            if (!DmShared.xNull(filter.get("discountDisplay").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  isnull(psd.discount, 0) <> 0  ";
            }

            if (!DmShared.xNull(filter.get("fromTime").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  psd.v_Time >= :fromTime ";
            }
            if (!DmShared.xNull(filter.get("toTime").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  psd.v_Time <= :toTime ";
            }

            if (null != RptOPDServicesReport_Vld.ReportGroup.getByText(DmShared.xNull(filter.get("reportGroup").getText()).trim())) {

                switch (RptOPDServicesReport_Vld.ReportGroup.getByText(DmShared.xNull(filter.get("reportGroup").getText()).trim())) {

                    case Consultant_Wise:
                        groupCode = " pr.consultantCode";
                        groupName = "case when isnull(con.initial,'') = '' then con.name else concat(con.initial,' ', con.name) end";
                        break;
                    case User_Wise:
                        groupCode = "ps.preparedBy";
                        groupName = "ps.preparedBy";
                        break;

                    default:
                        groupCode = "''";
                        groupName = "''";
                        break;
                }
            }

            mainQuery = "select	" + groupCode + " as groupCode, " + groupName + " as groupName, ps.docId, ps.recId, "
                    + "         ps.v_Date, vt.description, "
                    + "         psd.v_Type, pr.recId as regNo, pu.recId as uhid, psd.discountReason, "
                    + "         case when isnull(pu.patientInitial,'') = '' then pu.patientName else concat(pu.patientInitial,' ', pu.patientName) end as patientName, "
                    + "         psd.serviceCode, "
                    + "          ps.preparedBy, psm.name as serviceName, concat(con.initial, ' ', con.name) consultant,"
                    + "         psd.quantity,psd.rate, psd.grossAmount, psd.discount, psd.oh_Amt_Net, "
                    + "         pr.dobDisplay,  gen.shortName gender, "
                    + "         case when org.manualCode = 'CP' then '' else org.manualCode end as orgShortName,"
                    + "         case when isnull(dis.initial,'') = '' then dis.name else concat(dis.initial,' ', dis.name) end  as discountBy "
                    + "		from patientService ps"
                    + "		left join voucher_Type vt on vt.v_Type = ps.v_Type "
                    + "		left join patientServiceDetail psd on psd.docId = ps.docId "
                    + "		left join patientRegistration pr on pr.regDocId = psd.regDocId "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pr.uhid "
                    + "		left join subGroup org on org.subCode = pr.organizationCode "
                    + "		left join subGroup dis on dis.subCode = psd.discountGivenBy "
                    + "		left join subGroup con on con.subCode = pr.consultantCode "
                    + "		left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "		left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "		left join hl7mast gen on gen.code = pu2.genderCode"
                    + "		where vt.category in (" + filterCategory + ") and psd.isCancelled = 0 "
                    + "             and ps.site_Code = :siteCode and psm.shortName <> 'CF'  " + condition
                    + "         order by groupName, ps.v_Date, ps.v_Time, vt.short_Name, ps.v_No, ps.recId ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, mainQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }

            if (!DmShared.xNull(filter.get("fromTime").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("fromTime", DmShared.xNull(filter.get("fromTime").getText()));
            }
            if (!DmShared.xNull(filter.get("toTime").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("toTime", DmShared.xNull(filter.get("toTime").getText()));
            }

            rsTemp = psTemp.executeQuery();
            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                LinkedHashMap hm = new LinkedHashMap();

                jasperReport = DmServer.compileJasper("MIS_OPDServicesReport");

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
                            if (col.getColumnName().equalsIgnoreCase("groupCode")
                                    || col.getColumnName().equalsIgnoreCase("groupName")
                                    || col.getColumnName().equalsIgnoreCase("docId")
                                    || col.getColumnName().equalsIgnoreCase("description")
                                    || col.getColumnName().equalsIgnoreCase("v_Type")
                                    || col.getColumnName().equalsIgnoreCase("serviceCode")
                                    || col.getColumnName().equalsIgnoreCase("gender")
                                    || col.getColumnName().equalsIgnoreCase("uhid")
                                    || col.getColumnName().equalsIgnoreCase("docId")
                                    || col.getColumnName().equalsIgnoreCase("discountReason")
                                    || col.getColumnName().equalsIgnoreCase("PreparedBy")) {
                                i.remove();
                            }
                            switch (col.getDisplayName()) {
                                case "v_Date":
                                    col.setDisplayName("V.Date");
                                    break;
                                case "quantity":
                                    col.setDisplayName("Unit");
                                    break;
                                case "recId":
                                    col.setDisplayName("Doc.No.");
                                    break;
                                case "regNo":
                                    col.setDisplayName("OPD No.");
                                    break;
                                case "discountReason":
                                    col.setDisplayName("Discount Reason");
                                    break;
                                case "patientName":
                                    col.setDisplayName("Patient Name");
                                    break;
                                case "discountBy":
                                    col.setDisplayName("Discount By");
                                    break;
                                case "serviceName":
                                    col.setDisplayName("Service");
                                    break;

                                case "grossAmount":
                                    col.setDisplayName("Gross Amt.");
                                    break;
                                case "discount":
                                    col.setDisplayName("Discount");
                                    break;

                                case "uhid":
                                    col.setDisplayName("UHID No.");
                                    break;
                                case "orgShortName":
                                    col.setDisplayName("Organization");
                                    break;
                                case "dobDisplay":
                                    col.setDisplayName("Age");
                                    break;
                                case "rate":
                                    col.setDisplayName("Rate");
                                    break;
                                case "oh_Amt_Net":
                                    col.setDisplayName("Net.Amt.");
                                    break;
                                case "consultant":
                                    col.setDisplayName("Consultant");
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("All");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Only Discounted");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTimeWise(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getReportGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (RptOPDServicesReport_Vld.ReportGroup group : RptOPDServicesReport_Vld.ReportGroup.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(group.getText());
                listNode.setSharedKeyFieldName(group.getText());
                lstTemp.add(listNode);
            }
            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
