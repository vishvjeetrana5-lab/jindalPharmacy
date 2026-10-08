package pacsetup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import pacsetup.commons.Functions;
import pacsetup.forms.AbstractCommand;

public class InstallFontsAction extends AbstractAction {

    public InstallFontsAction(AbstractCommand command) {
        super(command);
    }

    public void setFontsFullPath(String fontsFullPath) {
        getPreExecutionParams().put("fontsfullpath", fontsFullPath);
    }

    public String getFontsFileFullPath() {
        return getPreExecutionParams().get("fontsfullpath");
    }

    @Override
    public void run() {
        EnumStatus status = EnumStatus.Starting;
        try {
            status = EnumStatus.Starting;
            if (Functions.getSystemProperties().getOS_Name().toLowerCase().startsWith("windows".toLowerCase())) {
                installFontsOnWindows();
            } else if (Functions.getSystemProperties().getOS_Name().toLowerCase().startsWith("linux".toLowerCase())) {
                installFontsOnLinux();
            }
            status = EnumStatus.Succeeded;
        } catch (Exception ex) {
            ex.printStackTrace();
            status = EnumStatus.Failed;
        } finally {
            setCurrentStatus(status);
        }
    }

    private void installFontsOnLinux() throws Exception {
        copyFontsToDirectory(createFontsDirectory());
        refreshLinuxFontsCache();
    }

    private void refreshLinuxFontsCache() throws Exception {
        String commandString = "fc-cache -f -v";
        CommandLine commandLine = CommandLine.parse(commandString);
        new DefaultExecutor().execute(commandLine);
    }

    private void copyFontsToDirectory(String userFontsDirectory) throws Exception {
        List<String> listFontFiles = getAllFontFilesFromDirectory(getFontsFileFullPath());
        for (int i = 0; i < listFontFiles.size(); i++) {
            copyFontFileToDirectory(listFontFiles.get(i), userFontsDirectory);
        }
    }

    private List<String> getAllFontFilesFromDirectory(String direcotoryPathContainingFonts) throws Exception {
        List<String> lstFontFiles = new ArrayList<>();
        File tempFile = new File(direcotoryPathContainingFonts);
        String[] lstFiles = tempFile.list();
        for (int i = 0; i < lstFiles.length; i++) {
            File lstFile = new File(direcotoryPathContainingFonts + File.separatorChar + lstFiles[i]);
            if (lstFile.isFile()) {
                if (lstFile.getName().toLowerCase().endsWith(".ttf") || lstFile.getName().toLowerCase().endsWith(".otf")) {
                    lstFontFiles.add(lstFile.getAbsolutePath());
                }
            } else if (lstFile.isDirectory()) {
                List<String> tempFontFiles = getAllFontFilesFromDirectory(lstFile.getAbsolutePath());
                if (tempFontFiles.size() > 0) {
                    lstFontFiles.addAll(tempFontFiles);
                }
            }
        }
        return lstFontFiles;
    }

