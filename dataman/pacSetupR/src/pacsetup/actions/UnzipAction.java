package pacsetup.actions;

import pacsetup.actions.AbstractAction;
import pacsetup.actions.EnumStatus;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import pacsetup.commons.Functions;
import pacsetup.forms.AbstractCommand;

public class UnzipAction extends AbstractAction {

    private String extractedFileName;

    public UnzipAction(AbstractCommand command) {
        super(command);

    }

    public void setZippedFilePath(String zippedFilePath) {
        getPreExecutionParams().put("zippedfilepath", zippedFilePath);
    }

    public String getZippedFilePath() {
        return getPreExecutionParams().get("zippedfilepath");
    }

    @Override
    public Map<String, String> getPostExecutionParams() {
        Map<String, String> params = new HashMap<>();
        params.put("extractedfilename", extractedFileName);
        return params;
    }

    @Override
    public void run() {
        try {
            this.setCurrentStatus(EnumStatus.Pending);

            extractedFileName = "PACPlugin_" + new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss").format(new Date());

            extractedFileName = Functions.getInstallationDirectory() + File.separatorChar + extractedFileName;

            this.setCurrentStatus(EnumStatus.Executing);

            extractFile();

            Functions.deleteFile(getPreExecutionParams().get("zippedfilepath"));

            this.setCurrentStatus(EnumStatus.Succeeded);

        } catch (Exception ex) {
            ex.printStackTrace();
            this.setCurrentStatus(EnumStatus.Failed);
        }
    }

    private void extractFile() throws Exception {
        String zippedFilePath = getPreExecutionParams().get("zippedfilepath");
        File destDir = new File(extractedFileName);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zippedFilePath));
        ZipEntry entry = zipIn.getNextEntry();

        while (entry != null) {
            String filePath = extractedFileName + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                extractFile(zipIn, filePath);
            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws Exception {
        File tempFile = new File(filePath);
        tempFile.getParentFile().mkdirs();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[1024 * 1024];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    @Override
    public String toString() {
        return "Un-Zipping...";
    }

}
