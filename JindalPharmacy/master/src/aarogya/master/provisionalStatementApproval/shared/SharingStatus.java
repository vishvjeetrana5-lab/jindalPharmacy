package aarogya.master.provisionalStatementApproval.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public enum SharingStatus {
    PENDING(0, "Pending"), CLOSED(1, "Closed");

    private int id;
    private String text;

    private SharingStatus(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public static SharingStatus getViaText(String text) {
        for (SharingStatus rtnValue : SharingStatus.values()) {
            if (DmShared.xNull(text).equalsIgnoreCase(rtnValue.text)) {
                return rtnValue;
            }
        }
        return null;
    }

    public static SharingStatus getViaId(int id) {
        for (SharingStatus rtnValue : SharingStatus.values()) {
            if (id == rtnValue.id) {
                return rtnValue;
            }
        }
        return null;
    }
}
