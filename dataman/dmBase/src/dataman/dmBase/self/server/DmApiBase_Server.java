package dataman.dmBase.self.server;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.File;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DmApiBase_Server {

    private final String SECURITY_CONTEXT_INSTANCE;
    private final int CONNECT_TIME_OUT, WRITE_TIME_OUT, READ_TIME_OUT;
    private final TimeUnit CONNECT_TIME_OUT_UNIT, WRITE_TIME_OUT_UNIT, READ_TIME_OUT_UNIT;

    public DmApiBase_Server(String security_context_instance, int connectTimeout, TimeUnit connectTimeOutUnit, int writeTimeout, TimeUnit writeTimeOutUnit, int readTimeout, TimeUnit readTimeOutUnit) {
        this.SECURITY_CONTEXT_INSTANCE = security_context_instance;
        this.CONNECT_TIME_OUT = connectTimeout;
        this.CONNECT_TIME_OUT_UNIT = connectTimeOutUnit;
        this.WRITE_TIME_OUT = writeTimeout;
        this.WRITE_TIME_OUT_UNIT = writeTimeOutUnit;
        this.READ_TIME_OUT = readTimeout;
        this.READ_TIME_OUT_UNIT = readTimeOutUnit;
    }

    public DmApiBase_Server() {
        //Default Configuration
        this.SECURITY_CONTEXT_INSTANCE = "SSL";
        this.CONNECT_TIME_OUT = 40;
        this.WRITE_TIME_OUT = 40;
        this.READ_TIME_OUT = 60;
        this.CONNECT_TIME_OUT_UNIT = TimeUnit.SECONDS;
        this.WRITE_TIME_OUT_UNIT = TimeUnit.SECONDS;
        this.READ_TIME_OUT_UNIT = TimeUnit.SECONDS;
    }

    public Response startProcessApiRequest(String url, HTTP_METHOD http_method, String content_type, String requestData, HashMap<String, String> headerList) throws Exception {
        RequestBody body = null;
        if (http_method == HTTP_METHOD.POST || http_method == HTTP_METHOD.PUT) {
            body = RequestBody.create(MediaType.parse(content_type), requestData);
        }
        return startProcessApiRequest(url, http_method, content_type, body, headerList);
    }

    public Response startProcessApiRequest(String url, HTTP_METHOD http_method, String content_type, File requestFile, HashMap<String, String> headerList) throws Exception {
        RequestBody multipartBody = null;
        if (http_method == HTTP_METHOD.POST || http_method == HTTP_METHOD.PUT) {
            RequestBody requestBody = RequestBody.create(MediaType.parse(content_type), requestFile);
            multipartBody = new MultipartBody.Builder()
                    .addFormDataPart("file", requestFile.getName(), requestBody).build();
        }
        return startProcessApiRequest(url, http_method, content_type, multipartBody, headerList);
    }

    public Response startProcessApiRequest(String url, HTTP_METHOD http_method, String content_type, RequestBody requestBody, HashMap<String, String> headerList) throws Exception {
        Response response = null;
        try {
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url);
            for (String key : headerList.keySet()) {
                requestBuilder.addHeader(key, headerList.get(key));
            }
            if (http_method == HTTP_METHOD.POST || http_method == HTTP_METHOD.PUT) {
                requestBuilder.method(http_method.name(), requestBody);
            }
            Request request = requestBuilder.build();
            OkHttpClient client = getCertifiedClient(SECURITY_CONTEXT_INSTANCE, CONNECT_TIME_OUT, CONNECT_TIME_OUT_UNIT, WRITE_TIME_OUT, WRITE_TIME_OUT_UNIT, READ_TIME_OUT, READ_TIME_OUT_UNIT);
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return response;
    }

    private OkHttpClient getCertifiedClient(String security_context_instance, int connectTimeout, TimeUnit connectTimeOutUnit, int writeTimeout, TimeUnit writeTimeOutUnit, int readTimeout, TimeUnit readTimeOutUnit) throws Exception {
        OkHttpClient.Builder builder = null;
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
            };
            SSLContext sslContext = SSLContext.getInstance(security_context_instance);
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return builder.connectTimeout(connectTimeout, connectTimeOutUnit)
                .writeTimeout(writeTimeout, writeTimeOutUnit)
                .readTimeout(readTimeout, readTimeOutUnit).build();
    }

    public static String getContent_Type(String extension) {
        String content_type = null;
        if (extension.equalsIgnoreCase("pdf")) {
            content_type = "application/pdf";
        } else if (extension.equalsIgnoreCase("xls")) {
            content_type = "application/vnd.ms-excel";
        } else if (extension.equalsIgnoreCase("xlsx")) {
            content_type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        } else if (extension.equalsIgnoreCase("doc")) {
            content_type = "application/msword";
        } else if (extension.equalsIgnoreCase("docx")) {
            content_type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (extension.equalsIgnoreCase("ppt")) {
            content_type = "application/vnd.ms-powerpoint";
        } else if (extension.equalsIgnoreCase("pptx")) {
            content_type = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        } else if (extension.equalsIgnoreCase("xml")) {
            content_type = "application/xml";
        } else if (extension.equalsIgnoreCase("png")) {
            content_type = "image/png";
        } else if (extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg")) {
            content_type = "image/jpeg";
        } else if (extension.equalsIgnoreCase("csv")) {
            content_type = "application/csv";
        } else if (extension.equalsIgnoreCase("3gp")) {
            content_type = "video/3gp";
        } else if (extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("m4a")) {
            content_type = "video/mp4";
        } else if (extension.equalsIgnoreCase("aac")) {
            content_type = "audio/aac";
        } else if (extension.equalsIgnoreCase("oga")) {
            content_type = "audio/oga";
        }
        return content_type;
    }

    public static enum HTTP_METHOD implements Serializable, IsSerializable {
        GET, POST, PUT, DELETE
    }

}
