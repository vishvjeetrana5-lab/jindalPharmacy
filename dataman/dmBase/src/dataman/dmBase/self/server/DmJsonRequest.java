package dataman.dmBase.self.server;

import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.jvmSerialization.shared.DmJsonResponse_Mdl;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DmJsonRequest {

    public DmJsonRequest() {
    }

    public <T> DmJsonResponse_Mdl<T> sendRequest(String jsonString, String sessionId, String targetURL, TypeToken<DmJsonResponse_Mdl<T>> typeToken) throws Exception {
        URL url = new URL(targetURL);
        String inputLine;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("DTMN_SERVICE", "TRUE");
        if (DmBaseShared.xNull(sessionId).trim().equalsIgnoreCase("")) {
            connection.setRequestProperty("DTMN_SESSIONID", "BLANK");
        } else {
            connection.setRequestProperty("DTMN_SESSIONID", sessionId);
        }
        connection.setDoOutput(true);

        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
        dos.writeBytes(jsonString);
        dos.flush();
        dos.close();

        int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + targetURL);
        System.out.println("Response Code : " + responseCode);

        try {
            InputStream inputStream = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            return DmBaseServer.convertFromJson(response.toString(), typeToken);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
