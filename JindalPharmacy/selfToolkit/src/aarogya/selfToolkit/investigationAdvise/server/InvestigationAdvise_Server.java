package aarogya.selfToolkit.investigationAdvise.server;

import aarogya.selfToolkit.investigationAdvise.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestigationAdvise_Server {

    public static void delete(String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*==============================<< Delete from patientPrescriptionServiceAdv >>===============================*/
            sqlQuery = "Delete from patientPrescriptionServiceAdv where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.executeUpdate();
            psTemp.close();
        } catch (SQLException | DmException e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void save(String docId, List<GrdInvestigation_Mdl> listInvestigation, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        int sno = 0;

        try {
            /*==============================<< Delete from patientPrescriptionServiceAdv >>===============================*/
            sqlQuery = "Delete from patientPrescriptionServiceAdv where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert records into patientPrescriptionServiceAdv >====================================*/
            if (listInvestigation != null) {
                for (GrdInvestigation_Mdl node : listInvestigation) {
                    if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                        sqlQuery = "Insert into patientPrescriptionServiceAdv(docId, sNo, serviceCode, packageCode) "
                                + " values (:docId, :sNo, :serviceCode, :packageCode) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(docId).toString());
                        psTemp.setInt("sNo", ++sno);
                        if (node.isPkg()) {
                            psTemp.setString("serviceCode", null);
                            psTemp.setString("packageCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                        } else {
                            psTemp.setString("serviceCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                            psTemp.setString("packageCode", null);
                        }
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }
        } catch (SQLException | DmException e) {
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

    public static List<GrdInvestigation_Mdl> getInvestigationAdvise(String docId, Connection cnTemp) {
        List<GrdInvestigation_Mdl> nodeList = new ArrayList<GrdInvestigation_Mdl>();
        GrdInvestigation_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get Investigation Advise Detail >====================================*/
            sqlQuery = "Select	ppsa.serviceCode, psm.name as serviceName, psm.categoryCode, pscm.name as categoryName, "
                    + "         ppsa.packageCode, pm.name as packageName "
                    + "		from patientPrescriptionServiceAdv as ppsa "
                    + "		left join patientServiceMast as psm on ppsa.serviceCode = psm.code "
                    + "		left join patientServiceCatMast as pscm on psm.categoryCode = pscm.code "
                    + "		left join packageMast as pm on ppsa.packageCode = pm.code "
                    + "		where ppsa.docId = :docId "
                    + "		order by psm.name, pm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdInvestigation_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceName")));
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() == 0) {
                    node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("packageCode")));
                    node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("packageName")));
                    node.setPkg(true);
                }
                node.setCategoryCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                node.setCategoryName(DmShared.xNull(rsTemp.getString("categoryName")));
                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

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
        return nodeList;
    }

}
