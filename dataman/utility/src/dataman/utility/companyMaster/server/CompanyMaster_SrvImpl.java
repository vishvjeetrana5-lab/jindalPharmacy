package dataman.utility.companyMaster.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.utility.companyMaster.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CompanyMaster_SrvImpl extends DmRSSPrivate implements CompanyMaster_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("Select Comp_Code As SearchCode From Company order by Comp_Code");

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
    public DmResult<CompanyMaster_Mdl> getMoveRec(String strKey) {
        DmResult<CompanyMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CompanyMaster_Mdl mrTemp = new CompanyMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "SELECT  plt.Comp_Name AS Name, cmp.Comp_Code, plt.sname, cmp.PLTCode, cmp.Start_Dt, cmp.End_Dt, "
                    + "         cmp.CYear, cmp.PYear, cmp.V_Prefix, cmp.CentralData_Path, cmp.centralFileData_Path, "
                    + "         cmp.u_name, cmp.preparedBy, plt.latestcompCode,cmp.majorCurrency, cmp.minorCurrency"
                    + "         FROM Company as cmp "
                    + "         left join productLicensedTo as plt on cmp.PLTCode  = plt.PLTCode "
                    + "         WHERE cmp.Comp_Code= :compCode "
                    + "         order by plt.Comp_Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("compCode", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Comp_Code")));
                mrTemp.setProductLicenseKyCode(DmShared.xNull(rsTemp.getString("PLTCode")));
                mrTemp.setProductLicenseKyName(DmShared.xNull(rsTemp.getString("Name")));
                mrTemp.setCompanyCode(DmShared.xNull(rsTemp.getString("Comp_Code")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("sname")));
                mrTemp.setCurrentYear(DmShared.xNull(rsTemp.getString("Cyear")));
                mrTemp.setPreviousYear(DmShared.xNull(rsTemp.getString("PYear")));
                mrTemp.setYearStartDate(DmShared.dNull(rsTemp.getDate("Start_Dt")));
                mrTemp.setYearEndDate(DmShared.dNull(rsTemp.getDate("End_Dt")));
                mrTemp.setVoucherPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                mrTemp.setDataBaseName(DmShared.xNull(rsTemp.getString("CentralData_Path")));
                mrTemp.setFileDataBaseName(DmShared.xNull(rsTemp.getString("centralFileData_Path")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setMajorCurrency(DmShared.xNull(rsTemp.getString("majorCurrency")));
                mrTemp.setMinorCurrency(DmShared.xNull(rsTemp.getString("minorCurrency")));

                // DmShared.iNull(rsTemp.getString("Comp_Code")).longValue()
                //if (DmShared.iNull(rsTemp.getString("latestCompCode")).longValue() != 0) {
                if (DmShared.iNull(rsTemp.getString("latestCompCode")).longValue() == DmShared.iNull(rsTemp.getString("Comp_Code")).longValue()) {
                    mrTemp.setFinacialYearTag("1");
                    mrTemp.setFinacialYearText("Yes");
                } else {
                    mrTemp.setFinacialYearTag("0");
                    mrTemp.setFinacialYearText("No");
                }
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
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From Company Where Comp_Code= :compCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("compCode", strKey);
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
    public DmResult<String> saveRecord(CompanyMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            CompanyMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                strSQL = "SELECT ISNULL(MAX(CAST(comp_Code AS INTEGER)), 0) + 1 AS Mx FROM company";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setSharedKeyFieldCode(rsTemp.getString("Mx"));
                }
                rsTemp.close();
                psTemp.close();

                blnAdd = true;
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            if (blnAdd) {
                strSQL = " Insert Into Company(Comp_Code, CentralData_Path, centralFileData_Path, Start_Dt, u_EntDt, "
                        + " CYear,PYear,V_Prefix,PLTCode,preparedBY,End_Dt,majorCurrency,minorCurrency) "
                        + " Values( :compid , :databaseName, :centralFileData_Path, :startDt, :u_EntDt ,  "
                        + " :currentyear ,:previousYear,:voucherPrefix , :pltCode,:preparedBY,:endDt,:majorCurrency,"
                        + " :minorCurrency ) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("compid", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("databaseName", DmShared.xNull(mrTemp.getDataBaseName()));
                psTemp.setString("centralFileData_Path", DmShared.xNull(mrTemp.getFileDataBaseName()));
                psTemp.setDate("startDt", DmServer.convertDate(mrTemp.getYearStartDate()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("currentyear", DmShared.xNull(mrTemp.getCurrentYear()));
                psTemp.setString("previousYear", DmShared.xNull(mrTemp.getPreviousYear()));
                psTemp.setString("voucherPrefix", DmShared.xNull(mrTemp.getVoucherPrefix()));
                psTemp.setString("pltCode", DmShared.foriegnKey(mrTemp.getProductLicenseKyCode()));
                psTemp.setString("preparedBY", DmServer.getSsnUserName(this));
                psTemp.setDate("endDt", DmServer.convertDate(mrTemp.getYearEndDate()));
                psTemp.setString("majorCurrency", DmShared.xNull(mrTemp.getMajorCurrency()));
                psTemp.setString("minorCurrency", DmShared.xNull(mrTemp.getMinorCurrency()));
            } else {
                strSQL = "Update Company  Set "
                        + "cyear =:currentyear , "
                        + "Pyear =:previousYear , "
                        + "End_Dt = :endDt , "
                        + "Start_Dt = :startDt ,"
                        + "V_Prefix = :voucherPrefix , "
                        + "CentralData_Path =:databaseName, "
                        + "centralFileData_Path =:centralFileData_Path, "
                        + "PLTCode = :pltCode, "
                        + "u_name = :u_name , "
                        + "u_EntDt =:u_EntDt,"
                        + " majorCurrency=:majorCurrency,"
                        + " minorCurrency=:minorCurrency "
                        + "Where Comp_Code =:compid ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("currentyear", DmShared.xNull(mrTemp.getCurrentYear()));
                psTemp.setString("previousYear", DmShared.xNull(mrTemp.getPreviousYear()));
                psTemp.setDate("endDt", DmServer.convertDate(mrTemp.getYearEndDate()));
                psTemp.setDate("startDt", DmServer.convertDate(mrTemp.getYearStartDate()));
                psTemp.setString("voucherPrefix", DmShared.xNull(mrTemp.getVoucherPrefix()));
                psTemp.setString("databaseName", DmShared.xNull(mrTemp.getDataBaseName()));
                psTemp.setString("centralFileData_Path", DmShared.xNull(mrTemp.getFileDataBaseName()));
                psTemp.setString("pltCode", DmShared.foriegnKey(mrTemp.getProductLicenseKyCode()));
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("compid", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("majorCurrency", DmShared.xNull(mrTemp.getMajorCurrency()));
                psTemp.setString("minorCurrency", DmShared.xNull(mrTemp.getMinorCurrency()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getFinacialYearTag()).intValue() == 1) {
                strSQL = "Update ProductLicensedTo set latestCompCode = :latestCompCode Where PLTCode = :PLTCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("PLTCode", DmShared.iNull(mrTemp.getProductLicenseKyCode()).toString());
                psTemp.setString("latestCompCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.executeUpdate();
                psTemp.close();
            }

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
    public DmResultPageing<List<TxtHProductLicenseKey_Mdl>> getTxtHProductLicensekey(DmHelper helper, boolean blnToEndOfFile, String strCode) {
        DmResultPageing<List<TxtHProductLicenseKey_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHProductLicenseKey_Mdl> lstTemp = new ArrayList<>();
        TxtHProductLicenseKey_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("SELECT  PLTCode, Comp_Name,latestCompCode, SName FROM  ProductLicensedTo ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHProductLicenseKey_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("PLTCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Comp_Name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("SName")));
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
    public DmResult<List<GrdCompanyMaster_Mdl>> fillGrid(String pltCode) {
        DmResult<List<GrdCompanyMaster_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdCompanyMaster_Mdl> data = new ArrayList<>();
        dmrTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT plt.Comp_Name, cmp.CentralData_Path, "
                    + "     cmp.centralFileData_Path, cmp.Start_Dt, cmp.End_Dt, cmp.CYear, cmp.PYear, cmp.V_Prefix, "
                    + "     CONCAT(cmp.Comp_Code, '-', cmp.PLTCode )AS ConcatPLTCode, "
                    + "     case when plt.latestCompCode = cmp.comp_Code then 'Yes' else 'No' end as isLatest "
                    + "     FROM Company as cmp "
                    + "     left join productLicensedTo as plt on cmp.pltCode = plt.pltCode "
                    + "     WHERE cmp.PLTCode = :pltCode "
                    + "     order by plt.Comp_Name ");
            psTemp.setString("pltCode", pltCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdCompanyMaster_Mdl grd = new GrdCompanyMaster_Mdl();
                grd.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("ConcatPLTCode")));
                grd.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Comp_Name")));
                grd.setCurrentYear(DmShared.xNull(rsTemp.getString("CYear")));
                grd.setPreviousYear(DmShared.xNull(rsTemp.getString("PYear")));
                grd.setYearStart(DmShared.dNull(rsTemp.getDate("Start_Dt")));
                grd.setYearEnd(DmShared.dNull(rsTemp.getDate("End_Dt")));
                grd.setVoucherPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                grd.setDatabaseName(DmShared.xNull(rsTemp.getString("CentralData_Path")));
                grd.setFileDatabaseName(DmShared.xNull(rsTemp.getString("centralFileData_Path")));
                grd.setLatestCompCode(DmShared.xNull(rsTemp.getString("isLatest")));
                data.add(grd);
            }
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

            sqlWithParam.setSQL("Select Comp_Code As Code, p.Comp_Name As Name, start_Dt,end_Dt, cYear  "
                    + "                 From Company c "
                    + "                 left join productLicensedTo p on p.pltCode = c.pltCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setYearStartDate(DmShared.dNull(rsTemp.getDate("start_Dt")));
                listNode.setYearEndDate(DmShared.dNull(rsTemp.getDate("end_Dt")));
                listNode.setCurrentFinancialYear(DmShared.xNull(rsTemp.getString("cYear")));
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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
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
}
