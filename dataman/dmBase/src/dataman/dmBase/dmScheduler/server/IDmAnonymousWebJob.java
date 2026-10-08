package dataman.dmBase.dmScheduler.server;

import java.util.concurrent.Callable;


public interface IDmAnonymousWebJob<T> extends Callable<DmWebJobResult<T>> {
}
