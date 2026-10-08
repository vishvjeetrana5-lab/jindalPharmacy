package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DmSQLWithParam implements Serializable,IsSerializable{

    private String SQL = "";
    private List<DmSQLParam> sqlParam = new ArrayList<>();

    public DmSQLWithParam() {
    }

    public String getSQL() {
        return SQL;
    }

    public void setSQL(String SQL) {
        this.SQL = SQL;
    }

    public List<DmSQLParam> getSqlParam() {
        return sqlParam;
    }

//        public void setSqlParam(List<DmSQLParam> sqlParam) {
//            this.sqlParam = sqlParam;
//        }
//        
//        public void addSQLParam(DmSQLParam sqlParam) {
//            this.sqlParam.add(sqlParam);
//        }
    public void setString(String paramName, String paramValue) {
        DmSQLParam sqlParam = new DmSQLParam();
        sqlParam.setString(paramName, paramValue);
        this.sqlParam.add(sqlParam);
    }

    public void setStringList(String paramName, String paramValue) {
        DmSQLParam sqlParam = new DmSQLParam();
        sqlParam.setStringList(paramName, paramValue);
        this.sqlParam.add(sqlParam);
    }

    public void setDate(String paramName, Date paramValue) {
        DmSQLParam sqlParam = new DmSQLParam();
        sqlParam.setDate(paramName, paramValue);
        this.sqlParam.add(sqlParam);
    }

    public void setDouble(String paramName, Double paramValue) {
        DmSQLParam sqlParam = new DmSQLParam();
        sqlParam.setDouble(paramName, paramValue);
        this.sqlParam.add(sqlParam);
    }

    public void setInteger(String paramName, Integer paramValue) {
        DmSQLParam sqlParam = new DmSQLParam();
        sqlParam.setInteger(paramName, paramValue);
        this.sqlParam.add(sqlParam);
    }
    
    public void setTimestamp(String paramName, Timestamp paramValue) {
        DmSQLParam sqlParam = new DmSQLParam();
        sqlParam.setTimestamp(paramName, paramValue);
        this.sqlParam.add(sqlParam);
    }
}
