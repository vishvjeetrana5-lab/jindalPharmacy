/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.radiology;

import aarogya.projectMenu.radiologyDoc.jvm.RadiologyDocumentUploadRequest_Mdl;
import aarogya.projectMenu.radiologyDoc.jvm.RadiologyDocumentRequest_Mdl;
import aarogya.projectMenu.servlet.shared.RadiologyDocumentsUpload_SrvAsync;
import com.google.gson.reflect.TypeToken;
import dataman.dmPlugin.CFunctions;
import dataman.stPlugin.STFunctions;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 *
 * @author abhishek
 */
public class RadiologyReportUploader implements Runnable {

    private RadiologyDocumentRequest_Mdl requestModel;
    private String userAgent;
    private File uploadDocumentFile;

    public RadiologyReportUploader(RadiologyDocumentRequest_Mdl requestModel, String userAgent, File uploadableFile) {
        this.requestModel = requestModel;
        this.userAgent = userAgent;
        this.uploadDocumentFile = uploadableFile;
    }

    public RadiologyDocumentUploadRequest_Mdl runAfterProcess() {
        HttpURLConnection httpConnection = null;
        RadiologyDocumentUploadRequest_Mdl recUploadModel = null;
        try {
            RadiologyDocumentUploadRequest_Mdl uploadRequest = new RadiologyDocumentUploadRequest_Mdl();
            uploadRequest.requestModel = requestModel;
            uploadRequest.documentContent = Base64.getEncoder().encodeToString(Files.readAllBytes(uploadDocumentFile.toPath()));

            String jsonString = CFunctions.getJsonFromObject(uploadRequest, new TypeToken<RadiologyDocumentUploadRequest_Mdl>() {
            });

            String urlToHit = CFunctions.getServerApplicationUrl() + "/" + RadiologyDocumentsUpload_SrvAsync.class.getName().trim().replace(".", "/") + "?u=" + requestModel.clientSessionId;

            URL url = new URL(urlToHit);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("user-agent", userAgent);

            httpConnection.getOutputStream().write(jsonString.getBytes("UTF-8"));

            int rCode = httpConnection.getResponseCode();

            if (rCode != 200) {
                throw new Exception("Server had some exception while processing upload...");
            }

            StringBuilder sb = new StringBuilder();
            byte[] BUFFEREDBYTES = new byte[1024 * 024];
            InputStream ins = httpConnection.getInputStream();
            int readLen = ins.read(BUFFEREDBYTES);
            try {
                while (readLen != -1) {
                    sb.append(new String(BUFFEREDBYTES, 0, readLen, "UTF-8"));
                    readLen = ins.read(BUFFEREDBYTES);
                }
            } finally {
                if (ins != null) {
                    ins.close();
                }
            }

            recUploadModel = STFunctions.getObjectFromJson(sb.toString(), new TypeToken<RadiologyDocumentUploadRequest_Mdl>() {
            });

            STFunctions.removeRadiologyReportProcess(requestModel);
            if (uploadDocumentFile.exists()) {
                uploadDocumentFile.delete();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                    httpConnection = null;
                }
            } catch (Exception ex) {
            } finally {
            }
        }
        return recUploadModel;
    }

    @Override
    public void run() {
        runAfterProcess();
    }

}
