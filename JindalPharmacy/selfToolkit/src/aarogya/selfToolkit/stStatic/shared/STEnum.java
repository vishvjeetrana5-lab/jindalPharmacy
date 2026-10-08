package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;

public class STEnum implements Serializable, IsSerializable {

    public enum MODALITY_CODES implements Serializable, IsSerializable {

        COMPUTED_RADIOGRAPHY("CR", "Computed Radiography"),
        COMPUTED_TOMOGRAPHY("CT", "Computed Tomography"),
        DIGITAL_RADIOGRAPHY("DX", "Digital Radiography"),
        ENDOSCOPY("ES", "Endoscopy"),
        GENERAL_MICROSCOPY("GM", "General Microscopy"),
        INTRA_ORAL_RADIOGRAPHY("IO", "Intra-oral Radiography"),
        MAMMOGRAPHY("MG", "Mammography"),
        MAGNETIC_RESONANCE("MR", "Magnetic Resonance"),
        NUCLEAR_MEDICINE("NM", "Nuclear Medicine"),
        OTHER("OT", "Other"),
        PANORAMIC_X_RAY("PX", "Panoramic X-Ray"),
        POSITRON_EMISSION_TOMOGRAPHY("PT", "Positron Emission Tomography"),
        RADIO_FLUOROSCOPY("RF", "Radio Fluoroscopy"),
        SECONDARY_CAPTURE_IMAGE("SC", "Secondary Capture Image"),
        SLIDE_MICROSCOPY("SM", "Slide Microscopy"),
        ULTRASOUND("US", "Ultrasound"),
        X_RAY_ANGIOGRAPHY("XA", "X-Ray Angiography"),
        EXTERNAL_CAMERA_PHOTOGRAPHY("XC", "External-camera Photography"),
        ELECTROCARDIOGRAPHY("ECG", "Electrocardiography");

        private final String code, description;

        MODALITY_CODES(String code, String text) {
            this.code = code;
            this.description = text;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static MODALITY_CODES getViaCode(String id) {
            for (MODALITY_CODES modality : MODALITY_CODES.values()) {
                if (DmShared.xNull(id).equalsIgnoreCase(modality.code)) {
                    return modality;
                }
            }
            return null;
        }

        public static MODALITY_CODES getViaText(String text) {
            for (MODALITY_CODES modality : MODALITY_CODES.values()) {
                if (DmShared.xNull(text).equalsIgnoreCase(modality.description)) {
                    return modality;
                }
            }
            return null;
        }
    }

    public enum RateListType implements Serializable, IsSerializable {

        FIX(0, "Fix"), CASH(1, "Cash");

        private String text;
        private int id;

        public String getText() {
            return text;
        }

        public int getId() {
            return id;
        }

        RateListType(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public static RateListType getViaKey(int id) {
            for (RateListType value : RateListType.values()) {
                if (id == value.id) {
                    return value;
                }
            }
            return null;
        }

        public static RateListType getViaText(String text) {
            for (RateListType value : RateListType.values()) {
                if (DmShared.xNull(text).equalsIgnoreCase(value.text)) {
                    return value;
                }
            }
            return null;
        }
    }

    public enum REGISTRATION_STATUS implements Serializable, IsSerializable {

        NONE(0, ""), DISCARDED(1, "Discarded"), REJECTED(2, "Rejected"), UNTESTED(3, "Untested");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        REGISTRATION_STATUS(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static REGISTRATION_STATUS getViaKey(int id) {
            REGISTRATION_STATUS algo = null;
            if (id == NONE.getId()) {
                algo = NONE;
            } else if (id == DISCARDED.getId()) {
                algo = DISCARDED;
            } else if (id == REJECTED.getId()) {
                algo = REJECTED;
            } else if (id == UNTESTED.getId()) {
                algo = UNTESTED;
            }
            return algo;
        }

        public static REGISTRATION_STATUS getViaText(String text) {
            REGISTRATION_STATUS algo = null;
            if (text.equalsIgnoreCase(NONE.getValue())) {
                algo = NONE;
            } else if (text.equalsIgnoreCase(DISCARDED.getValue())) {
                algo = DISCARDED;
            } else if (text.equalsIgnoreCase(REJECTED.getValue())) {
                algo = REJECTED;
            } else if (text.equalsIgnoreCase(UNTESTED.getValue())) {
                algo = UNTESTED;
            }
            return algo;
        }
    }

    public enum PrescriptionType implements Serializable, IsSerializable {

        INVESTIGATION_ADVISE(1, "Investigation Advise"), MEDICINE_ADVISE(2, "Medicine Advise");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        PrescriptionType(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static PrescriptionType getViaKey(int id) {
            PrescriptionType algo = null;
            if (id == INVESTIGATION_ADVISE.getId()) {
                algo = INVESTIGATION_ADVISE;
            } else if (id == MEDICINE_ADVISE.getId()) {
                algo = MEDICINE_ADVISE;
            }
            return algo;
        }

        public static PrescriptionType getViaText(String text) {
            PrescriptionType algo = null;
            if (text.equalsIgnoreCase(INVESTIGATION_ADVISE.getValue())) {
                algo = INVESTIGATION_ADVISE;
            } else if (text.equalsIgnoreCase(MEDICINE_ADVISE.getValue())) {
                algo = MEDICINE_ADVISE;
            }
            return algo;
        }
    }

    public enum TaskType implements Serializable, IsSerializable {

        NONE(0, "None"), HOT(1, "Immediate"), COLD(2, "Routine"), WARM(3, "As soon as possible"), CRITICAL(4, "Critical");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        TaskType(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static TaskType getViaKey(int id) {
            TaskType algo = null;
            if (id == NONE.getId()) {
                algo = NONE;
            } else if (id == HOT.getId()) {
                algo = HOT;
            } else if (id == COLD.getId()) {
                algo = COLD;
            } else if (id == WARM.getId()) {
                algo = WARM;
            } else if (id == CRITICAL.getId()) {
                algo = CRITICAL;
            }
            return algo;
        }

        public static TaskType getViaText(String text) {
            TaskType algo = null;
            if (text.equalsIgnoreCase(NONE.getValue())) {
                algo = NONE;
            } else if (text.equalsIgnoreCase(HOT.getValue())) {
                algo = HOT;
            } else if (text.equalsIgnoreCase(COLD.getValue())) {
                algo = COLD;
            } else if (text.equalsIgnoreCase(WARM.getValue())) {
                algo = WARM;
            } else if (text.equalsIgnoreCase(CRITICAL.getValue())) {
                algo = CRITICAL;
            }
            return algo;
        }
    }

    public enum RoomType implements Serializable, IsSerializable {

        ICU("ICU"), OT("O.T."), ROOM("Room"), EMERGENCY("Emergency"), DAY_CARE("Day Care"), CONSULTATION("Consultation");

        private final String value;

        public String getValue() {
            return value;
        }

        RoomType(String text) {
            this.value = text;
        }

        public static RoomType getViaText(String text) {
            RoomType algo = null;
            if (text.equalsIgnoreCase(ICU.getValue())) {
                algo = ICU;
            } else if (text.equalsIgnoreCase(OT.getValue())) {
                algo = OT;
            } else if (text.equalsIgnoreCase(ROOM.getValue())) {
                algo = ROOM;
            } else if (text.equalsIgnoreCase(EMERGENCY.getValue())) {
                algo = EMERGENCY;
            } else if (text.equalsIgnoreCase(DAY_CARE.getValue())) {
                algo = DAY_CARE;
            } else if (text.equalsIgnoreCase(CONSULTATION.getValue())) {
                algo = CONSULTATION;
            }
            return algo;
        }
    }

    public enum TaskStatus implements Serializable, IsSerializable {

        OPEN(0, "Open"), CLOSE(1, "Close"), COMPLETE(2, "Complete"), RE_OPEN(3, "Re-Open"), FOLLOW_UP(4, "Follow-Up"), CANCEL(5, "Cancel"),
        START(6, "Start");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        TaskStatus(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static TaskStatus getViaKey(int id) {
            TaskStatus algo = null;
            if (id == OPEN.getId()) {
                algo = OPEN;
            } else if (id == CLOSE.getId()) {
                algo = CLOSE;
            } else if (id == COMPLETE.getId()) {
                algo = COMPLETE;
            } else if (id == RE_OPEN.getId()) {
                algo = RE_OPEN;
            } else if (id == FOLLOW_UP.getId()) {
                algo = FOLLOW_UP;
            } else if (id == CANCEL.getId()) {
                algo = CANCEL;
            } else if (id == START.getId()) {
                algo = START;
            }
            return algo;
        }

        public static TaskStatus getViaText(String text) {
            TaskStatus algo = null;
            if (text.equalsIgnoreCase(OPEN.getValue())) {
                algo = OPEN;
            } else if (text.equalsIgnoreCase(CLOSE.getValue())) {
                algo = CLOSE;
            } else if (text.equalsIgnoreCase(COMPLETE.getValue())) {
                algo = COMPLETE;
            } else if (text.equalsIgnoreCase(RE_OPEN.getValue())) {
                algo = RE_OPEN;
            } else if (text.equalsIgnoreCase(FOLLOW_UP.getValue())) {
                algo = FOLLOW_UP;
            } else if (text.equalsIgnoreCase(CANCEL.getValue())) {
                algo = CANCEL;
            } else if (text.equalsIgnoreCase(START.getValue())) {
                algo = START;
            }
            return algo;
        }
    }

    public enum ITEM_MAKE_ENTRY_TYPE implements Serializable, IsSerializable {

