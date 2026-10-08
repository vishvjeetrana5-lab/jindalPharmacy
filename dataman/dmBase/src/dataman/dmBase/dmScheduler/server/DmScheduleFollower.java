package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import java.io.File;
import java.sql.Connection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DmScheduleFollower<V> implements Runnable {

    private DmScheduleEngine<V> engine;
    private DmScheduleEngine.IDmScheduleEngineCallback<V> callBack;

    public DmScheduleFollower(DmScheduleEngine<V> dmEngine, DmScheduleEngine.IDmScheduleEngineCallback<V> callback) {
        this.engine = dmEngine;
        this.callBack = callback;
    }

    public DmScheduleEngine<V> getEngine() {
        return engine;
    }

    public void setEngine(DmScheduleEngine<V> engine) {
        this.engine = engine;
    }

    public DmScheduleEngine.IDmScheduleEngineCallback<V> getCallBack() {
        return callBack;
    }

    public void setCallBack(DmScheduleEngine.IDmScheduleEngineCallback<V> callBack) {
        this.callBack = callBack;
    }

    @Override
    public void run() {
        try {
            while (!this.engine.isDone()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            processAfterExecution();

            if (this.engine.isCompletedNormally()) {
                this.callBack.callAfterCompletedNormally(this.engine.get());
            } else if (this.engine.isCompletedAbnormally()) {
                this.callBack.callExceptionWhileExecution(this.engine.getException(), this.engine.getSchedular());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void processAfterExecution() {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        try {
            if (this.engine.getSchedular().getSchedularModel().getSchType() == DmEnum.DmSchedularType.ONCE) {
                cnTemp = DmBaseServer.getConnection(DmBaseServer.dbCompany);
                String sql = "UPDATE SCHEDULERMAST SET ISACTIVE=:isActive WHERE (DOCID=:docId)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
                psTemp.setInt("isActive", DmEnum.DmYesNo.NO.toInt());
                psTemp.setLong("docId", this.engine.getSchedular().getSchedularModel().getDocId());
                psTemp.executeUpdate();

                DmBaseServer.getWebServiceSchedulars().schedule(this.engine.getSchedular().getSchedularModel(), new Date(), true);
            }
            if (this.engine.getSchedular().getSchedularModel().getWebJobType() != DmEnum.DmWebJobTypes.SMS) {
                if (engine.getSchedular().getAttachments() != null) {
                    for (int i = 0; i < engine.getSchedular().getAttachments().size(); i++) {
                        WebJobAttachment_Mdl attachedResource = engine.getSchedular().getAttachments().get(i);
                        File attachedFile = new File(attachedResource.getFileDirectory(), attachedResource.getActFileName());
                        if (attachedFile != null) {
                            if (!attachedFile.delete()) {
                                System.out.println("Unable to Delete attached file : " + attachedFile.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                    psTemp = null;
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
            }
        }
    }

}
