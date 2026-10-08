package dataman.payroll.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PREnum implements Serializable, IsSerializable {

    public enum HOLIDAY_TYPE implements Serializable, IsSerializable {

        NATIONAL(1, "National"), FESTIVAL(2, "Festival");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        HOLIDAY_TYPE(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static HOLIDAY_TYPE getViaKey(int id) {
            HOLIDAY_TYPE algo = null;
            if (id == FESTIVAL.getId()) {
                algo = FESTIVAL;
            } else if (id == NATIONAL.getId()) {
                algo = NATIONAL;
            }
            return algo;
        }

        public static HOLIDAY_TYPE getViaText(String text) {
            HOLIDAY_TYPE algo = null;
            if (text.equalsIgnoreCase(FESTIVAL.getValue())) {
                algo = FESTIVAL;
            } else if (text.equalsIgnoreCase(NATIONAL.getValue())) {
                algo = NATIONAL;
            }
            return algo;
        }

    }
//===================================<< LEAVE_Nature >>==================================
    public enum LEAVE_NATURE implements Serializable, IsSerializable {
        PAID(1, "Paid"), NOT_PAID(2, "Not Paid");
        //COMPENSATE(3, "Compensate");
        private final int id;
        private final String value;

        LEAVE_NATURE(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public static LEAVE_NATURE getViaText(String value) {
            for (LEAVE_NATURE node : LEAVE_NATURE.values()) {
                if (node.getValue().equalsIgnoreCase(value)) {
                    return node;
                }
            }
            return null;
        }

        public static LEAVE_NATURE getViaKey(int value) {

            for (LEAVE_NATURE node : LEAVE_NATURE.values()) {
                if (node.getId() == value) {
                    return node;
                }
            }
            return null;
        }

    }

//=================================================================================   
        public enum LEAVE_STATUS implements Serializable, IsSerializable {
        PENDING(2, "Pending"), APPROVED(1, "Approved"),CANCELED(0, "Rejected");
       
        private final int id;
        private final String value;

        LEAVE_STATUS(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public static LEAVE_STATUS getViaText(String value) {
            for (LEAVE_STATUS node : LEAVE_STATUS.values()) {
                if (node.getValue().equalsIgnoreCase(value)) {
                    return node;
                }
            }
            return null;
        }

        public static LEAVE_STATUS getViaKey(int value) {

            for (LEAVE_STATUS node : LEAVE_STATUS.values()) {
                if (node.getId() == value) {
                    return node;
                }
            }
            return null;
        }

    }
  //========================================<< Resignation Mode >>===========================
     public enum RESIGNATION_MODE implements Serializable, IsSerializable {
        PROPER(1, "Proper"), NOT_PROPER(2, "Not Proper"),TERMINATE(3, "Terminate"),
        RETIREMENT(4, "Retirement");
       
        private final int id;
        private final String value;
        RESIGNATION_MODE(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public static RESIGNATION_MODE getViaText(String value) {
            for (RESIGNATION_MODE node : RESIGNATION_MODE.values()) {
                if (node.getValue().equalsIgnoreCase(value)) {
                    return node;
                }
            }
            return null;
        }

        public static RESIGNATION_MODE getViaKey(int value) {

            for (RESIGNATION_MODE node : RESIGNATION_MODE.values()) {
                if (node.getId() == value) {
                    return node;
                }
            }
            return null;
        }

    }
        
  //========================================<< Resignation Mode >>===========================
     public enum SALARY_STATUS implements Serializable, IsSerializable {
        RELEASED(1, "Released"), CREDIT(2, "Credit"),HOLD(3, "Hold");
       
        private final int id;
        private final String value;
        SALARY_STATUS(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public static SALARY_STATUS getViaText(String value) {
            for (SALARY_STATUS node : SALARY_STATUS.values()) {
                if (node.getValue().equalsIgnoreCase(value)) {
                    return node;
                }
            }
            return null;
        }

        public static SALARY_STATUS getViaKey(int value) {

            for (SALARY_STATUS node : SALARY_STATUS.values()) {
                if (node.getId() == value) {
                    return node;
                }
            }
            return null;
        }

    }
        
    //===================================<< Nature >>==================================

    public enum LEAVE_TYPE implements Serializable, IsSerializable {
        BROUGHT_FORWARD(1, "Brought forward"), RIGHT_OFF(2, "Right off");
        private final int id;
        private final String value;

        LEAVE_TYPE(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public static LEAVE_TYPE getViaText(String value) {
            for (LEAVE_TYPE node : LEAVE_TYPE.values()) {
                if (node.getValue().equalsIgnoreCase(value)) {
                    return node;
                }
            }
            return null;
        }

        public static LEAVE_TYPE getViaKey(int value) {

            for (LEAVE_TYPE node : LEAVE_TYPE.values()) {
                if (node.getId() == value) {
                    return node;
                }
            }
            return null;
        }

    }
//===================================<< Type Attendance/Leave/OT >>==================================
    public enum TYPE_LEAVE_ATTENDANCE implements Serializable, IsSerializable {
        ATTENDANCE(1, "Attendance"), LEAVE(2, "Leave"), OT(3, "OT");
        //COMPENSATE(3, "Compensate");
        private final int id;
        private final String value;

        TYPE_LEAVE_ATTENDANCE(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public static TYPE_LEAVE_ATTENDANCE getViaText(String value) {
            for (TYPE_LEAVE_ATTENDANCE node : TYPE_LEAVE_ATTENDANCE.values()) {
                if (node.getValue().equalsIgnoreCase(value)) {
                    return node;
                }
            }
            return null;
        }

        public static TYPE_LEAVE_ATTENDANCE getViaKey(int value) {

            for (TYPE_LEAVE_ATTENDANCE node : TYPE_LEAVE_ATTENDANCE.values()) {
                if (node.getId() == value) {
                    return node;
                }
            }
            return null;
        }

    }
//=================================================================================  
}
