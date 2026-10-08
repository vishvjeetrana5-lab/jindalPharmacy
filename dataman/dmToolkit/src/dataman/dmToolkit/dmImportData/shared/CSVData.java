package dataman.dmToolkit.dmImportData.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class CSVData implements Serializable, IsSerializable {

    private String header, sample, instruction;

    public CSVData() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
