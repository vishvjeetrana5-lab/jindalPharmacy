package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class DmSQLParam implements Serializable,IsSerializable{

    private DmEnum.DmDataType dataType;
    private String paramStringValue;
    private Date paramDateValue;
    private Double paramDoubleValue;
    private Integer paramIntegerValue;
    private String paramName;
    private Timestamp paramTimestampValue;

    public DmSQLParam() {
    }

//        public DmSQLParam(String paramName, String paramValue, DmDataType dataType) {
//            this.dataType = dataType;
//            this.paramStringValue = paramValue;
//            this.paramName = paramName;
//        }
    public String getParamName() {
        return paramName;
    }

    public DmEnum.DmDataType getDataType() {
        return dataType;
    }

    public String getString() {
        return paramStringValue;
    }

    public Date getDate() {
        return paramDateValue;
    }

    public Double getDouble() {
        return paramDoubleValue;
    }

    public Integer getInteger() {
        return paramIntegerValue;
    }
    
    public Timestamp getTimestamp() {
        return paramTimestampValue;
    }

    public void setString(String paramName, String paramValue) {
        this.paramName = paramName;
        this.paramStringValue = paramValue;
        this.paramTimestampValue = null;
        this.paramDateValue = null;
        this.paramDoubleValue = null;
        this.paramIntegerValue = null;
        this.dataType = dataType.STRING;
    }

    public void setStringList(String paramName, String paramValue) {
        this.paramName = paramName;
        this.paramStringValue = paramValue;
        this.paramTimestampValue = null;
        this.paramDateValue = null;
        this.paramDoubleValue = null;
        this.paramIntegerValue = null;
        this.dataType = dataType.STRINGLIST;
    }

    public void setDate(String paramName, Date paramValue) {
        this.paramName = paramName;
        this.paramStringValue = null;
        this.paramTimestampValue = null;
        this.paramDateValue = paramValue;
        this.paramDoubleValue = null;
        this.paramIntegerValue = null;
        this.dataType = dataType.DATETIME;
    }

    public void setDouble(String paramName, double paramValue) {
        this.paramName = paramName;
        this.paramStringValue = null;
        this.paramTimestampValue = null;
        this.paramDateValue = null;
        this.paramDoubleValue = paramValue;
        this.paramIntegerValue = null;
        this.dataType = dataType.DOUBLE;
    }

    public void setInteger(String paramName, int paramValue) {
        this.paramName = paramName;
        this.paramStringValue = null;
        this.paramTimestampValue = null;
        this.paramDateValue = null;
        this.paramDoubleValue = null;
        this.paramIntegerValue = paramValue;
        this.dataType = dataType.INTEGER;
    }
    
    public void setTimestamp(String paramName, Timestamp paramValue) {
        this.paramName = paramName;
        this.paramStringValue = null;
        this.paramTimestampValue = paramValue;
        this.paramDateValue = null;
        this.paramDoubleValue = null;
        this.paramIntegerValue = null;
        this.dataType = dataType.TIMESTAMP;
    }
}
