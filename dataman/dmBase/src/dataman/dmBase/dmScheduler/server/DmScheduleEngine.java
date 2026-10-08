package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobEmailResult_Mdl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class DmScheduleEngine<V> extends RecursiveTask<List<DmWebJob<? extends IDmWebJob<V>, V>>> {

    private List<DmWebJob<? extends IDmWebJob<V>, V>> lstJobs = new ArrayList<>();

    private DmScheduler serverSchedular;

    public interface IDmScheduleEngineCallback<V> {

        public void callAfterCompletedNormally(List<DmWebJob<? extends IDmWebJob<V>, V>> result);

        public void callExceptionWhileExecution(Throwable exception, DmScheduler schedualar);

    }

    public DmScheduleEngine(DmScheduler dmSchedular) {
        this.serverSchedular = dmSchedular;
    }

    public DmScheduler getSchedular() {
        return this.serverSchedular;
    }

    @Override
    protected List<DmWebJob<? extends IDmWebJob<V>, V>> compute() {
        this.lstJobs.clear();

        this.serverSchedular.initialize();

        for (int i = 0; i < this.serverSchedular.getJobs().size(); i++) {
            DmWebJob wj = null;
            for (int s = 0; s < this.serverSchedular.getSchedularModel().getLstSites().size(); s++) {
                Integer siteCode = this.serverSchedular.getSchedularModel().getLstSites().get(s);
                if (this.serverSchedular.getEnviros().containsKey(siteCode)) {
                    SiteEnviro_Mdl sEnviro = this.serverSchedular.getEnviros().get(siteCode);
                    if (this.serverSchedular.getWebJobType() == DmEnum.DmWebJobTypes.SMS) {
                        wj = new DmWebJob<IDmWebJob<String>, String>(new DmSMSWebJob(), this.serverSchedular.getJobs().get(i), this.serverSchedular.getAttachments());
                    } else if (this.serverSchedular.getWebJobType() == DmEnum.DmWebJobTypes.EMAIL) {
                        wj = new DmWebJob<IDmWebJob<WebJobEmailResult_Mdl>, WebJobEmailResult_Mdl>(new DmEMailWebJob(), this.serverSchedular.getJobs().get(i), this.serverSchedular.getAttachments());
                    }
                    wj.setSiteEnviro(sEnviro);
                }
            }

            if (wj != null) {
                this.lstJobs.add(wj);
                wj.fork();
            }
        }

        for (int i = 0; i < this.lstJobs.size(); i++) {
            this.lstJobs.get(i).join();
        }

        Date dtNow = new Date();

        this.serverSchedular.getSchedularModel().setLastExecutionTime(new Date(dtNow.getYear(), dtNow.getMonth(), dtNow.getDate(), this.serverSchedular.getSchedularModel().getStartDateTime().getHours(), this.serverSchedular.getSchedularModel().getStartDateTime().getMinutes(), this.serverSchedular.getSchedularModel().getStartDateTime().getSeconds()));

        return this.lstJobs;
    }

}
