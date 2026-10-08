package aarogya.mis.graphicalDashboard.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class GraphicalDashboar_Vld {

    public enum GraphView {
        CONSULTANT_WISE("Consultant Wise"),
        DEPARTMENT_WISE("Department Wise"),
        CASE_WISE("Case Wise"),
        DATE_WISE("Date Wise"),
        ROOM_CATEGORY_WISE("Room Category Wise"),
        AREA_WISE("Area Wise");

        private String text;

        GraphView(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public static GraphView getGraphView(String text) {
            GraphView rtnVal = null;
            if (DmShared.xNull(text).equalsIgnoreCase(AREA_WISE.getText())) {
                rtnVal = AREA_WISE;
            } else if (DmShared.xNull(text).equalsIgnoreCase(CASE_WISE.getText())) {
                rtnVal = CASE_WISE;
            } else if (DmShared.xNull(text).equalsIgnoreCase(CONSULTANT_WISE.getText())) {
                rtnVal = CONSULTANT_WISE;
            } else if (DmShared.xNull(text).equalsIgnoreCase(DATE_WISE.getText())) {
                rtnVal = DATE_WISE;
            } else if (DmShared.xNull(text).equalsIgnoreCase(DEPARTMENT_WISE.getText())) {
                rtnVal = DEPARTMENT_WISE;
            } else if (DmShared.xNull(text).equalsIgnoreCase(ROOM_CATEGORY_WISE.getText())) {
                rtnVal = ROOM_CATEGORY_WISE;
            }
            return rtnVal;
        }
    }

    public enum TabType {

        TAB_OPD(1, "OPD"),
        TAB_DIAGNOSTIC(2, "Diagnostic"),
        TAB_PHARMACY(4, "Pharmacy"),
        TAB_REVENUE(5, "Net Revenue"),
        TAB_IPD(3, "IPD");

        private int id;
        private String name;

        TabType(int tabID, String tabName) {
            this.id = tabID;
            this.name = tabName;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }
}
