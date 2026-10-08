package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmEventbus.server.AbstractEventHandler;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmEventbus.server.EventBusSimple;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobEmailResult_Mdl;
import dataman.dmBase.dmSession.server.DmSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.websocket.CloseReason;

public class DmSchedulerFactory implements Runnable {

//    private ExecutorService cachedPool;
//    private ForkJoinPool forkjoinPool;
    private Thread thTimer;
    private AtomicBoolean boolThreadFlag;
    private List<DmAutoScheduler> autoSchedulars = new ArrayList<>();
    private DmObjectLock<List<DmAutoScheduler>> schedularsLock;
    private Calendar dtLastExecutionTimeOfThread;
    private long sleepSchedulerThreadTimeInMillis = 60000l;
    private Map<String, EventBusSimple> eventBuses;

    public DmSchedulerFactory() {
        this.schedularsLock = new DmObjectLock<>(this.autoSchedulars);
//        this.cachedPool = Executors.newCachedThreadPool();
//        this.forkjoinPool = new ForkJoinPool();
        this.eventBuses = new TreeMap<>();
        this.boolThreadFlag = new AtomicBoolean(true);
        this.thTimer = new Thread(this);
        this.thTimer.start();
    }

    public <T extends AbstractEventHandler> void addEventHandler(String eventName, T eventHandler) {
        if (!eventBuses.containsKey(eventName)) {
            System.err.println("in-1");
            EventBusSimple eventBus = new EventBusSimple();
            System.err.println("in-2");
            eventBus.subscribe(eventHandler);
            System.err.println("in-3");
            eventBuses.put(eventName, eventBus);
            System.err.println("in-4");
        } else if (eventBuses.containsKey(eventName)) {
            System.err.println("in-5");
            eventBuses.get(eventName).subscribe(eventHandler);
            System.err.println("in-6");
        }
        System.err.println("out");
    }

    public <T extends AbstractEventHandler> void removeEventHandler(String eventName, T eventHandler) {
        if (eventBuses.containsKey(eventName)) {
            eventBuses.get(eventName).unsubscribe(eventHandler);
        }
    }

    public <T extends Event> void publishEvent(T event) {
        String eventName = event.getType().trim();
        if (eventName != null) {
            if (eventBuses.containsKey(eventName)) {
                eventBuses.get(eventName).publish(event);
            }
        }
    }

    public boolean scheduleRemove(String docId) {
        SchedulerMast_Mdl s = new SchedulerMast_Mdl();
        s.setDocId(DmBaseShared.iNull(docId).longValue());
        return schedule(s, new Date(), true);
    }

    protected boolean schedule(SchedulerMast_Mdl schedular, Date dtNow, boolean calledForRemoval) {
        boolean bool = false;
        try {
            DmAutoScheduler tmpSchedular = new DmAutoScheduler(schedular, dtNow);
            if (!calledForRemoval) {
                if (!schedular.isActive()) {
                    this.schedularsLock.getObject().remove(tmpSchedular);
                } else if (this.schedularsLock.lockObject()) {

                    if (this.schedularsLock.getObject().stream().filter(s -> (s.getSchedularModel().getDocId().equals(schedular.getDocId()))).count() == 0) {
                        this.schedularsLock.getObject().add(tmpSchedular);
                    } else {
                        this.schedularsLock.getObject().remove(tmpSchedular);
                        this.schedularsLock.getObject().add(tmpSchedular);
                    }
                }
            } else if (this.schedularsLock.lockObject()) {
                this.schedularsLock.getObject().remove(tmpSchedular);
            }
            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        } finally {
            this.schedularsLock.releaseObject();
        }

        return bool;
    }

