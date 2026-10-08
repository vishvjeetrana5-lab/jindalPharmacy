package dataman.utility.branchMaster.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.utility.branchMaster.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BranchMaster_SrvImpl extends DmRSSPrivate implements BranchMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            rsTemp = cnTemp.createStatement().executeQuery("Select Code As BranchCode From BranchMast Order By Name;");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("BranchCode")));
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
    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("Select Name From BranchMast ");
            helper.addSort("Name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResult<BranchMaster_Mdl> getMoveRec(String strKey) {
        DmResult<BranchMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        BranchMaster_Mdl mrTemp = new BranchMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            strSQL = " SELECT B.ManualCode,B.Code,B.Name,City,b.preparedBY,b.u_name From BranchMast as b Where b.Code= :bracnhCode Order By B.Name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("bracnhCode", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                mrTemp.setBranchCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("City")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBY")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
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
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            strSQL = "Select Count(*) as cnt from SiteMast where branchCode= :branchcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("branchcode", strKey);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Branch already in use.");
            }
            rsTemp.close();
            psTemp.close();

            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From BranchMast Where Code = :branchcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("branchcode", strKey);
            psTemp.executeUpdate();

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
    public DmResult<String> saveRecord(BranchMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL;
        ResultSet rsTemp = null, rsTemp1 = null;

        dmrTemp.setData("");

        try {
            BranchMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                strSQL = " SELECT isNull(MAX(Code),0)+1 AS Mx FROM BranchMast";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setSharedKeyFieldCode(rsTemp.getString("Mx"));
                }
                rsTemp.close();
                psTemp.close();
                blnAdd = true;
            }
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setCityName(DmShared.removeSpace(mrTemp.getCityName()));
            mrTemp.setBranchCode(DmShared.xNull(mrTemp.getBranchCode()).replace(" ", ""));
            strSQL = "Select Count(Name) As Cnt From BranchMast BM Where BM.Name= :branchName And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("branchName", mrTemp.getSharedKeyFieldName());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Branch Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "Select Count(Name) As Cnt From BranchMast BM Where BM.ManualCode = :ManualCode  And Code <> :Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("ManualCode", mrTemp.getBranchCode());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Branch Code already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (mrTemp.getBranchCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            strSQL = "   SELECT"
                    + "  C.CentralData_Path "
                    + "  FROM "
                    + "  Company C  "
                    + "  LEFT JOIN SiteMast SM  "
                    + "  ON SM.PLTCode = C.PLTCode  "
                    + "  LEFT JOIN sys.databases AS  SD  "
                    + "  ON SD.name = RTRIM(LTRIM(C.CentralData_Path))  "
                    + "  WHERE isNull(SD.name, '') <> ''  "
                    + "  AND SM.BranchCode = :branchCode  "
                    + "  GROUP BY C.CentralData_Path";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp1.setString("branchCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp1 = psTemp1.executeQuery();
            if (blnAdd) {
                strSQL = "Insert Into BranchMast(Code,ManualCode,Name,City, preparedBY,U_EntDt ) Values  "
                        + "(:Code,:ManualCode,:Name,:City,:preparedBY,getdate()) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("ManualCode", mrTemp.getBranchCode());
                psTemp.setString("Name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("City", mrTemp.getCityName());
                psTemp.setString("preparedBY", DmServer.getSsnUserName(this));

            } else {
                strSQL = "Update BranchMast Set "
                        + "Name = :branchName, "
                        + "City = :cityName, "
                        + "ManualCode = :code, "
                        + "U_Name = :userName, "
                        + "U_EntDt = getdate() "
                        + " Where Code = :branchCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("branchName", mrTemp.getSharedKeyFieldName());
                psTemp.setString("cityName", mrTemp.getCityName());
                psTemp.setString("code", mrTemp.getBranchCode());
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setString("branchCode", mrTemp.getSharedKeyFieldCode());
            }
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "UPDATE SM "
                    + "SET "
                    + "SM.NAME = CONCAT(:name, ' [' , plt.sName , ']'), "
                    + "SM.city = :city "
                    + "FROM COMPANY AS C LEFT JOIN SITEMAST AS SM ON SM.PLTCODE = C.PLTCODE "
                    + "left join productLicensedTo as plt on plt.pltCode= sm.pltCode "
                    + "WHERE SM.BRANCHCODE = :branchCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("name", mrTemp.getSharedKeyFieldName());
            psTemp.setString("branchCode", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("city", mrTemp.getCityName());
            psTemp.executeUpdate();
            psTemp.close();
            //==============================================================================
            while (rsTemp1.next()) {
                strSQL = " SELECT COUNT(*) AS cnt "
                        + "	FROM " + rsTemp1.getString("CentralData_Path") + ".sys.objects T  "
                        + "	WHERE T.name = 'SiteMast' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    strSQL = "UPDATE SMM "
                            + "SET "
                            + "SMM.Name = SM.Name ,"
                            + "SMM.city = SM.city "
                            + " FROM " + rsTemp1.getString("CentralData_Path") + ".dbo.SiteMast SMM "
                            + "LEFT JOIN SiteMast SM ON SM.Code = SMM.Code "
                            + "WHERE SM.BranchCode = :branchCode";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("branchCode", mrTemp.getSharedKeyFieldCode());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            psTemp1.close();
            rsTemp1.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
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

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("Select Code,ManualCode, Name,City,U_Name From BranchMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setBranchCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("City")));
                listNode.setUserName(DmShared.xNull(rsTemp.getString("U_Name")));
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
