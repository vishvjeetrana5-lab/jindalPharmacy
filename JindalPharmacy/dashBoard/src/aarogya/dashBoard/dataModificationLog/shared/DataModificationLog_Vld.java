package aarogya.dashBoard.dataModificationLog.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DataModificationLog_Vld {

    public static void validateInput(SearchValue_Mdl searchValue) {
        DmShared.isValidText(searchValue.getFromDate(), "From Date", 100);
        DmShared.isValidText(searchValue.getToDate(), "To Date", 200);
        if (DmShared.convertDate(searchValue.getFromDate()).getTime() > DmShared.convertDate(searchValue.getToDate()).getTime()) {
            throw new UnsupportedOperationException("From Date not be grater than To Date . ");
        }

    }

}