        MAKE(0, "Make"), MARKETING(1, "Marketing");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        ITEM_MAKE_ENTRY_TYPE(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static ITEM_MAKE_ENTRY_TYPE getViaKey(int id) {
            ITEM_MAKE_ENTRY_TYPE algo = null;
            if (id == MAKE.getId()) {
                algo = MAKE;
            } else if (id == MARKETING.getId()) {
                algo = MARKETING;
            }
            return algo;
        }

        public static ITEM_MAKE_ENTRY_TYPE getViaText(String text) {
            ITEM_MAKE_ENTRY_TYPE algo = null;
            if (text.equalsIgnoreCase(MAKE.getValue())) {
                algo = MAKE;
            } else if (text.equalsIgnoreCase(MARKETING.getValue())) {
                algo = MARKETING;
            }
            return algo;
        }
    }

//    public enum OTBookingType implements Serializable, IsSerializable {
//
//        OT_Direct(1, "Direct"), OT_VIA_Booking(2, "Booking");
//
//        private final int id;
//        private final String value;
//
//        public int getId() {
//            return id;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        OTBookingType(int id, String text) {
//            this.id = id;
//            this.value = text;
//        }
//
//        public static OTBookingType getViaKey(int id) {
//            OTBookingType algo = null;
//            if (id == OT_Direct.getId()) {
//                algo = OT_Direct;
//            } else if (id == OT_VIA_Booking.getId()) {
//                algo = OT_VIA_Booking;
//            }
//            return algo;
//        }
//
//        public static OTBookingType getViaText(String text) {
//            OTBookingType algo = null;
//            if (text.equalsIgnoreCase(OT_Direct.getValue())) {
//                algo = OT_Direct;
//            } else if (text.equalsIgnoreCase(OT_VIA_Booking.getValue())) {
//                algo = OT_VIA_Booking;
//            }
//            return algo;
//        }
//
//    }
    public enum AssetVehicleNature implements Serializable, IsSerializable {

        GENERAL(1, "General"), VEHICLE(2, "Vehicle"), AMBULANCE(3, "Ambulance"), SUB_ASSET(4, "Sub-Asset");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        AssetVehicleNature(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static AssetVehicleNature getViaKey(int id) {
            AssetVehicleNature algo = null;
            if (id == GENERAL.getId()) {
                algo = GENERAL;
            } else if (id == VEHICLE.getId()) {
                algo = VEHICLE;
            } else if (id == AMBULANCE.getId()) {
                algo = AMBULANCE;
            } else if (id == SUB_ASSET.getId()) {
                algo = SUB_ASSET;
            }
            return algo;
        }

        public static AssetVehicleNature getViaText(String text) {
            AssetVehicleNature algo = null;
            if (text.equalsIgnoreCase(GENERAL.getValue())) {
                algo = GENERAL;
            } else if (text.equalsIgnoreCase(VEHICLE.getValue())) {
                algo = VEHICLE;
            } else if (text.equalsIgnoreCase(AMBULANCE.getValue())) {
                algo = AMBULANCE;
            } else if (text.equalsIgnoreCase(SUB_ASSET.getValue())) {
                algo = SUB_ASSET;
            }
            return algo;
        }

    }

    public enum CustomWidgetNature implements Serializable, IsSerializable {

        PHYSICAL_EXAMINATION("PEX", "Physical Examination"), COMPLAINTS("CPT", "Complaints"), COMORBID("CBD", "Comorbid"), FAMILY_HISTORY("FHX", "Family History"),
        HABIT("HBT", "Habit"), HISTORY_OF_PAST_ILLNESS("PSI", "History Of Past Illness"), HISTORY_OF_PRESENT_ILLNESS("PRI", "History Of Present Illness"),
        SYSTEMIC_EXAMINATION("SEX", "Systemic Examination"), INSTRUCTION("INS", "Instruction"), MEDICINE_INSTRUCTION("MIN", "Medicine Instruction"),
        PRECAUTION("PRC", "Precaution"), URGENT_CARE("UCR", "Urgent Care"), PERSONAL_HISTORY("PHS", "Personal History"), OBSTETRICS_HISTORY("OBH", "Obstetrics History"),
        MENSTRUAL_HISTORY("MSH", "Menstrual History"), OBSTETRIC_EXAMINATION("OBE", "Obstetric Examination"), GYNAECOLOGICAL_EXAMINATION("GYE", "Gynaecological Examination"),
        MENSTRUAL_EXAMINATION("MSE", "Menstrual Examination"), DEVELOPMENT_HISTORY("DVH", "Development History"), PERINATAL_HISTORY("PNH", "Perinatal History"),
        IMMUNIZATION_HISTORY("IMH", "Immunization History"), FEEDING_HISTORY("FDH", "Feeding History");

        private final String id;
        private final String value;

        public String getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        CustomWidgetNature(String id, String text) {
            this.id = id;
            this.value = text;
        }

        public static CustomWidgetNature getKeyOf(String id) {
            CustomWidgetNature widgetNature = null;
            if (id.equals(PHYSICAL_EXAMINATION.getId())) {
                widgetNature = PHYSICAL_EXAMINATION;
            } else if (id.equals(COMPLAINTS.getId())) {
                widgetNature = COMPLAINTS;
            } else if (id.equals(COMORBID.getId())) {
                widgetNature = COMORBID;
            } else if (id.equals(FAMILY_HISTORY.getId())) {
                widgetNature = FAMILY_HISTORY;
            } else if (id.equals(HABIT.getId())) {
                widgetNature = HABIT;
            } else if (id.equals(HISTORY_OF_PAST_ILLNESS.getId())) {
                widgetNature = HISTORY_OF_PAST_ILLNESS;
            } else if (id.equals(HISTORY_OF_PRESENT_ILLNESS.getId())) {
                widgetNature = HISTORY_OF_PRESENT_ILLNESS;
            } else if (id.equals(SYSTEMIC_EXAMINATION.getId())) {
                widgetNature = SYSTEMIC_EXAMINATION;
            } else if (id.equals(INSTRUCTION.getId())) {
                widgetNature = INSTRUCTION;
            } else if (id.equals(MEDICINE_INSTRUCTION.getId())) {
                widgetNature = MEDICINE_INSTRUCTION;
            } else if (id.equals(PRECAUTION.getId())) {
                widgetNature = PRECAUTION;
            } else if (id.equals(URGENT_CARE.getId())) {
                widgetNature = URGENT_CARE;
            } else if (id.equals(PERSONAL_HISTORY.getId())) {
                widgetNature = PERSONAL_HISTORY;
            } else if (id.equals(OBSTETRICS_HISTORY.getId())) {
                widgetNature = OBSTETRICS_HISTORY;
            } else if (id.equals(MENSTRUAL_HISTORY.getId())) {
                widgetNature = MENSTRUAL_HISTORY;
            } else if (id.equals(OBSTETRIC_EXAMINATION.getId())) {
                widgetNature = OBSTETRIC_EXAMINATION;
            } else if (id.equals(GYNAECOLOGICAL_EXAMINATION.getId())) {
                widgetNature = GYNAECOLOGICAL_EXAMINATION;
            } else if (id.equals(MENSTRUAL_EXAMINATION.getId())) {
                widgetNature = MENSTRUAL_EXAMINATION;
            } else if (id.equals(DEVELOPMENT_HISTORY.getId())) {
                widgetNature = DEVELOPMENT_HISTORY;
            } else if (id.equals(PERINATAL_HISTORY.getId())) {
                widgetNature = PERINATAL_HISTORY;
            } else if (id.equals(IMMUNIZATION_HISTORY.getId())) {
                widgetNature = IMMUNIZATION_HISTORY;
            } else if (id.equals(FEEDING_HISTORY.getId())) {
                widgetNature = FEEDING_HISTORY;
            }

            return widgetNature;
        }

        public static CustomWidgetNature getValueOf(String text) {
            CustomWidgetNature widgetNature = null;
            if (text.equals(PHYSICAL_EXAMINATION.getValue())) {
                widgetNature = PHYSICAL_EXAMINATION;
            } else if (text.equals(COMPLAINTS.getValue())) {
                widgetNature = COMPLAINTS;
            } else if (text.equals(COMORBID.getValue())) {
                widgetNature = COMORBID;
            } else if (text.equals(FAMILY_HISTORY.getValue())) {
                widgetNature = FAMILY_HISTORY;
            } else if (text.equals(HABIT.getValue())) {
                widgetNature = HABIT;
            } else if (text.equals(HISTORY_OF_PAST_ILLNESS.getValue())) {
                widgetNature = HISTORY_OF_PAST_ILLNESS;
            } else if (text.equals(HISTORY_OF_PRESENT_ILLNESS.getValue())) {
                widgetNature = HISTORY_OF_PRESENT_ILLNESS;
            } else if (text.equals(SYSTEMIC_EXAMINATION.getValue())) {
                widgetNature = SYSTEMIC_EXAMINATION;
            } else if (text.equals(INSTRUCTION.getValue())) {
                widgetNature = INSTRUCTION;
            } else if (text.equals(MEDICINE_INSTRUCTION.getValue())) {
                widgetNature = MEDICINE_INSTRUCTION;
            } else if (text.equals(PRECAUTION.getValue())) {
                widgetNature = PRECAUTION;
            } else if (text.equals(URGENT_CARE.getValue())) {
                widgetNature = URGENT_CARE;
            } else if (text.equals(PERSONAL_HISTORY.getValue())) {
                widgetNature = PERSONAL_HISTORY;
            } else if (text.equals(OBSTETRICS_HISTORY.getValue())) {
                widgetNature = OBSTETRICS_HISTORY;
            } else if (text.equals(MENSTRUAL_HISTORY.getValue())) {
                widgetNature = MENSTRUAL_HISTORY;
            } else if (text.equals(OBSTETRIC_EXAMINATION.getValue())) {
                widgetNature = OBSTETRIC_EXAMINATION;
            } else if (text.equals(GYNAECOLOGICAL_EXAMINATION.getValue())) {
                widgetNature = GYNAECOLOGICAL_EXAMINATION;
            } else if (text.equals(MENSTRUAL_EXAMINATION.getValue())) {
                widgetNature = MENSTRUAL_EXAMINATION;
            } else if (text.equals(DEVELOPMENT_HISTORY.getValue())) {
                widgetNature = DEVELOPMENT_HISTORY;
            } else if (text.equals(PERINATAL_HISTORY.getValue())) {
                widgetNature = PERINATAL_HISTORY;
            } else if (text.equals(IMMUNIZATION_HISTORY.getValue())) {
                widgetNature = IMMUNIZATION_HISTORY;
            } else if (text.equals(FEEDING_HISTORY.getValue())) {
                widgetNature = FEEDING_HISTORY;
            }

            return widgetNature;
        }
    }

