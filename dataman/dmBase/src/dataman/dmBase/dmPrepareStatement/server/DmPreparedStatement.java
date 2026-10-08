package dataman.dmBase.dmPrepareStatement.server;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;

public class DmPreparedStatement extends DmPreparedStatementDelegate {

    private enum FormatType {

        NULL, BOOLEAN, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, BIGDECIMAL, STRING, STRINGLIST, DATE, TIME, TIMESTAMP
    }

    private ParseResult parsedResult;

    //factory methods for all possible PreparedStatement constructors
    public static DmPreparedStatement prepareStatement(Connection conn, String sql) throws SQLException {
        ParseResult parseResult = ParseResult.parse(sql);
        return new DmPreparedStatement(conn.prepareStatement(parseResult.getGeneratedSql()), parseResult);
    }

    public static DmPreparedStatement prepareStatement(Connection conn, String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        ParseResult parseResult = ParseResult.parse(sql);
        return new DmPreparedStatement(conn.prepareStatement(parseResult.getGeneratedSql(), resultSetType, resultSetConcurrency),
                parseResult);
    }

    /**
     * Private constructor (use factory methods)
     *
     * @param delegate PreparedStatement delegate
     * @param orderedParameters Ordered list of named parameters produced during
     * parsing
     */
    private DmPreparedStatement(PreparedStatement delegate, ParseResult parsedResult) {
        super(delegate);
        this.parsedResult = parsedResult;
    }

    public ParseResult getParsedResult() {
        return parsedResult;
    }

    public boolean hasNamedParameters() {
        return !parsedResult.getOrderedParameters().isEmpty();
    }

