package dataman.dmToolkit.modifyReason.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ModifyData_Mdl implements Serializable, IsSerializable {

    private String keyId_1;
    private String keyName_1;
    private String keyDescription_1;
    private String keyId_2;
    private String keyName_2;
    private String keyDescription_2;
    private String tableName;

    private String vrTypeSName;
    private String vrTypeDescription;
    private String moduleName;
    private String menuName;
    private String controlText;

    private String oldData;
    private String newData;
    private String modelClassName;

    public ModifyData_Mdl() {
    }

    public String getKeyId_1() {
        return keyId_1;
    }

    public void setKeyId_1(String keyId_1) {
        this.keyId_1 = keyId_1;
    }

    public String getKeyName_1() {
        return keyName_1;
    }

    public void setKeyName_1(String keyName_1) {
        this.keyName_1 = keyName_1;
    }

    public String getKeyDescription_1() {
        return keyDescription_1;
    }

    public void setKeyDescription_1(String keyDescription_1) {
        this.keyDescription_1 = keyDescription_1;
    }

    public String getKeyId_2() {
        return keyId_2;
    }

    public void setKeyId_2(String keyId_2) {
        this.keyId_2 = keyId_2;
    }

    public String getKeyName_2() {
        return keyName_2;
    }

    public void setKeyName_2(String keyName_2) {
        this.keyName_2 = keyName_2;
    }

    public String getKeyDescription_2() {
        return keyDescription_2;
    }

    public void setKeyDescription_2(String keyDescription_2) {
        this.keyDescription_2 = keyDescription_2;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getVrTypeSName() {
        return vrTypeSName;
    }

    public void setVrTypeSName(String vrTypeSName) {
        this.vrTypeSName = vrTypeSName;
    }

    public String getVrTypeDescription() {
        return vrTypeDescription;
    }

    public void setVrTypeDescription(String vrTypeDescription) {
        this.vrTypeDescription = vrTypeDescription;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getControlText() {
        return controlText;
    }

    public void setControlText(String controlText) {
        this.controlText = controlText;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public String getModelClassName() {
        return modelClassName;
    }

    public void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName;
    }
}