    public enum CustomMultiLineNature implements Serializable, IsSerializable {

        DIAGNOSIS("DIA", "Diagnosis"), HOSPITAL_SUMMARY("HOS", "Hospital Summary"), FINAL_DIAGNOSIS("FDI", "Final Diagnosis");

        private final String id;
        private final String value;

        public String getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        CustomMultiLineNature(String id, String text) {
            this.id = id;
            this.value = text;
        }

        public static CustomMultiLineNature getKeyOf(String id) {
            for (CustomMultiLineNature value : CustomMultiLineNature.values()) {
                if (DmShared.xNull(id).trim().equalsIgnoreCase(value.id)) {
                    return value;
                }
            }
            return null;
        }

        public static CustomMultiLineNature getValueOf(String text) {
            for (CustomMultiLineNature value : CustomMultiLineNature.values()) {
                if (DmShared.xNull(text).trim().equalsIgnoreCase(value.value)) {
                    return value;
                }
            }
            return null;
        }
    }

    public enum QueueAlgoType implements Serializable, IsSerializable {

        EMERGENCY(1, "Emergency"), GENERAL(2, "General"), APPOINTMENT(3, "Appointment"), REPORTS(4, "Reports"), SKIPPED(5, "Skipped"), PAY_CLINIC(6, "Pay Clinic");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        QueueAlgoType(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static QueueAlgoType getKeyOf(int id) {
            QueueAlgoType algo = null;
            if (id == EMERGENCY.getId()) {
                algo = EMERGENCY;
            } else if (id == GENERAL.getId()) {
                algo = GENERAL;
            } else if (id == APPOINTMENT.getId()) {
                algo = APPOINTMENT;
            } else if (id == REPORTS.getId()) {
                algo = REPORTS;
            } else if (id == SKIPPED.getId()) {
                algo = SKIPPED;
            } else if (id == PAY_CLINIC.getId()) {
                algo = PAY_CLINIC;
            }
            return algo;
        }

        public static QueueAlgoType getValueOf(String text) {
            QueueAlgoType returnAlgo = null;
            if (text.equals(EMERGENCY.getValue())) {
                returnAlgo = EMERGENCY;
            } else if (text.equals(EMERGENCY.getValue())) {
                returnAlgo = EMERGENCY;
            } else if (text.equals(GENERAL.getValue())) {
                returnAlgo = GENERAL;
            } else if (text.equals(APPOINTMENT.getValue())) {
                returnAlgo = APPOINTMENT;
            } else if (text.equals(REPORTS.getValue())) {
                returnAlgo = REPORTS;
            } else if (text.equals(SKIPPED.getValue())) {
                returnAlgo = SKIPPED;
            } else if (text.equals(PAY_CLINIC.getValue())) {
                returnAlgo = PAY_CLINIC;
            }
            return returnAlgo;
        }
    }

    public enum QueueAlgoNature implements Serializable, IsSerializable {

        SEQUENCE(1, "Sequence");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        QueueAlgoNature(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static QueueAlgoNature getKeyOf(int id) {
            QueueAlgoNature algo = null;
            if (id == SEQUENCE.getId()) {
                algo = SEQUENCE;
            }
            return algo;
        }

        public static QueueAlgoNature getValueOf(String text) {
            QueueAlgoNature returnAlgo = null;
            if (text.equals(SEQUENCE.getValue())) {
                returnAlgo = SEQUENCE;
            }
            return returnAlgo;
        }
    }

    public enum TokenBlockNature implements Serializable, IsSerializable {

        NONE(0, "None"), EVEN_TOKEN(1, "Even Tokens"), ODD_TOKEN(3, "Odd Tokens");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        TokenBlockNature(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static TokenBlockNature getKeyOf(int id) {
            TokenBlockNature returnMedSale = null;
            if (id == NONE.getId()) {
                returnMedSale = NONE;
            } else if (id == EVEN_TOKEN.getId()) {
                returnMedSale = EVEN_TOKEN;
            } else if (id == ODD_TOKEN.getId()) {
                returnMedSale = ODD_TOKEN;
            }
            return returnMedSale;
        }

        public static TokenBlockNature getValueOf(String text) {
            TokenBlockNature returnMedSale = null;

            if (text.equals(NONE.getValue())) {
                returnMedSale = NONE;
            } else if (text.equals(EVEN_TOKEN.getValue())) {
                returnMedSale = EVEN_TOKEN;
            } else if (text.equals(ODD_TOKEN.getValue())) {
                returnMedSale = ODD_TOKEN;
            }
            return returnMedSale;
        }
    }

    public enum MedicineSaleOn implements Serializable, IsSerializable {

        SALE_RATE(0, "Sale Rate"), MRP(1, "MRP"), COST_PRICE(2, "Cost Price"), WHOLESALE_RATE(3, "Wholesale Rate");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        MedicineSaleOn(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static MedicineSaleOn getKeyOf(int id) {
            MedicineSaleOn returnMedSale = null;
            if (id == MRP.getId()) {
                returnMedSale = MRP;
            } else if (id == SALE_RATE.getId()) {
                returnMedSale = SALE_RATE;
            } else if (id == COST_PRICE.getId()) {
                returnMedSale = COST_PRICE;
            } else if (id == WHOLESALE_RATE.getId()) {
                returnMedSale = WHOLESALE_RATE;
            }
            return returnMedSale;
        }

        public static MedicineSaleOn getValueOf(String text) {
            MedicineSaleOn returnMedSale = null;

            if (text.equals(MRP.getValue())) {
                returnMedSale = MRP;
            } else if (text.equals(SALE_RATE.getValue())) {
                returnMedSale = SALE_RATE;
            } else if (text.equals(COST_PRICE.getValue())) {
                returnMedSale = COST_PRICE;
            } else if (text.equals(WHOLESALE_RATE.getValue())) {
                returnMedSale = WHOLESALE_RATE;
            }
            return returnMedSale;
        }
    }

    public enum DischargeStatus implements Serializable, IsSerializable {

        IMPROVED(1, "Improved"), RECOVERED(2, "Recovered"), BIRTH(3, "Birth"),
        NO_CHANGE(4, "No Change"), REFERRED(5, "Referred"), EXPIRED(6, "Expired"),
        LAMA(7, "L.A.M.A."), DOR(8, "DOPR"), DNTU(9, "DNTU"), TRANSFERRED(10, "Transferred"),
        SELF_DISCHARGE(11, "Self Discharge"), DAMA(12, "DAMA"), BROUGHT_DEAD(14, "Brought Dead");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        DischargeStatus(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static DischargeStatus getKeyOf(int id) {
            DischargeStatus returnStatus = null;
            if (id == IMPROVED.getId()) {
                returnStatus = IMPROVED;
            } else if (id == RECOVERED.getId()) {
                returnStatus = RECOVERED;
            } else if (id == BIRTH.getId()) {
                returnStatus = BIRTH;
            } else if (id == NO_CHANGE.getId()) {
                returnStatus = NO_CHANGE;
            } else if (id == REFERRED.getId()) {
                returnStatus = REFERRED;
            } else if (id == EXPIRED.getId()) {
                returnStatus = EXPIRED;
            } else if (id == LAMA.getId()) {
                returnStatus = LAMA;
            } else if (id == DOR.getId()) {
                returnStatus = DOR;
            } else if (id == DNTU.getId()) {
                returnStatus = DNTU;
            } else if (id == TRANSFERRED.getId()) {
                returnStatus = TRANSFERRED;
            } else if (id == SELF_DISCHARGE.getId()) {
                returnStatus = SELF_DISCHARGE;
            } else if (id == DAMA.getId()) {
                returnStatus = DAMA;
            } else if (id == BROUGHT_DEAD.getId()) {
                returnStatus = BROUGHT_DEAD;
            }

            return returnStatus;
        }

        public static DischargeStatus getValueOf(String text) {
            DischargeStatus returnStatus = null;

            if (text.equals(IMPROVED.getValue())) {
                returnStatus = IMPROVED;
            } else if (text.equals(RECOVERED.getValue())) {
                returnStatus = RECOVERED;
            } else if (text.equals(BIRTH.getValue())) {
                returnStatus = BIRTH;
            } else if (text.equals(NO_CHANGE.getValue())) {
                returnStatus = NO_CHANGE;
            } else if (text.equals(REFERRED.getValue())) {
                returnStatus = REFERRED;
            } else if (text.equals(EXPIRED.getValue())) {
                returnStatus = EXPIRED;
            } else if (text.equals(LAMA.getValue())) {
                returnStatus = LAMA;
            } else if (text.equals(DOR.getValue())) {
                returnStatus = DOR;
            } else if (text.equals(TRANSFERRED.getValue())) {
                returnStatus = TRANSFERRED;
            } else if (text.equals(SELF_DISCHARGE.getValue())) {
                returnStatus = SELF_DISCHARGE;
            } else if (text.equals(DAMA.getValue())) {
                returnStatus = DAMA;
            } else if (text.equals(BROUGHT_DEAD.getValue())) {
                returnStatus = BROUGHT_DEAD;
            }

            return returnStatus;
        }
    }

    public enum BillingNature implements Serializable, IsSerializable {

