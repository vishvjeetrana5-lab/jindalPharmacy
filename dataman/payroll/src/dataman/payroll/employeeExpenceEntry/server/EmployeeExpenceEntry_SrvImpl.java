package dataman.payroll.employeeExpenceEntry.server;

import static dataman.dmBase.dmBaseStatic.server.DmBaseServer.getServerDateTime;
import dataman.payroll.employeeExpenceEntry.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeExpenceEntry_SrvImpl extends DmRSSPrivate implements EmployeeExpenceEntry_Srv {

    LibraryVoucherCategory pvc = new LibraryVoucherCategory();
    String v_Type = pvc.PERKS_ENTRY;

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT docId FROM perksEntry");
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();
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
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From perksEntry_Detail Where docId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From perksEntry Where docId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
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
    public DmResult<List<GridEmployeeExpenceEntry_Mdl>> getRecords() {
        DmResult<List<GridEmployeeExpenceEntry_Mdl>> dmrTemp = new DmResult();
        List<GridEmployeeExpenceEntry_Mdl> grdMdlList = new ArrayList<GridEmployeeExpenceEntry_Mdl>();
        GridEmployeeExpenceEntry_Mdl grdMdlLstNode;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData(grdMdlList);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

//            sqlQuery = "select code, overHeadName,nature from empexpGD ";
            sqlQuery = "select edmp.code as earnigDedCode,edmp.name as earnigDedName,edmp.nature as natureCode,0 as eDValue \n"
                    + " 	,case edmp.nature when 0 then '-' when 1 then '+' end as nature\n"
                    + " 	,case edmp.nature when 0 then 'Deduction' when 1 then 'Earning' end as natureName\n"
                    + " 	from earningDeductionMastPayroll edmp\n"
                    + " 	where edmp.useInEntry=1\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                grdMdlLstNode = new GridEmployeeExpenceEntry_Mdl();
                grdMdlLstNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("earnigDedCode")).toString());
                grdMdlLstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("earnigDedName")));
                grdMdlLstNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                grdMdlLstNode.setNatureCode(DmShared.xNull(rsTemp.getString("natureCode")));
                grdMdlLstNode.setNatureName(DmShared.xNull(rsTemp.getString("natureName")));
                grdMdlLstNode.setValue(DmServer.vNull(rsTemp.getString("eDValue"), "0.00"));

                grdMdlList.add(grdMdlLstNode);
            }
            rsTemp.close();
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
    public DmResult<EmployeeExpenceEntry_Mdl> getMoveRec(String strKey) {
        DmResult<EmployeeExpenceEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        EmployeeExpenceEntry_Mdl mrTemp = new EmployeeExpenceEntry_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select pe.v_Date as entryDate, sg.name as name, scdm.name as sp "
                    + ", pe.employeeCode as EmpCode, pe.salaryPeriod as spCode ,pe.preparedBy,pe.modifiedBy,pe.v_Type "
                    + " from perksEntry as pe \n"
                    + "left join subGroup as sg on sg.subCode=pe.employeeCode\n"
                    + "left join salaryCodeDefinitionMast as scdm on scdm.code= pe.salaryPeriod"
                    + "   where docId= :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(strKey).toString());
                mrTemp.setEntryDate(DmShared.dNull(rsTemp.getDate("entryDate")));
//============================fetched name of all DmHelp===============
                mrTemp.setEmployeeText(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSalaryPeriodText(DmShared.xNull(rsTemp.getString("sp")));
//=========================fetched code of all Dmhelp===================             
                mrTemp.setEmployeeTag(DmShared.xNull(rsTemp.getString("EmpCode")));
                mrTemp.setSalaryPeriodTag(DmShared.xNull(rsTemp.getString("spCode")));

                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));

                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("modifiedBy")));

                if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() != 0) {
                    //------------------------------ getGridDetail---------------------------------------------------
                    mrTemp.getGrdDetailMdl().addAll(getDetail(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), cnTemp));
                }

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
    public DmResult<String> saveRecord(UserPermission userPermission, EmployeeExpenceEntry_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String[] currentDateTime;
        ResultSet rsTemp = null;
        String voucherTypeCode = "";
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended lstNode;

        dmrTemp.setData("");
        try {
            EmployeeExpenceEntry_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                currentDateTime = getServerDateTime();

                if (DmShared.dNull(mrTemp.getDtVoucher()).trim().equalsIgnoreCase("")) {
                    mrTemp.setDtVoucher(currentDateTime[0]);
                }
                voucherTypeCode = DmServer.getCodeViaManualCode(v_Type, "short_Name", "voucher_Type", "v_Type", cnTemp);
                mrTemp.setVoucherTypeCode(voucherTypeCode);

                if (DmShared.xNull(mrTemp.getTime()).trim().equalsIgnoreCase("")) {
                    mrTemp.setTime(currentDateTime[1]);
                }
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("perksEntry", "docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getDtVoucher(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(mrTemp.getRecId().getRecIdValue());

            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Select Count(*) As Cnt From perksEntry Where employeeCode = :Employee and salaryPeriod = :sp and docId<> :newEnteryCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Employee", mrTemp.getEmployeeTag());
            psTemp.setString("sp", mrTemp.getSalaryPeriodTag());
            psTemp.setString("newEnteryCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Duplicate Employee and salaryPeriod Not Allowed !!!");
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {

                sqlQuery = "Insert Into perksEntry(docId, v_Type,v_No,recIdPrefix, "
                        + "recId, v_Prefix, v_Date,site_code,employeeCode,salaryPeriod,u_Name,u_EntDt,preparedBy)"
                        + " values (:code , :v_Type, :v_No,  :recIdPrefix,:recId,:v_Prefix,:v_Date"
                        + ",:site_code,:employeeCode,:salaryPeriod,:u_Name,:u_EntDt,:preparedBy)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("v_Type", mrTemp.getVoucherTypeCode());
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecId().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("recId", mrTemp.getRecId().getRecIdValue());
                psTemp.setString("v_Prefix", voucherPrefix);

                psTemp.setString("site_code", siteCode);
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

                psTemp.setString("v_Date", mrTemp.getEntryDate());
                psTemp.setString("employeeCode", mrTemp.getEmployeeTag());
                psTemp.setString("salaryPeriod", mrTemp.getSalaryPeriodTag());
                psTemp.setString("preparedBy", userName);
            } else {
                sqlQuery = "Update perksEntry Set "
                        + " u_EntDt = :u_EntDt, "
                        + " v_Date = :v_Date, "
                        + " employeeCode = :employeeCode, "
                        + " salaryPeriod = :salaryPeriod, "
                        + " modifiedBy = :modifiedBy "
                        + " where docId = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("v_Date", mrTemp.getEntryDate());
                psTemp.setString("employeeCode", mrTemp.getEmployeeTag());
                psTemp.setString("salaryPeriod", mrTemp.getSalaryPeriodTag());
                psTemp.setString("modifiedBy", userName);
            }
            psTemp.executeUpdate();
            psTemp.close();

            //===============Delete Grid Info=======================
            sqlQuery = "Delete From perksEntry_Detail Where docId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();
            //================Insert Grid info======================         
            List<GridEmployeeExpenceEntry_Mdl> listLogDetail = mrTemp.getGrdDetailMdl();
            for (GridEmployeeExpenceEntry_Mdl logDetail_Mdl : listLogDetail) {

                if (!DmShared.xNull(logDetail_Mdl.getValue()).equalsIgnoreCase("") && !DmShared.xNull(logDetail_Mdl.getValue()).equalsIgnoreCase("0") && !DmShared.xNull(logDetail_Mdl.getValue()).equalsIgnoreCase("0.0")) {
                    sqlQuery = "insert Into perksEntry_Detail (docId,nature,value,earningDeductionCode) Values  "
                            + "(:docId,:nature,:value,:earningDeductionCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("nature", DmShared.xNull(logDetail_Mdl.getNatureCode()));

                    psTemp.setString("earningDeductionCode", DmShared.xNull(logDetail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("value", DmShared.xNull(logDetail_Mdl.getValue()));

                    psTemp.executeUpdate();

                }
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select ee.docid as code,ee.v_Date as EntDate ,sg.name as name, scdm.name as sp "
                    + " from perksEntry as ee \n"
                    + " left join subGroup as sg on sg.subCode=ee.employeeCode\n"
                    + " left join salaryCodeDefinitionMast as scdm on scdm.code= ee.salaryPeriod";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setEntryDate(DmShared.dNull(rsTemp.getDate("EntDate")));
                listNode.setEmployee(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSalaryPeriod(DmShared.xNull(rsTemp.getString("sp")));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
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

            sqlWithParam.setSQL("select sg.subCode as code,sg.name as Employee,isNull(sg2.fathername,'') fatherName,isNull(sg2.doj,'') doj,\n"
                    + "isNull(sgd.name,'') as department,isNull(sg2.hrDepartmentCode,'') DepartmentCode \n"
                    + "from subGroup  sg\n"
                    + "left join subGroup2 sg2 on sg2.subCode = sg.subCode\n"
                    + "left join subGroupNature sgn on sgn.code = sg.filterNature \n"
                    + "left join subGroup sgd on sgd.subCode = sg2.hrDepartmentCode\n"
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
    public DmResultPageing<List<SalaryMethod_Mdl>> getTxtHSalaryMethod(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<SalaryMethod_Mdl>> rstTemp = new DmResultPageing<>();
        List<SalaryMethod_Mdl> data = new ArrayList<>();
        SalaryMethod_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select code,name,fromDate from salaryCodeDefinitionMast where site_code =" + DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new SalaryMethod_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSalaryMethodName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setFromDate(DmShared.xNull(rsTemp.getString("fromDate")));
                data.add(listNode);
            }
            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setData(data);
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

    public List<GridEmployeeExpenceEntry_Mdl> getDetail(String strKey, Connection cnTemp) {
        List<GridEmployeeExpenceEntry_Mdl> lstNode = new ArrayList<GridEmployeeExpenceEntry_Mdl>();
        GridEmployeeExpenceEntry_Mdl node;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        String sqlQuery;

        try {

            /*====================================< Grid >====================================*/
            sqlQuery = "select edmp.code as earnigDedCode,edmp.name as earnigDedName,edmp.nature as natureCode,isNull(ped.value,0) as eDValue \n"
                    + " 	,case edmp.nature when 0 then '-' when 1 then '+' end as nature\n"
                    + " 	,case edmp.nature when 0 then 'Deduction' when 1 then 'Earning' end as natureName\n"
                    + " 	from earningDeductionMastPayroll edmp\n"
                    + " 	left join perksEntry_Detail ped on edmp.code=ped.earningDeductionCode and ped.docid=:docid\n"
                    + " 	where edmp.useInEntry=1";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(strKey).longValue() != 0) {
                psTemp.setString("docid", DmShared.iNull(strKey).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GridEmployeeExpenceEntry_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("earnigDedCode")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("earnigDedName")));
                node.setNature(DmShared.xNull(rsTemp.getString("nature")));
                node.setNatureCode(DmShared.xNull(rsTemp.getString("natureCode")));
                node.setNatureName(DmShared.xNull(rsTemp.getString("natureName")));
                node.setValue(DmServer.vNull(rsTemp.getString("eDValue"), "0.00"));

                lstNode.add(node);

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

        return lstNode;
    }

}
