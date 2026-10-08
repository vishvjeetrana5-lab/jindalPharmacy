package aarogya.ribbon.server;

import aarogya.dashBoard.self.shared.DashBoard_Model;
import dataman.libraryMenu.self.shared.lmAccounts;
import aarogya.master.self.shared.Master_Model;
import aarogya.inventory.self.shared.Inventory_Model;
import aarogya.mis.self.shared.MIS_Model;
import aarogya.nabh.self.shared.NABH_Model;
import aarogya.pharmacy.self.server.PharmacyDataStructure;
import aarogya.pharmacy.self.shared.Pharmacy_Model;
import aarogya.projectMenu.self.shared.*;
import aarogya.self.server.AarogyaDataStructure;
import aarogya.ribbon.shared.Ribbon_Srv;
import aarogya.self.server.AarogyaCompanyDataStructure;
import aarogya.ribbon.shared.UserInfo_Mdl;
import dataman.accounts.self.server.AccountsDataStructure;
import dataman.accounts.self.shared.Accounts_Model;
import dataman.company.self.server.CompanyDataStructure;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmRefreshDataStructure.server.DmRefreshDataStructure;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.libraryMenu.self.shared.lmUtility;
import dataman.libraryMenu.self.shared.lmWebJobs;
import dataman.utility.self.server.UtilityDataStructure;
import dataman.utility.self.shared.Utility_Model;
import dataman.webJobs.self.server.WebJobsDataStructure;
import dataman.webJobs.self.shared.WebJobs_Model;
import dmBuild.DmBuild;
import java.sql.Connection;
import java.sql.ResultSet;

public class Ribbon_SrvImpl extends DmRSSPrivate implements Ribbon_Srv {

    @Override
    public DmResult<String> rdsTransaction(String companyCode, boolean isExhaustiveRepair) {
        DmResult<String> dmrTemp = new DmResult();
        DmRefreshDataStructure refreshDataStructure = null;
        AarogyaDataStructure aarogyaDataStructure;
        AccountsDataStructure accountsDataStructure;
        UtilityDataStructure utilityDataStructure;
        WebJobsDataStructure webJobsDataStructure;
        PharmacyDataStructure pharmacyDataStructure;
        Connection cnTemp = null;
        String databaseName;

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            databaseName = DmServer.executeScalar("select max(centralData_Path) as centralData_Path from company where comp_code = '" + companyCode + "'", cnTemp);
            cnTemp.close();

            refreshDataStructure = new DmRefreshDataStructure(databaseName, isExhaustiveRepair, new DmRefreshDataStructure.CallBack() {
                @Override
                public Connection getConnection(String databaseName) {
                    return DmServer.getConnection(databaseName);
                }
            });

            accountsDataStructure = new AccountsDataStructure(refreshDataStructure);
            utilityDataStructure = new UtilityDataStructure(refreshDataStructure);
            webJobsDataStructure = new WebJobsDataStructure(refreshDataStructure);
            pharmacyDataStructure = new PharmacyDataStructure(refreshDataStructure);
            aarogyaDataStructure = new AarogyaDataStructure(refreshDataStructure);

            refreshDataStructure.executeDBScript();

            utilityDataStructure.initilizer();
            accountsDataStructure.initilizer();
            pharmacyDataStructure.initilizer();
//            webJobsDataStructure.initilizer();
            aarogyaDataStructure.initilizer();

            DmFileManagerMongo.synchronizeTransactionalWithFileServer();
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception ex) {
            }

