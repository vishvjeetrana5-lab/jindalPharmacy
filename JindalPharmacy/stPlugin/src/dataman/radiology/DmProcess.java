/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.radiology;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abhishek
 */
public class DmProcess {

    private Process applicationProcess;

    public DmProcess(Process applicationProcess) {
        this.applicationProcess = applicationProcess;
    }

    private static int getProcessIdOfProcess(Process appProcess) throws Exception {
        int processId = -1;
        if (appProcess.getClass().getName().equals("java.lang.UNIXProcess")) {
            Field pidField = appProcess.getClass().getDeclaredField("pid");
            pidField.setAccessible(true);
            processId = pidField.getInt(appProcess);
        } else if (appProcess.getClass().getName().equals("java.lang.Win32Process")
                || appProcess.getClass().getName().equals("java.lang.ProcessImpl")) {
            Field f = appProcess.getClass().getDeclaredField("handle");
            f.setAccessible(true);
            long handl = f.getLong(appProcess);

            Kernel32 kernel = Kernel32.INSTANCE;
//                W32API.HANDLE handle = new W32API.HANDLE();
            WinNT.HANDLE handle = new WinNT.HANDLE();
            handle.setPointer(Pointer.createConstant(handl));
            processId = kernel.GetProcessId(handle);
        }
        return processId;
    }

    private void getChildProcesses(int mainProcessId, List<Integer> listChildProcessIds) throws Exception {
        try {
            Process tempProcess = null;
            if (System.getProperties().getProperty("os.name").toLowerCase().contains("windows")) {
                tempProcess = Runtime.getRuntime().exec("wmic process where ParentProcessId=" + mainProcessId + " get ProcessId");
            } else {
                tempProcess = Runtime.getRuntime().exec("pgrep -P " + mainProcessId);
            }
            tempProcess.waitFor();
            BufferedReader bf = new BufferedReader(new InputStreamReader(tempProcess.getInputStream()));
            String processIdLine = bf.readLine();
            while (processIdLine != null) {
                try {
                    listChildProcessIds.add(Integer.parseInt(processIdLine.trim()));
                } catch (Exception ex) {

                } finally {
                    processIdLine = bf.readLine();
                }
            }
        } finally {

        }
    }

    public List<Integer> getProcessIds() throws Exception {
        List<Integer> listChildProcessIds = new ArrayList<>();
        try {
            int mainProcessId = getProcessIdOfProcess(applicationProcess);
            getChildProcesses(mainProcessId, listChildProcessIds);
            listChildProcessIds.add(mainProcessId);
        } finally {

        }
        return listChildProcessIds;
    }

    public void killAllChildProcesses(List<Integer> listChildProcessesId) throws Exception {
        try {
            if (System.getProperties().getProperty("os.name").toLowerCase().contains("windows")) {
                for (int i = 0; i < listChildProcessesId.size(); i++) {
                    Process tempProcess = Runtime.getRuntime().exec("taskkill /PID " + listChildProcessesId.get(i).toString());
                    tempProcess.waitFor();
                    //tempProcess.destroy();
                    tempProcess = null;
                }
            } else {
                for (int i = 0; i < listChildProcessesId.size(); i++) {
                    Process tempProcess = Runtime.getRuntime().exec("pkill -P " + listChildProcessesId.get(i).toString());
                    tempProcess.waitFor();
                    //tempProcess.destroy();
                    tempProcess = null;
                }
            }
        } finally {

        }
    }
}
