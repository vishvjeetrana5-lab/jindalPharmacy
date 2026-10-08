package dataman.dmBase.self.server;

import java.util.Map;
import net.sf.jasperreports.engine.JasperReport;

public interface DmIJasperCompiled {

    public JasperReport getReport();

    public Map<String, JasperReport> getSubReports();
}
