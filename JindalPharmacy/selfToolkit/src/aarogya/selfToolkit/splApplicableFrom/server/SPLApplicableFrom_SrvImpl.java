package aarogya.selfToolkit.splApplicableFrom.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.splApplicableFrom.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SPLApplicableFrom_SrvImpl extends DmRSSPrivate implements SPLApplicableFrom_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getApplicableDateList(PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code) {
        DmResult<List<KeyFieldExtended>> rstTemp = new DmResult<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", sqlCondition = "";
        DmPreparedStatement psTemp = null;
        try {

            switch (priceListEntryFrom) {
                case CONSULTANT_MASTER:
                    sqlCondition = "and consultantCode = :keyCode ";
                    break;
                case ORGANIZATION_MASTER:
                    sqlCondition = "and organizationCode = :keyCode ";
                    break;
//                case INDIVIDUAL_SERVICE:
//                    sqlCondition = "and serviceCode = :keyCode ";
//                    break;
//                case TestMaster:
//                    sqlCondition = "and serviceCode = :keyCode ";
//                    break;
                default:
                    throw new DmException(0, "Price List: Entry From not found.");
            }

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select applicableFrom,applicableTo "
                    + "from patientServicePriceList "
                    + "where entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + "and organizationCode = :organizationCode "
                    + sqlCondition
                    + "group by applicableFrom,applicableTo "
                    + "order by applicableFrom desc ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            psTemp.setString("site_Code", site_Code);
            psTemp.setString("organizationCode", organizationCode);
            psTemp.setString("keyCode", keyCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableTo")));
                lstTemp.add(listNode);
            }
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
    public DmResult<String> deleteApplicableDate(String applicableDate, PriceListEntryFrom priceListEntryFrom, String keyCode, String organizationCode, String site_Code) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            STServer.deletePriceList(applicableDate, priceListEntryFrom, keyCode, organizationCode, site_Code, cnTemp);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDtApplicable(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", sqlCondition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            if (DmShared.xNull(svm.getSiteCode()).equalsIgnoreCase("")) {
                throw new DmException(0, "Please Select Site.");
            }
            switch (svm.getPriceListEntryFrom()) {
                case CONSULTANT_MASTER:
                    if (DmShared.iNull(svm.getKeyCode()).longValue() <= 0) {
                        throw new DmException(0, "Select Consultant.");
                    }
                    sqlCondition = "and consultantCode = :keyCode ";
                    break;
                case ORGANIZATION_MASTER:
                    if (DmShared.iNull(svm.getKeyCode()).longValue() <= 0) {
                        throw new DmException(0, "Select Orgnization.");
                    }
                    sqlCondition = "and organizationCode = :keyCode ";
                    break;
//                case INDIVIDUAL_SERVICE:
//                    if (DmShared.iNull(svm.getKeyCode()).longValue() <= 0) {
//                        throw new DmException(0, "Select Service.");
//                    }
//                    sqlCondition = "and serviceCode = :keyCode ";
//                    break;
//                case TestMaster:
//                    if (DmShared.iNull(svm.getKeyCode()).longValue() <= 0) {
//                        throw new DmException(0, "Select Test.");
//                    }
//                    sqlCondition = "and serviceCode = :keyCode ";
//                    break;
                default:
                    throw new DmException(0, "Price List: Entry From not found.");
            }

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select   applicableFrom "
                    + "from patientServicePriceList "
                    + "where entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + "and organizationCode = :organizationCode "
                    + sqlCondition
                    + "group by applicableFrom ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("entryFrom", DmShared.iNull(svm.getPriceListEntryFrom().toInt()).toString());
            sqlWithParam.setString("site_Code", svm.getSiteCode());
            sqlWithParam.setString("organizationCode", svm.getOrganizationTag());
            sqlWithParam.setString("keyCode", svm.getKeyCode());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableFrom")));
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
    public DmResult<KeyFieldExtended> getMaxApplicableDate(SearchValue_Mdl svm) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended listNode = new KeyFieldExtended();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", sqlCondition = "";
        DmPreparedStatement psTemp = null;
        try {

            switch (svm.getPriceListEntryFrom()) {
                case CONSULTANT_MASTER:
                    sqlCondition = "and consultantCode = :keyCode ";
                    break;
                case ORGANIZATION_MASTER:
                    sqlCondition = "and organizationCode = :keyCode ";
                    break;
//                case INDIVIDUAL_SERVICE:
//                    sqlCondition = "and serviceCode = :keyCode ";
//                    break;
//                case TestMaster:
//                    sqlCondition = "and serviceCode = :keyCode ";
//                    break;
                default:
                    throw new DmException(0, "Price List: Entry From not found.");
            }

            rstTemp.setData(listNode);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select top 1  max(applicableFrom) as applicableFrom "
                    + "from patientServicePriceList "
                    + "where entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + "and organizationCode = :organizationCode "
                    + sqlCondition
                    + "group by applicableFrom "
                    + "order by applicableFrom desc ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryFrom", DmShared.iNull(svm.getPriceListEntryFrom().toInt()).toString());
            psTemp.setString("organizationCode", svm.getOrganizationTag());
            psTemp.setString("site_Code", svm.getSiteCode());
            psTemp.setString("keyCode", svm.getKeyCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableFrom")));
            }
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (DmException | SQLException e) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlCondition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            switch (svm.getPriceListEntryFrom()) {
                case CONSULTANT_MASTER:
                    sqlCondition = "and consultantCode = :keyCode ";
                    break;
                default:
                    throw new DmException(0, "Price List: Entry From not found.");
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select distinct site_Code,sm.name"
                    + "      from patientServicePriceList pspl"
                    + "	  LEFT JOIN siteMast AS sm ON sm.code=pspl.site_Code"
                    + "      where entryFrom =:entryFrom"
                    + "      and organizationCode =:organizationCode " + sqlCondition + "");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("entryFrom", DmShared.iNull(svm.getPriceListEntryFrom().toInt()).toString());
            psTemp.setString("organizationCode", svm.getOrganizationTag());
            psTemp.setString("keyCode", svm.getKeyCode());
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("site_Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
