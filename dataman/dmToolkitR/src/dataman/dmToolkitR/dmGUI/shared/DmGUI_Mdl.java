package dataman.dmToolkitR.dmGUI.shared;

import dataman.dmBase.self.shared.KeyField;
import java.util.ArrayList;
import java.util.List;

public class DmGUI_Mdl extends KeyField {

    private byte[] tableName;
    private byte[] sqlQuery;
    private String reportNamePrefix;
    List<DmGUIRow_Mdl> guiRowList = new ArrayList<DmGUIRow_Mdl>();
    List<String> listCustomWidget = new ArrayList<String>();

    public DmGUI_Mdl() {
    }

    public String getTableName() {
        return new String(tableName);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName.getBytes();
    }

    public String getSqlQuery() {
        return new String(sqlQuery);
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery.getBytes();
    }

    public List<DmGUIRow_Mdl> getGuiRowList() {
        return guiRowList;
    }

    public void setGuiRowList(List<DmGUIRow_Mdl> guiRowList) {
        this.guiRowList = guiRowList;
    }

    public List<String> getListCustomWidget() {
        return listCustomWidget;
    }

    public String getReportNamePrefix() {
        return reportNamePrefix;
    }

    public void setReportNamePrefix(String reportNamePrefix) {
        this.reportNamePrefix = reportNamePrefix;
    }
}
