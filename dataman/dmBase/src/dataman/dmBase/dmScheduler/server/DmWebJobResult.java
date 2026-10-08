package dataman.dmBase.dmScheduler.server;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmWebJobResult<T> implements Serializable, IsSerializable {

    private Throwable exeExcep;
    private boolean boolStatus;
    private T obj;

    public Throwable getExeExcep() {
        return exeExcep;
    }

    public void setExeExcep(Exception exeExcep) {
        this.exeExcep = exeExcep;
    }

    public boolean isBoolStatus() {
        return boolStatus;
    }

    public void setBoolStatus(boolean boolStatus) {
        this.boolStatus = boolStatus;
    }

    public T getResult() {
        return obj;
    }

    public void setResult(T obj) {
        this.obj = obj;
    }
}
