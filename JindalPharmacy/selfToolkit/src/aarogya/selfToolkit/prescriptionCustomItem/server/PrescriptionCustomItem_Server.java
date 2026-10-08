package aarogya.selfToolkit.prescriptionCustomItem.server;

import aarogya.selfToolkit.prescriptionCustomItem.shared.GrdPrescriptionCustomItem_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionCustomItem_Server {

    public static void delete(String nature, String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*==============================<< Delete from patientPrescriptionDetail >>===============================*/
            sqlQuery = "Delete from patientPrescriptionDetail where docId = :docId and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setString("nature", DmShared.xNull(nature));
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

    public static void save(String nature, String docId, List<GrdPrescriptionCustomItem_Mdl> listDiagnosis, DmRSS rssTemp, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        int sno = 0;

        try {
            /*==============================<< Delete from patientPrescriptionDetail >>===============================*/
            sqlQuery = "Delete from patientPrescriptionDetail where docId = :docId and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setString("nature", DmShared.xNull(nature));
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert records into patientPrescriptionDetail >====================================*/
            if (listDiagnosis != null) {
                for (GrdPrescriptionCustomItem_Mdl node : listDiagnosis) {
                    if ((DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) || (!DmShared.xNull(node.getRemark()).trim().equalsIgnoreCase(""))) {
                        sqlQuery = "Insert into patientPrescriptionDetail(docId, sNo, detailCode, nature, remark) "
                                + " values (:docId, :sNo, :detailCode, :nature, :remark) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(docId).toString());
                        psTemp.setInt("sNo", ++sno);
                        psTemp.setString("detailCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                        psTemp.setString("nature", DmShared.xNull(nature));
                        psTemp.setNString("remark", DmShared.xNull(node.getRemark()).trim());
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

    public static List<GrdPrescriptionCustomItem_Mdl> getPrescriptionCustomItemDetail(String nature, String docId, Connection cnTemp) {
        List<GrdPrescriptionCustomItem_Mdl> nodeList = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        GrdPrescriptionCustomItem_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get Diagnosis Detail >====================================*/
            sqlQuery = "Select  ppc.detailCode as code, pdm.name, pdm.shortName, ppc.remark, pdm.groupCode, pdgm.name as groupName "
                    + "         from patientPrescriptionDetail as ppc "
                    + "         left join prescriptionDetailMast as pdm on ppc.detailCode = pdm.code "
                    + "         left join prescriptionDetailGroupMast as pdgm on pdm.groupCode = pdgm.code "
                    + "         where ppc.docId = :docId and ppc.nature = :nature "
                    + "         order by ppc.sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setString("nature", DmShared.xNull(nature));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdPrescriptionCustomItem_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                node.setGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                node.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
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
