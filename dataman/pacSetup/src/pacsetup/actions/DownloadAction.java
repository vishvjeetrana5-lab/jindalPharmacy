package pacsetup.actions;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import pacsetup.commons.Functions;
import pacsetup.forms.AbstractCommand;

public class DownloadAction extends AbstractAction {

    private String downloadedFileName;
    private String executingDirectory;
    private Map<String, String> additionalReqParams = new HashMap<>();

    public DownloadAction(AbstractCommand command) throws Exception {
        super(command);
        executingDirectory = Functions.getInstallationDirectory();
    }

    @Override
    public Map<String, String> getPostExecutionParams() {
        Map<String, String> params = new HashMap<>();
        params.put("downloadedfilename", getDownloadedFullFilePath());
        return params;
    }

    public Map<String, String> getAdditionalReqParameters() {
        return this.additionalReqParams;
    }

    private String getDownloadedFullFilePath() {
        return executingDirectory + File.separatorChar + downloadedFileName;
    }

    @Override
    public void run() {
        try {
            this.setCurrentStatus(EnumStatus.Starting);

            this.downloadedFileName = UUID.randomUUID().toString() + ".zip";

            String serverUrl = getParentCommand().getUrlString();

            URL url = null;
            FileOutputStream fos = null;
            byte[] BUFFER = new byte[1024 * 1024];
            try {
                url = new URL(serverUrl);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestMethod("POST");
                httpConnection.setDoOutput(true);
                httpConnection.setInstanceFollowRedirects(false);

                String urlParams = "cmd=" + getParentCommand().getCommand();
                Iterator<String> iterator = additionalReqParams.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = additionalReqParams.get(key);
                    urlParams += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                }

                byte[] urlBytes = urlParams.getBytes(StandardCharsets.UTF_8);
                httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpConnection.setRequestProperty("charset", "utf-8");
                httpConnection.setRequestProperty("Content-Length", String.valueOf(urlBytes.length));

                DataOutputStream dos = new DataOutputStream(httpConnection.getOutputStream());
                dos.write(urlBytes);
                dos.flush();
                dos.close();

                int resCode = httpConnection.getResponseCode();

                this.setCurrentStatus(EnumStatus.Executing);

                fos = new FileOutputStream(getDownloadedFullFilePath());
                InputStream ins = httpConnection.getInputStream();
                int rLen = ins.read(BUFFER);
                while (rLen != -1) {
                    fos.write(BUFFER, 0, rLen);
                    rLen = ins.read(BUFFER);
                }

            } catch (Exception ex) {
                throw ex;
            } finally {
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            }

            this.setCurrentStatus(EnumStatus.Succeeded);

        } catch (Exception ex) {
            ex.printStackTrace();
            this.setCurrentStatus(EnumStatus.Failed);
        }
    }

    @Override
    public String toString() {
        return "Download...";
    }
    
    

}
