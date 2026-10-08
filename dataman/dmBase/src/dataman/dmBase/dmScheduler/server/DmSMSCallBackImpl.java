package dataman.dmBase.dmScheduler.server;

import java.util.List;

public class DmSMSCallBackImpl<V> implements DmScheduleEngine.IDmScheduleEngineCallback<String> {

    @Override
    public void callAfterCompletedNormally(List<DmWebJob<? extends IDmWebJob<String>, String>> result) {
        try {
            for (int i = 0; i < result.size(); i++) {
                DmWebJob<? extends IDmWebJob<String>, String> wj = result.get(i);
                System.out.println(wj.getResult().getResult());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void callExceptionWhileExecution(Throwable exception, DmScheduler schedualar) {
        exception.printStackTrace();
    }

}
