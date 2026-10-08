package aarogya.nabh.self.shared;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NABH_Shared {

    public static NABHIndicators_Mdl getNABHIndicators() throws Exception {
//        List<NABHIndicators_Mdl> nabhIndicatorsGroup = new ArrayList<>();
        NABHIndicators_Mdl nabhIndicators = new NABHIndicators_Mdl();

        Map<String, String> cells = new LinkedHashMap<>();
        NABH_Column c = new NABH_Column();

        cells.put(c.menu, "Indicator 1");
        cells.put(c.qi, "1");
        cells.put(c.std, "CQI 3(a)");
        cells.put(c.bo, "Time of Initial Assessment by Doctor \n(Wards / ER)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of Time taken for the assessment\n"
                + "_______________________________\n"
                + "Total No. of Patients in Ward");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 2");
        cells.put(c.qi, "2");
        cells.put(c.std, "CQI 3(a)");
        cells.put(c.bo, "Percentage of cases (IPD)  wherein care \n"
                + " plan with desired outcomes is \n"
                + " Documented & Countersigned");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of IP cases wherein the care plan with \n"
                + "desired outcomes has been documented \n& Countersigned\n"
                + "----------------------------------------  X 100\n"
                + "Total No. of Patients ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 3");
        cells.put(c.qi, "3");
        cells.put(c.std, "CQI 3(a)");
        cells.put(c.bo, "Percentage of cases (IPD) wherein \n"
                + "screening for nutritional needs has \n"
                + "been done.");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of IP case record wherein the \n"
                + "nutritional assessment has been \n"
                + "documented\n"
                + "------------------------------------- X  100\n"
                + "Total No. of Patients");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 4");
        cells.put(c.qi, "4");
        cells.put(c.std, "CQI 3(a)");
        cells.put(c.bo, "Percentage of cases (IP) wherein the \n"
                + "nursing care is documented.");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of IP case record wherein the nursing \n"
                + "care plan has been documented\n"
                + "------------------------------------- X  100\n"
                + "Total No. of Patients");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);
//        SubSectionMergeConfiguration config = new SubSectionMergeConfiguration(2, 2, 2, 6);
//        subSection.getMergeConfigurations().add(config);
//        nabhIndicatorsGroup.add(nabhIndicators);

//        subSection = new RptFileReceive_SubSec();
//        subSection.setSubSectionName("Laboratory");
        //  config = new SubSectionMergeConfiguration(3, 3, 8, 11);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(2, 2, 8, 11);
        //subSection.getMergeConfigurations().add(config);
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 5");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations \n "
                + "(Microbiology / Biochemistry \n"
                + "Pathology / Serology / \n"
                + "CT / MRI / USG / Mamograpgy).");
        cells.put(c.blank1, "Pre - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        //config = new SubSectionMergeConfiguration(3, 3, 12, 15);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(2, 2, 12, 15);
        // subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(3, 3, 16, 19);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(2, 2, 16, 19);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(3, 3, 20, 23);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(2, 2, 20, 23);
        //subSection.getMergeConfigurations().add(config);
//        nabhIndicatorsGroup.add(nabhIndicators);
//        subSection = new RptFileReceive_SubSec();
//        subSection.setSubSectionName("Radiology");
        //config = new SubSectionMergeConfiguration(3, 3, 26, 29);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(2, 2, 26, 29);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(3, 3, 30, 33);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(3, 3, 34, 37);
        //subSection.getMergeConfigurations().add(config);
//============================================================================================================================
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 6");
        cells.put(c.qi, "6");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Rate of Re - Dos.(Include Tests \n"
                + " repeated Before release of Results \n "
                + " for confirmation)");
        cells.put(c.blank1, "LABORATORY");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Re - Dos \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

//        nabhIndicatorsGroup.add(nabhIndicators);
//        subSection = new RptFileReceive_SubSec();
//        subSection.setSubSectionName("LABORATORY");
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 7");
        cells.put(c.qi, "7");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Percentage of Reports Co - relating \n"
                + " with clinical diagnosis ");
        cells.put(c.blank1, "HISTOPATHOLOGY");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Reports Co-relating with clinical \n"
                + " diagnosis \n "
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

