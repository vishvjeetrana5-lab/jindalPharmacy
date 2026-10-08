package dataman.dmPlugin.localSettings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;
import javax.print.PrintService;

public class PrinterInfo {

    public String getPrinterInfo() {
        String printers = "";
        try {
            PrintService[] printerService = PrinterJob.lookupPrintServices();

            List<String> lstPrinters = new ArrayList<>();
            for (int i = 0; i < printerService.length; i++) {
                lstPrinters.add(printerService[i].getName());
            }

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            printers = gson.toJson(lstPrinters);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return printers;
    }
}
