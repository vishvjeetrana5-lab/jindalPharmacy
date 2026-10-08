package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class RecId implements Serializable,IsSerializable{

    String recIdValue;
    Long counter;
//    String Suffix;
    String prefix;

    public RecId() {

    }

    public RecId(String recIdValue, Long counter) {
        this.recIdValue = recIdValue;
        this.counter = counter;
    }

    public String getRecIdValue() {
        return recIdValue;
    }

    public void setRecIdValue(String recIdValue) {
        this.recIdValue = recIdValue;
    }

    public Long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }

//    public String getSuffix() {
//        return Suffix;
//    }
//
//    public void setSuffix(String Suffix) {
//        this.Suffix = Suffix;
//    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
