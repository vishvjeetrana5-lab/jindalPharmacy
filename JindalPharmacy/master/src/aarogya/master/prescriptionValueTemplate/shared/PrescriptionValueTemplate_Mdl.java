package aarogya.master.prescriptionValueTemplate.shared;

import aarogya.selfToolkit.allergy.shared.GrdAllergy_Mdl;
import aarogya.selfToolkit.prescriptionCustomItem.shared.GrdPrescriptionCustomItem_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionValueTemplate_Mdl extends KeyField implements Serializable, IsSerializable {

    private String configCode;
    private String configName;
    private String consultantCode;
    private String consultantName;
    private List<KeyFieldExtended> listTemplateName = new ArrayList<KeyFieldExtended>();
    private List<GrdAllergy_Mdl> lstAllergy = new ArrayList<GrdAllergy_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstComplaint = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstComorbid = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstInstruction = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstFamilyHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstHabit = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstHistoryOfPastIllness = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstHistoryOfPresentIllness = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstPhysicalExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstSystemicExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstPrecaution = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstUrgentCare = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstPersonalHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstObstetricsHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstMenstrualHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstDevelopmentHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstPerinatalHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstImmunizationHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstFeedingHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstObstetricExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstGynaecologicalExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private List<GrdPrescriptionCustomItem_Mdl> lstMenstrualExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private DmGUI_Mdl gui_Mdl;

    public PrescriptionValueTemplate_Mdl() {
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public List<KeyFieldExtended> getListTemplateName() {
        return listTemplateName;
    }

    public List<GrdAllergy_Mdl> getLstAllergy() {
        return lstAllergy;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstComplaint() {
        return lstComplaint;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstComorbid() {
        return lstComorbid;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstInstruction() {
        return lstInstruction;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstFamilyHistory() {
        return lstFamilyHistory;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstHabit() {
        return lstHabit;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstHistoryOfPastIllness() {
        return lstHistoryOfPastIllness;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstHistoryOfPresentIllness() {
        return lstHistoryOfPresentIllness;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstPhysicalExamination() {
        return lstPhysicalExamination;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstSystemicExamination() {
        return lstSystemicExamination;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstPrecaution() {
        return lstPrecaution;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstUrgentCare() {
        return lstUrgentCare;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstPersonalHistory() {
        return lstPersonalHistory;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstObstetricsHistory() {
        return lstObstetricsHistory;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstMenstrualHistory() {
        return lstMenstrualHistory;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstDevelopmentHistory() {
        return lstDevelopmentHistory;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstPerinatalHistory() {
        return lstPerinatalHistory;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstImmunizationHistory() {
        return lstImmunizationHistory;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstFeedingHistory() {
        return lstFeedingHistory;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstObstetricExamination() {
        return lstObstetricExamination;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstGynaecologicalExamination() {
        return lstGynaecologicalExamination;
    }

    public List<GrdPrescriptionCustomItem_Mdl> getLstMenstrualExamination() {
        return lstMenstrualExamination;
    }

    public DmGUI_Mdl getGui_Mdl() {
        return gui_Mdl;
    }

    public void setGui_Mdl(DmGUI_Mdl gui_Mdl) {
        this.gui_Mdl = gui_Mdl;
    }

}
