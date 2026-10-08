package aarogya.dashBoard.favorite.server;

import aarogya.dashBoard.favorite.shared.Favorite_Mdl;
import aarogya.dashBoard.favorite.shared.Favorite_Srv;
import aarogya.dashBoard.favorite.shared.GrdModuleMenu_Mdl;
import aarogya.dashBoard.favorite.shared.MenuDetail_Mdl;
import aarogya.dashBoard.favorite.shared.ModuleDetail_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Favorite_SrvImpl extends DmRSSPrivate implements Favorite_Srv {

    @Override
    public DmResult<String> insertRecord(Favorite_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            if ((!DmShared.xNull(mrTemp.getAllModuleName()).trim().equalsIgnoreCase("")) && (!DmShared.xNull(mrTemp.getAllMenuText()).trim().equalsIgnoreCase(""))) {

                sqlQuery = "Delete from user_Favorite_Web where mnuModule = :mnuModule and mnuText = :mnuText ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getAllModuleName()));
                psTemp.setString("mnuText", DmShared.xNull(mrTemp.getAllMenuText()));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "Insert Into user_Favorite_Web (mnuModule, mnuName, mnuText, userName) "
                        + " values (:mnuModule, :mnuName, :mnuText, :userName) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("mnuModule", mrTemp.getAllModuleName());
                psTemp.setString("mnuName", mrTemp.getAllMenuName());
                psTemp.setString("mnuText", mrTemp.getAllMenuText());
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                throw new DmException(300, "Please select menu to add !");
            }
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
    public DmResult<String> deleteRecord(Favorite_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            if ((!DmShared.xNull(mrTemp.getFavModuleName()).trim().equalsIgnoreCase("")) && (!DmShared.xNull(mrTemp.getFavMenuText()).equalsIgnoreCase(""))) {

                sqlQuery = "Delete from user_Favorite_Web where mnuModule = :mnuModule and mnuText = :mnuText ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getFavModuleName()));
                psTemp.setString("mnuText", DmShared.xNull(mrTemp.getFavMenuText()));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                throw new DmException(300, "Please select menu to remove !");
            }
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
    public DmResult<String> setPosition(List<GrdModuleMenu_Mdl> listTemp, Favorite_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            if (mrTemp.getGrdType().equalsIgnoreCase(Favorite_Mdl.favMenu)) {
                for (int i = 0; i < listTemp.size(); i++) {
                    GrdModuleMenu_Mdl menu_Mdl = listTemp.get(i);

                    if (DmShared.iNull(menu_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                        sqlQuery = "Update user_Favorite_Web "
                                + " set menuSNo = :menuSNo "
                                + " where mnuModule = :mnuModule and mnuText = :mnuText";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getFavModuleName()));
                        psTemp.setString("mnuText", DmShared.xNull(menu_Mdl.getSharedKeyFieldName()));
                        psTemp.setString("menuSNo", DmServer.vNull(i + 1, "0"));
                        psTemp.executeUpdate();
                        psTemp.close();

                        dmrTemp.setValue1(Favorite_Mdl.favMenu);
                    }
                }

            } else if (mrTemp.getGrdType().equalsIgnoreCase(Favorite_Mdl.favModule)) {
                for (int i = 0; i < listTemp.size(); i++) {

                    GrdModuleMenu_Mdl menu_Mdl = listTemp.get(i);
                    if (DmShared.iNull(menu_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                        sqlQuery = "Update user_Favorite_Web "
                                + " set moduleSNo = :moduleSNo "
                                + " where mnuModule = :mnuModule ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("mnuModule", DmShared.xNull(menu_Mdl.getSharedKeyFieldName()));
                        psTemp.setString("moduleSNo", DmServer.vNull(i + 1, "0"));
                        psTemp.executeUpdate();
                        psTemp.close();

                        dmrTemp.setValue1(Favorite_Mdl.favModule);
                    }
                }
            }

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
    public DmResult<List<GrdModuleMenu_Mdl>> getModuleList(String moduleType) {
        DmResult<List<GrdModuleMenu_Mdl>> rstTemp = new DmResult<>();
        List<GrdModuleMenu_Mdl> lstTemp = new ArrayList<GrdModuleMenu_Mdl>();
        GrdModuleMenu_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            String userName = DmServer.getSsnUserName(this);

            if (moduleType.equalsIgnoreCase(Favorite_Mdl.allModule)) {
                sqlQuery = "select Max(mnuModule) as module "
                        + " from user_Permission_Web where userName = '" + userName + "' "
                        + " group by mnuModule order by mnuModule ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
            } else if (moduleType.equalsIgnoreCase(Favorite_Mdl.favModule)) {
                sqlQuery = "select Max(mnuModule) as module "
                        + " from user_Favorite_Web where userName = '" + userName + "' "
                        + " group by mnuModule order by Max(moduleSNo) asc ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
            }
            int count = 0;
            while (rsTemp.next()) {
                node = new GrdModuleMenu_Mdl();
                node.setSharedKeyFieldCode(++count + "");
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("module")));

                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResult<List<GrdModuleMenu_Mdl>> getMenuList(String menuType, String moduleName) {
        DmResult<List<GrdModuleMenu_Mdl>> rstTemp = new DmResult<>();
        List<GrdModuleMenu_Mdl> lstTemp = new ArrayList<>();
        GrdModuleMenu_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            String userName = DmServer.getSsnUserName(this);
            String compCode = DmShared.iNull(DmServer.getSsnCompanyCode(this)).toString();

            if (userName.equalsIgnoreCase("SA") || userName.equalsIgnoreCase("SUPER")) {
                compCode = "-1";
            }

            if (menuType.equalsIgnoreCase(Favorite_Mdl.allMenu)) {
                sqlQuery = "select upw.mnuName ,upw.mnuText,mc.mnuShortHand "
                        + " from user_Permission_Web upw "
                        + " inner join ( "
                        + " select upw.mnuModule, upw.mnuName ,upw.mnuText  "
                        + " from user_Permission_Web upw "
                        + " left join	user_Permission_Web upw1 on upw1.userName = 'SA' and "
                        + " upw.mnuModule = upw1.mnuModule and upw.mnuName = upw1.parent "
                        + " where upw.userName = 'SA' and upw.mnuModule = :mnuModule and upw1.mnuName is Null "
                        + " ) as u on u.mnuModule = upw.mnuModule and u.mnuName = upw.mnuName "
                        + " left join menuCodification as mc ON mc.mnuModule=upw.mnuModule AND mc.mnuName=upw.mnuName "
                        + " where upw.userName = :userName and upw.comp_Code = :compCode and upw.mnuModule = :mnuModule "
                        + " order by upw.mnuModule,upw.mnuText asc ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("mnuModule", moduleName);
                psTemp.setString("userName", userName);
                psTemp.setString("compCode", compCode);
                rsTemp = psTemp.executeQuery();

            } else if (menuType.equalsIgnoreCase(Favorite_Mdl.favMenu) && !DmShared.xNull(moduleName).equalsIgnoreCase("")) {
                sqlQuery = "select ufw.mnuName,ufw.mnuText,mc.mnuShortHand  "
                        + " from user_Favorite_Web AS ufw"
                        + " left join menuCodification as mc ON mc.mnuModule=ufw.mnuModule AND mc.mnuName=ufw.mnuName"
                        + " where userName = '" + userName + "' and ufw.mnuModule = '" + moduleName + "'"
                        + " order by menuSNo asc ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
            } else {
                throw new DmException(300, "Please select module to open menu !");
            }
            int count = 0;
            while (rsTemp.next()) {
                node = new GrdModuleMenu_Mdl();
                node.setSharedKeyFieldCode(++count + "");
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mnuText")));
                node.setMenuName(DmShared.xNull(rsTemp.getString("mnuName")));
                node.setMenuShortHand(DmShared.xNull(rsTemp.getString("mnuShortHand")));
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResult<List<ModuleDetail_Mdl>> getFillRecords() {
        DmResult<List<ModuleDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        List<ModuleDetail_Mdl> listNode = new ArrayList<ModuleDetail_Mdl>();
        dmrTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "SELECT Max(mnuModule) as mnuModule  "
                    + " FROM user_Favorite_Web  "
                    + " where userName= '" + DmServer.getSsnUserName(this) + "'  "
                    + " group by mnuModule "
                    + " order by Max(moduleSNo) asc, Max(mnuModule) asc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                ModuleDetail_Mdl mdlTmp = new ModuleDetail_Mdl();
                mdlTmp.getMenuDetailMdl().addAll(getMenuModuleWise(DmShared.xNull(rsTemp.getString("mnuModule")), cnTemp));
                mdlTmp.setModuleName(DmShared.xNull(rsTemp.getString("mnuModule")));
                listNode.add(mdlTmp);
            }
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
            dmrTemp.setData(listNode);
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

    public List<MenuDetail_Mdl> getMenuModuleWise(String moduleName, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<MenuDetail_Mdl> listNode = new ArrayList<MenuDetail_Mdl>();

        try {
            sqlQuery = "SELECT  "
                    + " mnuText "
                    + " FROM user_Favorite_Web  "
                    + " where mnuModule=:mnuModule and userName=:userName "
                    + " order by moduleSNo, mnuModule, menuSNo, mnuText ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", DmShared.xNull(moduleName));
            psTemp.setString("userName", DmShared.xNull(DmServer.getSsnUserName(this)));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                MenuDetail_Mdl menuDetail = new MenuDetail_Mdl();
                menuDetail.setMenuName(DmShared.xNull(rsTemp.getString("mnuText")));
                listNode.add(menuDetail);
            }
            psTemp.close();
            rsTemp.close();
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

        return listNode;
    }

}