        GENERAL(0, "General"), BILLABLE(1, "Billable"), NON_BILLABLE(2, "Non-Billable");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        BillingNature(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static BillingNature getKeyOf(int id) {
            BillingNature rtnValue = null;
            if (id == GENERAL.getId()) {
                rtnValue = GENERAL;
            } else if (id == BILLABLE.getId()) {
                rtnValue = BILLABLE;
            } else if (id == NON_BILLABLE.getId()) {
                rtnValue = NON_BILLABLE;
            }
            return rtnValue;
        }

        public static BillingNature getValueOf(String text) {
            BillingNature rtnValue = null;

            if (text.equalsIgnoreCase(GENERAL.getValue())) {
                rtnValue = GENERAL;
            } else if (text.equalsIgnoreCase(BILLABLE.getValue())) {
                rtnValue = BILLABLE;
            } else if (text.equalsIgnoreCase(NON_BILLABLE.getValue())) {
                rtnValue = NON_BILLABLE;
            }
            return rtnValue;
        }
    }

    public enum ServiceNature implements Serializable, IsSerializable {

        SERVICE(0, "Service"), PATHOLOGY_TEST(1, "Pathology Test"), OPERATION(2, "Operation"), PACKAGE(6, "Package"),
        RADIOLOGY_TEST(3, "Radiology Test"), OVERHEAD_SERVICE(4, "Overhead Service"), PATHOLOGY_PANEL(5, "Panel");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        ServiceNature(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static ServiceNature getKeyOf(int id) {
            for (ServiceNature nature : ServiceNature.values()) {
                if (id == nature.id) {
                    return nature;
                }
            }
            return null;
        }

        public static ServiceNature getValueOf(String text) {
            for (ServiceNature nature : ServiceNature.values()) {
                if (text.equalsIgnoreCase(nature.getValue())) {
                    return nature;
                }
            }
            return null;
        }
    }

    public enum MappingNature implements Serializable, IsSerializable {

        // OT_PRE_ANESTHESIA(0, "OT Pre Anesthesia"), OT_PRE_OPERATION(1, "OT Pre Operation"), OT_OPERATION_START(2, "OT Operation Start"),
        // OT_ANESTHESIA(3, "OT Anesthesia"), OT_OPERATION(4, "OT Operation"), OT_POST_OPERATION(5, "OT Post Operation"), OT_POST_ANESTHESIA(6, "OT Post Anesthesia"),
        NURSING_ROUTINE(7, "Nursing Routine"), OT_STERILIZATION(8, "OT Sterilization"), NABH_INDICATOR_1(9, "NABH Indicator 1"), NABH_INDICATOR_2(10, "NABH Indicator 2"),
        NABH_INDICATOR_3(11, "NABH Indicator 3"), NABH_INDICATOR_4(12, "NABH Indicator 4"), NABH_INDICATOR_5(13, "NABH Indicator 5"), NABH_INDICATOR_6(14, "NABH Indicator 6"),
        NABH_INDICATOR_7(15, "NABH Indicator 7"), NABH_INDICATOR_8(16, "NABH Indicator 8"), NABH_INDICATOR_9(17, "NABH Indicator 9"), NABH_INDICATOR_10(18, "NABH Indicator 10"),
        NABH_INDICATOR_11(19, "NABH Indicator 11"), NABH_INDICATOR_12(20, "NABH Indicator 12"), NABH_INDICATOR_13(21, "NABH Indicator 13"), NABH_INDICATOR_14(22, "NABH Indicator 14"),
        NABH_INDICATOR_15(23, "NABH Indicator 15"), NABH_INDICATOR_16(24, "NABH Indicator 16"), NABH_INDICATOR_17(25, "NABH Indicator 17"), NABH_INDICATOR_18(26, "NABH Indicator 18"),
        NABH_INDICATOR_19(27, "NABH Indicator 19"), NABH_INDICATOR_20(28, "NABH Indicator 20"), NABH_INDICATOR_21(29, "NABH Indicator 21"), NABH_INDICATOR_22(30, "NABH Indicator 22"),
        NABH_INDICATOR_23(31, "NABH Indicator 23"), NABH_INDICATOR_24(32, "NABH Indicator 24"), NABH_INDICATOR_25(33, "NABH Indicator 25"), NABH_INDICATOR_26(34, "NABH Indicator 26"),
        NABH_INDICATOR_27(35, "NABH Indicator 27"), NABH_INDICATOR_28(36, "NABH Indicator 28"), NABH_INDICATOR_29(37, "NABH Indicator 29"), NABH_INDICATOR_30(38, "NABH Indicator 30"),
        NABH_INDICATOR_31(39, "NABH Indicator 31"), NABH_INDICATOR_32(40, "NABH Indicator 32"), NABH_INDICATOR_33(41, "NABH Indicator 33"), NABH_INDICATOR_34(42, "NABH Indicator 34"),
        NABH_INDICATOR_35(43, "NABH Indicator 35"), NABH_INDICATOR_36(44, "NABH Indicator 36"), NABH_INDICATOR_37(45, "NABH Indicator 37"), NABH_INDICATOR_38(46, "NABH Indicator 38"),
        NABH_INDICATOR_39(47, "NABH Indicator 39"), NABH_INDICATOR_40(48, "NABH Indicator 40"), NABH_INDICATOR_41(49, "NABH Indicator 41"), NABH_INDICATOR_42(50, "NABH Indicator 42"),
        NABH_INDICATOR_43(51, "NABH Indicator 43"), NABH_INDICATOR_44(52, "NABH Indicator 44"), NABH_INDICATOR_45(53, "NABH Indicator 45"), NABH_INDICATOR_46(54, "NABH Indicator 46"),
        NABH_INDICATOR_47(55, "NABH Indicator 47"), NABH_INDICATOR_48(56, "NABH Indicator 48"), NABH_INDICATOR_49(57, "NABH Indicator 49"), NABH_INDICATOR_50(58, "NABH Indicator 50"),
        NABH_INDICATOR_51(59, "NABH Indicator 51"), NABH_INDICATOR_52(60, "NABH Indicator 52"), NABH_INDICATOR_53(61, "NABH Indicator 53"), NABH_INDICATOR_54(62, "NABH Indicator 54"),
        NABH_INDICATOR_55(63, "NABH Indicator 55"), NABH_INDICATOR_56(64, "NABH Indicator 56"), NABH_INDICATOR_57(65, "NABH Indicator 57"), NABH_INDICATOR_58(66, "NABH Indicator 58"),
        NABH_INDICATOR_59(67, "NABH Indicator 59"), NABH_INDICATOR_60(68, "NABH Indicator 60"), NABH_INDICATOR_61(69, "NABH Indicator 61"), NABH_INDICATOR_62(70, "NABH Indicator 62"),
        NABH_INDICATOR_63(71, "NABH Indicator 63"), NABH_INDICATOR_64(72, "NABH Indicator 64"), NABH_INDICATOR_65(73, "NABH Indicator 65"), NABH_INDICATOR_66(74, "NABH Indicator 66"),
        NABH_INDICATOR_67(75, "NABH Indicator 67"), NABH_INDICATOR_68(76, "NABH Indicator 68"), NABH_INDICATOR_69(77, "NABH Indicator 69"), NABH_INDICATOR_70(78, "NABH Indicator 70"),
        POINT_Of_Care(79, "Point of Care");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        MappingNature(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static MappingNature getNatureValue(String natureValue) {
            MappingNature mapping = null;
            for (MappingNature mn : MappingNature.values()) {
                if (mn.getValue().equalsIgnoreCase(natureValue)) {
                    mapping = mn;
                    break;
                }
            }
            return mapping;
        }

        public static MappingNature getNatureId(int natureId) {
            MappingNature mapping = null;
            for (MappingNature mn : MappingNature.values()) {
                if (mn.getId() == natureId) {
                    mapping = mn;
                    break;
                }
            }
            return mapping;
        }
    }

    public enum PaymentSource implements Serializable, IsSerializable {
        PAYMENT("Payment", 0), PARTIAL_PAYMENT("Partial Payment", 1), CREDIT("Credit", 2), NO_PAYMENT("No Payment", 10),
        CREDIT_TO_PATIENT("Credit To Patient", 20), CREDIT_TO_ORGANIZATION("Credit To Organization", 30),
        CREDIT_TO_REGISTRATION("Credit To Registration", 40), ORGANIZATION_LIMIT_APPROVAL("Organization Limit Approval", 50);

        private final String enumName;
        private final int enumValue;

        public String getEnumName() {
            return this.enumName;
        }

        public int getEnumValue() {
            return this.enumValue;
        }

        public static PaymentSource getValueOf(String chartId) {
            PaymentSource required = null;
            for (PaymentSource cardType : PaymentSource.values()) {
                if (!DmBaseShared.xNull(chartId).equalsIgnoreCase("") && DmBaseShared.xNull(cardType.getEnumName()).equalsIgnoreCase(chartId)) {
                    required = cardType;
                    break;
                }
            }
            return required;
        }

        public static PaymentSource getValueOf(int enumValue) {
            PaymentSource required = null;

            switch (DmBaseShared.iNull(enumValue).intValue()) {
                case 0:
                    required = PAYMENT;
                    break;
                case 1:
                    required = PARTIAL_PAYMENT;
                    break;

                case 2:
                    required = CREDIT;
                    break;

                case 10:
                    required = NO_PAYMENT;
                    break;
                case 20:
                    required = CREDIT_TO_PATIENT;
                    break;
                case 30:
                    required = CREDIT_TO_ORGANIZATION;
                    break;
                case 40:
                    required = CREDIT_TO_REGISTRATION;
                    break;
                case 50:
                    required = ORGANIZATION_LIMIT_APPROVAL;
                    break;
                default:
                    break;
            }

            return required;
        }

        private PaymentSource(String enumName, int enumValue) {
            this.enumValue = enumValue;
            this.enumName = enumName.trim();
        }
    }

