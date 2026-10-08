package dataman.dmToolkitR.dmJasper.server;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;

public class DmPLSqlMicrosoftQueryExecutor extends DmPLSqlAbstractQueryExecutor {

    public DmPLSqlMicrosoftQueryExecutor(JRDataset dataset, Map parameters, DmPLSqlExecutorFactory.DBType dbType) {
        super(dataset, parameters, dbType);
    }

    @Override
    protected void doCreateStatementMicrosoft(SQLServerConnection connection, PreparedStatement statement, int paramIndex, Object paramValue) throws JRException {
        if (statement instanceof SQLServerCallableStatement) {
            if (paramValue.getClass().equals(SQLServerDataTable.class)) {
                try {
                    ((SQLServerCallableStatement) statement).setStructured(paramIndex, "dbo.fieldUDT", (SQLServerDataTable) paramValue);
                } catch (Exception ex) {
                    throw new JRException(ex);
                }
            }
        }
    }

    @Override
    protected ResultSet doCreateDataSourceMicrosoft(CallableStatement callableStatement) throws JRException {
        ResultSet rsTemp = null;
        if (callableStatement instanceof SQLServerCallableStatement) {
            try {
                rsTemp = ((SQLServerCallableStatement) callableStatement).getResultSet();
            } catch (Exception e) {
                throw new JRException(e);
            }
        }
        return rsTemp;
    }

}
