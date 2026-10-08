/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.radiology;

import aarogya.projectMenu.radiologyDoc.jvm.RadiologyDocumentRequest_Mdl;
import aarogya.projectMenu.servlet.shared.RadiologyDocumentsRequest_SrvAsync;
import com.google.gson.reflect.TypeToken;
import dataman.dmPlugin.CFunctions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author abhishek
 */
public class RadiologyReportDownloader implements Runnable {

    private RadiologyDocumentRequest_Mdl requestModel;
    private String userAgent;
    private CountDownLatch countDownLatch;
    private boolean downloadForHeader;

    public RadiologyReportDownloader(RadiologyDocumentRequest_Mdl requestModel, boolean downloadForHeader, String userAgent, CountDownLatch countDownLatch) {
        this.requestModel = requestModel;
        this.userAgent = userAgent;
        this.countDownLatch = countDownLatch;
        this.downloadForHeader = downloadForHeader;
    }

    @Override
    public void run() {
        HttpURLConnection httpConnection = null;
        FileOutputStream fos = null;
        try {
            requestModel.calledForHeader = downloadForHeader;
            String jsonString = CFunctions.getJsonFromObject(requestModel, new TypeToken<RadiologyDocumentRequest_Mdl>() {
            });

            String urlToHit = CFunctions.getServerApplicationUrl() + "/" + RadiologyDocumentsRequest_SrvAsync.class.getName().trim().replace(".", "/") + "?u=" + requestModel.clientSessionId;

            URL url = new URL(urlToHit);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("user-agent", userAgent);

            httpConnection.getOutputStream().write(jsonString.getBytes("UTF-8"));

            int rCode = httpConnection.getResponseCode();

            if (rCode != 200) {
                throw new Exception("Server had some exception while processing machine data...");
            }

            InputStream ins = httpConnection.getInputStream();
            File reportFile = null;
            if (downloadForHeader) {
                reportFile = File.createTempFile("rpt", ".dotx");
            } else {
                reportFile = File.createTempFile("rpt", ".docx");
            }

            fos = new FileOutputStream(reportFile);

            byte[] BUFFEREDBYTES = new byte[1024 * 1024];

            int readLen = ins.read(BUFFEREDBYTES);
            while (readLen != -1) {
                fos.write(BUFFEREDBYTES, 0, readLen);
                readLen = ins.read(BUFFEREDBYTES);
            }

            if (downloadForHeader) {
                requestModel.temporaryHeaderDownloadedFilePath = reportFile.getAbsolutePath();
            } else {
                requestModel.temporaryContentDownloadedFilePath = reportFile.getAbsolutePath();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
                if (httpConnection != null) {
                    httpConnection.disconnect();
                    httpConnection = null;
                }
            } catch (Exception ex) {
            } finally {
                this.countDownLatch.countDown();
            }
        }
    }

}
