package dataman.webJobs.scheduler.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class Scheduler_Mdl extends KeyField implements Serializable,IsSerializable{

    private String pltCode;
    private String pltName;
    private String dbLookUp;
    private String siteName;
    private String siteCode;
    private String jobTypeCode;
    private String jobTypeName;
    private String templateCode;
    private String templateName;
    private String isActiveCode;
    private String isActiveName;
    private String startDate;
    private String startTime;
    private String expiryDate;
    private String expiryTime;
    private String rbTaskScheduler;
    private String recurrenceQty;
    private String recurrenceUnit;
    private String monthsName;
    private String monthsCode;
    private String rbDayOrMonth;
    private String dateCode;
    private String dateName;
    private String daysCode;
    private String daysName;
    private String weekCode;
    private String weekName;
    private String cbDays;
    private String responsibleUser;

    public Scheduler_Mdl() {
    }

    public String getResponsibleUser() {
        return responsibleUser;
    }

    public void setResponsibleUser(String responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public String getPltCode() {
        return pltCode;
    }

    public void setPltCode(String pltCode) {
        this.pltCode = pltCode;
    }

    public String getPltName() {
        return pltName;
    }

    public void setPltName(String pltName) {
        this.pltName = pltName;
    }

    public String getDbLookUp() {
        return dbLookUp;
    }

    public void setDbLookUp(String dbLookUp) {
        this.dbLookUp = dbLookUp;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getJobTypeCode() {
        return jobTypeCode;
    }

    public void setJobTypeCode(String jobTypeCode) {
        this.jobTypeCode = jobTypeCode;
    }

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getIsActiveCode() {
        return isActiveCode;
    }

    public void setIsActiveCode(String isActiveCode) {
        this.isActiveCode = isActiveCode;
    }

    public String getIsActiveName() {
        return isActiveName;
    }

    public void setIsActiveName(String isActiveName) {
        this.isActiveName = isActiveName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getRbTaskScheduler() {
        return rbTaskScheduler;
    }

    public void setRbTaskScheduler(String rbTaskScheduler) {
        this.rbTaskScheduler = rbTaskScheduler;
    }

    public String getRecurrenceQty() {
        return recurrenceQty;
    }

    public void setRecurrenceQty(String recurrenceQty) {
        this.recurrenceQty = recurrenceQty;
    }

    public String getRecurrenceUnit() {
        return recurrenceUnit;
    }

    public void setRecurrenceUnit(String recurrenceUnit) {
        this.recurrenceUnit = recurrenceUnit;
    }

    public String getMonthsName() {
        return monthsName;
    }

    public void setMonthsName(String monthsName) {
        this.monthsName = monthsName;
    }

    public String getMonthsCode() {
        return monthsCode;
    }

    public void setMonthsCode(String monthsCode) {
        this.monthsCode = monthsCode;
    }

    public String getRbDayOrMonth() {
        return rbDayOrMonth;
    }

    public void setRbDayOrMonth(String rbDayOrMonth) {
        this.rbDayOrMonth = rbDayOrMonth;
    }

    public String getDateCode() {
        return dateCode;
    }

    public void setDateCode(String dateCode) {
        this.dateCode = dateCode;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public String getDaysCode() {
        return daysCode;
    }

    public void setDaysCode(String daysCode) {
        this.daysCode = daysCode;
    }

    public String getDaysName() {
        return daysName;
    }

    public void setDaysName(String daysName) {
        this.daysName = daysName;
    }

    public String getWeekCode() {
        return weekCode;
    }

    public void setWeekCode(String weekCode) {
        this.weekCode = weekCode;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getCbDays() {
        return cbDays;
    }

    public void setCbDays(String cbDays) {
        this.cbDays = cbDays;
    }

}
