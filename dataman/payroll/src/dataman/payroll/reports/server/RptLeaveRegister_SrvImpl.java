package dataman.payroll.reports.server;

import dataman.payroll.reports.shared.RptLeaveRegister_Srv;
import dataman.payroll.reports.shared.RptLeaveRegister_Vld;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.payroll.reports.shared.Employee_Mdl;
import dataman.payroll.reports.shared.LeaveTextHelp_Mdl;
import dataman.payroll.self.shared.PREnum.TYPE_LEAVE_ATTENDANCE;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptLeaveRegister_SrvImpl extends DmRSSPrivate implements RptLeaveRegister_Srv {

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
            RptLeaveRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  pyl.applyDate >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  pyl.applyDate <= :toDate ";
            }

            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sg2.hrDepartmentCode in (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("employees").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sg.subCode in (" + DmServer.getParamList(":employees", filter.get("employees").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("leaves").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and lm.code in (" + DmServer.getParamList(":leaves", filter.get("leaves").getTag()) + " )";
            }

            sqlQuery = " SELECT   pyl.docId,pyl.v_No,sg.name as EmployeeName,sgDep.name as Department  \n"
                    + ",pyl.reason, pyl.applyDate, pyl.fromDate, pyl.toDate\n"
                    + ", pyl.duration, lm.name as leaveName,sg2.hrDepartmentCode as deptCode \n"
                    + "FROM   pyLeaveEntry pyl\n"
                    + "left join subGroup sg on sg.subCode = pyl.employeeCode\n"
                    + "left join subGroup2 sg2 on sg2.subCode = sg.subCode\n"
                    + "left join subGroup sgDep on sg2.hrDepartmentCode = sgDep.subCode\n"
                    + "left join attendenceCodeDefinitionMast lm on lm.code = pyl.leaveCode where 1=1"+condition + "order by sg2.hrDepartmentCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
//            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));

            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("department", DmShared.xNull(filter.get("department").getTag()));
            }
            if (!DmShared.xNull(filter.get("employees").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("employees", DmShared.xNull(filter.get("employees").getTag()));
            }
            if (!DmShared.xNull(filter.get("leaves").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("leaves", DmShared.xNull(filter.get("leaves").getTag()));
            }

            rsTemp = psTemp.executeQuery();
            LinkedHashMap hm = new LinkedHashMap();

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("s")) {
                reportName = "LeaveRegisterSummary";
            } else {
                reportName = "LeaveRegisterDetail";
            }

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
    public DmResultPageing<List<KeyFieldExtended>> getReportType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("s");
            listNode.setSharedKeyFieldName("Summary");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("d");
            listNode.setSharedKeyFieldName("Detail");
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
    public DmResultPageing<List<LeaveTextHelp_Mdl>> getTxtHLeaves(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<LeaveTextHelp_Mdl>> rstTemp = new DmResultPageing<>();
        List<LeaveTextHelp_Mdl> data = new ArrayList<>();
        LeaveTextHelp_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select lm.code,lm.name,lm.manualCode,lm.days,lm.plusMinus from attendenceCodeDefinitionMast lm where type_Leave_Attendance=:type_Leave_Attendance");
            sqlWithParam.setString("type_Leave_Attendance", DmServer.vNull(TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId()));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new LeaveTextHelp_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setLeave(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
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