    public enum LocalSettings implements Serializable, IsSerializable {

        PRESCRIPTION_PRINTER("prescriptionPrinter");

        private String optionValue;

        private LocalSettings(String optionValue) {
            this.optionValue = optionValue;
        }

        public String toString() {
            return optionValue;
        }
    }

    public enum PriceListEntryFrom implements Serializable, IsSerializable {

        NONE(0), CONSULTANT_MASTER(2), ORGANIZATION_MASTER(3), RATE_LIST_MASTER(4), PRICELIST_MASTER(5), PARTY_PRICELIST(6);

        private int optionValue;

        private PriceListEntryFrom(int optionValue) {
            this.optionValue = optionValue;
        }

        public int toInt() {
            return optionValue;
        }
    }

    public enum PriceListNature implements Serializable, IsSerializable {

        None(0, "None"), OPD(1, "OPD"), Emergency(2, "Emergency"), ReVisit(3, "Re-Visit"), PreAppointment(4, "Appointment"),
        Online(5, "Online"), AppointmentAdvance(6, "Appointment Advance"), PayClinic(7, "Pay Clinic"), Special(8, "Special");

        private int id;
        private String text;

        public String getText() {
            return text;
        }

        public int getId() {
            return id;
        }

        PriceListNature(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public static PriceListNature getViaKey(int id) {
            for (PriceListNature value : PriceListNature.values()) {
                if (id == value.id) {
                    return value;
                }
            }
            return null;
        }

        public static PriceListNature getViaText(String text) {
            for (PriceListNature value : PriceListNature.values()) {
                if (DmShared.xNull(text).equalsIgnoreCase(value.text)) {
                    return value;
                }
            }
            return null;
        }
    }

//    public enum DiaryStatus implements Serializable, IsSerializable {
//
//        Waiting(0, "Initialize"), Cancel(1, "Cancel"), Registered(2, "Registered"), Attended(3, "Attended"), Postpone(4, "Postpone");
//
//        private final int optionValue;
//        private final String optionText;
//
//        private DiaryStatus(int statusValue, String statusText) {
//            this.optionValue = statusValue;
//            this.optionText = statusText;
//        }
//
//        public int getOptionValue() {
//            return optionValue;
//        }
//
//        public String getOptionText() {
//            return optionText;
//        }
//
//        public int toInt() {
//            return optionValue;
//        }
//
//        public static String valueOf(int valueId) {
//            String value = "";
//            if (valueId == Waiting.optionValue) {
//                value = Waiting.getOptionText();
//            } else if (valueId == Cancel.optionValue) {
//                value = Cancel.getOptionText();
//            } else if (valueId == Registered.optionValue) {
//                value = Registered.getOptionText();
//            } else if (valueId == Attended.optionValue) {
//                value = Attended.getOptionText();
//            } else if (valueId == Postpone.optionValue) {
//                value = Postpone.getOptionText();
//            }
//            return value;
//        }
//
//        public static DiaryStatus diaryStatus(int valueId) {
//            DiaryStatus value = null;
//            if (valueId == Waiting.optionValue) {
//                value = Waiting;
//            } else if (valueId == Cancel.optionValue) {
//                value = Cancel;
//            } else if (valueId == Registered.optionValue) {
//                value = Registered;
//            } else if (valueId == Attended.optionValue) {
//                value = Attended;
//            } else if (valueId == Postpone.optionValue) {
//                value = Postpone;
//            }
//            return value;
//        }
//    }
    public enum DiaryStatus implements Serializable, IsSerializable {

        Waiting(0, "Initialize"), Cancel(1, "Cancel"), Registered(2, "Registered"), Attended(3, "Attended"), Postpone(4, "Postpone"), Block(5, "Block"),
        BookedForOT(6, "Booked For OT"), Confirmed(7, "Confirmed"), Started(8, "Started"), Completed(9, "Completed");

        private final int optionValue;
        private final String optionText;

        private DiaryStatus(int statusValue, String statusText) {
            this.optionValue = statusValue;
            this.optionText = statusText;
        }

        public int getOptionValue() {
            return optionValue;
        }

        public String getOptionText() {
            return optionText;
        }

        public int toInt() {
            return optionValue;
        }

        public static String valueOf(int valueId) {
            String value = "";
            if (valueId == Waiting.optionValue) {
                value = Waiting.getOptionText();
            } else if (valueId == Cancel.optionValue) {
                value = Cancel.getOptionText();
            } else if (valueId == Registered.optionValue) {
                value = Registered.getOptionText();
            } else if (valueId == Attended.optionValue) {
                value = Attended.getOptionText();
            } else if (valueId == Postpone.optionValue) {
                value = Postpone.getOptionText();
            } else if (valueId == Block.optionValue) {
                value = Block.getOptionText();
            } else if (valueId == BookedForOT.optionValue) {
                value = BookedForOT.getOptionText();
            } else if (valueId == Confirmed.optionValue) {
                value = Confirmed.getOptionText();
            } else if (valueId == Started.optionValue) {
                value = Started.getOptionText();
            } else if (valueId == Completed.optionValue) {
                value = Completed.getOptionText();
            }
            return value;
        }

        public static DiaryStatus diaryStatus(int valueId) {
            DiaryStatus value = null;
            if (valueId == Waiting.optionValue) {
                value = Waiting;
            } else if (valueId == Cancel.optionValue) {
                value = Cancel;
            } else if (valueId == Registered.optionValue) {
                value = Registered;
            } else if (valueId == Attended.optionValue) {
                value = Attended;
            } else if (valueId == Postpone.optionValue) {
                value = Postpone;
            } else if (valueId == Block.optionValue) {
                value = Block;
            } else if (valueId == BookedForOT.optionValue) {
                value = BookedForOT;
            } else if (valueId == Confirmed.optionValue) {
                value = Confirmed;
            } else if (valueId == Started.optionValue) {
                value = Started;
            } else if (valueId == Completed.optionValue) {
                value = Completed;
            }
            return value;
        }
    }

    public enum ConsultantDiaryNature implements Serializable, IsSerializable {

        OPD(0, "OPD"), PERSONAL(4, "Personal");

        private final int optionValue;
        private final String optionName;

        private ConsultantDiaryNature(int optionValue, String optionName) {
            this.optionValue = optionValue;
            this.optionName = optionName;
        }

        public int toInt() {
            return optionValue;
        }

        public String getOptionName() {
            return optionName;
        }

        public static ConsultantDiaryNature valueOf(int valueId) {
            ConsultantDiaryNature cdn = null;

            if (valueId == OPD.toInt()) {
                cdn = OPD;
            } else if (valueId == PERSONAL.toInt()) {
                cdn = PERSONAL;
            }
            return cdn;
        }
    }

    public enum AllergyNature implements Serializable, IsSerializable {

        OTHERS(0, "Others"), DRUG(1, "Drug"), CHEMICAL(2, "Chemical"), FOOD(3, "Food"), ENVIRONMENT(4, "Environment");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        AllergyNature(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static AllergyNature getKeyOf(int id) {
            AllergyNature returnAllergy = null;
            if (id == OTHERS.getId()) {
                returnAllergy = OTHERS;
            } else if (id == DRUG.getId()) {
                returnAllergy = DRUG;
            } else if (id == CHEMICAL.getId()) {
                returnAllergy = CHEMICAL;
            } else if (id == FOOD.getId()) {
                returnAllergy = FOOD;
            } else if (id == ENVIRONMENT.getId()) {
                returnAllergy = ENVIRONMENT;
            }
            return returnAllergy;
        }

        public static AllergyNature getValueOf(String text) {
            AllergyNature returnAllergy = null;

            if (text.equals(OTHERS.getValue())) {
                returnAllergy = OTHERS;
            } else if (text.equals(DRUG.getValue())) {
                returnAllergy = DRUG;
            } else if (text.equals(CHEMICAL.getValue())) {
                returnAllergy = CHEMICAL;
            } else if (text.equals(FOOD.getValue())) {
                returnAllergy = FOOD;
            } else if (text.equals(ENVIRONMENT.getValue())) {
                returnAllergy = ENVIRONMENT;
            }
            return returnAllergy;
        }
    }

 public enum ProcessEOD implements Serializable, IsSerializable {

