package dataman.utility.userMaster.server;

import dataman.dmBase.self.shared.UserMaster_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmEnum;
import dataman.utility.userMaster.shared.GrdLinkedUser_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.utility.userMaster.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserMaster_SrvImpl extends DmRSSPrivate implements UserMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        String sqlQuery = "";
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            if (DmServer.getSsnUserName(this).equalsIgnoreCase("SA")) {
                sqlQuery = "Select Code As SearchCode, User_Name As Name, PassWd From UserMast Where User_Name Not In ('SA') ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            } else if (DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER")) {
                sqlQuery = "Select Code As SearchCode, User_Name As Name, PassWd From UserMast Where User_Name Not In ('SA')";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            } else {
                sqlQuery = "Select Code As SearchCode,User_Name As Name,PassWd From UserMast Where User_Name = :usreName ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("usreName", DmServer.getSsnUserName(this));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
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
    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("Select User_Name From UserMast ");
            helper.addSort("User_Name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("User_Name")));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResult<UserMaster_Mdl> getMoveRec(String strKey) {
        DmResult<UserMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        UserMaster_Mdl mrTemp = new UserMaster_Mdl();
        String sqlQuery;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "SELECT  um.User_Name AS NAME, um.Description, um.PassWD, um.isAdmin, um.code,um.User_Role, um.u_name, "
                    + "         um.preparedBy, um.email, um.mobile, um.isActive, um.nature, un.name as natureName, um.UserLockTimeOut,"
                    + "         sdm.subCode as departmentCode, sdm.NAME as departmentName "
                    + "         FROM UserMast um "
                    + "         left join userNature un on un.code = um.nature"
                    + "         left join " + DmServer.getSsnDBName(this) + ".dbo.subgroup as sdm on sdm.subCode = um.departmentCode  "
                    + "         WHERE um.Code = :Code order by um.User_Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                mrTemp.setUserId(DmShared.xNull(rsTemp.getString("Code")));
                mrTemp.setRoleCode(DmShared.xNull(rsTemp.getString("User_Role")));
                mrTemp.setRoleName(DmShared.xNull(rsTemp.getString("User_Role")));
                mrTemp.setDescription(DmShared.xNull(rsTemp.getString("Description")));
                mrTemp.setLockTimeOut(DmShared.iNull(rsTemp.getString("UserLockTimeOut")).toString());
                mrTemp.setAdministratorYNCode(DmShared.xNull(rsTemp.getString("isAdmin")));
                mrTemp.setAdministratorYNName(DmShared.iNull(rsTemp.getString("isAdmin")).intValue() == 1 ? "Yes" : "No");
                mrTemp.setActiveYNCode(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setActiveYNName(DmShared.iNull(rsTemp.getString("isActive")).intValue() == 1 ? "Yes" : "No");
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("email")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setNatureName(DmShared.xNull(rsTemp.getString("natureName")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));

                sqlQuery = "Select  sg.name,sg.subcode as code From userLinkAccount ula "
                        + "         left join " + DmServer.getSsnDBName(this) + ".dbo.subGroup sg on sg.subCode = ula.subcode "
                        + "         Where ula.user_name = :linkedUser ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("linkedUser", mrTemp.getSharedKeyFieldName());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setLinkedAccountCode(DmShared.xNull(rsTemp.getString("code")));
                    mrTemp.setLinkedAccountName(DmShared.xNull(rsTemp.getString("name")));
                }
                rsTemp.close();
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
        DmSavePoint spBegin = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);
            DmServer.getManageUser().deleteUser(strKey, cnTemp);

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> saveRecord(UserMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
            }

            DmServer.getManageUser().saveUser(mrTemp, this, cnTemp);
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            dmrTemp.setStatus(true);
            spBegin.commit();
            cnTemp.close();
            if (blnAdd) {
                DmServer.getManageUser().sendMail(mrTemp.getSharedKeyFieldName(), this);
            }
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAdministrator(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRole(DmHelper helper, boolean blnToEndOfFile, boolean isResetCall, String userName) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        rstTemp.setData(lstTemp);

        try {
            if (!isResetCall) {
                condition = " Where isNull(User_Role,'') = '' And User_Name not in (:userName,'SA','SUPER') ";
            }

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("Select User_Name As Code, User_Name as Name, eMail, description From UserMast " + condition);
            if (!isResetCall) {
                sqlWithParam.setString("userName", DmShared.xNull(userName));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("eMail")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("description")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            String isAdministrator = DmServer.getCodeViaManualCode(DmServer.getSsnUserName(this), "user_Name", "UserMast", "isAdmin", cnTemp);

            if (DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER") || DmShared.iNull(isAdministrator).intValue() == 1) {
                condition = " User_Name Not In ('SA') ";
            } else {
                condition = " user_Name = :userName ";
            }
            sqlQuery = "Select  um.code, um.user_Name as name, um.eMail, isNull(um.User_Role,'') As User_Role, um.description, "
                    + "         case when um.isActive = 1 then 'Yes' else 'No' end as isActive, sdm.subCode as departmentCode, isNull(sdm.NAME,'') as departmentName "
                    + "         from UserMast as um "
                    + "         left join " + DmServer.getSsnDBName(this) + ".dbo.subgroup as sdm on sdm.subCode = um.departmentCode  "
                    + "         Where " + condition;
            sqlWithParam.setSQL(sqlQuery);

            if (!(DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER") || DmShared.iNull(isAdministrator).intValue() == 1)) {
                sqlWithParam.setString("userName", DmServer.getSsnUserName(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setRole(DmShared.xNull(rsTemp.getString("User_Role")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("description")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setEmail(DmShared.xNull(rsTemp.getString("eMail")));
                listNode.setIsActive(DmShared.xNull(rsTemp.getString("isActive")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResult<List<GrdLinkedUser_Mdl>> fillGridLinkedUser(String userName) {
        DmResult<List<GrdLinkedUser_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdLinkedUser_Mdl> data = new ArrayList<>();
        String sqlQuery = "";
        dmrTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "Select isNull(UserName_Linked,'') as UserName_Linked From UserMast Where User_Name = :User_Name  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("User_Name", DmShared.xNull(userName));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                userName = rsTemp.getString("UserName_Linked");
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(userName).equalsIgnoreCase("")) {
                userName = userName.replace("|", "','");
            } else {
                userName = "";
            }

            sqlQuery = "Select  User_Name As Code, Description As Name "
                    + "         From UserMast "
                    + "         Where User_Name IN ( " + DmServer.getParamList(":code", DmShared.xNull(userName)) + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("code", DmShared.xNull(userName));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdLinkedUser_Mdl grd = new GrdLinkedUser_Mdl();
                grd.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                grd.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Code")));
                grd.setDescription(DmShared.xNull(rsTemp.getString("Name")));
                data.add(grd);
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
    public DmResultPageing<List<GrdLinkedUser_Mdl>> getTxtHLinkedUser(DmHelper helper, String userName, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdLinkedUser_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdLinkedUser_Mdl> lstTemp = new ArrayList<>();
        GrdLinkedUser_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "SELECT  User_Name AS Code, Description AS Name "
                    + "         FROM UserMast "
                    + "         Where User_Name Not In (" + DmServer.getParamList(":User_Name", DmShared.xNull(userName)) + ") ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setStringList("User_Name", DmShared.xNull(userName));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdLinkedUser_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("Select code, name from userNature");
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
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResult<String> resetPassword(String manualCode) {
        DmResult<String> dmrTemp = new DmResult();

        try {
            DmServer.getManageUser().sendMail(manualCode, this);
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT sdm.subCode as code, sdm.NAME as Name "
                    + " FROM departmentMast dm "
                    + " left join subgroup sdm on dm.subCode = sdm.subcode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
