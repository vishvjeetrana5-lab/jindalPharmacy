package dataman.dmToolkitR.dmJasper.server;

import com.jaspersoft.jrx.query.PlSqlQueryExecuter;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.util.JRProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class DmPLSqlAbstractQueryExecutor extends PlSqlQueryExecuter {

    static {
        log = LogFactory.getLog(com.jaspersoft.jrx.query.PlSqlQueryExecuter.class);
    }

    private static final Log log;
    private static final int ORACLE_CURSOR_TYPE = -10;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private int cursorParameter;
    private boolean isStoredProcedure;
    private DmPLSqlExecutorFactory.DBType dbType;

    public DmPLSqlAbstractQueryExecutor(JRDataset dataset, Map parameters, DmPLSqlExecutorFactory.DBType dbType) {
        super(dataset, parameters);
        this.dbType = dbType;
        cursorParameter = -1;
        isStoredProcedure = false;
        connection = (Connection) getParameterValue("REPORT_CONNECTION");
        if (connection == null && log.isWarnEnabled()) {
            log.warn("The supplied java.sql.Connection object is null.");
        }
//        super.parseQuery();
    }

    @Override
    public JRDataSource createDatasource() throws JRException {
        JRDataSource dataSource = null;
        createStatement();
        if (statement != null) {
            try {
                Integer reportMaxCount = (Integer) getParameterValue("REPORT_MAX_COUNT");
                if (reportMaxCount != null) {
                    statement.setMaxRows(reportMaxCount.intValue());
                }
                if (isStoredProcedure) {
                    CallableStatement cstmt = (CallableStatement) statement;
                    cstmt.execute();
                    if (cursorParameter > 0) {
                        resultSet = (ResultSet) cstmt.getObject(cursorParameter);
                    } else if (dbType == DmPLSqlExecutorFactory.DBType.Microsoft) {
                        resultSet = doCreateDataSourceMicrosoft(cstmt);
                    }
                } else {
                    resultSet = statement.executeQuery();
                }
                dataSource = new JRResultSetDataSource(resultSet);
            } catch (SQLException e) {
                throw new JRException("Error executing SQL statement for : " + dataset.getName(), e);
            }
        }
        return dataSource;
    }

    @Override
    public synchronized void close() {
        super.close(); //To change body of generated methods, choose Tools | Templates.
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }
        } catch (Exception e) {
        }
    }

    private boolean isStoredProcedure(String queryString)
            throws SQLException {

        String sql = queryString.trim().toLowerCase();
        return sql.charAt(0) == '{' && sql.substring(1).trim().startsWith("call ");
    }

    protected abstract ResultSet doCreateDataSourceMicrosoft(CallableStatement callableStatement) throws JRException;

    protected abstract void doCreateStatementMicrosoft(SQLServerConnection connection, PreparedStatement statement, int paramIndex, Object paramValue) throws JRException;

    private void createStatement() throws JRException {
        String queryString = getQueryString();
        if (connection != null && queryString != null && queryString.trim().length() > 0) {
            try {
                isStoredProcedure = isStoredProcedure(queryString);
                if (isStoredProcedure) {
                    statement = connection.prepareCall(queryString);
                } else {
                    statement = connection.prepareStatement(queryString);
                }
                int fetchSize = JRProperties.getIntegerProperty(dataset.getPropertiesMap(), "net.sf.jasperreports.jdbc.fetch.size", 0);
                if (fetchSize > 0) {
                    statement.setFetchSize(fetchSize);
                }
                List<String> parameterNames = getCollectedParameterNames();
                if (!parameterNames.isEmpty()) {
                    for (int i = 0; i < parameterNames.size(); i++) {
                        String parameterName = (String) parameterNames.get(i);
                        JRFillParameter parameter = getParameter(parameterName);
                        Class clazz = parameter.getValueClass();
                        Object parameterValue = parameter.getValue();
                        if (clazz.equals(java.lang.Boolean.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, -7);
                            } else {
                                statement.setBoolean(i + 1, ((Boolean) parameterValue).booleanValue());
                            }
                        } else if (clazz.equals(java.lang.Byte.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, -6);
                            } else {
                                statement.setByte(i + 1, ((Byte) parameterValue).byteValue());
                            }
                        } else if (clazz.equals(java.lang.Double.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, 8);
                            } else {
                                statement.setDouble(i + 1, ((Double) parameterValue).doubleValue());
                            }
                        } else if (clazz.equals(java.lang.Float.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, 6);
                            } else {
                                statement.setFloat(i + 1, ((Float) parameterValue).floatValue());
                            }
                        } else if (clazz.equals(java.lang.Integer.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, 4);
                            } else {
                                statement.setInt(i + 1, ((Integer) parameterValue).intValue());
                            }
                        } else if (clazz.equals(java.lang.Long.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, -5);
                            } else {
                                statement.setLong(i + 1, ((Long) parameterValue).longValue());
                            }
                        } else if (clazz.equals(java.lang.Short.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, 5);
                            } else {
                                statement.setShort(i + 1, ((Short) parameterValue).shortValue());
                            }
                        } else if (clazz.equals(java.math.BigDecimal.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, 3);
                            } else {
                                statement.setBigDecimal(i + 1, (BigDecimal) parameterValue);
                            }
                        } else if (clazz.equals(java.lang.String.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, 12);
                            } else {
                                statement.setString(i + 1, parameterValue.toString());
                            }
                        } else if (clazz.equals(java.util.Date.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, 91);
                            } else {
                                statement.setDate(i + 1, new Date(((java.util.Date) parameterValue).getTime()));
                            }
                        } else if (clazz.equals(java.sql.Timestamp.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, 93);
                            } else {
                                statement.setTimestamp(i + 1, (Timestamp) parameterValue);
                            }
                        } else if (clazz.equals(java.sql.Time.class)) {
                            if (parameterValue == null) {
                                statement.setNull(i + 1, 92);
                            } else {
                                statement.setTime(i + 1, (Time) parameterValue);
                            }
                        } else if (clazz.equals(java.sql.ResultSet.class)) {
                            if (!isStoredProcedure) {
                                throw new JRException("OUT paramater used in non-stored procedure call : " + parameterName + " class " + clazz.getName());
                            }
                            if (cursorParameter > 0) {
                                throw new JRException("A stored procedure can have at most one cursor parameter : " + parameterName + " class " + clazz.getName());
                            }
                            ((CallableStatement) statement).registerOutParameter(i + 1, -10);
                            cursorParameter = i + 1;
                        } else {
//                            throw new JRException("Parameter type not supported in query : " + parameterName + " class " + clazz.getName());
                            if ((dbType != DmPLSqlExecutorFactory.DBType.Others || dbType != DmPLSqlExecutorFactory.DBType.NotSpecified) && (!clazz.equals(parameterValue.getClass()))) {
                                if (dbType == DmPLSqlExecutorFactory.DBType.Microsoft) {
                                    doCreateStatementMicrosoft((SQLServerConnection) connection, statement, i + 1, parameterValue);
                                }
                            } else if (clazz.equals(java.lang.Object.class)) {
                                if (parameterValue == null) {
                                    statement.setNull(i + 1, 2000);
                                } else {
                                    statement.setObject(i + 1, parameterValue);
                                }
                            }
                        }
                    }

                }
            } catch (SQLException e) {
                throw new JRException("Error preparing statement for executing the report query : \n\n" + queryString + "\n\n", e);
            }
        }
    }
}
