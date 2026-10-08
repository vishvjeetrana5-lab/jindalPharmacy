package dataman.payroll.salaryCreation.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.payroll.salaryCreation.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryCreation_SrvImpl extends DmRSSPrivate implements SalaryCreation_Srv {

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private final String vCatSalaryCreation =  lvc.SALARY_CREATION;
    
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
            rsTemp = cnTemp.createStatement().executeQuery("select max(hr.docId) docId from HrEmployeeSalary hr group by hr.docId");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
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

    private String getDate(String monthSelected) {

        String[] monthSelectedList;
        String monthName, monthYear, dateReturn;
        int monthNumber = 0;
        monthSelected = monthSelected.replace("'", "");
        monthSelectedList = monthSelected.split("-");
        monthName = monthSelectedList[1];
        dateReturn = "01-" + monthSelected.substring(3, 11);
        return dateReturn;
    }

    @Override
    public List<GrdSalaryCreation_Mdl> getFillRecords(SalaryCreation_Mdl param) {
        List<GrdSalaryCreation_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getSalary(param, cnTemp);
            cnTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
        return lstTemp;
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
        String salaryMethodCode = "";
        String[] salaryMethodCodeList;
        DmHelper helper2 = new DmHelper();

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
                listNode.setSharedModifiedBy(DmShared.dNull(rsTemp.getDate("fromDate")));
                listNode.setSharedPreparedBy(DmShared.dNull(rsTemp.getDate("toDate")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> data = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select subCode as code,name from subGroup where nature = " + DmServer.getCodeViaManualCode("PY", "manualCode", "subGroupNature", "code", cnTemp));

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

    private List<GrdSalaryCreation_Mdl> getSalaryCreationList(String docId, SalaryCreation_Mdl mrTemp, Connection cnTemp) {
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdSalaryCreation_Mdl> grdSalaryCreationList = new ArrayList<GrdSalaryCreation_Mdl>();
        GrdSalaryCreation_Mdl grdSalaryCreation_Mdl;

        try {
            if (!DmShared.xNull(docId).trim().equalsIgnoreCase("")) {
                condition += " where hr.docId = :docId";
            }

            sqlQuery = "select hr.docId code,sge.name as Employee, hr.employeeCode,hr.Father,hr.BasicPay,hr.CalcBasic\n"
                    + ",hr.GrossSalary,hr.NetSalary,hr.TotalDays,hr.OffDays,\n"
                    + "hr.Holidays,hr.WeekDays,hr.TotalDed,hr.TotalEarn,scd.name as SalaryMethodName,sg.name as DepartmentName,\n"
                    + "hr.leave "
                    + "from HrEmployeeSalary hr\n"
                    + "left join subgroup sge on sge.subCode = hr.employeeCode\n"
                    + "left join salaryCodeDefinitionMast scd on scd.code = hr.salaryMethodCode\n"
                    + "left join subGroup sg on sg.subCode = hr.hrDepartmentCode" + condition;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(docId).trim().equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.iNull(docId).toString());
            }

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                grdSalaryCreation_Mdl = new GrdSalaryCreation_Mdl();
                mrTemp.setSalaryMethodName(DmShared.xNull(rsTemp.getString("SalaryMethodName")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("DepartmentName")));
                grdSalaryCreation_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                grdSalaryCreation_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Employee")));
                grdSalaryCreation_Mdl.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));

                grdSalaryCreation_Mdl.setEmployee(DmShared.xNull(rsTemp.getString("Employee")));
                grdSalaryCreation_Mdl.setFather(DmShared.xNull(rsTemp.getString("Father")));
                grdSalaryCreation_Mdl.setBasic(DmShared.xNull(rsTemp.getString("BasicPay")));
                grdSalaryCreation_Mdl.setCalcBasic(DmShared.xNull(rsTemp.getString("CalcBasic")));
                grdSalaryCreation_Mdl.setGrossSalary(DmShared.xNull(rsTemp.getString("GrossSalary")));
                grdSalaryCreation_Mdl.setNetSalary(DmShared.xNull(rsTemp.getString("NetSalary")));
                grdSalaryCreation_Mdl.setTdays(DmShared.xNull(rsTemp.getString("TotalDays")));
                grdSalaryCreation_Mdl.setOffDays(DmShared.xNull(rsTemp.getString("OffDays")));
                grdSalaryCreation_Mdl.setHolidays(DmShared.xNull(rsTemp.getString("Holidays")));
                grdSalaryCreation_Mdl.setTotalEarn(DmShared.xNull(rsTemp.getString("TotalEarn")));
                grdSalaryCreation_Mdl.setTotalDed(DmShared.xNull(rsTemp.getString("TotalDed")));
                grdSalaryCreation_Mdl.setWdays(DmShared.xNull(rsTemp.getString("WeekDays")));
                grdSalaryCreation_Mdl.setLeave(DmShared.xNull(rsTemp.getString("leave")));
                grdSalaryCreationList.add(grdSalaryCreation_Mdl);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return grdSalaryCreationList;
    }

    @Override
    public DmResult<SalaryCreation_Mdl> getMoveRec(String strKey) {
        DmResult<SalaryCreation_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        SalaryCreation_Mdl mrTemp = new SalaryCreation_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp.getGrdSalaryCreationList().clear();
            if (!DmShared.xNull(strKey).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(strKey).toString());
                mrTemp.getGrdSalaryCreationList().addAll(getSalaryCreationList(strKey, mrTemp, cnTemp));
            }

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

            sqlQuery = "delete from HrEmployeeSalary where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
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
    public DmResult<String> saveRecord(SalaryCreation_Mdl mrTemp, String voucherPrefix) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        int vSno = 0;
        String v_Type = null;
        String condition = "";

        try {

            SalaryCreation_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getV_Type()).equalsIgnoreCase("")) {
                v_Type = DmServer.getVType(vCatSalaryCreation, cnTemp);// returns integer v_type as String
            } else {
                v_Type = mrTemp.getV_Type();
            }

            mrTemp.setV_Type(v_Type);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(v_Type), voucherPrefix, cnTemp, this));

            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (!DmShared.xNull(mrTemp.getDepartmentCode()).equalsIgnoreCase("")) {
                condition += " and hr.hrDepartmentCode = :departmentCode";
            } else {
                condition += " and isNull(hr.hrDepartmentCode,'') = ''";
            }

