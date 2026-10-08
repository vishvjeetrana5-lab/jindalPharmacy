/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.mi.routing;

import aarogya.projectMenu.mi.jvm.MiPersistenceModel;
import aarogya.projectMenu.servlet.shared.MachineInterfaceConfig_SrvAsync;
import aarogya.projectMenu.servlet.shared.MachineInterfaceData_SrvAsync;
import com.google.gson.reflect.TypeToken;
import dataman.dmPlugin.CFunctions;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 *
 * @author abhishek
 */
public class MiPersistanceService implements Runnable {

    private List<MiPersistenceModel> listModel;

    public static final String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:61.0) Gecko/20100101 Firefox/61.0";

    public MiPersistanceService(List<MiPersistenceModel> models) {
        listModel = models;
    }

    @Override
    public void run() {
        HttpURLConnection httpConnection = null;
        try {
            String jsonString = CFunctions.getJsonFromObject(listModel, new TypeToken<List<MiPersistenceModel>>() {
            });

            String urlToHit = CFunctions.getServerApplicationUrl() + "/" + MachineInterfaceData_SrvAsync.class.getName().trim().replace(".", "/");//+ "/dataman/selfToolkit/mi/miDataService";

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
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            listModel.clear();
            listModel = null;
            if (httpConnection != null) {
                httpConnection.disconnect();
                httpConnection = null;
            }
        }
    }

}
