package dataman.payroll.employeeShiftDetail.server;

import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.payroll.employeeShiftDetail.shared.EmployeeShiftDetail_Mdl;
import dataman.payroll.employeeShiftDetail.shared.EmployeeShiftDetail_Srv;
import dataman.payroll.employeeShiftDetail.shared.Employee_Mdl;
import dataman.payroll.employeeShiftDetail.shared.Find_Mdl;
import dataman.payroll.employeeShiftDetail.shared.TxtHShift_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeShiftDetail_SrvImpl extends DmRSSPrivate implements EmployeeShiftDetail_Srv {

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();

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
            rsTemp = cnTemp.createStatement().executeQuery("select docid from pyEmployeeShiftDetail ");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
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

    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "delete from pyEmployeeShiftDetail where docid = :docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResult<EmployeeShiftDetail_Mdl> getMoveRec(String strKey) {
        DmResult<EmployeeShiftDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        EmployeeShiftDetail_Mdl mrTemp = new EmployeeShiftDetail_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select esd.docid,esd.employeeCode,sg.name as employeeName,\n"
                    + "sm.shiftName,esd.shiftCode,esd.fromDate,esd.toDate,esd.preparedBy,esd.modifiedBy \n"
                    + "from pyEmployeeShiftDetail esd\n"
                    + "left join subGroup sg on sg.subCode = esd.employeeCode\n"
                    + "left join shiftMast sm on sm.code = esd.shiftCode\n"
                    + "where docid = :docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("employeeName")));
                mrTemp.setEmployeeName(DmShared.xNull(rsTemp.getString("employeeName")));
                mrTemp.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                mrTemp.setShiftName(DmShared.xNull(rsTemp.getString("shiftName")));
                mrTemp.setShiftCode(DmShared.xNull(rsTemp.getString("shiftCode")));
                mrTemp.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                mrTemp.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("modifiedBy")));
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
    public DmResultPageing<List<Employee_Mdl>> getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<TxtHShift_Mdl>> getTxtHShift(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHShift_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHShift_Mdl> data = new ArrayList<>();
        TxtHShift_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select sm.code,sm.shiftName,sm.fromTime,sm.toTime\n"
                    + "from shiftMast sm");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHShift_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("shiftName")));
                listNode.setFromTime(DmShared.xNull(rsTemp.getString("fromTime")));
                listNode.setToTime(DmShared.xNull(rsTemp.getString("toTime")));
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
    public DmResult<String> saveRecord(EmployeeShiftDetail_Mdl mrTemp, String voucherPrefix) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode, vType;
        ResultSet rsTemp = null;
        String[] currentDateTime;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            vType = DmServer.getCodeViaManualCode("EMSHD", "short_Name", "voucher_Type", "v_Type", cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                currentDateTime = getServerDateTime();
                if (DmShared.dNull(mrTemp.getDtVoucher()).trim().equalsIgnoreCase("")) {
                    mrTemp.setDtVoucher(currentDateTime[0]);
                }
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(vType), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdEmpShiftDetail(DmServer.getRecId("pyEmployeeShiftDetail", "docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdEmpShiftDetail(), mrTemp.getDtVoucher(), vType, voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(mrTemp.getRecIdEmpShiftDetail().getRecIdValue());
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /**
             * ****************Checking for inappropriate entry
             */
            sqlQuery = "select esd.fromDate,esd.toDate \n"
                    + "from pyEmployeeShiftDetail esd\n"
                    + "where esd.employeeCode = :empCode"
                    + " and esd.docId <> :docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("empCode", DmShared.iNull(mrTemp.getEmployeeCode()).toString());
            psTemp.setString("docid", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if ((!DmShared.dNull(rsTemp.getString("fromDate")).equalsIgnoreCase("")) && (!DmShared.dNull(rsTemp.getString("toDate")).equalsIgnoreCase(""))) {
                    if ((DmShared.convertDate(mrTemp.getFromDate()).getTime() >= DmShared.convertDate(DmShared.dNull(rsTemp.getDate("fromDate"))).getTime()
                            && DmShared.convertDate(mrTemp.getFromDate()).getTime() <= DmShared.convertDate(DmShared.dNull(rsTemp.getDate("toDate"))).getTime())
                            || (DmShared.convertDate(mrTemp.getToDate()).getTime() >= DmShared.convertDate(DmShared.dNull(rsTemp.getDate("fromDate"))).getTime()
                            && DmShared.convertDate(mrTemp.getToDate()).getTime() <= DmShared.convertDate(DmShared.dNull(rsTemp.getDate("toDate"))).getTime())
                            || (DmShared.convertDate(mrTemp.getFromDate()).getTime() <= DmShared.convertDate(DmShared.dNull(rsTemp.getDate("fromDate"))).getTime()
                            && DmShared.convertDate(mrTemp.getToDate()).getTime() >= DmShared.convertDate(DmShared.dNull(rsTemp.getDate("toDate"))).getTime())) {
                        throw new DmException(300, "Inappropriate Shift Date.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
            if (blnAdd) {
                sqlQuery = "insert into pyEmployeeShiftDetail(docid, v_Type, v_No, recIdPrefix, recId, v_Prefix, v_Date,\n"
                        + "employeeCode, shiftCode, fromDate, toDate, u_EntDt, preparedBy,site_Code) \n"
                        + "values(:docid, :v_Type, :v_No, :recIdPrefix, :recId, :v_Prefix, :v_Date,\n"
                        + ":employeeCode, :shiftCode, :fromDate, :toDate, :u_EntDt, :u_Name, :site_Code\n"
                        + ")";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docid", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Type", DmShared.xNull(vType));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdEmpShiftDetail().getCounter()).toString());
                psTemp.setString("recIdPrefix", mrTemp.getRecIdEmpShiftDetail().getPrefix());
                psTemp.setString("recId", mrTemp.getRecIdEmpShiftDetail().getRecIdValue());
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDtVoucher()));
                psTemp.setString("employeeCode", DmShared.iNull(mrTemp.getEmployeeCode()).toString());
                psTemp.setString("shiftCode", DmShared.iNull(mrTemp.getShiftCode()).toString());
                psTemp.setString("fromDate", DmShared.xNull(mrTemp.getFromDate()));
                psTemp.setString("toDate", DmShared.xNull(mrTemp.getToDate()));
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = "update pyEmployeeShiftDetail set"
                        + " shiftCode = :shiftCode,"
                        + " employeeCode = :employeeCode,"
                        + " fromDate = :fromDate,"
                        + " toDate = :toDate,"
                        + " modifiedBy = :u_Name,"
                        + " u_EntDt = :u_EntDt"
                        + " where docid = :docid";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docid", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("shiftCode", DmShared.iNull(mrTemp.getShiftCode()).toString());
                psTemp.setString("employeeCode", DmShared.iNull(mrTemp.getEmployeeCode()).toString());
                psTemp.setString("fromDate", DmShared.xNull(mrTemp.getFromDate()));
                psTemp.setString("toDate", DmShared.xNull(mrTemp.getToDate()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

            sqlQuery = "update subGroup set"
                    + " shiftCode = :shiftCode "
                    + " where subCode = :subCode ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shiftCode", DmShared.iNull(mrTemp.getShiftCode()).toString());
            psTemp.setString("subCode", DmShared.iNull(mrTemp.getEmployeeCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

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
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select esd.docid,sg.name as employeeName,sm.shiftName,\n"
                    + "esd.fromDate,esd.toDate \n"
                    + "from pyEmployeeShiftDetail esd\n"
                    + "left join subGroup sg on sg.subCode = esd.employeeCode\n"
                    + "left join shiftMast sm on sm.code = esd.shiftCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("employeeName")));
                listNode.setShiftName(DmShared.xNull(rsTemp.getString("shiftName")));
                listNode.setEmployeeName(DmShared.xNull(rsTemp.getString("employeeName")));
                listNode.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                listNode.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
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
