package dataman.payroll.reports.server;

import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.payroll.reports.shared.Employee_Mdl;
import dataman.payroll.reports.shared.RptSalarySheet_Srv;
import dataman.payroll.reports.shared.RptSalarySheet_Vld;
import dataman.payroll.self.shared.PREnum.TYPE_LEAVE_ATTENDANCE;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptSalarySheet_SrvImpl extends DmRSSPrivate implements RptSalarySheet_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = " ", reportName = "";

        try {
            RptSalarySheet_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("salaryMethod").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and hs.salaryMethodCode in (" + DmServer.getParamList(":salaryMethod", filter.get("salaryMethod").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and hs.hrDepartmentCode in (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("employees").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and hs.employeeCode in (" + DmServer.getParamList(":employees", filter.get("employees").getTag()) + " )";
            }

            sqlQuery = " select hs.docId,hs.hrDepartmentCode,sgEmp.name as employeeName,sgDep.name as departmentName,hs.Father,hs.BasicPay,hs.CalcBasic,\n"
                    + "hs.TotalDays,hs.HRA,hs.GrossSalary,hs.NetSalary,hs.TotalEarn,hs.TotalDed,hs.PF,hs.ESI,hs.GS,hs.NS,hs.SPA,hs.DA,\n"
                    + "hs.salaryMethodCode,hs.employeeCode,sgEmp.manualCode\n"
                    + "from HrEmployeeSalary hs\n"
                    + "left join subGroup sgEmp on sgEmp.subCode = hs.employeeCode\n"
                    + "left join subGroup sgDep on sgDep.subCode = hs.hrDepartmentCode\n"
                    + "left join salaryCodeDefinitionMast sdm on sdm.code = hs.salaryMethodCode where 1=1" + condition + "order by hs.hrDepartmentCode";

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
            LinkedHashMap hm = new LinkedHashMap();
            reportName = "SalarySheet";

            jasperReport = DmServer.compileJasper(reportName);
            jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, hm, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
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
                condition = " and sg2.hrDepartmentCode in(" +departmentCode+ ")";
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
