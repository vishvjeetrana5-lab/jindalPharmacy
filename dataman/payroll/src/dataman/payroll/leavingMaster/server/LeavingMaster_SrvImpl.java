package dataman.payroll.leavingMaster.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.payroll.leavingMaster.shared.Employee_Mdl;
import dataman.payroll.leavingMaster.shared.Find_Mdl;
import dataman.payroll.leavingMaster.shared.LeavingMaster_Mdl;
import dataman.payroll.leavingMaster.shared.LeavingMaster_Srv;
import dataman.payroll.leavingMaster.shared.LeavingMaster_Vld;
import dataman.payroll.self.shared.PREnum;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LeavingMaster_SrvImpl extends DmRSSPrivate implements LeavingMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("select docId as code from pyLeavingEntry");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<Employee_Mdl>> getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Employee_Mdl>> rstTemp = new DmResultPageing<>();
        List<Employee_Mdl> data = new ArrayList<>();
        Employee_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select sg.subCode as code,sg.name as Employee,sg.manualCode as EmployeeId,isNull(sg2.fathername,'') fatherName,sg2.doj doj,\n"
                    + "isNull(sgd.name,'') as department,isNull(sg2.hrDepartmentCode,'') DepartmentCode,sg2.designationCode,dm.name as Designation\n"
                    + "from subGroup  sg\n"
                    + "left join subGroup2 sg2 on sg2.subCode = sg.subCode\n"
                    + "left join subGroupNature sgn on sgn.code = sg.filterNature \n"
                    + "left join subGroup sgd on sgd.subCode = sg2.hrDepartmentCode\n"
                    + "left join designationMast dm on dm.code = sg2.designationCode\n"
                    + "where sgn.manualCode = 'EM' and sg.isActive = 1");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Employee_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Employee")));
                listNode.setEmployee(DmShared.xNull(rsTemp.getString("Employee")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("department")));
                listNode.setFatherName(DmShared.xNull(rsTemp.getString("fatherName")));
                listNode.setId(DmShared.xNull(rsTemp.getString("EmployeeId")));
                listNode.setDOJ(DmShared.dNull(rsTemp.getDate("doj")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("DepartmentCode")));
                listNode.setDesignation(DmShared.xNull(rsTemp.getString("Designation")));
                listNode.setDesignationCode(DmShared.xNull(rsTemp.getString("designationCode")));
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
    public String[] getServerDateTime() {
        String[] dateTime, returnValue;

        dateTime = DmServer.getServerDateTime();
        returnValue = new String[3];
        returnValue[0] = dateTime[0];
        returnValue[1] = dateTime[1];
        returnValue[2] = UUID.randomUUID().toString();

        return returnValue;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select code, name from pyReasonMast");

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHResignationMode(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> data = new ArrayList<>();
        KeyFieldExtended listNode;
        rstTemp.setData(data);

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(PREnum.RESIGNATION_MODE.PROPER.getId()).toString());
            listNode.setSharedKeyFieldName(DmShared.xNull(PREnum.RESIGNATION_MODE.PROPER.getValue()));
            data.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(PREnum.RESIGNATION_MODE.NOT_PROPER.getId()).toString());
            listNode.setSharedKeyFieldName(DmShared.xNull(PREnum.RESIGNATION_MODE.NOT_PROPER.getValue()));
            data.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(PREnum.RESIGNATION_MODE.TERMINATE.getId()).toString());
            listNode.setSharedKeyFieldName(DmShared.xNull(PREnum.RESIGNATION_MODE.TERMINATE.getValue()));
            data.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(PREnum.RESIGNATION_MODE.RETIREMENT.getId()).toString());
            listNode.setSharedKeyFieldName(DmShared.xNull(PREnum.RESIGNATION_MODE.RETIREMENT.getValue()));
            data.add(listNode);

            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSalaryStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> data = new ArrayList<>();
        KeyFieldExtended listNode;
        rstTemp.setData(data);

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(PREnum.SALARY_STATUS.RELEASED.getId()).toString());
            listNode.setSharedKeyFieldName(DmShared.xNull(PREnum.SALARY_STATUS.RELEASED.getValue()));
            data.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(PREnum.SALARY_STATUS.CREDIT.getId()).toString());
            listNode.setSharedKeyFieldName(DmShared.xNull(PREnum.SALARY_STATUS.CREDIT.getValue()));
            data.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(PREnum.SALARY_STATUS.HOLD.getId()).toString());
            listNode.setSharedKeyFieldName(DmShared.xNull(PREnum.SALARY_STATUS.HOLD.getValue()));
            data.add(listNode);

            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<LeavingMaster_Mdl> getMoveRec(String strKey) {
        DmResult<LeavingMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        LeavingMaster_Mdl mrTemp = new LeavingMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT   pyle.docId,pyle.v_Date, pyle.employeeCode,sg.name as EmployeeName ,sg2.hrDepartmentCode,sgdep.name as Department,\n"
                    + "sg2.designationCode,sg2.doj,dm.name as Designation,pyle.preparedBy,pyle.approvedBy,\n"
                    + "pyle.lastDuty, pyle.resignationMode, \n"
                    + "pyle.reasonCode,pyr.name as Reason, pyle.lastSalaryStatus,pyle.isStatus\n"
                    + "FROM   pyLeavingEntry pyle\n"
                    + "left join subGroup sg on pyle.employeeCode = sg.subCode\n"
                    + "left join pyReasonMast pyr on pyr.code = pyle.reasonCode \n"
                    + "left join subGroup2 sg2 on sg2.subCode = sg.subCode\n"
                    + "left join subGroup sgdep on sgdep.subCode = sg2.hrDepartmentCode\n"
                    + "left join designationMast dm on dm.code = sg2.designationCode where docId = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                mrTemp.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                mrTemp.setEmployeeName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("hrDepartmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("Department")));
                mrTemp.setDesignationCode(DmShared.xNull(rsTemp.getString("designationCode")));
                mrTemp.setDesignationName(DmShared.xNull(rsTemp.getString("Designation")));
                mrTemp.setJoiningDate(DmShared.dNull(rsTemp.getDate("doj")));
                mrTemp.setLastDuty(DmShared.dNull(rsTemp.getDate("lastDuty")));
                mrTemp.setResignationMode(DmShared.xNull(rsTemp.getString("resignationMode")));
                mrTemp.setReasonCode(DmShared.xNull(rsTemp.getString("reasonCode")));
                mrTemp.setReason(DmShared.xNull(rsTemp.getString("Reason")));
                mrTemp.setSalaryStatus(DmShared.xNull(rsTemp.getString("lastSalaryStatus")));
                mrTemp.setStatusCode(DmShared.xNull(rsTemp.getString("isStatus")));
                mrTemp.setDtVoucher(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("preparedBy")));

            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From pyLeavingEntry Where docId= :code ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("code", strKey);
            psTemp1.executeUpdate();
            psTemp1.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);

        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

    @Override
    public DmResult<String> saveRecord(LeavingMaster_Mdl mrTemp, String voucherPrefix) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, filterNature = "", acGroupConsCode = "", acLocationCode = "", acDepartmentCode = "";
        ResultSet rsTemp = null;
        int vSno = 0;
        String v_Type = null;
        String condition = "";
        String[] currentDateTime;
        String totalDed = "";
        String userName;
        try {

            LeavingMaster_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            userName = DmServer.getSsnUserName(this);
            v_Type = DmServer.getVType("RSMST", cnTemp);
            currentDateTime = getServerDateTime();
            if (DmShared.xNull(mrTemp.getDtVoucher()).trim().equalsIgnoreCase("")) {
                mrTemp.setDtVoucher(currentDateTime[0]);
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(v_Type), voucherPrefix, cnTemp, this));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            // if blnAdd is true, this means new record should be inserted into database.
            if (blnAdd) {
                sqlQuery = "insert into pyLeavingEntry (docId,v_Type,v_Date,v_Prefix,\n"
                        + "employeeCode,lastDuty,resignationMode,reasonCode,lastSalaryStatus,\n"
                        + "u_Name,u_EntDt,preparedBy,approvedBy,isStatus,site_Code)values(:docId,:v_Type,:v_Date,:v_Prefix,:employeeCode,:lastDuty,:resignationMode,:reasonCode,:lastSalaryStatus,\n"
                        + ":u_Name,:u_EntDt,:preparedBy,:u_Name,:isStatus,:site_Code)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(v_Type));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDtVoucher()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("employeeCode", DmShared.xNull(mrTemp.getEmployeeCode()));
                psTemp.setString("lastDuty", DmShared.xNull(mrTemp.getLastDuty()));
                psTemp.setString("resignationMode", DmShared.xNull(mrTemp.getResignationMode()));
                psTemp.setString("reasonCode", DmShared.xNull(mrTemp.getReasonCode()));
                psTemp.setString("lastSalaryStatus", DmShared.xNull(mrTemp.getSalaryStatus()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("preparedBy", userName);
                psTemp.setString("isStatus", "0");
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.executeUpdate();
                psTemp.close();

            }
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(
                    "  SELECT   pyle.docId,sg.name as EmployeeName ,sg.manualCode as EmpId,"
                    + "sgdep.name as Department,\n"
                    + "sg2.doj,dm.name as Designation\n"
                    + "FROM   pyLeavingEntry pyle\n"
                    + "left join subGroup sg on pyle.employeeCode = sg.subCode\n"
                    + "left join pyReasonMast pyr on pyr.code = pyle.reasonCode \n"
                    + "left join subGroup2 sg2 on sg2.subCode = sg.subCode\n"
                    + "left join subGroup sgdep on sgdep.subCode = sg2.hrDepartmentCode\n"
                    + "left join designationMast dm on dm.code = sg2.designationCode");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                listNode.setEmployeeName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                listNode.setEmpId(DmShared.xNull(rsTemp.getString("EmpId")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("Department")));
                listNode.setDesignationName(DmShared.xNull(rsTemp.getString("Designation")));
                listNode.setDateOfJoining(DmShared.dNull(rsTemp.getDate("doj")));
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
