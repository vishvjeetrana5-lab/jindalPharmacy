package dataman.dmToolkit.walkThrough.shared;

public enum WalkThroughEnum {
    ACCEPT(0), DECLINE(1);

    private final int optionValue;

    private WalkThroughEnum(int optionValue) {
        this.optionValue = optionValue;
    }

    public int toInt() {
        return optionValue;
    }

    public static String valueOf(int valueId) {
        String value = "";

        if (valueId == ACCEPT.toInt()) {
            value = ACCEPT.toString();
        } else if (valueId == DECLINE.toInt()) {
            value = DECLINE.toString();
        }

        return value.replace("_", " ");
    }
}
