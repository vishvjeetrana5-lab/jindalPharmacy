package dataman.payroll.attendanceEntry.client;

import dataman.payroll.attendanceEntry.shared.GrdAttendanceEntry_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.ArrayList;
import java.util.List;
import dataman.payroll.attendanceEntry.shared.AttendanceEntry_SrvAsync;
import dataman.payroll.attendanceEntry.shared.AttendanceEntry_Srv;
import dataman.payroll.attendanceEntry.shared.Attendance_Mdl;

public class AttendanceEntryGrdEdit<T1, T2> extends AttendanceEntryGrdEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler {

    interface CallBack {

        public void onOk(GrdAttendanceEntry_Mdl grdAttendanceMdl);

        public void onDelete();
    }
    private final ServiceInvoker<AttendanceEntry_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    private GrdAttendanceEntry_Mdl grdAttendanceMdl;
    private AttendanceEntry attendanceEntry;
    List<GrdAttendanceEntry_Mdl> attendanceList;
    private int columnIndex;

    String days = "", natureCode = "", type_Leave_Attendance = "", plusMinus = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {

        txtHStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<Attendance_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHStatus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Attendance_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHStatus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Attendance_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHStatus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHStatus).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Attendance_Mdl t1Row) {
                days = t1Row.getDays();
                natureCode = t1Row.getNatureCode();
                type_Leave_Attendance = t1Row.getType_Leave_Attendance();
                plusMinus = t1Row.getPlusMinus();
            }
        });

    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdAttendanceEntry_Mdl();
    }

    public void show(int currentRowIndex, int currentColumnIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, final boolean isDeleteFocus, final AttendanceEntry attendanceEntry) {
        this.attendanceEntry = attendanceEntry;
        this.columnIndex = currentColumnIndex;
        super.showRelativeTo(currentRowIndex, currentColumnIndex, okNeeded, newNeeded, deleteNeeded);
        this.setVisibleNew(false);
        this.setVisibleDelete(false);
    }

    public void manageDisplay(boolean courseEditable) {

    }

    @Override
    public void displayRow(T1 row) {
        GrdAttendanceEntry_Mdl ourRow = (GrdAttendanceEntry_Mdl) row;
        switch (columnIndex) {

            case 1:
                hide();
                break;
            case 3:
                if (!DmShared.xNull(ourRow.getDay1()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay1()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay1Code()));
                    days = DmShared.xNull(ourRow.getDays1());
                    natureCode = DmShared.xNull(ourRow.getNatureCode1());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance1());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus1());
                } else {
                    hide();
                }
                break;
            case 4:
                if (!DmShared.xNull(ourRow.getDay2()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay2()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay2Code()));
                    days = DmShared.xNull(ourRow.getDays2());
                    natureCode = DmShared.xNull(ourRow.getNatureCode2());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance2());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus2());
                } else {
                    hide();
                }

                break;
            case 5:
                if (!DmShared.xNull(ourRow.getDay3()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay3()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay3Code()));
                    days = DmShared.xNull(ourRow.getDays3());
                    natureCode = DmShared.xNull(ourRow.getNatureCode3());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance3());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus3());
                } else {
                    hide();
                }

                break;
            case 6:
                if (!DmShared.xNull(ourRow.getDay4()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay4()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay4Code()));
                    days = DmShared.xNull(ourRow.getDays4());
                    natureCode = DmShared.xNull(ourRow.getNatureCode4());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance4());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus4());
                } else {
                    hide();
                }

                break;
            case 7:
                if (!DmShared.xNull(ourRow.getDay5()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay5()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay5Code()));
                    days = DmShared.xNull(ourRow.getDays5());
                    natureCode = DmShared.xNull(ourRow.getNatureCode5());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance5());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus5());
                } else {
                    hide();
                }

                break;
            case 8:
                if (!DmShared.xNull(ourRow.getDay6()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay6()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay6Code()));
                    days = DmShared.xNull(ourRow.getDays6());
                    natureCode = DmShared.xNull(ourRow.getNatureCode6());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance6());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus6());
                } else {
                    hide();
                }

                break;
            case 9:
                if (!DmShared.xNull(ourRow.getDay7()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay7()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay7Code()));
                    days = DmShared.xNull(ourRow.getDays7());
                    natureCode = DmShared.xNull(ourRow.getNatureCode7());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance7());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus7());
                } else {
                    hide();
                }

                break;
            case 10:
                if (!DmShared.xNull(ourRow.getDay8()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay8()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay8Code()));
                    days = DmShared.xNull(ourRow.getDays8());
                    natureCode = DmShared.xNull(ourRow.getNatureCode8());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance8());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus8());
                } else {
                    hide();
                }

                break;
            case 11:
                if (!DmShared.xNull(ourRow.getDay9()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay9()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay9Code()));
                    days = DmShared.xNull(ourRow.getDays9());
                    natureCode = DmShared.xNull(ourRow.getNatureCode9());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance9());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus9());
                } else {
                    hide();
                }

                break;
            case 12:
                if (!DmShared.xNull(ourRow.getDay10()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay10()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay10Code()));
                    days = DmShared.xNull(ourRow.getDays10());
                    natureCode = DmShared.xNull(ourRow.getNatureCode10());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance10());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus10());
                } else {
                    hide();
                }

                break;
            case 13:
                if (!DmShared.xNull(ourRow.getDay11()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay11()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay11Code()));
                    days = DmShared.xNull(ourRow.getDays11());
                    natureCode = DmShared.xNull(ourRow.getNatureCode11());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance11());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus11());
                } else {
                    hide();
                }

                break;
            case 14:
                if (!DmShared.xNull(ourRow.getDay12()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay12()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay12Code()));
                    days = DmShared.xNull(ourRow.getDays12());
                    natureCode = DmShared.xNull(ourRow.getNatureCode12());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance12());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus12());
                } else {
                    hide();
                }

                break;
            case 15:
                if (!DmShared.xNull(ourRow.getDay13()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay13()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay13Code()));
                    days = DmShared.xNull(ourRow.getDays13());
                    natureCode = DmShared.xNull(ourRow.getNatureCode13());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance13());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus13());
                } else {
                    hide();
                }

                break;
            case 16:
                if (!DmShared.xNull(ourRow.getDay14()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay14()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay14Code()));
                    days = DmShared.xNull(ourRow.getDays14());
                    natureCode = DmShared.xNull(ourRow.getNatureCode14());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance14());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus14());
                } else {
                    hide();
                }

                break;
            case 17:
                if (!DmShared.xNull(ourRow.getDay15()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay15()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay15Code()));
                    days = DmShared.xNull(ourRow.getDays15());
                    natureCode = DmShared.xNull(ourRow.getNatureCode15());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance15());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus15());
                } else {
                    hide();
                }

                break;
            case 18:
                if (!DmShared.xNull(ourRow.getDay16()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay16()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay16Code()));
                    days = DmShared.xNull(ourRow.getDays16());
                    natureCode = DmShared.xNull(ourRow.getNatureCode16());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance16());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus16());
                } else {
                    hide();
                }

                break;
            case 19:
                if (!DmShared.xNull(ourRow.getDay17()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay17()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay17Code()));
                    days = DmShared.xNull(ourRow.getDays17());
                    natureCode = DmShared.xNull(ourRow.getNatureCode17());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance17());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus17());
                } else {
                    hide();
                }

                break;
            case 20:
                if (!DmShared.xNull(ourRow.getDay18()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay18()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay18Code()));
                    days = DmShared.xNull(ourRow.getDays18());
                    natureCode = DmShared.xNull(ourRow.getNatureCode18());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance18());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus18());
                } else {
                    hide();
                }

                break;
            case 21:
                if (!DmShared.xNull(ourRow.getDay19()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay19()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay19Code()));
                    days = DmShared.xNull(ourRow.getDays19());
                    natureCode = DmShared.xNull(ourRow.getNatureCode19());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance19());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus19());
                } else {
                    hide();
                }

                break;
            case 22:
                if (!DmShared.xNull(ourRow.getDay20()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay20()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay20Code()));
                    days = DmShared.xNull(ourRow.getDays20());
                    natureCode = DmShared.xNull(ourRow.getNatureCode20());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance20());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus20());
                } else {
                    hide();
                }

                break;
            case 23:
                if (!DmShared.xNull(ourRow.getDay21()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay21()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay21Code()));
                    days = DmShared.xNull(ourRow.getDays21());
                    natureCode = DmShared.xNull(ourRow.getNatureCode21());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance21());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus21());
                } else {
                    hide();
                }

                break;
            case 24:
                if (!DmShared.xNull(ourRow.getDay22()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay22()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay22Code()));
                    days = DmShared.xNull(ourRow.getDays22());
                    natureCode = DmShared.xNull(ourRow.getNatureCode22());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance22());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus22());
                } else {
                    hide();
                }

                break;
            case 25:
                if (!DmShared.xNull(ourRow.getDay23()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay23()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay23Code()));
                    days = DmShared.xNull(ourRow.getDays23());
                    natureCode = DmShared.xNull(ourRow.getNatureCode23());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance23());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus23());
                } else {
                    hide();
                }

                break;
            case 26:
                if (!DmShared.xNull(ourRow.getDay24()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay24()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay24Code()));
                    days = DmShared.xNull(ourRow.getDays24());
                    natureCode = DmShared.xNull(ourRow.getNatureCode24());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance24());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus24());
                } else {
                    hide();
                }

                break;
            case 27:
                if (!DmShared.xNull(ourRow.getDay25()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay25()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay25Code()));
                    days = DmShared.xNull(ourRow.getDays25());
                    natureCode = DmShared.xNull(ourRow.getNatureCode25());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance25());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus25());
                } else {
                    hide();
                }

                break;
            case 28:
                if (!DmShared.xNull(ourRow.getDay26()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay26()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay26Code()));
                    days = DmShared.xNull(ourRow.getDays26());
                    natureCode = DmShared.xNull(ourRow.getNatureCode26());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance26());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus26());
                } else {
                    hide();
                }

                break;
            case 29:
                if (!DmShared.xNull(ourRow.getDay27()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay27()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay27Code()));
                    days = DmShared.xNull(ourRow.getDays27());
                    natureCode = DmShared.xNull(ourRow.getNatureCode27());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance27());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus27());
                } else {
                    hide();
                }

                break;
            case 30:
                if (!DmShared.xNull(ourRow.getDay28()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay28()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay28Code()));
                    days = DmShared.xNull(ourRow.getDays28());
                    natureCode = DmShared.xNull(ourRow.getNatureCode28());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance28());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus28());
                } else {
                    hide();
                }

                break;
            case 31:
                if (!DmShared.xNull(ourRow.getDay29()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay29()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay29Code()));
                    days = DmShared.xNull(ourRow.getDays29());
                    natureCode = DmShared.xNull(ourRow.getNatureCode29());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance29());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus29());
                } else {
                    hide();
                }

                break;
            case 32:
                if (!DmShared.xNull(ourRow.getDay30()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay30()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay30Code()));
                    days = DmShared.xNull(ourRow.getDays30());
                    natureCode = DmShared.xNull(ourRow.getNatureCode30());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance30());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus30());
                } else {
                    hide();
                }

                break;
            case 33:
                if (!DmShared.xNull(ourRow.getDay31()).equalsIgnoreCase("NA")) {
                    txtHStatus.setText(DmShared.xNull(ourRow.getDay31()));
                    txtHStatus.setTag(DmShared.xNull(ourRow.getDay31Code()));
                    days = DmShared.xNull(ourRow.getDays31());
                    natureCode = DmShared.xNull(ourRow.getNatureCode31());
                    type_Leave_Attendance = DmShared.xNull(ourRow.getType_Leave_Attendance31());
                    plusMinus = DmShared.xNull(ourRow.getPlusMinus31());
                } else {
                    hide();
                }

                break;
            default:
                hide();
                break;
        }
        hashCode = ourRow.getSharedKeyFieldHashCode();
    }

    @Override
    public void onOk() {
        try {
            proceedOk();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHStatus).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdAttendanceEntry_Mdl ourRow = (GrdAttendanceEntry_Mdl) row;
        switch (columnIndex) {
            case 1:
                hide();
                break;
            case 3:
                ourRow.setDay1(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay1Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays1(days);
                ourRow.setNatureCode1(natureCode);
                ourRow.setType_Leave_Attendance1(type_Leave_Attendance);
                ourRow.setPlusMinus1(plusMinus);
                break;
            case 4:
                ourRow.setDay2(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay2Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays2(days);
                ourRow.setNatureCode1(natureCode);
                ourRow.setType_Leave_Attendance2(type_Leave_Attendance);
                ourRow.setPlusMinus2(plusMinus);
                break;
            case 5:
                ourRow.setDay3(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay3Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays3(days);
                ourRow.setNatureCode3(natureCode);
                ourRow.setType_Leave_Attendance1(type_Leave_Attendance);
                ourRow.setPlusMinus3(plusMinus);
                break;
            case 6:
                ourRow.setDay4(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay4Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays4(days);
                ourRow.setNatureCode4(natureCode);
                ourRow.setType_Leave_Attendance4(type_Leave_Attendance);
                ourRow.setPlusMinus4(plusMinus);
                break;
            case 7:
                ourRow.setDay5(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay5Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays5(days);
                ourRow.setNatureCode5(natureCode);
                ourRow.setType_Leave_Attendance5(type_Leave_Attendance);
                ourRow.setPlusMinus5(plusMinus);
                break;
            case 8:
                ourRow.setDay6(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay6Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays6(days);
                ourRow.setNatureCode6(natureCode);
                ourRow.setType_Leave_Attendance6(type_Leave_Attendance);
                ourRow.setPlusMinus6(plusMinus);
                break;
            case 9:
                ourRow.setDay7(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay7Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays7(days);
                ourRow.setNatureCode7(natureCode);
                ourRow.setType_Leave_Attendance7(type_Leave_Attendance);
                ourRow.setPlusMinus7(plusMinus);
                break;
            case 10:
                ourRow.setDay8(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay8Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays8(days);
                ourRow.setNatureCode8(natureCode);
                ourRow.setType_Leave_Attendance8(type_Leave_Attendance);
                ourRow.setPlusMinus8(plusMinus);
                break;
            case 11:
                ourRow.setDay9(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay9Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays9(days);
                ourRow.setNatureCode9(natureCode);
                ourRow.setType_Leave_Attendance9(type_Leave_Attendance);
                ourRow.setPlusMinus9(plusMinus);
                break;
            case 12:
                ourRow.setDay10(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay10Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays10(days);
                ourRow.setNatureCode10(natureCode);
                ourRow.setType_Leave_Attendance10(type_Leave_Attendance);
                ourRow.setPlusMinus10(plusMinus);
                break;
            case 13:
                ourRow.setDay11(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay11Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays11(days);
                ourRow.setNatureCode11(natureCode);
                ourRow.setType_Leave_Attendance11(type_Leave_Attendance);
                ourRow.setPlusMinus11(plusMinus);
                break;
            case 14:
                ourRow.setDay12(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay12Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays12(days);
                ourRow.setNatureCode12(natureCode);
                ourRow.setType_Leave_Attendance12(type_Leave_Attendance);
                ourRow.setPlusMinus12(plusMinus);
                break;
            case 15:
                ourRow.setDay13(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay13Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays13(days);
                ourRow.setNatureCode13(natureCode);
                ourRow.setType_Leave_Attendance13(type_Leave_Attendance);
                ourRow.setPlusMinus13(plusMinus);
                break;
            case 16:
                ourRow.setDay14(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay14Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays14(days);
                ourRow.setNatureCode14(natureCode);
                ourRow.setType_Leave_Attendance14(type_Leave_Attendance);
                ourRow.setPlusMinus14(plusMinus);
                break;
            case 17:
                ourRow.setDay15(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay15Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays15(days);
                ourRow.setNatureCode15(natureCode);
                ourRow.setType_Leave_Attendance15(type_Leave_Attendance);
                ourRow.setPlusMinus15(plusMinus);
                break;
            case 18:
                ourRow.setDay16(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay16Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays16(days);
                ourRow.setNatureCode16(natureCode);
                ourRow.setType_Leave_Attendance16(type_Leave_Attendance);
                ourRow.setPlusMinus16(plusMinus);
                break;
            case 19:
                ourRow.setDay17(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay17Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays17(days);
                ourRow.setNatureCode17(natureCode);
                ourRow.setType_Leave_Attendance17(type_Leave_Attendance);
                ourRow.setPlusMinus17(plusMinus);
                break;
            case 20:
                ourRow.setDay18(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay18Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays18(days);
                ourRow.setNatureCode18(natureCode);
                ourRow.setType_Leave_Attendance18(type_Leave_Attendance);
                ourRow.setPlusMinus18(plusMinus);
                break;
            case 21:
                ourRow.setDay19(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay19Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays19(days);
                ourRow.setNatureCode19(natureCode);
                ourRow.setType_Leave_Attendance19(type_Leave_Attendance);
                ourRow.setPlusMinus19(plusMinus);
                break;
            case 22:
                ourRow.setDay20(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay20Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays20(days);
                ourRow.setNatureCode20(natureCode);
                ourRow.setType_Leave_Attendance20(type_Leave_Attendance);
                ourRow.setPlusMinus20(plusMinus);
                break;
            case 23:
                ourRow.setDay21(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay21Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays21(days);
                ourRow.setNatureCode21(natureCode);
                ourRow.setType_Leave_Attendance21(type_Leave_Attendance);
                ourRow.setPlusMinus21(plusMinus);
                break;
            case 24:
                ourRow.setDay22(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay22Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays22(days);
                ourRow.setNatureCode22(natureCode);
                ourRow.setType_Leave_Attendance22(type_Leave_Attendance);
                ourRow.setPlusMinus22(plusMinus);
                break;
            case 25:
                ourRow.setDay23(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay23Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays23(days);
                ourRow.setNatureCode23(natureCode);
                ourRow.setType_Leave_Attendance23(type_Leave_Attendance);
                ourRow.setPlusMinus23(plusMinus);
                break;
            case 26:
                ourRow.setDay24(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay24Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays24(days);
                ourRow.setNatureCode24(natureCode);
                ourRow.setType_Leave_Attendance24(type_Leave_Attendance);
                ourRow.setPlusMinus24(plusMinus);
                break;
            case 27:
                ourRow.setDay25(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay25Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays25(days);
                ourRow.setNatureCode25(natureCode);
                ourRow.setType_Leave_Attendance25(type_Leave_Attendance);
                ourRow.setPlusMinus25(plusMinus);
                break;
            case 28:
                ourRow.setDay26(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay26Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays26(days);
                ourRow.setNatureCode26(natureCode);
                ourRow.setType_Leave_Attendance26(type_Leave_Attendance);
                ourRow.setPlusMinus26(plusMinus);
                break;
            case 29:
                ourRow.setDay27(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay27Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays27(days);
                ourRow.setNatureCode27(natureCode);
                ourRow.setType_Leave_Attendance27(type_Leave_Attendance);
                ourRow.setPlusMinus27(plusMinus);
                break;
            case 30:
                ourRow.setDay28(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay28Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays28(days);
                ourRow.setNatureCode28(natureCode);
                ourRow.setType_Leave_Attendance28(type_Leave_Attendance);
                ourRow.setPlusMinus28(plusMinus);
                break;
            case 31:
                ourRow.setDay29(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay29Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays29(days);
                ourRow.setNatureCode29(natureCode);
                ourRow.setType_Leave_Attendance29(type_Leave_Attendance);
                ourRow.setPlusMinus29(plusMinus);
                break;
            case 32:
                ourRow.setDay30(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay30Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays30(days);
                ourRow.setNatureCode30(natureCode);
                ourRow.setType_Leave_Attendance30(type_Leave_Attendance);
                ourRow.setPlusMinus30(plusMinus);
                break;
            case 33:
                ourRow.setDay31(DmShared.xNull(txtHStatus.getText()));
                ourRow.setDay31Code(DmShared.xNull(txtHStatus.getTag()));
                ourRow.setDays31(days);
                ourRow.setNatureCode31(natureCode);
                ourRow.setType_Leave_Attendance31(type_Leave_Attendance);
                ourRow.setPlusMinus31(plusMinus);
                break;
        }

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {

    }

    public AttendanceEntryGrdEdit(DmGrid<T1, T2> grid) {
        super(grid, "", "25px", "10px");
        srvAsync = new ServiceInvoker<AttendanceEntry_SrvAsync>(GWT.create(AttendanceEntry_Srv.class), AttendanceEntry.class.getName());
        textHelpEvents();

    }
}
