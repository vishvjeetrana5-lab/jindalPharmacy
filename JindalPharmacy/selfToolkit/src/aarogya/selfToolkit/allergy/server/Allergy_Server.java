package aarogya.selfToolkit.allergy.server;

import aarogya.selfToolkit.allergy.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.AllergyNature;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Allergy_Server {

    public static void delete(String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*==============================<< Delete from patientAllergicToMedicine >>===============================*/
            sqlQuery = "Delete from patientAllergicToMedicine where docId = :docId ";
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

    public static void save(String docId, List<GrdAllergy_Mdl> listAllergy, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*==============================<< Delete from patientAllergicToMedicine >>===============================*/
            sqlQuery = "Delete from patientAllergicToMedicine where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert records into patientAllergicToMedicine >====================================*/
            if (listAllergy != null) {
                for (GrdAllergy_Mdl node : listAllergy) {
                    if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                        sqlQuery = "Insert into patientAllergicToMedicine(docId, medicineCompoCode, remark) "
                                + " values (:docId, :medicineCompoCode, :remark) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(docId).toString());
                        psTemp.setString("medicineCompoCode", DmShared.iNull(node.getSharedKeyFieldCode()).toString());
                        psTemp.setString("remark", DmShared.xNull(node.getRemark()).trim());
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

    public static List<GrdAllergy_Mdl> getAllergyDetail(String docId, Connection cnTemp) {
        List<GrdAllergy_Mdl> nodeList = new ArrayList<GrdAllergy_Mdl>();
        GrdAllergy_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get Allergy Detail >====================================*/
            sqlQuery = "select	patm.medicineCompoCode, mcm.name as medicineCompoName, mcm.nature, patm.remark "
                    + "		from patientAllergicToMedicine as patm "
                    + "		left join medicineCompoMast as mcm on patm.medicineCompoCode = mcm.code "
                    + "		where patm.docId = :docId "
                    + "		order by mcm.name, patm.medicineCompoCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAllergy_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("medicineCompoCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicineCompoName")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setNatureCode(DmShared.iNull(rsTemp.getString("nature")).toString());
                node.setNatureName(AllergyNature.getKeyOf(DmShared.iNull(rsTemp.getString("nature")).intValue()).getValue());
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
