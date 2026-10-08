package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class DmWebJob<T extends IDmWebJob<V>, V> extends RecursiveTask<DmWebJobResult<V>> {

    T webJob;
    DmWebJobDataRow datarow;
    DmWebJobResult<V> result;
    SiteEnviro_Mdl sEnviro;
    List<WebJobAttachment_Mdl> attachments;

    public DmWebJob(T job, DmWebJobDataRow wjRow, List<WebJobAttachment_Mdl> emailAttachments) {
        this.datarow = wjRow;
        this.webJob = job;
        this.attachments = emailAttachments;
    }

    public void setSiteEnviro(SiteEnviro_Mdl siteEnviro) {
        this.sEnviro = siteEnviro;
    }

    public DmWebJobResult<V> getResult() {
        return this.result;
    }

    @Override
    protected DmWebJobResult<V> compute() {
        this.result = this.webJob.executeJob(datarow, this.sEnviro, this.attachments);
        return this.result;
    }
}
