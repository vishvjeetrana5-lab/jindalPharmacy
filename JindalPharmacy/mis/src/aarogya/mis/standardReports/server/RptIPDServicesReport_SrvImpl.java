package aarogya.mis.standardReports.server;

import aarogya.mis.standardReports.shared.RptIPDServicesReport_Srv;
import aarogya.mis.standardReports.shared.RptIPDServicesReport_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
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
import dataman.dmBase.self.shared.DmSQLWithParam;
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

public class RptIPDServicesReport_SrvImpl extends DmRSSPrivate implements RptIPDServicesReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vrCatBill = "'" + pvc.IPD_BILL + "'";

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        String sqlQuery, condition;
        String fileName = widgetName.replaceAll(" ", "");
        try {
            RptIPDServicesReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = " and (isnull(sb.v_Date, :toDate) between :fromDate and :toDate)";

            if (!DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("")) {
                condition += filter.get("billType").getTag();
            }

            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and pr.consultantCode in (" + DmServer.getParamList(":consultant", filter.get("consultant").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("service").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and psd.serviceCode in (" + DmServer.getParamList(":service", filter.get("service").getTag()) + " )";
            }

            sqlQuery = "select  pr.consultantCode as groupCode, pr.regDocId as docId, sb.v_Date, pd.dischargeDate, "
                    + "         (case when isnull(con.initial, '') = '' then con.name else concat(con.initial, ' ', con.name) end) as groupName, "
                    + "         pr.recId as regNo, sb.recId, pu.patientName, "
                    + "         pr.dobDisplay, gen.shortName as gender, org.manualCode as organization, pu.recId as uhidNo, "
                    + "		(case when isnull(per.initial, '') = '' then per.name else concat(per.initial, ' ', per.name) end) as performing, "
                    + "          case when isnull(dis.initial, '') = '' then dis.name else concat(dis.initial, ' ', dis.name) end as discountBy, "
                    + "         psd.v_Date as serviceDate, psm.name as serviceName, psd.quantity, psd.grossAmount, psd.discount, psd.oh_Amt_Net, "
                    + "           psm.categoryCode, psd.serviceCode, "
                    + "         psd.consultantCode, pscm.name as categroyName, rm.roomCategoryCode, rcm.name as roomCategory, "
                    + "		 pr.v_Date as admitDate "
                    + "		from patientRegistration pr  "
                    + "		left join patientDischarge pd on pr.regDocId = pd.regDocId "
                    + "		left join voucher_Type vt on vt.v_Type = pr.v_Type 		"
                    + "		left join patientServiceDetail psd on psd.regDocId = pr.regDocId "
                    + "		left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "		left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "		left join roomMast rm on rm.code = psd.roomCode "
                    + "		left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "		left join ( "
                    + "			select  sb.regDocId, sb.v_Date, sb.recId, sb.v_No, vt.short_Name "
                    + "				from saleBill sb "
                    + "				left join voucher_Type vt on sb.v_Type = sb.v_Type "
                    + "				where vt.category = " + vrCatBill + " and isNull(sb.approvedBy,'') <> '' and sb.docId <> sb.regDocId "
                    + "		) as sb on sb.regDocId = pd.regDocId "
                    + "		left join patientUHID pu on pu.docId = pr.uhid "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pu.docId "
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		left join subGroup org on org.subCode = pr.organizationCode "
                    + "		left join subGroup con on con.subCode = pr.consultantCode "
                    + "		left join subGroup per on per.subCode = psd.consultantCode "
                    + "		left join subGroup dis on dis.subCode = psd.discountGivenBy "
                    + "		left join subGroup dep on dep.subCode = pr.departmentCode "
                    + "		where vt.category in ('" + pvc.IPD + "') and psd.isCancelled = 0 and pr.site_Code = :siteCode "
                    + "             and psd.billingNature <> 2 " + condition
                    + "		order by con.name, isnull(sb.v_Date, pd.dischargeDate), sb.short_Name, sb.v_No, sb.recId, psd.v_Date, psd.v_Sno";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultant", DmShared.xNull(filter.get("consultant").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("service").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("service", DmShared.xNull(filter.get("service").getTag()), true);
            }

            rsTemp = psTemp.executeQuery();
            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                LinkedHashMap hm = new LinkedHashMap();

                jasperReport = DmServer.compileJasper("MIS_IPDServicesReport");

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
                                    || col.getColumnName().equalsIgnoreCase("docId")
                                    || col.getColumnName().equalsIgnoreCase("uhidNo")
                                    || col.getColumnName().equalsIgnoreCase("categoryCode")
                                    || col.getColumnName().equalsIgnoreCase("serviceCode")
                                    || col.getColumnName().equalsIgnoreCase("roomCategoryCode")
                                    || col.getColumnName().equalsIgnoreCase("serviceCode")
                                    || col.getColumnName().equalsIgnoreCase("categroyName")
                                    || col.getColumnName().equalsIgnoreCase("admitDate")
                                    || col.getColumnName().equalsIgnoreCase("roomCategory")
                                    || col.getColumnName().equalsIgnoreCase("consultantCode")) {
                                i.remove();
                            }
                            switch (col.getDisplayName()) {
                                case "v_Date":
                                    col.setDisplayName("Bill Date");
                                    break;
                                case "recId":
                                    col.setDisplayName("Bill No.");
                                    break;
                                case "regNo":
                                    col.setDisplayName("IPD No.");
                                    break;
                                case "dobDisplay":
                                    col.setDisplayName("Age");
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
                                case "groupName":
                                    col.setDisplayName("Consultant");
                                    break;
                                case "dischargeDate":
                                    col.setDisplayName("Disch.Date");
                                    break;
                                case "serviceName":
                                    col.setDisplayName("Service");
                                    break;
                                case "quantity":
                                    col.setDisplayName("Unit");
                                    break;
                                case "grossAmount":
                                    col.setDisplayName("Gross Amt.");
                                    break;
                                case "oh_Amt_Net":
                                    col.setDisplayName("Net. Amt.");
                                    break;
                                case "serviceDate":
                                    col.setDisplayName("Service Date");
                                    break;
                                case "performing":
                                    col.setDisplayName("Performing Consultant");
                                    break;
                                case "roomCategory":
                                    col.setDisplayName("Room Category");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(" and sb.regDocId is not null");
            listNode.setSharedKeyFieldName("Billed");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(" and sb.regDocId is null");
            listNode.setSharedKeyFieldName("Not Billed");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select	cm.subCode as code, sg.name "
                    + "		from consultantMast as cm  "
                    + "		left join subGroupSiteList as sgsl on cm.subCode = sgsl.subCode "
                    + "		left join subGroup as sg on cm.subCode = sg.subCode  "
                    + "		left join subgroupNature as sgn on sg.nature = sgn.code  "
                    + "		where sgn.manualCode = 'CN' and sg.isActive = 1 and sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select  psm.code, psm.name"
                    + "		from patientServiceMast psm "
                    + "		left join patientServiceSiteList pssl on pssl.code = psm.code "
                    + "		where pssl.site_Code = :siteCode and psm.isActive = 1 "
                    + "             and psm.serviceNature in (" + ServiceNature.OPERATION.getId() + "," + ServiceNature.SERVICE.getId() + ")");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