    public List<SchedulerMast_Mdl> getListedSchedulers() throws Exception {
        List<SchedulerMast_Mdl> lstSchedulers = new ArrayList<>();
        try {
            if (this.schedularsLock.lockObject()) {
                for (int i = 0; i < this.schedularsLock.getObject().size(); i++) {
                    SchedulerMast_Mdl oModel = this.schedularsLock.getObject().get(i).getSchedularModel();
                    SchedulerMast_Mdl model = new SchedulerMast_Mdl();
                    model.setActive(oModel.isActive());
                    model.setDayOrMonth(oModel.getDayOrMonth());
                    model.setDocId(oModel.getDocId());
                    model.setExpDateTime(oModel.getExpDateTime());
                    model.setExpUsed(oModel.isExpUsed());
                    model.setLastExecutionTime(oModel.getLastExecutionTime());
                    model.setName(oModel.getName());
                    model.setPlt(oModel.getPlt());
                    model.setRecurQuan(oModel.getRecurQuan());
                    model.setRecurUnit(oModel.getRecurUnit());
                    model.setSchType(oModel.getSchType());
                    model.setStartDateTime(oModel.getStartDateTime());
                    model.setUserEntryDate(oModel.getUserEntryDate());
                    model.setUserName(oModel.getUserName());
                    model.setWebJobTemplateId(oModel.getWebJobTemplateId());
                    model.setWebJobType(oModel.getWebJobType());
                    lstSchedulers.add(model);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.schedularsLock.releaseObject();
        }
        return lstSchedulers;
    }

    public boolean schedule(SchedulerMast_Mdl schedular, Date dtNow) {
        return schedule(schedular, dtNow, false);
    }

    public void clearAutoSchedulars() throws Exception {
        if (this.schedularsLock.lockObject()) {
            this.schedularsLock.getObject().clear();
            this.schedularsLock.releaseObject();
        }
    }

//    private void scheduleManualScheduler(DmManualScheduler manualScheduler) throws Exception {
//        if (manualScheduler.getWebJobType() == DmEnum.DmWebJobTypes.SMS) {
//            DmScheduleFollower<String> follower = new DmScheduleFollower<>(new DmScheduleEngine<>(manualScheduler), new DmSMSCallBackImpl<>());
//            this.cachedPool.submit(follower);
//            this.forkjoinPool.execute(follower.getEngine());
//        } else if (manualScheduler.getWebJobType() == DmEnum.DmWebJobTypes.EMAIL) {
//            DmScheduleFollower<WebJobEmailResult_Mdl> follower = new DmScheduleFollower<>(new DmScheduleEngine<>(manualScheduler), new DmEMailCallBackImpl());
//            this.cachedPool.submit(follower);
//            this.forkjoinPool.execute(follower.getEngine());
//        }
//    }
//    public boolean addManualSchedular(SiteEnviro_Mdl siteEnviro, SchedulerMast_Mdl schedular, Date dtNow, Map<String, List<String>> namesWithValues) {
//        boolean bool = false;
//        try {
//            if (!schedular.isActive()) {
//                throw new Exception("Scheduler is in-active.");
//            }
//            if (schedular.getStartDateTime().compareTo(dtNow) < 0) {
//                throw new Exception("Start Datetime of Scheduler is before the date provided.");
//            }
//            if (schedular.getExpDateTime() != null) {
//                if (schedular.getExpDateTime().compareTo(dtNow) < 0) {
//                    throw new Exception("Expiry Datetime of Scheduler is before the date provided.");
//                }
//                if (schedular.getStartDateTime().compareTo(schedular.getExpDateTime()) > 0) {
//                    throw new Exception("Start Datetime can't be greater than from Expiry Datetime.");
//                }
//            }
//
//            DmManualScheduler manualSchedular = new DmManualScheduler(siteEnviro, schedular, new Date(), namesWithValues);
//            scheduleManualScheduler(manualSchedular);
//
//        } catch (Exception ex) {
//            bool = false;
//            ex.printStackTrace();
//        }
//        return bool;
//    }
    public void stopWatching() {
        this.boolThreadFlag.set(false);
    }

    public void startWatching() {
        this.boolThreadFlag.set(true);
    }

    @Override
    public void run() {
        ExecutorService cachedPool = null;
        ForkJoinPool forkjoinPool = null;

        dtLastExecutionTimeOfThread = new GregorianCalendar();
        dtLastExecutionTimeOfThread.setTime(new Date());
        while (true) {
            Calendar dtNow = new GregorianCalendar();
            if (this.boolThreadFlag.get()) {
                try {
                    cachedPool = Executors.newCachedThreadPool();
                    forkjoinPool = new ForkJoinPool();

                    if (this.schedularsLock.lockObject()) {
                        dtNow.setTime(new Date());
                        List<DmAutoScheduler> executableSchedulars = this.schedularsLock.getObject().stream().filter(s -> ((s.shouldExecute(dtLastExecutionTimeOfThread, dtNow)))).collect(Collectors.toList());
                        fireSchdulers(dtNow, executableSchedulars, cachedPool, forkjoinPool);
                    }
                    checkSessionTasks();

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    forkjoinPool.shutdown();
                    cachedPool.shutdown();

                    cachedPool = null;
                    forkjoinPool = null;
                    this.schedularsLock.releaseObject();
                }
            }

            try {
                dtLastExecutionTimeOfThread.setTime(dtNow.getTime());
                Thread.sleep(sleepSchedulerThreadTimeInMillis);
            } catch (Exception exc) {
            }
        }
    }

    private void checkSessionTasks() {
        try {
            cleanExpiredSessions();
            lockSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    private void lockSessions() {
//        String sessionId;
//        try {
//            List<DmSession> sessionList = DmSession.getSessions().values().stream().filter(s -> s.getLastAccessDurationInSeconds() > DmBaseServer.sessionLockTimeOut).collect(Collectors.toList());
//            for (int i = 0; i < sessionList.size(); i++) {
//                sessionId = sessionList.get(i).getId();
//                DmSession.getSessions().get(sessionId.trim()).setSessionStatus(DmEnum.DmSessionStatus.LOCK);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    
    private void lockSessions() {
//        String sessionId;
        try {
            Iterator<DmSession> sessionIterator = DmSession.getSessions().values().iterator();
            while (sessionIterator.hasNext()) {
                DmSession session = sessionIterator.next();
                if (session.getLastAccessDurationInSeconds() > DmBaseServer.sessionLockTimeOut) {
                    session.setSessionStatus(DmEnum.DmSessionStatus.LOCK);
                }

                if ((session.getLockTimeOut() > 0) && (session.getLastAccessDurationInSeconds() > session.getLockTimeOut())) {
                    session.setSessionStatus(DmEnum.DmSessionStatus.LOCK);
                }
            }

//            List<DmSession> sessionList = DmSession.getSessions().values().stream().filter(s -> s.getLastAccessDurationInSeconds() > DmBaseServer.sessionLockTimeOut).collect(Collectors.toList());
//            for (int i = 0; i < sessionList.size(); i++) {
//                sessionId = sessionList.get(i).getId();
//                DmSession.getSessions().get(sessionId.trim()).setSessionStatus(DmEnum.DmSessionStatus.LOCK);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cleanExpiredSessions() {
        String sessionId;
        try {
            List<DmSession> sessionList = DmSession.getSessions().values().stream().filter(s -> s.getLastAccessDurationInSeconds() > DmBaseServer.sessionTimeOut).collect(Collectors.toList());
            for (int i = 0; i < sessionList.size(); i++) {
                sessionId = sessionList.get(i).getId();
                if (sessionList.get(i).getReceiverSession() != null) {
                    if (sessionList.get(i).getReceiverSession().isOpen()) {
                        sessionList.get(i).getReceiverSession().close(new CloseReason(CloseReason.CloseCodes.RESERVED, "Session get Expired."));
                    }
                }
                DmBaseServer.deleteFilesAndDirectories(sessionId); 
                DmSession.getSessions().remove(sessionId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fireSchdulers(Calendar dtNow, List<DmAutoScheduler> executableSchedulars, ExecutorService cachedPool, ForkJoinPool forkjoinPool) {
        try {
            for (int i = 0; i < executableSchedulars.size(); i++) {
                if (executableSchedulars.get(i).getWebJobType() == DmEnum.DmWebJobTypes.SMS) {
                    DmScheduleFollower<String> follower = new DmScheduleFollower<>(new DmScheduleEngine<>(executableSchedulars.get(i)), new DmSMSCallBackImpl<>());
                    cachedPool.submit(follower);
                    forkjoinPool.execute(follower.getEngine());
                } else if (executableSchedulars.get(i).getWebJobType() == DmEnum.DmWebJobTypes.EMAIL) {
                    DmScheduleFollower<WebJobEmailResult_Mdl> follower = new DmScheduleFollower<>(new DmScheduleEngine<>(executableSchedulars.get(i)), new DmEMailCallBackImpl());
                    cachedPool.submit(follower);
                    forkjoinPool.execute(follower.getEngine());
                } else if (executableSchedulars.get(i).getWebJobType() == DmEnum.DmWebJobTypes.EVENT) {
                    try {
                        DmSchedulerEventInvoker invoker = new DmSchedulerEventInvoker(executableSchedulars.get(i));
                        cachedPool.submit(invoker);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
