package dataman.utility.productLicensedTo.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.utility.productLicensedTo.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductLicensedTo_SrvImpl extends DmRSSPrivate implements ProductLicensedTo_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("Select PLTCode As SearchCode,Comp_Name As Name From ProductLicensedTo order by Comp_Name ;");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
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
            sqlWithParam.setSQL("Select comp_name From ProductLicensedTo");
            helper.addSort("comp_name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("comp_name")));
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
    public DmResult<ProductLicensedTo_Mdl> getMoveRec(String strKey) {
        DmResult<ProductLicensedTo_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        ProductLicensedTo_Mdl mrTemp = new ProductLicensedTo_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            strSQL = "Select plt.Comp_Name As Name,plt.PLTCode,plt.sname,plt.latestCompCode,"
                    + " cmp.cYear,plt.u_Name,plt.preparedBy"
                    + " From ProductLicensedTo as plt "
                    + " left join company as cmp on plt.latestCompCode=cmp.comp_Code "
                    + " Where plt.PLTCode=  :PLTCode order by plt.Comp_Name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("PLTCode", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("PLTCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("sname")));
                mrTemp.setLatestCompCode(DmShared.xNull(rsTemp.getString("latestCompCode")));
                mrTemp.setLatestCYear(DmShared.xNull(rsTemp.getString("cYear")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));

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

            strSQL = "Select Count(*) as cnt from Company Where PLTCode=  :PLTCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("PLTCode", strKey);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Company Already In Use.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "Select Count(*) as cnt from SiteMast Where PLTCode=  :PLTCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("PLTCode", strKey);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Company Already In Use.");
            }
            rsTemp.close();
            psTemp.close();

            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From ProductLicensedTo Where PLTCode=  :PLTCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("PLTCode", strKey);
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
    public DmResult<String> saveRecord(ProductLicensedTo_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null, psTemp2 = null, psTemp3 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        ResultSet rsTemp1 = null;

        dmrTemp.setData("");

        try {
            ProductLicensedTo_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setShortName(DmShared.removeSpace(mrTemp.getShortName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                sqlQuery = "SELECT ISNULL(MAX(CAST(pltCode AS integer)), 0) + 1 AS Mx FROM productlicensedto ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setSharedKeyFieldCode(rsTemp.getString("Mx"));
                }
                rsTemp.close();
                psTemp.close();
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT COUNT(comp_Name) AS Cnt FROM productlicensedto WHERE (comp_Name = :compName) AND (pltCode <> :pltCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("compName", mrTemp.getSharedKeyFieldName());
            psTemp.setString("pltCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Duplicate Company Name.");
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.setShortName(DmShared.xNull(mrTemp.getShortName()).replace(" ", ""));
            sqlQuery = "SELECT COUNT(comp_Name) AS Cnt FROM productlicensedto WHERE (sName = :SName) AND (pltCode <> :pltCode) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("SName", mrTemp.getShortName());
            psTemp.setString("pltCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Duplicate Short Name.");
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {

                sqlQuery = " Insert Into ProductLicensedTo(PLTCode,Comp_Name,sname,latestCompCode,PreparedBY,u_EntDt) "
                        + " Values (:PLTCode, :Comp_Name, :sname,:latestCompCode,:PreparedBY,:u_EntDt)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("PLTCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("Comp_Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("sname", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("latestCompCode", DmShared.foriegnKey(mrTemp.getLatestCompCode()));
                psTemp.setString("PreparedBY", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

                psTemp.executeUpdate();
                psTemp.close();

            } else {

                sqlQuery = "Update ProductLicensedTo Set Comp_Name= :Comp_Name,sname= :sname ,"
                        + " latestCompCode= :latestCompCode,u_Name=:uName,u_EntDt =:u_EntDt Where PLTCode = :PLTCode ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                psTemp.setString("Comp_Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("PLTCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("sname", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("latestCompCode", DmShared.foriegnKey(mrTemp.getLatestCompCode()));
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = " UPDATE SM SET SM.name = BM.name + ' [' + p.sName + ']' "
                        + " FROM company AS C LEFT OUTER JOIN "
                        + " sitemast AS SM ON SM.pltCode = C.pltCode LEFT OUTER JOIN "
                        + " branchmast AS BM ON BM.code = SM.branchCode LEFT JOIN"
                        + " productLicensedTo as p on p.pltCode=c.pltCode "
                        + "WHERE (SM.pltCode = :PLTCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("PLTCode", mrTemp.getSharedKeyFieldCode());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "SELECT C.centralData_Path "
                        + "FROM company AS C LEFT OUTER JOIN "
                        + " sys.databases AS SD ON SD.name = RTRIM(LTRIM(C.centralData_Path)) "
                        + "WHERE (ISNULL(SD.name, '') <> '') AND (C.pltCode = :pltCode) "
                        + "GROUP BY C.centralData_Path";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("PLTCode", mrTemp.getSharedKeyFieldCode());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    sqlQuery = " SELECT COUNT(name) AS cnt "
                            + "	FROM " + rsTemp.getString("CentralData_Path") + ".sys.objects T  "
                            + "	WHERE T.name = 'SiteMast' ";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    rsTemp1 = psTemp1.executeQuery();
                    rsTemp1.next();
                    if (rsTemp1.getLong("cnt") > 0) {
                        sqlQuery = " UPDATE SMM  SET SMM.Name = SM.name "
                                + "FROM " + rsTemp.getString("CentralData_Path") + ".dbo.SiteMast AS SMM LEFT OUTER JOIN "
                                + " sitemast AS SM ON SM.code = SMM.Code "
                                + "WHERE (SM.pltCode = :PLTCode )";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("PLTCode", mrTemp.getSharedKeyFieldCode());
                        psTemp.executeUpdate();
                        psTemp.close();

                    }
                }
                rsTemp.close();
                psTemp.close();

            }

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
                if (psTemp2 != null) {
                    psTemp2.close();
                }
                if (psTemp3 != null) {
                    psTemp3.close();
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
            sqlWithParam.setSQL(" Select plt.Comp_Name As Comp_Name,plt.PLTCode,plt.sname,plt.latestCompCode,cmp.cYear as cYear"
                    + " From ProductLicensedTo as plt left join company as cmp "
                    + " on plt.latestCompCode=cmp.comp_Code Where plt.PLTCode=  cmp.PLTCode");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("PLTCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Comp_Name")));
                listNode.setSname(DmShared.xNull(rsTemp.getString("sname")));
                listNode.setLatestCompCode(DmShared.xNull(rsTemp.getString("latestCompCode")));

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
    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(String pltCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCompany_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCompany_Mdl> lstTemp = new ArrayList<>();
        TxtHCompany_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlQuery = ("SELECT comp_Code as Code ,start_dt,end_Dt,cYear,centralData_Path FROM company where pltCode= :pltCode");
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("pltCode", DmShared.iNull(pltCode).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCompany_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cYear")));
                listNode.setStartDt(DmShared.dNull(rsTemp.getDate("start_dt")));
                listNode.setEndDt(DmShared.dNull(rsTemp.getDate("end_Dt")));
                listNode.setCentralDataPath(DmShared.xNull(rsTemp.getString("centralData_Path")));
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
