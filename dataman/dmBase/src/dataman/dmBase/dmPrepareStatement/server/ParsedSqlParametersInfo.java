package dataman.dmBase.dmPrepareStatement.server;

import java.util.ArrayList;
import java.util.List;

class ParsedSqlParametersInfo {

    private List<Integer> listIndex;
    private String paramName;
    private String paramValue;

    public ParsedSqlParametersInfo(String parameterName) {
        listIndex = new ArrayList<>();
        paramName = parameterName;
    }

    public List<Integer> getIndexList() {
        return listIndex;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String parameterName) {
        paramName = parameterName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String parameterValue) {
        paramValue = parameterValue;
    }
}