        OPD_AC_POSTING(1, "OPD A/c Posting"), IPD_AC_POSTING(2, "IPD A/c Posting"), DAY_CARE_AC_POSTING(3, "Day Care A/c Posting"),
        EMERGENCY_AC_POSTING(4, "Emergency A/c Posting"), DIAGNOSTIC_AC_POSTING(5, "Diagnostic A/c Posting"),
        ROOM_POSTING(6, "Room Posting"), LAB_CONSUMPTION(7, "Lab Consumption"), PHARMACY_AC_POSTING(8, "Pharmacy A/c Posting"),
        MIS_APP_UPDATION(9, "MIS App Updation"), EMail_SYSTEM_REPORT(10, "EMail System Report"),
        D365_To_HIS_GRNDataSync(11, "D365 To HIS Grn DataSync"), D365_To_HIS_ItemSync(13, "D365 To HIS ItemSync"),
        Comman_Scheduler(12, "Comman Scheduler");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        ProcessEOD(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static ProcessEOD getKeyOf(int id) {
            ProcessEOD rtnValue = null;
            if (id == OPD_AC_POSTING.getId()) {
                rtnValue = OPD_AC_POSTING;
            } else if (id == IPD_AC_POSTING.getId()) {
                rtnValue = IPD_AC_POSTING;
            } else if (id == DAY_CARE_AC_POSTING.getId()) {
                rtnValue = DAY_CARE_AC_POSTING;
            } else if (id == EMERGENCY_AC_POSTING.getId()) {
                rtnValue = EMERGENCY_AC_POSTING;
            } else if (id == DIAGNOSTIC_AC_POSTING.getId()) {
                rtnValue = DIAGNOSTIC_AC_POSTING;
            } else if (id == ROOM_POSTING.getId()) {
                rtnValue = ROOM_POSTING;
            } else if (id == LAB_CONSUMPTION.getId()) {
                rtnValue = LAB_CONSUMPTION;
            } else if (id == PHARMACY_AC_POSTING.getId()) {
                rtnValue = PHARMACY_AC_POSTING;
            } else if (id == MIS_APP_UPDATION.getId()) {
                rtnValue = MIS_APP_UPDATION;
            } else if (id == EMail_SYSTEM_REPORT.getId()) {
                rtnValue = EMail_SYSTEM_REPORT;
            } else if (id == D365_To_HIS_GRNDataSync.getId()) {
                rtnValue = D365_To_HIS_GRNDataSync;
            } else if (id == D365_To_HIS_ItemSync.getId()) {
                rtnValue = D365_To_HIS_ItemSync;
            } else if (id == Comman_Scheduler.getId()) {
                rtnValue = Comman_Scheduler;
            }
            return rtnValue;
        }

        public static ProcessEOD getValueOf(String text) {
            ProcessEOD rtnValue = null;

            if (text.equalsIgnoreCase(OPD_AC_POSTING.getValue())) {
                rtnValue = OPD_AC_POSTING;
            } else if (text.equalsIgnoreCase(IPD_AC_POSTING.getValue())) {
                rtnValue = IPD_AC_POSTING;
            } else if (text.equalsIgnoreCase(EMERGENCY_AC_POSTING.getValue())) {
                rtnValue = EMERGENCY_AC_POSTING;
            } else if (text.equalsIgnoreCase(DIAGNOSTIC_AC_POSTING.getValue())) {
                rtnValue = DIAGNOSTIC_AC_POSTING;
            } else if (text.equalsIgnoreCase(DAY_CARE_AC_POSTING.getValue())) {
                rtnValue = DAY_CARE_AC_POSTING;
            } else if (text.equalsIgnoreCase(ROOM_POSTING.getValue())) {
                rtnValue = ROOM_POSTING;
            } else if (text.equalsIgnoreCase(LAB_CONSUMPTION.getValue())) {
                rtnValue = LAB_CONSUMPTION;
            } else if (text.equalsIgnoreCase(PHARMACY_AC_POSTING.getValue())) {
                rtnValue = PHARMACY_AC_POSTING;
            } else if (text.equalsIgnoreCase(MIS_APP_UPDATION.getValue())) {
                rtnValue = MIS_APP_UPDATION;
            } else if (text.equalsIgnoreCase(EMail_SYSTEM_REPORT.getValue())) {
                rtnValue = EMail_SYSTEM_REPORT;
            } else if (text.equalsIgnoreCase(D365_To_HIS_GRNDataSync.getValue())) {
                rtnValue = D365_To_HIS_GRNDataSync;
            } else if (text.equalsIgnoreCase(D365_To_HIS_ItemSync.getValue())) {
                rtnValue = D365_To_HIS_ItemSync;
            } else if (text.equalsIgnoreCase(Comman_Scheduler.getValue())) {
                rtnValue = Comman_Scheduler;
            }
            return rtnValue;
        }
    }

    public enum ITSType implements Serializable, IsSerializable {
        PURCHASE("Purchase", 1),
        PURCHASE_RETURN("Purchase Return", 2),
        SALES("Sales", 3),
        SALES_RETURN("Sales Return", 4);

        private final int enumValue;
        private final String enumName;

        public String getValue() {
            return this.enumName;
        }

        public int getId() {
            return this.enumValue;
        }

        public static ITSType getValueFrom(String value) {
            ITSType itsType = null;
            if (PURCHASE.getValue().equalsIgnoreCase(value)) {
                itsType = PURCHASE;
            } else if (PURCHASE_RETURN.getValue().equalsIgnoreCase(value)) {
                itsType = PURCHASE_RETURN;
            } else if (SALES.getValue().equalsIgnoreCase(value)) {
                itsType = SALES;
            } else if (SALES_RETURN.getValue().equalsIgnoreCase(value)) {
                itsType = SALES_RETURN;
            }
            return itsType;
        }

        public static ITSType getValueFrom(int id) {
            ITSType itsType = null;
            if (PURCHASE.getId() == id) {
                itsType = PURCHASE;
            } else if (PURCHASE_RETURN.getId() == id) {
                itsType = PURCHASE_RETURN;
            } else if (SALES.getId() == id) {
                itsType = SALES;
            } else if (SALES_RETURN.getId() == id) {
                itsType = SALES_RETURN;
            }
            return itsType;
        }

        private ITSType(String enumName, int enumValue) {
            this.enumName = enumName;
            this.enumValue = enumValue;
        }
    }

    public enum PhysicalStockFilterType implements Serializable, IsSerializable {

        ITEM_CATEGORY(1, "Item Category", "Medicine Category"),
        ITEM_GROUP(2, "Item Group", "Medicine Group"),
        ITEM_MAKE(3, "Item Make", "Medicine Company"),
        ITEM(4, "Item", "Medicine"),
        RESTRICTED_GROUP(5, "Restricted Group", "Restricted Group");

        private final int id;
        private final String valueInventory;
        private final String valuePharmacy;

        public int getId() {
            return id;
        }

        public String getValueInventory() {
            return valueInventory;
        }

        public String getValuePharmacy() {
            return valuePharmacy;
        }

        private PhysicalStockFilterType(int id, String valueInventory, String valuePharmacy) {
            this.id = id;
            this.valueInventory = valueInventory;
            this.valuePharmacy = valuePharmacy;
        }

        public static PhysicalStockFilterType get(String value) {
            for (PhysicalStockFilterType filterType : PhysicalStockFilterType.values()) {
                if (filterType.getValueInventory().equalsIgnoreCase(DmShared.xNull(value).trim())
                        || filterType.getValuePharmacy().equalsIgnoreCase(DmShared.xNull(value).trim())) {
                    return filterType;
                }
            }
            return null;
        }

        public static PhysicalStockFilterType get(int id) {
            for (PhysicalStockFilterType filterType : PhysicalStockFilterType.values()) {
                if (filterType.getId() == DmShared.iNull(id).intValue()) {
                    return filterType;
                }
            }
            return null;
        }

    }

    public enum ItemBatchType implements Serializable, IsSerializable {

        ALL_BATCH(0, "All"),
        NON_EXPIRED_BATCH(1, "Non-Expired"),
        EXPIRED_BATCH(2, "Expired");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        private ItemBatchType(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public static ItemBatchType get(int id) {
            ItemBatchType required = null;

            switch (DmBaseShared.iNull(id).intValue()) {
                case 0:
                    required = ALL_BATCH;
                    break;
                case 1:
                    required = NON_EXPIRED_BATCH;
                    break;
                case 2:
                    required = EXPIRED_BATCH;
                    break;
                default:
                    break;
            }

            return required;
        }

        public static ItemBatchType get(String value) {
            ItemBatchType required = null;

            switch (DmBaseShared.xNull(value).toUpperCase()) {
                case "ALL":
                    required = ALL_BATCH;
                    break;
                case "NON-EXPIRED":
                    required = NON_EXPIRED_BATCH;
                    break;
                case "EXPIRED":
                    required = EXPIRED_BATCH;
                    break;
                default:
                    break;
            }

            return required;
        }

    }

    public enum GateType implements Serializable, IsSerializable {

        NON_RETURNABLE(0, "Non Returnabel"), RETURNABLE(1, "Returnabel"), PREVIOUS_GATE_IN_RETURNABLE(2, "Previous Gate In Returnable");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        GateType(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static GateType getViaKey(int id) {
            GateType algo = null;
            if (id == NON_RETURNABLE.getId()) {
                algo = NON_RETURNABLE;
            } else if (id == RETURNABLE.getId()) {
                algo = RETURNABLE;
            } else if (id == PREVIOUS_GATE_IN_RETURNABLE.getId()) {
                algo = PREVIOUS_GATE_IN_RETURNABLE;
            }
            return algo;
        }

        public static GateType getViaText(String text) {
            GateType algo = null;
            if (text.equalsIgnoreCase(NON_RETURNABLE.getValue())) {
                algo = NON_RETURNABLE;
            } else if (text.equalsIgnoreCase(RETURNABLE.getValue())) {
                algo = RETURNABLE;
            } else if (text.equalsIgnoreCase(PREVIOUS_GATE_IN_RETURNABLE.getValue())) {
                algo = PREVIOUS_GATE_IN_RETURNABLE;
            }
            return algo;
        }
    }

    public enum LabRequired implements Serializable, IsSerializable {

        YES(1, "Yes", "Y"), NO(2, "No", "N"), MAY_BE(3, "May Be", "M");

        private final int id;
        private final String value;
        private final String shortName;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public String getShortName() {
            return shortName;
        }

        LabRequired(int id, String text, String shortNm) {
            this.id = id;
            this.value = text;
            this.shortName = shortNm;
        }

        public static LabRequired getViaKey(int id) {
            LabRequired algo = null;
            if (id == NO.getId()) {
                algo = NO;
            } else if (id == YES.getId()) {
                algo = YES;
            } else if (id == MAY_BE.getId()) {
                algo = MAY_BE;

            }
            return algo;
        }

        public static LabRequired getViaText(String text) {
            LabRequired algo = null;
            if (text.equalsIgnoreCase(NO.getValue())) {
                algo = NO;
            } else if (text.equalsIgnoreCase(YES.getValue())) {
                algo = YES;
            } else if (text.equalsIgnoreCase(MAY_BE.getValue())) {
                algo = MAY_BE;
            }
            return algo;
        }

