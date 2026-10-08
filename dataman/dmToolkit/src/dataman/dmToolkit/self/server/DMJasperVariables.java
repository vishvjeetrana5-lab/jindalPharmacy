package dataman.dmToolkit.self.server;

import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.util.HashMap;
import java.util.LinkedHashMap;
import net.sf.jasperreports.engine.JasperReport;

public interface DMJasperVariables {

    public HashMap setJasperVariables(DmRSS rssTemp, String reportTitle, JasperReport jasperReport, LinkedHashMap<String, FieldUDT_Mdl> filter, HashMap parameter) throws Exception;
}
