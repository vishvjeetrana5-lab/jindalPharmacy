package dataman.webJobs.smsTemplate.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable,IsSerializable{

    private String tempName;
    private String templateGroupName;
    private DmEnum.DmWebJobDataSources datasource;
    private String functionName;
    private String msg;

    public Find_Mdl() {
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getTemplateGroupName() {
        return templateGroupName;
    }

    public void setTemplateGroupName(String templateGroupName) {
        this.templateGroupName = templateGroupName;
    }

    public DmEnum.DmWebJobDataSources getDatasource() {
        return datasource;
    }

    public String getDataSourceValue() {
        return this.datasource.getSourceName();
    }

    public void setDatasource(DmEnum.DmWebJobDataSources datasource) {
        this.datasource = datasource;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
