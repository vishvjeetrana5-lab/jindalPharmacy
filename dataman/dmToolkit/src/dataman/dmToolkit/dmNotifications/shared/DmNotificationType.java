package dataman.dmToolkit.dmNotifications.shared;

public enum DmNotificationType {
    NONE(0),ALERT(1), NOTIFICATION(2);

    private int value;

    public int getValue() {
        return this.value;
    }

    private DmNotificationType(int avalue) {
        this.value = avalue;
    }
}
