package dataman.dmBase.dmScheduler.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.PLT;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SchedulerMast_Mdl implements Serializable, IsSerializable {

    private Long docId;
    private String name;
    private PLT plt;
//    private DmEnum.DmScheduleExecutionType scheduleExecutionType;
    private long webJobTemplateId;
    private Date startDateTime;
    private Date expDateTime;
    private Date lastExecTime;
    private boolean expUsed;
    private boolean active;
    private boolean executed;
    private int recurQuan;
    private String actDbName;
    private String actFileDbName;
    private String dayOrmonth;
    private DmEnum.DmWebJobTypes webJobType;
    private DmEnum.DmRecurrenceUnit recurUnit;
    private DmEnum.DmSchedularType schType;
    private String userName;
    private Date userEntryDate;
    private String senderUserName;
    private String emailId;
    private String password;
    private String bcc;
    private String cc;
    private String companyCode;
    private String voucherPrefix;

    private List<DmEnum.DmWeekdays> lstDays = new ArrayList<>();
    private List<DmEnum.DmMonths> lstMonths = new ArrayList<>();
    private List<DmEnum.DmDates> lstDates = new ArrayList<>();
    private List<DmEnum.DmWeeks> lstWeeks = new ArrayList<>();
    private List<Integer> lstSites = new ArrayList<>();
    private List<String> lstBcc = new ArrayList<>();
    private List<String> lstCc = new ArrayList<>();

    public SchedulerMast_Mdl() {
    }

    public DmEnum.DmWebJobTypes getWebJobType() {
        return webJobType;
    }

    public String getEmailSenderUserName() {
        return senderUserName;
    }

    public void setEmailSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailBcc() {
        String retVal = "";
        for (int i = 0; i < this.lstBcc.size(); i++) {
            retVal += this.lstBcc.get(i).trim() + "|";
        }
        if (retVal.endsWith("|")) {
            retVal = retVal.substring(0, retVal.length() - 1);
        }
        return retVal;
    }

    public List<String> getLstEmailBcc() {
        return this.lstBcc;
    }

    public String getEmailSenderPassword() {
        return password;
    }

    public void setEmailSenderPassword(String password) {
        this.password = password;
    }

    public void setEmailBcc(String bcc) {
        this.lstBcc.clear();
        String[] tmpBcc = bcc.split("\\|");
        for (int i = 0; i < tmpBcc.length; i++) {
            this.lstBcc.add(tmpBcc[i].trim());
        }
    }

    public List<String> getLstEmailCc() {
        return this.lstCc;
    }

    public String getEmailCc() {
        String retVal = "";
        for (int i = 0; i < this.lstCc.size(); i++) {
            retVal += this.lstCc.get(i).trim() + "|";
        }
        if (retVal.endsWith("|")) {
            retVal = retVal.substring(0, retVal.length() - 1);
        }
        return retVal;
    }

    public void setEmailCc(String cc) {
        this.lstCc.clear();
        String[] tmpCc = cc.split("\\|");
        for (int i = 0; i < tmpCc.length; i++) {
            this.lstCc.add(tmpCc[i].trim());
        }
    }

    public void setWebJobType(DmEnum.DmWebJobTypes webJobType) {
        this.webJobType = webJobType;
    }

    public String getDayOrMonth() {
        return dayOrmonth;
    }

    public void setDayOrMonth(String dayOrmonth) {
        this.dayOrmonth = dayOrmonth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUserEntryDate() {
        return userEntryDate;
    }

    public void setUserEntryDate(Date userEntryDate) {
        this.userEntryDate = userEntryDate;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public void setActiveDB(String dbName) {
        this.actDbName = dbName.trim();
    }

    public String getActiveDB() {
        return this.actDbName;
    }

    public String getActiveFileDbName() {
        return actFileDbName;
    }

    public void setActiveFileDbName(String actFileDbName) {
        this.actFileDbName = actFileDbName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PLT getPlt() {
        return plt;
    }

    public void setPlt(PLT plt) {
        this.plt = plt;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

//    public DmEnum.DmScheduleExecutionType getScheduleExecutionType() {
//        return scheduleExecutionType;
//    }
//
//    public void setScheduleExecutionType(DmEnum.DmScheduleExecutionType scheduleExecutionType) {
//        this.scheduleExecutionType = scheduleExecutionType;
//    }
    public long getWebJobTemplateId() {
        return this.webJobTemplateId;
    }

    public void setWebJobTemplateId(long templateId) {
        this.webJobTemplateId = templateId;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getExpDateTime() {
        return expDateTime;
    }

    public void setExpDateTime(Date expDateTime) {
        this.expDateTime = expDateTime;
    }

    public boolean isExpUsed() {
        return expUsed;
    }

    public void setExpUsed(boolean expUsed) {
        this.expUsed = expUsed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRecurQuan() {
        return recurQuan;
    }

    public void setRecurQuan(int recurQuan) {
        this.recurQuan = recurQuan;
    }

    public DmEnum.DmRecurrenceUnit getRecurUnit() {
        return recurUnit;
    }

    public void setRecurUnit(DmEnum.DmRecurrenceUnit recurUnit) {
        this.recurUnit = recurUnit;
    }

    public DmEnum.DmSchedularType getSchType() {
        return schType;
    }

    public void setSchType(DmEnum.DmSchedularType schType) {
        this.schType = schType;
    }

    public List<DmEnum.DmWeekdays> getLstDays() {
        return lstDays;
    }

    public void setSelDays(String lstDays) throws Exception {
        this.lstDays.clear();
        String[] days = lstDays.split("\\|");
        for (int i = 0; i < days.length; i++) {
            DmEnum.DmWeekdays dmDays = DmEnum.DmWeekdays.getValueFrom(DmBaseShared.iNull(days[i].trim()).intValue());
            this.lstDays.add(dmDays);
        }
    }

    public List<Integer> getLstSites() {
        return this.lstSites;
    }

    public void setSelSites(String lstSites) throws Exception {
        this.lstSites.clear();
        String[] siteCodes = lstSites.split("\\|");
        for (int i = 0; i < siteCodes.length; i++) {
            int siteCode = DmBaseShared.iNull(siteCodes[i].trim()).intValue();
            if (!this.lstSites.contains(siteCode)) {
                this.lstSites.add(siteCode);
            }
        }
    }

    public String getSelSites() throws Exception {
        String retValue = "";
        for (int i = 0; i < this.lstSites.size(); i++) {
            retValue += this.lstSites.get(i).toString() + "|";
        }
        if (retValue.endsWith("|")) {
            retValue = retValue.substring(0, retValue.length() - 1);
        }
        return retValue;
    }

    public String getSelDays() throws Exception {
        String retValue = "";
        for (int i = 0; i < this.lstDays.size(); i++) {
            retValue += DmBaseShared.xNull(String.valueOf(this.lstDays.get(i).getWeekDay())) + "|";
        }
        if (retValue.endsWith("|")) {
            retValue = retValue.substring(0, retValue.length() - 1);
        }
        return retValue;
    }

    public List<DmEnum.DmMonths> getLstMonths() {
        return lstMonths;
    }

    public void setSelMonths(String strMonths) throws Exception {
        this.lstMonths.clear();
        String[] months = strMonths.split("\\|");
        for (int i = 0; i < months.length; i++) {
            if (!DmBaseShared.xNull(months[i].trim()).equalsIgnoreCase("")) {
                DmEnum.DmMonths dmMonth = DmEnum.DmMonths.getValueFrom(DmBaseShared.iNull(months[i].trim()).intValue());
                this.lstMonths.add(dmMonth);
            }
        }
    }

    public String getSelMonths() throws Exception {
        String retVal = null;
        for (int i = 0; i < this.lstMonths.size(); i++) {
            retVal += this.lstMonths.get(i).getMonthCalendarValue() + "|";
        }
        if (retVal.endsWith("|")) {
            retVal = retVal.substring(0, retVal.length() - 1);
        }
        return retVal;
    }

    public List<DmEnum.DmDates> getLstDates() {
        return lstDates;
    }

    public void setSelDates(String strDates) throws Exception {
        this.lstDates.clear();
        String[] dates = strDates.split("\\|");
        for (int i = 0; i < dates.length; i++) {
            if (!DmBaseShared.xNull(dates[i].trim()).equalsIgnoreCase("")) {
                this.lstDates.add(DmEnum.DmDates.getValueFrom(DmBaseShared.iNull(Integer.parseInt(dates[i].trim())).intValue()));
            }
        }
    }

    public String getSelDates() {
        String retVal = "";
        for (int i = 0; i < this.lstDates.size(); i++) {
            retVal = DmBaseShared.xNull(String.valueOf(this.lstDates.get(i))) + "|";
        }
        if (retVal.endsWith("|")) {
            retVal = retVal.substring(0, retVal.length() - 1);
        }
        return retVal;
    }

    public List<DmEnum.DmWeeks> getLstWeeks() {
        return lstWeeks;
    }

    public void setSelWeeks(String strWeeks) throws Exception {
        this.lstWeeks.clear();
        String[] weeks = strWeeks.split("\\|");
        for (int i = 0; i < weeks.length; i++) {
            if (!DmBaseShared.xNull(weeks[i]).equalsIgnoreCase("")) {
                DmEnum.DmWeeks week = DmEnum.DmWeeks.getValueFrom(DmBaseShared.iNull(weeks[i].trim()).intValue());
                this.lstWeeks.add(week);
            }
        }
    }

    public String getSelWeeks() {
        String retVal = "";
        for (int i = 0; i < this.lstWeeks.size(); i++) {
            retVal += this.lstWeeks.get(i).getWeekCalendarValue() + "|";
        }
        if (retVal.endsWith("|")) {
            retVal = retVal.substring(0, retVal.length() - 1);
        }
        return retVal;
    }

    public Date getLastExecutionTime() {
        return lastExecTime;
    }

    public void setLastExecutionTime(Date lastExecTime) {
        this.lastExecTime = lastExecTime;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;
        if (obj != null) {
            if (obj instanceof SchedulerMast_Mdl) {
                retVal = this.docId.equals(((SchedulerMast_Mdl) obj).getDocId());
            }
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.docId != null ? this.docId.hashCode() : 0);
        return hash;
    }
}
