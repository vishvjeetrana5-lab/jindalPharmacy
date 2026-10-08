package dataman.dmBase.self.server;

import java.io.File;
import java.io.FileWriter;

public class DmHandelFile {

    private File file;
    FileWriter writer;
    
    public DmHandelFile(String filePath) throws Exception {
        startUp(filePath, false);
    }

    public DmHandelFile(String filePath, boolean createWhenNotExists) throws Exception {
        startUp(filePath, createWhenNotExists);
    }

    private void startUp(String filePath, boolean createWhenNotExists) throws Exception {
        file = new File(filePath);

        if (createWhenNotExists) {
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        
        writer = new FileWriter(file, true);
    }

    public void writeLine(String content) throws Exception {
        writer.write(content);
        writer.write(System.lineSeparator());
    }

    public void close() throws Exception {
        writer.close();
        writer = null;
        file = null;
    }

}
