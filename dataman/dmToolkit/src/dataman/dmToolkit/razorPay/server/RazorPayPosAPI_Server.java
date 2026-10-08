package dataman.dmToolkit.razorPay.server;

import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmToolkit.razorPay.shared.pmtCancelReq.PmtCancelRequest_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtCancelRes.PmtCancelResponse_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtIniReq.PmtIniRequest_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtIniRes.PmtIniResponse_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtStatusReq.PmtStatusRequest_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtStatusRes.GetStatusResponse_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtStatusRes.PmtStatusResponse_Mdl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RazorPayPosAPI_Server {

    public static PmtIniResponse_Mdl sendInitiateTransactions(PmtIniRequest_Mdl razorPay, String urlInitiate) throws Exception {
        PmtIniResponse_Mdl response_Mdl = new PmtIniResponse_Mdl();

        String jsonString = DmBaseServer.convertToJson(razorPay, new TypeToken<PmtIniRequest_Mdl>() {
        });

//        OkHttpClient okHttpClient = new OkHttpClient();
//
//        // json request body
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
//
//        Request request = new Request.Builder()
//                .url(urlInitiate)
//                .post(body)
//                .build();
//
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                byte[] responseBytes = response.body().bytes();
//                String responseString = new String(responseBytes, 0, responseBytes.length, StandardCharsets.UTF_8);
//
//                response_Mdl = DmBaseServer.convertFromJson(responseString, new TypeToken<PmtIniResponse_Mdl>() {
//                });
//
//                response_Mdl.setResponseString(responseString);
//                System.out.println(responseString);
//            
//            
//            
//            
//            }
//            
//            else {
//                throw new IOException("Unexpected code " + response);
//            }
//        }
        String apiResponse = callEzetap(urlInitiate, jsonString);

        response_Mdl = DmBaseServer.convertFromJson(apiResponse, new TypeToken<PmtIniResponse_Mdl>() {
        });

        response_Mdl.setResponseString(apiResponse.toString());
        System.out.println(apiResponse);

        return response_Mdl;
    }

    public static String callEzetap(String apiUrl, String jsonBody) throws Exception {
        StringBuilder response = new StringBuilder();
        try {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            doTrustToCertificates();
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setDoOutput(true);

            // Write JSON request
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes("UTF-8"));
                os.flush();
            }

            int responseCode = conn.getResponseCode();

            // Read response
            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }

//            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        return response.toString();

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

    public static PmtStatusResponse_Mdl checkStatusApi(PmtStatusRequest_Mdl razorPay, String urlCheckStatus) throws Exception {
        PmtStatusResponse_Mdl response_Mdl = new PmtStatusResponse_Mdl();
        GetStatusResponse_Mdl respMdl = new GetStatusResponse_Mdl();
        String jsonString = DmBaseServer.convertToJson(razorPay, new TypeToken<PmtStatusRequest_Mdl>() {
        });

//        OkHttpClient okHttpClient = new OkHttpClient();
//
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
//
//        Request request = new Request.Builder()
//                .url(urlCheckStatus)
//                .post(body)
//                .build();
//
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                byte[] responseBytes = response.body().bytes();
//                String responseString = new String(responseBytes, 0, responseBytes.length, StandardCharsets.UTF_8);
//
////                response_Mdl = DmBaseServer.convertFromJson(responseString, new TypeToken<PmtStatusResponse_Mdl>() {
////                });
//                respMdl = DmBaseServer.convertFromJson(responseString, new TypeToken<GetStatusResponse_Mdl>() {
//                });
//
//                respMdl.setResponseString(responseString);
//                response_Mdl.setRespMdl(respMdl);
//
//                System.out.println(responseString);
//            } else {
//                throw new IOException("Unexpected code " + response);
//            }
        String apiResponse = callEzetap(urlCheckStatus, jsonString);

        respMdl = DmBaseServer.convertFromJson(apiResponse, new TypeToken<GetStatusResponse_Mdl>() {
        });

        respMdl.setResponseString(apiResponse);
        response_Mdl.setRespMdl(respMdl);

        System.out.println(apiResponse);

        return response_Mdl;
    }

    public static PmtCancelResponse_Mdl cancelTransactionApi(PmtCancelRequest_Mdl razorPay, String urlCancel) throws Exception {
        GetStatusResponse_Mdl respMdl = new GetStatusResponse_Mdl();

        PmtCancelResponse_Mdl response_Mdl = new PmtCancelResponse_Mdl();
        String jsonString = DmBaseServer.convertToJson(razorPay, new TypeToken<PmtCancelRequest_Mdl>() {
        });

//        OkHttpClient okHttpClient = new OkHttpClient();
//
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
//
//        Request request = new Request.Builder()
//                .url(urlCancel)
//                .post(body)
//                .build();
//
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                byte[] responseBytes = response.body().bytes();
//                String responseString = new String(responseBytes, 0, responseBytes.length, StandardCharsets.UTF_8);
//
////                response_Mdl = DmBaseServer.convertFromJson(responseString, new TypeToken<PmtCancelResponse_Mdl>() {
////                });
//                respMdl = DmBaseServer.convertFromJson(responseString, new TypeToken<GetStatusResponse_Mdl>() {
//                });
//
//                respMdl.setResponseString(responseString);
//                response_Mdl.setRespMdl(respMdl);
//
//                System.out.println(responseString);
//            } else {
//                throw new IOException("Unexpected code " + response);
//            }
//        }
        String apiResponse = callEzetap(urlCancel, jsonString);

        respMdl = DmBaseServer.convertFromJson(apiResponse, new TypeToken<GetStatusResponse_Mdl>() {
        });

        respMdl.setResponseString(apiResponse);
        response_Mdl.setRespMdl(respMdl);

        System.out.println(apiResponse);

        return response_Mdl;
    }
}
