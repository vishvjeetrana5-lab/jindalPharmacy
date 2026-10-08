package dataman.dmToolkit.singleLineMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.SQLDataType;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class SingleLineMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private byte[] tableName;
    private byte[] fieldCode;
    private byte[] fieldText;
    private byte[] fieldTitle;
    private boolean haveSiteList;
    private boolean haveSiteCode;
    private SQLDataType fieldCodeDataType;
    private String fileName;

    public SingleLineMaster_Mdl() {

    }

    public String getTableName() {
        return new String(tableName);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName.getBytes();
    }

    public String getFieldTitle() {
        return new String(fieldTitle);
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle.getBytes();
    }

    public String getFieldCode() {
        return new String(fieldCode);
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode.getBytes();
    }

    public String getFieldText() {
        return new String(fieldText);
    }

    public void setFieldText(String fieldText) {
        this.fieldText = fieldText.getBytes();
    }

    public boolean isHaveSiteList() {
        return haveSiteList;
    }

    public void setHaveSiteList(boolean haveSiteList) {
        this.haveSiteList = haveSiteList;
    }

    public boolean isHaveSiteCode() {
        return haveSiteCode;
    }

    public void setHaveSiteCode(boolean haveSiteCode) {
        this.haveSiteCode = haveSiteCode;
    }

    public SQLDataType getFieldCodeDataType() {
        return fieldCodeDataType;
    }

    public void setFieldCodeDataType(SQLDataType fieldCodeDataType) {
        this.fieldCodeDataType = fieldCodeDataType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
