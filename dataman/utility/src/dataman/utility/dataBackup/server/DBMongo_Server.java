package dataman.utility.dataBackup.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DataBackUp;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.dmEventbus.server.AbstractEventHandler;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.utility.dataBackup.shared.GrdDataBackup_Mdl;
import dataman.dmBase.self.server.DmHandelFile;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DBMongo_Server extends AbstractEventHandler<Event> {

    public DBMongo_Server() {
        super(DataBackUp.DB_MONGO.getValue());
    }

    @Override
    public void handle(Event event) {
        SchedulerMast_Mdl schedulerMast_Mdl;
        List<GrdDataBackup_Mdl> mrTemp = new ArrayList<>();
        GrdDataBackup_Mdl node;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        try {
            schedulerMast_Mdl = event.get(DmEnum.DmEventSystemArgs.SchedulerMaster_Model.toString(), SchedulerMast_Mdl.class);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlQuery = "select elementType, elementName, elementLocation, backupLocation "
                    + " from backupInfo "
                    + " where elementType = :elementType ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setInt("elementType", DataBackUp.DB_MONGO.getId());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdDataBackup_Mdl();
                node.setChecked(true);
                node.setBackupLocation(DmShared.xNull(rsTemp.getString("backupLocation")));
                node.setElementLocation(DmShared.xNull(rsTemp.getString("elementLocation")));
                node.setElementName(DmShared.xNull(rsTemp.getString("elementName")));
                node.setElementType(DataBackUp.getKeyOf(DmShared.iNull(rsTemp.getString("elementType")).intValue()).getValue());
                node.setElementTypeCode(DmShared.iNull(rsTemp.getString("elementType")).toString());
                mrTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            if (mrTemp.size() > 0) {
                backup(mrTemp);
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void backup(List<GrdDataBackup_Mdl> mrTemp) {
        DmHandelFile handelFileLog = null;
        SimpleDateFormat sdf;
        String formattedDateTime, formattedDate, backupLocation, directoryPath;
        File tempFile = null;
        try {
            sdf = new SimpleDateFormat("yyyyMMdd");
            formattedDate = sdf.format(DmServer.getServerTimestamp().getTime());

            sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
            formattedDateTime = sdf.format(DmServer.getServerTimestamp().getTime());
//            handelFileLog = new DmHandelFile(DmServer.Path.logs + File.separatorChar + "dbBackup" + File.separatorChar + "dbMongo.log", true);
            directoryPath = DmServer.Path.logs + File.separatorChar + "dbBackup";
            tempFile = new File(directoryPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            tempFile = null;
            handelFileLog = new DmHandelFile(directoryPath + File.separatorChar + "dbMongo.log", true);
            for (GrdDataBackup_Mdl dbBackup_Mdl : mrTemp) {
                if (dbBackup_Mdl.isChecked()) {
                    if (DmShared.xNull(dbBackup_Mdl.getElementName()).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Element Name not found");
                    }
                    if (DmShared.xNull(dbBackup_Mdl.getElementLocation()).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Element Location not found");
                    }
                    if (DmShared.xNull(dbBackup_Mdl.getBackupLocation()).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Backup Location not found");
                    }
                    if (DmShared.iNull(dbBackup_Mdl.getElementTypeCode()).intValue() == DataBackUp.DB_MONGO.getId()) {
                        try {
                            backupLocation = dbBackup_Mdl.getBackupLocation().replaceAll("\\{date\\}", formattedDate);
                            tempFile = new File(backupLocation);
                            if (!tempFile.exists()) {
                                tempFile.mkdirs();
                            }
                            tempFile = null;

                            dbBackup_Mdl.setBackupFileLocation(DmShared.xNull(backupLocation) + File.separatorChar + formattedDateTime + "_" + DmShared.xNull(dbBackup_Mdl.getElementName()));
                            ExecCommand cmd = new ExecCommand("\"" + DmShared.xNull(dbBackup_Mdl.getElementLocation()) + "\" --host " + DmServer.mongoDBServer + " --port " + DmServer.mongoDBPort + " --db " + DmShared.xNull(dbBackup_Mdl.getElementName()) + " --out \"" + dbBackup_Mdl.getBackupFileLocation() + "\"");
                            dbBackup_Mdl.setStatus(cmd.getTerminationCode() == 0 ? "Backup Successfully." : "Unsuccessful");

                            handelFileLog.writeLine(formattedDateTime + " " + dbBackup_Mdl.getStatus() + " " + dbBackup_Mdl.getBackupFileLocation());
                            handelFileLog.writeLine(formattedDateTime + " " + dbBackup_Mdl.getStatus() + " " + cmd.getError());
                        } catch (Exception e) {
                            dbBackup_Mdl.setStatus("Unsuccessful");
                            handelFileLog.writeLine(formattedDateTime + " " + dbBackup_Mdl.getElementType() + ": " + e.getMessage());
                        } finally {
                        }
                    }
                }
            }
        } catch (Exception ee) {
            try {
                handelFileLog.writeLine("dbMongo: " + ee.getMessage());
            } catch (Exception he) {
            }
        } finally {
            try {
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception fe) {
            }
        }
    }

}
