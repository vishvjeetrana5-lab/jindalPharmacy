package dataman.dmToolkit.dmImportData.server;

import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DmImportData_Server {

    public static boolean validateField(String fieldName, String fieldValue, boolean isMandatory,
            boolean isNumeric, Boolean isNegative, boolean isEmail, boolean isDate, boolean isTime,
            int maxLength, String lineNoDisplay, DmHandelFile handelFile) {
        boolean flag = true;

        try {
            if (isMandatory) {
                if (isNumeric) {
                    if (DmShared.vNull(fieldValue).doubleValue() == 0) {
                        flag = false;
                        handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is mandatory.");
                    }
                } else if (DmShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
                    flag = false;
                    handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is mandatory.");
                } else if (DmShared.dNull(fieldValue).trim().equalsIgnoreCase("") && isDate) {
                    flag = false;
                    handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is mandatory.");
                } else if (isTime) {
                    if (DmShared.isNumeric(fieldValue)) {
                        if (DmShared.vNull(fieldValue).doubleValue() > 23.59 && DmShared.vNull(fieldValue).doubleValue() < 0.0) {
                            flag = false;
                            handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " value between 00.00 to 23.59.");
                        }
                    } else {
                        flag = false;
                        handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is double value.");
                    }
                }
            }

            if (isDate) {
                if (!DmShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
                    if (DmShared.dNull(fieldValue).trim().equalsIgnoreCase("")) {
                        flag = false;
                        handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is not valid.");
                    }
                }
            }
            if (isNumeric) {
                if (isNegative != null) {
                    if (isNegative) {
                        if (DmShared.vNull(fieldValue).doubleValue() > 0) {
                            flag = false;
                            handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " positive not allowed.");
                        }
                    } else {
                        if (DmShared.vNull(fieldValue).doubleValue() < 0) {
                            flag = false;
                            handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is negative not allowed.");
                        }
                    }
                }
            }

            if (isTime) {
                if (DmShared.isNumeric(fieldValue)) {
                    if (DmShared.vNull(fieldValue).doubleValue() > 23.59 && DmShared.vNull(fieldValue).doubleValue() < 0.0) {
                        flag = false;
                        handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " value between 00.00 to 23.59.");
                    }
                } else {
                    flag = false;
                    handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is double value.");
                }
            }
            if (isEmail && !DmShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
                String eMailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                if (!DmShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
                    if (!fieldValue.matches(eMailRegex)) {
                        flag = false;
                        handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " invalid eMail Id.");
                    }
                }
            }

            if (maxLength != 0) {
                if (DmShared.iNull(fieldValue.length()).longValue() > maxLength) {
                    flag = false;
                    handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is exceed limit.");
                }
            }
        } catch (Exception e) {
            try {
                flag = false;
                handelFile.writeLine(e.getMessage());
            } catch (Exception ex) {
            }
        }

        return flag;
    }

    public static HashMap<String, String> getCSVFormats(List<CSVData> importDataLst) {
        HashMap<String, String> hmReturn = new LinkedHashMap<String, String>();
        String header = "", sample = "", instruction = "", quote = "\"";
        for (CSVData csv : importDataLst) {

            if (DmShared.xNull(header).trim().equalsIgnoreCase("")) {
                header = csv.getHeader();
            } else {
                header += "," + csv.getHeader();
            }

            if (DmShared.xNull(sample).trim().equalsIgnoreCase("")) {
                sample = quote + csv.getSample() + quote;
            } else {
                sample += "," + quote + csv.getSample() + quote;
            }

            if (DmShared.xNull(instruction).trim().equalsIgnoreCase("")) {
                instruction = quote + csv.getInstruction() + quote;
            } else {
                instruction += "," + quote + csv.getInstruction() + quote;
            }
        }
        hmReturn.put("sample", sample);
        hmReturn.put("header", header);
        hmReturn.put("instruction", instruction);
        return hmReturn;
    }

    public static int checkHeader(String[] csvFileFormat, DmCsvReader csvReader, DmHandelFile handleFile) throws Exception {
        boolean isFound;
        int status = 0;
        try {
            for (String headerName : csvFileFormat) {
                isFound = false;
                for (String columnName : csvReader.getHeaders()) {
                    if (columnName.equalsIgnoreCase(headerName)) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    throw new Exception("Invalid file format: " + headerName + " field not found.");
                }
            }
        } catch (Exception ex) {
            status = DmImportStatus.ERROR.getId();
            handleFile.writeLine(ex.getMessage());
        }
        return status;
    }
}
