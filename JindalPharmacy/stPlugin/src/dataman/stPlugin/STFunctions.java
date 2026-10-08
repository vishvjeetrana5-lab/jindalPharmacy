/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.stPlugin;

import aarogya.projectMenu.mi.jvm.MiPersistenceModel;
import aarogya.projectMenu.radiologyDoc.jvm.RadiologyDocumentRequest_Mdl;
import dataman.dmPlugin.CFunctions;
import dataman.mi.routing.MiPersistanceService;
import dataman.miToolkit.MiToolkit;
import dataman.radiology.DmProcess;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author abhishek
 */
public class STFunctions extends CFunctions {

    private static Hashtable<RadiologyDocumentRequest_Mdl, DmProcess> radiologyReportsProcesses = new Hashtable<>();

    public static DmProcess putRadiologyReportProcess(RadiologyDocumentRequest_Mdl requestModel) {
        DmProcess appProcess = null;
        try {
            validateRadiologyRequestModel(requestModel);
            if (!radiologyReportsProcesses.containsKey(requestModel)) {
                Process process = null;
                String processApplicationName = "lowriter";
                if (System.getProperty("os.name", "").toLowerCase().contains("windows")) {
//                    processApplicationName = "soffice --writer";
                    processApplicationName = "\"start winword \"\"\"" + requestModel.physicalFilePath + "\"\"\"";
//                    process = new ProcessBuilder("cmd.exe", "/K", processApplicationName + " \"" + requestModel.physicalFilePath + "\"").start();
                    process = new ProcessBuilder("cmd.exe", "/K", processApplicationName).start();
                } else {
                    process = new ProcessBuilder(processApplicationName, requestModel.physicalFilePath).start();
                }

                appProcess = new DmProcess(process);
                radiologyReportsProcesses.put(requestModel, appProcess);

            } else {
                appProcess = getRadiologyReportProcess(requestModel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return appProcess;
    }

    private static void validateRadiologyRequestModel(RadiologyDocumentRequest_Mdl requestModel) throws Exception {
        if (MiToolkit.isNullOrEmpty(requestModel.clientSessionId)) {
            throw new Exception("Client Session Id not found on request model.");
        }
        if (MiToolkit.isNullOrEmpty(requestModel.headerDocumentId)) {
            throw new Exception("Header Document Id not found on request model.");
        }
        if (MiToolkit.isNullOrEmpty(requestModel.contentDocumentId)) {
            throw new Exception("Content Document Id not found on request model.");
        }
        if (MiToolkit.isNullOrEmpty(requestModel.headerVoucherLineItemSerialNo)) {
            throw new Exception("Header Voucher Line Item serial no not found on request model.");
        }
        if (MiToolkit.isNullOrEmpty(requestModel.contentVoucherLineItemSerialNo)) {
            throw new Exception("Content Voucher Line Item serial no not found on request model.");
        }
        if (MiToolkit.isNullOrEmpty(requestModel.virtualFileId)) {
            throw new Exception("Virtual File Id not found on request model.");
        }
    }

    public static RadiologyDocumentRequest_Mdl getStoredRadiologyRequestModel(RadiologyDocumentRequest_Mdl requestModel) {
        RadiologyDocumentRequest_Mdl storedModel = null;
        try {
            validateRadiologyRequestModel(requestModel);
            Iterator<RadiologyDocumentRequest_Mdl> keyIterator = radiologyReportsProcesses.keySet().iterator();
            while (keyIterator.hasNext()) {
                RadiologyDocumentRequest_Mdl tempModel = keyIterator.next();
                if (tempModel.equals(requestModel)) {
                    storedModel = tempModel;
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return storedModel;
    }

    public static DmProcess getRadiologyReportProcess(RadiologyDocumentRequest_Mdl requestModel) {
        DmProcess appProcess = null;
        try {
            if (radiologyReportsProcesses.containsKey(requestModel)) {
                appProcess = radiologyReportsProcesses.get(requestModel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return appProcess;
    }

    public static DmProcess removeRadiologyReportProcess(RadiologyDocumentRequest_Mdl requestModel) {
        DmProcess appProcess = null;
        try {
            appProcess = getRadiologyReportProcess(requestModel);
            if (appProcess != null) {
                appProcess.killAllChildProcesses(appProcess.getProcessIds());
                File savedFile = new File(requestModel.temporaryHeaderDownloadedFilePath);
                savedFile.delete();
                savedFile = new File(requestModel.temporaryContentDownloadedFilePath);
                savedFile.delete();
                savedFile = new File(requestModel.physicalFilePath);
                savedFile.delete();
                radiologyReportsProcesses.remove(requestModel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return appProcess;
    }

    public static void removeAndKillAllRadiologyReportsProcesses(RadiologyDocumentRequest_Mdl requestModel) {
        try {
            Iterator<RadiologyDocumentRequest_Mdl> keyIterator = radiologyReportsProcesses.keySet().iterator();
            while (keyIterator.hasNext()) {
                RadiologyDocumentRequest_Mdl storedModel = keyIterator.next();
                if ((MiToolkit.nullToEmpty(requestModel.clientSessionId).
                        equalsIgnoreCase(MiToolkit.nullToEmpty(storedModel.clientSessionId)))
                        && (MiToolkit.nullToEmpty(requestModel.virtualFileId).equalsIgnoreCase(storedModel.virtualFileId))) {
                    DmProcess appProcess = getRadiologyReportProcess(storedModel);
                    boolean shouldProcessRemove = false;
                    try {
                        appProcess.killAllChildProcesses(appProcess.getProcessIds());
                        File savedFile = new File(storedModel.temporaryHeaderDownloadedFilePath);
                        savedFile.delete();
                        savedFile = new File(storedModel.temporaryContentDownloadedFilePath);
                        savedFile.delete();
                        savedFile = new File(storedModel.physicalFilePath);
                        savedFile.delete();
                        shouldProcessRemove = true;
                    } catch (Exception ex) {
                        shouldProcessRemove = false;
                    } finally {
                        if (shouldProcessRemove) {
                            radiologyReportsProcesses.remove(storedModel);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void transmitPersistanceModelsToServer(List<MiPersistenceModel> listPersistanceModels) {
        ExecutorService executors = Executors.newSingleThreadExecutor();
        try {
            MiPersistanceService service = new MiPersistanceService(listPersistanceModels);

            Future future = executors.submit(service);
            future.get(1, TimeUnit.MINUTES);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            executors.shutdown();
        }
    }

//    
//
//    public static void localSettingsReLoaded(JVMLocalSetting_Mdl localSettings) {
//        System.out.println("Called stlocalsettings...");
//    }
}
