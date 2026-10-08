package dataman.dmToolkitR.paymentGateway.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class EazyPayPoller extends TimerTask {

    Connection cnTemp = null;

    public EazyPayPoller(Connection connection) {
        this.cnTemp = connection;
        System.out.println("EazyPayPoller Constructor Called.");

    }

    @Override
    public void run() {
        try {
            startPolling();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean cancel() {
        boolean bool = false;
        try {
//            bool = super.cancel();
            System.out.println("Token Release Timer cancell called...");
//            if (cnTemp != null && !cnTemp.isClosed()) {
//                cnTemp.close();
//                cnTemp = null;
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bool;
    }

    private void startPolling() throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {

//            sqlQuery = " update consultantDiary set isTokenLock=NULL where ISNULL(uhid,0)=0 and isnull(isTokenLock,0)=1 ";
            sqlQuery = " update cdTemp set cdTemp.isTokenLock=NULL\n"
                    + "from onlinePaymentStatus onl\n"
                    + "left join consultantDiary cdTemp on cdTemp.docid=onl.tempTokenDocid \n"
                    + "left join enviroCompany envc on 1=1 \n"
                    + "where cdTemp.isTokenLock=1\n"
                    + "and DATEDIFF(MINUTE, onl.request_TimeStamp, GETDATE()) >=envc.TokenReleaseTimer\n"
                    + "and isnull(cdTemp.uhid ,0)=0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();
            System.out.println("Token Release Query executed...");

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new Exception(ex);
        } finally {

            if (psTemp != null) {
                psTemp.close();
                psTemp = null;
            }
        }

    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : params) {
            if (param.split("=").length > 1) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            } else if (param.split("=").length == 1) {
                String name = param.split("=")[0];
                String value = "";
                map.put(name, value);
            }
        }
        return map;
    }

    private static String getOrderPollingStatus(String merchantId, String recid) throws Exception {
        String status = "";
        try {

            String url = "https://eazypay.icicibank.com/EazyPGVerify?ezpaytranid=&amount=&paymentmode=&merchantid=" + merchantId + "&trandate=&pgreferenceno=" + recid + "";
            String urlParameters = "";
            int rCode = -1;
            InputStream inputStream = null;
            if (url.startsWith("https")) {
                if (doTrustToCertificates()) {
                    System.out.println("Certificates checked has been passed...");
                    URL obj = new URL(url);
                    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                    System.out.println("Https Connection is opened...");
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                    con.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                    con.setRequestProperty("Accept", "*/*");
                    con.setDoOutput(true);
                    System.out.println("Trying to get outputstream...");

                    rCode = con.getResponseCode();
                    System.out.println("Response code is : " + rCode);
                    inputStream = con.getInputStream();
                    System.out.println("Input stream got..." + inputStream);
                }
            } else if (url.startsWith("http")) {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                System.out.println("Https Connection is opened...");
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                con.setRequestProperty("Accept", "*/*");
                con.setDoOutput(true);
                System.out.println("Trying to get outputstream...");

                rCode = con.getResponseCode();
                System.out.println("Response code is : " + rCode);
                inputStream = con.getInputStream();
                System.out.println("Input stream got..." + inputStream);
            }

            StringBuffer response = new StringBuffer();
            if ((rCode == HttpURLConnection.HTTP_OK) && (inputStream != null)) {
                System.out.println("Response Code : " + rCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
            System.out.println("Polling API Called");
            status = response.toString();

        } catch (Exception ex) {
            status = "";
            ex.printStackTrace();
            throw new Exception(ex);
        }
        return status;
    }

    private static boolean doTrustToCertificates() throws Exception {
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
