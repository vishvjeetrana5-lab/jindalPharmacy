package aarogya.master.workingDayEntry.server;

import aarogya.master.workingDayEntry.shared.Find_Mdl;
import aarogya.master.workingDayEntry.shared.WorkingDayEntry_Mdl;
import aarogya.master.workingDayEntry.shared.WorkingDayEntry_Srv;
import aarogya.master.workingDayEntry.shared.WorkingDayEntry_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkingDayEntry_SrvImpl extends DmRSSPrivate implements WorkingDayEntry_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
//    private final String vCat = "'" + pvc.WORKING_DAYS + "'";
    private String vCat = new ProjectVoucherCategory().WORKING_DAYS;

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String condition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("Select docId From earningDeduction ed  "
                    + "  left Join voucher_Type vt on vt.v_type = ed.v_type "
                    + "  where vt.category = '" + vCat + "'"
                    + "  Order By employeeCode desc  "
            );
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

    @Override
    public DmResult<WorkingDayEntry_Mdl> getMoveRec(String strKey) {
        DmResult<WorkingDayEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        WorkingDayEntry_Mdl mrTemp = new WorkingDayEntry_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  clm.docId, clm.employeeCode,clm.salaryCode,clm.minimumGurantee, scdm.name as salaryName ,sg.name, clm.workingDays,clm.u_Name, clm.preparedBy "
                    + "         from earningDeduction AS clm "
                    + "         left join subGroup as sg ON clm.employeeCode = sg.subCode "
                    + "         left join salaryCodeDefinitionMast as scdm ON scdm.code = clm.salaryCode"
                    + "         where clm.docId=:docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSalCode(DmShared.xNull(rsTemp.getString("salaryCode")));
                mrTemp.setSalName(DmShared.xNull(rsTemp.getString("salaryName")));
                mrTemp.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                mrTemp.setEmployeeName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setWorkingDays(DmServer.vNull(rsTemp.getString("workingDays"), "00"));
                mrTemp.setMinimumGurantee(DmServer.vNull(rsTemp.getString("minimumGurantee")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));

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
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From earningDeduction Where docId= :docId ";

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
    public DmResult<String> saveRecord(WorkingDayEntry_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;

        boolean blnAdd = false;
        String sqlQuery, userName, siteCode, vType;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            WorkingDayEntry_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            vType = DmShared.xNull(DmServer.getCodeViaManualCode(pvc.WORKING_DAYS, "short_Name", "voucher_Type", "v_type", cnTemp));

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(vType), voucherPrefix, cnTemp, this));
            }

            sqlQuery = " SELECT COUNT(sg.name) AS cnt FROM earningDeduction wd "
                    + "  left Join voucher_Type vt on vt.v_type = wd.v_type "
                    + "  left join subGroup as sg ON wd.employeeCode = sg.subCode WHERE employeeCode = :employeeCode and salaryCode = :salaryCode "
                    + "  and vt.category = '" + vCat + "'"
                    + "  AND docId <> :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("employeeCode", DmShared.xNull(mrTemp.getEmployeeCode()));
            psTemp.setString("salaryCode", DmShared.xNull(mrTemp.getSalCode()));
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Working Days already exists for " + mrTemp.getEmployeeName() + "," + mrTemp.getSalName() + ".");

            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "INSERT INTO earningDeduction ("
                        + "     docId, v_Prefix, v_Type, employeeCode, workingDays, salaryCode ,minimumGurantee, preparedBy, u_EntDt)"
                        + " VALUES ("
                        + "     :docId, :v_Prefix, :v_Type, :employeeCode, :workingDays,:salaryCode , :minimumGurantee,:preparedBy, :uEntDt)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("workingDays", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_Type", DmShared.foriegnKey(vType));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("employeeCode", DmShared.foriegnKey(mrTemp.getEmployeeCode()));
                psTemp.setString("salaryCode", DmShared.xNull(mrTemp.getSalCode()));
//                psTemp.setString("year", DmShared.xNull(mrTemp.getYearCode()));
                psTemp.setString("workingDays", DmShared.xNull(mrTemp.getWorkingDays()));
                psTemp.setString("minimumGurantee", DmShared.xNull(mrTemp.getMinimumGurantee()));
//                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

            } else {
                sqlQuery = "UPDATE earningDeduction SET"
                        + "  employeeCode = :employeeCode"
                        + " ,salaryCode = :salaryCode"
                        + " ,workingDays = :workingDays"
                        + " ,minimumGurantee = :minimumGurantee"
                        + " ,preparedBy = :preparedBy"
                        + " ,u_EntDt = :uEntDt"
                        + " ,u_Name = :u_Name"
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("employeeCode", DmShared.foriegnKey(mrTemp.getEmployeeCode()));
                psTemp.setString("salaryCode", DmShared.xNull(mrTemp.getSalCode()));
//                psTemp.setString("year", DmShared.xNull(mrTemp.getYearCode()));
                psTemp.setString("workingDays", DmShared.xNull(mrTemp.getWorkingDays()));
                psTemp.setString("minimumGurantee", DmShared.xNull(mrTemp.getMinimumGurantee()));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("u_Name", userName);
            }
            psTemp.executeUpdate();
            psTemp.close();

            /////////////////////////////////////////////////////////////////////////////////////
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCName(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select sg.subCode as code,sg.name "
                    + "  from subGroup sg "
                    + "  left join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + "	 where sg.nature in(6,15)");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rstTemp.setData(lstTemp);
            sqlQuery = " select	wd.docId, wd.employeeCode, sg.name as empName, wd.minimumGurantee,"
                    + "   wd.workingDays "
                    + "  ,scdm.name as salaryName "
                    + "	  from earningDeduction as wd "
                    + "   left join salaryCodeDefinitionMast as scdm ON scdm.code = wd.salaryCode"
                    + "    left join subGroup as sg ON wd.employeeCode = sg.subCode "
                    + "  left Join voucher_Type vt on vt.v_type = wd.v_type "
                    + "  where vt.category = '" + vCat + "'";

            sqlWithParam.setSQL(sqlQuery);
//            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("workingDays")));
//                listNode.setYearName(DmShared.xNull(rsTemp.getString("year")));
                listNode.setSalName(DmShared.xNull(rsTemp.getString("salaryName")));
                listNode.setEmployeeName(DmShared.xNull(rsTemp.getString("empName")));
                listNode.setWorkingDays(DmServer.vNull(rsTemp.getString("workingDays"), "00"));
                listNode.setMinimumGurantee(DmServer.vNull(rsTemp.getString("minimumGurantee")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSalaryPeriod(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select cm.code ,cm.name "
                    + " from salaryCodeDefinitionMast cm ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
