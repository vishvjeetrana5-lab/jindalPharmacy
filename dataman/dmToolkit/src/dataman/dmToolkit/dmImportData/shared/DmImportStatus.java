package dataman.dmToolkit.dmImportData.shared;

public enum DmImportStatus {
    NONE(0, ""),
    SUCCESS(10, "Success"),
    WARNING(20, "Warning"),
    ERROR(30, "Error");

    DmImportStatus(int value, String name) {
        this.id = value;
        this.text = name;
    }
    private int id;
    private String text;

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public static DmImportStatus getImportStatus(int value) {
        DmImportStatus rtnValue = null;
        if (value == NONE.id) {
            rtnValue = NONE;
        } else if (value == ERROR.id) {
            rtnValue = ERROR;
        } else if (value == SUCCESS.id) {
            rtnValue = SUCCESS;
        } else if (value == WARNING.id) {
            rtnValue = WARNING;
        }

        return rtnValue;
    }
}
