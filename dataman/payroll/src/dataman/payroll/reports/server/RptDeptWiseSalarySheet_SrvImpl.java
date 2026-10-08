package dataman.payroll.reports.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.xls.server.DmResultSetToExcelWriter;
import dataman.dmBase.self.xls.server.DmXlsColumn;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.payroll.reports.shared.Employee_Mdl;
import dataman.payroll.reports.shared.RptDeptWiseSalarySheet_Srv;
import dataman.payroll.reports.shared.RptDeptWiseSalarySheet_Vld;
import aarogya.selfToolkit.stStatic.server.STServer;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptDeptWiseSalarySheet_SrvImpl extends DmRSSPrivate implements RptDeptWiseSalarySheet_Srv {

    @Override
    public DmResult<KeyFieldExtended> execute(String session, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = " ", condition = " ", reportName = "", additionalFieldsEarning = "", additionalFieldsDeduction = "";
        int rowIndex = 0, totalColumnsEarnings = 0, totalColumnsDeductions = 0;
        String sessionId, fileName;
        DmXlsSheet xlsSheet = null;
        try {
            RptDeptWiseSalarySheet_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sessionId = session;
            fileName = widgetName.trim().replace(" ", "");

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId;
            String serverFileName = fileName + ".xls";
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

            if (!DmShared.xNull(filter.get("salaryMethod").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and hs.salaryMethodCode in (" + DmServer.getParamList(":salaryMethod", filter.get("salaryMethod").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and hs.hrDepartmentCode in (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("employees").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and hs.employeeCode in (" + DmServer.getParamList(":employees", filter.get("employees").getTag()) + " )";
            }

            sqlQuery = "select name, shortName, serialNo, nature from earningDeductionMastPayroll \n"
                    + "	where isSystemDefine = 0 and nature = 1 \n"
                    + "	order by nature desc, serialNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                additionalFieldsEarning += "hs." + DmShared.xNull(rsTemp.getString("shortName")) + " as [" + DmShared.xNull(rsTemp.getString("name")) + "],";
                totalColumnsEarnings += 1;
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select name, shortName, serialNo, nature from earningDeductionMastPayroll \n"
                    + "	where isSystemDefine = 0 and nature = 0 \n"
                    + "	order by nature desc, serialNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                additionalFieldsDeduction += "hs." + DmShared.xNull(rsTemp.getString("shortName")) + " as [" + DmShared.xNull(rsTemp.getString("name")) + "],";
                totalColumnsDeductions += 1;
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " select sgDep.name as [Department Name], sgEmp.name as [Employee Name], sgEmp.manualCode as [Employee ID], sdm.name as [Month], "
                    + " sgEmp.pan as [PAN], hs.BasicPay as [Basic], hs.CalcBasic as [Calc Basic],"
                    + additionalFieldsEarning + " hs.TotalEarn as [Total Earnings], hs.GrossSalary as [Gross Salary],"
                    + additionalFieldsDeduction + " hs.TotalDed as [Total Ded.], hs.NetSalary as [Net Salary]"
                    //                    + " hs.docId,hs.hrDepartmentCode,hs.Father,\n"
                    //                    + " hs.TotalDays,\n"
                    //                    + " hs.salaryMethodCode,hs.employeeCode"
                    + " from HrEmployeeSalary hs\n"
                    + " left join subGroup sgEmp on sgEmp.subCode = hs.employeeCode\n"
                    + " left join subGroup sgDep on sgDep.subCode = hs.hrDepartmentCode\n"
                    + " left join salaryCodeDefinitionMast sdm on sdm.code = hs.salaryMethodCode "
                    + " where 1=1" + condition + ""
                    + " order by hs.hrDepartmentCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
//            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));

            if (!DmShared.xNull(filter.get("salaryMethod").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("salaryMethod", DmShared.xNull(filter.get("salaryMethod").getTag()));
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("department", DmShared.xNull(filter.get("department").getTag()));
            }
            if (!DmShared.xNull(filter.get("employees").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("employees", DmShared.xNull(filter.get("employees").getTag()));
            }

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
                    STServer.setXLSFileFooter(workSheet, RptDeptWiseSalarySheet_SrvImpl.this);
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

            xlsSheet = STServer.setXLSFileHeader(fileName, xlsRsTemp, widgetName, PageOrientation.LANDSCAPE, PaperSize.LEGAL, filter, this);

            rowIndex = 4;
            WritableFont cellFontBold = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD);
            WritableCellFormat cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);

            WritableCell cellToAdd = null;
            xlsSheet.mergeCells(7, rowIndex, 7 + totalColumnsEarnings - 1, rowIndex);
            Label lblEarnings = new Label(7, rowIndex, "Earnings", cellFormat);
            cellToAdd = lblEarnings;
            xlsSheet.getWorkSheet().addCell(cellToAdd);

            xlsSheet.mergeCells(7 + totalColumnsEarnings, rowIndex, 7 + totalColumnsEarnings+2 + totalColumnsDeductions - 1, rowIndex);
            Label lblDeductions = new Label(21, rowIndex, "Deductions", cellFormat);
            cellToAdd = lblDeductions;
            xlsSheet.getWorkSheet().addCell(cellToAdd);

            xlsRsTemp.writeWorkBook(xlsSheet, 4);

            xlsRsTemp.commitChanges();

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSalaryMethod(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> data = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select code,name,fromDate,toDate from salaryCodeDefinitionMast where site_code =" + DmServer.getSsnSiteCode(this));

//            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                data.add(listNode);
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResultPageing<List<Employee_Mdl>> getTxtHEmployee(String departmentCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Employee_Mdl>> rstTemp = new DmResultPageing<>();
        List<Employee_Mdl> data = new ArrayList<>();
        Employee_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        rstTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(departmentCode).equalsIgnoreCase("")) {
                condition = " and sg2.hrDepartmentCode in(" + departmentCode + ")";
            }

            sqlWithParam.setSQL("select sg.subCode as code,sg.name as Employee,isNull(sg2.fathername,'') fatherName,format(sg2.doj,'dd-MMM-yyyy') doj,\n"
                    + "isNull(sgd.name,'') as department,isNull(sg2.hrDepartmentCode,'') DepartmentCode,sg.manualCode as id \n"
                    + "from subGroup  sg\n"
                    + "left join subGroup2 sg2 on sg2.subCode = sg.subCode\n"
                    + "left join subGroupNature sgn on sgn.code = sg.filterNature \n"
                    + "left join subGroup sgd on sgd.subCode = sg2.hrDepartmentCode\n"
                    + "where sgn.manualCode = 'EM' and sg.isActive = 1" + condition);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Employee_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Employee")));
                listNode.setEmployee(DmShared.xNull(rsTemp.getString("Employee")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("department")));
                listNode.setFatherName(DmShared.xNull(rsTemp.getString("fatherName")));
                listNode.setId(DmShared.xNull(rsTemp.getString("id")));
                listNode.setDOJ(DmShared.xNull(rsTemp.getString("doj")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("DepartmentCode")));
                data.add(listNode);
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResultPageing<List<KeyFieldExtended>> getDepartment(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select sg.subCode as code, sg.name as departmentName from subGroup sg\n"
                    + "left join subGroupNature sgn on sgn.code = sg.filterNature\n"
                    + "where sgn.manualCode = 'PY' and sg.isActive='1' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("departmentName")));
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
