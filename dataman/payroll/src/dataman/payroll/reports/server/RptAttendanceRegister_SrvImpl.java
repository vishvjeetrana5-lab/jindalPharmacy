package dataman.payroll.reports.server;

import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.payroll.reports.shared.Employee_Mdl;
import dataman.payroll.reports.shared.RptAttendanceRegister_Vld;
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

public class RptAttendanceRegister_SrvImpl extends DmRSSPrivate implements RptSalarySheet_Srv {

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
            RptAttendanceRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));


            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and a.fromDate >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and a.fromDate <= :toDate ";
            }

            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and a.hrDepartmentCode in (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("employees").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and a.employeeCode in (" + DmServer.getParamList(":employees", filter.get("employees").getTag()) + " )";
            }

            sqlQuery = " select a.employeeCode,max(a.departmentName) as departmentName,max(sg.name) as employeeName,max(a.fatherName) as fatherName,max(a.fromDate) fromDate\n"
                    + ",max(date1) as date1\n"
                    + ",max(date2) as date2\n"
                    + ",max(date3) as date3\n"
                    + ",max(date4) as date4\n"
                    + ",max(date5) as date5\n"
                    + ",max(date6) as date6\n"
                    + ",max(date7) as date7\n"
                    + ",max(date8) as date8\n"
                    + ",max(date9) as date9\n"
                    + ",max(date10) as date10\n"
                    + ",max(date11) as date11\n"
                    + ",max(date12) as date12\n"
                    + ",max(date13) as date13\n"
                    + ",max(date14) as date14\n"
                    + ",max(date15) as date15\n"
                    + ",max(date16) as date16\n"
                    + ",max(date17) as date17\n"
                    + ",max(date18) as date18\n"
                    + ",max(date19) as date19\n"
                    + ",max(date20) as date20\n"
                    + ",max(date21) as date21\n"
                    + ",max(date22) as date22\n"
                    + ",max(date23) as date23\n"
                    + ",max(date24) as date24\n"
                    + ",max(date25) as date25\n"
                    + ",max(date26) as date26\n"
                    + ",max(date27) as date27\n"
                    + ",max(date28) as date28\n"
                    + ",max(date29) as date29\n"
                    + ",max(date30) as date30\n"
                    + ",max(date31) as date31\n"
                    + ",sum(offDay) as offday\n"
                    + ",sum(present) as present\n"
                    + ",sum(absent) as absent\n"
                    + ",sum(leave) as leave\n"
                    + ",sum(halfDay) as halfDay"
                    + "	from (\n"
                    + "		select eald.employeeCode,sg2.hrDepartmentCode,eald.fromDate,sg2.fathername,sgDep.name as departmentName\n"
                    + "		,case when datename(d,eald.fromDate)=1 then acdm.manualCode else '' end  as date1\n"
                    + "		,case when datename(d,eald.fromDate)=2 then acdm.manualCode else '' end  as date2\n"
                    + "		,case when datename(d,eald.fromDate)=3 then acdm.manualCode else '' end  as date3\n"
                    + "		,case when datename(d,eald.fromDate)=4 then acdm.manualCode else '' end  as date4\n"
                    + "		,case when datename(d,eald.fromDate)=5 then acdm.manualCode else '' end  as date5\n"
                    + "		,case when datename(d,eald.fromDate)=6 then acdm.manualCode else '' end  as date6\n"
                    + "		,case when datename(d,eald.fromDate)=7 then acdm.manualCode else '' end  as date7\n"
                    + "		,case when datename(d,eald.fromDate)=8 then acdm.manualCode else '' end  as date8\n"
                    + "		,case when datename(d,eald.fromDate)=9 then acdm.manualCode else '' end  as date9\n"
                    + "		,case when datename(d,eald.fromDate)=10 then acdm.manualCode else '' end  as date10\n"
                    + "		,case when datename(d,eald.fromDate)=11 then acdm.manualCode else '' end  as date11\n"
                    + "		,case when datename(d,eald.fromDate)=12 then acdm.manualCode else '' end  as date12\n"
                    + "		,case when datename(d,eald.fromDate)=13 then acdm.manualCode else '' end  as date13\n"
                    + "		,case when datename(d,eald.fromDate)=14 then acdm.manualCode else '' end  as date14\n"
                    + "		,case when datename(d,eald.fromDate)=15 then acdm.manualCode else '' end  as date15\n"
                    + "		,case when datename(d,eald.fromDate)=16 then acdm.manualCode else '' end  as date16\n"
                    + "		,case when datename(d,eald.fromDate)=17 then acdm.manualCode else '' end  as date17\n"
                    + "		,case when datename(d,eald.fromDate)=18 then acdm.manualCode else '' end  as date18\n"
                    + "		,case when datename(d,eald.fromDate)=19 then acdm.manualCode else '' end  as date19\n"
                    + "		,case when datename(d,eald.fromDate)=20 then acdm.manualCode else '' end  as date20\n"
                    + "		,case when datename(d,eald.fromDate)=21 then acdm.manualCode else '' end  as date21\n"
                    + "		,case when datename(d,eald.fromDate)=22 then acdm.manualCode else '' end  as date22\n"
                    + "		,case when datename(d,eald.fromDate)=23 then acdm.manualCode else '' end  as date23\n"
                    + "		,case when datename(d,eald.fromDate)=24 then acdm.manualCode else '' end  as date24\n"
                    + "		,case when datename(d,eald.fromDate)=25 then acdm.manualCode else '' end  as date25\n"
                    + "		,case when datename(d,eald.fromDate)=26 then acdm.manualCode else '' end  as date26\n"
                    + "		,case when datename(d,eald.fromDate)=27 then acdm.manualCode else '' end  as date27\n"
                    + "		,case when datename(d,eald.fromDate)=28 then acdm.manualCode else '' end  as date28\n"
                    + "		,case when datename(d,eald.fromDate)=29 then acdm.manualCode else '' end  as date29\n"
                    + "		,case when datename(d,eald.fromDate)=30 then acdm.manualCode else '' end  as date30\n"
                    + "		,case when datename(d,eald.fromDate)=31 then acdm.manualCode else '' end  as date31\n"
                    + "		,case when acdm.manualCode='OFD' then 1 else 0 end as offDay\n"
                    + "		,case when acdm.manualCode='PP' then 1 else 0 end as Present\n"
                    + "		,case when acdm.manualCode='AA' then 1 else 0 end as absent\n"
                    + "         ,case when acdm.manualCode='HD' then 1 else 0 end as halfDay\n"
                    + "		,case when eald.att_Lev_OT=2 then 1 else 0 end as leave\n"
                    + "			from Employee_Attendance_Leave_Detail eald\n"
                    + "			left join attendenceCodeDefinitionMast acdm on eald.attendanceCode=acdm.code\n"
                    + "			left join subGroup sgEmp on sgEmp.subCode = eald.employeeCode\n"
                    + "			left join subGroup2 sg2 on sg2.subCode = sgEmp.subCode\n"
                    + "			left join subGroup sgDep on sgDep.subCode = sg2.hrDepartmentCode\n"
                    + "			where eald.att_Lev_OT=1\n"
                    + "		) as a\n"
                    + "		left join subGroup sg on a.employeeCode=sg.subCode\n"
                    + " where 1=1"+condition
                    + "group by a.employeeCode,a.hrDepartmentCode\n"
                    + "order by a.hrDepartmentCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
//            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
//
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("department", DmShared.xNull(filter.get("department").getTag()));
            }
            if (!DmShared.xNull(filter.get("employees").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("employees", DmShared.xNull(filter.get("employees").getTag()));
            }

            rsTemp = psTemp.executeQuery();
            LinkedHashMap hm = new LinkedHashMap();
            reportName = "AttendanceRegister";

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
