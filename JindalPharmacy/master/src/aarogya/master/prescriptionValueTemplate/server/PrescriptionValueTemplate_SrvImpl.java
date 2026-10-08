package aarogya.master.prescriptionValueTemplate.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.prescriptionValueTemplate.shared.*;
import aarogya.selfToolkit.allergy.server.Allergy_Server;
import aarogya.selfToolkit.prescriptionCustomItem.server.PrescriptionCustomItem_Server;
import aarogya.selfToolkit.stStatic.shared.STEnum.CustomWidgetNature;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionValueTemplate_SrvImpl extends DmRSSPrivate implements PrescriptionValueTemplate_Srv {

    private final String vCat = "GUITV";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        String sqlQuery;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select docId from guiValueTemplateMast where nature is null order by name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
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
    public DmResult<PrescriptionValueTemplate_Mdl> getMoveRec(String docId) {
        DmResult<PrescriptionValueTemplate_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PrescriptionValueTemplate_Mdl mrTemp = new PrescriptionValueTemplate_Mdl();
        String sqlQuery = "";
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	vtm.docId, vtm.name, vtm.guiConfigCode, gc.name as guiConfigName, "
                    + "		vtm.consultantCode, con.name as consultantName, vtm.preparedBy, vtm.u_Name "
                    + "		from guiValueTemplateMast as vtm "
                    + "		left join patientPrescription as pp on vtm.docId = pp.docId "
                    + "		left join guiConfig as gc on vtm.guiConfigCode = gc.code "
                    + "		left join subGroup as con ON vtm.consultantCode = con.subCode "
                    + "		where vtm.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                mrTemp.setConfigName(DmShared.xNull(rsTemp.getString("guiConfigName")));
                mrTemp.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
            }
            psTemp.close();
            rsTemp.close();

            getGUIData(mrTemp, docId, cnTemp);

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
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

    private void getGUIData(PrescriptionValueTemplate_Mdl mrTemp, String docId, Connection cnTemp) {
        if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() != 0l) {
            /*====================================< Get Allergy Data >====================================*/
            mrTemp.getLstAllergy().addAll(Allergy_Server.getAllergyDetail(mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Complaints Data >====================================*/
            mrTemp.getLstComplaint().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.COMPLAINTS.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Comorbid Data >====================================*/
            mrTemp.getLstComorbid().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.COMORBID.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get INSTRUCTION Data >====================================*/
            mrTemp.getLstInstruction().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.INSTRUCTION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Family History Data >====================================*/
            mrTemp.getLstFamilyHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.FAMILY_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Habit Data >====================================*/
            mrTemp.getLstHabit().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.HABIT.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get History Of Past Illness Data >====================================*/
            mrTemp.getLstHistoryOfPastIllness().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.HISTORY_OF_PAST_ILLNESS.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get History Of Present Illness Data >====================================*/
            mrTemp.getLstHistoryOfPresentIllness().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.HISTORY_OF_PRESENT_ILLNESS.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Physical Examination Data >====================================*/
            mrTemp.getLstPhysicalExamination().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.PHYSICAL_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Systemic Examination Data >====================================*/
            mrTemp.getLstSystemicExamination().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.SYSTEMIC_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Precaution Data >====================================*/
            mrTemp.getLstPrecaution().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.PRECAUTION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Urgent Care Data >====================================*/
            mrTemp.getLstUrgentCare().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.URGENT_CARE.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Personal History Data >====================================*/
            mrTemp.getLstPersonalHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.PERSONAL_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Obstetrics History Data >====================================*/
            mrTemp.getLstObstetricsHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.OBSTETRICS_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Menstrual History Data >====================================*/
            mrTemp.getLstMenstrualHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.MENSTRUAL_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Development History Data >====================================*/
            mrTemp.getLstDevelopmentHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.DEVELOPMENT_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Perinatal History Data >====================================*/
            mrTemp.getLstPerinatalHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.PERINATAL_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Immunization History Data >====================================*/
            mrTemp.getLstImmunizationHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.IMMUNIZATION_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Feeding History Data >====================================*/
            mrTemp.getLstFeedingHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.FEEDING_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Obstetric Examination Data >====================================*/
            mrTemp.getLstObstetricExamination().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.OBSTETRIC_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Gynaecological Examination Data >====================================*/
            mrTemp.getLstGynaecologicalExamination().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.GYNAECOLOGICAL_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Menstrual Examination Data >====================================*/
            mrTemp.getLstMenstrualExamination().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.MENSTRUAL_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));

            /*====================================< Get Value Templates Details >====================================*/
            mrTemp.getListTemplateName().addAll(getValueTemplates(DmShared.iNull(mrTemp.getConfigCode()).toString(), DmShared.iNull(mrTemp.getConsultantCode()).toString(), cnTemp));

            /*====================================< Get GUI Details >====================================*/
            mrTemp.setGui_Mdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getConfigCode(), docId, DmServer.getSsnSiteCode(this), cnTemp));
        }
    }

    @Override
    public DmResult<String> deleteRecord(String guiCode, String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            DmGUI_Server.deleteViaConfigCode(guiCode, strKey, DmServer.getSsnSiteCode(this), cnTemp);

            /*====================================< Delete Allergy Data >====================================*/
            Allergy_Server.delete(strKey, cnTemp);

            /*====================================< Delete Complaints Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.COMPLAINTS.getId(), strKey, cnTemp);

            /*====================================< Delete Comorbid Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.COMORBID.getId(), strKey, cnTemp);

            /*====================================< Delete Instruction Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.INSTRUCTION.getId(), strKey, cnTemp);

            /*====================================< Delete Family History Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.FAMILY_HISTORY.getId(), strKey, cnTemp);

            /*====================================< Delete Habit Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.HABIT.getId(), strKey, cnTemp);

            /*====================================< Delete History Of Past Illness Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.HISTORY_OF_PAST_ILLNESS.getId(), strKey, cnTemp);

            /*====================================< Delete History Of Present Illness Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.HISTORY_OF_PRESENT_ILLNESS.getId(), strKey, cnTemp);

            /*====================================< Delete Physical Examination Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.PHYSICAL_EXAMINATION.getId(), strKey, cnTemp);

            /*====================================< Delete Systemic Examination Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.SYSTEMIC_EXAMINATION.getId(), strKey, cnTemp);

            /*====================================< Delete Precaution Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.PRECAUTION.getId(), strKey, cnTemp);

            /*====================================< Delete Urgent Care Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.URGENT_CARE.getId(), strKey, cnTemp);

            /*====================================< Delete Personal History Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.PERSONAL_HISTORY.getId(), strKey, cnTemp);

            /*====================================< Delete Obstetrics History Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.OBSTETRICS_HISTORY.getId(), strKey, cnTemp);

            /*====================================< Delete Menstrual History Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.MENSTRUAL_HISTORY.getId(), strKey, cnTemp);

            /*====================================< Delete Development History Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.DEVELOPMENT_HISTORY.getId(), strKey, cnTemp);

            /*====================================< Delete Perinatal History Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.PERINATAL_HISTORY.getId(), strKey, cnTemp);

            /*====================================< Delete Immunization History Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.IMMUNIZATION_HISTORY.getId(), strKey, cnTemp);

            /*====================================< Delete Feeding History Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.FEEDING_HISTORY.getId(), strKey, cnTemp);

            /*====================================< Delete Obstetric Examination Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.OBSTETRIC_EXAMINATION.getId(), strKey, cnTemp);

            /*====================================< Delete Gynaecological Examination Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.GYNAECOLOGICAL_EXAMINATION.getId(), strKey, cnTemp);

            /*====================================< Delete Menstrual Examination Data >====================================*/
            PrescriptionCustomItem_Server.delete(CustomWidgetNature.MENSTRUAL_EXAMINATION.getId(), strKey, cnTemp);

            /*============================< Delete records from patientPrescription >==============================*/
            sqlQuery = "Delete From patientPrescription Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*============================< Delete records from guiValueTemplateMast >==============================*/
            sqlQuery = "Delete From guiValueTemplateMast Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
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
    public DmResult<String> saveRecord(PrescriptionValueTemplate_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName;
        dmrTemp.setData("");

        try {
            PrescriptionValueTemplate_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0l) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(DmServer.getVType(vCat, cnTemp)), voucherPrefix, cnTemp, this));
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0l) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==========================< Check whether Name & GUI & Consultant already exist or not >============================*/
            sqlQuery = "Select  docId from guiValueTemplateMast "
                    + "         where name = :name and guiConfigCode = :guiConfigCode and consultantCode = :consultantCode and docId <> :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
            psTemp.setString("guiConfigCode", DmShared.iNull(mrTemp.getConfigCode()).toString());
            psTemp.setString("consultantCode", DmShared.iNull(mrTemp.getConsultantCode()).toString());
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException(DmShared.xNull(mrTemp.getConfigName()) + " and " + DmShared.xNull(mrTemp.getSharedKeyFieldName()) + " already exists.");
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Delete GUI Data >====================================*/
            DmGUI_Server.deleteViaConfigCode(mrTemp.getConfigCode(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnSiteCode(this), cnTemp);

            /*====================================< Insert/Update into guiValueTemplateMast >====================================*/
            if (blnAdd) {
                sqlQuery = "Insert into guiValueTemplateMast (docId, name, consultantCode, guiConfigCode, preparedBy, u_EntDt, transferDt) "
                        + " values (:docId, :name, :consultantCode, :guiConfigCode, :preparedBy, :u_EntDt, :transferDt)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getConfigCode()));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
            } else {
                sqlQuery = "Update guiValueTemplateMast set "
                        + " name = :name, "
                        + " consultantCode = :consultantCode, "
                        + " guiConfigCode = :guiConfigCode, "
                        + " u_EntDt = :u_EntDt, "
                        + " u_Name = :u_Name, "
                        + " transferDt = :transferDt "
                        + " where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getConfigCode()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert/Update into patientPrescription >====================================*/
            if (blnAdd) {
                sqlQuery = "Insert into patientPrescription (docId, consultantCode, guiConfigCode, preparedBy, site_Code, u_EntDt, transferDt) "
                        + " values (:docId, :consultantCode, :guiConfigCode, :preparedBy, :site_Code, :u_EntDt, :transferDt)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getConfigCode()));
                psTemp.setString("preparedBy", userName);
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
            } else {
                sqlQuery = "Update patientPrescription set "
                        + " consultantCode = :consultantCode, "
                        + " guiConfigCode = :guiConfigCode, "
                        + " u_EntDt = :u_EntDt, "
                        + " u_Name = :u_Name, "
                        + " transferDt = :transferDt "
                        + " where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getConfigCode()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Save Allergy Data >====================================*/
            Allergy_Server.save(mrTemp.getSharedKeyFieldCode(), mrTemp.getLstAllergy(), cnTemp);

            /*====================================< Save Complaints Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.COMPLAINTS.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstComplaint(), this, cnTemp);

            /*====================================< Save Comorbid Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.COMORBID.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstComorbid(), this, cnTemp);

            /*====================================< Save Instruction Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.INSTRUCTION.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstInstruction(), this, cnTemp);

            /*====================================< Save Family History Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.FAMILY_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstFamilyHistory(), this, cnTemp);

            /*====================================< Save Habit Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.HABIT.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstHabit(), this, cnTemp);

            /*====================================< Save History Of Past Illness Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.HISTORY_OF_PAST_ILLNESS.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstHistoryOfPastIllness(), this, cnTemp);

            /*====================================< Save History Of Present Illness Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.HISTORY_OF_PRESENT_ILLNESS.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstHistoryOfPresentIllness(), this, cnTemp);

            /*====================================< Save Physical Examination Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.PHYSICAL_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstPhysicalExamination(), this, cnTemp);

            /*====================================< Save Systemic Examination Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.SYSTEMIC_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstSystemicExamination(), this, cnTemp);

            /*====================================< Save Precaution Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.PRECAUTION.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstPrecaution(), this, cnTemp);

            /*====================================< Save Urgent Care Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.URGENT_CARE.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstUrgentCare(), this, cnTemp);

            /*====================================< Save Personal History Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.PERSONAL_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstPersonalHistory(), this, cnTemp);

            /*====================================< Save Obstetrics History Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.OBSTETRICS_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstObstetricsHistory(), this, cnTemp);

            /*====================================< Save Menstrual History Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.MENSTRUAL_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstMenstrualHistory(), this, cnTemp);

            /*====================================< Save Development History Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.DEVELOPMENT_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstDevelopmentHistory(), this, cnTemp);

            /*====================================< Save Perinatal History Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.PERINATAL_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstPerinatalHistory(), this, cnTemp);

            /*====================================< Save Immunization History Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.IMMUNIZATION_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstImmunizationHistory(), this, cnTemp);

            /*====================================< Save Feeding History Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.FEEDING_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstFeedingHistory(), this, cnTemp);

            /*====================================< Save Obstetric Examination Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.OBSTETRIC_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstObstetricExamination(), this, cnTemp);

            /*====================================< Save Gynaecological Examination Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.GYNAECOLOGICAL_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstGynaecologicalExamination(), this, cnTemp);

            /*====================================< Save Menstrual Examination Data >====================================*/
            PrescriptionCustomItem_Server.save(CustomWidgetNature.MENSTRUAL_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), mrTemp.getLstMenstrualExamination(), this, cnTemp);

            /*====================================< Save GUI Data >====================================*/
            DmGUI_Server.saveViaConfigCode(mrTemp.getGui_Mdl().getGuiRowList(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnSiteCode(this), cnTemp);

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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<Find_Mdl> listNode = new ArrayList<>();
        Find_Mdl node;
        String sqlQuery;
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	vtm.docId, vtm.name, gc.name as guiConfigName, con.name as consultantName "
                    + "		from guiValueTemplateMast as vtm "
                    + "		left join guiConfig as gc on vtm.guiConfigCode = gc.code "
                    + "		left join subGroup as con ON vtm.consultantCode = con.subCode "
                    + "         where vtm.nature is null ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new Find_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setGuiName(DmShared.xNull(rsTemp.getString("guiConfigName")));
                node.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            pageing.positioning(listNode.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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

    @Override
    public DmResultPageing<List<String>> getSuggestionName(String guiConfigCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<String> data = new ArrayList<>();
        rstTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select name from guiValueTemplateMast where guiConfigCode = :guiConfigCode ");
            helper.addSort("name", DmEnum.DmSortType.ASC);
            sqlWithParam.setString("guiConfigCode", DmShared.iNull(guiConfigCode).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(data.size(), blnToEndOfFile);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGUITemplate(String guiTemplate, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            sqlQuery = "Select  max(gc.code) as code, max(gc.name) as name "
//                    + "         from guiConfig gc "
//                    + "         left join guiConfigApplicableTo gca on gca.code = gc.code "
//                    + "         where gca.guiTemplateCode = :guiTemplateCode "
//                    + "         group by gc.code ";
            sqlQuery = "Select  max(gc.code) as code, max(gc.name) as name "
                    + "         from guiConfig gc "
                    + "         group by gc.code ";
            sqlWithParam.setSQL(sqlQuery);
//            sqlWithParam.setString("guiTemplateCode", DmShared.iNull(guiTemplate).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> listNode = new ArrayList<>();
        KeyFieldExtended node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  cm.subcode as code, sg.name, csm.guiTemplateCodePost "
                    + "         from consultantMast as cm "
                    + "         left join subGroup as sg ON cm.subCode = sg.subCode "
                    + "         left join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + "         left join subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + "         left join consultantSpeciMast csm on csm.code = cm.consultantSpeciCode "
                    + "         where csl.site_Code = :site_Code and sg.isActive = 1 and sgn.manualCode = 'CN' ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("guiTemplateCodePost")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(listNode);
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
    public DmResultPageing<List<TxtHCopyGUI_Mdl>> getTxtHCopyGUI(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCopyGUI_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHCopyGUI_Mdl> listNode = new ArrayList<TxtHCopyGUI_Mdl>();
        TxtHCopyGUI_Mdl node;
        String sqlQuery;
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	vtm.docId, vtm.name, vtm.consultantCode, con.name as consultantName, vtm.guiConfigCode, guic.name as guiConfigName "
                    + "		from guiValueTemplateMast as vtm "
                    + "		left join subGroup as con on vtm.consultantCode = con.subCode "
                    + "         left join guiConfig as guic on vtm.guiConfigCode = guic.code ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TxtHCopyGUI_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                node.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                node.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                node.setGuiConfigName(DmShared.xNull(rsTemp.getString("guiConfigName")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
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

    @Override
    public DmResult<List<KeyFieldExtended>> getTemplateName(String guiCode, String consultantCode) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> listNode = new ArrayList<>();
        Connection cnTemp = null;
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Get Value Templates Details >====================================*/
            listNode.addAll(getValueTemplates(DmShared.iNull(guiCode).toString(), DmShared.iNull(consultantCode).toString(), cnTemp));

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private List<KeyFieldExtended> getValueTemplates(String guiConfigCode, String consultantCode, Connection cnTemp) {
        List<KeyFieldExtended> listNode = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            sqlQuery = "Select  vtm.docId, vtm.name "
                    + "         from guiValueTemplateMast as vtm "
                    + "         where vtm.guiConfigCode = :guiConfigCode and vtm.consultantCode = :consultantCode "
                    + "         order by vtm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("guiConfigCode", DmShared.iNull(guiConfigCode).toString());
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.add(node);
            }
            psTemp.close();
            rsTemp.close();
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
        return listNode;
    }

    @Override
    public DmResult<PrescriptionValueTemplate_Mdl> getCopyPrescriptionCustomData(String docId, List<String> listCustomWidgetToCopy) {
        DmResult<PrescriptionValueTemplate_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        PrescriptionValueTemplate_Mdl mrTemp = new PrescriptionValueTemplate_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setSharedKeyFieldCode(docId);
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() != 0l) {

                if (listCustomWidgetToCopy != null) {
                    for (String customWidget : listCustomWidgetToCopy) {
                        if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Allergy")) {
                            /*====================================< Get Allergy Data >====================================*/
                            mrTemp.getLstAllergy().addAll(Allergy_Server.getAllergyDetail(mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Complaints")) {
                            /*====================================< Get Complaints Data >====================================*/
                            mrTemp.getLstComplaint().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.COMPLAINTS.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Comorbid")) {
                            /*====================================< Get Comorbid Data >====================================*/
                            mrTemp.getLstComorbid().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.COMORBID.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Family History")) {
                            /*====================================< Get Family History Data >====================================*/
                            mrTemp.getLstFamilyHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.FAMILY_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Habit")) {
                            /*====================================< Get Habit Data >====================================*/
                            mrTemp.getLstHabit().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.HABIT.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("History Of Past Illness")) {
                            /*====================================< Get History Of Past Illness Data >====================================*/
                            mrTemp.getLstHistoryOfPastIllness().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.HISTORY_OF_PAST_ILLNESS.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("History Of Present Illness")) {
                            /*====================================< Get History Of Present Illness Data >====================================*/
                            mrTemp.getLstHistoryOfPresentIllness().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.HISTORY_OF_PRESENT_ILLNESS.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Instruction")) {
                            /*====================================< Get Instruction Data >====================================*/
                            mrTemp.getLstInstruction().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.INSTRUCTION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Physical Examination")) {
                            /*====================================< Get Physical Examination Data >====================================*/
                            mrTemp.getLstPhysicalExamination().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.PHYSICAL_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Systemic Examination")) {
                            /*====================================< Get Systemic Examination Data >====================================*/
                            mrTemp.getLstSystemicExamination().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.SYSTEMIC_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Precaution")) {
                            /*====================================< Get Precaution Data >====================================*/
                            mrTemp.getLstPrecaution().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.PRECAUTION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Urgent Care")) {
                            /*====================================< Get Urgent Care Data >====================================*/
                            mrTemp.getLstUrgentCare().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.URGENT_CARE.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Personal History")) {
                            /*====================================< Get Personal History Data >====================================*/
                            mrTemp.getLstPersonalHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.PERSONAL_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Obstetrics History")) {
                            /*====================================< Get Obstetrics History Data >====================================*/
                            mrTemp.getLstObstetricsHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.OBSTETRICS_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Menstrual History")) {
                            /*====================================< Get Menstrual History Data >====================================*/
                            mrTemp.getLstMenstrualHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.MENSTRUAL_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Development History")) {
                            /*====================================< Get Development History Data >====================================*/
                            mrTemp.getLstDevelopmentHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.DEVELOPMENT_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Perinatal History")) {
                            /*====================================< Get Perinatal History Data >====================================*/
                            mrTemp.getLstPerinatalHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.PERINATAL_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Immunization History")) {
                            /*====================================< Get Immunization History Data >====================================*/
                            mrTemp.getLstImmunizationHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.IMMUNIZATION_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Feeding History")) {
                            /*====================================< Get Feeding History Data >====================================*/
                            mrTemp.getLstFeedingHistory().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.FEEDING_HISTORY.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Obstetric Examination")) {
                            /*====================================< Get Obstetric Examination Data >====================================*/
                            mrTemp.getLstObstetricExamination().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.OBSTETRIC_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Gynaecological Examination")) {
                            /*====================================< Get Gynaecological Examination Data >====================================*/
                            mrTemp.getLstGynaecologicalExamination().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.GYNAECOLOGICAL_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Menstrual Examination")) {
                            /*====================================< Get Menstrual Examination Data >====================================*/
                            mrTemp.getLstMenstrualExamination().addAll(PrescriptionCustomItem_Server.getPrescriptionCustomItemDetail(CustomWidgetNature.MENSTRUAL_EXAMINATION.getId(), mrTemp.getSharedKeyFieldCode(), cnTemp));
                        }
                    }
                }
            } else {
                mrTemp = null;
            }

            cnTemp.close();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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
    public DmResult<DmGUI_Mdl> getInitializeGUI(String guiCode) {
        DmResult<DmGUI_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmGUI_Mdl mrTemp = new DmGUI_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Get GUI Details >====================================*/
            mrTemp = DmGUI_Server.getGUIViaConfigCode(guiCode, "", DmServer.getSsnSiteCode(this), cnTemp);

            cnTemp.close();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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
    public DmResult<PrescriptionValueTemplate_Mdl> getRecordViaAction(String consultantCode, String guiConfigCode) {
        DmResult<PrescriptionValueTemplate_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PrescriptionValueTemplate_Mdl mrTemp = new PrescriptionValueTemplate_Mdl();
        String sqlQuery, guiTemplateCode = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  gcat.guiTemplateCode "
                    + "		from guiConfigApplicableTo gcat "
                    + "		where code = :guiConfigCode and site_code = :siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("guiConfigCode", guiConfigCode);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                guiTemplateCode = DmShared.iNull(rsTemp.getString("guiTemplateCode")).toString();
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  cm.subCode, sg.name as consultant, gtm.name as guiTemplate, :guiTemplateCode as guiTemplateCode"
                    + "		from consultantMast cm "
                    + "		left join subGroup sg on sg.subCode = cm.subCode "
                    + "		left join guiTemplateMast gtm on gtm.code = :guiTemplateCode "
                    + "		where cm.subCode = :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", consultantCode);
            psTemp.setString("guiTemplateCode", guiTemplateCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setConsultantCode(consultantCode);
                mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultant")));
    //=====================< 22-10-2021 >========================================
                mrTemp.setConfigCode(DmShared.xNull(guiConfigCode));
     //         mrTemp.setConfigCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
    //===========================================================================================
                mrTemp.setConfigName(DmShared.xNull(rsTemp.getString("guiTemplate")));
            }
            cnTemp.close();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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
}
