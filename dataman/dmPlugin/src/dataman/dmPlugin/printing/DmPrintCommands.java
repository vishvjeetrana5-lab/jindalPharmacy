/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmPlugin.printing;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmPlugin.server.NanoHTTPD;
import dataman.dmPlugin.server.router.RouterNanoHTTPD;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Dataman
 */
public class DmPrintCommands extends RouterNanoHTTPD.DefaultHandler {

    @Override
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        NanoHTTPD.Response response = null;
        try {
            String userAgent = session.getHeaders().get("user-agent");
            Map<String, List<String>> queryParams = NanoHTTPD.decodeParameters(session.getQueryParameterString());
            if (queryParams.containsKey("cmd")) {
                if (queryParams.get("cmd") != null) {
                    String command = queryParams.get("cmd").get(0);
                    if (command.equalsIgnoreCase("print")) {
                        String reportUrl = queryParams.get("reportUrl").get(0);
                        String printerName = queryParams.get("printerName").get(0);
                        String copiesToPrint = queryParams.get("copies").get(0);
                        String printerType = queryParams.get("printerType").get(0);

                        int printCopies = 0;
                        try {
                            printCopies = Integer.parseInt(copiesToPrint);
                        } catch (Exception ex) {
                        }

                        DmEnum.DmPrinterImageType printingType = null;
                        try {
                            printingType = DmEnum.DmPrinterImageType.getPrinterImageType(Integer.parseInt(printerType));
                        } catch (Exception ex) {
                        }

                        printFile(reportUrl, userAgent, printingType, printerName, printCopies);

                        response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), "OK");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }
        return response;
    }

    private void printFile(String reportUrl, String userAgent, DmEnum.DmPrinterImageType printingType, String printerName, int noOfCopies) {
        ExecutorService executors = Executors.newSingleThreadExecutor();
        try {
            DmFilePrintRequest printRequest = new DmFilePrintRequest(reportUrl, userAgent, printingType, printerName, noOfCopies);
            Future<?> future = executors.submit(printRequest);
            future.get(20, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            executors.shutdown();
        }
    }

    @Override
    public String getText() {
        return "DOWNLOAD_PRINTABLE_FILE";
    }

    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.OK;
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

}
