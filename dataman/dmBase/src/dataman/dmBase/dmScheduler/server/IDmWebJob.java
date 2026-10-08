package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import java.util.List;

public interface IDmWebJob<T> {

    public DmWebJobResult<T> executeJob(DmWebJobDataRow dataRow, SiteEnviro_Mdl siteEnviro, List<WebJobAttachment_Mdl> attachments);
}