            if (refreshDataStructure != null) {
                refreshDataStructure.close();
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> rdsCompany(boolean isExhaustiveRepair) {
        DmResult<String> dmrTemp = new DmResult();
        DmRefreshDataStructure refreshDataStructure = null;
        CompanyDataStructure companyDataStructure;
        AarogyaCompanyDataStructure aarogyaCompanyDataStructure;
        String databaseName, dbTemp;

        try {
            databaseName = DmServer.dbCompany; //"J2EE_Company";
            refreshDataStructure = new DmRefreshDataStructure(databaseName, isExhaustiveRepair, new DmRefreshDataStructure.CallBack() {
                @Override
                public Connection getConnection(String databaseName) {
                    return DmServer.getConnection(databaseName);
                }
            });
            companyDataStructure = new CompanyDataStructure(refreshDataStructure);
            aarogyaCompanyDataStructure = new AarogyaCompanyDataStructure(refreshDataStructure);

            refreshDataStructure.executeDBScript();
            dbTemp = DmServer.dbCompany.toLowerCase().replace("_", "").replace("company", "").trim();
            companyDataStructure.initilizer(dbTemp + "_Transaction", dbTemp);

            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
        } finally {

            if (refreshDataStructure != null) {
                refreshDataStructure.close();
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> repairMenu() {
        DmResult<String> dmrTemp = new DmResult();
        String sqlQry = "";
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            DmServer.deleteMenu();
            DmServer.repairMenu(new pmMaster().moduleName, new Master_Model().getMenu());
            DmServer.repairMenu(new pmPharmacy().moduleName, new Pharmacy_Model().getMenu());
            DmServer.repairMenu(new pmInventory().moduleName, new Inventory_Model().getMenu());
            DmServer.repairMenu(new lmAccounts().moduleName, new Accounts_Model().getMenu());
            DmServer.repairMenu(new pmMIS().moduleName, new MIS_Model().getMenu());
            DmServer.repairMenu(new pmNABH().moduleName, new NABH_Model().getMenu());
            DmServer.repairMenu(new lmWebJobs().moduleName, new WebJobs_Model().getMenu());
            DmServer.repairMenu(new lmUtility().moduleName, new Utility_Model().getMenu());
            DmServer.repairMenu(new pmDashBoard().moduleName, new DashBoard_Model().getMenu());

            //================================== User Permission ===============================
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQry = "select	upw2.mnuModule, upw2.mnuName, upw2.mnuLevel "
                    + "		from user_Permission_Web upw2 "
                    + "		left join user_Permission_Web upw on upw.userName = 'SA' and upw2.mnuModule = upw.mnuModule and upw2.mnuName = upw.parent "
                    + "		where upw2.userName = 'SA' and upw.mnuName is null and isNull(upw2.mnuLevel,0) <> 2 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQry);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "In corect Menu Level. Module ("
                        + DmShared.xNull(rsTemp.getString("mnuModule")) + ")  Menu ("
                        + DmShared.xNull(rsTemp.getString("mnuName")) + ") Menu Level ("
                        + DmShared.iNull(rsTemp.getString("mnuLevel")).toString() + ")");
            }
            rsTemp.close();
            psTemp.close();

            sqlQry = "DELETE    upwAll "
                    + "		FROM user_permission_web upwAll "
                    + "		LEFT JOIN ( "
                    + "			SELECT 	upw.mnuModule,upw.mnuName,upw.mnuText,upw.sNo,upw.mnuLevel,upw.parent,upw.reportfor,upw.isActive "
                    + "				FROM user_permission_web upw "
                    + "				WHERE upw.UserName = 'SA' "
                    + "		) AS upwSA ON upwAll.mnuModule = upwSA.mnuModule AND upwAll.mnuName = upwSA.mnuName "
                    + "        WHERE upwAll.userName NOT IN ('SA','SUPER') AND upwSA.mnuModule IS NULL; ";
            DmServer.executeSQL(sqlQry, cnTemp);

            sqlQry = "delete	upwAll "
                    + "         FROM user_Favorite_Web upwAll "
                    + "         LEFT JOIN ( "
                    + "             SELECT  upw.mnuModule,upw.mnuName,upw.mnuText,upw.sNo,upw.mnuLevel,upw.parent,upw.reportfor,upw.isActive  "
                    + "                     FROM user_permission_web upw "
                    + "                     WHERE upw.UserName = 'SA' "
                    + "         ) AS upwSA ON upwAll.mnuModule = upwSA.mnuModule AND upwAll.mnuName = upwSA.mnuName "
                    + "         WHERE upwSA.mnuModule IS NULL; ";
            DmServer.executeSQL(sqlQry, cnTemp);

            sqlQry = "delete	mc "
                    + "         FROM menuCodification mc "
                    + "         LEFT JOIN ( "
                    + "             SELECT  upw.mnuModule,upw.mnuName,upw.mnuText,upw.sNo,upw.mnuLevel,upw.parent,upw.reportfor,upw.isActive   "
                    + "                     FROM user_permission_web upw  "
                    + "                     WHERE upw.UserName = 'SA'  "
                    + "         ) AS upwSA ON mc.mnuModule = upwSA.mnuModule AND mc.mnuName = upwSA.mnuName  "
                    + "         WHERE upwSA.mnuModule IS NULL ";
            DmServer.executeSQL(sqlQry, cnTemp);

            sqlQry = "UPDATE  upwAll SET "
                    + "upwAll.mnuText = upwSA.mnuText,      "
                    + "upwAll.sNo = upwSA.sNo,         "
                    + "upwAll.mnuLevel = upwSA.mnuLevel,        "
                    + "upwAll.parent = upwSA.parent,        "
                    + "upwAll.reportFor = upwSA.reportFor,        "
                    + "upwAll.isActive = upwSA.isActive "
                    + "from user_permission_web upwAll "
                    + "     LEFT JOIN (        "
                    + "         SELECT 	upw.mnuModule,upw.mnuName,upw.mnuText,upw.sNo,upw.mnuLevel,upw.parent,upw.reportfor,upw.isActive 		 "
                    + "                 FROM user_permission_web upw		 "
                    + "                 WHERE upw.UserName = 'SA'         "
                    + "     ) AS upwSA ON upwAll.mnuModule = upwSA.mnuModule AND upwAll.mnuName = upwSA.mnuName        "
                    + "     WHERE upwAll.userName NOT IN ('SA','SUPER'); ";
            DmServer.executeSQL(sqlQry, cnTemp);

            try {
                //Insert missing level-1 menus for all users except SA & SUPER
                sqlQry = "insert into user_Permission_Web (comp_Code, userName, mnuModule, mnuName, mnuText, sNo, mnuLevel, parent, permission, "
                        + "reportFor, vp_Days, sp_days, dp_Days, allowValueUpto, isActive) "
                        + "select	up.comp_Code, up.userName, up.mnuModule, upsa.mnuName,  max(upsa.mnuText) as mnuText,  max(upsa.sNo) as sNo,   "
                        + "		max(upsa.mnuLevel) as mnuLevel,  max(upsa.parent) as parent,  max(upsa.permission) as permission,   "
                        + "		max(upsa.reportFor) as reportFor,  max(upsa.vp_Days) as vp_Days,  max(upsa.sp_days) as sp_days,   "
                        + "		max(upsa.dp_Days) as dp_Days,  max(upsa.allowValueUpto) as allowValueUpto,  max(upsa.isActive) as isActive "
                        + "		from user_Permission_Web up "
                        + "		left join ( "
                        + "			select	upw.mnuName as mainMnuName, upw1.* "
                        + "					from user_Permission_Web upw "
                        + "					left join user_Permission_Web upw_np on upw_np.userName = 'SA' and upw_np.mnuModule = upw.mnuModule and upw_np.parent = upw.mnuName "
                        + "					left join user_Permission_Web upw1 on upw1.userName = 'SA' and upw1.mnuModule = upw.mnuModule and upw1.mnuName = upw.parent "
                        + "					where upw.userName in ('SA') and upw_np.mnuModule is Null "
                        + "		) as upsa on upsa.mnuModule = up.mnuModule and upsa.mainMnuName = up.mnuName "
                        + "		left join user_Permission_Web up1 on up1.comp_Code = up.comp_Code and up1.userName = up.userName and up1.mnuModule = up.mnuModule and up1.mnuName = upsa.mnuName   "
                        + "		where up.userName not in ('SA', 'SUPER') and upsa.mnuModule is not null  "
                        + "		and up1.mnuName is null "
                        + "		group by up.comp_Code, up.userName, up.mnuModule, upsa.mnuName  "
                        + "		order by up.userName, up.comp_Code";
                DmServer.executeSQL(sqlQry, cnTemp);

                //Insert missing level-0 menus for all users except SA & SUPER
                sqlQry = "insert into user_Permission_Web (comp_Code, userName, mnuModule, mnuName, mnuText, sNo, mnuLevel, parent, permission, "
                        + "reportFor, vp_Days, sp_days, dp_Days, allowValueUpto, isActive) "
                        + "select	up.comp_Code, up.userName, up.mnuModule, upsa.mnuName,  max(upsa.mnuText) as mnuText,  max(upsa.sNo) as sNo,   "
                        + "		max(upsa.mnuLevel) as mnuLevel,  max(upsa.parent) as parent,  max(upsa.permission) as permission,   "
                        + "		max(upsa.reportFor) as reportFor,  max(upsa.vp_Days) as vp_Days,  max(upsa.sp_days) as sp_days,   "
                        + "		max(upsa.dp_Days) as dp_Days,  max(upsa.allowValueUpto) as allowValueUpto,  max(upsa.isActive) as isActive "
                        + "		from user_Permission_Web up "
                        + "		left join ( "
                        + "			select	upw.mnuName as mainMnuName, upw0.* "
                        + "					from user_Permission_Web upw "
                        + "					left join user_Permission_Web upw_np on upw_np.userName = 'SA' and upw_np.mnuModule = upw.mnuModule and upw_np.parent = upw.mnuName "
                        + "					left join user_Permission_Web upw1 on upw1.userName = 'SA' and upw1.mnuModule = upw.mnuModule and upw1.mnuName = upw.parent "
                        + "					left join user_Permission_Web upw0 on upw0.userName = 'SA' and upw0.mnuModule = upw.mnuModule and upw0.mnuName = upw1.parent "
                        + "					where upw.userName in ('SA') and upw_np.mnuModule is Null "
                        + "		) as upsa on upsa.mnuModule = up.mnuModule and upsa.mainMnuName = up.mnuName "
                        + "		left join user_Permission_Web up1 on up1.comp_Code = up.comp_Code and up1.userName = up.userName and up1.mnuModule = up.mnuModule and up1.mnuName = upsa.mnuName   "
                        + "		where up.userName not in ('SA', 'SUPER') and upsa.mnuModule is not null  "
                        + "		and up1.mnuName is null "
                        + "		group by up.comp_Code, up.userName, up.mnuModule, upsa.mnuName ";
                DmServer.executeSQL(sqlQry, cnTemp);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //========================== Control Permission ====================================
            sqlQry = "DELETE  ucpwAll "
                    + "     FROM user_control_permission_web ucpwAll "
                    + "     LEFT JOIN ( "
                    + "		SELECT 	ucpw.mnuModule,ucpw.mnuName,ucpw.groupName "
                    + "                 FROM user_control_permission_web ucpw "
                    + "                 WHERE ucpw.UserName = 'SA' "
                    + "     ) AS ucpwSA ON ucpwAll.mnuModule = ucpwSA.mnuModule AND "
                    + "                    ucpwAll.mnuName = ucpwSA.mnuName and "
                    + "                    ucpwAll.groupName = ucpwSA.groupName "
                    + "     WHERE ucpwAll.userName NOT IN ('SA','SUPER') AND ucpwSA.mnuModule IS NULL; ";
            DmServer.executeSQL(sqlQry, cnTemp);

            sqlQry = "UPDATE  upwAll SET  "
                    + "upwAll.mnuText = upwSA.mnuText,  "
                    + "upwAll.grouptext = upwSA.grouptext  "
                    + "from user_control_permission_web upwAll "
                    + "     LEFT JOIN ( "
                    + "         SELECT  upw.mnuModule,upw.mnuName,upw.mnuText,upw.grouptext,upw.groupName  "
                    + "                 FROM user_control_permission_web upw  "
                    + "                 WHERE upw.UserName = 'SA'  "
                    + "     ) AS upwSA ON upwAll.mnuModule = upwSA.mnuModule AND  "
                    + "             upwAll.mnuName = upwSA.mnuName and  "
                    + "             upwAll.groupName = upwSA.groupName "
                    + "     WHERE upwAll.userName NOT IN ('SA','SUPER');  ";
            DmServer.executeSQL(sqlQry, cnTemp);

            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
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
            } catch (Exception ee) {

            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getMenuInfo(String menuCode) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        dmrTemp.setData(keyField);
        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
//            menuCode = DmShared.removeSpace(menuCode);
            menuCode = (DmShared.removeSpace(menuCode).replaceAll(" ", ""));
            menuCode = (DmShared.properCase(menuCode).replaceAll(" ", ""));
            sqlQuery = "select	max(mc.mnuModule) as mnuModule ,max(mc.mnuName) as mnuName,max(mc.mnuShortHand) as  mnuShortHand, "
                    + "		max(upw.mnuText) as mnuText  		"
                    + "		from  menuCodification  as mc 		"
                    + "		left join user_Permission_Web as upw on upw.mnuModule = mc.mnuModule AND upw.mnuName=mc.mnuName "
                    + "         where mnuShortHand=:mnuShortHand "
                    + "         group by mc.mnuModule ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuShortHand", menuCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                keyField.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("mnuModule")));
                keyField.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mnuText")));
            }
            dmrTemp.setData(keyField);
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
    public DmResult<UserInfo_Mdl> getUserInfo(String userName) {
        DmResult<UserInfo_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        UserInfo_Mdl userInfo_Mdl = new UserInfo_Mdl();
        dmrTemp.setData(userInfo_Mdl);
        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "select  um.user_Name, um.description, um.eMail "
                    + "from userMast um "
                    + "where um.user_Name = :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmShared.xNull(userName));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                userInfo_Mdl.setUserName(DmShared.xNull(rsTemp.getString("user_Name")));
                userInfo_Mdl.setUserDescription(DmShared.xNull(rsTemp.getString("description")));
                userInfo_Mdl.seteMail(DmShared.xNull(rsTemp.getString("eMail")));
//                userInfo_Mdl.setBuidDetail("Version-" + DmBuild.getDmMajorVersion() + DmBuild.getDmBuildVersion());
                userInfo_Mdl.setVersionDetail("Version-" + DmBuild.getDmMajorVersion());
                userInfo_Mdl.setBuidDetail("Released on " + DmBuild.getDmBuildVersion().replace("-b", ""));
            }
            dmrTemp.setData(userInfo_Mdl);
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
}
