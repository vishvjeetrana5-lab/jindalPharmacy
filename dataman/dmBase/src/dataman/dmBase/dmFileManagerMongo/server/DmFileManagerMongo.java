package dataman.dmBase.dmFileManagerMongo.server;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import org.bson.types.ObjectId;

public class DmFileManagerMongo {

    private DB dbSelected;

    public DmFileManagerMongo(DB db) throws Exception {
        this.dbSelected = db;
    }

    public DB getMongoDBConnection() {
        return this.dbSelected;
    }

    public static boolean synchronizeTransactionalWithFileServer() throws Exception {
        boolean bool = false;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String mongoDBName = "", ruleCode = "", sql = "";
        DmFileManagerMongo mngTemp = null;

        try {
            cnTemp = DmBaseServer.getConnection("master");
            sql = "select count(*) as cnt from sysDatabases where name = '" + DmBaseServer.dbCompany + "' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmBaseShared.iNull(rsTemp.getString("cnt")).longValue() > 0) {
                    rsTemp.close();
                    psTemp.close();
                    sql = "select distinct centralFileData_Path from " + DmBaseServer.dbCompany + ".dbo.company";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
                    rsTemp = psTemp.executeQuery();
                    while (rsTemp.next()) {
                        mongoDBName = DmBaseShared.xNull(rsTemp.getString("centralFileData_Path"));
                        if (!DmBaseShared.xNull(mongoDBName).trim().equalsIgnoreCase("")) {
                            mngTemp = DmBaseServer.getConnectionMongo(mongoDBName, true);

                            sql = "select distinct ruleCode from " + DmBaseServer.dbCompany + ".dbo.fileTypeMast";
                            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sql);
                            rsTemp1 = psTemp1.executeQuery();

                            while (rsTemp1.next()) {
                                ruleCode = DmBaseShared.xNull(rsTemp1.getString("ruleCode"));
                                if (!ruleCode.equalsIgnoreCase("")) {
                                    if (!mngTemp.isFolderExists(ruleCode)) {
                                        mngTemp.createFolder(ruleCode);
                                    }
                                }
                            }
                            rsTemp1.close();
                            psTemp1.close();
                        }
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            bool = true;
        } catch (Exception ex) {
            bool = false;
            throw ex;
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }

            if (rsTemp1 != null) {
                rsTemp1.close();
            }

            if (psTemp != null) {
                psTemp.close();
            }

            if (psTemp1 != null) {
                psTemp1.close();
            }
            if (cnTemp != null && !cnTemp.isClosed()) {
                cnTemp.close();
            }
        }
        return bool;
    }

    public boolean createFolder(String folderName) {
        boolean bool = false;
        GridFS gridFS = null;

        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: File database not selected.");
            }

            if (!isFolderExists(folderName)) {
                gridFS = new GridFS(dbSelected, folderName);
            }

            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        }
        return bool;
    }

    public GridFSDBFile getFile(String fileId) throws Exception {
        GridFSDBFile file = null;
        String[] folders;
        String folderName;

        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: File database not selected.");
            }

            folders = this.dbSelected.getCollectionNames().toArray(new String[]{});

            for (int i = 0; i < folders.length; i++) {
                if (folders[i].endsWith(".files")) {
                    folderName = folders[i].trim();
                    folderName = folderName.substring(0, folderName.indexOf("."));
                    file = getFileFromFolder(folderName, fileId);
                    if (file != null) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return file;
    }

    public boolean isFileExists(String fileId) throws Exception {
        boolean bool = false;
        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: File database not selected.");
            }

            String[] folders = this.dbSelected.getCollectionNames().toArray(new String[]{});
            for (int i = 0; i < folders.length; i++) {
                if (folders[i].endsWith(".files")) {
                    String folderName = folders[i].trim();
                    folderName = folderName.substring(0, folderName.indexOf("."));
                    bool = isFileExistsInFolder(folderName, fileId);
                    if (!bool) {
                        throw new Exception("some exception is drawn while checking for exsitance of file.");
                    } else if (bool) {
                        break;
                    }
                }
            }
            bool = true;
        } catch (Exception ex) {
            bool = false;
            throw ex;
        }

        return bool;
    }

    public boolean isFileExistsInFolder(String folderName, String fileId) throws Exception {
        boolean bool = false;
        try {
            if (this.dbSelected == null) {
                throw new Exception("file database not selected.");
            }

            if (!isFolderExists(folderName)) {
                throw new Exception("folder doesn't exist");
            }

            GridFSDBFile file = new GridFS(dbSelected, folderName).findOne(new ObjectId(fileId));
            bool = !(file == null);
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        }
        return bool;
    }

    public GridFSDBFile getFileFromFolder(String folderName, String fileLink) throws Exception {
        GridFSDBFile file = null;

        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: File database not selected.");
            }

            if (!isFolderExists(folderName)) {
                throw new Exception("DmFileManagerMongo: Folder doesn't exist.");
            }

            file = new GridFS(dbSelected, folderName).findOne(new ObjectId(fileLink));
        } catch (Exception ex) {
            file = null;
            ex.printStackTrace();
        }
        return file;
    }

    public boolean renameFolder(String oldFolderName, String newFolderName) throws Exception {
        boolean bool = false;
        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: File database not selected.");
            }

            if (isFolderExists(oldFolderName)) {
                this.dbSelected.getCollection(oldFolderName + ".files").rename(newFolderName + ".files");
                this.dbSelected.getCollection(oldFolderName + ".chunks").rename(newFolderName + ".chunks");
            }

            bool = true;
        } catch (Exception ex) {
            bool = false;
            throw ex;
        }
        return bool;
    }

    public boolean removeFolder(String folderName) throws Exception {
        boolean bool = false;
        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: File database not selected.");
            }

            if (isFolderExists(folderName)) {
                this.dbSelected.getCollection(folderName + ".files").drop();
                this.dbSelected.getCollection(folderName + ".chunks").drop();
            }

            bool = true;
        } catch (Exception ex) {
            bool = false;
            throw ex;
        }
        return bool;
    }

    private boolean isFolderExists(String folderName) throws Exception {
        boolean bool = false;
        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: File database not selected.");
            }

            bool = ((this.dbSelected.collectionExists(folderName + ".files")) && (this.dbSelected.collectionExists(folderName + ".chunks")));
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        }
        return bool;
    }

    public GridFSInputFile createFileOnDBFolder(String folderName) throws Exception {
        GridFSInputFile file = null;
        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: File database not selected.");
            }

            if (!isFolderExists(folderName)) {
                throw new Exception("DmFileManagerMongo: Folder doesn't exist.");
            }

            file = new GridFS(dbSelected, folderName).createFile();
        } catch (Exception ex) {
            file = null;
            throw ex;
        }
        return file;
    }

    public GridFSInputFile createFileOnDBFolder(String folderName, File file) throws Exception {
        GridFSInputFile gridFile = null;
        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: File database not selected.");
            }

            if (!isFolderExists(folderName)) {
                throw new Exception("DmFileManagerMongo: Folder doesn't exists.");
            }

            gridFile = new GridFS(dbSelected, folderName).createFile(file);
        } catch (Exception ex) {
            gridFile = null;
            throw ex;
        }
        return gridFile;
    }

    public GridFSInputFile createFileOnDBFolder(String folderName, InputStream inStream) throws Exception {
        GridFSInputFile gridFile = null;
        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: File database not selected.");
            }

            if (!isFolderExists(folderName)) {
                throw new Exception("DmFileManagerMongo: Folder doesn't exists.");
            }

            gridFile = new GridFS(dbSelected, folderName).createFile(inStream);
        } catch (Exception ex) {
            gridFile = null;
            throw ex;
        }
        return gridFile;
    }

    public GridFSInputFile createFileOnDBFolder(String folderName, String fileName) throws Exception {
        GridFSInputFile gridFile = null;
        try {
            if (this.dbSelected == null) {
                throw new Exception("DmFileManagerMongo: file database not selected.");
            }

            if (!isFolderExists(folderName)) {
                throw new Exception("DmFileManagerMongo: folder doesn't exists.");
            }

            gridFile = new GridFS(dbSelected, fileName).createFile(fileName);
        } catch (Exception ex) {
            gridFile = null;
            throw ex;
        }
        return gridFile;
    }

    public boolean saveFileFromStream(String fileName, GridFSInputFile fileToSave) throws Exception {
        boolean bool = false;
        try {

            if (this.dbSelected == null) {
                throw new Exception("file database not selected.");
            }

            fileToSave.setFilename(fileName);

            fileToSave.getOutputStream().close();

            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
            throw ex;
        }
        return bool;
    }

    public boolean saveFileNormally(String fileName, GridFSInputFile fileToSave) throws Exception {
        boolean bool = false;
        try {

            if (this.dbSelected == null) {
                throw new Exception("file database not selected.");
            }

            fileToSave.setFilename(fileName);

            fileToSave.save();

            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
            throw ex;
        }
        return bool;
    }

    public boolean deleteFile(String folderName, String fileId) throws Exception {
        boolean bool = false;
        try {
            if (this.dbSelected == null) {
                throw new Exception("file database not selected.");
            }

            if (!isFolderExists(folderName)) {
                throw new Exception("folder doesn't exist");
            }

            new GridFS(dbSelected, folderName).remove(new ObjectId(fileId));

            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
            throw ex;
        }
        return bool;
    }

    public boolean deleteFiles(String folderName, List<String> fileIds) throws Exception {
        boolean bool = false;
        try {

            if (this.dbSelected == null) {
                throw new Exception("file database not selected.");
            }

            if (fileIds.isEmpty()) {
                throw new Exception("please provide file ids to delete them.");
            } else if (fileIds.size() > 0) {
                for (String fileId : fileIds) {
                    bool = deleteFile(folderName, fileId);
                    if (!bool) {
                        throw new Exception("unable to delete file from specified folder.");
                    }
                }
            }
        } catch (Exception ex) {
            bool = false;
            throw ex;
        }
        return bool;
    }

    public boolean deleteFiles(String docId, String sNo, String ruleCode, Connection cnTemp) throws Exception {
        boolean bool = false;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {

            if (DmBaseShared.xNull(docId.trim()).equals("")) {
                throw new Exception("DocId not provided to get Uploaded File Link.");
            }

            docId = docId.trim();

            if (DmBaseShared.xNull(sNo.trim()).equals("")) {
                throw new Exception("SNo. not provided to get Uploaded File Link.");
            }

            sNo = sNo.trim();

            if (DmBaseShared.xNull(ruleCode.trim()).equals("")) {
                throw new Exception("RuleCode not provided to get Uploaded File Link.");
            }

            ruleCode = ruleCode.trim();

            if (cnTemp.isClosed()) {
                throw new Exception("Connection is Not found to Update Records.");
            }

            String sql = "SELECT fileLink,docId,sNo,ruleCode FROM LINKFILECOLLECTION WHERE (docId=:docId) and (sNo=:sNo) and (ruleCode=:ruleCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("docId", docId);
            psTemp.setString("sNo", sNo);
            psTemp.setString("ruleCode", ruleCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                String mongoId = DmBaseShared.xNull(rsTemp.getString("fileLink"));

                if (!deleteFile(ruleCode, mongoId)) {
                    throw new Exception("Unable to Delete File Link.");
                }
            }
            rsTemp.close();

            sql = "DELETE FROM LINKFILECOLLECTION WHERE (docId=:docId) and (sNo=:sNo) and (ruleCode=:ruleCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("docId", docId);
            psTemp.setString("sNo", sNo);
            psTemp.setString("ruleCode", ruleCode);
            psTemp.executeUpdate();

            bool = true;
        } catch (Exception ex) {
            bool = false;
            throw ex;
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
                rsTemp = null;
            }
            if (psTemp != null) {
                psTemp.close();
                psTemp = null;
            }
        }
        return bool;
    }

    public String saveFileInMongo(String fileLinkId, String ruleCode, DmFile_Mdl fileModel) throws Exception {
        String mongoId = "";
        FileInputStream fis = null;
        int READ_BUFFER_SIZE = 1024 * 1024 * 1;
        try {

            if (DmBaseShared.xNull(ruleCode.trim()).equals("")) {
                throw new Exception("RuleCode not provided to Upload File.");
            }

            ruleCode = ruleCode.trim();

            if (fileModel == null) {
                throw new Exception("File Model not found to Save Record.");
            }

            if (DmBaseShared.xNull(fileModel.getServerDirectory()).equalsIgnoreCase("")
                    || DmBaseShared.xNull(fileModel.getServerFileName()).equalsIgnoreCase("")) {
                return "";
            }

            File fileToUpload = new File(fileModel.getServerDirectory(), fileModel.getServerFileName());
            if (!fileToUpload.exists()) {
                throw new Exception("File not found to get Uploaded.");
            }

            GridFSInputFile mongoInFile = createFileOnDBFolder(ruleCode);

            if (mongoInFile == null) {
                throw new Exception("Unable to create File On specified Directory.");
            }

            mongoInFile.setFilename(fileModel.getFileName());
            if (!DmBaseShared.xNull(fileLinkId).equalsIgnoreCase("")) {
                mongoInFile.setId(new ObjectId(fileLinkId));
            }

            try {
                fis = new FileInputStream(fileToUpload);
                byte[] byteBuffer = new byte[READ_BUFFER_SIZE];

                long totalFileSize = 0l;

                int readLength = fis.read(byteBuffer);

                while (readLength != -1) {
                    totalFileSize += readLength;
                    if (readLength >= READ_BUFFER_SIZE) {
                        mongoInFile.getOutputStream().write(byteBuffer);
                    } else if (readLength < READ_BUFFER_SIZE) {
                        mongoInFile.getOutputStream().write(byteBuffer, 0, readLength);
                    }
                    readLength = fis.read(byteBuffer);
                }
                mongoInFile.getOutputStream().close();
                mongoId = mongoInFile.getId().toString();

            } catch (Exception ex) {
                throw ex;
            } finally {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            }

        } catch (Exception ex) {
            throw ex;
        }
        return mongoId.trim();
    }

    public String saveFile(String docId, String sNo, String ruleCode, Connection cnTemp, DmFile_Mdl fileModel, String userName) throws Exception {
        String mongoId = "";
        FileInputStream fis = null;
        int READ_BUFFER_SIZE = 1024 * 1024 * 1;
        try {
            if (DmBaseShared.xNull(docId.trim()).equals("")) {
                throw new Exception("DocId not provided to Upload File.");
            }

            docId = docId.trim();

            if (DmBaseShared.xNull(sNo.trim()).equals("")) {
                throw new Exception("SNo. not provided to Upload File.");
            }

            sNo = sNo.trim();

            if (DmBaseShared.xNull(ruleCode.trim()).equals("")) {
                throw new Exception("RuleCode not provided to Upload File.");
            }

            ruleCode = ruleCode.trim();

            if (cnTemp.isClosed()) {
                throw new Exception("Connection is Not found to Update Records.");
            }

            if (fileModel == null) {
                throw new Exception("File Model not found to Save Record.");
            }

            if (DmBaseShared.xNull(fileModel.getServerDirectory()).equalsIgnoreCase("")
                    || DmBaseShared.xNull(fileModel.getServerFileName()).equalsIgnoreCase("")) {
                return "";
            }

            File fileToUpload = new File(fileModel.getServerDirectory(), fileModel.getServerFileName());
            if (!fileToUpload.exists()) {
                throw new Exception("File not found to get Uploaded.");
            }

            GridFSInputFile mongoInFile = createFileOnDBFolder(ruleCode);

            if (mongoInFile == null) {
                throw new Exception("Unable to create File On specified Directory.");
            }

            mongoInFile.setFilename(fileModel.getFileName());

            try {
                fis = new FileInputStream(fileToUpload);
                byte[] byteBuffer = new byte[READ_BUFFER_SIZE];

                long totalFileSize = 0l;

                int readLength = fis.read(byteBuffer);

                while (readLength != -1) {
                    totalFileSize += readLength;
                    if (readLength >= READ_BUFFER_SIZE) {
                        mongoInFile.getOutputStream().write(byteBuffer);
                    } else if (readLength < READ_BUFFER_SIZE) {
                        mongoInFile.getOutputStream().write(byteBuffer, 0, readLength);
                    }
                    readLength = fis.read(byteBuffer);
                }

                if (!saveFileDetail(mongoInFile, cnTemp, docId, sNo, ruleCode, fileModel, totalFileSize, userName)) {
                    throw new Exception("Unable to save File.");
                }

                mongoId = mongoInFile.getId().toString();

            } catch (Exception ex) {
                throw ex;
            } finally {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            }

        } catch (Exception ex) {
            throw ex;
        }
        return mongoId.trim();
    }

    private boolean saveFileDetail(GridFSInputFile mongoInFile, Connection cnTemp, String docId, String sNo, String ruleCode, DmFile_Mdl fileModel, long totalFileSize, String userName) throws Exception {
        boolean bool = false;
        DmPreparedStatement psTemp = null;
        try {
            String sql = "INSERT INTO LINKFILECOLLECTION (fileLink,docId,sNo,ruleCode,fileName,fileSize,u_Name,u_EntDt) VALUES (:fileLink,:docId,:sNo,:ruleCode,:fileName,:fileSize,:userName,:u_EntDt)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("fileLink", mongoInFile.getId().toString());
            psTemp.setString("docId", docId);
            psTemp.setString("sNo", sNo);
            psTemp.setString("ruleCode", ruleCode);
//            psTemp.setString("fileType", "default");
            psTemp.setString("fileName", fileModel.getFileName());
            psTemp.setString("fileSize", DmBaseShared.iNull(totalFileSize).toString());
            psTemp.setString("userName", userName);
            psTemp.setDate("u_EntDt", new java.sql.Date((new java.util.Date()).getTime()));
            psTemp.executeUpdate();

            mongoInFile.getOutputStream().close();

            bool = true;
        } catch (Exception ex) {
            bool = false;
            throw ex;
        } finally {
            if (psTemp != null) {
                psTemp.close();
                psTemp = null;
            }
        }
        return bool;
    }

}
