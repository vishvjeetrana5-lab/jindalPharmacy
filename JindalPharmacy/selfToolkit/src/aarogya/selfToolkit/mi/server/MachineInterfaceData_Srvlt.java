package aarogya.selfToolkit.mi.server;

import aarogya.projectMenu.mi.jvm.MiPersistenceModel;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MachineInterfaceData_Srvlt extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint savePoint = null;
        boolean bool = false;
        try {

            String userAgent = DmShared.xNull(req.getHeader("user-agent")).trim();
            String remoteHost = DmShared.xNull(req.getRemoteHost()).trim();

            StringBuilder sb = new StringBuilder();
            byte[] BUFFEREDBYTES = new byte[1024 * 1024];
            int readLength = req.getInputStream().read(BUFFEREDBYTES);
            while (readLength != -1) {
                String readString = new String(BUFFEREDBYTES, 0, readLength);
                sb.append(readString);
                readLength = req.getInputStream().read(BUFFEREDBYTES);
            }

            List<MiPersistenceModel> listPersistenceModel = DmServer.convertFromJson(sb.toString(), new TypeToken<List<MiPersistenceModel>>() {
            });

            if (listPersistenceModel.size() > 0) {
                cnTemp = DmServer.getConnection(DmServer.dbCompany);
                savePoint = new DmSavePoint(cnTemp);

                for (int i = 0; i < listPersistenceModel.size(); i++) {
                    MiPersistenceModel persistenceModel = listPersistenceModel.get(i);

                    String sql = "DELETE FROM MACHINESUBTESTOUTPUT WHERE (SAMPLERECID=:sampleRecId) AND (MACHINESUBTESTNAME=:machineSubTestName)";;
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
                    if (!DmShared.xNull(persistenceModel.sampleRecId).trim().equals("")) {
                        psTemp.setString("sampleRecId", DmShared.xNull(persistenceModel.sampleRecId).trim());
                    } else {
                        psTemp.setString("sampleRecId", DmShared.xNull(persistenceModel.labRecordId).trim());
                    }
                    psTemp.setString("machineSubTestName", DmShared.xNull(persistenceModel.subTestName).trim());
                    psTemp.executeUpdate();

                    psTemp.close();

                    sql = "INSERT INTO machineSubTestOutput(SAMPLERECID,MACHINESUBTESTNAME,MACHINESUBTESTVALUE,MACHINESUBTESTUNIT,U_ENTDT,"
                            + "LOCALPC_IP,LOCALPCNAME,REMOTEPC_IP,MACHINENAME) VALUES (:sampleRecId,:machineSubTestName,"
                            + ":machineSubTestValue,:machineSubTestUnit,:u_entDt,:localPc_Ip,:localPcName,:remotePc_Ip,:machineName)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);

//                    psTemp.setString("sampleRecId", DmShared.xNull(persistenceModel.labRecordId).trim());
                    if (!DmShared.xNull(persistenceModel.sampleRecId).trim().equals("")) {
                        psTemp.setString("sampleRecId", DmShared.xNull(persistenceModel.sampleRecId).trim());
                    } else {
                        psTemp.setString("sampleRecId", DmShared.xNull(persistenceModel.labRecordId).trim());
                    }
                    psTemp.setString("machineSubTestName", DmShared.xNull(persistenceModel.subTestName).trim());
                    psTemp.setString("machineSubTestValue", DmShared.xNull(persistenceModel.subTestValue).trim());
                    psTemp.setString("machineSubTestUnit", DmShared.xNull(persistenceModel.subTestUnit).trim());
//                    psTemp.setString("machineCode", DmShared.xNull(persistenceModel.machineCode).trim());
                    psTemp.setTimestamp("u_entDt", DmServer.getServerTimestamp());
                    psTemp.setString("localPc_Ip", DmShared.xNull(persistenceModel.localComputerIp).trim());
                    psTemp.setString("localPcName", DmShared.xNull(persistenceModel.localComputerName).trim());
                    psTemp.setString("remotePc_Ip", DmShared.xNull(remoteHost).trim());
                    psTemp.setString("machineName", DmShared.xNull(persistenceModel.machineName).trim());

                    psTemp.executeUpdate();
                    psTemp.close();
                }

            }

            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        } finally {
            try {
                if (savePoint != null) {
                    if (bool) {
                        savePoint.commit();
                    } else {
                        savePoint.rollBack();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (psTemp != null) {
                    psTemp.close();
                    psTemp = null;
                }
            } catch (Exception ex) {
            }
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
