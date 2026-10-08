package aarogya.selfToolkit.customMultiLine.server;

import aarogya.selfToolkit.stStatic.shared.STEnum.CustomMultiLineNature;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomMultiLine_Server {

    public static void delete(CustomMultiLineNature nature, String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery, column = "";
        try {
            switch (nature) {
                case DIAGNOSIS:
                    column = "diagnosis";
                    break;
                case FINAL_DIAGNOSIS:
                    column = "finalDiagnosis";
                    break;
                case HOSPITAL_SUMMARY:
                    column = "hospitalSummary";
                    break;
            }
            /*==============================<< Delete from patientPrescriptionDetail >>===============================*/
            sqlQuery = "update patientPrescriptionGUI set " + column + " = null where docId = :docId ";
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

    public static void save(CustomMultiLineNature nature, String remark, String docId, DmRSS rssTemp, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, column = "", param = "", guiConfigCode = null;
        int sno = 0;
        boolean blnAdd = false;
        try {

            switch (nature) {
                case HOSPITAL_SUMMARY:
                    column = "hospitalSummary";
                    param = ":" + column;
                    break;
                case DIAGNOSIS:
                    column = "diagnosis";
                    param = ":" + column;
                    break;
                case FINAL_DIAGNOSIS:
                    column = "finalDiagnosis";
                    param = ":" + column;
                    break;
            }

            sqlQuery = "    select count(ppGUI.docId) as cnt, max(pp.guiConfigCode) guiConfigCode "
                    + "         from patientPrescriptionGUI ppGUI"
                    + "         left join patientPrescription pp on ppGUI.docid=pp.docid "
                    + "         where ppGUI.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                blnAdd = DmShared.iNull(rsTemp.getLong("cnt")).longValue() == 0;
                guiConfigCode = DmShared.iNull(rsTemp.getString("guiConfigCode")).toString();
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "     select count(code)as  cnt from guiConfigDetail "
                    + "         where code = :configCode and customFieldName = :customField  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("configCode", DmShared.iNull(guiConfigCode).toString());
            psTemp.setString("customField", DmShared.xNull(nature.getValue()));
            rsTemp = psTemp.executeQuery();
            guiConfigCode = null;
            if (rsTemp.next()) {
                guiConfigCode = DmShared.iNull(rsTemp.getLong("cnt")).toString();
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(guiConfigCode).longValue() != 0) {
                if (blnAdd) {
                    sqlQuery = "insert into patientPrescriptionGUI(docId," + column + ") values(:docId, " + param + ")";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", docId);
                    psTemp.setNString(column, DmShared.xNull(remark));
                } else {
                    sqlQuery = "update patientPrescriptionGUI set " + column + " = " + param + " where docId = :docId";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", docId);
                    psTemp.setNString(column, DmShared.xNull(remark));
                }
                psTemp.executeUpdate();
                psTemp.close();
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

    public static String getCustomMultiLineRemark(CustomMultiLineNature nature, String docId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, rtnValue = "";

        try {
            /*====================================< Get Diagnosis Detail >====================================*/
            sqlQuery = "Select  ppg.hospitalSummary, ppg.finalDiagnosis, ppg.diagnosis "
                    + "         from patientPrescriptionGUI ppg "
                    + "         where ppg.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                switch (nature) {
                    case DIAGNOSIS:
                        rtnValue = DmShared.xNull(rsTemp.getString("diagnosis"));
                        break;
                    case FINAL_DIAGNOSIS:
                        rtnValue = DmShared.xNull(rsTemp.getString("finalDiagnosis"));
                        break;
                    case HOSPITAL_SUMMARY:
                        rtnValue = DmShared.xNull(rsTemp.getString("hospitalSummary"));
                        break;
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            rtnValue = "";
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
        return rtnValue;
    }
}
