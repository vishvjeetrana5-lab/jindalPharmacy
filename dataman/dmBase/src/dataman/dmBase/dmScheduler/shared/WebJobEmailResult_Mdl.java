package dataman.dmBase.dmScheduler.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WebJobEmailResult_Mdl implements Serializable, IsSerializable {

    private String fromEmailId;
    private String toEmailId;
    private String subject;
    private String bodyText;
    private String uniqueId;
    private List<String> lstBcc = new ArrayList<>();
    private List<String> lstCc = new ArrayList<>();
    private List<String> lstAttachments = new ArrayList<>();

    public WebJobEmailResult_Mdl() {
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getFromEmailId() {
        return fromEmailId;
    }

    public void setFromEmailId(String fromEmailId) {
        this.fromEmailId = fromEmailId;
    }

    public String getToEmailId() {
        return toEmailId;
    }

    public void setToEmailId(String toEmailId) {
        this.toEmailId = toEmailId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public List<String> getLstAttachments() {
        return lstAttachments;
    }

    public List<String> getLstBcc() {
        return lstBcc;
    }

    public List<String> getLstCc() {
        return lstCc;
    }

    public void setBcc(String bcc) {
        this.lstBcc.clear();
        String[] tmpBcc = bcc.split("\\|");
        for (int i = 0; i < tmpBcc.length; i++) {
            this.lstBcc.add(tmpBcc[i].trim());
        }
    }

    public void setCc(String cc) {
        this.lstCc.clear();
        String[] tmpCc = cc.split("\\|");
        for (int i = 0; i < cc.length(); i++) {
            this.lstCc.add(tmpCc[i].trim());
        }
    }

    public String getFromUserName() {
        return this.fromEmailId;
//        return (this.fromEmailId.indexOf("@") >= 0 ? this.fromEmailId.substring(0, this.fromEmailId.indexOf("@")) : this.fromEmailId);
    }

    public String getToUserName() {
        return this.toEmailId;
//        return (this.toEmailId.indexOf("@") >= 0 ? this.toEmailId.substring(0, this.toEmailId.indexOf("@")) : this.toEmailId);
    }
}
