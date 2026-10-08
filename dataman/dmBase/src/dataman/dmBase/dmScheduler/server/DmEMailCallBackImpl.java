package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmScheduler.shared.WebJobEmailResult_Mdl;
import java.util.List;

public class DmEMailCallBackImpl implements DmScheduleEngine.IDmScheduleEngineCallback<WebJobEmailResult_Mdl> {

    @Override
    public void callAfterCompletedNormally(List<DmWebJob<? extends IDmWebJob<WebJobEmailResult_Mdl>, WebJobEmailResult_Mdl>> result) {
        try {
            for (int i = 0; i < result.size(); i++) {
                DmWebJob<? extends IDmWebJob<WebJobEmailResult_Mdl>, WebJobEmailResult_Mdl> wj = result.get(i);
                System.out.println(wj.getResult().getResult());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void callExceptionWhileExecution(Throwable exception, DmScheduler schedualar) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
