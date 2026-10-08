package aarogya.master.packageRateList.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.packageRateList.shared.PackageRateList_Mdl;
import aarogya.master.packageRateList.shared.GrdPackageDetail_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.master.packageRateList.shared.PackageRateList_Srv;
import aarogya.master.packageRateList.shared.TxtHPackage_Mdl;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PackageRateList_SrvImpl extends DmRSSPrivate implements PackageRateList_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	om.subCode "
                    + "		from organizationMast as om "
                    + "		left join subGroup as sg on om.subCode = sg.subCode "
                    + "		order by sg.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();
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
    public DmResult<PackageRateList_Mdl> getMoveRec(String strKey) {
        DmResult<PackageRateList_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PackageRateList_Mdl mrTemp = new PackageRateList_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	om.subCode, sg.name as organizationName, om.shortName"
                    + "		from organizationMast as om "
                    + "		left join subGroup as sg on om.subCode = sg.subCode "
                    + "         where om.subCode = :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("organizationName")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(strKey).longValue() != 0) {
                /*==============================<< Get Package detail >>===============================*/
                mrTemp.getListPackageDetail().addAll(getPackageDetail(DmShared.iNull(strKey).toString(), cnTemp));
            }

            dmrTemp.setData(mrTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	om.subCode, sg.name as organizationName, om.shortName "
                    + "		from organizationMast as om "
                    + "		left join subGroup as sg on om.subCode = sg.subCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("organizationName")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("shortName")));
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

    public List<GrdPackageDetail_Mdl> getPackageDetail(String organizationCode, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        List<GrdPackageDetail_Mdl> lstNode = new ArrayList<GrdPackageDetail_Mdl>();
        GrdPackageDetail_Mdl node;

        try {
            /*==============================<< Get Package detail >>===============================*/
            sqlQuery = "Select	pm.code as packageCode, pm.name as packageName, pm.shortName, pm.nature, "
                    + "		pd.code, pd.organizationManualCode, pd.isActive, pd.fromDate, pd.toDate, pd.packageValue "
                    + "		from packageMast as pm "
                    + "         left join patientServiceSiteList psl on psl.code = pm.code "
                    + "		left join packageDetail as pd on pm.code = pd.packageCode and pd.site_Code = :site_Code "
                    + "                  and pd.organizationCode = :organizationCode and pd.roomCategoryCode is null "
                    + "		where pm.nature in (0,1) and psl.site_Code = :site_Code "
                    + "         order by pm.name, pd.fromDate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdPackageDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setPackageCode(DmShared.xNull(rsTemp.getString("packageCode")));
                node.setPackageName(DmShared.xNull(rsTemp.getString("packageName")));
                node.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                node.setNature(DmShared.iNull(rsTemp.getString("nature")).intValue() == 0 ? "Hospital"
                        : DmShared.iNull(rsTemp.getString("nature")).intValue() == 1 ? "Day Care" : "");

                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                    node.setOrgManualCode(DmShared.xNull(rsTemp.getString("organizationManualCode")));
                    node.setApplicableFrom(DmShared.dNull(rsTemp.getDate("fromDate")));
                    node.setApplicableTo(DmShared.dNull(rsTemp.getDate("toDate")));
                    node.setActive(DmShared.iNull(rsTemp.getString("isActive")).intValue() == 1 ? "Yes" : "No");
                    node.setAmount(DmServer.vNull(rsTemp.getString("packageValue"), "0.00"));
                    node.setApplicable(node.getApplicableFrom() + " to " + node.getApplicableTo());
                }

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

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
        return lstNode;
    }

    @Override
    public DmResult<String> checkApplicableDate(String fromDate, String toDate) {
        DmResult<String> dmrTemp = new DmResult();
        String applicableFromDate = "";
        dmrTemp.setData("");

        try {
            if ((!DmShared.dNull(fromDate).equalsIgnoreCase("")) && (!DmShared.dNull(toDate).equalsIgnoreCase(""))) {
                if ((DmShared.convertDate(toDate).getTime() >= (DmShared.convertDate("31-Dec-9999").getTime()))) {
                    throw new UnsupportedOperationException("Applicable To date can't be greater than or equal to 31-Dec-9999.");
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    Date date = formatter.parse(DmShared.dNull(toDate));
                    date = new Date(date.getTime() + (1000 * 60 * 60 * 24));

                    applicableFromDate = formatter.format(date);
                }
            }

            dmrTemp.setData(applicableFromDate);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<TxtHPackage_Mdl>> getTxtHPackage(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPackage_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPackage_Mdl> lstTemp = new ArrayList<TxtHPackage_Mdl>();
        TxtHPackage_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	pm.code, pm.name, pm.shortName, pm.nature, "
                    + "		case pm.nature when 0 then 'Hospital' when 1 then 'Day Care' end as natureName "
                    + "		from packageMast as pm "
                    + "         left join patientServiceSiteList psl on psl.code = pm.code "
                    + "         where pm.nature in (0,1) and psl.site_Code = :site_Code  ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TxtHPackage_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                node.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                node.setNatureName(DmShared.xNull(rsTemp.getString("natureName")));
                lstTemp.add(node);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
