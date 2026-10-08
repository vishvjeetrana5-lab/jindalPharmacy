package aarogya.selfToolkit.medicineAdvise.server;

import aarogya.selfToolkit.medicineAdvise.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineAdvise_Server {

    public static void delete(String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*==============================<< Delete from patientPrescriptionMedAdv >>===============================*/
            sqlQuery = "Delete from patientPrescriptionMedAdv where docId = :docId ";
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

    public static void save(String docId, List<GrdMedicineDetail_Mdl> listMedicine, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        int sNo = 0;

        try {
            /*==============================<< Delete from patientPrescriptionMedAdv >>===============================*/
            sqlQuery = "Delete from patientPrescriptionMedAdv where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert records into patientPrescriptionMedAdv >====================================*/
            if (listMedicine != null) {
                for (GrdMedicineDetail_Mdl node : listMedicine) {
                    if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                        sqlQuery = "insert into patientPrescriptionMedAdv(docId, sNo, itemCode, doseValue, doseText, doseDisplay, frequencyCode, days, "
                                + "         durationCode, remark, instructionCode) "
                                + " values (:docId, :sNo, :itemCode, :doseValue, :doseText, :doseDisplay, :frequencyCode, :days, "
                                + "         :durationCode, :remark, :instructionCode) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(docId).toString());
                        psTemp.setInt("sNo", ++sNo);
                        psTemp.setString("itemCode", DmShared.iNull(node.getSharedKeyFieldCode()).toString());
                        psTemp.setString("doseValue", DmServer.vNull(node.getNetDoseValue()));
                        psTemp.setString("doseText", DmShared.xNull(node.getDoseText()).trim());
                        psTemp.setNString("doseDisplay", DmShared.xNull(node.getDoseDisplayText()).trim());
                        psTemp.setString("frequencyCode", DmShared.foriegnKey(node.getFrequencyCode()));
                        psTemp.setString("days", DmShared.iNull(node.getDays()).toString());
                        psTemp.setString("durationCode", DmShared.iNull(node.getDurationCode()).toString());
                        psTemp.setString("remark", DmShared.xNull(node.getRemark()).trim());
                        psTemp.setString("instructionCode", DmShared.foriegnKey(node.getInstructionCode()));
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

    public static List<GrdMedicineDetail_Mdl> getMedicineDetail(String docId, Connection cnTemp) {
        List<GrdMedicineDetail_Mdl> nodeList = new ArrayList<GrdMedicineDetail_Mdl>();
        GrdMedicineDetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get Medicine Detail >====================================*/
            sqlQuery = "Select	ppma.itemCode as code, im.name, concat(im.displayName, ' ',ic.name) as displayName, im.compositionCode, mcm.name as compositionName, "
                    + "         ppma.frequencyCode, fm.frequencyName, fm.presentation, ppma.doseText, "
                    + "         ppma.doseDisplay, ppma.doseValue, ppma.days, ppma.durationCode, ppma.remark,"
                    + "         ppma.instructionCode, ins.name as instruction "
                    + "		from patientPrescriptionMedAdv as ppma "
                    + "		left join itemMast as im on ppma.itemCode = im.code "
                    + "		left join itemCat as ic on im.itemCategoryCode = ic.code "
                    + "		left join medicineCompoMast as mcm on im.compositionCode = mcm.code "
                    + "		left join frequencyMast as fm on ppma.frequencyCode = fm.code "
                    + "         left join prescriptionDetailMast ins on ins.code = ppma.instructionCode"
                    + "		where ppma.docId = :docId "
                    + "		order by ppma.sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicineDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setInstructionCode(DmShared.xNull(rsTemp.getString("instructionCode")));
                node.setInstructionName(DmShared.xNull(rsTemp.getString("instruction")));
                node.setCompositionCode(DmShared.xNull(rsTemp.getString("compositionCode")));
                node.setCompositionName(DmShared.xNull(rsTemp.getString("compositionName")));
                node.setFrequencyCode(DmShared.xNull(rsTemp.getString("frequencyCode")));
                node.setFrequencyName(DmShared.xNull(rsTemp.getString("frequencyName")));
                node.setDosePresentation(DmShared.xNull(rsTemp.getString("presentation")));
                node.setDoseValue(DmShared.xNull(rsTemp.getString("doseText")));
                node.setDoseText(DmShared.xNull(rsTemp.getString("doseText")));
                node.setDoseDisplayText(DmShared.xNull(rsTemp.getString("doseDisplay")));
                node.setNetDoseValue(DmShared.xNull(rsTemp.getString("doseValue")));
                node.setDays(DmShared.xNull(rsTemp.getString("days")));
                node.setDurationCode(DmShared.xNull(rsTemp.getString("durationCode")));
                node.setDurationName(DmShared.iNull(node.getDurationCode()).intValue() == 1 ? "Day (Next)"
                        : DmShared.iNull(node.getDurationCode()).intValue() == 2 ? "Week"
                        : DmShared.iNull(node.getDurationCode()).intValue() == 3 ? "Week (Next)"
                        : DmShared.iNull(node.getDurationCode()).intValue() == 4 ? "Month"
                        : DmShared.iNull(node.getDurationCode()).intValue() == 6 ? "Dose"
                        : DmShared.iNull(node.getDurationCode()).intValue() == 5 ? "Month (Next)" : "Day");
                node.setDuration(node.getDays() + " " + (node.getDurationName().equalsIgnoreCase("Day (Next)") ? "DN"
                        : node.getDurationName().equalsIgnoreCase("Week") ? "W"
                        : node.getDurationName().equalsIgnoreCase("Week (Next)") ? "WN"
                        : node.getDurationName().equalsIgnoreCase("Month") ? "M"
                        : node.getDurationName().equalsIgnoreCase("Dose") ? "Ds"
                        : node.getDurationName().equalsIgnoreCase("Month (Next)") ? "MN" : "D"));
                node.setFieldUniqueCode(String.valueOf(node.hashCode()));
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
