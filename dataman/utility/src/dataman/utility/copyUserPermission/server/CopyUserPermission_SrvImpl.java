package dataman.utility.copyUserPermission.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.utility.copyUserPermission.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CopyUserPermission_SrvImpl extends DmRSSPrivate implements CopyUserPermission_Srv {

    @Override
    public DmResult<String> saveRecord(List<GrdCopyPermissionFromTo_Mdl> mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = "", userRole = "";
        ResultSet rsTemp = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            for (GrdCopyPermissionFromTo_Mdl copyPermission_Mdl : mrTemp) {
                if ((DmShared.xNull(copyPermission_Mdl.getIsCheckedOrDelete()).equalsIgnoreCase("1")
                        || DmShared.xNull(copyPermission_Mdl.getIsCopy()).equalsIgnoreCase("1"))) {

                    sqlQuery = " delete FROM " + DmServer.dbCompany + ".dbo.user_voucher_permission WHERE user_name=:username and comp_Code = :comp_Code";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("username", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
                    psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = " delete FROM " + DmServer.dbCompany + ".dbo.user_permission_web WHERE userName=:username and comp_Code = :comp_Code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("username", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
                    psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = " delete FROM " + DmServer.dbCompany + ".dbo.user_Control_Permission_Web WHERE userName=:username and comp_Code = :comp_Code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("username", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
                    psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
                    psTemp.executeUpdate();
                    psTemp.close();

//                    sqlQuery = " delete FROM " + DmServer.dbCompany + ".dbo.user_Control_Permission WHERE userName=:username and comp_Code = :comp_Code ";
//                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                    psTemp.setString("username", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
//                    psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
//                    psTemp.executeUpdate();
//                    psTemp.close();
                    if ((DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()).equalsIgnoreCase("SA")
                            || DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()).equalsIgnoreCase("SUPER"))) {
                        if ((DmShared.xNull(copyPermission_Mdl.getIsCheckedOrDelete()).equalsIgnoreCase("0")
                                && DmShared.xNull(copyPermission_Mdl.getIsCopy()).equalsIgnoreCase("1"))) {
                            sqlQuery = " delete FROM " + DmServer.dbCompany + ".dbo.UserSite WHERE user_name=:username and compCode = :comp_Code ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("username", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
                            psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }

                    } else {
                        sqlQuery = " delete FROM " + DmServer.dbCompany + ".dbo.UserSite WHERE user_name=:username and compCode = :comp_Code ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("username", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
                        psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
                if ((DmShared.xNull(copyPermission_Mdl.getIsCheckedOrDelete()).equalsIgnoreCase("0")
                        && DmShared.xNull(copyPermission_Mdl.getIsCopy()).equalsIgnoreCase("1"))) {
                    if (!(DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()).equalsIgnoreCase("SA")
                            || DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()).equalsIgnoreCase("SUPER"))) {

                        sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.user_permission_web("
                                + "     userName, mnumodule, mnuName, mnuText, SNO, mnuLevel, parent, permission, reportfor, "
                                + "     comp_code, isActive, vp_Days, sp_days, dp_Days, allowValueUpto) "
                                + " SELECT :userName as userName, mnumodule, mnuName, mnuText, SNO, mnuLevel, parent, permission, reportfor,  "
                                + "     :comp_Code, isActive, vp_Days, sp_days, dp_Days, allowValueUpto"
                                + " FROM " + DmServer.dbCompany + ".dbo.user_permission_web WHERE  userName=:userName and comp_Code = :comp_Code_From ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("userName", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
                        psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
                        psTemp.setString("comp_Code_From", DmShared.xNull(copyPermission_Mdl.getFromCompanyCode()));
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.user_Control_Permission_Web(userName, mnuModule, mnuName, mnuText, groupText, groupName, comp_Code) "
                                + " SELECT :userName as userName,mnuModule,mnuName,mnuText,groupText,groupName,:comp_Code FROM "
                                + " " + DmServer.dbCompany + ".dbo.user_Control_Permission_Web WHERE  userName = :userName and comp_Code = :comp_Code_From ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("userName", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
                        psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
                        psTemp.setString("comp_Code_From", DmShared.xNull(copyPermission_Mdl.getFromCompanyCode()));
                        psTemp.executeUpdate();
                        psTemp.close();

//                        sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.user_Control_Permission (userName, mnuModule, mnuName, mnuText, groupText, groupName, comp_Code) "
//                                + " SELECT :userName as userName,mnuModule,mnuName,mnuText,groupText,groupName,:comp_Code FROM "
//                                + " " + DmServer.dbCompany + ".dbo.user_Control_Permission_Web WHERE  userName = :userName and comp_Code = :comp_Code_From ";
//                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                        psTemp.setString("userName", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
//                        psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
//                        psTemp.setString("comp_Code_From", DmShared.xNull(copyPermission_Mdl.getFromCompanyCode()));
//                        psTemp.executeUpdate();
//                        psTemp.close();
                    }

                    sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.user_voucher_permission(comp_code,user_name,vTypeSname,category,vp_Days,sp_days,dp_days,allowvalueUpto,isAllowAudit) "
                            + " SELECT :comp_Code,:username as user_name,vTypeSname,category,vp_Days,sp_days,dp_days,allowvalueUpto,isAllowAudit FROM "
                            + " " + DmServer.dbCompany + ".dbo.user_voucher_permission WHERE  user_name=:userName and comp_Code = :comp_Code_From ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("username", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
                    psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
                    psTemp.setString("comp_Code_From", DmShared.xNull(copyPermission_Mdl.getFromCompanyCode()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.UserSite(User_Name,CompCode,SiteList,CostCenterList) "
                            + " SELECT :username as user_name,:comp_Code,SiteList,CostCenterList FROM " + DmServer.dbCompany + ".dbo.UserSite WHERE user_name=:userName and compCode = :comp_Code_From ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("username", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
                    psTemp.setString("comp_Code", DmShared.xNull(copyPermission_Mdl.getToCompanyCode()));
                    psTemp.setString("comp_Code_From", DmShared.xNull(copyPermission_Mdl.getFromCompanyCode()));
                    psTemp.executeUpdate();
                    psTemp.close();

                }

                userRole = "";
                sqlQuery = " select user_Role from " + DmServer.dbCompany + ".dbo.userMast WHERE user_name=:userName  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("username", DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    userRole = DmShared.xNull(rsTemp.getString("user_Role"));
                }
                rsTemp.close();
                psTemp.close();

                DmServer.getManageUser().saveRole(DmShared.xNull(copyPermission_Mdl.getSharedKeyFieldName()), DmShared.xNull(userRole), cnTemp);
            }
            dmrTemp.setData("");
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
    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile, String compCode) {
        DmResultPageing<List<TxtHCompany_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCompany_Mdl> lstTemp = new ArrayList<>();
        TxtHCompany_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
//            sqlWithParam.setSQL(" SELECT plt.Comp_Name, cmp.Comp_Code,plt.sname,cmp.CYear, "
//                    + "  cmp.PLTCode,cmp.Start_Dt,cmp.End_Dt,cmp.V_Prefix,plt.latestcompCode  "
//                    + "  FROM Company as cmp  "
//                    + "  left join productLicensedTo as plt on cmp.comp_Code = plt.latestCompCode "
//                    + "  where cmp.Comp_Code not in (" + compCode + ")");

            sqlWithParam.setSQL("SELECT plt.Comp_Name, cmp.Comp_Code, plt.sname,cmp.CYear, cmp.PLTCode, cmp.Start_Dt, cmp.End_Dt, cmp.V_Prefix "
                    + "FROM Company as cmp "
                    + "left join productLicensedTo as plt on cmp.pltCode = plt.pltCode "
                    + "where cmp.Comp_Code not in ( " + DmShared.iNull(compCode).toString() + ") ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCompany_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Comp_Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Comp_Name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("sname")));
//                listNode.setLatestCompCode(DmShared.xNull(rsTemp.getString("latestcompCode")));
                listNode.setPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                listNode.setStartDate(DmShared.dNull(rsTemp.getDate("Start_Dt")));
                listNode.setEndDate(DmShared.dNull(rsTemp.getDate("End_Dt")));
                listNode.setFinanceYear(DmShared.xNull(rsTemp.getString("CYear")));
                listNode.setPltCode(DmShared.xNull(rsTemp.getString("PLTCode")));
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
    public DmResult<List<GrdCopyPermissionFromTo_Mdl>> fillGrid(String compCode) {
        DmResult<List<GrdCopyPermissionFromTo_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        GrdCopyPermissionFromTo_Mdl node;
        List<GrdCopyPermissionFromTo_Mdl> lstTemp = new ArrayList<>();

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, " Select UM.User_Name As UserName,Max(UM.Description) As Description,Max(UM.isAdmin) As Admin, "
                    + "   Max(ST) As ST,Max(CT) As CT,Max(UP) As UP,Max(VP) As VP,IsNull(Max(UMR.User_Name),'') As UR  "
                    + "  From UserMast UM  "
                    + "  Left Join  "
                    + "  (  "
                    + "  Select User_Name As UserName,1 As ST,(Case When IsNull(Max(CostCenterList),'') <> '' Then 1 Else 0 End) As CT,"
                    + "  0 As UP,0 As VP From UserSite Where CompCode = :compCode Group By User_Name  "
                    + "  Union All  "
                    + "  Select UserName,0 As ST,0 As CT,1 As UP,0 As VP From user_Permission_Web Where Comp_Code = :compCode Group By UserName  "
                    + "  Union All  "
                    + "  Select UserName,0 As ST,0 As CT,1 As UP,0 As VP From User_Control_Permission_Web Where Comp_Code = :compCode Group By UserName  "
                    //                    + "  Union All "
                    //                    + "  Select UserName,0 As ST,0 As CT,1 As UP,0 As VP From user_Control_Permission Where Comp_Code = :compCode Group By UserName  "
                    + "  Union All  "
                    + "  Select User_Name As UserName,0 As ST,0 As CT,0 As UP,1 As VP From User_Voucher_Permission Where Comp_Code = :compCode Group By User_Name  "
                    + "  ) As Tmp On UM.User_Name = Tmp.UserName  "
                    + "  Left Join UserMast UMR On IsNull(UM.User_Name,'') = IsNull(UMR.User_Role,'')  "
                    + "  Where IsNull(UM.User_Role,'') = '' and UM.User_Name not in ('SA','SUPER') " //And IsNull(Tmp.UserName,'') <> ''
                    + "  Group By UM.User_Name  "
                    + "  Order By UM.User_Name  ");
            psTemp.setString("compCode", DmShared.xNull(compCode));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdCopyPermissionFromTo_Mdl();
                node.setUserDescription(DmShared.xNull(rsTemp.getString("Description")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("UserName")));
                node.setIsAdmin(DmShared.xNull(rsTemp.getString("Admin")).equalsIgnoreCase("0") ? "No" : "Yes");
                node.setIsSite(DmShared.xNull(rsTemp.getString("ST")));
                node.setIsCostCenter(DmShared.xNull(rsTemp.getString("CT")));
                node.setIsUserPermission(DmShared.xNull(rsTemp.getString("UP")));
                node.setIsVoucherPermission(DmShared.xNull(rsTemp.getString("VP")));
                node.setIsCheckedOrDelete(DmShared.xNull("0"));
                node.setIsCopy(DmShared.xNull("0"));

                lstTemp.add(node);
            }
            dmrTemp.setData(lstTemp);
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = " SELECT plt.Comp_Name,cmp.Comp_Code,plt.sname,cmp.CYear, "
                    + "  cmp.PLTCode,cmp.Start_Dt,cmp.End_Dt,cmp.V_Prefix  "
                    + "  FROM Company as cmp  "
                    + "  left join productLicensedTo as plt on cmp.pltCode = plt.pltCode "
                    + "  where cmp.Comp_Code = :compCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("compCode", DmServer.getSsnCompanyCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setCompCode(DmShared.xNull(rsTemp.getString("Comp_Code")));
                defaultValue.setCompName(DmShared.xNull(rsTemp.getString("Comp_Name")));
                defaultValue.setShortName(DmShared.xNull(rsTemp.getString("sname")));
//                defaultValue.setLatestCompCode(DmShared.xNull(rsTemp.getString("latestcompCode")));
                defaultValue.setPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                defaultValue.setStartDate(DmShared.dNull(rsTemp.getDate("Start_Dt")));
                defaultValue.setEndDate(DmShared.dNull(rsTemp.getDate("End_Dt")));
                defaultValue.setFinanceYear(DmShared.xNull(rsTemp.getString("CYear")));
                defaultValue.setPltCode(DmShared.xNull(rsTemp.getString("PLTCode")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(defaultValue);
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

}
