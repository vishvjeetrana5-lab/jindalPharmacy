package dataman.dmPlugin.getmac;

import dataman.dmPlugin.CFunctions;
import static dataman.dmPlugin.CFunctions.getSettingsFile;
import dataman.dmPlugin.server.NanoHTTPD;
import dataman.dmPlugin.server.router.RouterNanoHTTPD;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Lakhan
 */
public class GetSavePOSdetails extends RouterNanoHTTPD.DefaultHandler {

    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        NanoHTTPD.Response response = null;
        try {
            String requestModelJson = "";
            String responseString = null;
            Map<String, List<String>> queryParams = NanoHTTPD.decodeParameters(session.getQueryParameterString());
            String userAgent = session.getHeaders().get("user-agent");

            if ((queryParams == null) || (queryParams.isEmpty())) {
                throw new Exception("Invalid request");
            }
            List<String> commands = queryParams.get("cmd");
            if ((commands == null) || (commands.isEmpty())) {
                throw new Exception("Command not found about processing of request.");
            }
            String requestCommand = StringUtils.stripToEmpty(commands.get(0));
            if (StringUtils.isBlank(requestCommand)) {
                throw new Exception("Command not found about processing of request.");
            }

            List<String> dataList = queryParams.get("data");
//            if ((dataList == null) || (dataList.isEmpty())) {
//                throw new Exception("Command not found about processing of request.");
//            }
            String requestData = "";
            if (dataList != null) {
                requestData = StringUtils.stripToEmpty(dataList.get(0));
                if (StringUtils.isBlank(requestData) && requestCommand.equalsIgnoreCase("write")) {
                    throw new Exception("Data not found about processing of request.");
                }
            }

            if ("write".equalsIgnoreCase(requestCommand)) {
//                StringBuilder stringBuffer = new StringBuilder();
//                InputStream is = session.getInputStream();
//                byte[] readBytes = new byte[is.available()];
//                int readLen = is.read(readBytes);
//                stringBuffer.append(new String(readBytes, 0, readLen, StandardCharsets.UTF_8));

                System.out.println("data: " + requestData);

//                responseString = stringBuffer.toString();
                savePrinterSettings(requestData);
            }

            responseString = loadSavedSettings();

            response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), responseString.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }
        return response;
    }

    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        NanoHTTPD.Response response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), StringUtils.EMPTY);
        try {
            if (session.getMethod() == NanoHTTPD.Method.OPTIONS) {
                response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
                response.addHeader("Access-Control-Allow-Origin", "*");
            }
//            String requestModelJson = "";
//            String responseString = null;
//            Map<String, List<String>> queryParams = NanoHTTPD.decodeParameters(session.getQueryParameterString());
//            String userAgent = session.getHeaders().get("user-agent");
//
//            if ((queryParams == null) || (queryParams.isEmpty())) {
//                throw new Exception("Invalid request");
//            }
//            List<String> commands = queryParams.get("cmd");
//            if ((commands == null) || (commands.isEmpty())) {
//                throw new Exception("Command not found about processing of request.");
//            }
//            String requestCommand = StringUtils.stripToEmpty(commands.get(0));
//            if (StringUtils.isBlank(requestCommand)) {
//                throw new Exception("Command not found about processing of request.");
//            }
//
//            InputStream inputStream = session.getInputStream();
//            int readLen = inputStream.available();
//            byte[] readBytes = new byte[readLen];
//            inputStream.read(readBytes, 0, readLen);
//            responseString = new String(readBytes, 0, readLen, StandardCharsets.UTF_8);
//            if ("write".equalsIgnoreCase(requestCommand)) {
//                savePrinterSettings(responseString);
//            }
            //responseString = loadSavedSettings();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            response.addHeader("Access-Control-Allow-Method", "*");
        }
        return response;
    }

    public static String savePrinterSettings(String json) throws Exception {
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            if (!CFunctions.getPOSSettingsFile().exists()) {
                if (!CFunctions.getPOSSettingsFile().createNewFile()) {
                    throw new Exception("Unable to create the file: " + getSettingsFile().getAbsolutePath());
                }
            }
            fos = new FileOutputStream(CFunctions.getPOSSettingsFile());
            fos.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex);
        } finally {
            if (oos != null) {
                oos.close();
                oos = null;
            }
            if (fos != null) {
                fos.close();
                fos = null;
            }
        }
        return loadSavedSettings();
    }

    public static String loadSavedSettings() throws Exception {
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        String localSettings = null;
        try {
            File configFile = CFunctions.getPOSSettingsFile();
            if (configFile.exists()) {
                byte[] readBytes = Files.readAllBytes(configFile.toPath());
                localSettings = new String(readBytes, 0, readBytes.length, StandardCharsets.UTF_8);
            }
        } finally {
            if (ois != null) {
                ois.close();
                ois = null;
            }
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
        return localSettings;
    }

    @Override
    public String getText() {
        return "GET MAC ADDRESS";
    }

    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.OK;
    }

    @Override
    public String getMimeType() {
        return "application/text";
    }

    private synchronized List<String> getProcessOutput(String commandToRun) throws Exception {
        Process process = Runtime.getRuntime().exec(commandToRun);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> listResult = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (!StringUtils.isBlank(line)) {
                listResult.add(line);
            }
        }
        return listResult;
    }
}
