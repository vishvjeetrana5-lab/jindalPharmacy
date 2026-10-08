package pacsetup.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import mslinks.ShellLink;
import pacsetup.commons.Functions;
import pacsetup.forms.AbstractCommand;

public class InstallPACAction extends AbstractAction {

    public InstallPACAction(AbstractCommand command) {
        super(command);
    }

    public String getDownloadedPACPath() {
        return getPreExecutionParams().get("downloadedpacpath");
    }

    public void setDownloadedPACPath(String downloadedPACPath) {
        getPreExecutionParams().put("downloadedpacpath", downloadedPACPath);
    }

    @Override
    public Map<String, String> getPostExecutionParams() {
        return new HashMap<>();
    }

    @Override
    public void run() {
        EnumStatus status = EnumStatus.Starting;
        try {
            if (Functions.getSystemProperties().getOS_Name().toLowerCase().startsWith("windows")) {
                String scriptFilePath = installPacOnWindows();
                runPac(scriptFilePath);
                status = EnumStatus.Succeeded;
            } else if (Functions.getSystemProperties().getOS_Name().toLowerCase().startsWith("linux")) {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            status = EnumStatus.Failed;
        } finally {
            setCurrentStatus(status);
        }
    }

    private void runPac(String scriptFilePath) throws Exception {
        Process p = null;
        try {
            File linkFilePath = new File(scriptFilePath);
            p = Runtime.getRuntime().exec("cmd /c start " + linkFilePath.getName(), null, linkFilePath.getParentFile());
            p.waitFor();
        } finally {

            p.destroy();
            p = null;
        }
    }

    private String installPacOnWindows() throws Exception {
        String userStartupPath = findUserStartupPath(createScriptUserPath());
        userStartupPath = userStartupPath + File.separatorChar + "STPlugin.lnk";
        String jarFilePath = getDownloadedPACPath() + File.separatorChar + "stPlugin.jar";
        ShellLink.createLink(jarFilePath).saveTo(userStartupPath);
        return userStartupPath;
    }

    private String findUserStartupPath(String userPathScriptFilePath) throws Exception {
        Process p = Runtime.getRuntime().exec("cscript " + userPathScriptFilePath);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String result = "";
        while ((line = input.readLine()) != null) {
            result += line + "\n";
        }
        input.close();

        String[] results = result.split("\\n");
        if (results.length > 0) {
            result = results[results.length - 1];
        }

        Functions.deleteFile(userPathScriptFilePath);

        return result;
    }

    private String createScriptUserPath() throws Exception {
        String homeDirectoryPath = Functions.getInstallationDirectory();
        String scriptDirectoryPath = homeDirectoryPath + File.separatorChar + "startup.vbs";
        StringBuilder buffer = new StringBuilder();
        buffer.append("Option Explicit\n");
        buffer.append("Dim objShell\n");
        buffer.append("set objShell = CreateObject(\"WScript.Shell\")\n");
        buffer.append("WScript.Echo objShell.SpecialFolders(\"Startup\")\n");
        FileWriter writer = new FileWriter(scriptDirectoryPath);
        writer.write(buffer.toString());
        writer.close();
        writer = null;
        return scriptDirectoryPath;
    }

    private void intallPacOnLinux() throws Exception {

    }

    @Override
    public String toString() {
        return "PAC...";
    }

}