        public static LabRequired getViaShortName(String shortNm) {
            LabRequired algo = null;
            if (shortNm.equalsIgnoreCase(NO.getShortName())) {
                algo = NO;
            } else if (shortNm.equalsIgnoreCase(YES.getShortName())) {
                algo = YES;
            } else if (shortNm.equalsIgnoreCase(MAY_BE.getShortName())) {
                algo = MAY_BE;
            }
            return algo;
        }
    }

    public enum LabOverhead implements Serializable, IsSerializable {
        RANGE1("R1", "Range1"), RANGE2("R2", "Range2"), FOUND_VALUE("FV", "Found Value"), RECIEVE_QTY("RQ", "Receive Qty"), SUB_RESULT_QTY("SRQ", "Sub Result Qty"), ACCEPT_QTY("AQ", "Accept Qty"), RATE("RT", "Rate"), SUB_RESULT_AMOUNT("SRA", "Sub Result Amount");

        private final String code;
        private final String value;

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        LabOverhead(String code, String text) {
            this.code = code;
            this.value = text;
        }

        public static LabOverhead getViaKey(String code) {
            LabOverhead impurity = null;
            if (code.equals(RANGE1.getCode())) {
                impurity = RANGE1;
            } else if (code.equals(RANGE2.getCode())) {
                impurity = RANGE2;
            } else if (code.equals(FOUND_VALUE.getCode())) {
                impurity = FOUND_VALUE;
            } else if (code.equals(RECIEVE_QTY.getCode())) {
                impurity = RECIEVE_QTY;
            } else if (code.equals(SUB_RESULT_QTY.getCode())) {
                impurity = SUB_RESULT_QTY;
            } else if (code.equals(ACCEPT_QTY.getCode())) {
                impurity = ACCEPT_QTY;
            } else if (code.equals(RATE.getCode())) {
                impurity = RATE;
            } else if (code.equals(SUB_RESULT_AMOUNT.getCode())) {
                impurity = SUB_RESULT_AMOUNT;
            }
            return impurity;
        }

        public static LabOverhead getViaText(String text) {
            LabOverhead impurity = null;
            if (text.equalsIgnoreCase(RANGE1.getValue())) {
                impurity = RANGE1;
            } else if (text.equalsIgnoreCase(RANGE2.getValue())) {
                impurity = RANGE2;

            } else if (text.equalsIgnoreCase(FOUND_VALUE.getValue())) {
                impurity = FOUND_VALUE;

            } else if (text.equalsIgnoreCase(RECIEVE_QTY.getValue())) {
                impurity = RECIEVE_QTY;

            } else if (text.equalsIgnoreCase(SUB_RESULT_QTY.getValue())) {
                impurity = SUB_RESULT_QTY;

            } else if (text.equalsIgnoreCase(ACCEPT_QTY.getValue())) {
                impurity = ACCEPT_QTY;
            } else if (text.equalsIgnoreCase(RATE.getValue())) {
                impurity = RATE;

            } else if (text.equalsIgnoreCase(SUB_RESULT_AMOUNT.getValue())) {
                impurity = SUB_RESULT_AMOUNT;
            }
            return impurity;
        }
    }

    public enum OHManualCode implements Serializable, IsSerializable {

        G("G"), DISC("DISC"), IV("IV"), TV("TV"), SGST("SGST"), UGST("UGST"), CGST("CGST"), IGST("IGST"), SGSTRCM("SGSTRCM"),
        UGSTRCM("UGSTRCM"), CGSTRCM("CGSTRCM"), IGSTRCM("IGSTRCM"), BAMT("BAMT"),
        ROFF("ROFF"), NAMT("NAMT"), LV("LV");

        private final String manualCode;

        public String getManualCode() {
            return manualCode;
        }

        OHManualCode(String manualCode) {

            this.manualCode = manualCode;

        }

        public static OHManualCode getViaShortName(String manualCode) {
            OHManualCode algo = null;
            if (manualCode.equalsIgnoreCase(G.getManualCode())) {
                algo = G;
            } else if (manualCode.equalsIgnoreCase(DISC.getManualCode())) {
                algo = DISC;
            } else if (manualCode.equalsIgnoreCase(IV.getManualCode())) {
                algo = IV;
            } else if (manualCode.equalsIgnoreCase(TV.getManualCode())) {
                algo = TV;
            } else if (manualCode.equalsIgnoreCase(SGST.getManualCode())) {
                algo = SGST;
            } else if (manualCode.equalsIgnoreCase(UGST.getManualCode())) {
                algo = UGST;
            } else if (manualCode.equalsIgnoreCase(CGST.getManualCode())) {
                algo = CGST;
            } else if (manualCode.equalsIgnoreCase(IGST.getManualCode())) {
                algo = IGST;
            } else if (manualCode.equalsIgnoreCase(SGSTRCM.getManualCode())) {
                algo = SGSTRCM;
            } else if (manualCode.equalsIgnoreCase(UGSTRCM.getManualCode())) {
                algo = UGSTRCM;
            } else if (manualCode.equalsIgnoreCase(CGSTRCM.getManualCode())) {
                algo = CGSTRCM;
            } else if (manualCode.equalsIgnoreCase(IGSTRCM.getManualCode())) {
                algo = IGSTRCM;
            } else if (manualCode.equalsIgnoreCase(BAMT.getManualCode())) {
                algo = BAMT;
            } else if (manualCode.equalsIgnoreCase(NAMT.getManualCode())) {
                algo = NAMT;
            } else if (manualCode.equalsIgnoreCase(ROFF.getManualCode())) {
                algo = ROFF;
            } else if (manualCode.equalsIgnoreCase(LV.getManualCode())) {
                algo = LV;
            }

            return algo;
        }
    }

    public enum ImpurityNature implements Serializable, IsSerializable {

        QC(1, "QC"), LAB(2, "Lab");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        ImpurityNature(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static ImpurityNature getViaKey(int id) {
            ImpurityNature algo = null;
            if (id == LAB.getId()) {
                algo = LAB;
            } else if (id == QC.getId()) {
                algo = QC;
            }
            return algo;
        }

        public static ImpurityNature getViaText(String text) {
            ImpurityNature algo = null;
            if (text.equalsIgnoreCase(LAB.getValue())) {
                algo = LAB;
            } else if (text.equalsIgnoreCase(QC.getValue())) {
                algo = QC;
            }
            return algo;
        }
    }

    public enum ItemStatus implements Serializable, IsSerializable {

        INACTIVE(0, "Inactive"), ACTIVE(1, "Active"), HOLD(2, "Hold");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        ItemStatus(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static ItemStatus getViaKey(int id) {
            ItemStatus algo = null;
            if (id == INACTIVE.getId()) {
                algo = INACTIVE;
            } else if (id == ACTIVE.getId()) {
                algo = ACTIVE;
            } else if (id == HOLD.getId()) {
                algo = HOLD;
            }
            return algo;
        }

        public static ItemStatus getViaText(String text) {
            ItemStatus algo = null;
            if (text.equalsIgnoreCase(INACTIVE.getValue())) {
                algo = INACTIVE;
            } else if (text.equalsIgnoreCase(ACTIVE.getValue())) {
                algo = ACTIVE;
            } else if (text.equalsIgnoreCase(HOLD.getValue())) {
                algo = HOLD;
            }
            return algo;
        }
    }

    public enum ImpurityValueType implements Serializable, IsSerializable {

        RANGE(1, "Range", "RA"), HELP(2, "Help", "H"), REMARK(3, "Remark", "R");

        private final int id;
        private final String value;
        private final String shortName;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public String getShortName() {
            return shortName;
        }

        ImpurityValueType(int id, String text, String shortNm) {
            this.id = id;
            this.value = text;
            this.shortName = shortNm;
        }

        public static ImpurityValueType getViaKey(int id) {
            ImpurityValueType algo = null;
            if (id == REMARK.getId()) {
                algo = REMARK;
            } else if (id == HELP.getId()) {
                algo = HELP;
            } else if (id == RANGE.getId()) {
                algo = RANGE;
            }
            return algo;
        }

        public static ImpurityValueType getViaText(String text) {
            ImpurityValueType algo = null;
            if (text.equalsIgnoreCase(REMARK.getValue())) {
                algo = REMARK;
            } else if (text.equalsIgnoreCase(HELP.getValue())) {
                algo = HELP;
            } else if (text.equalsIgnoreCase(RANGE.getValue())) {
                algo = RANGE;
            }
            return algo;
        }

        public static ImpurityValueType getViaShortName(String shortNm) {
            ImpurityValueType algo = null;
            if (shortNm.equalsIgnoreCase(REMARK.getShortName())) {
                algo = REMARK;
            } else if (shortNm.equalsIgnoreCase(HELP.getShortName())) {
                algo = HELP;
            } else if (shortNm.equalsIgnoreCase(RANGE.getShortName())) {
                algo = RANGE;
            }
            return algo;
        }
    }

    public enum GatePurpose implements Serializable, IsSerializable {

        LOADING(1, "Loading"), UNLOADING(2, "Unloading");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        GatePurpose(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static GatePurpose getViaKey(int id) {
            GatePurpose algo = null;
            if (id == LOADING.getId()) {
                algo = LOADING;
            } else if (id == UNLOADING.getId()) {
                algo = UNLOADING;
            }
            return algo;
        }

        public static GatePurpose getViaText(String text) {
            GatePurpose algo = null;
            if (text.equalsIgnoreCase(LOADING.getValue())) {
                algo = LOADING;
            } else if (text.equalsIgnoreCase(UNLOADING.getValue())) {
                algo = UNLOADING;
            }
            return algo;
        }
    }

    public enum WeighmentType implements Serializable, IsSerializable {

