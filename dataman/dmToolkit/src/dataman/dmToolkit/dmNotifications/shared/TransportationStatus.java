package dataman.dmToolkit.dmNotifications.shared;

public enum TransportationStatus {
    SENT_TO_SERVER(0), SENT_TO_REPIENT(1), READ_BY_CLIENT(2),FAILED_TO_SEND_TO_SERVER(3),FAILED_TO_SEND_TO_RECIPIENT(4),SENDING_TO_SERVER(5);

    private int value;

    public int getValue(){
        return this.value;
    }
    
    private TransportationStatus(int avalue) {
        this.value = avalue;
    }
}
