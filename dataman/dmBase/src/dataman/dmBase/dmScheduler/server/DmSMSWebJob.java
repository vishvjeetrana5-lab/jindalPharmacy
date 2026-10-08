package dataman.dmBase.dmScheduler.server;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;

public class DmSMSWebJob implements IDmWebJob<String> {

    @Override
    public DmWebJobResult<String> executeJob(DmWebJobDataRow dataRow, SiteEnviro_Mdl siteEnviro, List<WebJobAttachment_Mdl> attachments) {
        DmWebJobResult<String> result = new DmWebJobResult<>();
        try {
            Map<String, String> urlParams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            urlParams.put("url", siteEnviro.getWebJobSMSUrl());
            urlParams.put("user", siteEnviro.getWebJobAccName());
            urlParams.put("password", siteEnviro.getWebJobPassword());
            
//            urlParams.put("mobile", dataRow.getDataRow().get("send_to_mobile"));
            
            String mobNo = DmBaseShared.xNull(dataRow.getDataRow().get("send_to_mobile")).trim();
            if (mobNo.length() == 0) {
                throw new Exception("Mobile no. is blank.");
            } else if (mobNo.length() <= 10) {
                urlParams.put("mobile", "91" + mobNo);
            } else {
                urlParams.put("mobile", mobNo);
            }
            
            String smsUrl = DmMapFormat.format(siteEnviro.getWebJobFormat(), urlParams);
            String encodedMsg = java.net.URLEncoder.encode(dataRow.getFormattedMessage(), "UTF-8");

            urlParams.clear();
            urlParams.put("msg", encodedMsg);
            smsUrl = DmMapFormat.format(smsUrl, urlParams);
            String finUrl = smsUrl;

            System.out.println("Final Url for Hitting SMS API : " + finUrl);

            int rCode = -1;
            InputStream inStream = null;
            if (finUrl.startsWith("https")) {
                if (doTrustToCertificates()) {
                    System.out.println("Certificates has been checked... Trying to call api...");
                    URL fUrl = new URL(finUrl);
                    HttpsURLConnection httpsConnection = (HttpsURLConnection) fUrl.openConnection();
                    System.out.println("Https Connection opened...");
                    httpsConnection.setRequestMethod("GET");
                    httpsConnection.setDoOutput(true);
                    rCode = httpsConnection.getResponseCode();
                    inStream = httpsConnection.getInputStream();
                }
            } else if (finUrl.startsWith("http")) {
                System.out.println("Trying to call api...");
                URL fUrl = new URL(finUrl);
                HttpURLConnection httpConnection = (HttpURLConnection) fUrl.openConnection();
                System.out.println("Http Connection opened...");
                httpConnection.setRequestMethod("GET");
                httpConnection.setDoOutput(true);
                rCode = httpConnection.getResponseCode();
                inStream = httpConnection.getInputStream();
            }

            System.out.println("Response Code is : " + rCode);
            System.out.println("Input Stream is : " + inStream);

            if ((rCode == HttpServletResponse.SC_OK) && (inStream != null)) {
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream));
                StringBuilder sb = new StringBuilder();
                String lineRead;
                while ((lineRead = buffReader.readLine()) != null) {
                    sb.append(lineRead);
                }
                buffReader.close();

                result.setResult(sb.toString());
                result.setBoolStatus(true);
            }

        } catch (Exception ex) {
            result.setBoolStatus(false);
            result.setExeExcep(ex);
            ex.printStackTrace();
        }
        return result;
    }

    private boolean doTrustToCertificates() throws Exception {
        boolean bool = false;
        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                        System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                    }
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
            throw new Exception(ex);
        }
        return bool;
    }
}
