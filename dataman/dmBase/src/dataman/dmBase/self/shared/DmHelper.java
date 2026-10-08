package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.DmFilter;
import dataman.dmBase.self.shared.DmSort;
import dataman.dmBase.self.shared.DmEnum;
import java.util.ArrayList;
import java.util.List;

public class DmHelper implements IsSerializable {

    private DmPaging pageing = new DmPaging();
    private List<DmFilter> lstFilter = new ArrayList<>();
    private List<DmSort> lstSort = new ArrayList<>();

//    public <T> DmHelper() {
//        pageing.setRowsPerPage(0);
//        pageing.setRowsStartFrom(0);
//    }
    
    public DmHelper() {
        pageing.setRowsPerPage(0);
        pageing.setRowsStartFrom(0);
    }

//    public <T> DmHelper(int rowsStartFrom, int rowsPerPage) {
//        pageing.setRowsStartFrom(rowsStartFrom);
//        pageing.setRowsPerPage(rowsPerPage);
//    }
    
    public DmHelper(int rowsStartFrom, int rowsPerPage) {
        pageing.setRowsStartFrom(rowsStartFrom);
        pageing.setRowsPerPage(rowsPerPage);
    }

    public boolean isEndOfFile() {
        return pageing.isEndOfFile();
    }

    public boolean isBeginOfFile() {
        return pageing.isBeginOfFile();
    }

    public int getRowCount() {
        return pageing.getRowCount();
    }

    public void setRowCount(int rowCount) {
        pageing.setRowCount(rowCount);
    }

    public int getRowsPerPage() {
        return pageing.getRowsPerPage();
    }

    public void setRowsPerPage(int rowsPerPage) {
        pageing.setRowsPerPage(rowsPerPage);
    }

    public int getRowsStartFrom() {
        return pageing.getRowsStartFrom();
    }

    public void setRowsStartFrom(int rowsStartFrom) {
        pageing.setRowsStartFrom(rowsStartFrom);
    }

    public DmPaging getPaging() {
        return pageing;
    }

    public void setPaging(DmPaging pageing) {
        this.pageing = pageing;
    }

    public List<DmFilter> getFilter() {
        return lstFilter;
    }

    public void setFilter(List<DmFilter> lstFilter) {
        this.lstFilter = lstFilter;
    }

    public void addFilter(String columnName, DmEnum.DmDataType dataType, DmEnum.DmOperator operator, String value1, String value2, DmEnum.DmOperand operand) {
        DmFilter filter = new DmFilter();

        filter.setColumnName(columnName);
        filter.setDataType(dataType);
        filter.setOperator(operator);
        filter.setValue1(value1);
        filter.setValue2(value2);
        filter.setOperand(operand);
        lstFilter.add(filter);
    }

    public List<DmSort> getSort() {
        return lstSort;
    }

    public void setSort(List<DmSort> lstSort) {
        this.lstSort = lstSort;
    }

    public void addSort(String columnName, DmEnum.DmSortType sortType, String columnCaption) {
        DmSort sort = new DmSort();

        sort.setColumnCaption(columnCaption);
        sort.setColumnName(columnName);
        sort.setSortType(sortType);

        lstSort.add(sort);
    }

    public void addSort(String columnName, DmEnum.DmSortType sortType) {
        addSort(columnName, sortType, "");
    }

    public DmSQLWithParam prepareQuery(DmSQLWithParam sqlWithParam) {
        String sqlQuery, tempString, condition = "";
        DmFilter filter;
        int index = 0;
        String paramName;

        if (lstFilter != null) {
            for (int i = 0; i < lstFilter.size(); i++) {
                tempString = "";
                filter = lstFilter.get(i);
                if (!DmBaseShared.xNull(filter.getColumnName()).trim().equalsIgnoreCase("")) {
                    if (!tempString.equalsIgnoreCase("")) {
                        switch (filter.getOperand()) {
                            case AND:
                                tempString += " And ";
                                break;
                            case OR:
                                tempString += " Or ";
                                break;
                        }
                    }

//                    tempString += columns[filter.getColumnIndex() - 1].toString().trim() + " ";
                    tempString += filter.getColumnName() + " ";

                    switch (filter.getOperator()) {
                        case EQUAL:
                            index++;
                            paramName = "DmShared_Helper_PrepareQuery" + String.valueOf(index);
                            tempString += " = :" + paramName + " ";
                            sqlWithParam.setString(paramName, DmBaseShared.xNull(filter.getValue1()));
                            //sqlWithParam.addSQLParam(new DmSQLParam(paramName,DmBaseShared.xNull(filter.value1), DmDataType.STRING));
                            break;
                        case CONTAINS:
                            index++;
                            paramName = "DmShared_Helper_PrepareQuery" + String.valueOf(index);
                            tempString += " Like :" + paramName + " ";
                            sqlWithParam.setString(paramName, "%" + DmBaseShared.xNull(filter.getValue1()) + "%");
                            //sqlWithParam.addSQLParam(new DmSQLParam(paramName,"%" + DmBaseShared.xNull(filter.value1) + "%", DmDataType.STRING));
                            break;
                        case BEGINSWITH:
                            index++;
                            paramName = "DmShared_Helper_PrepareQuery" + String.valueOf(index);
                            tempString += " Like :" + paramName + " ";
                            sqlWithParam.setString(paramName, DmBaseShared.xNull(filter.getValue1()) + "%");
                            //sqlWithParam.addSQLParam(new DmSQLParam(paramName,DmBaseShared.xNull(filter.value1) + "%", DmDataType.STRING));
                            break;
                    }

                    condition += tempString;
                }
            }
        }

        //strSQL = "Select * From ( " + strQuery + ") As T ";
        sqlQuery = "Select * From ( " + sqlWithParam.getSQL() + ") As T ";
        if (!condition.equalsIgnoreCase("")) {
            sqlQuery += "Where " + condition;
        }
        sqlWithParam.setSQL(sqlQuery);

        return sqlWithParam;
    }

    public void positioning(int intRecordsFetched, boolean blnEOF) {
        pageing.positioning(intRecordsFetched, blnEOF);
    }
}