    private void copyFontFileToDirectory(String fontFilePath, String userFontsDirectory) throws Exception {
        File sourceFile = new File(fontFilePath);
        File destFile = new File(userFontsDirectory + File.separatorChar + sourceFile.getName());
        byte[] BUFFER = new byte[1024 * 1024];
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            if (!destFile.exists()) {
                fis = new FileInputStream(sourceFile);
                fos = new FileOutputStream(destFile);
                int rLen = fis.read(BUFFER);
                while (rLen != -1) {
                    fos.write(BUFFER, 0, rLen);
                    rLen = fis.read(BUFFER);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (fis != null) {
                fis.close();
                fis = null;
            }
            if (fos != null) {
                fos.close();
                fos = null;
            }
        }
    }

    private void installFontsOnWindows() throws Exception {
        runScriptFile(createScriptFile());
    }

    private String createScriptFile() throws Exception {
        String scriptFileName = "installfonts.vbs";
        String scriptFullFilePath = Functions.getInstallationDirectory() + File.separatorChar + scriptFileName;
        StringBuilder buffer = new StringBuilder();
        buffer.append("Option Explicit\n");
        buffer.append("' http://www.cloudtec.ch 2011\n");
        buffer.append("Dim objShell, objFSO, wshShell\n");
        buffer.append("Dim strFontSourcePath, objFolder, objFont, objNameSpace, objFile\n");
        buffer.append("Set objShell = CreateObject(\"Shell.Application\")\n");
        buffer.append("Set wshShell = CreateObject(\"WScript.Shell\")\n");
        buffer.append("Set objFSO = createobject(\"Scripting.Filesystemobject\")\n");
        buffer.append("Wscript.Echo \"--------------------------------------\"\n");
        buffer.append("Wscript.Echo \" Install Fonts \"\n");
        buffer.append("Wscript.Echo \"--------------------------------------\"\n");
        buffer.append("Wscript.Echo \" \"\n");
        buffer.append("strFontSourcePath = \"" + getFontsFileFullPath() + "\"\n");
        buffer.append("If objFSO.FolderExists(strFontSourcePath) Then\n");
        buffer.append("    Set objNameSpace = objShell.Namespace(strFontSourcePath)\n");
        buffer.append("    Set objFolder = objFSO.getFolder(strFontSourcePath)\n\n\n");
        buffer.append("    For Each objFile In objFolder.files\n");
        buffer.append("       If LCase(right(objFile,4)) = \".ttf\" OR LCase(right(objFile,4)) = \".otf\" Then\n");
        buffer.append("         If objFSO.FileExists(\"C:\\Windows\\Fonts\\\" & objFile.Name) Then\n");
        buffer.append("           Wscript.Echo \"Font already installed: \" & objFile.Name\n");
        buffer.append("         Else\n");
        buffer.append("           Set objFont = objNameSpace.ParseName(objFile.Name)\n");
        buffer.append("           objFont.InvokeVerb(\"Install\")\n");
        buffer.append("           Wscript.Echo \"Installed Font: \" & objFile.Name\n");
        buffer.append("           Set objFont = Nothing\n");
        buffer.append("         End If\n");
        buffer.append("       End If\n");
        buffer.append("    Next\n");
        buffer.append("Else\n");
        buffer.append("    Wscript.Echo \"Font Source Path does not exists\"\n");
        buffer.append("End If\n");
        buffer.append("Set wshShell = Nothing\n");
        buffer.append("Set objShell = Nothing\n");
        buffer.append("Set objFSO = Nothing\n");
        FileWriter writer = null;
        try {
            writer = new FileWriter(scriptFullFilePath);
            writer.write(buffer.toString());
        } catch (Exception ex) {
            Functions.writeException(ex);
            throw ex;
        } finally {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        }
        return scriptFullFilePath;
    }

    private void runScriptFile(String scriptFilePath) throws Exception {
        String commandString = "cscript " + scriptFilePath;
        CommandLine commandLine = CommandLine.parse(commandString);
        DefaultExecutor executor = new DefaultExecutor();
        executor.execute(commandLine);

        Functions.deleteFile(scriptFilePath);
        Functions.deleteFile(getFontsFileFullPath());
    }

    private String createFontsDirectory() throws Exception {
        String homeDirectoryPath = Functions.getSystemProperties().getUser_Home();
        String fontsDirectoryPath = homeDirectoryPath + File.separatorChar + ".fonts";
        File fontsDirectoryFile = new File(fontsDirectoryPath);        
        if (!fontsDirectoryFile.exists()) {
            boolean bool = fontsDirectoryFile.mkdirs();
            if (bool) {
                System.out.println("Successfully created fonts directory..." + fontsDirectoryPath);
            } else {
                throw new Exception("Unable to create fonts directory...");
            }
        }
        return fontsDirectoryPath;
    }

    @Override
    public Map<String, String> getPostExecutionParams() {
        return new HashMap<>();
    }

    @Override
    public String toString() {
        return "Installing Fonts...";
    }

}
