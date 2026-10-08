package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RefferedMaster implements DmImportData_Interface {

    class Model {

        private String code, name, shortName, departmentName, specialization, dtAnniversary,
                dtBirth, email, mobile, mciReg, chamberNo, displayName, remark;

        public Model() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public String getDtAnniversary() {
            return dtAnniversary;
        }

        public void setDtAnniversary(String dtAnniversary) {
            this.dtAnniversary = dtAnniversary;
        }

        public String getDtBirth() {
            return dtBirth;
        }

        public void setDtBirth(String dtBirth) {
            this.dtBirth = dtBirth;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMciReg() {
            return mciReg;
        }

        public void setMciReg(String mciReg) {
            this.mciReg = mciReg;
        }

        public String getChamberNo() {
            return chamberNo;
        }

        public void setChamberNo(String chamberNo) {
            this.chamberNo = chamberNo;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public RefferedMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("Initial");
        node.setSample("Dr.");
        node.setInstruction("{mandatory;alphanumeric;5c,Dr./'M/s'}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("name");
        node.setSample("Deshraj Gurjar");
        node.setInstruction("{unique;mandatory;alphanumeric;100c,without initial 'Dr.'}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("displayName");
        node.setSample("Deshraj Gurjar");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("shortName");
        node.setSample("DeshG");
        node.setInstruction("{unique;mandatory;alphanumeric;25c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("department");
        node.setSample("Neporlogy");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("specilization");
        node.setSample("Nepro");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("anniversaryDate");
        node.setSample("22-June-2014");
        node.setInstruction("{alphanumeric;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("dateOfBirth");
        node.setSample("28-Oct-1985");
        node.setInstruction("{alphanumeric;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("eMail");
        node.setSample("drdeshraj@regency.in");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("mobile");
        node.setSample("9876543210");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("MCIRegNo");
        node.setSample("123456ABC");
        node.setInstruction("{alphanumeric;25c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("remark");
        node.setSample("");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Referred Master");
        fileDetail.setServerClassName(RefferedMaster.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        Connection cnTemp = null;
        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue, acGroupRefferedCode, filterNature;
        String[] csvFileFormat;
        Object objValue;
        DmImportStatus importStatus = null;
        int rtnStatus = 0, status = 0;

        try {
            this.rss = rss;

            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            acGroupRefferedCode = DmServer.getCodeViaManualCode("1", "1", "enviroCompany", "acGroupReferredCode", cnTemp);
            if (DmShared.iNull(acGroupRefferedCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Define A/c Group for reffered consultant.");
            }
            filterNature = DmServer.getCodeViaManualCode("RC", "manualCode", "subgroupNature", "Code", cnTemp);
            if (DmShared.iNull(filterNature).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Reffered (Consultant) 'RC' nature not exists.");
            }

            cnTemp.close();
            cnTemp.close();

            if (DmShared.xNull(importData.getDataFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Data file path can not be blank.");
            } else {
                csvFileFormat = ImportData_Server.getCSVFormats(importData.getHmCSV()).get("header").split(",");
                dataFile = new File(importData.getDataFilePath());
                if (dataFile.exists()) {
                    csvReader = new DmCsvReader(dataFile);
                    csvReader.readLines();
                    status = ImportData_Server.checkHeader(csvFileFormat, csvReader, handelFileLog);
                    if (status == DmImportStatus.ERROR.getId()) {
                        throw new DmException(0, "");
                    }
                } else {
                    status = DmImportStatus.ERROR.getId();
                    throw new Exception("Data file not found.");
                }

                for (int i = 0; i < csvReader.getRows().size(); i++) {
                    lineNoDisplay = "Line " + String.valueOf(i + 1) + ": ";
                    dynamicGridRow = csvReader.getRows().get(i);
                    mrTemp = new Model();
                    isFound = true;

                    for (String headerName : csvFileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }
                        if (headerName.trim().equalsIgnoreCase("name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("displayName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDisplayName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("shortName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 25, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setShortName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("department")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDepartmentName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("specilization")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSpecialization(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("anniversaryDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 0, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDtAnniversary(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("dateOfBirth")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 0, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDtAnniversary(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("eMail")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, true, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmail(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("mobile")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMobile(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("MCIRegNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 25, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMciReg(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("remark")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRemark(fieldValue);
                            }
                        }
                    }

                    if (isFound) {
                        status = saveRefferedConsultant(mrTemp, acGroupRefferedCode, filterNature);
                    }
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                }
            }
        } catch (Exception e) {

            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                handelFileLog.writeLine(e.getMessage());
            } catch (Exception ex) {

            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
                if (csvReader != null) {
                    csvReader = null;
                }
                if (dataFile != null) {
                    dataFile = null;
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return importStatus;
    }

    private int saveRefferedConsultant(Model mrTemp, String acGroupReffered, String filterNature) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, department = "", specialization = "";
        int rtnStatus = 0, status = 0;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setDisplayName(DmShared.xNull(mrTemp.getDisplayName()).trim().equalsIgnoreCase("") ? mrTemp.getName() : DmShared.removeSpace(mrTemp.getDisplayName()));

            mrTemp.setCode(DmServer.getSerialNoBigInt(rss, "subGroup", cnTemp));

            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            //================================== END =====================================================
            //================================== GET DEPARTMENT CODE =====================================
            sqlQuery = "SELECT	dm.subCode  "
                    + "FROM departmentMast dm "
                    + "left join subgroup sg on dm.subCode = sg.subCode "
                    + "WHERE sg.name = :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getDepartmentName()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                department = DmShared.iNull(rsTemp.getString("subCode")).toString();
            }
            psTemp.close();
            rsTemp.close();

            if (DmShared.iNull(department).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, mrTemp.getDepartmentName() + " department not exists");
            }
            //================================== END =====================================================

            //================================== GET SPECIALIZATION CODE =================================
            specialization = DmServer.getCodeViaManualCode(mrTemp.getSpecialization(), "name", "consultantSpeciMast", "code", cnTemp);
            if (DmShared.iNull(specialization).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, mrTemp.getSpecialization() + " specialization not exists");
            }
            //================================== END =====================================================

            //================================== CHECK FOR DUPLICATE MANUAL CODE =========================
            sqlQuery = " SELECT COUNT(sg.manualCode) AS cnt FROM subgroup sg "
                    + " WHERE sg.manualCode = :shortName AND sg.subCode <> :code AND IsNULL(sg.manualCode, '') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException(mrTemp.getShortName() + " short Name is already exists.");
                }
            }
            psTemp.close();
            rsTemp.close();
            //================================== END =====================================================

            //================================== CHECK FOR DUPLICATE NAME ================================
            sqlQuery = " SELECT COUNT(sg.name) AS cnt FROM subgroup sg "
                    + " WHERE sg.name = :name AND sg.subCode <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException(mrTemp.getName() + " name is already exists.");
                }
            }
            psTemp.close();
            rsTemp.close();

            //================================== END =====================================================
            //================================== INSERT INTO SUBGROUP ====================================
            sqlQuery = "INSERT INTO subGroup(subCode,initial,NAME, displayName, manualCode,email, mobile, isActive,"
                    + " u_EntDt, preparedBy,nature,filterNature,groupCode,site_code,remark) "
                    + " VALUES (:subCode,:initial,:name, :displayName, :manualCode,:email, :mobile, :isActive, GETDATE(), "
                    + " :preparedBy,:filterNature,:filterNature,:groupCode,:site_code,:remark);";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", mrTemp.getCode());
            psTemp.setString("initial", "Dr.");
            psTemp.setString("name", mrTemp.getName());
            psTemp.setString("displayName", mrTemp.getDisplayName());
            psTemp.setString("manualCode", mrTemp.getShortName());
            psTemp.setString("eMail", mrTemp.getEmail());
            psTemp.setString("mobile", mrTemp.getMobile());
            psTemp.setString("isActive", "1");
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
            psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("groupCode", DmShared.foriegnKey(acGroupReffered));
            psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
            psTemp.setString("site_code", DmServer.getSsnSiteCode(rss));
            psTemp.executeUpdate();
            psTemp.close();
            //================================== END =====================================================

            //================================== INSERT INTO SUBGROUP2 ====================================
            sqlQuery = "INSERT INTO subGroup2(subCode, departmentCode, dob, doa) "
                    + " VALUES (:subCode, :departmentCode, :dob, :doa );";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subcode", DmShared.foriegnKey(mrTemp.getCode()));
            psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDtBirth()));
            psTemp.setDate("doa", DmServer.convertDate(mrTemp.getDtAnniversary()));
            psTemp.setString("departmentCode", DmShared.foriegnKey(department));
            psTemp.executeUpdate();
            psTemp.close();
            //================================== END =====================================================

            //================================== INSERT INTO CONSULTANT MAST =============================
            sqlQuery = "INSERT INTO consultantMast (SubCode, consultantSpeciCode, mobileHome,  mciReg) "
                    + " VALUES (:subCode, :consultantSpeciCode,:mobileHome, :mciReg) ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCode()));
            psTemp.setString("mobileHome", mrTemp.getMobile());
            psTemp.setString("consultantSpeciCode", DmShared.foriegnKey(specialization));
            psTemp.setString("mciReg", mrTemp.getMciReg());
            psTemp.executeUpdate();
            psTemp.close();
            //================================== END =====================================================

            //============================= INSERT INTO SUBGROUP SITELIST ================================
            sqlQuery = "INSERT INTO subGroupSiteList(subCode, site_Code) "
                    + " VALUES (:subcode, :siteCode );";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subcode", mrTemp.getCode());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
            psTemp.executeUpdate();
            psTemp.close();
            //================================== END =====================================================

            spBegin.commit();
            cnTemp.close();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                handelFileLog.writeLine(lineNoDisplay + e.getMessage());
                if (spBegin != null) {
                    spBegin.rollBack();
                }

            } catch (Exception ex) {
            }
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }
}
