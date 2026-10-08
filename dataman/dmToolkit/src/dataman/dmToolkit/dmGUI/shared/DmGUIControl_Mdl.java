package dataman.dmToolkit.dmGUI.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum;
import java.io.Serializable;

public class DmGUIControl_Mdl implements Serializable, IsSerializable {

    private String saveInFieldName;
    private String fieldCaption;
    private DmEnum.DmDataType dataType;
    private String controlWidth;
    private String controlStyle;
    private String captionWidth;
    private String captionStyle;
    private String maxLength;
    private boolean mandatory;
    private boolean allowNegative;
    private boolean copy;
    private String allowDecimalPlaces;
    private String defaultText;
    private String defaultCode;
    private String viewFieldText;
    private String viewFieldCode;
    private byte[] helpSQLQuery;
    private String helpWidth;
    private String helpRowPerPage;
    private String customFieldName;
    private String fieldName;

    public DmGUIControl_Mdl() {
    }

    public String getControlStyle() {
        return controlStyle;
    }

    public void setControlStyle(String controlStyle) {
        this.controlStyle = controlStyle;
    }

    public String getCaptionStyle() {
        return captionStyle;
    }

    public void setCaptionStyle(String captionStyle) {
        this.captionStyle = captionStyle;
    }

    public String getFieldCaption() {
        return fieldCaption;
    }

    public void setFieldCaption(String fieldCaption) {
        this.fieldCaption = fieldCaption;
    }

    public DmEnum.DmDataType getDataType() {
        return dataType;
    }

    public void setDataType(DmEnum.DmDataType dataType) {
        this.dataType = dataType;
    }

    public String getControlWidth() {
        return controlWidth;
    }

    public void setControlWidth(String controlWidth) {
        this.controlWidth = controlWidth;
    }

    public String getCaptionWidth() {
        return captionWidth;
    }

    public void setCaptionWidth(String captionWidth) {
        this.captionWidth = captionWidth;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isAllowNegative() {
        return allowNegative;
    }

    public void setAllowNegative(boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public String getAllowDecimalPlaces() {
        return allowDecimalPlaces;
    }

    public void setAllowDecimalPlaces(String allowDecimalPlaces) {
        this.allowDecimalPlaces = allowDecimalPlaces;
    }

    public String getSaveInFieldName() {
        return saveInFieldName;
    }

    public void setSaveInFieldName(String saveInFieldName) {
        this.saveInFieldName = saveInFieldName;
    }

    public String getDefaultCode() {
        return defaultCode;
    }

    public void setDefaultCode(String defaultCode) {
        this.defaultCode = defaultCode;
    }

    public String getViewFieldText() {
        return viewFieldText;
    }

    public void setViewFieldText(String viewFieldText) {
        this.viewFieldText = viewFieldText;
    }

    public String getViewFieldCode() {
        return viewFieldCode;
    }

    public void setViewFieldCode(String viewFieldCode) {
        this.viewFieldCode = viewFieldCode;
    }

    public byte[] getHelpSQLQueryBytes() {
        return helpSQLQuery;
    }

    public String getHelpSQLQuery() {
        return new String(helpSQLQuery);
    }

    public void setHelpSQLQuery(String helpSQLQuery) {
        this.helpSQLQuery = helpSQLQuery.getBytes();
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getHelpWidth() {
        return helpWidth;
    }

    public void setHelpWidth(String helpWidth) {
        this.helpWidth = helpWidth;
    }

    public String getHelpRowPerPage() {
        return helpRowPerPage;
    }

    public void setHelpRowPerPage(String helpRowPerPage) {
        this.helpRowPerPage = helpRowPerPage;
    }

    public String getCustomFieldName() {
        return customFieldName;
    }

    public void setCustomFieldName(String customFieldName) {
        this.customFieldName = customFieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}