        SINGLE(1, "Single"), MULTIPLE(2, "Multiple");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        WeighmentType(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static WeighmentType getViaKey(int id) {
            WeighmentType algo = null;
            if (id == SINGLE.getId()) {
                algo = SINGLE;
            } else if (id == MULTIPLE.getId()) {
                algo = MULTIPLE;
            }
            return algo;
        }

        public static WeighmentType getViaText(String text) {
            WeighmentType algo = null;
            if (text.equalsIgnoreCase(SINGLE.getValue())) {
                algo = SINGLE;
            } else if (text.equalsIgnoreCase(MULTIPLE.getValue())) {
                algo = MULTIPLE;
            }
            return algo;
        }
    }

    public enum PriorityDays implements Serializable, IsSerializable {

        LOW_PRIORITYDAYS(1, "Low"), HIGH_PRIORITYDAYS(3, "High"), MEDIUM_PRIORITYDAYS(HIGH_PRIORITYDAYS.getDays() - LOW_PRIORITYDAYS.getDays(), "Medium");

        private final int days;
        private final String value;

        public int getDays() {
            return days;
        }

        public String getValue() {
            return value;
        }

        PriorityDays(int days, String text) {
            this.days = days;
            this.value = text;
        }

        public static PriorityDays getViaKey(int id) {
            PriorityDays algo = null;
            if (id == LOW_PRIORITYDAYS.getDays()) {
                algo = LOW_PRIORITYDAYS;
            } else if (id == MEDIUM_PRIORITYDAYS.getDays()) {
                algo = MEDIUM_PRIORITYDAYS;
            } else if (id == HIGH_PRIORITYDAYS.getDays()) {
                algo = HIGH_PRIORITYDAYS;
            }
            return algo;
        }

        public static PriorityDays getViaText(String text) {
            PriorityDays algo = null;
            if (text.equalsIgnoreCase(LOW_PRIORITYDAYS.getValue())) {
                algo = LOW_PRIORITYDAYS;
            } else if (text.equalsIgnoreCase(MEDIUM_PRIORITYDAYS.getValue())) {
                algo = MEDIUM_PRIORITYDAYS;
            } else if (text.equalsIgnoreCase(HIGH_PRIORITYDAYS.getValue())) {
                algo = HIGH_PRIORITYDAYS;
            }
            return algo;
        }
    }

    public enum PurchaseSalesAcPostingBasedOn implements Serializable, IsSerializable {
        ITEM(1, "Item"), GSTS(2, "GST Structure");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        PurchaseSalesAcPostingBasedOn(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static PurchaseSalesAcPostingBasedOn getViaKey(int id) {
            PurchaseSalesAcPostingBasedOn p = null;
            if (id == ITEM.getId()) {
                p = ITEM;
            } else if (id == GSTS.getId()) {
                p = GSTS;
            }
            return p;
        }

        public static PurchaseSalesAcPostingBasedOn getViaText(String text) {
            PurchaseSalesAcPostingBasedOn p = null;

            if (text.equals(ITEM.getValue())) {
                p = ITEM;
            } else if (text.equals(GSTS.getValue())) {
                p = GSTS;
            }
            return p;
        }
    }

    public enum TransportMode implements Serializable, IsSerializable {

        BY_ROAD(1, "By Road", "Road"), BY_RAIL(2, "By Rail", "Rail"), BY_AIR(3, "By Air", "Air"), BY_SHIP(4, "By Ship", "Ship");

        private final int id;
        private final String value;
        private final String shortName;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public String getShortName() {
            return shortName;
        }

        TransportMode(int id, String text, String shortNm) {
            this.id = id;
            this.value = text;
            this.shortName = shortNm;
        }

        public static TransportMode getViaKey(int id) {
            TransportMode algo = null;
            if (id == BY_AIR.getId()) {
                algo = BY_AIR;
            } else if (id == BY_ROAD.getId()) {
                algo = BY_ROAD;
            } else if (id == BY_RAIL.getId()) {
                algo = BY_RAIL;
            } else if (id == BY_SHIP.getId()) {
                algo = BY_SHIP;
            }
            return algo;
        }

        public static TransportMode getViaText(String text) {
            TransportMode algo = null;
            if (text.equalsIgnoreCase(BY_AIR.getValue())) {
                algo = BY_AIR;
            } else if (text.equalsIgnoreCase(BY_ROAD.getValue())) {
                algo = BY_ROAD;
            } else if (text.equalsIgnoreCase(BY_RAIL.getValue())) {
                algo = BY_RAIL;
            } else if (text.equalsIgnoreCase(BY_SHIP.getValue())) {
                algo = BY_SHIP;
            }
            return algo;
        }

        public static TransportMode getViaShortName(String shortNm) {
            TransportMode algo = null;
            if (shortNm.equalsIgnoreCase(BY_AIR.getShortName())) {
                algo = BY_AIR;
            } else if (shortNm.equalsIgnoreCase(BY_ROAD.getShortName())) {
                algo = BY_ROAD;
            } else if (shortNm.equalsIgnoreCase(BY_RAIL.getShortName())) {
                algo = BY_RAIL;
            } else if (shortNm.equalsIgnoreCase(BY_SHIP.getShortName())) {
                algo = BY_SHIP;
            }
            return algo;
        }
    }

    public enum AssetTypeMode implements Serializable, IsSerializable {
        OPENING(1, "Opening"), NEW(2, "New");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        AssetTypeMode(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static AssetTypeMode getViaKey(int id) {
            AssetTypeMode p = null;
            if (id == OPENING.getId()) {
                p = OPENING;
            } else if (id == NEW.getId()) {
                p = NEW;
            }
            return p;
        }

        public static AssetTypeMode getViaText(String text) {
            AssetTypeMode p = null;

            if (text.equals(OPENING.getValue())) {
                p = OPENING;
            } else if (text.equals(NEW.getValue())) {
                p = NEW;
            }
            return p;
        }
    }

    public enum PriceListType implements Serializable, IsSerializable {

        FIX(1, "Fix", "Fix");

        private final int id;
        private final String value;
        private final String shortName;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public String getShortName() {
            return shortName;
        }

        PriceListType(int id, String text, String shortNm) {
            this.id = id;
            this.value = text;
            this.shortName = shortNm;
        }

        public static PriceListType getViaKey(int id) {
            PriceListType algo = null;
            if (id == FIX.getId()) {
                algo = FIX;
            }
            return algo;
        }

        public static PriceListType getViaText(String text) {
            PriceListType algo = null;
            if (text.equalsIgnoreCase(FIX.getValue())) {
                algo = FIX;
            }
            return algo;
        }

        public static PriceListType getViaShortName(String shortNm) {
            PriceListType algo = null;
            if (shortNm.equalsIgnoreCase(FIX.getShortName())) {
                algo = FIX;
            }
            return algo;
        }
    }

    public enum PriceListApplicableOn implements Serializable, IsSerializable {

        PHARMACY(1, "PH"), INVENTORY(2, "SM"), OTHER(3, "OT");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        PriceListApplicableOn(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static PriceListApplicableOn getViaKey(int id) {
            PriceListApplicableOn algo = null;
            if (id == PHARMACY.getId()) {
                algo = PHARMACY;
            } else if (id == INVENTORY.getId()) {
                algo = INVENTORY;
            } else if (id == OTHER.getId()) {
                algo = OTHER;
            }
            return algo;
        }

        public static PriceListApplicableOn getViaText(String text) {
            PriceListApplicableOn algo = null;
            if (text.equalsIgnoreCase(PHARMACY.getValue())) {
                algo = PHARMACY;
            } else if (text.equalsIgnoreCase(INVENTORY.getValue())) {
                algo = INVENTORY;
            } else if (text.equalsIgnoreCase(OTHER.getValue())) {
                algo = OTHER;
            }
            return algo;
        }
    }

    public enum WholeSalePaymentMode implements Serializable, IsSerializable {

        CASH(1, "Cash"), CREDIT(2, "Credit");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        WholeSalePaymentMode(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static WholeSalePaymentMode getViaKey(int id) {
            WholeSalePaymentMode algo = null;
            if (id == CASH.getId()) {
                algo = CASH;
            } else if (id == CREDIT.getId()) {
                algo = CREDIT;
            }
            return algo;
        }

        public static WholeSalePaymentMode getViaText(String text) {
            WholeSalePaymentMode algo = null;
            if (text.equalsIgnoreCase(CASH.getValue())) {
                algo = CASH;
            } else if (text.equalsIgnoreCase(CREDIT.getValue())) {
                algo = CREDIT;
            }
            return algo;
        }

    }

    public enum WholeSaleType implements Serializable, IsSerializable {

        WALK_IN(1, "Walk-In"), DEBTOR(2, "Debtor");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        WholeSaleType(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static WholeSaleType getViaKey(int id) {
            WholeSaleType algo = null;
            if (id == WALK_IN.getId()) {
                algo = WALK_IN;
            } else if (id == DEBTOR.getId()) {
                algo = DEBTOR;
            }
            return algo;
        }

        public static WholeSaleType getViaText(String text) {
            WholeSaleType algo = null;
            if (text.equalsIgnoreCase(WALK_IN.getValue())) {
                algo = WALK_IN;
            } else if (text.equalsIgnoreCase(DEBTOR.getValue())) {
                algo = DEBTOR;
            }
            return algo;
        }

    }

    //======================================<MJ>=======================================//
    public enum POSIntegrationType {

        RAZOR_PAY(1, "Razor Pay");

        private final int code;
        private final String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        POSIntegrationType(int code, String value) {
            this.code = code;
            this.name = value;
        }

        public static POSIntegrationType getViaText(String value) {
            for (POSIntegrationType g : POSIntegrationType.values()) {
                if (g.getName().equalsIgnoreCase(DmShared.xNull(value).trim())) {
                    return g;
                }
            }
            return null;
        }

        public static POSIntegrationType getViaKey(int code) {
            for (POSIntegrationType g : POSIntegrationType.values()) {
                if (g.getCode() == DmShared.iNull(code).intValue()) {
                    return g;
                }
            }
            return null;
        }
    }

    //==================================<*********>======================================//
}
