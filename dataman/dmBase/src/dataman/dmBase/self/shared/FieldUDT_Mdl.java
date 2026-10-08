package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class FieldUDT_Mdl implements Serializable,IsSerializable{

    private String fieldCaption;
    private String text;
    private String tag;
    private String isMandatory;

    public FieldUDT_Mdl() {

    }

    public FieldUDT_Mdl(String caption, String text, String tag) {
        this.fieldCaption = caption;
        this.text = text;
        this.tag = tag;
    }

    public String getFieldCaption() {
        return fieldCaption;
    }

    public void setFieldCaption(String fieldCaption) {
        this.fieldCaption = fieldCaption;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

}
