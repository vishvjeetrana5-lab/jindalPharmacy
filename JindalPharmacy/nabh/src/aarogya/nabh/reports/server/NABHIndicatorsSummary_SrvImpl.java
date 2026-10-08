package aarogya.nabh.reports.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.projectMenu.self.shared.pmNABH;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.xls.server.DmXlsColumn;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmBase.self.xls.server.DmXlsWriter;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import aarogya.nabh.reports.shared.NABHIndicatorsSummary_Srv;
import aarogya.nabh.self.shared.NABHIndicators_Mdl;
import aarogya.nabh.self.shared.NABH_Shared;

public class NABHIndicatorsSummary_SrvImpl extends DmRSSPrivate implements NABHIndicatorsSummary_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = " ";

        try {
//            RptFileReceive_Vld.validateInput(filter);
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//
//            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
//                condition += " AND  fi.v_Date >= :fromDate ";
//            }
//            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
//                condition += " AND  fi.v_Date <= :toDate ";
//            }
//            if (!DmShared.xNull(filter.get("issueStaff").getTag()).trim().equalsIgnoreCase("")) {
//                condition += " AND fi.issueToStaff IN(" + DmServer.getParamList(":issueStaff", filter.get("issueStaff").getTag()) + " )";
//            }
//            if (!DmShared.xNull(filter.get("issueDepartment").getTag()).trim().equalsIgnoreCase("")) {
//                condition += " AND fi.issueToDepartment IN(" + DmServer.getParamList(":issueDepartment", filter.get("issueDepartment").getTag()) + " )";
//            }
//
//            sqlQuery = "select    "
//                    + " fi.recId as issueNo, fi.v_Date as issueDt,pu.recId  as uhid,   "
//                    + " pr.recId as ipdNo,CASE WHEN isNull(pu.patientInitial, '') <> '' THEN CONCAT(pu.patientInitial, ' ', pu.patientName) ELSE pu.patientName END AS patient,   "
//                    + " concat(pu.guardianInitial,' ',pu.guardianName) as guardian,pu.dobDisplay,hm.name AS gender, "
//                    + " sdm.name as isuDeaprt,sg.name as isuStaff,frg.recId as fileNo,frg.v_Date as fileDt,frg.remark, "
//                    + " frq.recId as reqNo,fi.receiveBy,fi.receiveDate "
//                    + " from fileIssue as fi   "
//                    + " left join fileRequisition as frq on frq.docId = fi.fileReqDocId "
//                    + "  left join fileRegistration as frg on fi.fileRegDOcId = frg.docId "
//                    + " left join patientRegistration as pr on pr.regDocId = frg.regDocId   "
//                    + " left join patientUHID as pu on pu.docId = pr.uhid   "
//                    + " left join subgroup as sdm on sdm.subcode = fi.issueToDepartment "
//                    + " left join subGroup as sg on sg.subCode = fi.issueToStaff  "
//                    + " left join voucher_Type as vt on vt.v_Type = fi.v_Type   "
//                    + " LEFT JOIN patientuhid2 as pu2 on pu.docId=pu2.uhid "
//                    + "	LEFT JOIN hl7Mast as hm on pu2.genderCode=hm.code"
//                    + " where vt.category in ('FLISU') AND isNull(fi.receiveBy,'')<>''  " + condition
//                    + " ORDER BY fi.receiveDate ASC, vt.category ASC, fi.recId ";
//
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
//                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
//            }
//            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
//                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
//            }
//            if (!DmShared.xNull(filter.get("issueStaff").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("issueStaff", DmShared.xNull(filter.get("issueStaff").getTag()), true);
//            }
//            if (!DmShared.xNull(filter.get("issueDepartment").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("issueDepartment", DmShared.xNull(filter.get("issueDepartment").getTag()), true);
//            }
//
//            rsTemp = psTemp.executeQuery();

            rstTemp.setData(createExcelFile(rsTemp));

//            HashMap hm = new HashMap();
//
//            String reportTitle = "";
//            if ((!DmShared.dNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase(""))
//                    && (!DmShared.dNull(filter.get("toDate").getText()).trim().equalsIgnoreCase(""))) {
//                reportTitle = "from " + DmShared.dNull(filter.get("fromDate").getText()) + " to "
//                        + DmShared.dNull(filter.get("toDate").getText());
//            } else if ((!DmShared.dNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase(""))
//                    && (DmShared.dNull(filter.get("toDate").getText()).trim().equalsIgnoreCase(""))) {
//                reportTitle = "from " + DmShared.dNull(filter.get("fromDate").getText());
//            } else if ((!DmShared.dNull(filter.get("toDate").getText()).trim().equalsIgnoreCase(""))
//                    && (DmShared.dNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase(""))) {
//                reportTitle = "upto " + DmShared.dNull(filter.get("toDate").getText());
//            }
//
//            jasperReport = DmServer.compileJasper("MRD_FileReceiveReport");
//
//            jasperPrint = DmServer.fillJasper(this, "File Receive Report " + reportTitle, jasperReport, hm, filter, rsTemp);
//            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
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

    private String createExcelFile(ResultSet resultSet) throws Exception {
        String filePath = DmBaseServer.Path.sessions + "/" + getLocalRequest().getSession().getId().trim();
        // String fileName = new SimpleDateFormat("ddMMMyyyyHHmmss").format(new java.util.Date()) + ".xls";
        String fileName = UUID.randomUUID().toString() + ".xls";
        List<NABHIndicators_Mdl> menuRecords = NABH_Shared.getNABHIndicators_Report();

        DmXlsWriter xlsWriter = new DmXlsWriter(filePath, fileName);
        xlsWriter = xlsWriter.setWorkSheetCreationCallback(new DmXlsWriter.IXlsWriterWorkSheetCreationCallback() {
            @Override
            public boolean onWorkSheetCreating(DmXlsSheet workSheet) {
                return true;
            }

            @Override
            public String getDefaultFontName() {
                return "Arial";
            }

            @Override
            public int getDefaultFontSize() {
                return 10;
            }

            @Override
            public Alignment getDefaultHorizontalAlignment() {
                return Alignment.LEFT;
            }

            @Override
            public VerticalAlignment getDefaultVerticalAlignment() {
                return VerticalAlignment.CENTRE;
            }
        }).build(new pmNABH().NABH_INDICATORS_SUMMARY.caption);

        DmXlsSheet workSheet = xlsWriter.getDefaultWorkSheet();
        workSheet.setDefaultFontName(fileName);

        int rowIndex = 0;

        workSheet.getWorkSheetColumns().add(new DmXlsColumn("menu", "Menu", DmXlsColumn.XlsContentType.Text));
        workSheet.getWorkSheetColumns().add(new DmXlsColumn("qi", "QI", DmXlsColumn.XlsContentType.Text));
        workSheet.getWorkSheetColumns().add(new DmXlsColumn("std", "STD", DmXlsColumn.XlsContentType.Text));
        workSheet.getWorkSheetColumns().add(new DmXlsColumn("bo", "Bed Occupancy", DmXlsColumn.XlsContentType.Text));
        workSheet.getWorkSheetColumns().add(new DmXlsColumn("blank1", "", DmXlsColumn.XlsContentType.Text));
        workSheet.getWorkSheetColumns().add(new DmXlsColumn("blank2", "", DmXlsColumn.XlsContentType.Text));
        workSheet.getWorkSheetColumns().add(new DmXlsColumn("formula", "Formula", DmXlsColumn.XlsContentType.Text));
        workSheet.getWorkSheetColumns().add(new DmXlsColumn("result", "Result", DmXlsColumn.XlsContentType.Text));

        workSheet.getWorkSheet().setRowView(rowIndex, 1150);
        workSheet.mergeCells(0, rowIndex, workSheet.getWorkSheetColumns().size() - 1, rowIndex);

        WritableFont cellFont = new WritableFont(WritableFont.createFont("Calibri"), 20, WritableFont.BOLD);

        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        workSheet.getWorkSheet().setColumnView(0, 100);

        Label lblHeader = new Label(0, rowIndex, "BASIC SHEET TO UNDERSTAND THE DATA MAPPING AND ANALYSIS FOR \n QUALITY INDICATORS AS PER NABH 4th \n EDITION", cellFormat);
        workSheet.getWorkSheet().addCell(lblHeader);

        rowIndex += 1;

        for (int i = 0; i < workSheet.getWorkSheetColumns().size(); i++) {
            cellFont = new WritableFont(WritableFont.createFont("Calibri"), 10, WritableFont.BOLD);
            cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            cellFormat.setBackground(Colour.YELLOW);
            DmXlsColumn col = workSheet.getWorkSheetColumns().get(i);
            if (!DmShared.xNull(col.getDisplayName()).trim().equals("")) {
                if (col.getColumnName().equalsIgnoreCase("bo") || col.getColumnName().equalsIgnoreCase("formula")) {
                    workSheet.getWorkSheet().setColumnView(i, col.getDisplayName().length() + 25);
                } else {
                    workSheet.getWorkSheet().setColumnView(i, col.getDisplayName().length() + 7);
                }
            } else {
                workSheet.getWorkSheet().setColumnView(i, 15);
            }
            Label lbl = new Label(i, rowIndex, col.getDisplayName(), cellFormat);
            workSheet.getWorkSheet().addCell(lbl);
        }

        rowIndex += 1;

        for (int s = 0; s < menuRecords.size(); s++) {
            List<Map<String, String>> listRecords = menuRecords.get(s).getRows();

            //////////////////printing of subsection name
            String subSectionName = DmShared.xNull(menuRecords.get(s).getGroupName()).trim();
            if (!subSectionName.equals("")) {
                cellFont = new WritableFont(WritableFont.createFont("Calibri"), 10, WritableFont.BOLD);
                cellFormat = new WritableCellFormat(cellFont);
                cellFormat.setAlignment(Alignment.CENTRE);
                cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
                cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                cellFormat.setShrinkToFit(true);
                cellFormat.setBackground(Colour.GREEN);
                workSheet.mergeCells(2, rowIndex, 4, rowIndex);
                Label lbl = new Label(2, rowIndex, subSectionName, cellFormat);
                workSheet.getWorkSheet().addCell(lbl);
                rowIndex += 1;
            }

            ////////////////printing of subsection records
            for (int i = 0; i < listRecords.size(); i++) {
                Map<String, String> cells = listRecords.get(i);
                for (int c = 0; c < cells.size(); c++) {
                    cellFont = new WritableFont(WritableFont.createFont("Calibri"), 10, WritableFont.NO_BOLD);
                    cellFormat = new WritableCellFormat(cellFont);
                    cellFormat.setAlignment(Alignment.CENTRE);
                    cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
                    cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                    cellFormat.setWrap(true);
                    DmXlsColumn col = workSheet.getWorkSheetColumns().get(c);
                    String cellValue = cells.get(col.getColumnName());
                    if (!DmShared.xNull(col.getDisplayName()).trim().equals("")) {
                        if (col.getColumnName().equalsIgnoreCase("bo") || col.getColumnName().equalsIgnoreCase("formula")) {
                            workSheet.getWorkSheet().setColumnView(c, col.getDisplayName().length() + 25);
                        } else {
                            workSheet.getWorkSheet().setColumnView(c, col.getDisplayName().length() + 7);
                        }
                    } else {
                        workSheet.getWorkSheet().setColumnView(c, 15);
                    }
                    workSheet.getWorkSheet().setRowView(rowIndex, 700);
                    Label lbl = new Label(c, rowIndex, cellValue, cellFormat);
                    workSheet.getWorkSheet().addCell(lbl);
                }
                rowIndex += 1;
            }

            ///////////printing of merged rows and columns
//            for (int m = 0; m < menuRecords.get(s).getMergeConfigurations().size(); m++) {
//                SubSectionMergeConfiguration config = menuRecords.get(s).getMergeConfigurations().get(m);
//                workSheet.mergeCells(config.getStartColIndex(), config.getStartRowIndex(), config.getEndColIndex(), config.getEndRowIndex());
//            }

        }

        xlsWriter.commit();
        xlsWriter.close();

        //  return filePath + "/" + fileName;
        return fileName;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIssueToStaff(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" select	sg.subCode as code, sg.displayName as name  "
                    + "		from subGroup as sg "
                    + "		left join subgroupNature as sgn on sg.nature = sgn.code "
                    + "		where sgn.manualCode in ('EM') and sg.isActive = 1 and sg.site_Code = :site_Code ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIssueDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT sdm.subCODE, sdm.NAME "
                    + "FROM departmentMast dm "
                    + "left join subgroup sdm on dm.subcode = sdm.subcode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
