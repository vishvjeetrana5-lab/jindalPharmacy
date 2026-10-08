package dataman.dmBase.self.server;

import java.io.File;
import java.util.Objects;
import java.util.TreeMap;

public class DmCsvFileFormat_Mdl {

    public enum CsvContentType {
        Text,
        Numeric;
    }

    private File csvFile;
    private TreeMap<String, CsvContentType> columns;

    public DmCsvFileFormat_Mdl() {
        columns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public DmCsvFileFormat_Mdl(File csvFile) throws Exception {
        this();
        if (!csvFile.exists()) {
            throw new Exception("File : " + csvFile.getAbsolutePath() + " not found.");
        }
        this.csvFile = csvFile;
    }

    public File getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(File csvFile) {
        this.csvFile = csvFile;
    }

    public TreeMap<String, CsvContentType> getColumns() {
        return columns;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.csvFile);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DmCsvFileFormat_Mdl other = (DmCsvFileFormat_Mdl) obj;
        if (!Objects.equals(this.csvFile, other.csvFile)) {
            return false;
        }
        return true;
    }

}
