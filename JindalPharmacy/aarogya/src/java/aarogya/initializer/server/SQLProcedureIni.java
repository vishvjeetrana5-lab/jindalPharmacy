package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.utility.exportProcedure.server.Procedure_Mdl;
import dataman.utility.importProcedure.shared.GrdProcedure_Mdl;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SQLProcedureIni implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    public SQLProcedureIni() {
    }

    @Override
    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model) {
        File uploadedDir = null, datFile = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        GrdProcedure_Mdl node = null;
        List<GrdProcedure_Mdl> lstTemp = new ArrayList<>();
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {
            this.rssTemp = rssTemp;
            refreshDataStructure = new DmHandelFile(model.getLogFilePath(), true);
            uploadedDir = new File(model.getDataPath());
            if (uploadedDir.exists()) {
                if (uploadedDir.list() != null) {
                    datFile = new File(model.getDataPath() + File.separatorChar + uploadedDir.list()[0]);
                    if (datFile.isFile()) {
                        try {
                            fis = new FileInputStream(datFile);
                            ois = new ObjectInputStream(fis);
                            for (Procedure_Mdl procMdl : (List<Procedure_Mdl>) ois.readObject()) {
                                node = new GrdProcedure_Mdl();
                                node.setProcedureName(procMdl.getProcedureName());
                                node.setDisProcedureName(procMdl.getProcedureName().replace(".dat", ""));
                                node.setDropProcedure(procMdl.getDropProcedure());
                                node.setCreateProcedure(procMdl.getCreateProcedure());
                                node.setIsChecked("1");
                                lstTemp.add(node);
                            }
                            status = importProcedure(lstTemp);
                            if (status > rtnStatus) {
                                rtnStatus = status;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            if (ois != null) {
                                ois.close();
                                ois = null;
                            }
                            if (fis != null) {
                                fis.close();
                                fis = null;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                throw new Exception(e);
            } catch (Exception ex) {
            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
                if (uploadedDir != null) {
                    uploadedDir = null;
                }
                if (refreshDataStructure != null) {
                    refreshDataStructure.close();
                }
            } catch (Exception e) {
            }
        }
        return importStatus;
    }

    public int importProcedure(List<GrdProcedure_Mdl> lstProcedure) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        GrdProcedure_Mdl procMdl;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            spBegin = new DmSavePoint(cnTemp);
            for (int i = 0; i < lstProcedure.size(); i++) {
                procMdl = lstProcedure.get(i);
                if (!DmShared.xNull(procMdl.getProcedureName()).equalsIgnoreCase("")) {
                    sqlQuery = DmShared.xNull(procMdl.getDropProcedure());
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = DmShared.xNull(procMdl.getCreateProcedure());
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            spBegin.commit();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {

                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                refreshDataStructure.writeLine(e.getMessage());
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }
}
