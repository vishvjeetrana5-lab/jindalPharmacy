package dataman.dmToolkit.self.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.dmScheduler.server.DmAnonymousWebJobFactory;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserMaster_Mdl;
import dataman.dmBase.self.shared.UserMaster_Vld;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ManageUser extends ManageUser_Abstract {

    private static final LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private ManageUser_Abstract mua = null;

    public ManageUser(ManageUser_Abstract mua) {
        this.mua = mua;
    }

    public void saveUser(UserMaster_Mdl mrTemp, DmRSS rssTemp, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", userName, pltCode;
        boolean blnAdd = false;

        try {
            UserMaster_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(rssTemp);
            pltCode = DmServer.getSsnPLTCode(rssTemp);

//            cnTemp.setCatalog(DmServer.dbCompany);
            if (DmShared.xNull(mrTemp.getRoleName()).equalsIgnoreCase(DmShared.xNull(mrTemp.getSharedKeyFieldName()))) {
                throw new UnsupportedOperationException("User and Role can not be same.");
            }

            if (DmShared.xNull(mrTemp.getRoleName()).equalsIgnoreCase("SA") || DmShared.xNull(mrTemp.getRoleName()).equalsIgnoreCase("SUPER")) {
                mrTemp.setAdministratorYNCode("1");
            }

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()).replace(" ", ""));
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                sqlQuery = " select isNull(max(code),0) + 1 as code from " + DmServer.dbCompany + ".dbo.userMast ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                }
                rsTemp.close();
                psTemp.close();
                blnAdd = true;
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*=======================< Check whether User Name already exist or not >======================*/
            sqlQuery = " select	code from " + DmServer.dbCompany + ".dbo.userMast where user_Name = :user_Name and code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("user_Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("User Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

//            /*=======================< Check whether Email already exist or not >======================*/
//            sqlQuery = " select	code from userMast where email = :email and code <> :code ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("email", DmShared.xNull(mrTemp.getEmail()).trim());
//            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                throw new UnsupportedOperationException("Email already exists.");
//            }
//            rsTemp.close();
//            psTemp.close();

            /*=======================< Check whether Mobile already exist or not >======================*/
//            if (!DmShared.xNull(mrTemp.getMobile()).trim().equalsIgnoreCase("")) {
//                sqlQuery = " select	code from userMast where mobile = :mobile and code <> :code ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()).trim());
//                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//                rsTemp = psTemp.executeQuery();
//                if (rsTemp.next()) {
//                    throw new UnsupportedOperationException("Mobile already exists.");
//                }
//                rsTemp.close();
//                psTemp.close();
//            }
            /*=======================< Check whether Roled User under Role or not >======================*/
            if (!DmShared.xNull(mrTemp.getRoleName()).trim().equalsIgnoreCase("")) {
                sqlQuery = "Select code from " + DmServer.dbCompany + ".dbo.userMast where user_Role = :user_Role ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("user_Role", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new UnsupportedOperationException("Roled User can not be under Role.");
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "Select code from " + DmServer.dbCompany + ".dbo.userMast where user_name = :user_Role and isNull(user_role,'') <> '' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("user_Role", DmShared.xNull(mrTemp.getRoleName()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new UnsupportedOperationException("Roled User can not be under Role.");
                }
                rsTemp.close();
                psTemp.close();
            }

            /*=======================< Insert /Update record into userMast >======================*/
            if (blnAdd) {
                sqlQuery = "Insert into " + DmServer.dbCompany + ".dbo.userMast(code, user_Name, description, passWd, isAdmin, user_Role, username_linked, preparedBy, "
                        + " tokenKey, nature, email, isActive, mobile, u_EntDt, UserLockTimeOut, departmentCode) "
                        + " Values (:code, :user_Name, :description, convert(varchar(32),HashBytes('MD5',:PassWd),2), :isAdmin, :user_Role, :username_linked, :preparedBy, "
                        + " :tokenKey, :nature, :email, :isActive, :mobile, :u_EntDt, :UserLockTimeOut, :departmentCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("user_Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("description", DmShared.xNull(mrTemp.getDescription()));
                psTemp.setString("passWd", DmShared.xNull(mrTemp.getSharedKeyFieldName().toLowerCase() + mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("isAdmin", DmServer.vNull(mrTemp.getAdministratorYNCode(), "0"));
                psTemp.setString("isActive", DmServer.vNull(mrTemp.getActiveYNCode(), "0"));
                psTemp.setString("user_Role", DmShared.xNull(mrTemp.getRoleName()));
                psTemp.setString("preparedBy", DmShared.xNull(userName));
                psTemp.setString("email", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("tokenKey", DmShared.xNull(mrTemp.getTokenKey()));
                psTemp.setString("username_linked", DmShared.xNull(mrTemp.getGridLinkedUser()));
                psTemp.setString("nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("UserLockTimeOut", DmShared.iNull(mrTemp.getLockTimeOut()).toString());
                psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = "Update " + DmServer.dbCompany + ".dbo.userMast set "
                        + " user_Name = :user_Name, "
                        + " description = :description, "
                        + " user_Role = :user_Role, "
                        + " isAdmin = :isAdmin, "
                        + " isActive = :isActive, "
                        + " u_name = :u_name, "
                        + " email = :email, "
                        + " username_linked = :username_linked, "
                        + " mobile = :mobile, "
                        + " UserLockTimeOut = :UserLockTimeOut, "
                        + " departmentCode = :departmentCode, "
                        + " nature = :nature, "
                        + " u_EntDt = :u_EntDt "
                        + " where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("user_Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("description", DmShared.xNull(mrTemp.getDescription()));
                psTemp.setString("isAdmin", DmServer.vNull(mrTemp.getAdministratorYNCode(), "0"));
                psTemp.setString("isActive", DmServer.vNull(mrTemp.getActiveYNCode(), "0"));
                psTemp.setString("user_Role", DmShared.xNull(mrTemp.getRoleName()));
                psTemp.setString("username_linked", DmShared.xNull(mrTemp.getGridLinkedUser()));
                psTemp.setString("u_name", DmShared.xNull(userName));
                psTemp.setString("email", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("UserLockTimeOut", DmShared.iNull(mrTemp.getLockTimeOut()).toString());
                psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*=======================< Delete record from User_Permission >======================*/
            sqlQuery = " delete from  " + DmServer.dbCompany + ".dbo.userLinkAccount  Where User_Name =:LinkedUser ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("LinkedUser", mrTemp.getSharedKeyFieldName());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert into userLinkAccount >====================================*/
            if (!DmShared.xNull(mrTemp.getLinkedAccountCode()).equalsIgnoreCase("")) {
                sqlQuery = " insert into  " + DmServer.dbCompany + ".dbo.userLinkAccount(pltCode,user_name,subCode) values (:pltCode,:user_name,:subCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("pltCode", DmShared.foriegnKey(pltCode));
                psTemp.setString("user_name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getLinkedAccountCode()));
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (mua != null) {
                mua.saveUser(mrTemp, rssTemp, cnTemp);
            }
            saveRole(mrTemp.getSharedKeyFieldName(), !mrTemp.getRoleName().equalsIgnoreCase("") ? mrTemp.getRoleName() : mrTemp.getSharedKeyFieldName(), cnTemp);

//            String userRole = "";
//            if (!mrTemp.getRoleName().equalsIgnoreCase("")) {
//                userRole = mrTemp.getRoleName();
//            } else {
//                userRole = mrTemp.getSharedKeyFieldName();
//            }
//            String StrSQL_Role = "Select User_Name From " + DmServer.dbCompany + ".dbo.UserMast Where User_Role = '" + userRole + "' ";
//
//            /*=======================< Delete record from UserSite >======================*/
//            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.UserSite Where User_Name In ( " + StrSQL_Role + ")";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*=======================< Delete record from User_Permission >======================*/
//            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Permission Where UserName In ( " + StrSQL_Role + ")";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*=======================< Delete record from User_Permission_Web >======================*/
//            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Permission_Web Where UserName In ( " + StrSQL_Role + ")";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*=======================< Delete record from User_Control_Permission >======================*/
//            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Control_Permission Where UserName In ( " + StrSQL_Role + ")";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*=======================< Delete record from User_Control_Permission_Web >======================*/
//            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Control_Permission_Web Where UserName In ( " + StrSQL_Role + ")";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*=======================< Delete record from User_Voucher_Permission >======================*/
//            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Voucher_Permission Where User_Name In ( " + StrSQL_Role + ")";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*====================================< Update records of userMast >====================================*/
//            sqlQuery = "Update um_Role "
//                    + " set um_Role.isAdmin = um.isAdmin , um_Role.userName_Linked = um.userName_Linked "
//                    + " from " + DmServer.dbCompany + ".dbo.userMast um_Role"
//                    + " left join " + DmServer.dbCompany + ".dbo.userMast um on um.user_Name = um_Role.user_Role "
//                    + " where um_Role.user_Role = :user_Role and isNull(um.user_Name,'') <> '' ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("user_Role", userRole);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*====================================< Insert into UserSite >====================================*/
//            sqlQuery = " Insert Into  " + DmServer.dbCompany + ".dbo.UserSite(User_Name,CompCode,SiteList,CostCenterList) "
//                    + " Select	UM.User_Name,U.CompCode,U.SiteList,U.CostCenterList "
//                    + " From  " + DmServer.dbCompany + ".dbo.UserSite U "
//                    + " Left Join "
//                    + " ( "
//                    + " Select UM_Role.User_Name "
//                    + " From  " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
//                    + " Left Join  " + DmServer.dbCompany + ".dbo.UserMast UM On UM.User_Name = UM_Role.User_Role  "
//                    + " Where UM_Role.User_Role = :User_Role  "
//                    + " ) As UM On 1 = 1 "
//                    + " Where U.User_Name = :User_Role And isNull(UM.User_Name,'') <> ''  ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("User_Role", userRole);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*====================================< Insert into User_Permission >====================================*/
//            sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.User_Permission(UserName,Comp_Code,Parent,MnuText,MnuName,Permission,SNo,MnuModule,MnuLevel,ReportFor) "
//                    + " Select	UM.User_Name,U.Comp_Code,U.Parent,U.MnuText,U.MnuName,U.Permission,U.SNo,U.MnuModule,U.MnuLevel,U.ReportFor "
//                    + " From " + DmServer.dbCompany + ".dbo.User_Permission U "
//                    + " Left Join  "
//                    + " ( "
//                    + " Select UM_Role.User_Name "
//                    + " From " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
//                    + " Left Join " + DmServer.dbCompany + ".dbo.UserMast UM On UM.User_Name = UM_Role.User_Role  "
//                    + " Where UM_Role.User_Role = :User_Role  "
//                    + " ) As UM On 1 = 1 "
//                    + " Where U.UserName = :User_Role And isNull(UM.User_Name,'') <> '' ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("User_Role", userRole);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*====================================< Insert into User_Permission_Web >====================================*/
//            sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.User_Permission_Web(UserName,Comp_Code,Parent,MnuText,MnuName,Permission,SNo,MnuModule,MnuLevel,ReportFor) "
//                    + " Select	UM.User_Name,U.Comp_Code,U.Parent,U.MnuText,U.MnuName,U.Permission,U.SNo,U.MnuModule,U.MnuLevel,U.ReportFor "
//                    + " From " + DmServer.dbCompany + ".dbo.User_Permission_Web U "
//                    + " Left Join  "
//                    + " ( "
//                    + " Select UM_Role.User_Name "
//                    + " From " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
//                    + " Left Join " + DmServer.dbCompany + ".dbo.UserMast UM On UM.User_Name = UM_Role.User_Role  "
//                    + " Where UM_Role.User_Role = :User_Role "
//                    + " ) As UM On 1 = 1 "
//                    + " Where U.UserName = :User_Role And isNull(UM.User_Name,'') <> ''  ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("User_Role", userRole);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*====================================< Insert into User_Control_Permission >====================================*/
//            sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.User_Control_Permission(UserName,MnuModule,GroupName,MnuText,GroupText,MnuName,Comp_Code)"
//                    + " Select	UM.User_Name,U.MnuModule,U.GroupName,U.MnuText,U.GroupText,U.MnuName,U.Comp_Code "
//                    + " From " + DmServer.dbCompany + ".dbo.User_Control_Permission U "
//                    + " Left Join  "
//                    + " ( "
//                    + " Select UM_Role.User_Name "
//                    + " From " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
//                    + " Left Join " + DmServer.dbCompany + ".dbo.UserMast UM On UM.User_Name = UM_Role.User_Role  "
//                    + " Where UM_Role.User_Role = :User_Role  "
//                    + " ) As UM On 1 = 1 "
//                    + "Where U.UserName = :User_Role And isNull(UM.User_Name,'') <> '' ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("User_Role", userRole);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*====================================< Insert into User_Control_Permission_Web >====================================*/
//            sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.User_Control_Permission_Web(UserName,MnuModule,GroupName,MnuText,GroupText,MnuName,Comp_Code)"
//                    + " Select	UM.User_Name,U.MnuModule,U.GroupName,U.MnuText,U.GroupText,U.MnuName,U.Comp_Code "
//                    + " From " + DmServer.dbCompany + ".dbo.User_Control_Permission_Web U "
//                    + " Left Join  "
//                    + " ( "
//                    + " Select UM_Role.User_Name "
//                    + " From " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
//                    + " Left Join " + DmServer.dbCompany + ".dbo.UserMast UM On UM.User_Name = UM_Role.User_Role  "
//                    + " Where UM_Role.User_Role = :User_Role  "
//                    + " ) As UM On 1 = 1 "
//                    + "Where U.UserName = :User_Role And isNull(UM.User_Name,'') <> '' ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("User_Role", userRole);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*====================================< Insert into user_Voucher_Permission >====================================*/
//            sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.user_Voucher_Permission(User_Name,Comp_Code,vTypeSName,Category,VP_Days,SP_Days,DP_Days,AllowValueUpto,isAllowAudit) "
//                    + " Select	UM.User_Name,U.Comp_Code,U.vTypeSName,U.Category,U.VP_Days,U.SP_Days,U.DP_Days,U.AllowValueUpto,U.isAllowAudit "
//                    + " From " + DmServer.dbCompany + ".dbo.User_Voucher_Permission U "
//                    + " Left Join  "
//                    + " ( "
//                    + " Select UM_Role.User_Name "
//                    + " From " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
//                    + " Left Join " + DmServer.dbCompany + ".dbo.UserMast UM On UM.User_Name = UM_Role.User_Role  "
//                    + " Where UM_Role.User_Role = :User_Role  "
//                    + " ) As UM On 1 = 1 "
//                    + " Where U.User_Name = :User_Role And isNull(UM.User_Name,'') <> '' ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("User_Role", userRole);
//            psTemp.executeUpdate();
//            psTemp.close();
            /*====================================< Check No. of Licence User >====================================*/
            sqlQuery = "Select count(user_Name) as cnt from " + DmServer.dbCompany + ".dbo.userMast where isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(DmServer.getSsnNoOfUser(rssTemp)).intValue() >= 0) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > DmShared.iNull(DmServer.getSsnNoOfUser(rssTemp)).intValue()) {
                        throw new DmException(111, "Invalid License Key for No. of Users.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (SQLException | DmException e) {
            throw new UnsupportedOperationException(e.getMessage());
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

    public void sendMail(String userName, DmRSS rssTemp) throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL, templateCode = "";
        ResultSet rsTemp = null;

        DmAnonymousWebJobFactory webJobFactory = null;
        SiteEnviro_Mdl siteEnviro;
        List<Long> templateIds = new ArrayList<Long>();
        List<String> uniqueId = new ArrayList<String>();
        String[] templateId = null;
        try {

            UUID uid = UUID.randomUUID();

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            strSQL = "SELECT attachWJTemplates FROM voucher_Type WHERE short_Name = '" + lvc.USER_MASTER + "' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                templateCode = DmShared.xNull(rsTemp.getString("attachWJTemplates"));
            }
            rsTemp.close();
            psTemp.close();

            spBegin = new DmSavePoint(cnTemp);
            if (!templateCode.trim().equalsIgnoreCase("")) {
                strSQL = " Delete From " + DmServer.dbCompany + ".dbo.linkVerification Where userName=:userName and keyField = :keyField ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("userName", userName);
                psTemp.setString("keyField", "UserMast:" + userName);
                psTemp.executeUpdate();
                psTemp.close();

                strSQL = "INSERT INTO " + DmShared.xNull(DmServer.dbCompany).trim() + ".dbo.linkVerification (code,keyField, userName, pltCode, compCode, siteCode, expiryDate,  preparedBy,  u_EntDt) "
                        + "VALUES (:code,:keyField, :username, :pltcode, :compcode, :sitecode, dateadd(HOUR, 24, :u_EntDt),  :preparedby, :u_EntDt)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", uid.toString());
                psTemp.setString("keyField", "UserMast:" + userName);
                psTemp.setString("username", userName);
                psTemp.setString("pltcode", DmServer.getSsnPLTCode(rssTemp));
                psTemp.setString("compcode", DmServer.getSsnCompanyCode(rssTemp));
                psTemp.setString("sitecode", DmServer.getSsnSiteCode(rssTemp));
                psTemp.setString("preparedby", DmServer.getSsnUserName(rssTemp));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

            spBegin.commit();
            cnTemp.close();

            if (!templateCode.trim().equalsIgnoreCase("")) {
                uniqueId.add(uid.toString());
                webJobFactory = new DmAnonymousWebJobFactory();
                siteEnviro = webJobFactory.getEnviroSettings(rssTemp);

                if (DmShared.xNull(templateCode).contains("|")) {
                    templateId = templateCode.split("\\|");
                    for (int i = 0; i < templateId.length; i++) {
                        templateIds.add(DmShared.iNull(templateId[i]).longValue());
                    }
                } else {
                    templateIds.add(DmShared.iNull(templateCode).longValue());
                }

                webJobFactory.runJobsAsync(rssTemp, siteEnviro, templateIds, uniqueId, null);
            }

            if (mua != null) {
                mua.sendMail(userName, rssTemp);
            }
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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

    }

    public void deleteUser(String userName, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "Select code from " + DmServer.dbCompany + ".dbo.userMast where User_Role In ( " + DmServer.getParamList(":userName", userName) + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("This is system defined. You can not delete it.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.linkVerification Where userName=:userName and keyField = :keyField ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", userName);
            psTemp.setString("keyField", "UserMast:" + userName);
            psTemp.executeUpdate();
            psTemp.close();

//            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Permission Where UserName In ( " + DmServer.getParamList(":userName", userName) + ") ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setStringList("userName", userName);
//            psTemp.executeUpdate();
//            psTemp.close();
            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Permission_Web Where UserName In ( " + DmServer.getParamList(":userName", userName) + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("userName", userName);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Voucher_Permission Where User_Name In ( " + DmServer.getParamList(":userName", userName) + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("userName", userName);
            psTemp.executeUpdate();
            psTemp.close();

//            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Control_Permission Where UserName In ( " + DmServer.getParamList(":userName", userName) + ") ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setStringList("userName", userName);
//            psTemp.executeUpdate();
//            psTemp.close();
            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.user_control_permission_web Where UserName In ( " + DmServer.getParamList(":userName", userName) + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("userName", userName);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.UserSite Where User_Name In ( " + DmServer.getParamList(":userName", userName) + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("userName", userName);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " delete from  " + DmServer.dbCompany + ".dbo.userLinkAccount  Where User_Name In ( " + DmServer.getParamList(":userName", userName) + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("userName", userName);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.UserMast Where User_Name In ( " + DmServer.getParamList(":userName", userName) + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("userName", userName);
            psTemp.executeUpdate();
            psTemp.close();

            if (mua != null) {
                mua.deleteUser(userName, cnTemp);
            }

        } catch (SQLException | DmException e) {
            throw new UnsupportedOperationException(e.getMessage());
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

    public void saveRole(String userName, String roleName, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, sqlRoleQuery, userRole;
        try {
            if (!DmShared.xNull(roleName).equalsIgnoreCase("")) {
                userRole = roleName;
            } else {
                userRole = userName;
            }
            sqlRoleQuery = "Select User_Name From " + DmServer.dbCompany + ".dbo.UserMast Where User_Role = '" + userRole + "' ";

            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.UserSite Where User_Name In ( " + sqlRoleQuery + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

//            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Permission Where UserName In ( " + sqlRoleQuery + ")";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.executeUpdate();
//            psTemp.close();
            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Permission_Web Where UserName In ( " + sqlRoleQuery + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

//            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Control_Permission Where UserName In ( " + sqlRoleQuery + ")";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.executeUpdate();
//            psTemp.close();
            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.user_Control_Permission_Web Where UserName In ( " + sqlRoleQuery + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.User_Voucher_Permission Where User_Name In ( " + sqlRoleQuery + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " Delete From " + DmServer.dbCompany + ".dbo.user_Favorite_Web Where userName In ( " + sqlRoleQuery + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " UPDATE UM_Role SET UM_Role.isAdmin = UM.isAdmin , UM_Role.UserName_Linked = UM.UserName_Linked  "
                    + "FROM " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
                    + "LEFT JOIN " + DmServer.dbCompany + ".dbo.UserMast UM ON UM.User_Name = UM_Role.User_Role "
                    + "WHERE UM_Role.User_Role = :User_Role AND isNull(UM.User_Name,'') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("User_Role", userRole);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " Insert Into  " + DmServer.dbCompany + ".dbo.UserSite(User_Name,CompCode,SiteList,CostCenterList) "
                    + " Select	UM.User_Name,U.CompCode,U.SiteList,U.CostCenterList "
                    + " From  " + DmServer.dbCompany + ".dbo.UserSite U "
                    + " Left Join ( "
                    + " Select UM_Role.User_Name "
                    + " From  " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
                    + " Where UM_Role.User_Role = :User_Role  "
                    + " ) As UM On 1 = 1 "
                    + " Where U.User_Name = :User_Role And isNull(UM.User_Name,'') <> ''  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("User_Role", userRole);
            psTemp.executeUpdate();
            psTemp.close();

//            sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.User_Permission(UserName,Comp_Code,Parent,MnuText,MnuName,Permission,SNo,MnuModule,MnuLevel,ReportFor,isActive) "
//                    + " Select	UM.User_Name,U.Comp_Code,U.Parent,U.MnuText,U.MnuName,U.Permission,U.SNo,U.MnuModule,U.MnuLevel,U.ReportFor,U.isActive "
//                    + " From " + DmServer.dbCompany + ".dbo.User_Permission U "
//                    + " Left Join  "
//                    + " ( "
//                    + " Select UM_Role.User_Name "
//                    + " From " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
//                    + " Where UM_Role.User_Role = :User_Role  "
//                    + " ) As UM On 1 = 1 "
//                    + " Where U.UserName = :User_Role And isNull(UM.User_Name,'') <> '' ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("User_Role", userRole);
//            psTemp.executeUpdate();
//            psTemp.close();
//            sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.User_Control_Permission(UserName,MnuModule,GroupName,MnuText,GroupText,MnuName,Comp_Code)"
//                    + " Select	UM.User_Name,U.MnuModule,U.GroupName,U.MnuText,U.GroupText,U.MnuName,U.Comp_Code "
//                    + " From " + DmServer.dbCompany + ".dbo.User_Control_Permission U "
//                    + " Left Join  "
//                    + " ( "
//                    + " Select UM_Role.User_Name "
//                    + " From " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
//                    + " Where UM_Role.User_Role = :User_Role  "
//                    + " ) As UM On 1 = 1 "
//                    + "Where U.UserName = :User_Role And isNull(UM.User_Name,'') <> '' ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("User_Role", userRole);
//            psTemp.executeUpdate();
//            psTemp.close();
            sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.user_Control_Permission_Web(UserName,MnuModule,GroupName,MnuText,GroupText,MnuName,Comp_Code)"
                    + " Select	UM.User_Name,U.MnuModule,U.GroupName,U.MnuText,U.GroupText,U.MnuName,U.Comp_Code "
                    + " From " + DmServer.dbCompany + ".dbo.user_Control_Permission_Web U "
                    + " Left Join  "
                    + " ( "
                    + " Select UM_Role.User_Name "
                    + " From " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
                    + " Where UM_Role.User_Role = :User_Role  "
                    + " ) As UM On 1 = 1 "
                    + "Where U.UserName = :User_Role And isNull(UM.User_Name,'') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("User_Role", userRole);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.User_Voucher_Permission(User_Name,Comp_Code,vTypeSName,Category,VP_Days,SP_Days,DP_Days,AllowValueUpto,isAllowAudit) "
                    + " Select	UM.User_Name,U.Comp_Code,U.vTypeSName,U.Category,U.VP_Days,U.SP_Days,U.DP_Days,U.AllowValueUpto,U.isAllowAudit "
                    + " From " + DmServer.dbCompany + ".dbo.User_Voucher_Permission U "
                    + " Left Join  "
                    + " ( "
                    + " Select UM_Role.User_Name "
                    + " From " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
                    + " Where UM_Role.User_Role = :User_Role  "
                    + " ) As UM On 1 = 1 "
                    + " Where U.User_Name = :User_Role And isNull(UM.User_Name,'') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("User_Role", userRole);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " Insert Into " + DmServer.dbCompany + ".dbo.User_Permission_Web(UserName, Comp_Code, Parent, MnuText, MnuName, "
                    + "                 Permission, SNo, MnuModule, MnuLevel, ReportFor, isActive, vp_Days, sp_days, dp_Days, allowValueUpto) "
                    + " Select	UM.User_Name, U.Comp_Code, U.Parent, U.MnuText, U.MnuName, U.Permission, U.SNo, U.MnuModule, U.MnuLevel, "
                    + "             U.ReportFor, U.isActive, U.vp_Days , U.sp_days , U.dp_Days  ,U.allowValueUpto  "
                    + " From " + DmServer.dbCompany + ".dbo.User_Permission_Web U "
                    + " Left Join  "
                    + " ( "
                    + " Select UM_Role.User_Name "
                    + " From " + DmServer.dbCompany + ".dbo.UserMast UM_Role "
                    + " Where UM_Role.User_Role = :User_Role "
                    + " ) As UM On 1 = 1 "
                    + " Where U.UserName = :User_Role and isNull(UM.User_Name,'') <> ''  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("User_Role", userRole);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Insert Into " + DmServer.dbCompany + ".dbo.user_Favorite_Web(userName, mnuModule, mnuName, mnuText, moduleSNo, menuSNo) "
                    + "SELECT ur.user_Name, mnuModule, mnuName, mnuText, moduleSNo, menuSNo "
                    + "FROM " + DmServer.dbCompany + ".dbo.user_Favorite_Web AS ufw "
                    + "left join ( "
                    + "     select um.user_Name "
                    + "         from " + DmServer.dbCompany + ".dbo.userMast um "
                    + "         where um.User_Role = :User_Role "
                    + ") ur On 1 = 1 "
                    + "where ufw.userName = :User_Role and isNull(ur.User_Name,'') <> ''  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("User_Role", userRole);
            psTemp.executeUpdate();
            psTemp.close();

            if (mua != null) {
                mua.saveRole(userName, roleName, cnTemp);
            }
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
    }
}
