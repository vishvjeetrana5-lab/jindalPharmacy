package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum;
import java.io.Serializable;

public class DmFilter implements Serializable, IsSerializable {

    private String columnName;
    private DmEnum.DmDataType dataType;
    private DmEnum.DmOperator operator;
    private String value1;
    private String value2;
    private DmEnum.DmOperand operand;

    public DmFilter() {
    }

    public DmEnum.DmDataType getDataType() {
        return dataType;
    }

    public void setDataType(DmEnum.DmDataType dataType) {
        this.dataType = dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public DmEnum.DmOperator getOperator() {
        return operator;
    }

    public void setOperator(DmEnum.DmOperator operator) {
        this.operator = operator;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public DmEnum.DmOperand getOperand() {
        return operand;
    }

    public void setOperand(DmEnum.DmOperand operand) {
        this.operand = operand;
    }
}
