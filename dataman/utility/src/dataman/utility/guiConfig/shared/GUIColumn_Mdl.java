package dataman.utility.guiConfig.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GUIColumn_Mdl extends KeyField implements Serializable, IsSerializable {
// sharedCode as ColSrNo, SharedName as saveInField

    private String fieldCaption, fieldDataTypeTag, fieldDataTypeText, mandatoryTag, mandatoryText,
            maxLength, captionWidth, controlWidth, negativeTag, negativeText, decimalText, defaultText,
            defaultCode, viewFieldCode, viewFieldText, helpRow, helpWidth, helpQuery, rowSno, customFieldName,
            captionStyle, controlStyle, fieldName, isCopyTag, isCopyText;

    public GUIColumn_Mdl() {
    }

    public String getFieldCaption() {
        return fieldCaption;
    }

    public void setFieldCaption(String fieldCaption) {
        this.fieldCaption = fieldCaption;
    }

    public String getFieldDataTypeTag() {
        return fieldDataTypeTag;
    }

    public void setFieldDataTypeTag(String fieldDataTypeTag) {
        this.fieldDataTypeTag = fieldDataTypeTag;
    }

    public String getFieldDataTypeText() {
        return fieldDataTypeText;
    }

    public void setFieldDataTypeText(String fieldDataTypeText) {
        this.fieldDataTypeText = fieldDataTypeText;
    }

    public String getMandatoryTag() {
        return mandatoryTag;
    }

    public void setMandatoryTag(String mandatoryTag) {
        this.mandatoryTag = mandatoryTag;
    }

    public String getMandatoryText() {
        return mandatoryText;
    }

    public void setMandatoryText(String mandatoryText) {
        this.mandatoryText = mandatoryText;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getCaptionWidth() {
        return captionWidth;
    }

    public void setCaptionWidth(String captionWidth) {
        this.captionWidth = captionWidth;
    }

    public String getControlWidth() {
        return controlWidth;
    }

    public void setControlWidth(String controlWidth) {
        this.controlWidth = controlWidth;
    }

    public String getNegativeTag() {
        return negativeTag;
    }

    public void setNegativeTag(String negativeTag) {
        this.negativeTag = negativeTag;
    }

    public String getNegativeText() {
        return negativeText;
    }

    public void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
    }

    public String getDecimalText() {
        return decimalText;
    }

    public void setDecimalText(String decimalText) {
        this.decimalText = decimalText;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getDefaultCode() {
        return defaultCode;
    }

    public void setDefaultCode(String defaultCode) {
        this.defaultCode = defaultCode;
    }

    public String getViewFieldCode() {
        return viewFieldCode;
    }

    public void setViewFieldCode(String viewFieldCode) {
        this.viewFieldCode = viewFieldCode;
    }

    public String getViewFieldText() {
        return viewFieldText;
    }

    public void setViewFieldText(String viewFieldText) {
        this.viewFieldText = viewFieldText;
    }

    public String getHelpRow() {
        return helpRow;
    }

    public void setHelpRow(String helpRow) {
        this.helpRow = helpRow;
    }

    public String getHelpWidth() {
        return helpWidth;
    }

    public void setHelpWidth(String helpWidth) {
        this.helpWidth = helpWidth;
    }

    public String getHelpQuery() {
        return helpQuery;
    }

    public void setHelpQuery(String helpQuery) {
        this.helpQuery = helpQuery;
    }

    public String getRowSno() {
        return rowSno;
    }

    public void setRowSno(String rowSno) {
        this.rowSno = rowSno;
    }

    public String getCustomFieldName() {
        return customFieldName;
    }

    public void setCustomFieldName(String customFieldName) {
        this.customFieldName = customFieldName;
    }

    public String getCaptionStyle() {
        return captionStyle;
    }

    public void setCaptionStyle(String captionStyle) {
        this.captionStyle = captionStyle;
    }

    public String getControlStyle() {
        return controlStyle;
    }

    public void setControlStyle(String controlStyle) {
        this.controlStyle = controlStyle;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getIsCopyTag() {
        return isCopyTag;
    }

    public void setIsCopyTag(String isCopyTag) {
        this.isCopyTag = isCopyTag;
    }

    public String getIsCopyText() {
        return isCopyText;
    }

    public void setIsCopyText(String isCopyText) {
        this.isCopyText = isCopyText;
    }

    public GUIColumn_Mdl clone() {
        GUIColumn_Mdl local = new GUIColumn_Mdl();
        super.copy(local);
        local.setCaptionWidth(this.getCaptionWidth());
        local.setControlWidth(this.getControlWidth());
        local.setDecimalText(this.getDecimalText());
        local.setDefaultCode(this.getDefaultCode());
        local.setDefaultText(this.getDefaultText());
        local.setFieldCaption(this.getFieldCaption());
        local.setFieldDataTypeTag(this.getFieldDataTypeTag());
        local.setFieldDataTypeText(this.getFieldDataTypeText());
        local.setHelpQuery(this.getHelpQuery());
        local.setHelpRow(this.getHelpRow());
        local.setHelpWidth(this.getHelpWidth());
        local.setMandatoryTag(this.getMandatoryTag());
        local.setMandatoryText(this.getMandatoryText());
        local.setMaxLength(this.getMaxLength());
        local.setNegativeTag(this.getNegativeTag());
        local.setNegativeText(this.getNegativeText());
        local.setRowSno(this.getRowSno());
        local.setViewFieldCode(this.getViewFieldCode());
        local.setViewFieldText(this.getViewFieldText());
        local.setCustomFieldName(this.getCustomFieldName());
        local.setCaptionStyle(this.getCaptionStyle());
        local.setControlStyle(this.getControlStyle());
        local.setFieldName(this.getFieldName());
        local.setIsCopyTag(this.getIsCopyTag());
        local.setIsCopyText(this.getIsCopyText());

        return local;
    }

}