    private Collection<Integer> getParameterIndexes(String parameter) {
        Collection<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < parsedResult.getOrderedParameters().size(); i++) {
            if (parsedResult.getOrderedParameters().get(i).equalsIgnoreCase(parameter)) {
                //add i + 1, since all indexes ever are 0 based EXCEPT JDBC PARAMS WHYYYYY
                indexes.add(i + 1);
            }
        }
        if (indexes.isEmpty()) {
            throw new IllegalArgumentException(String.format("SQL statement doesn't contain the parameter '%s'",
                    parameter));
        }
        return indexes;
    }

    public void setNull(String parameter, int sqlType) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((String) null, FormatType.NULL));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setNull(i, sqlType);
        }
    }

    public void setBoolean(String parameter, boolean x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((Boolean) x, FormatType.BOOLEAN));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setBoolean(i, x);
        }
    }

    public void setByte(String parameter, byte x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((Byte) x, FormatType.BYTE));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setByte(i, x);
        }
    }

    public void setShort(String parameter, short x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((Short) x, FormatType.SHORT));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setShort(i, x);
        }
    }

    public void setInt(String parameter, int x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((Integer) x, FormatType.INTEGER));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setInt(i, x);
        }
    }

    public void setLong(String parameter, long x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((Long) x, FormatType.LONG));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setLong(i, x);
        }
    }

    public void setFloat(String parameter, float x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((Float) x, FormatType.FLOAT));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setFloat(i, x);
        }
    }

    public void setDouble(String parameter, double x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((Double) x, FormatType.DOUBLE));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setDouble(i, x);
        }
    }

    public void setBigDecimal(String parameter, BigDecimal x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((BigDecimal) x, FormatType.BIGDECIMAL));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setBigDecimal(i, x);
        }
    }

    public void setString(String parameter, String x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((String) x, FormatType.STRING));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setString(i, x);
        }
    }

    public void setString(String parameter, String x, boolean ignoreIfBlank) throws SQLException {
        if (ignoreIfBlank) {
            if (DmBaseShared.xNull(x).trim().equalsIgnoreCase("")) {
                return;
            }
        }

        setString(parameter, x);
    }
    
    public void setNString(String parameter, String x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((String) x, FormatType.STRING));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setNString(i, x);
        }
    }
    
    public void setNString(String parameter, String x, boolean ignoreIfBlank) throws SQLException {
        if (ignoreIfBlank) {
            if (DmBaseShared.xNull(x).trim().equalsIgnoreCase("")) {
                return;
            }
        }

        setNString(parameter, x);
    }

    public void setStringList(String parameter, String x) throws SQLException {
        String[] xList;
        String paramName;

        x = x.replace("'", "");
        xList = x.split(",");

        for (int j = 0; j < xList.length; j++) {
            paramName = parameter.trim() + String.valueOf(j + 1);
            setString(paramName, DmBaseShared.xNull(xList[j]).trim());
        }
    }

    public void setStringList(String parameter, String x, boolean ignoreIfBlank) throws SQLException {
        if (ignoreIfBlank) {
            if (DmBaseShared.xNull(x).trim().equalsIgnoreCase("")) {
                return;
            }
        }

        setStringList(parameter, x);
    }

    public void setBytes(String parameter, byte[] x) throws SQLException {
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setBytes(i, x);
            String fval = "";
            for (int j = 0; j < x.length; j++) {
                fval += (char) x[j] + ",";
            }
            if (fval.endsWith(",")) {
                fval = fval.substring(0, fval.length() - 1);
            }
        }
    }

    public void setDate(String parameter, Date x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((Date) x, FormatType.DATE));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setDate(i, x);
        }
    }

    public void setDate(String parameter, Date x, boolean ignoreIfBlank) throws SQLException {
        if (ignoreIfBlank) {
            if (x == null) {
                return;
            }
        }

        setDate(parameter, x);
    }

    public void setTime(String parameter, Time x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((Time) x, FormatType.TIME));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setTime(i, x);
        }
    }

    public void setTimestamp(String parameter, Timestamp x) throws SQLException {
        String formattedValue = Matcher.quoteReplacement(format((Timestamp) x, FormatType.TIMESTAMP));
        if (parsedResult.getParsedParams().get(parameter).getParamValue() == null) {
            parsedResult.getParsedParams().get(parameter).setParamValue(formattedValue);
        }
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setTimestamp(i, x);
        }
    }

    public void setAsciiStream(String parameter, InputStream x) throws SQLException {
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setAsciiStream(i, x);
        }
    }

    @Deprecated
    public void setUnicodeStream(String parameter, InputStream x, int length) throws SQLException {
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setUnicodeStream(i, x, length);
        }
    }

    public void setBinaryStream(String parameter, InputStream x, int length) throws SQLException {
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setBinaryStream(i, x, length);
        }
    }

    public void setObject(String parameter, Object x, int targetSqlType, int scale) throws SQLException {
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setObject(i, x, targetSqlType, scale);
        }
    }

    public void setObject(String parameter, Object x, int targetSqlType) throws SQLException {
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setObject(i, x, targetSqlType);
        }
    }

    public void setObject(String parameter, Object x) throws SQLException {
        for (Integer i : getParameterIndexes(parameter)) {
            getDelegate().setObject(i, x);
        }
    }

    public String getQuery() {
        return this.parsedResult.getExecutableSql();
    }

    public String getParsedQuery() {
        return this.parsedResult.getGeneratedSql();
    }

    public String getOriginalQuery() {
        return this.parsedResult.getOriginalSql().trim();
    }

    private String format(Object o, FormatType type) {
        String returnParam = "";
        try {
            switch (type) {
                case NULL:
                    returnParam = "NULL";
                    break;
                case BIGDECIMAL:
                    returnParam = ((o == null) ? "NULL" : "'" + ((BigDecimal) o).toString() + "'");
                    break;
                case BOOLEAN:
                    returnParam = ((o == null) ? "NULL" : "'" + (((Boolean) o == Boolean.TRUE) ? "1" : "0") + "'");
                    break;
                case BYTE:
                    returnParam = ((o == null) ? "NULL" : "'" + ((Byte) o).intValue() + "'");
                    break;
                case DATE:
                    returnParam = ((o == null) ? "NULL" : "'" + new SimpleDateFormat("yyyy-MM-dd").format((Date) o) + "'");
                    break;
                case DOUBLE:
                    returnParam = ((o == null) ? "NULL" : "'" + ((Double) o).toString() + "'");
                    break;
                case FLOAT:
                    returnParam = ((o == null) ? "NULL" : "'" + ((Float) o).toString() + "'");
                    break;
                case INTEGER:
                    returnParam = ((o == null) ? "NULL" : "'" + ((Integer) o).toString() + "'");
                    break;
                case LONG:
                    returnParam = ((o == null) ? "NULL" : "'" + ((Long) o).toString() + "'");
                    break;
                case SHORT:
                    returnParam = ((o == null) ? "NULL" : "'" + ((Short) o).toString() + "'");
                    break;
                case STRING:
                    returnParam = ((o == null) ? "NULL" : "'" + o.toString() + "'");
                    break;
                case STRINGLIST:
                    returnParam = ((o == null) ? "NULL" : "'" + o.toString() + "'");
                    break;
                case TIME:
                    returnParam = ((o == null) ? "NULL" : "'" + new SimpleDateFormat("hh:mm:ss a").format(o) + "'");
                    break;
                case TIMESTAMP:
                    returnParam = ((o == null) ? "NULL" : "'" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(o) + "'");
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnParam.trim();
    }
}
