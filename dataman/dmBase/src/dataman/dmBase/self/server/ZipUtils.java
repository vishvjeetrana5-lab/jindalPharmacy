package dataman.dmBase.self.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private List<String> fileList;
    private String outputZipFile;
    private String sourceFolder;

    public ZipUtils(String sourceFolder, String outputZipFile) {
        fileList = new ArrayList< String>();
        this.sourceFolder = sourceFolder.trim();
        this.outputZipFile = outputZipFile.trim();
        generateFileList(new File(sourceFolder));
    }

    public String getOutput_Zip_File() {
        return outputZipFile;
    }

    public String getSourceFolder() {
        return sourceFolder;
    }

    public void zipIt() {        
        byte[] buffer = new byte[1024];
        String source = new File(sourceFolder).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            
            fos = new FileOutputStream(outputZipFile);
            zos = new ZipOutputStream(fos);

//            System.out.println("Output to Zip : " + outputZipFile);
            FileInputStream in = null;

            for (String file : this.fileList) {
                System.out.println("File Added : " + file);
                //ZipEntry ze = new ZipEntry(source + File.separator + file);
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(sourceFolder + File.separator + file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
//            System.out.println("Folder successfully compressed");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateFileList(File node) {
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(sourceFolder.length() + 1, file.length());
    }
}