//    check Duplicate values for Salary Method and Department Name
            sqlQuery = "select hr.salaryMethodCode,hr.hrDepartmentCode from HrEmployeeSalary hr\n"
                    + " where hr.salaryMethodCode = :salaryMethodCode"
                    + " and docId <> :docId" + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("salaryMethodCode", DmShared.xNull(mrTemp.getSalaryMethodCode()));

            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            if (!DmShared.xNull(mrTemp.getDepartmentCode()).equalsIgnoreCase("")) {
                psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(500, "Record already exists.");
            }

            rsTemp.close();
            psTemp.close();

            sqlQuery = "select count(vSno) as cnt from HrEmployeeSalary";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                vSno = (int) rsTemp.getLong("cnt");
            }
            rsTemp.close();
            psTemp.close();
            // if blnAdd is true, this means new record should be inserted into database.
            if (blnAdd) {
                if (mrTemp.getGrdSalaryCreationList() != null) {
                    for (GrdSalaryCreation_Mdl salaryMdl : mrTemp.getGrdSalaryCreationList()) {
                        vSno = vSno + 1;

                        sqlQuery = "insert into HrEmployeeSalary (docId,vSno,EmployeeCode,Father,BasicPay,CalcBasic,GrossSalary,\n"
                                + "NetSalary,TotalDays,OffDays,Holidays,WeekDays,salaryMethodCode,hrDepartmentCode,TotalEarn,\n"
                                + "TotalDed,leave,UEntDt,Uname,cl,el,pp,aa,hd,lwp,loanDocId,loanEmiDate," + salaryMdl.getEarningDeductionFields() + ") \n"
                                + "values(:docId,:vSno,:EmployeeCode,:Father,:BasicPay,:CalcBasic,:GrossSalary,\n"
                                + ":NetSalary,:TotalDays,:OffDays,:Holidays,:WeekDays,:salaryMethodCode,:hrDepartmentCode,\n"
                                + ":TotalEarn,:TotalDed,:leave,:UEntDt,:Uname,:cl,:el,:pp,:aa,:hd,:lwp,:loanDocId,:loanEmiDate," + salaryMdl.getEarningDeductionValues() + ")";

                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setInt("vSno", vSno);
                        psTemp.setString("EmployeeCode", DmShared.foriegnKey(salaryMdl.getEmployeeCode()));
                        psTemp.setString("loanDocId", DmShared.foriegnKey(salaryMdl.getLoanDocID()));
                        psTemp.setString("loanEmiDate", DmShared.foriegnKey(salaryMdl.getEmiDate()));
                        psTemp.setString("Father", DmShared.xNull(salaryMdl.getFather()));
                        psTemp.setString("BasicPay", DmShared.iNull(salaryMdl.getBasic()).toString());
                        psTemp.setString("CalcBasic", DmShared.iNull(salaryMdl.getCalcBasic()).toString());
                        psTemp.setString("GrossSalary", DmShared.iNull(salaryMdl.getGrossSalary()).toString());
                        psTemp.setString("NetSalary", DmShared.iNull(salaryMdl.getNetSalary()).toString());
                        psTemp.setString("TotalDays", DmShared.xNull(salaryMdl.getTdays()));
                        psTemp.setString("OffDays", DmShared.xNull(salaryMdl.getOffDays()));
                        psTemp.setString("Holidays", DmShared.xNull(salaryMdl.getHolidays()));
                        psTemp.setString("WeekDays", DmShared.xNull(salaryMdl.getWdays()));

                        psTemp.setString("cl", DmShared.xNull(salaryMdl.getCasual_Leave()));
                        psTemp.setString("el", DmShared.xNull(salaryMdl.getEarn_Leave()));
                        psTemp.setString("pp", DmShared.xNull(salaryMdl.getPresent()));
                        psTemp.setString("aa", DmShared.xNull(salaryMdl.getAbsent()));
                        psTemp.setString("hd", DmShared.xNull(salaryMdl.getHalfDay()));
                        psTemp.setString("lwp", DmShared.xNull(salaryMdl.getLwp()));

                        psTemp.setString("salaryMethodCode", DmShared.foriegnKey(salaryMdl.getSalaryMethodCode()));
                        psTemp.setString("hrDepartmentCode", DmShared.foriegnKey(salaryMdl.getDepartmentCode()));

                        psTemp.setString("TotalEarn", DmShared.xNull(salaryMdl.getTotalEarn()));
                        psTemp.setString("TotalDed", DmShared.xNull(salaryMdl.getTotalDed()));
                        psTemp.setString("leave", DmShared.xNull(salaryMdl.getLeave()));
                        psTemp.setTimestamp("UEntDt", DmServer.getServerTimestamp());
                        psTemp.setString("Uname", DmServer.getSsnUserName(this));

                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

            // DmResult data = ....
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
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select max(hr.docId) as docId,max(hr.salaryMethodCode) as SalaryMethodCode,"
                    + "max(hr.hrDepartmentCode) as DepartmentName,max(scd.name) as SalaryMethodName,\n"
                    + "max(sg.name) as HrDepartmentName\n"
                    + "from HrEmployeeSalary hr\n"
                    + "left join salaryCodeDefinitionMast scd on scd.code = hr.salaryMethodCode \n"
                    + "left join subGroup sg on sg.subcode = hr.hrDepartmentCode \n"
                    + "group by docId ";//order by max(scd.name)
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSalaryMethod(DmShared.xNull(rsTemp.getString("SalaryMethodName")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("HrDepartmentName")));
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

    private List<GrdSalaryCreation_Mdl> getSalary(SalaryCreation_Mdl param, Connection cnTemp) {
        List<GrdSalaryCreation_Mdl> lstTemp = new ArrayList<>();
        GrdSalaryCreation_Mdl listNode;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp1 = null;
        DmPreparedStatement psTemp1 = null;
        ResultSet rsTemp2 = null;
        DmPreparedStatement psTemp2 = null;
        String sqlQuery, condition, insertFieldCode, insertFieldvalue;
        double grossSalary, totalDed = 0, totalEarn = 0, netSalary = 0, totalLeave = 0, grossSalaryFull;

        try {
            condition = "";
            insertFieldCode = "";
            insertFieldvalue = "";
            if (DmShared.iNull(param.getDepartmentCode()).longValue() != 0) {
                condition = " 	where sg2.hrDepartmentCode= :hrDepartmentCode \n";
            }
            sqlQuery = " select a.employeeCode,max(sg.name) as employeeName\n"
//                    + " ,sum(a.salaryDays) as salaryDays,sum(a.holiday) as holiday,\n"
                    + " ,(max(ep.days_In_SalaryPeriod)- sum(LWP)-sum(notAppliCable)) as salaryDays,sum(a.holiday) as holiday,\n"
                    + " sum(a.offDay) as offDays,sum(a.leaves) as leaves,sum(a.halfDay) as halfDay,sum(earnedLeaves) as earnedLeaves\n"
                    + " ,sum(LWP) as LWP,sum(casualLeaves) as casualLeaves,sum(a.presents) as presents,sum(absents) as absents\n"
                    + " ,max(ep.days_In_SalaryPeriod) as days_In_SalaryPeriod,max(sg.basicPay) as basicPay\n"
//                    + " ,round((max(sg.basicPay)/max(ep.days_In_SalaryPeriod))* (case when max(ep.days_In_SalaryPeriod)< sum(a.salaryDays) then max(ep.days_In_SalaryPeriod) else sum(a.salaryDays) end),0) as calcBasic\n"
                    + " ,round((max(sg.basicPay)/max(ep.days_In_SalaryPeriod))* (max(ep.days_In_SalaryPeriod)- sum(LWP)-sum(notAppliCable) ),0) as calcBasic\n"
                    + " ,max(sg2.hrDepartmentCode) as hrDepartmentCode,max(sg2.fathername) as fathername\n"
                    + " ,max(lon.docid) as loanDocID,max(lon.emiDate) as loanEmiDate\n"
                    + " 	from (\n"
                    + " 	/********************************Fetching All Leaves and Attendance data***********************************************/\n"
                    + " 			select eald.employeeCode,acdm.name as att_LvName,eald.fromDate,eald.att_Lev_OT,eald.days\n"
                    + " 			,case acdm.manualCode when 'HH' then 1 else 0 end as holiday,case acdm.manualCode when 'OFD' then 1 else 0 end as offDay\n"
                    + " 			,case acdm.manualCode when 'HD' then 1 else 0 end as halfDay,case acdm.manualCode when 'EL' then 1 else 0 end as earnedLeaves\n"
                    + " 			,case acdm.manualCode when 'CL' then 1 else 0 end as casualLeaves,case acdm.manualCode when 'LWP' then 1 else 0 end as LWP\n"
                    + " 			,case acdm.manualCode when 'PP' then 1 else 0 end as presents,case acdm.manualCode when 'AA' then 1 else 0 end as absents\n"
                    + " 			,case acdm.manualCode when 'NA' then 1 else 0 end as notAppliCable\n"
                    + " 			,case when acdm.type_Leave_Attendance=2 then 1 else 0 end as leaves,case when acdm.plusMinus=0 then 0 else acdm.days end as salaryDays\n"
                    + " 				from Employee_Attendance_Leave_Detail_Final eald\n"
                    + " 				left join attendenceCodeDefinitionMast acdm on isNull(eald.attendanceCode,eald.leaveCode)=acdm.code\n"
                    + " 				where eald.fromDate between :fromDate and :toDate\n"
                    + " 		 ) a\n"
                    + " 	left join subGroup sg on a.employeeCode=sg.subCode\n"
                    + " 	left join subGroup2 sg2 on sg.subCode=sg2.subCode\n"
                    + " 	left join enviro_Payroll ep on ep.site_Code =1\n"
                    + " 	left join (\n"
                    + "                   select elh.EmployeeCode,elh.docid,eld.emiDate\n"
                    + "                   	from employeeLoanHead elh\n"
                    + "                   	left join employeeLoanDetail eld on elh.docId=eld.docId\n"
                    + "                   	where emiDate between :fromDate and :toDate\n"
                    + "                   ) as lon on a.employeeCode=lon.EmployeeCode\n"
                    + condition
                    + " 	group by a.employeeCode\n";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("fromDate", param.getFromDate());
            psTemp.setString("toDate", param.getToDate());
            if (DmShared.iNull(param.getDepartmentCode()).longValue() != 0) {
                psTemp.setString("hrDepartmentCode", DmShared.xNull(param.getDepartmentCode()));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grossSalary = 0;
                totalDed = 0;
                totalEarn = 0;
                netSalary = 0;
                totalLeave = 0;
                sqlQuery = "select eed.EarningCode,max(edm.name) as E_D_Name,max(edm.shortName) as shortName \n"
                        + " , :calcBasic +sum(isNull(a.amount1,0)) as calc_Gross,:basicPay+sum(isNull(a.fullAmount,0)) as full_Gross\n"
                        + " 	from employeeEarningDed_Detail eed \n"
                        + " 	left join earningDeductionMastPayroll edm on eed.EarningCode =edm.code\n"
                        + " 	left join (\n"
                        + " 			  select sohc.earning_Deduction_Code,edmo.name,eed.Amount,sohc.overHeadCode\n"
                        + " 				/*******************************************************************************************************************************/\n"
                        + " 				/***************************Calculate on <==>1/Actual WorkingDays, 0<==>Fix****************************************************/\n"
                        + " 				/*********************************amt_Per=1<==>On Percint, 2<==>Amount*********************************************************/\n"
                        + " 				/*******************************************************************************************************************************/\n"
                        + " 				,case edmo.calculatedOn when 1 then (case when eed.amt_Per =1 then :calcBasic * eed.Amount/100 else (eed.amount/ :days_In_SalaryPeriod)* :salaryDays end) \n"
                        + " 				 when 0 then (case when eed.amt_Per =2 then eed.amount end) end as amount1\n"
                        + " 				 ,case edmo.calculatedOn when 1 then (case when eed.amt_Per =1 then (case edmo.roundOff when 1 then round(:basicPay * eed.Amount/100,0) else round(:basicPay * eed.Amount/100,2) end) else eed.amount end) \n"
                        + " 				 when 0 then (case when eed.amt_Per =2 then eed.amount end) end as fullAmount\n"
                        + " 					from salary_OverHeadCalculateOn sohc\n"
                        + " 					left join earningDeductionMastPayroll edmo on sohc.overheadCode=edmo.code\n"
                        + " 					left join employeeEarningDed_Detail eed on sohc.overheadCode=eed.EarningCode\n"
                        + " 					where eed.EmployeeCode= :employeeCode \n"
                        + " 			  ) a on eed.EarningCode=a.earning_Deduction_Code\n"
                        + " 	where edm.shortName='GS'\n"
                        + " 	group by eed.EarningCode \n";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("employeeCode", DmShared.xNull(rsTemp.getString("employeeCode")));
                psTemp1.setDouble("calcBasic", DmShared.vNull(rsTemp.getDouble("calcBasic")));
                psTemp1.setDouble("basicPay", DmShared.vNull(rsTemp.getDouble("basicPay")));
                psTemp1.setDouble("days_In_SalaryPeriod", DmShared.vNull(rsTemp.getDouble("days_In_SalaryPeriod")));
                psTemp1.setDouble("salaryDays", DmShared.vNull(rsTemp.getDouble("salaryDays")));
                rsTemp1 = psTemp1.executeQuery();

                listNode = new GrdSalaryCreation_Mdl();
                listNode.setEmployee(DmShared.xNull(rsTemp.getString("employeeName")));
                listNode.setSalaryMethodCode(DmShared.xNull(param.getSalaryMethodCode()));
                listNode.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("hrDepartmentCode")));
                listNode.setLoanDocID(DmShared.xNull(rsTemp.getString("loanDocID")));
                listNode.setEmiDate(DmShared.xNull(rsTemp.getString("loanEmiDate")));
                listNode.setFather(DmShared.xNull(rsTemp.getString("fathername")));
                listNode.setBasic(DmShared.xNull(rsTemp.getString("basicPay")));
                listNode.setCalcBasic(DmShared.xNull(rsTemp.getString("calcBasic")));
                listNode.setOffDays(DmShared.xNull(rsTemp.getString("offDays")));
                listNode.setHolidays(DmShared.xNull(rsTemp.getString("holiday")));
                listNode.setTdays(DmShared.xNull(rsTemp.getString("salaryDays")));
                listNode.setWdays(DmShared.xNull(rsTemp.getString("days_In_SalaryPeriod")));
                listNode.setLeave(DmShared.xNull(rsTemp.getString("leaves")));

                listNode.setEarn_Leave(DmShared.xNull(rsTemp.getString("earnedLeaves")));
                listNode.setCasual_Leave(DmShared.xNull(rsTemp.getString("casualLeaves")));
                listNode.setLwp(DmShared.xNull(rsTemp.getString("LWP")));
                listNode.setPresent(DmShared.xNull(rsTemp.getString("presents")));
                listNode.setAbsent(DmShared.xNull(rsTemp.getString("halfDay")));
                listNode.setHalfDay(DmShared.xNull(rsTemp.getString("absents")));

                if (rsTemp1.next()) {
                    insertFieldCode = DmShared.xNull(rsTemp1.getString("shortName"));
                    insertFieldvalue = DmShared.xNull(rsTemp1.getString("calc_Gross"));
                    grossSalary = DmShared.vNull(rsTemp1.getDouble("calc_Gross")).doubleValue();
                    grossSalaryFull = DmShared.vNull(rsTemp1.getDouble("full_Gross")).doubleValue();
//                    netSalary = DmShared.vNull(grossSalary).doubleValue();
                    netSalary = DmShared.vNull(rsTemp.getDouble("calcBasic")).doubleValue();

                    //*************************************************************************************************/
                    //******************************Calculation off all earning deductions*****************************/
                    //*************************************************************************************************/
                    sqlQuery = "select main.EarningCode,max(main.shortName) as shortName,max(main.amt_Per) as amt_Per,max(main.Amount) as a_P_value\n"
                            + " ,max(main.limit_Of_Amount) as limit_Of_Amount,max(main.nature) as nature,sum(main.calc_EarningAmt) as calc_EarningAmt\n"
                            + " ,sum(main.FullEarningAmt) as FullEarningAmt,max(main.limit_For_Calculation_Restriction) as limit_For_Calculation_Restriction\n"
                            + " ,case when max(main.limit_Of_Amount)>0 \n"
                            + " 	then (case when max(main.limit_For_Calculation_Restriction) = 0 \n"
                            + " 		then (case when sum(main.FullEarningAmt)>=max(main.limit_Of_Amount) \n"
                            + " 				then (case max(main.amt_Per) when 0 then (case when max(main.roundOff)=1 then round(max(main.limit_Of_Amount)*max(main.Amount)/100,0) else round(max(main.limit_Of_Amount)*max(main.Amount)/100,2) end) else sum(main.calc_EarningAmt) end) \n"
                            + " 			else sum(main.calc_EarningAmt) end) when max(main.limit_For_Calculation_Restriction) = 1 \n"
                            + " 		then (case when sum(main.FullEarningAmt)>=max(main.limit_Of_Amount) \n"
                            + " 				then (0)  else max(main.calc_EarningAmt) \n"
                            + " 			end)\n"
                            + " 	end )\n"
                            + " else sum(main.calc_EarningAmt) end as actualEarningDeduction\n"
                            + " 	from (\n"
                            + " 		select eed.EarningCode,edm.name as E_D_Name,edm.shortName,eed.amt_Per,eed.Amount,sohc.earning_Deduction_Code\n"
                            + " 		,edmo.shortName as subShortName,sohc.overHeadCode,isNull(edm.limit_Of_Amount,0) as limit_Of_Amount\n"
                            + " 		,edm.nature,edm.limit_For_Calculation_Restriction,edm.roundOff \n"
                            + " 		,case edmo.shortName\n"
                            + " 		/***********************************************Calculation of Main Earning Deducton with Calc Basic**************************************************/\n"
                            + " 		when 'BS' then (\n"
                            + " 						case edm.calculatedOn when 0 then (case when eed.amt_Per =1 then (case when edm.roundOff=1 then round(:calcBasic * eed.Amount/100,0) else round(:calcBasic * eed.Amount/100,2) end) else (case when edm.roundOff=1 then round((eed.amount/ :days_In_SalaryPeriod)* :salaryDays,0) else round((eed.amount/ :days_In_SalaryPeriod)* :salaryDays,2) end) end)\n"
                            + " 						else (case when eed.amt_Per =2 then eed.amount end) end\n"
                            + " 						) \n"
                            + " 		when 'GS' then (\n"
                            + " 						case edm.calculatedOn when 0 then (case when eed.amt_Per =1 then (case when edm.roundOff=1 then round(:grossSalary * eed.Amount/100,0) else round(:grossSalary * eed.Amount/100,2) end) else (eed.amount/ :days_In_SalaryPeriod)* :salaryDays end)\n"
                            + " 						else (case when eed.amt_Per =2 then eed.amount end) end\n"
                            + " 						)\n"
                            + " 			else	\n"
                            + " 		/*******************************************Calculation of Earning Deducton under Main Heads with Calc Basic*********************************************/\n"
                            + " 			(\n"
                            + " 			select \n"
                            + " 			case edmo2.shortName when 'BS' then \n"
                            + " 				(\n"
                            + " 				case edmo1.calculatedOn when 0 then (case when eedsub.amt_Per =1 then (case when edmo1.roundOff=1 then round((:calcBasic * eedsub.Amount/100),0) else round((:calcBasic * eedsub.Amount/100)* eedsub.Amount/100,2) end) else (case when edmo1.roundOff=1 then round(((eedsub.amount/ :days_In_SalaryPeriod)* :salaryDays)*eed.Amount/100,0) else round((eedsub.amount/ :days_In_SalaryPeriod)* :salaryDays,2) end) end)\n"
                            + " 				else (case when eedsub.amt_Per =2 then (case when eed.amt_Per=1 then (eedsub.amount*eed.Amount/100) else eedsub.amount end) end) end\n"
                            + " 				)\n"
                            + " 			when 'GS' then \n"
                            + " 				(\n"
                            + " 				case edmo1.calculatedOn when 0 then (case when eedsub.amt_Per =1 then (case when edmo1.roundOff=1 then round((:grossSalary * eedsub.Amount/100),0) else round((:grossSalary * eedsub.Amount/100)* eedsub.Amount/100,2) end) else (case when edmo1.roundOff=1 then round((eedsub.amount/ :days_In_SalaryPeriod)* :salaryDays,0) else round((eedsub.amount/ :days_In_SalaryPeriod)* :salaryDays,2) end) end)\n"
                            + " 				else (case when eedsub.amt_Per =2 then (case when eed.amt_Per=1 then (eedsub.amount*eed.Amount/100) else eedsub.amount end) end) end\n"
                            + " 				) \n"
                            + " 				end as subAmt \n"
                            + " 				from employeeEarningDed_Detail eedsub \n"
                            + " 				left join salary_OverHeadCalculateOn sohc on eedsub.earningCode=sohc.earning_Deduction_Code\n"
                            + " 				left join earningDeductionMastPayroll edmo1 on sohc.earning_Deduction_Code=edmo1.code \n"
                            + " 				left join earningDeductionMastPayroll edmo2 on sohc.overHeadCode=edmo2.code \n"
                            + " 				where eedsub.EmployeeCode= :employeeCode and edmo1.shortName=edmo.shortName\n"
                            + " 			) \n"
                            + " 		end as calc_EarningAmt\n"
                            + "		,case edmo.shortName \n"
                            + " 		/*******************************************Calculation of Earning Deducton under Main Heads with Full Basic*********************************************/\n"
                            + " 		when 'BS' then :calcBasic \n"
                            + " 		when 'GS' then :grossSalary\n"
                            + " 			else	\n"
                            + " 		/*******************************************Calculation of Earning Deducton under Main Heads with Full Basic*********************************************/\n"
                            + " 			(\n"
                            + " 			select \n"
                            + " 			case edmo2.shortName when 'BS' then \n"
                            + " 				(\n"
                            + " 				case edmo1.calculatedOn when 1 then (case when eed.amt_Per =1 then (case when edmo1.roundOff=1 then round((:basicPay * eed.Amount/100)* eed.Amount/100,0) else round((:basicPay * eed.Amount/100)* eed.Amount/100,2) end) else eed.amount end)\n"
                            + " 				else (case when eed.amt_Per =2 then eed.amount end) end\n"
                            + " 				)\n"
                            + " 			when 'GS' then \n"
                            + " 				(\n"
                            + " 				case edmo1.calculatedOn when 1 then (case when eed.amt_Per =1 then (case when edmo1.roundOff=1 then round((:grossSalary * eed.Amount/100)* eed.Amount/100,0) else round((:grossSalary * eed.Amount/100)* eed.Amount/100,2) end) else eed.amount end)\n"
                            + " 				else (case when eed.amt_Per =2 then eed.amount end) end\n"
                            + " 				) \n"
                            + " 				end as subAmt \n"
                            + " 				from employeeEarningDed_Detail eed \n"
                            + " 				left join salary_OverHeadCalculateOn sohc on eed.earningCode=sohc.earning_Deduction_Code\n"
                            + " 				left join earningDeductionMastPayroll edmo1 on sohc.earning_Deduction_Code=edmo1.code \n"
                            + " 				left join earningDeductionMastPayroll edmo2 on sohc.overHeadCode=edmo2.code \n"
                            + " 				where eed.EmployeeCode= :employeeCode and edmo1.shortName=edmo.shortName\n"
                            + " 			)\n"
                            + " 		end as FullEarningAmt\n"
                            + " 			from employeeEarningDed_Detail eed \n"
                            + " 			left join earningDeductionMastPayroll edm on eed.EarningCode=edm.code \n"
                            + " 			left join salary_OverHeadCalculateOn sohc on eed.earningCode=sohc.earning_Deduction_Code\n"
                            + " 			left join earningDeductionMastPayroll edmo on sohc.overHeadCode=edmo.code \n"
                            + " 			where eed.EmployeeCode= :employeeCode and edm.shortName<>'GS'\n"
                            + "                 /*******************************************Integration of PERKS Entry*********************************************/\n"
                            + "                 union all\n"
                            + "                 select ped.earningDeductionCode as earningcode,edmp.name as e_D_Name,edmp.shortName,2 as amt_Per,ped.value as amount,ped.earningDeductionCode as earning_Deduction_Code\n"
                            + "                 ,'BS' as subShortName,ped.earningDeductionCode as overHeadCode,0 as limit_Of_Amount,ped.nature,0 limit_For_Calculation_Restriction,1 as roundOff\n"
                            + "                 ,ped.value as calc_EarningAmt,ped.value as fullEarningAmt\n"
                            + "                 	from perksEntry_Detail ped\n"
                            + "                 	left join perksEntry pe on ped.docId=pe.docId\n"
                            + "                 	left join earningDeductionMastPayroll edmp on ped.earningDeductionCode=edmp.code\n"
                            + "                 	where pe.EmployeeCode= :employeeCode and pe.salaryPeriod=:salaryPeriod\n"
                            + "                 /*******************************************Integration of LOAN Entry*********************************************/\n"
                            + "                 union all\n"
                            + "                 select elh.earningDeductionCode as earningcode,edmp.name as e_D_Name,edmp.shortName,2 as amt_Per,eld.emiAmt as amount,elh.earningDeductionCode as earning_Deduction_Code\n"
                            + "                 ,'BS' as subShortName,elh.earningDeductionCode as overHeadCode,0 as limit_Of_Amount,edmp.nature,0 limit_For_Calculation_Restriction,1 as roundOff\n"
                            + "                 ,eld.emiAmt as calc_EarningAmt,eld.emiAmt as fullEarningAmt\n"
                            + "                 	from employeeLoanHead elh\n"
                            + "                 	left join employeeLoanDetail eld on eld.docId=elh.docId\n"
                            + "                 	left join earningDeductionMastPayroll edmp on elh.earningDeductionCode=edmp.code\n"
                            + "                 	where eld.docid= :loanDocID and eld.emiDate= :emiDate and isNull(elh.stopStatus,0)=0\n"
                            + " 			) as main\n"
                            + " group by main.EarningCode\n";
                    psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp2.setString("employeeCode", DmShared.xNull(rsTemp.getString("employeeCode")));
                    psTemp2.setDouble("calcBasic", DmShared.vNull(rsTemp.getDouble("calcBasic")));
                    psTemp2.setDouble("basicPay", DmShared.vNull(rsTemp.getDouble("basicPay")));
                    psTemp2.setDouble("grossSalary", DmShared.vNull(grossSalary));
                    psTemp2.setDouble("salaryDays", DmShared.vNull(rsTemp.getDouble("salaryDays")));
                    psTemp2.setString("salaryPeriod", DmShared.xNull(param.getSalaryMethodCode()));
                    psTemp2.setDouble("days_In_SalaryPeriod", DmShared.vNull(rsTemp.getDouble("days_In_SalaryPeriod")));
                    psTemp2.setString("loanDocID", DmShared.xNull(rsTemp.getString("loanDocID")));
                    psTemp2.setString("emiDate", DmShared.xNull(rsTemp.getString("loanEmiDate")));
                    rsTemp2 = psTemp2.executeQuery();
                    while (rsTemp2.next()) {
                        insertFieldCode += " ," + DmShared.xNull(rsTemp2.getString("shortName"));
                        insertFieldvalue += " ," + DmShared.vNull(rsTemp2.getString("actualEarningDeduction")).toString();
                        if (DmShared.vNull(rsTemp2.getString("nature")).intValue() == 1) {
                            totalEarn += DmShared.vNull(rsTemp2.getString("actualEarningDeduction")).doubleValue();
                            netSalary += DmShared.vNull(rsTemp2.getString("actualEarningDeduction")).doubleValue();
                        } else if (DmShared.vNull(rsTemp2.getString("nature")).intValue() == 0) {
                            totalDed += DmShared.vNull(rsTemp2.getString("actualEarningDeduction")).doubleValue();
                            netSalary -= DmShared.vNull(rsTemp2.getString("actualEarningDeduction")).doubleValue();
                        }
                    }
                    listNode.setEarningDeductionFields(insertFieldCode);
                    listNode.setEarningDeductionValues(insertFieldvalue);
                    listNode.setTotalEarn(DmShared.vNull(totalEarn).toString());
                    listNode.setTotalDed(DmShared.vNull(totalDed).toString());
                    listNode.setGrossSalary(DmShared.vNull(grossSalary).toString());
                    listNode.setNetSalary(DmShared.vNull(netSalary).toString());
                    lstTemp.add(listNode);
                    psTemp2.close();
                    rsTemp2.close();
                }
                psTemp1.close();
                rsTemp1.close();
            }
            psTemp.close();
            rsTemp.close();

        } catch (Exception e) {
//            rsTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return lstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getDefaultValues() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<>();
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        Connection cnTemp = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setSharedKeyFieldCode(DmServer.getVType(vCatSalaryCreation, cnTemp));
            mrTemp.setSharedKeyFieldName(vCatSalaryCreation);

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            dmrTemp.setValue1(DmServer.getServerDateTime()[0]);
            dmrTemp.setValue2(DmServer.getServerDateTime()[1]);
            cnTemp.close();

        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
}
