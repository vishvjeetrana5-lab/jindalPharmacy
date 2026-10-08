package dataman.webJobs.scheduler.shared;

import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class Scheduler_Vld {

    public static void chkSave(Scheduler_Mdl mrTemp) {

        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName()), "Scheduler Name", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getPltCode()), "Product License To", 150);
        DmShared.isValidText(DmShared.xNull(mrTemp.getDbLookUp()), "Database Look Up", 150);
        DmShared.isValidText(DmShared.xNull(mrTemp.getJobTypeCode()), "Job Type", 200);
        DmShared.isValidText(DmShared.xNull(mrTemp.getTemplateCode()), "Template", 250);
        DmShared.isValidText(DmShared.xNull(mrTemp.getResponsibleUser()), "Responsible User", 300);
        DmShared.isValidText(DmShared.xNull(mrTemp.getIsActiveCode()), "Active", 350);
        DmShared.isValidText(DmShared.xNull(mrTemp.getRbTaskScheduler()), "Task Scheduler", 400);
        DmShared.isValidText(DmShared.xNull(mrTemp.getStartDate()), "Start Date", 450);
        DmShared.isValidText(DmShared.xNull(mrTemp.getStartTime()), "Start Time", 500);
        if (DmShared.xNull(mrTemp.getRbTaskScheduler()).equalsIgnoreCase("2")) {
            if (DmShared.iNull(mrTemp.getRecurrenceQty()).intValue() == 0) {
                throw new DmException(550, "Repeat Every (Day) is mandatory.");
            }
            if (!((DmShared.iNull(mrTemp.getRecurrenceQty()).intValue() > 0) && (DmShared.iNull(mrTemp.getRecurrenceQty()).intValue() < 8))) {
                throw new DmException(550, "Invalid Repeat (Day).");
            }
        }
        if (DmShared.xNull(mrTemp.getRbTaskScheduler()).equalsIgnoreCase("3")) {
            if (DmShared.iNull(mrTemp.getRecurrenceQty()).intValue() == 0) {
                throw new DmException(600, "Repeat Every (Week On) is mandatory.");
            }
            if (!((DmShared.iNull(mrTemp.getRecurrenceQty()).intValue() > 0) && (DmShared.iNull(mrTemp.getRecurrenceQty()).intValue() < 6))) {
                throw new DmException(600, "Invalid Repeat (Week).");
            }
            DmShared.isValidText(DmShared.xNull(mrTemp.getCbDays()), "Weekly Days", 650);
        }
        if (DmShared.xNull(mrTemp.getRbTaskScheduler()).equalsIgnoreCase("4")) {
            DmShared.isValidText(DmShared.xNull(mrTemp.getMonthsCode()), "Months", 700);
            DmShared.isValidText(DmShared.xNull(mrTemp.getRbDayOrMonth()), "Days / On", 750);
            if (DmShared.xNull(mrTemp.getRbDayOrMonth()).equalsIgnoreCase("d")) {
                DmShared.isValidText(DmShared.xNull(mrTemp.getDateCode()), "Days", 800);
            } else if (DmShared.xNull(mrTemp.getRbDayOrMonth()).equalsIgnoreCase("m")) {
                DmShared.isValidText(DmShared.xNull(mrTemp.getWeekCode()), "Monthly Week", 850);
                DmShared.isValidText(DmShared.xNull(mrTemp.getDaysCode()), "Monthly Days", 900);
            }
        }

        if (!(DmShared.xNull(mrTemp.getExpiryDate()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(DmClient.returnDate(DmShared.xNull(mrTemp.getExpiryDate()))).getTime() < DmShared.convertDate(DmClient.returnDate(DmShared.xNull(mrTemp.getStartDate()))).getTime()) {
                throw new DmException(1000, "Expire Date  can't be less than Start Date.");
            }
        }
        if (!(DmShared.xNull(mrTemp.getExpiryDate()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(DmClient.returnDate(DmShared.xNull(mrTemp.getExpiryDate()))).getTime() == DmShared.convertDate(DmClient.returnDate(DmShared.xNull(mrTemp.getStartDate()))).getTime()) {
                if (Double.compare(Double.parseDouble(mrTemp.getExpiryTime()), Double.parseDouble(mrTemp.getStartTime())) <= 0) {
                    throw new DmException(1100, "Expire Time can't be less than Start Time.");
                }
            }
        }
        if (!(DmShared.xNull(mrTemp.getExpiryDate()).equalsIgnoreCase(""))) {
            DmShared.isValidText(DmShared.xNull(mrTemp.getExpiryTime()), "Expire Time", 1100);
        }
    }
}
