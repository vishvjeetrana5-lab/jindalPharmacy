package dataman.utility.userPermission.server;

import dataman.utility.userPermission.shared.TxtHLinkedAccount_Mdl;
import dataman.utility.userPermission.shared.TxtHUsers_Mdl;
import dataman.utility.userPermission.shared.UserPermission_Mdl;
import dataman.utility.userPermission.shared.GrdCostCenter_Mdl;
import dataman.utility.userPermission.shared.GrdVoucher_Mdl;
import dataman.utility.userPermission.shared.GrdMenu_Mdl;
import dataman.utility.userPermission.shared.GrdSite_Mdl;
import dataman.utility.userPermission.shared.TxtHVoucher_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.utility.userPermission.shared.GrdMenu_Mdl.EventType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.utility.userPermission.shared.UserPermission_Srv;

public class UserPermission_SrvImpl extends DmRSSPrivate implements UserPermission_Srv {

    @Override
    public DmResult<UserPermission_Mdl> getUserRecord(String userName, String userCode) {
        DmResult<UserPermission_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        UserPermission_Mdl mrTemp = new UserPermission_Mdl();
        String sqlQuery, pltCode = "", siteList = "", costCenterList = "";
        dmrTemp.setData(mrTemp);

        try {
            pltCode = DmServer.getSsnPLTCode(this);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            /*===========================<< Get User detail >>=============================*/
            sqlQuery = "Select  um.code, um.user_Name, um.description, um.isAdmin, um.User_Role, um.email, ula.subCode, sg.name as subGroupName "
                    + "         from UserMast um "
                    + "         left join userLinkAccount ula on um.user_Name = ula.user_Name and ula.pltCode = :pltCode "
                    + "         left join " + DmServer.getSsnDBName(this).trim() + ".dbo.subGroup as sg on ula.subCode = sg.subCode "
                    + "         Where um.code = :code "
                    + "         order by um.User_Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", userCode);
            psTemp.setString("pltCode", DmShared.iNull(pltCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("user_Name")));
                mrTemp.setUserId(DmShared.xNull(rsTemp.getString("Code")));
                mrTemp.setRoleCode(DmShared.xNull(rsTemp.getString("User_Role")));
                mrTemp.setRoleName(DmShared.xNull(rsTemp.getString("User_Role")));
                mrTemp.setDescription(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("email")));
                mrTemp.setIsAdministrator(DmShared.iNull(rsTemp.getString("isAdmin")).intValue() == 1 ? "Yes" : "No");
                mrTemp.setLinkedAccountCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setLinkedAccountName(DmShared.xNull(rsTemp.getString("subGroupName")));
            }
            rsTemp.close();
            psTemp.close();

            /*===========================<< Get Voucher detail >>=============================*/
            mrTemp.getListVoucher().addAll(getVoucherDetails(userName, cnTemp));

            /*===========================<< Get Module Permission detail >>=============================*/
            mrTemp.getListMenuDetail().addAll(getModulePermission(userName, cnTemp));

            /*===========================<< Get Site detail >>=============================*/
            sqlQuery = "Select SiteList From UserSite Where User_Name = :User_Name And CompCode = :CompCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("User_Name", DmShared.xNull(userName));
            psTemp.setString("CompCode", DmServer.getSsnCompanyCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                siteList = DmShared.xNull(rsTemp.getString("SiteList")).trim();
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(siteList).equalsIgnoreCase("")) {
                siteList = siteList.replace("|", "','");
            } else {
                siteList = "''";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "Select Code,Name,ManualCode From SiteMast Where Code"
                    + " IN ( " + DmServer.getParamList(":code", DmShared.xNull(siteList)) + ") Order By Name  ");
            psTemp.setStringList("code", DmShared.xNull(siteList));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdSite_Mdl node = new GrdSite_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                node.setShortName(DmShared.xNull(rsTemp.getString("ManualCode")));
                mrTemp.getListSite().add(node);
            }
            rsTemp.close();
            psTemp.close();

            /*===========================<< Get Cost center detail >>=============================*/
            sqlQuery = "Select isNull(CostCenterList,'') As CostCenterList From UserSite Where User_Name = :User_Name And CompCode=:CompCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("User_Name", DmShared.xNull(userName));
            psTemp.setString("CompCode", DmServer.getSsnCompanyCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                costCenterList = rsTemp.getString("CostCenterList");
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(costCenterList).equalsIgnoreCase("")) {
                costCenterList = costCenterList.replace("|", "','");
            } else {
                costCenterList = "''";
            }

            cnTemp.setCatalog(DmServer.getSsnDBName(this));

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, " Select Code,Name,ManualCode  From CostCenterMast Where manualCode "
                    + " IN ( " + DmServer.getParamList(":code", DmShared.xNull(costCenterList)) + ") Order By Name  ");
            psTemp.setStringList("code", DmShared.xNull(costCenterList));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdCostCenter_Mdl node = new GrdCostCenter_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                node.setShortName(DmShared.xNull(rsTemp.getString("ManualCode")));
                mrTemp.getListCostCenter().add(node);
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
    public DmResult<String> deleteVoucherPermission(String userName, String vTypeSName, String roleName) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData(vTypeSName);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            /*====================================< Delete records from user_Voucher_Permission >====================================*/
            sqlQuery = "Delete from user_Voucher_Permission where comp_Code = :comp_Code and vTypeSName = :vTypeSName and user_name = :user_name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("comp_Code", DmServer.getSsnCompanyCode(this));
            psTemp.setString("user_Name", DmShared.xNull(userName).trim());
            psTemp.setString("vTypeSName", DmShared.xNull(vTypeSName).trim());
            psTemp.executeUpdate();
            psTemp.close();

            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            DmServer.getManageUser().saveRole(userName, roleName, cnTemp);

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
    public DmResult<String> saveVoucherPermission(GrdVoucher_Mdl mrTemp, String userName, String roleName) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, compCode;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);
            compCode = DmServer.getSsnCompanyCode(this);

            /*==============================< Check whether Voucher Type exist or not >=============================*/
            sqlQuery = "Select vTypeSName from user_Voucher_Permission where comp_Code = :comp_Code and vTypeSName = :vTypeSName and user_name = :user_Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("comp_Code", DmShared.iNull(compCode).toString());
            psTemp.setString("user_name", DmShared.xNull(userName).trim());
            psTemp.setString("vTypeSName", DmShared.xNull(mrTemp.getShortName()).trim());
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                blnAdd = true;
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() != 0) {
                if (blnAdd) {
                    /*=======================< Insert record into user_Voucher_Permission >========================*/
                    sqlQuery = "Insert Into user_Voucher_Permission(comp_Code, user_Name, vTypeSName, category, vp_Days, sp_Days, dp_Days, allowValueUpto, isAllowAudit) "
                            + " Values (:comp_Code, :user_Name, :vTypeSName, :category, :vp_Days, :sp_Days, :dp_Days, :allowValueUpto, :isAllowAudit) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("comp_Code", DmShared.iNull(compCode).toString());
                    psTemp.setString("user_name", DmShared.xNull(userName).trim());
                    psTemp.setString("vTypeSName", DmShared.xNull(mrTemp.getShortName()).trim());
                    psTemp.setString("category", DmShared.xNull(mrTemp.getCategory()).trim());
                    psTemp.setString("vp_Days", DmShared.iNull(mrTemp.getViewPrevious()).toString());
                    psTemp.setString("sp_Days", DmShared.iNull(mrTemp.getSavePrevious()).toString());
                    psTemp.setString("dp_Days", DmShared.iNull(mrTemp.getDeletePrevious()).toString());
                    psTemp.setString("allowValueUpto", DmShared.iNull(mrTemp.getAllowValueUpto()).toString());
                    psTemp.setString("isAllowAudit", DmShared.iNull(mrTemp.getAuditAllowCode()).toString());

                } else {
                    /*=======================< Update record of patientReceiptDetail >========================*/
                    sqlQuery = "Update user_Voucher_Permission set "
                            + " vp_Days = :vp_Days, "
                            + " sp_Days = :sp_Days, "
                            + " dp_Days = :dp_Days, "
                            + " allowValueUpto = :allowValueUpto, "
                            + " isAllowAudit = :isAllowAudit "
                            + " where comp_Code = :comp_Code and vTypeSName = :vTypeSName and user_name = :user_name ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("comp_Code", DmShared.iNull(compCode).toString());
                    psTemp.setString("user_name", DmShared.xNull(userName).trim());
                    psTemp.setString("vTypeSName", DmShared.xNull(mrTemp.getShortName()).trim());
                    psTemp.setString("category", DmShared.xNull(mrTemp.getCategory()).trim());
                    psTemp.setString("vp_Days", DmShared.iNull(mrTemp.getViewPrevious()).toString());
                    psTemp.setString("sp_Days", DmShared.iNull(mrTemp.getSavePrevious()).toString());
                    psTemp.setString("dp_Days", DmShared.iNull(mrTemp.getDeletePrevious()).toString());
                    psTemp.setString("allowValueUpto", DmShared.iNull(mrTemp.getAllowValueUpto()).toString());
                    psTemp.setString("isAllowAudit", DmShared.iNull(mrTemp.getAuditAllowCode()).toString());
                }
                psTemp.executeUpdate();
                psTemp.close();
            }

            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            DmServer.getManageUser().saveRole(userName, roleName, cnTemp);

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
    public DmResultPageing<List<GrdMenu_Mdl>> getMenuRecord(String seletedUserName, String mnuModule, String parent, String mnuLevel, boolean isRoot) {
        DmResultPageing<List<GrdMenu_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdMenu_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            if (isRoot) {
                lstTemp.addAll(getModulePermission(seletedUserName, cnTemp));
            } else {
                if (DmShared.iNull(mnuLevel).intValue() == 3) {
                    lstTemp.addAll(getControlPermission(seletedUserName, mnuModule, parent, cnTemp));
                } else {
                    lstTemp.addAll(getMenuPermission(seletedUserName, mnuModule, parent, mnuLevel, cnTemp));
                }
            }

            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHVoucher_Mdl>> getTxtHVoucher(DmHelper helper, String voucher, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucher_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucher_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucher_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select v_Type as code, description, category, short_Name from voucher_Type where 1 = 1 ";
            if (!DmShared.xNull(voucher).equalsIgnoreCase("")) {
                sqlQuery += " and V_Type not in ( " + DmServer.getParamList(":v_Type", DmShared.xNull(voucher)) + ") ";
                sqlWithParam.setSQL(sqlQuery);
                psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
                psTemp.setStringList("v_Type", voucher);
            } else {
                sqlQuery = sqlQuery;
                sqlWithParam.setSQL(sqlQuery);
                psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucher_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherDescription(DmShared.xNull(rsTemp.getString("description")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
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
    public DmResultPageing<List<TxtHUsers_Mdl>> getTxtHUser(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHUsers_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHUsers_Mdl> lstTemp = new ArrayList<>();
        TxtHUsers_Mdl listNode;
        String sqlQuery, condition = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            /*==============================< Get userName_Linked from userMast >=============================*/
            sqlQuery = "Select userName_Linked from userMast where user_Name = :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("userName_Linked")).trim().equalsIgnoreCase("")) {
                    condition = " and user_Name in (" + DmShared.xNull(rsTemp.getString("userName_Linked")).trim().replace("|", "'") + ") ";
                } else {
                    condition = " and user_Name not in ('SA', 'SUPER', '" + DmServer.getSsnUserName(this) + "') ";
                }
            } else {
                condition = " and 1 = 0 ";
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select code, user_Name as name, description from userMast where 1 = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHUsers_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("Description")));
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
    public DmResultPageing<List<GrdSite_Mdl>> getTxtHSite(DmHelper pageing, String siteList, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdSite_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdSite_Mdl> lstTemp = new ArrayList<>();
        GrdSite_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("Select code, name, manualCode from siteMast where pltCode = :pltCode and code not in (" + DmServer.getParamList(":siteList", DmShared.xNull(siteList)) + ") ");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setStringList("siteList", DmShared.xNull(siteList));
            psTemp.setString("pltCode", DmServer.getSsnPLTCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdSite_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<GrdCostCenter_Mdl>> getTxtHCostCenter(DmHelper helper, String ccList, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdCostCenter_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdCostCenter_Mdl> lstTemp = new ArrayList<>();
        GrdCostCenter_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select code, manualCode, name from costCenterMast where isNull(underCostCenter,'') = '' "
                    + " and manualCode not in (" + DmServer.getParamList(":siteList", DmShared.xNull(ccList)) + ") ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setStringList("siteList", DmShared.xNull(ccList));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdCostCenter_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<TxtHLinkedAccount_Mdl>> getTxtHLinkedAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHLinkedAccount_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHLinkedAccount_Mdl> lstTemp = new ArrayList<>();
        TxtHLinkedAccount_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select sg.subCode, sg.name as sgName, sg.manualCode, ag.groupName as gName, ct.cityName "
                    + "                 from subGroup as sg "
                    + "                 left join acGroup as ag on ag.groupCode = sg.groupCode "
                    + "                 left join city as ct on ct.cityCode = sg.cityCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHLinkedAccount_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("sgName")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setCodeValue(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setGroupName(DmShared.xNull(rsTemp.getString("gName")));
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
    public DmResult<String> saveSitePermission(String name, String selectedUser, boolean isSite, String roleName) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, compCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);
            compCode = DmServer.getSsnCompanyCode(this);

            sqlQuery = "Select user_name From UserSite where CompCode=:CompCode and user_name=:User_Name  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("CompCode", compCode);
            psTemp.setString("User_Name", DmShared.xNull(selectedUser));
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                blnAdd = true;
            }
            rsTemp.close();
            psTemp.close();

            if (isSite) {
                if (blnAdd) {
                    sqlQuery = " Insert Into UserSite(User_Name,CompCode,SiteList) Values"
                            + "( :User_Name,:CompCode,:SiteList) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("User_Name", selectedUser);
                    psTemp.setString("CompCode", compCode);
                    psTemp.setString("SiteList", DmShared.foriegnKey(name));
                    psTemp.executeUpdate();
                    psTemp.close();
                } else {
                    sqlQuery = "update UserSite set SiteList=:SiteList where User_Name=:User_Name and CompCode=:CompCode";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("User_Name", selectedUser);
                    psTemp.setString("CompCode", compCode);
                    psTemp.setString("SiteList", DmShared.foriegnKey(name));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            } else {
                if (blnAdd) {
                    sqlQuery = " Insert Into UserSite(User_Name,CompCode,CostCenterList) Values"
                            + "( :User_Name,:CompCode,:CostCenterList) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("User_Name", selectedUser);
                    psTemp.setString("CompCode", compCode);
                    psTemp.setString("CostCenterList", DmShared.foriegnKey(name));
                    psTemp.executeUpdate();
                    psTemp.close();
                } else {
                    sqlQuery = " update UserSite set CostCenterList=:CostCenterList where User_Name=:User_Name and CompCode=:CompCode";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("User_Name", selectedUser);
                    psTemp.setString("CompCode", compCode);
                    psTemp.setString("CostCenterList", DmShared.foriegnKey(name));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Delete userSite where user_Name = :User_Name and CompCode = :CompCode and isNull(siteList,'') = '' and isNull(costCenterList,'') = '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("User_Name", selectedUser);
            psTemp.setString("CompCode", compCode);
            psTemp.executeUpdate();
            psTemp.close();

            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            DmServer.getManageUser().saveRole(selectedUser, roleName, cnTemp);

            dmrTemp.setData(name);
            spBegin.commit();
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
    public DmResult<String> copyPermission(String fromUserName, String toUserName) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            /*=======================< Delete records from userSite >========================*/
            sqlQuery = "Delete from userSite where user_name = :user_name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("user_name", DmShared.xNull(toUserName));
            psTemp.executeUpdate();
            psTemp.close();

            /*=======================< Delete records from user_voucher_permission >========================*/
            sqlQuery = "Delete from user_voucher_permission where user_name = :user_name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("user_name", DmShared.xNull(toUserName));
            psTemp.executeUpdate();
            psTemp.close();

            /*=======================< Delete records from user_Control_Permission_Web  >========================*/
            sqlQuery = "Delete from user_Control_Permission_Web where userName = :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmShared.xNull(toUserName));
            psTemp.executeUpdate();
            psTemp.close();


            /*=======================< Delete records from user_permission_web >========================*/
            sqlQuery = "Delete from user_permission_web where userName = :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmShared.xNull(toUserName));
            psTemp.executeUpdate();
            psTemp.close();

            /*=======================< Insert records into userSite >========================*/
            sqlQuery = "Insert  into userSite (user_Name, compCode, siteList, costCenterList) "
                    + " Select  :toUserName as user_Name, compCode, siteList, costCenterList"
                    + "         from userSite where user_Name = :fromUserName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("toUserName", DmShared.xNull(toUserName));
            psTemp.setString("fromUserName", DmShared.xNull(fromUserName));
            psTemp.executeUpdate();
            psTemp.close();

            /*=======================< Insert records into user_Voucher_Permission >========================*/
            sqlQuery = "Insert  into user_Voucher_Permission (comp_code, user_Name, vTypeSname, category, vp_Days, sp_days, dp_days, allowvalueUpto, isAllowAudit) "
                    + " Select  comp_code, :toUserName as user_Name, vTypeSname, category, vp_Days, sp_days, dp_days, allowvalueUpto, isAllowAudit "
                    + "         from user_Voucher_Permission "
                    + "         where user_name = :fromUserName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("toUserName", DmShared.xNull(toUserName));
            psTemp.setString("fromUserName", DmShared.xNull(fromUserName));
            psTemp.executeUpdate();
            psTemp.close();

            /*=======================< Insert records into user_Control_Permission_Web >========================*/
            sqlQuery = "Insert  into user_Control_Permission_Web (userName, mnuModule, mnuName, mnuText, groupText, groupName, comp_Code) "
                    + " Select  :toUserName as userName, mnuModule, mnuName, mnuText, groupText, groupName, comp_Code "
                    + "         from user_Control_Permission_Web "
                    + "         where userName = :fromUserName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("toUserName", DmShared.xNull(toUserName));
            psTemp.setString("fromUserName", DmShared.xNull(fromUserName));
            psTemp.executeUpdate();
            psTemp.close();

            /*=======================< Insert records into user_Permission_Web >========================*/
            sqlQuery = "Insert  into user_Permission_Web(userName, mnumodule, mnuName, mnuText, sNO, mnuLevel, parent, permission, reportfor, "
                    + "         comp_code, isActive, vp_Days, sp_days, dp_Days, allowValueUpto) "
                    + " Select  :toUserName as userName, mnumodule, mnuName, mnuText, SNO, mnuLevel, parent, permission, reportfor, "
                    + "         comp_code, isActive, vp_Days, sp_days, dp_Days, allowValueUpto "
                    + "         from user_Permission_Web "
                    + "         where userName = :fromUserName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("toUserName", DmShared.xNull(toUserName));
            psTemp.setString("fromUserName", DmShared.xNull(fromUserName));
            psTemp.executeUpdate();
            psTemp.close();

            /*=======================< Update userName_Linked of userMast >========================*/
            sqlQuery = "Update	umt set "
                    + "		umt.userName_Linked = umf.userName_Linked "
                    + "		from userMast as umf "
                    + "		left join userMast as umt on umt.user_Name = :fromUserName "
                    + "		where umf.user_Name = :toUserName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("toUserName", DmShared.xNull(toUserName));
            psTemp.setString("fromUserName", DmShared.xNull(fromUserName));
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(toUserName);
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
    public DmResult<String> saveLinkedAccount(String selectedUser, String linkedAccountCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            /*=======================< Insert record into userLinkAccount (If not exist) >========================*/
            sqlQuery = "Insert into userLinkAccount (user_name, pltCode) "
                    + " Select	um.user_Name, :pltCode as pltCode "
                    + "         from userMast as um "
                    + "         left join userLinkAccount as ula on um.user_Name = ula.user_Name and ula.pltCode = :pltCode "
                    + "         where isNull(ula.user_Name,'') = '' and um.user_Name = :user_Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("user_Name", DmShared.xNull(selectedUser));
            psTemp.setString("pltCode", DmServer.getSsnPLTCode(this));
            psTemp.executeUpdate();
            psTemp.close();

            /*=======================< Update subCode into userLinkAccount >========================*/
            sqlQuery = "Update userLinkAccount set subCode = :subCode where pltCode = :pltCode and user_Name = :user_Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("user_Name", DmShared.xNull(selectedUser));
            psTemp.setString("pltCode", DmServer.getSsnPLTCode(this));
            psTemp.setString("subCode", DmShared.foriegnKey(linkedAccountCode));
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(selectedUser);
            spBegin.commit();
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

    private List<GrdVoucher_Mdl> getVoucherDetails(String userName, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdVoucher_Mdl> lstNode = new ArrayList<GrdVoucher_Mdl>();
        GrdVoucher_Mdl node;
        String sqlQuery = "";

        try {
            /*===========================<< Get Voucher Type detail >>=============================*/
            sqlQuery = "Select	vt.v_type, vt.description, uvp.category, uvp.vTypeSName, uvp.vp_Days, uvp.sp_days, uvp.dp_Days, "
                    + "         uvp.allowValueUpto, uvp.isAllowAudit "
                    + "		from user_Voucher_Permission as uvp "
                    + "		left join " + DmServer.getSsnDBName(this).trim() + ".dbo.voucher_Type as vt on uvp.vTypeSName = vt.short_name "
                    + "		Where uvp.user_Name = :user_Name and uvp.comp_Code = :comp_Code "
                    + "		order by uvp.category, vt.short_Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("user_Name", DmShared.xNull(userName).trim());
            psTemp.setString("comp_Code", DmServer.getSsnCompanyCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdVoucher_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_type")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                node.setShortName(DmShared.xNull(rsTemp.getString("vTypeSName")));
                node.setCategory(DmShared.xNull(rsTemp.getString("category")));
                node.setShortName(DmShared.xNull(rsTemp.getString("vTypeSName")));
                node.setAllowValueUpto(DmShared.xNull(rsTemp.getString("allowValueUpto")));
                node.setDeletePrevious(DmShared.xNull(rsTemp.getString("dp_Days")));
                node.setSavePrevious(DmShared.xNull(rsTemp.getString("sp_days")));
                node.setViewPrevious(DmShared.xNull(rsTemp.getString("vp_Days")));
                node.setAuditAllowCode(DmShared.iNull(rsTemp.getString("isAllowAudit")).toString());
                node.setAuditAllowName(DmShared.iNull(rsTemp.getString("isAllowAudit")).intValue() == 1 ? "Yes" : "No");
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

    private List<GrdMenu_Mdl> getModulePermission(String userName, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdMenu_Mdl> lstNode = new ArrayList<GrdMenu_Mdl>();
        GrdMenu_Mdl node;
        String sqlQuery = "";

        try {
            /*===========================<< Get Module Permission >>=============================*/
            sqlQuery = "Select	upw.mnuModule, upw.sNo, "
                    + "		upwB.mnuCount as buMnuCount, upwB.isAdd as buIsAdd, upwB.isEdit as buIsEdit, upwB.isDelete as buIsDelete, "
                    + "		upwB.isPrint as buIsPrint, upwB.isEditable as buIsEditable, "
                    + "		upwC.mnuCount as cuMnuCount, upwC.isAdd as cuIsAdd, upwC.isEdit as cuIsEdit, upwC.isDelete as cuIsDelete, "
                    + "		upwC.isPrint as cuIsPrint, upwC.isEditable as cuIsEditable, "
                    + "         upwC.vp_Days, upwC.sp_days, upwC.dp_Days, upwC.allowValueUpto "
                    + "		from ( "
                    + "			Select	upw.mnuModule, max(upw.sNo) as sNo "
                    + "				from user_permission_web as upw "
                    + "				Where upw.userName = :baseUserName and comp_Code = :baseCompanyCode "
                    + "				group by upw.mnuModule "
                    + "		) as upw "
                    + "		left join ( "
                    + "			select  upw.mnuModule, count(upw.mnuModule) as mnuCount, sum(ucpw.isEditable) as isEditable, "
                    + "                         min(case when substring(isNull(upw.permission,'****'),1,1)  = '*' then 0 else 1 end) as isAdd, "
                    + "				min(case when substring(isNull(upw.permission,'****'),2,1)  = '*' then 0 else 1 end) as isEdit, "
                    + "				min(case when substring(isNull(upw.permission,'****'),3,1)  = '*' then 0 else 1 end) as isDelete, "
                    + "				min(case when substring(isNull(upw.permission,'****'),4,1)  = '*' then 0 else 1 end) as isPrint "
                    + "				from user_Permission_Web upw "
                    + "				left join ( "
                    + "                                 select  ucpw.mnuModule, ucpw.mnuName, count(ucpw.mnuModule) as isEditable "
                    + "                                         from user_Control_Permission_Web ucpw "
                    + "						where ucpw.userName = :baseUserName and ucpw.comp_Code = :baseCompanyCode "
                    + "						group by ucpw.mnuModule, ucpw.mnuName "
                    + "				) as ucpw on upw.mnuModule = ucpw.mnuModule and upw.mnuName = ucpw.mnuName "
                    + "				where upw.userName = :baseUserName and upw.comp_Code = :baseCompanyCode and upw.mnuLevel = 2 "
                    + "				group by upw.mnuModule "
                    + "		) as upwB on upw.mnuModule = upwB.mnuModule "
                    + "		left join ( "
                    + "			select  upw2.mnuModule, count(upw2.mnuModule) as mnuCount, sum(ucpw.isEditable) as isEditable, "
                    + "                         max(upw2.vp_Days) as vp_Days, max(upw2.sp_days) as sp_days, max(upw2.dp_Days) as dp_Days, max(upw2.allowValueUpto) as allowValueUpto, "
                    + "                         sum(case when substring(isNull(upw2.permission,'****'),1,1)  = '*' then 0 else 1 end) as isAdd, "
                    + "				sum(case when substring(isNull(upw2.permission,'****'),2,1)  = '*' then 0 else 1 end) as isEdit, "
                    + "				sum(case when substring(isNull(upw2.permission,'****'),3,1)  = '*' then 0 else 1 end) as isDelete, "
                    + "				sum(case when substring(isNull(upw2.permission,'****'),4,1)  = '*' then 0 else 1 end) as isPrint "
                    + "				from user_Permission_Web upw2 "
                    + "				left join ( "
                    + "                                 select  ucpw.mnuModule, ucpw.mnuName, count(ucpw.mnuModule) as isEditable "
                    + "                                         from user_Control_Permission_Web ucpw "
                    + "						where ucpw.userName = :currentUserName and ucpw.comp_Code = :currentCompanyCode "
                    + "						group by ucpw.mnuModule, ucpw.mnuName "
                    + "				) as ucpw on upw2.mnuModule = ucpw.mnuModule and upw2.mnuName = ucpw.mnuName "
                    + "				where upw2.userName = :currentUserName and upw2.comp_Code = :currentCompanyCode and upw2.mnuLevel = 2 "
                    + "				group by upw2.mnuModule "
                    + "		) as upwC on upw.mnuModule = upwC.mnuModule  "
                    + "		order by upw.sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("currentUserName", DmShared.xNull(userName).trim());
            psTemp.setString("currentCompanyCode", DmServer.getSsnCompanyCode(this));
            psTemp.setString("baseUserName", DmServer.getSsnUserName(this));
            if (DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER")) {
                psTemp.setString("baseCompanyCode", "-1");
            } else {
                psTemp.setString("baseCompanyCode", DmServer.getSsnCompanyCode(this));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMenu_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("mnuModule")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mnuModule")));
                node.setMnuModule(DmShared.xNull(rsTemp.getString("mnuModule")));
                node.setSerialNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                node.setBuMnuCount(DmShared.iNull(rsTemp.getString("buMnuCount")).toString());
                node.setBuIsAdd(DmShared.iNull(rsTemp.getString("buIsAdd")).toString());
                node.setBuIsEdit(DmShared.iNull(rsTemp.getString("buIsEdit")).toString());
                node.setBuIsDelete(DmShared.iNull(rsTemp.getString("buIsDelete")).toString());
                node.setBuIsPrint(DmShared.iNull(rsTemp.getString("buIsPrint")).toString());
                node.setBuIsEnable(DmShared.iNull(rsTemp.getString("buIsEditable")).toString());
                node.setCuMnuCount(DmShared.iNull(rsTemp.getString("cuMnuCount")).toString());
                node.setCuIsAdd(DmShared.iNull(rsTemp.getString("cuIsAdd")).toString());
                node.setCuIsEdit(DmShared.iNull(rsTemp.getString("cuIsEdit")).toString());
                node.setCuIsDelete(DmShared.iNull(rsTemp.getString("cuIsDelete")).toString());
                node.setCuIsPrint(DmShared.iNull(rsTemp.getString("cuIsPrint")).toString());
                node.setCuIsEnable(DmShared.iNull(rsTemp.getString("cuIsEditable")).toString());
                node.setDp_Days(DmShared.xNull(rsTemp.getString("dp_Days")));
                node.setSp_days(DmShared.xNull(rsTemp.getString("sp_days")));
                node.setVp_Days(DmShared.xNull(rsTemp.getString("vp_Days")));
                node.setAllowValueUpto(DmShared.xNull(rsTemp.getString("allowValueUpto")));
//                node.setReportFor(DmShared.xNull(rsTemp.getString("reportFor")));
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

    private List<GrdMenu_Mdl> getMenuPermission(String userName, String mnuModule, String parent, String mnuLevel, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdMenu_Mdl> lstNode = new ArrayList<GrdMenu_Mdl>();
        GrdMenu_Mdl node;
        String sqlQuery, groupByCol;

        try {
            if (DmShared.iNull(mnuLevel).intValue() == 2) {
                groupByCol = "upw2.mnuName";
            } else if (DmShared.iNull(mnuLevel).intValue() == 1) {
                groupByCol = "upw1.mnuName";
            } else {
                groupByCol = "upw0.mnuName";
                parent = "";
            }

            /*===========================<< Get Menu Permission (Level 1) >>=============================*/
            sqlQuery = "Select	upw.mnuModule, upw.mnuName, upw.mnuText, upw.parent, isNull(upw.mnuLevel,0) + 1 as mnuLevel, upw.sNo, "
                    + "		upwB.mnuCount as buMnuCount, upwB.isAdd as buIsAdd, upwB.isEdit as buIsEdit, upwB.isDelete as buIsDelete, "
                    + "		upwB.isPrint as buIsPrint, upwB.isEditable as buIsEditable, "
                    + "		upwC.mnuCount as cuMnuCount, upwC.isAdd as cuIsAdd, upwC.isEdit as cuIsEdit, upwC.isDelete as cuIsDelete, "
                    + "		upwC.isPrint as cuIsPrint, upwC.isEditable as cuIsEditable, "
                    + "         upwC.vp_Days, upwC.sp_days, upwC.dp_Days, upwC.allowValueUpto "
                    + "		from ( "
                    + "			select	upw.mnuModule, upw.mnuName, upw.mnuText, upw.parent, upw.mnuLevel, upw.sNo "
                    + "                         from user_Permission_Web upw "
                    + "				where upw.userName = :baseUserName and upw.mnuModule = :mnuModule and upw.comp_Code = :baseCompanyCode "
                    + "                         and upw.parent = :parent and upw.mnuLevel = :mnuLevel "
                    + "		) as upw "
                    + "		left join ( "
                    + "			select  upw2.mnuModule, " + groupByCol + " as mnuName, "
                    + "				count(upw2.mnuModule) as mnuCount,  sum(ucpw.isEditable) as isEditable, "
                    + "				min(case when substring(isNull(upw2.permission,'****'),1,1)  = '*' then 0 else 1 end) as isAdd, "
                    + "				min(case when substring(isNull(upw2.permission,'****'),2,1)  = '*' then 0 else 1 end) as isEdit, "
                    + "				min(case when substring(isNull(upw2.permission,'****'),3,1)  = '*' then 0 else 1 end) as isDelete, "
                    + "				min(case when substring(isNull(upw2.permission,'****'),4,1)  = '*' then 0 else 1 end) as isPrint "
                    + "				from user_Permission_Web upw2 "
                    + "				left join user_Permission_Web upw1 on upw1.userName = upw2.userName and upw1.comp_Code = upw2.comp_Code and upw2.mnuModule = upw1.mnuModule and upw2.parent = upw1.mnuName  "
                    + "				left join user_Permission_Web upw0 on upw0.userName = upw2.userName and upw0.comp_Code = upw2.comp_Code and upw2.mnuModule = upw0.mnuModule and upw1.parent = upw0.mnuName "
                    + "				left join ( "
                    + "                                 select  ucpw.mnuModule, ucpw.mnuName, count(ucpw.mnuModule) as isEditable "
                    + "                                         from user_Control_Permission_Web ucpw "
                    + "						where ucpw.userName = :baseUserName and ucpw.comp_Code = :baseCompanyCode "
                    + "						group by mnuModule, ucpw.mnuName "
                    + "				) as ucpw on upw2.mnuModule = ucpw.mnuModule and upw2.mnuName = ucpw.mnuName "
                    + "				where upw2.userName = :baseUserName and upw2.mnuModule = :mnuModule and upw2.comp_Code = :baseCompanyCode and upw2.mnuLevel = 2 "
                    + "				group by upw2.mnuModule, " + groupByCol + " "
                    + "		) as upwB on upw.mnuModule = upwB.mnuModule and upw.mnuName = upwB.mnuName "
                    + "		left join ( "
                    + "			select  upw2.mnuModule, " + groupByCol + " as mnuName, count(upw2.mnuModule) as mnuCount, sum(ucpw.isEditable) as isEditable, "
                    + "                         max(upw2.vp_Days) as vp_Days, max(upw2.sp_days) as sp_days, max(upw2.dp_Days) as dp_Days, max(upw2.allowValueUpto) as allowValueUpto, "
                    + "				sum(case when substring(isNull(upw2.permission,'****'),1,1)  = '*' then 0 else 1 end) as isAdd, "
                    + "				sum(case when substring(isNull(upw2.permission,'****'),2,1)  = '*' then 0 else 1 end) as isEdit, "
                    + "				sum(case when substring(isNull(upw2.permission,'****'),3,1)  = '*' then 0 else 1 end) as isDelete, "
                    + "				sum(case when substring(isNull(upw2.permission,'****'),4,1)  = '*' then 0 else 1 end) as isPrint "
                    + "				from user_Permission_Web as upw2 "
                    + "				left join user_Permission_Web upw1 on upw1.userName = upw2.userName and upw1.comp_Code = upw2.comp_Code and upw2.mnuModule = upw1.mnuModule and upw2.parent = upw1.mnuName  "
                    + "				left join user_Permission_Web upw0 on upw0.userName = upw2.userName and upw0.comp_Code = upw2.comp_Code and upw2.mnuModule = upw0.mnuModule and upw1.parent = upw0.mnuName "
                    + "				left join ( "
                    + "                                 select  ucpw.mnuModule, ucpw.mnuName, count(ucpw.mnuModule) as isEditable "
                    + "                                         from user_Control_Permission_Web ucpw "
                    + "						where ucpw.userName = :currentUserName and ucpw.comp_Code = :currentCompanyCode "
                    + "						group by mnuModule, ucpw.mnuName "
                    + "				) as ucpw on upw2.mnuModule = ucpw.mnuModule and upw2.mnuName = ucpw.mnuName "
                    + "				where upw2.userName = :currentUserName and upw2.comp_Code = :currentCompanyCode and upw2.mnuModule = :mnuModule and upw2.mnuLevel = 2 "
                    + "				group by upw2.mnuModule, " + groupByCol + " "
                    + "		) as upwC on upw.mnuModule = upwC.mnuModule and upw.mnuName = upwC.mnuName "
                    + "		order by upw.sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("currentUserName", DmShared.xNull(userName).trim());
            psTemp.setString("currentCompanyCode", DmServer.getSsnCompanyCode(this));
            psTemp.setString("baseUserName", DmServer.getSsnUserName(this));
            if (DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER")) {
                psTemp.setString("baseCompanyCode", "-1");
            } else {
                psTemp.setString("baseCompanyCode", DmServer.getSsnCompanyCode(this));
            }
            psTemp.setString("mnuLevel", DmShared.iNull(mnuLevel).toString());
            psTemp.setString("mnuModule", DmShared.xNull(mnuModule).trim());
            psTemp.setString("parent", DmShared.xNull(parent).trim());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMenu_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("mnuName")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mnuText")));
                node.setMnuModule(DmShared.xNull(rsTemp.getString("mnuModule")));
                node.setMnuName(DmShared.xNull(rsTemp.getString("mnuName")));
                node.setMnuText(DmShared.xNull(rsTemp.getString("mnuText")));
                node.setMnuLevel(DmShared.xNull(rsTemp.getString("mnuLevel")));
                node.setParent(DmShared.xNull(rsTemp.getString("parent")));
                node.setSerialNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                node.setBuMnuCount(DmShared.iNull(rsTemp.getString("buMnuCount")).toString());
                node.setBuIsAdd(DmShared.iNull(rsTemp.getString("buIsAdd")).toString());
                node.setBuIsEdit(DmShared.iNull(rsTemp.getString("buIsEdit")).toString());
                node.setBuIsDelete(DmShared.iNull(rsTemp.getString("buIsDelete")).toString());
                node.setBuIsPrint(DmShared.iNull(rsTemp.getString("buIsPrint")).toString());
                node.setBuIsEnable(DmShared.iNull(rsTemp.getString("buIsEditable")).toString());
                node.setCuMnuCount(DmShared.iNull(rsTemp.getString("cuMnuCount")).toString());
                node.setCuIsAdd(DmShared.iNull(rsTemp.getString("cuIsAdd")).toString());
                node.setCuIsEdit(DmShared.iNull(rsTemp.getString("cuIsEdit")).toString());
                node.setCuIsDelete(DmShared.iNull(rsTemp.getString("cuIsDelete")).toString());
                node.setCuIsPrint(DmShared.iNull(rsTemp.getString("cuIsPrint")).toString());
                node.setCuIsEnable(DmShared.iNull(rsTemp.getString("cuIsEditable")).toString());
                node.setDp_Days(DmShared.xNull(rsTemp.getString("dp_Days")));
                node.setSp_days(DmShared.xNull(rsTemp.getString("sp_days")));
                node.setVp_Days(DmShared.xNull(rsTemp.getString("vp_Days")));
                node.setAllowValueUpto(DmShared.xNull(rsTemp.getString("allowValueUpto")));
//                node.setReportFor(DmShared.xNull(rsTemp.getString("reportFor")));
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

    private List<GrdMenu_Mdl> getControlPermission(String userName, String mnuModule, String mnuName, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdMenu_Mdl> lstNode = new ArrayList<GrdMenu_Mdl>();
        GrdMenu_Mdl node;
        String sqlQuery = "";

        try {
            /*===========================<< Get Menu Control Permission >>=============================*/
            sqlQuery = "Select  ucpwB.mnuModule, ucpwB.mnuName, ucpwB.mnuText, ucpwB.groupName, ucpwB.groupText, 4 as mnuLevel, "
                    + "         1 as buMnuCount, 1 as buIsEditable, (case when isNull(ucpw.groupName,'') = '' then 0 else 1 end) as cuIsEditable "
                    + "		from user_Control_Permission_Web ucpwB "
                    + "		left join user_Control_Permission_Web ucpw on ucpwB.mnuModule = ucpw.mnuModule and ucpwB.mnuName = ucpw.mnuName and ucpwB.groupName = ucpw.groupName "
                    + "		and ucpw.userName = :currentUserName and ucpw.comp_Code = :currentCompanyCode "
                    + "		where ucpwB.userName = :baseUserName and ucpwB.comp_Code = :baseCompanyCode "
                    + "         and ucpwB.mnuModule = :mnuModule and ucpwB.mnuName = :mnuName "
                    + "         order by ucpwB.groupName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("currentUserName", DmShared.xNull(userName).trim());
            psTemp.setString("currentCompanyCode", DmServer.getSsnCompanyCode(this));
            psTemp.setString("baseUserName", DmServer.getSsnUserName(this));
            if (DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER")) {
                psTemp.setString("baseCompanyCode", "-1");
            } else {
                psTemp.setString("baseCompanyCode", DmServer.getSsnCompanyCode(this));
            }
            psTemp.setString("mnuModule", DmShared.xNull(mnuModule).trim());
            psTemp.setString("mnuName", DmShared.xNull(mnuName).trim());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMenu_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("groupName")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("groupText")));
                node.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setGroupText(DmShared.xNull(rsTemp.getString("groupText")));
                node.setMnuModule(DmShared.xNull(rsTemp.getString("mnuModule")));
                node.setMnuName(DmShared.xNull(rsTemp.getString("mnuName")));
                node.setMnuText(DmShared.xNull(rsTemp.getString("mnuText")));
                node.setMnuLevel(DmShared.xNull(rsTemp.getString("mnuLevel")));
                node.setBuMnuCount(DmShared.iNull(rsTemp.getString("buMnuCount")).toString());
                node.setBuIsEnable(DmShared.iNull(rsTemp.getString("buIsEditable")).toString());
                node.setCuIsEnable(DmShared.iNull(rsTemp.getString("cuIsEditable")).toString());
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

    @Override
    public DmResult<String> saveMenuPermission(EventType eventType, GrdMenu_Mdl mrTemp, String userName, String roleName) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        boolean isAdd = false;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            /*==============================< Manage Permission >=============================*/
            if (null != eventType) {
                if (eventType == EventType.ENABLE) {
                    manageControlPermission((DmShared.iNull(mrTemp.getCuIsEnable()).intValue() == 0), userName, DmShared.xNull(mrTemp.getMnuModule()),
                            DmShared.xNull(mrTemp.getMnuLevel()), DmShared.xNull(mrTemp.getMnuName()), DmShared.xNull(mrTemp.getGroupName()), cnTemp);

                } else {
                    switch (eventType) {
                        case VIEW:
                            isAdd = DmShared.iNull(mrTemp.getCuMnuCount()).intValue() != DmShared.iNull(mrTemp.getBuMnuCount()).intValue();
                            break;
                        case ADD:
                            isAdd = DmShared.iNull(mrTemp.getCuIsAdd()).intValue() == 0;
                            break;
                        case EDIT:
                            isAdd = DmShared.iNull(mrTemp.getCuIsEdit()).intValue() == 0;
                            break;
                        case DELETE:
                            isAdd = DmShared.iNull(mrTemp.getCuIsDelete()).intValue() == 0;
                            break;
                        case PRINT:
                            isAdd = DmShared.iNull(mrTemp.getCuIsPrint()).intValue() == 0;
                            break;
                        case VIEW_DAYS:
                        case SAVE_DAYS:
                        case DELETE_DAYS:
                        case ALLOW_UPTO:
                            break;
                        default:
                            break;
                    }
                    manageMenuPermission(eventType, isAdd, userName, mrTemp, cnTemp);
                }
            }

            /*==============================< Manage Role >=============================*/
            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            DmServer.getManageUser().saveRole(userName, roleName, cnTemp);

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private void manageMenuPermission(EventType eventType, boolean isAdd, String userName, GrdMenu_Mdl node, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", currentCompanyCode;

        try {
            currentCompanyCode = DmServer.getSsnCompanyCode(this);

            if (DmShared.iNull(node.getMnuLevel()).intValue() == 1) {
                condition += " and upw0.mnuName = :mnuName ";
            } else if (DmShared.iNull(node.getMnuLevel()).intValue() == 2) {
                condition += " and upw1.mnuName = :mnuName ";
            } else if (DmShared.iNull(node.getMnuLevel()).intValue() == 3) {
                condition += " and upw2.mnuName = :mnuName ";
            }

            if (eventType == EventType.VIEW) {
                /*=======================< Delete records from user_Control_Permission_Web >========================*/
                manageControlPermission(false, userName, node.getMnuModule(), node.getMnuLevel(), node.getMnuName(), "", cnTemp);

                /*==============================< Get Permission details >=============================*/
                sqlQuery = "Select  upw2.mnuModule, upw2.reportFor, upw2.isActive, '****' as permission, "
                        + "         upw2.mnuName as mnuName2, upw2.mnuText as mnuText2, upw2.parent as parent2, upw2.sNo as sNo2, upw2.mnuLevel as mnuLevel2, "
                        + "         upw1.mnuName as mnuName1, upw1.mnuText as mnuText1, upw1.parent as parent1, upw1.sNo as sNo1, upw1.mnuLevel as mnuLevel1, "
                        + "         upw0.mnuName as mnuName0, upw0.mnuText as mnuText0, upw0.parent as parent0, upw0.sNo as sNo0, upw0.mnuLevel as mnuLevel0 "
                        + "         from user_Permission_Web as upw2 "
                        + "         left join user_Permission_Web upw1 on upw1.userName = upw2.userName and upw1.comp_Code = upw2.comp_Code and upw2.mnuModule = upw1.mnuModule and upw2.parent = upw1.mnuName  "
                        + "         left join user_Permission_Web upw0 on upw0.userName = upw2.userName and upw0.comp_Code = upw2.comp_Code and upw2.mnuModule = upw0.mnuModule and upw1.parent = upw0.mnuName "
                        + "         where upw2.userName = :baseUserName and upw2.comp_Code = :baseCompanyCode and upw2.mnuModule = :mnuModule and upw2.mnuLevel = 2 " + condition;
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("baseUserName", DmServer.getSsnUserName(this));
                if (DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER")) {
                    psTemp.setString("baseCompanyCode", "-1");
                } else {
                    psTemp.setString("baseCompanyCode", DmServer.getSsnCompanyCode(this));
                }
                psTemp.setString("mnuModule", DmShared.xNull(node.getMnuModule()).trim());
                if (DmShared.iNull(node.getMnuLevel()).intValue() == 1 || DmShared.iNull(node.getMnuLevel()).intValue() == 2 || DmShared.iNull(node.getMnuLevel()).intValue() == 3) {
                    psTemp.setString("mnuName", DmShared.xNull(node.getMnuName()).trim());
                }
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    GrdMenu_Mdl mrTemp = new GrdMenu_Mdl();
                    mrTemp.setMnuModule(DmShared.xNull(rsTemp.getString("mnuModule")).trim());
                    mrTemp.setReportFor(DmShared.xNull(rsTemp.getString("reportFor")).trim());
                    mrTemp.setPermission(DmShared.xNull(rsTemp.getString("permission")).trim());

                    mrTemp.setMnuName(DmShared.xNull(rsTemp.getString("mnuName0")).trim());
                    mrTemp.setMnuText(DmShared.xNull(rsTemp.getString("mnuText0")).trim());
                    mrTemp.setParent(DmShared.xNull(rsTemp.getString("parent0")).trim());
                    mrTemp.setSerialNo(DmShared.xNull(rsTemp.getString("sNo0")).trim());
                    mrTemp.setMnuLevel(DmShared.xNull(rsTemp.getString("mnuLevel0")).trim());

                    /*==============================< Save data for level 0 >=============================*/
                    if (DmShared.iNull(node.getMnuLevel()).intValue() <= 1 || isAdd) {
                        saveMenuPermission(isAdd, userName, currentCompanyCode, mrTemp, cnTemp);
                    }

                    mrTemp.setMnuName(DmShared.xNull(rsTemp.getString("mnuName1")).trim());
                    mrTemp.setMnuText(DmShared.xNull(rsTemp.getString("mnuText1")).trim());
                    mrTemp.setParent(DmShared.xNull(rsTemp.getString("parent1")).trim());
                    mrTemp.setSerialNo(DmShared.xNull(rsTemp.getString("sNo1")).trim());
                    mrTemp.setMnuLevel(DmShared.xNull(rsTemp.getString("mnuLevel1")).trim());

                    /*==============================< Save data for level 1 >=============================*/
                    if (DmShared.iNull(node.getMnuLevel()).intValue() <= 2 || isAdd) {
                        saveMenuPermission(isAdd, userName, currentCompanyCode, mrTemp, cnTemp);
                    }

                    mrTemp.setMnuName(DmShared.xNull(rsTemp.getString("mnuName2")).trim());
                    mrTemp.setMnuText(DmShared.xNull(rsTemp.getString("mnuText2")).trim());
                    mrTemp.setParent(DmShared.xNull(rsTemp.getString("parent2")).trim());
                    mrTemp.setSerialNo(DmShared.xNull(rsTemp.getString("sNo2")).trim());
                    mrTemp.setMnuLevel(DmShared.xNull(rsTemp.getString("mnuLevel2")).trim());

                    /*==============================< Save data for level 2 >=============================*/
                    saveMenuPermission(isAdd, userName, currentCompanyCode, mrTemp, cnTemp);
                }
                rsTemp.close();
                psTemp.close();

                /*=======================< Delete record from user_permission_web (Level - 1) >========================*/
                sqlQuery = "Delete  upw1 "
                        + "         from user_Permission_Web as upw1 "
                        + "         left join user_Permission_Web as upw2 on upw1.userName = upw2.userName and upw1.comp_Code = upw2.comp_Code and upw1.mnuModule = upw2.mnuModule and upw1.mnuName = upw2.parent "
                        + "         where upw1.userName = :userName and upw1.comp_Code = :comp_Code and upw1.mnuModule = :mnuModule and upw1.mnuLevel = 1 "
                        + "         and upw2.mnuModule is null ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("comp_Code", DmShared.iNull(currentCompanyCode).toString());
                psTemp.setString("userName", DmShared.xNull(userName).trim());
                psTemp.setString("mnuModule", DmShared.xNull(node.getMnuModule()).trim());
                psTemp.executeUpdate();
                psTemp.close();

                /*=======================< Delete record from user_permission_web (Level - 0) >========================*/
                sqlQuery = "Delete  upw0 "
                        + "         from user_Permission_Web as upw0 "
                        + "         left join user_Permission_Web as upw2 on upw0.userName = upw2.userName and upw0.comp_Code = upw2.comp_Code and upw0.mnuModule = upw2.mnuModule and upw0.mnuName = upw2.parent "
                        + "         where upw0.userName = :userName and upw0.comp_Code = :comp_Code and upw0.mnuModule = :mnuModule and upw0.mnuLevel = 0 "
                        + "         and upw2.mnuModule is null ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("comp_Code", DmShared.iNull(currentCompanyCode).toString());
                psTemp.setString("userName", DmShared.xNull(userName).trim());
                psTemp.setString("mnuModule", DmShared.xNull(node.getMnuModule()).trim());
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                /*=======================< Update record of user_permission_web >========================*/
                String updateColumns = "";
                if (eventType == EventType.ADD) {
                    if (isAdd) {
                        updateColumns = "'A'";
                    } else {
                        updateColumns = "'*'";
                    }
                    updateColumns = " upw2.permission = " + updateColumns + " + upw2.isEdit + upw2.isDelete + upw2.isPrint ";
                } else if (eventType == EventType.DELETE) {
                    if (isAdd) {
                        updateColumns = "'D'";
                    } else {
                        updateColumns = "'*'";
                    }
                    updateColumns = " upw2.permission = upw2.isAdd + upw2.isEdit + " + updateColumns + " + upw2.isPrint ";
                } else if (eventType == EventType.EDIT) {
                    if (isAdd) {
                        updateColumns = "'E'";
                    } else {
                        updateColumns = "'*'";
                    }
                    updateColumns = " upw2.permission = upw2.isAdd + " + updateColumns + " + upw2.isDelete + upw2.isPrint ";
                } else if (eventType == EventType.PRINT) {
                    if (isAdd) {
                        updateColumns = "'P'";
                    } else {
                        updateColumns = "'*'";
                    }
                    updateColumns = " upw2.permission = upw2.isAdd + upw2.isEdit + upw2.isDelete + " + updateColumns + " ";
                } else if (eventType == EventType.ALLOW_UPTO || eventType == EventType.DELETE_DAYS || eventType == EventType.SAVE_DAYS || eventType == EventType.VIEW_DAYS) {
                    updateColumns = " upw2.vp_Days = :vp_Days, upw2.sp_days = :sp_days, upw2.dp_Days = :dp_Days, upw2.allowValueUpto = :allowValueUpto ";
                }

                sqlQuery = "Update  upw2 set " + updateColumns
                        + "         from ( "
                        + "             select  upw2.mnuModule, upw0.mnuName as mnuName, upw2.permission, upw2.vp_Days, upw2.sp_days, upw2.dp_Days, upw2.allowValueUpto, "
                        + "                     case when substring(isNull(upw2.permission,'****'),1,1)  = 'A' then 'A' else '*' end as isAdd, "
                        + "			case when substring(isNull(upw2.permission,'****'),2,1)  = 'E' then 'E' else '*' end as isEdit, "
                        + "			case when substring(isNull(upw2.permission,'****'),3,1)  = 'D' then 'D' else '*' end as isDelete, "
                        + "			case when substring(isNull(upw2.permission,'****'),4,1)  = 'P' then 'P' else '*' end as isPrint "
                        + "			from user_Permission_Web as upw2 "
                        + "			left join user_Permission_Web upw1 on upw1.userName = :currentUserName and upw1.comp_Code = :currentCompanyCode and upw2.mnuModule = upw1.mnuModule and upw2.parent = upw1.mnuName  "
                        + "			left join user_Permission_Web upw0 on upw0.userName = :currentUserName and upw0.comp_Code = :currentCompanyCode and upw2.mnuModule = upw0.mnuModule and upw1.parent = upw0.mnuName "
                        + "			left join user_Permission_Web upwB on upwB.userName = :baseUserName and upwB.comp_Code = :baseCompanyCode and upw2.mnuModule = upwB.mnuModule and upwB.mnuLevel = upw2.mnuLevel and upw2.mnuName = upwB.mnuName "
                        + "			where upw2.userName = :currentUserName and upw2.mnuModule = :mnuModule and upw2.mnuLevel = 2 "
                        + "                     and upw2.comp_Code = :currentCompanyCode and upwB.mnuName is not null " + condition
                        + "         ) as upw2 ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("currentUserName", DmShared.xNull(userName).trim());
                psTemp.setString("currentCompanyCode", DmServer.getSsnCompanyCode(this));
                psTemp.setString("baseUserName", DmServer.getSsnUserName(this));
                if (DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER")) {
                    psTemp.setString("baseCompanyCode", "-1");
                } else {
                    psTemp.setString("baseCompanyCode", DmServer.getSsnCompanyCode(this));
                }
                psTemp.setString("mnuModule", DmShared.xNull(node.getMnuModule()).trim());
                if (DmShared.iNull(node.getMnuLevel()).intValue() == 1 || DmShared.iNull(node.getMnuLevel()).intValue() == 2 || DmShared.iNull(node.getMnuLevel()).intValue() == 3) {
                    psTemp.setString("mnuName", DmShared.xNull(node.getMnuName()).trim());
                }
                if (eventType == EventType.ALLOW_UPTO || eventType == EventType.DELETE_DAYS || eventType == EventType.SAVE_DAYS || eventType == EventType.VIEW_DAYS) {
                    psTemp.setString("vp_Days", DmShared.iNull(node.getVp_Days()).toString());
                    psTemp.setString("sp_days", DmShared.iNull(node.getSp_days()).toString());
                    psTemp.setString("dp_Days", DmShared.iNull(node.getDp_Days()).toString());
                    psTemp.setString("allowValueUpto", DmServer.vNull(node.getAllowValueUpto(), "0.00"));
                }
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
    }

    private void saveMenuPermission(boolean isAdd, String userName, String comp_Code, GrdMenu_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*=======================< Delete record from user_permission_web >========================*/
            sqlQuery = "Delete  from user_permission_web Where comp_Code = :comp_Code and userName = :userName and mnuModule = :mnuModule and mnuName = :mnuName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("comp_Code", DmShared.iNull(comp_Code).toString());
            psTemp.setString("userName", DmShared.xNull(userName).trim());
            psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getMnuModule()).trim());
            psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMnuName()).trim());
            psTemp.executeUpdate();
            psTemp.close();

            if (isAdd) {
                /*=======================< Insert record into user_permission_web >========================*/
                sqlQuery = "Insert  into user_permission_web (comp_Code, userName, mnuModule, mnuName, mnuText, sNo, mnuLevel, "
                        + "         parent, permission, reportFor, isActive) "
                        + " Values  (:comp_Code, :userName, :mnuModule, :mnuName, :mnuText, :sNo, :mnuLevel, "
                        + "         :parent, :permission, :reportFor, :isActive) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("comp_Code", DmShared.iNull(comp_Code).toString());
                psTemp.setString("userName", DmShared.xNull(userName).trim());
                psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getMnuModule()).trim());
                psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMnuName()).trim());
                psTemp.setString("mnuText", DmShared.xNull(mrTemp.getMnuText()).trim());
                psTemp.setString("sNo", DmShared.iNull(mrTemp.getSerialNo()).toString());
                psTemp.setString("mnuLevel", DmShared.iNull(mrTemp.getMnuLevel()).toString());
                psTemp.setString("parent", DmShared.xNull(mrTemp.getParent()).trim());
                psTemp.setString("permission", "****");
                psTemp.setString("reportFor", DmShared.xNull(mrTemp.getReportFor()).trim());
                psTemp.setString("isActive", "1");
                psTemp.executeUpdate();
                psTemp.close();
            }
        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
    }

    private void manageControlPermission(boolean isAdd, String userName, String mnuModule, String mnuLevel, String mnuName, String groupName, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";

        try {
            if (DmShared.iNull(mnuLevel).intValue() == 1) {
                condition += " and upw0.mnuName = :mnuName ";
            } else if (DmShared.iNull(mnuLevel).intValue() == 2) {
                condition += " and upw1.mnuName = :mnuName ";
            } else if (DmShared.iNull(mnuLevel).intValue() == 3) {
                condition += " and upw2.mnuName = :mnuName ";
            } else if (DmShared.iNull(mnuLevel).intValue() == 4) {
                condition += " and ucpw.mnuName = :mnuName and ucpw.groupName = :groupName ";
            }

            /*=======================< Delete records from user_Control_Permission_Web >========================*/
            sqlQuery = "Delete  ucpw "
                    + "         from user_Permission_Web as upw2 "
                    + "         left join user_Permission_Web upw1 on upw1.userName = upw2.userName and upw1.comp_Code = upw2.comp_Code and upw2.mnuModule = upw1.mnuModule and upw2.parent = upw1.mnuName "
                    + "         left join user_Permission_Web upw0 on upw0.userName = upw2.userName and upw0.comp_Code = upw2.comp_Code and upw2.mnuModule = upw0.mnuModule and upw1.parent = upw0.mnuName "
                    + "         left join user_Control_Permission_Web as ucpw on ucpw.comp_Code = :currentCompanyCode and ucpw.userName = :currentUserName and upw2.mnuModule = ucpw.mnuModule and upw2.mnuName = ucpw.mnuName "
                    + "         where upw2.userName = :baseUserName and upw2.comp_Code = :baseCompanyCode and upw2.mnuModule = :mnuModule and upw2.mnuLevel = 2 "
                    + "         and ucpw.mnuModule is not null " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("currentUserName", DmShared.xNull(userName).trim());
            psTemp.setString("currentCompanyCode", DmServer.getSsnCompanyCode(this));
            psTemp.setString("baseUserName", DmServer.getSsnUserName(this));
            if (DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER")) {
                psTemp.setString("baseCompanyCode", "-1");
            } else {
                psTemp.setString("baseCompanyCode", DmServer.getSsnCompanyCode(this));
            }
            psTemp.setString("mnuModule", DmShared.xNull(mnuModule).trim());
            if (DmShared.iNull(mnuLevel).intValue() == 1 || DmShared.iNull(mnuLevel).intValue() == 2 || DmShared.iNull(mnuLevel).intValue() == 3) {
                psTemp.setString("mnuName", DmShared.xNull(mnuName).trim());
            } else if (DmShared.iNull(mnuLevel).intValue() == 4) {
                psTemp.setString("mnuName", DmShared.xNull(mnuName).trim());
                psTemp.setString("groupName", DmShared.xNull(groupName).trim());
            }
            psTemp.executeUpdate();
            psTemp.close();

            if (isAdd) {
                /*=======================< Insert record into user_Control_Permission_Web >========================*/
                sqlQuery = "Insert  into user_Control_Permission_Web (userName, mnuModule, mnuName, mnuText, groupText, groupName, comp_Code) "
                        + " Select  :currentUserName as userName, ucpw.mnuModule, ucpw.mnuName, ucpw.mnuText, ucpw.groupText, ucpw.groupName, :currentCompanyCode as comp_Code "
                        + "         from user_Permission_Web as upw2 "
                        + "         left join user_Permission_Web upw1 on upw1.userName = upw2.userName and upw1.comp_Code = upw2.comp_Code and upw2.mnuModule = upw1.mnuModule and upw2.parent = upw1.mnuName "
                        + "         left join user_Permission_Web upw0 on upw0.userName = upw2.userName and upw0.comp_Code = upw2.comp_Code and upw2.mnuModule = upw0.mnuModule and upw1.parent = upw0.mnuName "
                        + "         left join user_Control_Permission_Web as ucpw on upw2.comp_Code = ucpw.comp_Code and upw2.userName = ucpw.userName and upw2.mnuModule = ucpw.mnuModule and upw2.mnuName = ucpw.mnuName "
                        + "         where upw2.userName = :baseUserName and upw2.comp_Code = :baseCompanyCode and upw2.mnuModule = :mnuModule and upw2.mnuLevel = 2 "
                        + "         and ucpw.mnuModule is not null " + condition;
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("currentUserName", DmShared.xNull(userName).trim());
                psTemp.setString("currentCompanyCode", DmServer.getSsnCompanyCode(this));
                psTemp.setString("baseUserName", DmServer.getSsnUserName(this));
                if (DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER")) {
                    psTemp.setString("baseCompanyCode", "-1");
                } else {
                    psTemp.setString("baseCompanyCode", DmServer.getSsnCompanyCode(this));
                }
                psTemp.setString("mnuModule", DmShared.xNull(mnuModule).trim());
                if (DmShared.iNull(mnuLevel).intValue() == 1 || DmShared.iNull(mnuLevel).intValue() == 2 || DmShared.iNull(mnuLevel).intValue() == 3) {
                    psTemp.setString("mnuName", DmShared.xNull(mnuName).trim());
                } else if (DmShared.iNull(mnuLevel).intValue() == 4) {
                    psTemp.setString("mnuName", DmShared.xNull(mnuName).trim());
                    psTemp.setString("groupName", DmShared.xNull(groupName).trim());
                }
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
    }
}
