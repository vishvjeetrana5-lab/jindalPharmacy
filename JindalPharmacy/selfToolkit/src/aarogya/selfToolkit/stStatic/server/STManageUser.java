package aarogya.selfToolkit.stStatic.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.server.ManageUser_Abstract;
import dataman.dmBase.self.shared.UserMaster_Mdl;
import java.sql.Connection;

public class STManageUser extends ManageUser_Abstract {

    @Override
    public void saveUser(UserMaster_Mdl mrTemp, DmRSS rssTemp, Connection cnTemp) throws Exception {

    }

    @Override
    public void sendMail(String userName, DmRSS rssTemp) throws Exception {

    }

    @Override
    public void deleteUser(String userName, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        sqlQuery = "Delete from userServicePermission where userName = :userName ";
//                + "userName in (select user_Name from " + DmServer.dbCompany + ".dbo.userMast where user_Role = :userName) ";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("userName", DmShared.xNull(userName));
        psTemp.executeUpdate();
        psTemp.close();
    }

    @Override
    public void saveRole(String userName, String roleName, Connection cnTemp) throws Exception {
//        DmPreparedStatement psTemp = null;
//        String sqlQuery;

//        if (!DmShared.xNull(roleName).equalsIgnoreCase("")) {
//            userName = roleName;
//        } else {
//            userName = userName;
//        }
//        sqlQuery = "insert into " + DmServer.dbCompany + ".dbo.userLinkAccount(user_name,pltCode) "
//                + "select	um.user_name,ula.pltCode  "
//                + "from " + DmServer.dbCompany + ".dbo.userLinkAccount ula "
//                + "left join ( "
//                + "select  um.user_Name "
//                + "from " + DmServer.dbCompany + ".dbo.userMast um "
//                + "where um.user_Role = :user_name "
//                + ") as um on 1 = 1 "
//                + "left join " + DmServer.dbCompany + ".dbo.userLinkAccount ula1 on ula1.user_Name = um.user_Name and ula1.pltCode = ula.pltCode "
//                + "where ula.user_name = :user_name and ula1.user_name is null and um.user_name is not null";
//        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//        psTemp.setString("user_name", userName);
//        psTemp.executeUpdate();
//        psTemp.close();
//        sqlQuery = "update t set  "
//                + "t.ula_departmentCode = t.ula1_departmentCode, "
//                + "t.ula_nursingStationCode = t.ula1_nursingStationCode, "
//                + "t.ula_allowedDepartmentCodeList = t.ula1_allowedDepartmentCodeList "
//                + "from ( "
//                + "select	ula.user_name,ula.pltCode as ula_PltCode, "
//                + "ula.departmentCode as ula_departmentCode,ula.nursingStationCode as ula_nursingStationCode,ula.allowedDepartmentCodeList as ula_allowedDepartmentCodeList, "
//                + "ula1.departmentCode as ula1_departmentCode,ula1.nursingStationCode as ula1_nursingStationCode,ula1.allowedDepartmentCodeList as ula1_allowedDepartmentCodeList "
//                + "from " + DmServer.dbCompany + ".dbo.userLinkAccount ula "
//                + "left join " + DmServer.dbCompany + ".dbo.userLinkAccount ula1 on ula1.user_Name = :user_name and ula1.pltcode = ula.pltCode "
//                + "where ula.user_name in  "
//                + "(select  um.user_Name from " + DmServer.dbCompany + ".dbo.userMast um where um.user_Role = :user_name) "
//                + ") as t ";
//        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//        psTemp.setString("user_name", userName);
//        psTemp.executeUpdate();
//        psTemp.close();
//        sqlQuery = "Delete from userServicePermission "
//                + "where userName in (select user_Name from " + DmServer.dbCompany + ".dbo.userMast where user_Role = :userName) ";
//        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//        psTemp.setString("userName", DmShared.xNull(userName));
//        psTemp.executeUpdate();
//        psTemp.close();
//        sqlQuery = "insert into userServicePermission  "
//                + "select  um.user_Name as userName,usp.serviceCode,usp.serviceCategoryCode "
//                + "from userServicePermission usp "
//                + "left join " + DmServer.dbCompany + ".dbo.userMast um on 1 = 1 "
//                + "where usp.userName = :userName and um.user_role = :userName "
//                + "order by um.user_Name,usp.serviceCategoryCode,usp.serviceCode ";
//        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//        psTemp.setString("userName", DmShared.xNull(userName));
//        psTemp.executeUpdate();
//        psTemp.close();
    }
}