//        nabhIndicatorsGroup.add(nabhIndicators);
//=======================================================================================
//        subSection = new RptFileReceive_SubSec();
//        subSection.setSubSectionName("RADIOLOGY");
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 8");
        cells.put(c.qi, "8");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Percentage of adherence to safety \n"
                + " precautions by employee woring in \n"
                + " Diagnostics ");
        cells.put(c.blank1, "LABORATORY");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Employees Adhering to safety  \n"
                + " Precautions  \n "
                + "-----------------------------------  X 100\n"
                + "No. of Employees Sampled ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 9");
        cells.put(c.qi, "9");
        cells.put(c.std, "CQI (3c)");
        cells.put(c.bo, "Incidence of Medication Errors (Per \n"
                + " 1000 Patient Days) ");
        cells.put(c.blank1, "Prescription Error");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Prescription Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Patient Days ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 10");
        cells.put(c.qi, "10");
        cells.put(c.std, "CQI (3c)");
        cells.put(c.bo, "Percentage of Admissions with  \n"
                + " Adverse Drug Reactions ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Adverse Drug Reactions \n"
                + "-----------------------------------  X 100\n"
                + "No. of Discharges and Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 11");
        cells.put(c.qi, "11");
        cells.put(c.std, "CQI (3c)");
        cells.put(c.bo, "Percentage of Medication Charts  \n"
                + " with Error Prone abbreviations");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of medication charts with error \n"
                + " prone abbreviations \n"
                + "-----------------------------------  X 100\n"
                + " No. of Medication Charts Reviewed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 12");
        cells.put(c.qi, "12");
        cells.put(c.std, "CQI (3c)");
        cells.put(c.bo, "Percentage of patients receiving High \n"
                + " Risk Medications developing ADR \n"
                + " (Adverse Drug Reaction)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Patients Receiving High Risk \n"
                + " Medications who have an ADR \n"
                + " -----------------------------------  X 100\n"
                + " No. of patients receiving High Risk \n"
                + " Medications");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 13");
        cells.put(c.qi, "13");
        cells.put(c.std, "CQI (3d)");
        cells.put(c.bo, "Percentage of Modification of  \n"
                + " Anesthesia Plans ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of patients in whome the Anesthesia\n"
                + " plan was modified \n"
                + " -----------------------------------  X 100\n"
                + " No. of patients who underwent  \n"
                + " Anesthesia");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 14");
        cells.put(c.qi, "14");
        cells.put(c.std, "CQI (3d)");
        cells.put(c.bo, "Percentage of Unplanned  \n"
                + " Ventilation following anesthesia ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of patients requiring unplanned \n"
                + " ventilation following anaesthesia \n"
                + " -----------------------------------  X 100\n"
                + " No. of Patients who underwent  \n"
                + " Anesthesia");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 15");
        cells.put(c.qi, "15");
        cells.put(c.std, "CQI (3d)");
        cells.put(c.bo, "Percentage of Adverse Anesthesia \n"
                + " Events");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of Patients who develop adverse  \n"
                + " anesthesia event\n"
                + " -----------------------------------  X 100\n"
                + " No. of patients who underwent \n"
                + " anaesthesia");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 16");
        cells.put(c.qi, "16");
        cells.put(c.std, "CQI (3d)");
        cells.put(c.bo, "Anaesthesia Related Mortality Rate");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of patients who died due to \n"
                + " anesthesia \n"
                + " -----------------------------------  X 100\n"
                + " No. of Patients who underwent \n"
                + " Anaesthesia");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 17");
        cells.put(c.qi, "17");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Percentage of Unplanned Return to \n"
                + " OT  ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Unplanned return to OT \n"
                + " -----------------------------------  X 100\n"
                + " No. of Patients Operated ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 18");
        cells.put(c.qi, "18");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Percentage of Re-scheduling of  \n"
                + " OT Surgeries  ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of Cases Re-scheduled  \n"
                + " -----------------------------------  X 100\n"
                + " No. of Surgeries Planned ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 19");
        cells.put(c.qi, "19");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Percentage of Adherence to  surgical \n"
                + " safety checklist (For Surgical \n"
                + " Procedures)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Cases where the Surgical Safety \n"
                + " Checklist was Adhered to \n"
                + " -----------------------------------  X 100\n"
                + " No. of Surgeries Performed ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 20");
        cells.put(c.qi, "20");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Percentage of cases who recieved  \n"
                + " appropriate prophilactic antibiotic  \n"
                + " within the specified time frame ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of patients who did received \n"
                + " appropriate prophylactic antibiotic \n"
                + " -----------------------------------  X 100\n"
                + " Total No. of Surgeries performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 21");
        cells.put(c.qi, "21");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Percentage of cases in which the  \n"
                + " planned surgery is changed  \n"
                + " intraoperatively ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of cases in which the planned \n"
                + " surgery is changed intraoperatively \n"
                + " -----------------------------------  X 100\n"
                + " Total No. of surgeries Performed ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 22");
        cells.put(c.qi, "22");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Re - exploration Rate ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of re - explorations done during \n"
                + " same admission\n"
                + " -----------------------------------  X 100\n"
                + " Total No. of Surgeries");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 23");
        cells.put(c.qi, "23");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "Percentage of Transfusion Reactions\n"
                + "(Recipient)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of transfusion reactions \n"
                + " -----------------------------------  X 100\n"
                + " No. of units transfused");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 24");
        cells.put(c.qi, "24");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "Percentage of Wastage of Blood and\n"
                + "Blood Components ");
        cells.put(c.blank1, "a) Wastage  In Wards\n"
                + "after issue");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Blood and Blood Components \n"
                + " Wasted among those issued \n"
                + " -----------------------------------  X 100\n"
                + " No.of Blood and Blood Component units \n"
                + " issued from the blood bank / Storage\n"
                + "Center");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 25");
        cells.put(c.qi, "25");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "Percentage of blood component \n"
                + "usage ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Components Used  \n"
                + " -----------------------------------  X 100\n"
                + " No. of blood and blood products used ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 26");
        cells.put(c.qi, "26");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "TAT for Issue of blood & blood  \n"
                + "components ");
        cells.put(c.blank1, "Total Average TAT");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of Time Taken  \n"
                + " -----------------------------------  X 100\n"
                + " Total No. of Blood and Blood \n"
                + " Components Issued ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 27");
        cells.put(c.qi, "27");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "Catheter Associated Urinary Tract  \n"
                + "Infection rate ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Urinary Catheter Associated UTIs  \n"
                + " in a month\n"
                + " -----------------------------------  X 1000\n"
                + " No. of urinary catheter days in that  \n"
                + " month ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 28");
        cells.put(c.qi, "28");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "Ventilator Associated Pneumonia\n"
                + "rate");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Ventilator Associated  \n"
                + "  Pneumonia in a month\n"
                + " -----------------------------------  X 1000\n"
                + "No. of Ventilator days in that month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 29");
        cells.put(c.qi, "29");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "Central Line Associated Blood\n"
                + " Stream Infection Rate");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Central Line  Associated BSI in a \n"
                + "  month\n"
                + " -----------------------------------  X 1000\n"
                + "No. of Central Line days in that month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 30");
        cells.put(c.qi, "30");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "(SSI)Surgical Site Infection Rate");
        cells.put(c.blank1, "SSI - Inguinal\n"
                + "Herniorraphy with \n"
                + "mesh");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Surgical Site Infections in a \n"
                + "  month\n"
                + " -----------------------------------  X 100\n"
                + "No. of Surgeries performed in that \n"
                + "month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 31");
        cells.put(c.qi, "31");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "Mortality Rate ");
        cells.put(c.blank1, "Proportional\n Maternal Mortality\n "
                + "Rate");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Maternal Deaths  \n"
                + " -----------------------------------  X 100\n"
                + "Total No. of Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 32");
        cells.put(c.qi, "32");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "Return to ICU within 48 Hrs");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of return to ICU within 48 hrs \n"
                + " -----------------------------------  X 100\n"
                + "No.of Discharges / Transfers in the ICU");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 33");
        cells.put(c.qi, "33");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "Return to Emergency within 72 hrs \n"
                + " with similar presenting complaints");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of return to ER within 72 hrs with\n"
                + " similar presenting complaints \n"
                + " -----------------------------------  X 100\n"
                + "No.of patients who have come to the ER");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 34");
        cells.put(c.qi, "34");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "Re - Intubatin Rate \n"
                + "(Re - Intubation within 48 hrs of\n"
                + " Extubation)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Re - intubations within 48 hrs of \n"
                + " extubation"
                + " -----------------------------------  X 100\n"
                + "No.of Intubations");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 35");
        cells.put(c.qi, "");
        cells.put(c.std, "");
        cells.put(c.bo, "");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 36");
        cells.put(c.qi, "");
        cells.put(c.std, "");
        cells.put(c.bo, "");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 37");
        cells.put(c.qi, "");
        cells.put(c.std, "");
        cells.put(c.bo, "");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 38");
        cells.put(c.qi, "");
        cells.put(c.std, "");
        cells.put(c.bo, "");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 39");
        cells.put(c.qi, "39");
        cells.put(c.std, "CQI (4a)");
        cells.put(c.bo, "Percentage of Drugs and \n"
                + " Consumables procured by local\n"
                + " purchase ");
        cells.put(c.blank1, "Local Purchase  \n"
                + "(Within Formulary)");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of drugs / items purchased by local  \n"
                + "purchase within formulary\n"
                + " -----------------------------------  X 100\n"
                + " No. of Drugs / Items in hospital \n"
                + " formulary list");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 40");
        cells.put(c.qi, "40");
        cells.put(c.std, "CQI (4a)");
        cells.put(c.bo, "Percentage of stock outs including \n"
                + "emergency drugs ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of stock outs  \n"
                + " -----------------------------------  X 100\n"
                + "No. of drugs listed in hospital \n"
                + "formulary and hospital consumables \n"
                + "list");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 41");
        cells.put(c.qi, "41");
        cells.put(c.std, "CQI (4a)");
        cells.put(c.bo, "Percentage of drugs and \n"
                + "consumables rejected before\n"
                + " preperation of GRN (Goods Receipt\n"
                + " Note)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total Quantity Rejected \n"
                + " -----------------------------------  X 100\n"
                + "Total Quantity Recieved before GRN");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 42");
        cells.put(c.qi, "42");
        cells.put(c.std, "CQI (4a)");
        cells.put(c.bo, "Percentage of Variations from the\n"
                + " procurement process ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Variations from the\n"
                + " defined procurement process  \n"
                + " -----------------------------------  X 100\n"
                + "Total No. of Items Procured");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 43");
        cells.put(c.qi, "43");
        cells.put(c.std, "CQI (4b)");
        cells.put(c.bo, "No. of Variations observed in Mock \n"
                + " Drills ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of variations in a mock drill");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 44");
        cells.put(c.qi, "44");
        cells.put(c.std, "CQI (4b)");
        cells.put(c.bo, "Incidence Of Fall");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Falls \n"
                + "-------------------------- X 1000\n"
                + "Total No. of Patient Days ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 45");
        cells.put(c.qi, "45");
        cells.put(c.std, "CQI (4b)");
        cells.put(c.bo, "Incidence of hospital associated \n"
                + " pressure ulcer after admission\n"
                + " (Bed sore per 1000 patient days)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Patients who developed new / \n"
                + " worsening of pressure ulcers  \n"
                + " -----------------------------------  X 1000\n"
                + "Total No. of Patient Days ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 46");
        cells.put(c.qi, "46");
        cells.put(c.std, "CQI (4b)");
        cells.put(c.bo, "Percentage of staff provided with \n"
                + " pre - exposure prophylaxis ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of employees who were provided \n"
                + " pre exposure prophylaxis \n"
                + "------------------------------------------ 100\n"
                + "Total No. of employees who were due\n"
                + " to be provided pre - exposure \n"
                + "prophylaxis ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 47");
        cells.put(c.qi, "47");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Bed Occupancy Rate ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Number of Inpatient Days in a given\n"
                + " month\n"
                + "---------------------------------------- X 100\n"
                + "No.of Available Bed days in that month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 48");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "OT Utilization Rate / \n"
                + " ICU Utilization Rate / Bed Utilization \n"
                + "Rate / ICU Equipment Utilization Rate");
        cells.put(c.blank1, "OT - 1");
        cells.put(c.blank2, "");
        cells.put(c.formula, "OT utilization time in Hrs  \n"
                + " -----------------------------------  X 100\n"
                + "Resource Hrs");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 49");
        cells.put(c.qi, "49");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Critical Equipment Down Time ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of down time for all critical \n"
                + "equipment in hours in a month ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 50");
        cells.put(c.qi, "50");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Nurse Patient Ratio for ICU`s / Wards ");
        cells.put(c.blank1, "Morning Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Nursing Staff  \n"
                + " -----------------------------------  \n"
                + "No. of Beds  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 51");
        cells.put(c.qi, "51");
        cells.put(c.std, "CQI (4d)");
        cells.put(c.bo, "Out Patient Satisfaction Index");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Average Score Achieved \n"
                + " ----------------------------------- X100 \n"
                + "Maximum possible score ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 52");
        cells.put(c.qi, "52");
        cells.put(c.std, "CQI (4d)");
        cells.put(c.bo, "In Patient Satisfaction Index");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Average Score Achieved \n"
                + " ----------------------------------- X100 \n"
                + "Maximum possible score ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 53");
        cells.put(c.qi, "53");
        cells.put(c.std, "CQI (4d)");
        cells.put(c.bo, "Waiting Time");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "SUM (Patient 's Time for Initiation of Test - Time of \n"
                + "registration for Test)"
                + " -----------------------------------  \n"
                + " No. of Patients Reported in Diagnostic\n"
                + " Laboratory");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 54");
        cells.put(c.qi, "54");
        cells.put(c.std, "CQI (4d)");
        cells.put(c.bo, "Time Taken For Discharge");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of Time taken for Discahrge \n"
                + " -----------------------------------  \n"
                + "No. of Patients Discharged  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 55");
        cells.put(c.qi, "55");
        cells.put(c.std, "CQI (4e)");
        cells.put(c.bo, "Employee Satisfaction Index");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Average Score Achieved  \n"
                + " -----------------------------------X100  \n"
                + "Maximum Possible Score ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 56");
        cells.put(c.qi, "56");
        cells.put(c.std, "CQI (4e)");
        cells.put(c.bo, "Employee Attrition Rate ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of employees who have left\n"
                + " during the month "
                + " -----------------------------------X100  \n"
                + " (No. Of employees at the begining of the\n"
                + " month + Newly Joined Staff) ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 57");
        cells.put(c.qi, "57");
        cells.put(c.std, "CQI (4e)");
        cells.put(c.bo, "Employee Absenteesim Rate ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of employees who are on\n"
                + " unauthorised absence "
                + " -----------------------------------X100  \n"
                + " (No. Of employees) ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 58");
        cells.put(c.qi, "58");
        cells.put(c.std, "CQI (4e)");
        cells.put(c.bo, "Percentage of Employees who are\n"
                + " aware of employee rights,\n"
                + " responsibilities and welfare\n"
                + " schemes ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Percentage of Employees who are\n"
                + " aware of employee rights,\n"
                + " responsibilities and welfare schemes,\n"
                + " -----------------------------------X100  \n"
                + "No. of employees interviewed ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 59");
        cells.put(c.qi, "59");
        cells.put(c.std, "CQI (4f)");
        cells.put(c.bo, "No. of Sentinel Events reported,\n"
                + " collected and analysed within the \n"
                + " defined timeframe");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Sentinel events analysed within\n"
                + " the defined timeframe"
                + " unauthorised absence "
                + " -----------------------------------X100  \n"
                + "No. of Sentinel Events Reported /\n"
                + " Collected");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 60");
        cells.put(c.qi, "60");
        cells.put(c.std, "CQI (4f)");
        cells.put(c.bo, "Percentage of Near Miss");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Near Misses Reported\n"
                + " -----------------------------------X100  \n"
                + "Number of Incidents Reported");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 61");
        cells.put(c.qi, "61");
        cells.put(c.std, "CQI (4f)");
        cells.put(c.bo, "Incidence of Blood Body Fluid\n"
                + " Exposure");
        cells.put(c.blank1, "In IPD Areas ");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Blood Body Fluid Exposures \n"
                + " -----------------------------------X1000  \n"
                + "No. of Inpatient Days ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 62");
        cells.put(c.qi, "62");
        cells.put(c.std, "CQI (4f)");
        cells.put(c.bo, "Incidence of Needle Stick Injury\n"
                + " (NSI)");
        cells.put(c.blank1, "In IPD Areas");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Parenteral Exposures  \n"
                + " -----------------------------------X1000  \n"
                + "No. of Inpatient Days  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 63");
        cells.put(c.qi, "63");
        cells.put(c.std, "CQI (4g)");
        cells.put(c.bo, "Percentage of Medical Records not \n"
                + " having Discharge Summary");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Medical Records not having \n"
                + " Discharge Summary \n"
                + " -----------------------------------X100  \n"
                + "No. of Discharges and Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 64");
        cells.put(c.qi, "64");
        cells.put(c.std, "CQI (4g)");
        cells.put(c.bo, "Percentage of Medical Records Not\n"
                + " having Codification as per ICD");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Medical Records Not having\n"
                + " Codification as per ICD"
                + " -----------------------------------X100  \n"
                + "No. of Discharges and Deaths");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 65");
        cells.put(c.qi, "65");
        cells.put(c.std, "CQI (4g)");
        cells.put(c.bo, "Percentage of Medical Records\n"
                + " having Incomplete and / or\n"
                + " Improper Consent");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of Medical Records having\n"
                + " Incomplete and / or Improper Consent"
                + " -----------------------------------X100  \n"
                + "No.of Discharges and Deaths");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 66");
        cells.put(c.qi, "66");
        cells.put(c.std, "CQI (4g)");
        cells.put(c.bo, "Percentage of Missing Records ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Missing Records "
                + " -----------------------------------X100  \n"
                + "No. of Records");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 67");
        cells.put(c.qi, "67");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Appropriate Handover During Shift\n"
                + "  Change (DOCTORS / Nurse)");
        cells.put(c.blank1, "Morning Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Handovers done \n"
                + "appropriately "
                + " -----------------------------------X100  \n"
                + "Total No. of Handover Opportunities ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 68");
        cells.put(c.qi, "68");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Incidence of Patient Identification\n"
                + " Errors ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Patient Identification Errors  "
                + " -----------------------------------  \n"
                + "No. of Patients ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 69");
        cells.put(c.qi, "69");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Compliance to Hand Hygiene\n"
                + " Practice");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Hand Hygiene Missed\n"
                + " Opportunities "
                + " -----------------------------------X100  \n"
                + "Total No. of Hand hygiene \n"
                + "Opportunities ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "Indicator 70");
        cells.put(c.qi, "70");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Compliance Rate to Medication\n"
                + "Prescription in Capital");
        cells.put(c.blank1, "IPD");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Priscriptions in Capital \n"
                + " Letters "
                + " -----------------------------------X100  \n"
                + "Total No. of Prescriptions");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

//        nabhIndicatorsGroup.add(nabhIndicators);
        return nabhIndicators;
    }

    public static List<NABHIndicators_Mdl> getNABHIndicators_Report() throws Exception {
        List<NABHIndicators_Mdl> nabhIndicatorsGroup = new ArrayList<>();
        NABHIndicators_Mdl nabhIndicators = new NABHIndicators_Mdl();

        Map<String, String> cells = new LinkedHashMap<>();
        NABH_Column c = new NABH_Column();

        cells.put(c.menu, "1");
        cells.put(c.qi, "1");
        cells.put(c.std, "CQI 3(a)");
        cells.put(c.bo, "Time of Initial Assessment by Doctor \n(Wards)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of Time taken for the assessment\n"
                + "_______________________________\n"
                + "Total No. of Patients in Ward");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "2");
        cells.put(c.qi, "1");
        cells.put(c.std, "CQI 3(a)");
        cells.put(c.bo, "Time of Initial Assessment by Doctor \n(ER)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of Time taken for the assessment\n"
                + "_______________________________\n"
                + "Total No. of Patients in ER");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "3");
        cells.put(c.qi, "2");
        cells.put(c.std, "CQI 3(a)");
        cells.put(c.bo, "Percentage of cases (IPD)  wherein care \n"
                + " plan with desired outcomes is \n"
                + " Documented & Countersigned");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of IP cases wherein the care plan with \n"
                + "desired outcomes has been documented \n& Countersigned\n"
                + "----------------------------------------  X 100\n"
                + "Total No. of Patients ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "4");
        cells.put(c.qi, "3");
        cells.put(c.std, "CQI 3(a)");
        cells.put(c.bo, "Percentage of cases (IPD) wherein \n"
                + "screening for nutritional needs has \n"
                + "been done.");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of IP case record wherein the \n"
                + "nutritional assessment has been \n"
                + "documented\n"
                + "------------------------------------- X  100\n"
                + "Total No. of Patients");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "5");
        cells.put(c.qi, "4");
        cells.put(c.std, "CQI 3(a)");
        cells.put(c.bo, "Percentage of cases (IP) wherein the \n"
                + "nursing care is documented.");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of IP case record wherein the nursing \n"
                + "care plan has been documented\n"
                + "------------------------------------- X  100\n"
                + "Total No. of Patients");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);
//        SubSectionMergeConfiguration config = new SubSectionMergeConfiguration(2, 2, 2, 6);
//        subSection.getMergeConfigurations().add(config);
        nabhIndicatorsGroup.add(nabhIndicators);

//        subSection = new RptFileReceive_SubSec();
//        subSection.setSubSectionName("Laboratory");
        //  config = new SubSectionMergeConfiguration(3, 3, 8, 11);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(2, 2, 8, 11);
        //subSection.getMergeConfigurations().add(config);
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "6");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Microbiology).");
        cells.put(c.blank1, "Pre - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "7");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Microbiology).");
        cells.put(c.blank1, "Post - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "8");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Microbiology).");
        cells.put(c.blank1, "Transcription Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "9");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Microbiology).");
        cells.put(c.blank1, "Net Reporting Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        //config = new SubSectionMergeConfiguration(3, 3, 12, 15);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(2, 2, 12, 15);
        // subSection.getMergeConfigurations().add(config);
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "10");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Biochemistry).");
        cells.put(c.blank1, "Pre - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "11");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Biochemistry).");
        cells.put(c.blank1, "Post - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "12");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n "
                + "investigations (Biochemistry).");
        cells.put(c.blank1, "Transcription Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "13");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n "
                + "investigations (Biochemistry).");
        cells.put(c.blank1, "Net Reporting Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        //config = new SubSectionMergeConfiguration(3, 3, 16, 19);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(2, 2, 16, 19);
        //subSection.getMergeConfigurations().add(config);
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "14");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Pathology).");
        cells.put(c.blank1, "Pre - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "15");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Pathology).");
        cells.put(c.blank1, "Post - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "16");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Pathology).");
        cells.put(c.blank1, "Transcription Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "17");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Pathology).");
        cells.put(c.blank1, "Net Reporting Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        //config = new SubSectionMergeConfiguration(3, 3, 20, 23);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(2, 2, 20, 23);
        //subSection.getMergeConfigurations().add(config);
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "18");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Serology).");
        cells.put(c.blank1, "Pre - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "19");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Serology).");
        cells.put(c.blank1, "Post - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "20");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Serology).");
        cells.put(c.blank1, "Transcription Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "21");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Serology).");
        cells.put(c.blank1, "Net Reporting Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "22");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "");
        cells.put(c.blank1, "GROSS NO. OF \n"
                + "REPORTING ERRORS \n"
                + "(LABORATORY)");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        nabhIndicatorsGroup.add(nabhIndicators);

//        subSection = new RptFileReceive_SubSec();
//        subSection.setSubSectionName("Radiology");
        //config = new SubSectionMergeConfiguration(3, 3, 26, 29);
        //subSection.getMergeConfigurations().add(config);
        //config = new SubSectionMergeConfiguration(2, 2, 26, 29);
        //subSection.getMergeConfigurations().add(config);
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "23");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (CT).");
        cells.put(c.blank1, "Pre - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "24");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (CT).");
        cells.put(c.blank1, "Post - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "25");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (CT).");
        cells.put(c.blank1, "Transcription Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "26");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (CT).");
        cells.put(c.blank1, "Net Reporting Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);
        //config = new SubSectionMergeConfiguration(3, 3, 30, 33);
        //subSection.getMergeConfigurations().add(config);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "27");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (MRI).");
        cells.put(c.blank1, "Pre - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "28");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (MRI).");
        cells.put(c.blank1, "Post - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "29");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (MRI).");
        cells.put(c.blank1, "Transcription Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "30");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (MRI).");
        cells.put(c.blank1, "Net Reporting Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);
        //config = new SubSectionMergeConfiguration(3, 3, 34, 37);
        //subSection.getMergeConfigurations().add(config);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "31");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (USG).");
        cells.put(c.blank1, "Pre - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "32");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (USG).");
        cells.put(c.blank1, "Post - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "33");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (USG).");
        cells.put(c.blank1, "Transcription Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "34");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (USG).");
        cells.put(c.blank1, "Net Reporting Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);
        //config = new SubSectionMergeConfiguration(3, 3, 38, 41);
        //subSection.getMergeConfigurations().add(config);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "35");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Mamograpgy).");
        cells.put(c.blank1, "Pre - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "36");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Mamograpgy).");
        cells.put(c.blank1, "Post - Dispatch Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "37");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Mamograpgy).");
        cells.put(c.blank1, "Transcription Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "38");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI 3(b)");
        cells.put(c.bo, "No. of reporting errors per 1000 \n"
                + "investigations (Mamograpgy).");
        cells.put(c.blank1, "Net Reporting Errors");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "39");
        cells.put(c.qi, "5");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "");
        cells.put(c.blank1, "GROSS NO. OF \n"
                + "REPORTING ERRORS \n"
                + "(RADIOLOGY)");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of reporting Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        nabhIndicators.getRows().add(cells);
//============================================================================================================================
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "40");
        cells.put(c.qi, "6");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Rate of Re - Dos.(Include Tests \n"
                + " repeated Before release of Results \n "
                + " for confirmation)");
        cells.put(c.blank1, "LABORATORY");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Re - Dos \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "41");
        cells.put(c.qi, "6");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Rate of Re - Dos.(Include Tests \n"
                + " repeated Before release of Results \n "
                + " for confirmation)");
        cells.put(c.blank1, "RADIOLOGY");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Re - Dos \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        nabhIndicatorsGroup.add(nabhIndicators);

//        subSection = new RptFileReceive_SubSec();
//        subSection.setSubSectionName("LABORATORY");
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "42");
        cells.put(c.qi, "7");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Percentage of Reports Co - relating \n"
                + " with clinical diagnosis ");
        cells.put(c.blank1, "HISTOPATHOLOGY");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Reports Co-relating with clinical \n"
                + " diagnosis \n "
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "43");
        cells.put(c.qi, "7");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Percentage of Reports Co - relating \n"
                + " with clinical diagnosis ");
        cells.put(c.blank1, "OTHER COSTLY TESTS");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Reports Co-relating with clinical \n"
                + " diagnosis \n "
                + "-----------------------------------  X 1000\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        nabhIndicatorsGroup.add(nabhIndicators);
//=======================================================================================
//        subSection = new RptFileReceive_SubSec();
//        subSection.setSubSectionName("RADIOLOGY");

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "44");
        cells.put(c.qi, "7");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Percentage of Reports Co - relating \n"
                + " with clinical diagnosis ");
        cells.put(c.blank1, "CT - SCAN");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Reports Co-relating with clinical \n"
                + " diagnosis \n "
                + "-----------------------------------  X 100\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "45");
        cells.put(c.qi, "7");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Percentage of Reports Co - relating \n"
                + " with clinical diagnosis ");
        cells.put(c.blank1, "MRI");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Reports Co-relating with clinical \n"
                + " diagnosis \n "
                + "-----------------------------------  X 100\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "46");
        cells.put(c.qi, "7");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Percentage of Reports Co - relating \n"
                + " with clinical diagnosis ");
        cells.put(c.blank1, "OTHER COSTLY TESTS");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Reports Co-relating with clinical \n"
                + " diagnosis \n "
                + "-----------------------------------  X 100\n"
                + "No. of Tests Performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "47");
        cells.put(c.qi, "8");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Percentage of adherence to safety \n"
                + " precautions by employee woring in \n"
                + " Diagnostics ");
        cells.put(c.blank1, "LABORATORY");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Employees Adhering to safety  \n"
                + " Precautions  \n "
                + "-----------------------------------  X 100\n"
                + "No. of Employees Sampled ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "48");
        cells.put(c.qi, "8");
        cells.put(c.std, "CQI (3b)");
        cells.put(c.bo, "Percentage of adherence to safety \n"
                + " precautions by employee woring in \n"
                + " Diagnostics ");
        cells.put(c.blank1, "RADIOLOGY");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Employees Adhering to safety  \n"
                + " Precautions  \n "
                + "-----------------------------------  X 100\n"
                + "No. of Employees Sampled ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "49");
        cells.put(c.qi, "9");
        cells.put(c.std, "CQI (3c)");
        cells.put(c.bo, "Incidence of Medication Errors (Per \n"
                + " 1000 Patient Days) ");
        cells.put(c.blank1, "Prescription Error");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Prescription Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Patient Days ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "50");
        cells.put(c.qi, "9");
        cells.put(c.std, "CQI (3c)");
        cells.put(c.bo, "Incidence of Medication Errors (Per \n"
                + " 1000 Patient Days) ");
        cells.put(c.blank1, "Dispensing Error");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Dispensing Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Patient Days ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "51");
        cells.put(c.qi, "9");
        cells.put(c.std, "CQI (3c)");
        cells.put(c.bo, "Incidence of Medication Errors (Per \n"
                + " 1000 Patient Days) ");
        cells.put(c.blank1, "Total No. of \n Medication Error");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Medication Errors \n"
                + "-----------------------------------  X 1000\n"
                + "No. of Patient Days ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "52");
        cells.put(c.qi, "10");
        cells.put(c.std, "CQI (3c)");
        cells.put(c.bo, "Percentage of Admissions with  \n"
                + " Adverse Drug Reactions ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Adverse Drug Reactions \n"
                + "-----------------------------------  X 100\n"
                + "No. of Discharges and Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "53");
        cells.put(c.qi, "11");
        cells.put(c.std, "CQI (3c)");
        cells.put(c.bo, "Percentage of Medication Charts  \n"
                + " with Error Prone abbreviations");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of medication charts with error \n"
                + " prone abbreviations \n"
                + "-----------------------------------  X 100\n"
                + " No. of Medication Charts Reviewed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "54");
        cells.put(c.qi, "12");
        cells.put(c.std, "CQI (3c)");
        cells.put(c.bo, "Percentage of patients receiving High \n"
                + " Risk Medications developing ADR \n"
                + " (Adverse Drug Reaction)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Patients Receiving High Risk \n"
                + " Medications who have an ADR \n"
                + " -----------------------------------  X 100\n"
                + " No. of patients receiving High Risk \n"
                + " Medications");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "55");
        cells.put(c.qi, "13");
        cells.put(c.std, "CQI (3d)");
        cells.put(c.bo, "Percentage of Modification of  \n"
                + " Anesthesia Plans ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of patients in whome the Anesthesia\n"
                + " plan was modified \n"
                + " -----------------------------------  X 100\n"
                + " No. of patients who underwent  \n"
                + " Anesthesia");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "56");
        cells.put(c.qi, "14");
        cells.put(c.std, "CQI (3d)");
        cells.put(c.bo, "Percentage of Unplanned  \n"
                + " Ventilation following anesthesia ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of patients requiring unplanned \n"
                + " ventilation following anaesthesia \n"
                + " -----------------------------------  X 100\n"
                + " No. of Patients who underwent  \n"
                + " Anesthesia");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "57");
        cells.put(c.qi, "14");
        cells.put(c.std, "CQI (3d)");
        cells.put(c.bo, "Percentage of Adverse Anesthesia \n"
                + " Events");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of Patients who develop adverse  \n"
                + " anesthesia event\n"
                + " -----------------------------------  X 100\n"
                + " No. of patients who underwent \n"
                + " anaesthesia");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "58");
        cells.put(c.qi, "16");
        cells.put(c.std, "CQI (3d)");
        cells.put(c.bo, "Anaesthesia Related Mortality Rate");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of patients who died due to \n"
                + " anesthesia \n"
                + " -----------------------------------  X 100\n"
                + " No. of Patients who underwent \n"
                + " Anaesthesia");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "59");
        cells.put(c.qi, "17");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Percentage of Unplanned Return to \n"
                + " OT  ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Unplanned return to OT \n"
                + " -----------------------------------  X 100\n"
                + " No. of Patients Operated ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "60");
        cells.put(c.qi, "18");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Percentage of Re-scheduling of  \n"
                + " OT Surgeries  ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of Cases Re-scheduled  \n"
                + " -----------------------------------  X 100\n"
                + " No. of Surgeries Planned ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "61");
        cells.put(c.qi, "19");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Percentage of Adherence to  surgical \n"
                + " safety checklist (For Surgical \n"
                + " Procedures)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Cases where the Surgical Safety \n"
                + " Checklist was Adhered to \n"
                + " -----------------------------------  X 100\n"
                + " No. of Surgeries Performed ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "62");
        cells.put(c.qi, "20");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Percentage of cases who recieved  \n"
                + " appropriate prophilactic antibiotic  \n"
                + " within the specified time frame ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of patients who did received \n"
                + " appropriate prophylactic antibiotic \n"
                + " -----------------------------------  X 100\n"
                + " Total No. of Surgeries performed");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "63");
        cells.put(c.qi, "21");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Percentage of cases in which the  \n"
                + " planned surgery is changed  \n"
                + " intraoperatively ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of cases in which the planned \n"
                + " surgery is changed intraoperatively \n"
                + " -----------------------------------  X 100\n"
                + " Total No. of surgeries Performed ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "64");
        cells.put(c.qi, "22");
        cells.put(c.std, "CQI (3e)");
        cells.put(c.bo, "Re - exploration Rate ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of re - explorations done during \n"
                + " same admission\n"
                + " -----------------------------------  X 100\n"
                + " Total No. of Surgeries");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "65");
        cells.put(c.qi, "23");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "Percentage of Transfusion Reactions\n"
                + "(Recipient)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of transfusion reactions \n"
                + " -----------------------------------  X 100\n"
                + " No. of units transfused");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "66");
        cells.put(c.qi, "24");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "Percentage of Wastage of Blood and\n"
                + "Blood Components ");
        cells.put(c.blank1, "a) Wastage  In Wards\n"
                + "after issue");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Blood and Blood Components \n"
                + " Wasted among those issued \n"
                + " -----------------------------------  X 100\n"
                + " No.of Blood and Blood Component units \n"
                + " issued from the blood bank / Storage\n"
                + "Center");
        cells.put(c.result, "");

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "67");
        cells.put(c.qi, "24");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "Percentage of Wastage of Blood and\n"
                + "Blood Components ");
        cells.put(c.blank1, "b) Wastage in Blood \n"
                + "Bank / Blood Storage\n"
                + "Centers before issue");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of Blood and Blood Components \n"
                + " Wasted at the Blood Bank / Blood\n"
                + " Storage Center \n"
                + " -----------------------------------  X 100\n"
                + " No. of Blood and Blood component \n"
                + "units stored in the Blood bank / \n"
                + "Storage Center");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "68");
        cells.put(c.qi, "25");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "Percentage of blood component \n"
                + "usage ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Components Used  \n"
                + " -----------------------------------  X 100\n"
                + " No. of blood and blood products used ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "69");
        cells.put(c.qi, "26");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "TAT for Issue of blood & blood  \n"
                + "components ");
        cells.put(c.blank1, "Total Average TAT");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of Time Taken  \n"
                + " -----------------------------------  X 100\n"
                + " Total No. of Blood and Blood \n"
                + " Components Issued ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "70");
        cells.put(c.qi, "26");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "TAT for Issue of blood & blood  \n"
                + "components ");
        cells.put(c.blank1, "Elective Cases TAT");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of Time Taken  \n"
                + " -----------------------------------  X 100\n"
                + " Total No. of Blood and Blood \n"
                + " Components Issued ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "71");
        cells.put(c.qi, "26");
        cells.put(c.std, "CQI (3f)");
        cells.put(c.bo, "TAT for Issue of blood & blood  \n"
                + "components ");
        cells.put(c.blank1, "Emergency Cases TAT");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of Time Taken  \n"
                + " -----------------------------------  X 100\n"
                + " Total No. of Blood and Blood \n"
                + " Components Issued ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "72");
        cells.put(c.qi, "27");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "Catheter Associated Urinary Tract  \n"
                + "Infection rate ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Urinary Catheter Associated UTIs  \n"
                + " in a month\n"
                + " -----------------------------------  X 1000\n"
                + " No. of urinary catheter days in that  \n"
                + " month ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "73");
        cells.put(c.qi, "28");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "Ventilator Associated Pneumonia\n"
                + "rate");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Ventilator Associated  \n"
                + "  Pneumonia in a month\n"
                + " -----------------------------------  X 1000\n"
                + "No. of Ventilator days in that month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "74");
        cells.put(c.qi, "29");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "Central Line Associated Blood\n"
                + " Stream Infection Rate");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Central Line  Associated BSI in a \n"
                + "  month\n"
                + " -----------------------------------  X 1000\n"
                + "No. of Central Line days in that month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "75");
        cells.put(c.qi, "30");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "(SSI)Surgical Site Infection Rate");
        cells.put(c.blank1, "SSI - Inguinal\n"
                + "Herniorraphy with \n"
                + "mesh");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Surgical Site Infections in a \n"
                + "  month\n"
                + " -----------------------------------  X 100\n"
                + "No. of Surgeries performed in that \n"
                + "month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "76");
        cells.put(c.qi, "30");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "(SSI)Surgical Site Infection Rate");
        cells.put(c.blank1, "SSI - LSCS");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Surgical Site Infections in a \n"
                + "  month\n"
                + " -----------------------------------  X 100\n"
                + "No. of Surgeries performed in that \n"
                + "month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "77");
        cells.put(c.qi, "30");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "(SSI)Surgical Site Infection Rate");
        cells.put(c.blank1, "SSI - Laproscopic \n"
                + "Cholecystectomy");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Surgical Site Infections in a \n"
                + "  month\n"
                + " -----------------------------------  X 100\n"
                + "No. of Surgeries performed in that \n"
                + "month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "78");
        cells.put(c.qi, "30");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "(SSI)Surgical Site Infection Rate");
        cells.put(c.blank1, "SSI - CABG ");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Surgical Site Infections in a \n"
                + "  month\n"
                + " -----------------------------------  X 100\n"
                + "No. of Surgeries performed in that \n"
                + "month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "79");
        cells.put(c.qi, "30");
        cells.put(c.std, "CQI (3g)");
        cells.put(c.bo, "(SSI)Surgical Site Infection Rate");
        cells.put(c.blank1, "OVER ALL SSI - Rate");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Surgical Site Infections in a \n"
                + "  month\n"
                + " -----------------------------------  X 100\n"
                + "No. of Surgeries performed in that \n"
                + "month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "80");
        cells.put(c.qi, "31");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "Mortality Rate ");
        cells.put(c.blank1, "Proportional\n Maternal Mortality\n "
                + "Rate");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Maternal Deaths  \n"
                + " -----------------------------------  X 100\n"
                + "Total No. of Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "81");
        cells.put(c.qi, "31");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "Mortality Rate ");
        cells.put(c.blank1, "Proportional Infant \n"
                + " Mortality Rate ");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Infant Deaths   \n"
                + " -----------------------------------  X 100\n"
                + "Total No. of Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "82");
        cells.put(c.qi, "31");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "Mortality Rate ");
        cells.put(c.blank1, "Overall Mortality Rate \n"
                + " of HCO");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of Deaths   \n"
                + " -----------------------------------  X 100\n"
                + "Number of Discharges & Deaths  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "83");
        cells.put(c.qi, "31");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "CASE FATALITY RATE \n"
                + " (5 Most Frequent Conditions for \n"
                + "Deaths in HCO)");
        cells.put(c.blank1, "Septisemic Shock");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Deaths due to Septisemic Shock\n"
                + " -----------------------------------  X 100\n"
                + "Total No. of Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "84");
        cells.put(c.qi, "31");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "CASE FATALITY RATE \n"
                + " (5 Most Frequent Conditions for \n"
                + "Deaths in HCO)");
        cells.put(c.blank1, "ARDS");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Deaths due to ARDS \n"
                + " -----------------------------------  X 100\n"
                + "Total No. of Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "85");
        cells.put(c.qi, "31");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "CASE FATALITY RATE \n"
                + " (5 Most Frequent Conditions for \n"
                + "Deaths in HCO)");
        cells.put(c.blank1, "CKD");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Deaths due to CKD \n"
                + " -----------------------------------  X 100\n"
                + "Total No. of Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "86");
        cells.put(c.qi, "31");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "CASE FATALITY RATE \n"
                + " (5 Most Frequent Conditions for \n"
                + "Deaths in HCO)");
        cells.put(c.blank1, "MI");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Deaths due to  MIs\n"
                + " -----------------------------------  X 100\n"
                + "Total No. of Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "87");
        cells.put(c.qi, "31");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "CASE FATALITY RATE \n"
                + " (5 Most Frequent Conditions for \n"
                + "Deaths in HCO)");
        cells.put(c.blank1, "TRAUMA (RTA /\n"
                + " ASSAULT)");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Deaths due to TRAUMA\n"
                + " -----------------------------------  X 100\n"
                + "Total No. of Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "88");
        cells.put(c.qi, "32");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "Return to ICU within 48 Hrs");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of return to ICU within 48 hrs \n"
                + " -----------------------------------  X 100\n"
                + "No.of Discharges / Transfers in the ICU");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "89");
        cells.put(c.qi, "33");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "Return to Emergency within 72 hrs \n"
                + " with similar presenting complaints");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of return to ER within 72 hrs with\n"
                + " similar presenting complaints \n"
                + " -----------------------------------  X 100\n"
                + "No.of patients who have come to the ER");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "90");
        cells.put(c.qi, "34");
        cells.put(c.std, "CQI (3h)");
        cells.put(c.bo, "Re - Intubatin Rate \n"
                + "(Re - Intubation within 48 hrs of\n"
                + " Extubation)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Re - intubations within 48 hrs of \n"
                + " extubation"
                + " -----------------------------------  X 100\n"
                + "No.of Intubations");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "91");
        cells.put(c.qi, "39");
        cells.put(c.std, "CQI (4a)");
        cells.put(c.bo, "Percentage of Drugs and \n"
                + " Consumables procured by local\n"
                + " purchase ");
        cells.put(c.blank1, "Local Purchase  \n"
                + "(Within Formulary)");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of drugs / items purchased by local  \n"
                + "purchase within formulary\n"
                + " -----------------------------------  X 100\n"
                + " No. of Drugs / Items in hospital \n"
                + " formulary list");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "92");
        cells.put(c.qi, "39");
        cells.put(c.std, "CQI (4a)");
        cells.put(c.bo, "Percentage of Drugs and \n"
                + " Consumables procured by local\n"
                + " purchase ");
        cells.put(c.blank1, "Local Purchase  \n"
                + "(Outside Formulary");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of drugs / items purchased by local purchase\n"
                + " Outside formulary"
                + " -----------------------------------  X 100\n"
                + "No. of Drugs / Items procured in hospital from with in\n "
                + "as well as outside \n"
                + "formulary list");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "93");
        cells.put(c.qi, "40");
        cells.put(c.std, "CQI (4a)");
        cells.put(c.bo, "Percentage of stock outs including \n"
                + "emergency drugs ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of stock outs  \n"
                + " -----------------------------------  X 100\n"
                + "No. of drugs listed in hospital \n"
                + "formulary and hospital consumables \n"
                + "list");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "94");
        cells.put(c.qi, "41");
        cells.put(c.std, "CQI (4a)");
        cells.put(c.bo, "Percentage of drugs and \n"
                + "consumables rejected before\n"
                + " preperation of GRN (Goods Receipt\n"
                + " Note)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total Quantity Rejected \n"
                + " -----------------------------------  X 100\n"
                + "Total Quantity Recieved before GRN");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "95");
        cells.put(c.qi, "42");
        cells.put(c.std, "CQI (4a)");
        cells.put(c.bo, "Percentage of Variations from the\n"
                + " procurement process ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Variations from the\n"
                + " defined procurement process  \n"
                + " -----------------------------------  X 100\n"
                + "Total No. of Items Procured");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "96");
        cells.put(c.qi, "43");
        cells.put(c.std, "CQI (4b)");
        cells.put(c.bo, "No. of Variations observed in Mock \n"
                + " Drills ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of variations in a mock drill");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "97");
        cells.put(c.qi, "44");
        cells.put(c.std, "CQI (4b)");
        cells.put(c.bo, "Incidence Of Fall");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Falls \n"
                + "-------------------------- X 1000\n"
                + "Total No. of Patient Days ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "98");
        cells.put(c.qi, "45");
        cells.put(c.std, "CQI (4b)");
        cells.put(c.bo, "Incidence of hospital associated \n"
                + " pressure ulcer after admission\n"
                + " (Bed sore per 1000 patient days)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Patients who developed new / \n"
                + " worsening of pressure ulcers  \n"
                + " -----------------------------------  X 1000\n"
                + "Total No. of Patient Days ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "99");
        cells.put(c.qi, "46");
        cells.put(c.std, "CQI (4b)");
        cells.put(c.bo, "Percentage of staff provided with \n"
                + " pre - exposure prophylaxis ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of employees who were provided \n"
                + " pre exposure prophylaxis \n"
                + "------------------------------------------ 100\n"
                + "Total No. of employees who were due\n"
                + " to be provided pre - exposure \n"
                + "prophylaxis ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "100");
        cells.put(c.qi, "47");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Bed Occupancy Rate ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Number of Inpatient Days in a given\n"
                + " month\n"
                + "---------------------------------------- X 100\n"
                + "No.of Available Bed days in that month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "101");
        cells.put(c.qi, "47");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Average Leangth of Stay (ALOS)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of inpatient days in a given month \n"
                + "---------------------------------------- X 100\n"
                + "No. of Discharges & Deaths in that \n"
                + "month");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "102");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "OT Utilization Rate ");
        cells.put(c.blank1, "OT - 1");
        cells.put(c.blank2, "");
        cells.put(c.formula, "OT utilization time in Hrs  \n"
                + " -----------------------------------  X 100\n"
                + "Resource Hrs");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "103");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "OT Utilization Rate ");
        cells.put(c.blank1, "OT - 2");
        cells.put(c.blank2, "");
        cells.put(c.formula, "OT utilization time in Hrs  \n"
                + " -----------------------------------  X 100\n"
                + "Resource Hrs");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "104");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "OT Utilization Rate ");
        cells.put(c.blank1, "OT - 3");
        cells.put(c.blank2, "");
        cells.put(c.formula, "OT utilization time in Hrs  \n"
                + " -----------------------------------  X 100\n"
                + "Resource Hrs");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "105");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "OT Utilization Rate ");
        cells.put(c.blank1, "OT - 4");
        cells.put(c.blank2, "");
        cells.put(c.formula, "OT utilization time in Hrs  \n"
                + " -----------------------------------  X 100\n"
                + "Resource Hrs");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "106");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "OT Utilization Rate ");
        cells.put(c.blank1, "OT - 5");
        cells.put(c.blank2, "");
        cells.put(c.formula, "OT utilization time in Hrs  \n"
                + " -----------------------------------  X 100\n"
                + "Resource Hrs");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "107");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "ICU Utilization Rate / Bed Utilization \n"
                + "Rate");
        cells.put(c.blank1, "ICU");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of bed Utilized Days   \n"
                + " -----------------------------------  X 100\n"
                + "Bed Days Available ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "108");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "ICU Utilization Rate / Bed Utilization \n"
                + "Rate");
        cells.put(c.blank1, "CT - ICU");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of bed Utilized Days   \n"
                + " -----------------------------------  X 100\n"
                + "Bed Days Available ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "109");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "ICU Utilization Rate / Bed Utilization \n"
                + "Rate");
        cells.put(c.blank1, "MICU");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of bed Utilized Days   \n"
                + " -----------------------------------  X 100\n"
                + "Bed Days Available ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "110");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "ICU Utilization Rate / Bed Utilization \n"
                + "Rate");
        cells.put(c.blank1, "NICU");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of bed Utilized Days   \n"
                + " -----------------------------------  X 100\n"
                + "Bed Days Available ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "111");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "ICU Utilization Rate / Bed Utilization \n"
                + "Rate");
        cells.put(c.blank1, "NSICU");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of bed Utilized Days   \n"
                + " -----------------------------------  X 100\n"
                + "Bed Days Available ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "112");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "ICU Utilization Rate / Bed Utilization \n"
                + "Rate");
        cells.put(c.blank1, "SICU");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of bed Utilized Days   \n"
                + " -----------------------------------  X 100\n"
                + "Bed Days Available ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "113");
        cells.put(c.qi, "48");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "ICU Equipment Utilization Rate");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Equipment Utilized Days  \n"
                + " -----------------------------------  X 100\n"
                + "Equipment Days Available ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "114");
        cells.put(c.qi, "49");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Critical Equipment Down Time ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of down time for all critical \n"
                + "equipment in hours in a month ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "115");
        cells.put(c.qi, "50");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Nurse Patient Ratio for ICU`s ");
        cells.put(c.blank1, "Morning Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Nursing Staff  \n"
                + " -----------------------------------  \n"
                + "No. of Beds  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "116");
        cells.put(c.qi, "50");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Nurse Patient Ratio for ICU`s ");
        cells.put(c.blank1, "Evening Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Nursing Staff  \n"
                + " -----------------------------------  \n"
                + "No. of Beds  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);
        cells = new LinkedHashMap<>();
        cells.put(c.menu, "117");
        cells.put(c.qi, "50");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Nurse Patient Ratio for ICU`s ");
        cells.put(c.blank1, "Night Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Nursing Staff  \n"
                + " -----------------------------------  \n"
                + "No. of Beds  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "118");
        cells.put(c.qi, "50");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Nurse Patient Ratio for ICU`s ");
        cells.put(c.blank1, "General Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Nursing Staff  \n"
                + " -----------------------------------  \n"
                + "No. of Beds  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "119");
        cells.put(c.qi, "50");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Nurse Patient Ratio for wards");
        cells.put(c.blank1, "Morning Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Nursing Staff  \n"
                + " -----------------------------------  \n"
                + "No. of Beds  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "120");
        cells.put(c.qi, "50");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Nurse Patient Ratio for wards");
        cells.put(c.blank1, "Evening Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Nursing Staff  \n"
                + " -----------------------------------  \n"
                + "No. of Beds  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "121");
        cells.put(c.qi, "50");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Nurse Patient Ratio for wards");
        cells.put(c.blank1, "Night Shift ");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Nursing Staff  \n"
                + " -----------------------------------  \n"
                + "No. of Beds  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "122");
        cells.put(c.qi, "50");
        cells.put(c.std, "CQI (4c)");
        cells.put(c.bo, "Nurse Patient Ratio for wards");
        cells.put(c.blank1, "General Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Nursing Staff  \n"
                + " -----------------------------------  \n"
                + "No. of Beds  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "123");
        cells.put(c.qi, "51");
        cells.put(c.std, "CQI (4d)");
        cells.put(c.bo, "Out Patient Satisfaction Index");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Average Score Achieved \n"
                + " ----------------------------------- X100 \n"
                + "Maximum possible score ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "124");
        cells.put(c.qi, "52");
        cells.put(c.std, "CQI (4d)");
        cells.put(c.bo, "In Patient Satisfaction Index");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Average Score Achieved \n"
                + " ----------------------------------- X100 \n"
                + "Maximum possible score ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "125");
        cells.put(c.qi, "53");
        cells.put(c.std, "CQI (4d)");
        cells.put(c.bo, "Waiting Time  (Radiology) - IN OPD");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "SUM (Patient 's Time for Initiation of Test - Time of \n"
                + "registration for Test)"
                + " -----------------------------------  \n"
                + " No. of Patients Reported in Diagnostic\n"
                + " Laboratory");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "126");
        cells.put(c.qi, "53");
        cells.put(c.std, "CQI (4d)");
        cells.put(c.bo, "Waiting Time  (OPD Consultation)");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "SUM (Patient in Time for Consultation \n"
                + "- Time of registration at OPD))"
                + " -----------------------------------  \n"
                + " No. of Patients Reported in OPD");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "127");
        cells.put(c.qi, "54");
        cells.put(c.std, "CQI (4d)");
        cells.put(c.bo, "Time Taken For Discharge");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Sum of Time taken for Discahrge \n"
                + " -----------------------------------  \n"
                + "No. of Patients Discharged  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "128");
        cells.put(c.qi, "55");
        cells.put(c.std, "CQI (4e)");
        cells.put(c.bo, "Employee Satisfaction Index");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Average Score Achieved  \n"
                + " -----------------------------------X100  \n"
                + "Maximum Possible Score ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "129");
        cells.put(c.qi, "56");
        cells.put(c.std, "CQI (4e)");
        cells.put(c.bo, "Employee Attrition Rate ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of employees who have left\n"
                + " during the month "
                + " -----------------------------------X100  \n"
                + " (No. Of employees at the begining of the\n"
                + " month + Newly Joined Staff) ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "130");
        cells.put(c.qi, "58");
        cells.put(c.std, "CQI (4e)");
        cells.put(c.bo, "Percentage of Employees who are\n"
                + " aware of employee rights,\n"
                + " responsibilities and welfare\n"
                + " schemes ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Percentage of Employees who are\n"
                + " aware of employee rights,\n"
                + " responsibilities and welfare schemes,\n"
                + " -----------------------------------X100  \n"
                + "No. of employees interviewed ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "131");
        cells.put(c.qi, "59");
        cells.put(c.std, "CQI (4f)");
        cells.put(c.bo, "No. of Sentinel Events reported,\n"
                + " collected and analysed within the \n"
                + " defined timeframe");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Sentinel events analysed within\n"
                + " the defined timeframe"
                + " unauthorised absence "
                + " -----------------------------------X100  \n"
                + "No. of Sentinel Events Reported /\n"
                + " Collected");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "132");
        cells.put(c.qi, "60");
        cells.put(c.std, "CQI (4f)");
        cells.put(c.bo, "Percentage of Near Miss");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Near Misses Reported\n"
                + " -----------------------------------X100  \n"
                + "Number of Incidents Reported");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "133");
        cells.put(c.qi, "61");
        cells.put(c.std, "CQI (4f)");
        cells.put(c.bo, "Incidence of Blood Body Fluid\n"
                + " Exposure");
        cells.put(c.blank1, "In IPD Areas ");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Blood Body Fluid Exposures \n"
                + " -----------------------------------X1000  \n"
                + "No. of Inpatient Days ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "134");
        cells.put(c.qi, "61");
        cells.put(c.std, "CQI (4f)");
        cells.put(c.bo, "Incidence of Blood Body Fluid\n"
                + " Exposure");
        cells.put(c.blank1, "In OPD Areas");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Blood Body Fluid Exposures \n"
                + " -----------------------------------X1000  \n"
                + "No. of OPD Patient Visits ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "135");
        cells.put(c.qi, "62");
        cells.put(c.std, "CQI (4f)");
        cells.put(c.bo, "Incidence of Needle Stick Injury\n"
                + " (NSI)");
        cells.put(c.blank1, "In IPD Areas");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Parenteral Exposures  \n"
                + " -----------------------------------X1000  \n"
                + "No. of Inpatient Days  ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "136");
        cells.put(c.qi, "62");
        cells.put(c.std, "CQI (4f)");
        cells.put(c.bo, "Incidence of Needle Stick Injury\n"
                + " (NSI)");
        cells.put(c.blank1, "In OPD Areas");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Parenteral Exposures  \n"
                + " -----------------------------------X1000  \n"
                + "No. of OPD Patient Visits");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "137");
        cells.put(c.qi, "63");
        cells.put(c.std, "CQI (4g)");
        cells.put(c.bo, "Percentage of Medical Records not \n"
                + " having Discharge Summary");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Medical Records not having \n"
                + " Discharge Summary \n"
                + " -----------------------------------X100  \n"
                + "No. of Discharges and Deaths ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "138");
        cells.put(c.qi, "64");
        cells.put(c.std, "CQI (4g)");
        cells.put(c.bo, "Percentage of Medical Records Not\n"
                + " having Codification as per ICD");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Medical Records Not having\n"
                + " Codification as per ICD"
                + " -----------------------------------X100  \n"
                + "No. of Discharges and Deaths");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "139");
        cells.put(c.qi, "65");
        cells.put(c.std, "CQI (4g)");
        cells.put(c.bo, "Percentage of Medical Records\n"
                + " having Incomplete and / or\n"
                + " Improper Consent");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No.of Medical Records having\n"
                + " Incomplete and / or Improper Consent"
                + " -----------------------------------X100  \n"
                + "No.of Discharges and Deaths");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "140");
        cells.put(c.qi, "66");
        cells.put(c.std, "CQI (4g)");
        cells.put(c.bo, "Percentage of Missing Records ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Missing Records "
                + " -----------------------------------X100  \n"
                + "No. of Records");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "141");
        cells.put(c.qi, "67");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Appropriate Handover During Shift\n"
                + "  Change (DOCTORS)");
        cells.put(c.blank1, "Morning Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Handovers done \n"
                + "appropriately "
                + " -----------------------------------X100  \n"
                + "Total No. of Handover Opportunities ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "142");
        cells.put(c.qi, "67");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Appropriate Handover During Shift\n"
                + "  Change (DOCTORS)");
        cells.put(c.blank1, "Evening Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Handovers done \n"
                + "appropriately "
                + " -----------------------------------X100  \n"
                + "Total No. of Handover Opportunities ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "143");
        cells.put(c.qi, "67");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Appropriate Handover During Shift\n"
                + "  Change (DOCTORS)");
        cells.put(c.blank1, "Night Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Handovers done \n"
                + "appropriately "
                + " -----------------------------------X100  \n"
                + "Total No. of Handover Opportunities ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "144");
        cells.put(c.qi, "67");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Appropriate Handover During Shift \n"
                + "Change (NURSES)");
        cells.put(c.blank1, "Morning Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Handovers done \n"
                + "appropriately "
                + " -----------------------------------X100  \n"
                + "Total No. of Handover Opportunities ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "145");
        cells.put(c.qi, "67");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Appropriate Handover During Shift \n"
                + "Change (NURSES)");
        cells.put(c.blank1, "Evening Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Handovers done \n"
                + "appropriately "
                + " -----------------------------------X100  \n"
                + "Total No. of Handover Opportunities ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "146");
        cells.put(c.qi, "67");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Appropriate Handover During Shift \n"
                + "Change (NURSES)");
        cells.put(c.blank1, "Night Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Handovers done \n"
                + "appropriately "
                + " -----------------------------------X100  \n"
                + "Total No. of Handover Opportunities ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "147");
        cells.put(c.qi, "67");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Appropriate Handover During Shift \n"
                + "Change (NURSES)");
        cells.put(c.blank1, "General Shift");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Handovers done \n"
                + "appropriately "
                + " -----------------------------------X100  \n"
                + "Total No. of Handover Opportunities ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "148");
        cells.put(c.qi, "68");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Incidence of Patient Identification\n"
                + " Errors ");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "No. of Patient Identification Errors  "
                + " -----------------------------------  \n"
                + "No. of Patients ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "149");
        cells.put(c.qi, "69");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Compliance to Hand Hygiene\n"
                + " Practice");
        cells.put(c.blank1, "");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Hand Hygiene Missed\n"
                + " Opportunities "
                + " -----------------------------------X100  \n"
                + "Total No. of Hand hygiene \n"
                + "Opportunities ");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "150");
        cells.put(c.qi, "70");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Compliance Rate to Medication\n"
                + "Prescription in Capital");
        cells.put(c.blank1, "IPD");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Priscriptions in Capital \n"
                + " Letters "
                + " -----------------------------------X100  \n"
                + "Total No. of Prescriptions");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "151");
        cells.put(c.qi, "70");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Compliance Rate to Medication\n"
                + "Prescription in Capital");
        cells.put(c.blank1, "OPD");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Priscriptions in Capital \n"
                + " Letters "
                + " -----------------------------------X100  \n"
                + "Total No. of Prescriptions");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        cells = new LinkedHashMap<>();
        cells.put(c.menu, "152");
        cells.put(c.qi, "70");
        cells.put(c.std, "CQI (3j)");
        cells.put(c.bo, "Compliance Rate to Medication\n"
                + "Prescription in Capital");
        cells.put(c.blank1, "ER");
        cells.put(c.blank2, "");
        cells.put(c.formula, "Total No. of Priscriptions in Capital \n"
                + " Letters "
                + " -----------------------------------X100  \n"
                + "Total No. of Prescriptions");
        cells.put(c.result, "");
        nabhIndicators.getRows().add(cells);

        nabhIndicatorsGroup.add(nabhIndicators);

        return nabhIndicatorsGroup;
    }
}
