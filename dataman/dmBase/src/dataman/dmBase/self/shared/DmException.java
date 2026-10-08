package dataman.dmBase.self.shared;

public class DmException extends UnsupportedOperationException {

    private int errorNo = 0;
    private String errorSource = "";
    private String errorSourceCode = "";

    public DmException(int errorNo, String message) {
        super(message);
        this.errorNo = errorNo;
    }

    public DmException(int errorNo, String errorSource, String errorSourceCode, String message) {
        super(message);
        this.errorNo = errorNo;
        this.errorSource = errorSource;
        this.errorSourceCode = errorSourceCode;
    }

    public int getErrorNo() {
        return errorNo;
    }

    public String getErrorSourceCode() {
        return errorSourceCode;
    }

    public String getErrorSource() {
        return errorSource;
    }
}
