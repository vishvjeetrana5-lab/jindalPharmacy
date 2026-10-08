package dataman.dmBase.dmPrepareStatement.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseResult {

    private String originalSql;
    private List<String> orderedParameters;
    private String generatedSql;
    private TreeMap<String, ParsedSqlParametersInfo> parsedParams;

    public ParseResult() {
        parsedParams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        orderedParameters = new ArrayList<>();
    }

    public ParseResult(String originalQuery) {
        this();
        originalSql = originalQuery;
    }

    public String getOriginalSql() {
        return originalSql;
    }

    public List<String> getOrderedParameters() {
        return orderedParameters;
    }

    public String getGeneratedSql() {
        return generatedSql;
    }

    public void setGeneratedSql(String parsedSql) {
        generatedSql = parsedSql;
    }

    public TreeMap<String, ParsedSqlParametersInfo> getParsedParams() {
        return parsedParams;
    }

    public String getExecutableSql() {
        String tempOriginalSql = originalSql;
        Iterator<String> distinctParams = parsedParams.keySet().iterator();
        while (distinctParams.hasNext()) {
            String paramName = distinctParams.next();
            ParsedSqlParametersInfo info = parsedParams.get(paramName);
            String paramValue = info.getParamValue();
            String searchString = "(?i)\\:\\b" + paramName + "\\b.*?";
            if (paramValue != null) {
                tempOriginalSql = tempOriginalSql.replaceAll(searchString, paramValue);
            }
        }

        return tempOriginalSql;
    }

    public static ParseResult parse(String query) {
        ParseResult parseResult = new ParseResult(query);
        int length = query.length();

        StringBuilder parsedQuery = new StringBuilder(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inSingleLineComment = false;
        boolean inMultiLineComment = false;

        for (int i = 0; i < length; i++) {

            char c = query.charAt(i);
            boolean parameterFound = false;

            if (inSingleQuote) {
                if (c == '\'') {
                    inSingleQuote = false;
                }
            } else if (inDoubleQuote) {
                if (c == '"') {
                    inDoubleQuote = false;
                }
            } else if (inMultiLineComment) {
                if (c == '*' && query.charAt(i + 1) == '/') {
                    inMultiLineComment = false;
                }
            } else if (inSingleLineComment) {
                if (c == '\n') {
                    inSingleLineComment = false;
                }
            } else if (c == '\'') {
                inSingleQuote = true;
            } else if (c == '"') {
                inDoubleQuote = true;
            } else if (c == '/' && query.charAt(i + 1) == '*') {
                inMultiLineComment = true;
            } else if (c == '-' && query.charAt(i + 1) == '-') {
                inSingleLineComment = true;
            } else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query.charAt(i + 1))) {
                int j = i + 2;
                while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
                    j++;
                }
                String name = query.substring(i + 1, j);

                parseResult.getOrderedParameters().add(name);
                c = '?'; // replace the parameter with a question mark                
                parameterFound = true;

                i += name.length(); // skip past the end if the parameter
            }

            if ((c == '?') && (parameterFound)) {
                String parameterName = parseResult.getOrderedParameters().get(parseResult.getOrderedParameters().size() - 1);
                if (!parseResult.getParsedParams().containsKey(parameterName)) {
                    ParsedSqlParametersInfo info = new ParsedSqlParametersInfo(parameterName);
                    info.setParamName(parameterName);
                    parseResult.getParsedParams().put(parameterName, info);
                }

                int charIndex = parsedQuery.length();
                if (!parseResult.getParsedParams().get(parameterName).getIndexList().contains(charIndex)) {
                    parseResult.getParsedParams().get(parameterName).getIndexList().add(charIndex);
                }

                parameterFound = false;
            }

            parsedQuery.append(c);

        }
        parseResult.setGeneratedSql(parsedQuery.toString());
        return parseResult;
    }

}
