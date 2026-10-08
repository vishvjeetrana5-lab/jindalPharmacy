package pacsetup.actions;

public enum EnumStatus {
    Pending("Pending"),
    Starting("Starting"),
    Executing("Executing"),
    Succeeded("Succeeded"),
    Failed("Failed");

    private String aValue;

    public static EnumStatus getValueFrom(String status) {
        EnumStatus[] values = EnumStatus.values();
        EnumStatus enumStatus = null;
        for (int i = 0; i < values.length; i++) {
            if (values[i].toString().equalsIgnoreCase(status)) {
                enumStatus = values[i];
                break;
            }
        }
        return enumStatus;
    }

    @Override
    public String toString() {
        return aValue;
    }

    private EnumStatus(String value) {
        this.aValue = value.trim();
    }
}
