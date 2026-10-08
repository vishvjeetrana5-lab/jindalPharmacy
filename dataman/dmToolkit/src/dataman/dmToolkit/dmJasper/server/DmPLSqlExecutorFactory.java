package dataman.dmToolkit.dmJasper.server;

import com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory;
import java.sql.Connection;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.query.JRQueryExecuter;

public class DmPLSqlExecutorFactory extends PlSqlQueryExecuterFactory {

    protected enum DBType {

        NotSpecified,
        Oracle,
        Microsoft,
        MySql,
        PostgreSql,
        Others;
    }

    @Override
    public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map parameters) throws JRException {
        JRQueryExecuter queryExecutor = null;
        DBType dbType = DBType.NotSpecified;
            if (!parameters.containsKey("REPORT_CONNECTION")) {
            throw new JRException("Report Connection not found...");
        }

        dbType = getDbVendor((Connection)((JRFillParameter) parameters.get("REPORT_CONNECTION")).getValue());
        if (dbType == DBType.Microsoft) {
            queryExecutor = new DmPLSqlMicrosoftQueryExecutor(dataset, parameters, dbType);
        }
        return queryExecutor;//new DmPlSqlQueryExecutor(dataset, parameters);//super.createQueryExecuter(dataset, parameters); //To change body of generated methods, choose Tools | Templates.
    }

    private DBType getDbVendor(Connection connection) throws JRException {
        DBType dbType = DBType.NotSpecified;
        try {
            if (connection == null) {
                throw new JRException("Connection not found...");
            }

            String dbVendor = connection.getMetaData().getDatabaseProductName().toLowerCase();
            if (dbVendor.equalsIgnoreCase("oracle")) {
                dbType = DBType.Oracle;
            } else if (dbVendor.equalsIgnoreCase("microsoft sql server")) {
                dbType = DBType.Microsoft;
            }
        } catch (Exception ex) {
            throw new JRException(ex);
        }
        return dbType;
    }

}
