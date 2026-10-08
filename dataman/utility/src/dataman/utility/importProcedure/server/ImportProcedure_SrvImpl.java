package dataman.utility.importProcedure.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.utility.exportProcedure.server.Procedure_Mdl;
import dataman.utility.importProcedure.shared.ImportProcedure_Mdl;
import dataman.utility.importProcedure.shared.ImportProcedure_Srv;
import dataman.utility.importProcedure.shared.GrdProcedure_Mdl;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ImportProcedure_SrvImpl extends DmRSSPrivate implements ImportProcedure_Srv {

    @Override
    public DmResultPageing<List<GrdProcedure_Mdl>> getProcedureList(String filePath, String fileName) {
        DmResultPageing<List<GrdProcedure_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdProcedure_Mdl> lstTemp = new ArrayList<>();
        GrdProcedure_Mdl node = null;
        Connection cnTemp = null;
        File sessionDir;

        try {
            if (filePath.equalsIgnoreCase("") && fileName.equalsIgnoreCase("")) {
                throw new DmException(0, "Please give the file path.");
            }
            String readDirectoryPath = filePath + "\\" + fileName;

            sessionDir = new File(readDirectoryPath);
            if (sessionDir.isFile()) {
                FileInputStream fis = null;
                ObjectInputStream ois = null;
                try {
                    fis = new FileInputStream(readDirectoryPath);
                    ois = new ObjectInputStream(fis);
                    List<Procedure_Mdl> model = (List<Procedure_Mdl>) ois.readObject();
                    for (Procedure_Mdl model1 : model) {
                        node = new GrdProcedure_Mdl();
                        node.setProcedureName(model1.getProcedureName());
                        node.setDisProcedureName(model1.getProcedureName().replace(".dat", ""));
                        node.setDropProcedure(model1.getDropProcedure());
                        node.setCreateProcedure(model1.getCreateProcedure());
                        node.setIsChecked("1");
                        lstTemp.add(node);
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

            } else {
                throw new DmException(0, "Please give the appropriate file path.");
            }
            rstTemp.setData(lstTemp);
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<String> importProcedure(ImportProcedure_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            for (int i = 0; i < mrTemp.getGrdReport_Mdl().size(); i++) {
                if (!DmShared.xNull(mrTemp.getGrdReport_Mdl().get(i).getProcedureName()).equalsIgnoreCase("")) {

                    sqlQuery = DmShared.xNull(mrTemp.getGrdReport_Mdl().get(i).getDropProcedure());
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = DmShared.xNull(mrTemp.getGrdReport_Mdl().get(i).getCreateProcedure());
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            spBegin.commit();
            dmrTemp.setData("");
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return dmrTemp;
    }

}
