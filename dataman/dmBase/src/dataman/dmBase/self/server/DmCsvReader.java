package dataman.dmBase.self.server;

import com.opencsv.CSVReader;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DmCsvReader {

    private File fileObject;
//    private CSVReaderBuilder builder;
    private boolean readHeader = true;
    private List<DmDynamicGridRow> lstRows = new ArrayList<>();
    private List<String> headers = new ArrayList<>();

    public interface Callback {

        public boolean getRow(int lineNo, List<String> headers, DmDynamicGridRow row);

        public boolean getHeaders(List<String> headers);
    }

    public DmCsvReader(String fullFilePath) throws Exception {
        this.fileObject = new File(fullFilePath);
        if (!this.fileObject.exists()) {
            throw new Exception("File not exists.");
        }
    }

    public DmCsvReader(File file) throws Exception {
        this.fileObject = file;
        if (!this.fileObject.exists()) {
            throw new Exception("File not exists.");
        }
    }

    public boolean isHeaderReadEnabled() {
        return this.readHeader;
    }

    public void readLines(Callback callback) throws Exception {
        readLines(callback, false);
    }

    public void readLines(Callback callback, boolean strictQuotes) throws Exception {
        CSVReader reader = null;
        FileInputStream fReader = null;
        try {
            if (callback == null) {
                lstRows.clear();
            }
            headers.clear();
            if (this.fileObject == null) {
                throw new Exception("File not provided");
            }
            fReader = new FileInputStream(fileObject);
            if (fReader == null) {
                throw new Exception("File not found.");
            }
//            this.builder = new CSVReaderBuilder(fReader);
//            reader = this.builder.build();            
            if (strictQuotes) {
                reader = new CSVReader(new InputStreamReader(fReader), ',', '"', true);
            } else {
                reader = new CSVReader(new InputStreamReader(fReader), ',', '"', '|');
            }
            String[] lineRead = null;
            boolean firstLine = true;
            headers = new ArrayList<>();
            int lineNo = 0;
            while ((lineRead = reader.readNext()) != null) {
                DmDynamicGridRow dRow = new DmDynamicGridRow();
                if (firstLine) {
                    if (this.readHeader) {
                        for (int i = 0; i < lineRead.length; i++) {
                            headers.add(DmBaseShared.removeSpace(DmBaseShared.xNull(lineRead[i])).trim());
                        }
                    } else if (!this.readHeader) {
                        for (int i = 0; i < lineRead.length; i++) {
                            headers.add("value" + i);
                        }
                    }
                } else {
                    for (int i = 0; i < lineRead.length; i++) {
                        dRow.getCells().put(headers.get(i).trim().toLowerCase(), DmBaseShared.xNull(lineRead[i]));
                    }
                }
                if (!firstLine) {
                    if (callback == null) {
                        this.lstRows.add(dRow);
                    } else if (callback != null) {
                        if (!callback.getRow(lineNo, headers, dRow)) {
                            return;
                        }
                    }
                } else {
                    if (callback != null) {
                        if (!callback.getHeaders(headers)) {
                            return;
                        }
                    }
                }
                lineNo += 1;
                firstLine = false;
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (reader != null) {
                reader.close();
                reader = null;
            }
//            if (this.reader != null) {
//                this.reader = null;
//                this.builder = null;
//            }
            if (fReader != null) {
                fReader.close();
                fReader = null;
            }
        }
    }

//    public void readLines(Callback callback) throws Exception {
//        CSVReader reader = null;
//        FileReader fReader = null;
//        try {
//            if (callback == null) {
//                lstRows.clear();
//            }
//            headers.clear();
//            if (this.fileObject == null) {
//                throw new Exception("File not provided");
//            }
//            fReader = new FileReader(fileObject);
//            if (fReader == null) {
//                throw new Exception("File not found.");
//            }
//            this.builder = new CSVReaderBuilder(fReader);
//            reader = this.builder.build();
//            String[] lineRead = null;
//            boolean firstLine = true;
//            headers = new ArrayList<>();
//            int lineNo = 0;
//            while ((lineRead = reader.readNext()) != null) {
//                DmDynamicGridRow dRow = new DmDynamicGridRow();
//                if (firstLine) {
//                    if (this.readHeader) {
//                        for (int i = 0; i < lineRead.length; i++) {
//                            headers.add(DmBaseShared.removeSpace(DmBaseShared.xNull(lineRead[i])).toLowerCase().trim());
//                        }
//                    } else if (!this.readHeader) {
//                        for (int i = 0; i < lineRead.length; i++) {
//                            headers.add("value" + i);
//                        }
//                    }
//                } else {
//                    for (int i = 0; i < lineRead.length; i++) {
//                        dRow.getCells().put(headers.get(i).trim(), DmBaseShared.removeSpace(DmBaseShared.xNull(lineRead[i])).trim());
//                    }
//                }
//                if (!firstLine) {
//                    if (callback == null) {
//                        this.lstRows.add(dRow);
//                    } else if (callback != null) {
//                        if (!callback.getRow(lineNo, headers, dRow)) {
//                            return;
//                        }
//                    }
//                }
//                lineNo += 1;
//                firstLine = false;
//            }
//        } catch (Exception ex) {
//            throw ex;
//        } finally {
//            if (reader != null) {
//                reader.close();
//                reader = null;
//            }
//            if (this.builder != null) {
//                this.builder = null;
//                this.builder = null;
//            }
//            if (fReader != null) {
//                fReader.close();
//                fReader = null;
//            }
//        }
//    }
    public void readLines() throws Exception {
        readLines(null);
    }

    public List<DmDynamicGridRow> getRows() {
        return this.lstRows;
    }

    public List<String> getHeaders() {
        return this.headers;
    }
}
