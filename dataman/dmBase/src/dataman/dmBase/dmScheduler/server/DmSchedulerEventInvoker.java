package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.TemplateMast_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class DmSchedulerEventInvoker implements Callable<Void> {

    private DmAutoScheduler scheduler;

    public DmSchedulerEventInvoker(DmAutoScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Void call() throws Exception {
        try {
            List<TemplateMast_Mdl> lstTemplates = getTemplates();
            for (int i = 0; i < lstTemplates.size(); i++) {
                TemplateMast_Mdl model = lstTemplates.get(i);
                String schedulerEventName = model.getTemplateName();
                Event event = new Event(schedulerEventName);
                event.set(DmEnum.DmEventSystemArgs.SchedulerMaster_Model.toString(), scheduler.getSchedularModel());
                event.set(DmEnum.DmEventSystemArgs.Template_Model.toString(), model);
                event.set(DmEnum.DmEventSystemArgs.CurrentDateTime.toString(), new Date());
                DmBaseServer.getWebServiceSchedulars().publishEvent(event);
            }

        } catch (Exception ex) {
            throw new Exception(ex);
        } finally {
            this.scheduler.getSchedularModel().setLastExecutionTime(new Date());
        }
        return null;
    }

    public List<TemplateMast_Mdl> getTemplates() throws Exception {
        List<TemplateMast_Mdl> lstTemplates = new ArrayList<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            cnTemp = DmBaseServer.getConnection(scheduler.getSchedularModel().getActiveDB());
            String sql = "SELECT * FROM DBO.WJTEMPLATEMAST WHERE (CODE=:templateCode) AND (TEMPLATETYPE=:templateType)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setLong("templateCode", DmBaseShared.iNull(scheduler.getSchedularModel().getWebJobTemplateId()).longValue());
            psTemp.setLong("templateType", DmBaseShared.iNull(scheduler.getWebJobType().getWebJobType()).shortValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                TemplateMast_Mdl templateModel = new TemplateMast_Mdl();
                templateModel.setTemplateId(this.scheduler.getSchedularModel().getWebJobTemplateId());
                templateModel.setWebJobType(this.scheduler.getWebJobType());
                templateModel.setWebJobDataSource(null);
                templateModel.setFileOrFuncName(DmBaseShared.xNull(rsTemp.getString("functionName")));
                templateModel.setMessageFormat(DmBaseShared.xNull(rsTemp.getString("message")));
                templateModel.setSubjectFormat(DmBaseShared.xNull(rsTemp.getString("subject")));
                templateModel.setTemplateName(DmBaseShared.xNull(rsTemp.getString("name")));
                lstTemplates.add(templateModel);
            }
            rsTemp.close();
            psTemp.close();
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
                rsTemp = null;
            }
            if (psTemp != null) {
                psTemp.close();
                psTemp = null;
            }
            if (cnTemp != null && !cnTemp.isClosed()) {
                cnTemp.close();
                cnTemp = null;
            }
        }
        return lstTemplates;
    }

}
