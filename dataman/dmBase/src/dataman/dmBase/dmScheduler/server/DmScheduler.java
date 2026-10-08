package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.TemplateMast_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public abstract class DmScheduler {

    private SchedulerMast_Mdl model;
    private TemplateMast_Mdl tempModel;
    protected Date dtCmp;
    private Map<String, DmMessageParam> lstMsgParams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private Map<Integer, SiteEnviro_Mdl> lstEnviros = new HashMap<>();
    private List<DmWebJobDataRow> lstWebJobs = new ArrayList<>();
    private List<WebJobAttachment_Mdl> lstAttachments = new ArrayList<>();

    public DmScheduler(SchedulerMast_Mdl model, Date dtTemp) {
        this.dtCmp = dtTemp;
        this.model = model;
    }

    public List<WebJobAttachment_Mdl> getAttachments() {
        return this.lstAttachments;
    }

    public List<DmWebJobDataRow> getWebJobs() {
        return this.lstWebJobs;
    }

    public Map<Integer, SiteEnviro_Mdl> getEnviros() {
        return this.lstEnviros;
    }

    public Map<String, DmMessageParam> getMessageParams() {
        return this.lstMsgParams;
    }

    public SchedulerMast_Mdl getSchedularModel() {
        return this.model;
    }

    public void setTemplateModel(TemplateMast_Mdl tModel) {
        this.tempModel = tModel;
    }

    public TemplateMast_Mdl getTemplateModel() {
        return this.tempModel;
    }

    public String getTemplateName() {
        return this.tempModel.getTemplateName();
    }

    public long getTemplateId() {
        return this.tempModel.getTemplateId();
    }

    public String getSubjectFormat() {
        return this.tempModel.getSubjectFormat().trim();
    }

    public String getFileOrFunctionName() {
        return this.tempModel.getFileOrFuncName();
    }

    public String getMessageFormat() {
        return this.tempModel.getMessageFormat();
    }

    public DmEnum.DmWebJobTypes getWebJobType() {
        return this.model.getWebJobType();
    }

    public DmEnum.DmWebJobDataSources getDataSource() {
        return this.tempModel.getWebJobDataSource();
    }

    protected boolean processForDaily(Calendar dtPrevious, Calendar dtNow) throws Exception {
        boolean bool = false;

        if ((this.model.isActive()) && (this.model.getRecurUnit() == DmEnum.DmRecurrenceUnit.DAYS)) {
            Calendar dtTemp = new GregorianCalendar();
            if (this.model.getLastExecutionTime() == null) {
                dtTemp.setTime(this.model.getStartDateTime());
                dtTemp.set(dtNow.get(Calendar.YEAR), dtNow.get(Calendar.MONTH), dtNow.get(Calendar.DAY_OF_MONTH));
                bool = ((dtTemp.compareTo(dtPrevious) >= 0) && (dtNow.compareTo(dtTemp) >= 0));
            } else if (this.model.getLastExecutionTime() != null) {
                boolean willAsses = false;
                if (this.model.isExpUsed()) {
                    dtTemp.setTime(this.model.getExpDateTime());
                    //if (dtTemp.compareTo(dtNow) <= 0) {
                    if (dtTemp.compareTo(dtNow) >= 0) {
                        willAsses = true;
                    }
                } else if (!this.model.isExpUsed()) {
                    willAsses = true;
                }
                if (willAsses) {
                    dtTemp.setTime(this.model.getLastExecutionTime());
                    dtTemp.add(Calendar.DAY_OF_YEAR, this.model.getRecurQuan());
                    bool = ((dtTemp.compareTo(dtPrevious) >= 0) && (dtNow.compareTo(dtTemp) >= 0));
                }
            }
        }

        if (bool) {
            System.out.println("Pocessing Daily Scheduler..." + this.model.getName() + "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]");
        }

        return bool;
    }

    protected boolean processForWeekly(Calendar dtPrevious, Calendar dtNow) throws Exception {
        boolean bool = false;

        boolean processFurther = false;
        Calendar dtTemp = new GregorianCalendar();

        if ((this.model.isActive()) && (this.model.getRecurUnit() == DmEnum.DmRecurrenceUnit.WEEKS)) {
            if (this.model.getLastExecutionTime() == null) {
                dtTemp.setTime(this.model.getStartDateTime());
                dtTemp.set(dtNow.get(Calendar.YEAR), dtNow.get(Calendar.MONTH), dtNow.get(Calendar.DAY_OF_MONTH));
                processFurther = ((dtTemp.compareTo(dtPrevious) >= 0) && (dtNow.compareTo(dtTemp) >= 0));
            } else if (this.model.getLastExecutionTime() != null) {
                boolean willAsses = false;
                if (this.model.isExpUsed()) {
                    dtTemp.setTime(this.model.getExpDateTime());
                    if (dtTemp.compareTo(dtNow) >= 0) {
                        willAsses = true;
                    }
                } else if (!this.model.isExpUsed()) {
                    willAsses = true;
                }
                if (willAsses) {
                    dtTemp.setTime(this.model.getLastExecutionTime());
                    dtTemp.add(Calendar.WEEK_OF_YEAR, this.model.getRecurQuan());
                    processFurther = ((dtTemp.compareTo(dtPrevious) >= 0) && (dtNow.compareTo(dtTemp) >= 0));
                }
            }
        }

        if (processFurther) {
            for (int i = 0; i < this.model.getLstDays().size(); i++) {
                DmEnum.DmWeekdays day = this.model.getLstDays().get(i);
                bool = (day.getWeekDay() == dtTemp.get(Calendar.DAY_OF_WEEK));
                if (bool) {
                    break;
                }
            }
        }

        if (bool) {
            System.out.println("Pocessing Weekly Scheduler..." + this.model.getName() + "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]");
        }

        return bool;
    }

//    protected boolean processForMonthly(Calendar dtPrevious, Calendar dtNow) throws Exception {
//        boolean bool = false;
//
//        Calendar dtTemp = new GregorianCalendar();
//
//        if (this.model.isActive() && (this.model.getRecurUnit() == DmEnum.DmRecurrenceUnit.MONTHS)) {
//            boolean processFurther = false;
//            dtTemp.setTime(dtNow.getTime());
//            moveout:
//            {
//                for (int i = 0; i < this.model.getLstMonths().size(); i++) {
//                    DmEnum.DmMonths month = this.model.getLstMonths().get(i);
//                    if (month.getMonthCalendarValue() == dtTemp.get(Calendar.MONTH)) {
//                        if (this.model.getDayOrMonth().equalsIgnoreCase("d")) {
//                            for (int d = 0; d < this.model.getLstDates().size(); d++) {
//                                DmEnum.DmDates date = this.model.getLstDates().get(d);
//                                if (((date == DmEnum.DmDates.DATE_LAST) && (dtTemp.getActualMaximum(Calendar.DATE) == dtTemp.get(Calendar.DATE))) || (date.getValue() == dtTemp.get(Calendar.DATE))) {
//                                    processFurther = true;
//                                    break moveout;
//                                }
//                            }
//                        } else if (this.model.getDayOrMonth().equalsIgnoreCase("m")) {
//                            for (int m = 0; m < this.model.getLstWeeks().size(); m++) {
//                                DmEnum.DmWeeks week = this.model.getLstWeeks().get(m);
//                                if (((week == DmEnum.DmWeeks.LAST) && (week.getWeekCalendarValue() == dtTemp.getActualMaximum(Calendar.WEEK_OF_MONTH))) || (week.getWeekCalendarValue() == dtTemp.get(Calendar.WEEK_OF_MONTH))) {
//                                    for (int wd = 0; wd < this.model.getLstDays().size(); wd++) {
//                                        DmEnum.DmWeekdays day = this.model.getLstDays().get(wd);
//                                        if (day.getWeekDay() == dtTemp.get(Calendar.DAY_OF_WEEK)) {
//                                            processFurther = true;
//                                            break moveout;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (processFurther) {
//                if (this.model.getLastExecutionTime() == null) {
//                    dtTemp.setTime(this.model.getStartDateTime());
//                    dtTemp.set(dtNow.get(Calendar.YEAR), dtNow.get(Calendar.MONTH), dtNow.get(Calendar.DAY_OF_MONTH));
//                    bool = ((dtTemp.compareTo(dtPrevious) >= 0) && (dtNow.compareTo(dtTemp) >= 0));
//                } else {
//                    if (this.model.isExpUsed()) {
//                        dtTemp.setTime(this.model.getExpDateTime());
//                        if (dtTemp.compareTo(dtNow) >= 0) {
//                            dtTemp.setTime(this.model.getLastExecutionTime());
//                            bool = ((dtTemp.compareTo(dtPrevious) >= 0) && (dtNow.compareTo(dtTemp) >= 0));
//                        }
//                    } else if (!this.model.isExpUsed()) {
//                        dtTemp.setTime(this.model.getLastExecutionTime());
//                        bool = ((dtTemp.compareTo(dtPrevious) >= 0) && (dtNow.compareTo(dtTemp) >= 0));
//                    }
//                }
//            }
//        }
//
//        if (bool) {
//            System.out.println("Pocessing Monthly Scheduler..." + this.model.getName() + "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]");
//        }
//
//        return bool;
//    }
    protected boolean processForMonthly(Calendar dtPrevious, Calendar dtNow) throws Exception {
        boolean bool = false;

        Calendar dtTemp = new GregorianCalendar();

        if (this.model.isActive() && (this.model.getRecurUnit() == DmEnum.DmRecurrenceUnit.MONTHS)) {
            boolean processFurther = false;
            dtTemp.setTime(dtNow.getTime());
            moveout:
            {
                for (int i = 0; i < this.model.getLstMonths().size(); i++) {
                    DmEnum.DmMonths month = this.model.getLstMonths().get(i);
                    if (month.getMonthCalendarValue() == dtTemp.get(Calendar.MONTH)) {
                        if (this.model.getDayOrMonth().equalsIgnoreCase("d")) {
                            for (int d = 0; d < this.model.getLstDates().size(); d++) {
                                DmEnum.DmDates date = this.model.getLstDates().get(d);
                                if (((date == DmEnum.DmDates.DATE_LAST) && (dtTemp.getActualMaximum(Calendar.DATE) == dtTemp.get(Calendar.DATE))) || (date.getValue() == dtTemp.get(Calendar.DATE))) {
                                    processFurther = true;
                                    break moveout;
                                }
                            }
                        } else if (this.model.getDayOrMonth().equalsIgnoreCase("m")) {
                            for (int m = 0; m < this.model.getLstWeeks().size(); m++) {
                                DmEnum.DmWeeks week = this.model.getLstWeeks().get(m);
                                if (((week == DmEnum.DmWeeks.LAST) && (week.getWeekCalendarValue() == dtTemp.getActualMaximum(Calendar.WEEK_OF_MONTH))) || (week.getWeekCalendarValue() == dtTemp.get(Calendar.WEEK_OF_MONTH))) {
                                    for (int wd = 0; wd < this.model.getLstDays().size(); wd++) {
                                        DmEnum.DmWeekdays day = this.model.getLstDays().get(wd);
                                        if (day.getWeekDay() == dtTemp.get(Calendar.DAY_OF_WEEK)) {
                                            processFurther = true;
                                            break moveout;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (processFurther) {
                if (this.model.getLastExecutionTime() == null) {
//                    dtTemp.setTime(this.model.getStartDateTime());
//                    dtTemp.set(dtNow.get(Calendar.YEAR), dtNow.get(Calendar.MONTH), dtNow.get(Calendar.DAY_OF_MONTH));
                    GregorianCalendar dtStartTimeStamp = new GregorianCalendar();
                    dtStartTimeStamp.setTime(model.getStartDateTime());

                    dtTemp.set(Calendar.HOUR_OF_DAY, dtStartTimeStamp.get(Calendar.HOUR_OF_DAY));
                    dtTemp.set(Calendar.MINUTE, dtStartTimeStamp.get(Calendar.MINUTE));
                    dtTemp.set(Calendar.SECOND, dtStartTimeStamp.get(Calendar.SECOND));

                    bool = ((dtTemp.compareTo(dtPrevious) >= 0) && (dtNow.compareTo(dtTemp) >= 0));
                } else {
                    if (this.model.isExpUsed()) {
                        dtTemp.setTime(this.model.getExpDateTime());
                        if (dtTemp.compareTo(dtNow) >= 0) {

                            GregorianCalendar dtStartTimeStamp = new GregorianCalendar();
                            dtStartTimeStamp.setTime(model.getStartDateTime());

                            //dtTemp.setTime(this.model.getLastExecutionTime());
                            dtTemp.set(Calendar.HOUR_OF_DAY, dtStartTimeStamp.get(Calendar.HOUR_OF_DAY));
                            dtTemp.set(Calendar.MINUTE, dtStartTimeStamp.get(Calendar.MINUTE));
                            dtTemp.set(Calendar.SECOND, dtStartTimeStamp.get(Calendar.SECOND));

                            bool = ((dtTemp.compareTo(dtPrevious) >= 0) && (dtNow.compareTo(dtTemp) >= 0));
                        }
                    } else if (!this.model.isExpUsed()) {
//                        dtTemp.setTime(this.model.getLastExecutionTime());

                        GregorianCalendar dtStartTimeStamp = new GregorianCalendar();
                        dtStartTimeStamp.setTime(model.getStartDateTime());

                        //dtTemp.setTime(this.model.getLastExecutionTime());
                        dtTemp.set(Calendar.HOUR_OF_DAY, dtStartTimeStamp.get(Calendar.HOUR_OF_DAY));
                        dtTemp.set(Calendar.MINUTE, dtStartTimeStamp.get(Calendar.MINUTE));
                        dtTemp.set(Calendar.SECOND, dtStartTimeStamp.get(Calendar.SECOND));

                        bool = ((dtTemp.compareTo(dtPrevious) >= 0) && (dtNow.compareTo(dtTemp) >= 0));
                    }
                }
            }
        }

        if (bool) {
            System.out.println("Pocessing Monthly Scheduler..." + this.model.getName() + "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]");
        }

        return bool;
    }

    public List<DmWebJobDataRow> getJobs() {
        return this.lstWebJobs;
    }

    public boolean initialize() {
        boolean bool = false;
        Connection cnTemp = null;

        try {
            cnTemp = DmBaseServer.getConnection(this.getSchedularModel().getActiveDB());

            fillJobDetails(cnTemp);

            this.getAttachments().clear();
            if (this.model.getWebJobType() == DmEnum.DmWebJobTypes.EMAIL) {
                fillAttachmentDetails(cnTemp);
            }

            this.getWebJobs().clear();
            fillSiteCodes(cnTemp);

            if (this.model.getWebJobType() != DmEnum.DmWebJobTypes.EVENT) {
                for (Map.Entry<Integer, SiteEnviro_Mdl> entry : this.getEnviros().entrySet()) {
                    fillDataAndParameters(entry.getValue(), cnTemp);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
            }
        }
        return bool;
    }

    public void fillDataAndParameters(SiteEnviro_Mdl siteEnviro, Connection cnTemp) throws Exception {
        this.getMessageParams().clear();
        if (this.getDataSource() == DmEnum.DmWebJobDataSources.CSV) {
            fillDataAndParametersFromFile(siteEnviro);
        } else if (this.getDataSource() == DmEnum.DmWebJobDataSources.FUNCTION) {
            fillDataAndParametersFromDB(siteEnviro, cnTemp);
        }
    }

    protected void addWebJobRow(DmWebJobDataRow row) {
        row.setSubjectFormat(this.getSubjectFormat());
        row.setDataSource(this.getDataSource());
        row.setFormattedSubject(row.getMessageFromUnformatted(this.getSubjectFormat()));
        row.setFunctionOrfileName(this.getFileOrFunctionName());
        row.setJobtype(this.getWebJobType());
        row.setTemplateId(this.getTemplateId());
        row.setTemplateName(this.getTemplateName());
        this.getWebJobs().add(row);
    }

    public abstract boolean shouldExecute(Calendar dtPrevious, Calendar dtNow);

    public abstract void fillJobDetails(Connection cnTemp) throws Exception;

    public abstract void fillAttachmentDetails(Connection cnTemp) throws Exception;

    public abstract void fillSiteCodes(Connection cnTemp) throws Exception;

    public abstract void fillDataAndParametersFromDB(SiteEnviro_Mdl siteEnviro, Connection cnTemp) throws Exception;

    public abstract void fillDataAndParametersFromFile(SiteEnviro_Mdl siteEnviro) throws Exception;

    public long getDifferenceFromStartingDateTimeInSeconds(Date dtNow, Date lastExecDate) {
        Date dtTemp = null;
        if (this.model.getLastExecutionTime() != null) {
            dtTemp = new Date(dtNow.getYear(), dtNow.getMonth(), dtNow.getDate(), this.model.getLastExecutionTime().getHours(), this.model.getLastExecutionTime().getMinutes(), this.model.getLastExecutionTime().getSeconds());
        } else if (this.model.getLastExecutionTime() == null) {
            dtTemp = new Date(this.model.getStartDateTime().getYear(), this.model.getStartDateTime().getMonth(), this.model.getStartDateTime().getDate(), this.model.getStartDateTime().getHours(), this.model.getStartDateTime().getMinutes(), this.model.getStartDateTime().getSeconds());
        }
        return TimeUnit.SECONDS.toSeconds(dtTemp.getTime() - lastExecDate.getTime());
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;
        if (obj != null) {
            if (obj instanceof DmScheduler) {
                retVal = this.getSchedularModel().equals(((DmScheduler) obj).getSchedularModel());
            }
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getSchedularModel() != null ? this.getSchedularModel().hashCode() : 0);
        return hash;
    }
}
