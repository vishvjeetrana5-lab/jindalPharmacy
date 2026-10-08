package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
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
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConsultantTimeDetail implements DmImportData_Interface {

    class Model {

        private String code, consultant, dayName, dayCode, fromTime, toTime, graceMin, OPD, Emergency,
                onlinePatient, preAppointment, appointmentAdvance, reVisit, onlineCharge, noOfPatient, payClinic;

        public Model() {
        }

        public String getDayName() {
            return dayName;
        }

        public String getPayClinic() {
            return payClinic;
        }

        public void setPayClinic(String payClinic) {
            this.payClinic = payClinic;
        }

        public String getAppointmentAdvance() {
            return appointmentAdvance;
        }

        public void setAppointmentAdvance(String appointmentAdvance) {
            this.appointmentAdvance = appointmentAdvance;
        }

        public void setDayName(String dayName) {
            this.dayName = dayName;
        }

        public String getFromTime() {
            return DmServer.vNull(fromTime, "00.00");
        }

        public void setFromTime(String fromTime) {
            this.fromTime = DmServer.vNull(fromTime, "00.00");
        }

        public String getToTime() {
            return DmServer.vNull(toTime, "00.00");
        }

        public void setToTime(String toTime) {
            this.toTime = DmServer.vNull(toTime, "00.00");
        }

        public String getGraceMin() {
            return graceMin;
        }

        public void setGraceMin(String graceMin) {
            this.graceMin = graceMin;
        }

        public String getOPD() {
            return OPD;
        }

        public void setOPD(String OPD) {
            this.OPD = OPD;
        }

        public String getEmergency() {
            return Emergency;
        }

        public void setEmergency(String Emergency) {
            this.Emergency = Emergency;
        }

        public String getOnlinePatient() {
            return onlinePatient;
        }

        public void setOnlinePatient(String onlinePatient) {
            this.onlinePatient = onlinePatient;
        }

        public String getPreAppointment() {
            return preAppointment;
        }

        public void setPreAppointment(String preAppointment) {
            this.preAppointment = preAppointment;
        }

        public String getReVisit() {
            return reVisit;
        }

        public void setReVisit(String reVisit) {
            this.reVisit = reVisit;
        }

        public String getOnlineCharge() {
            return onlineCharge;
        }

        public void setOnlineCharge(String onlineCharge) {
            this.onlineCharge = onlineCharge;
        }

        public String getNoOfPatient() {
            return noOfPatient;
        }

        public void setNoOfPatient(String noOfPatient) {
            this.noOfPatient = noOfPatient;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getConsultant() {
            return consultant;
        }

        public void setConsultant(String consultant) {
            this.consultant = consultant;
        }

        public String getDayCode() {
            return dayCode;
        }

        public void setDayCode(String dayCode) {
            this.dayCode = dayCode;
        }
    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;

    private String lineNoDisplay = "";

    public ConsultantTimeDetail() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("consultantName");
        node.setSample("Ajay Mehta");
        node.setInstruction("{unique;mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("dayName");
        node.setSample("All Day");
        node.setInstruction("{mandatory, anyOne:All Day/Sunday/Monday/Tuesday/Wednesday/Thursday/Friday/Saturday}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("graceMinut");
        node.setSample("5");
        node.setInstruction("{numeric,4c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("fromTime");
        node.setSample("10.00");
        node.setInstruction("{mandatory, 00.00-23.59}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("toTime");
        node.setSample("14.00");
        node.setInstruction("{mandatory, 00.00-23.59}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("noOfPatient");
        node.setSample("10");
        node.setInstruction("{numeric,4c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("OPD");
        node.setSample("500");
        node.setInstruction("{numeric,10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Emergency");
        node.setSample("420");
        node.setInstruction("{numeric,10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("appointmentAdvance");
        node.setSample("150");
        node.setInstruction("{numeric,10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("preAppointment");
        node.setSample("150");
        node.setInstruction("{numeric,10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("reVisit");
        node.setSample("100");
        node.setInstruction("{numeric,10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("payClinic");
        node.setSample("100");
        node.setInstruction("{numeric,10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("onlineCharge");
        node.setSample("650");
        node.setInstruction("{numeric,10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("onlinePatient");
        node.setSample("10");
        node.setInstruction("{numeric,4c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Consultant Time Detail");
        fileDetail.setServerClassName(ConsultantTimeDetail.class.getName());

        return fileDetail;
    }

    private int saveConsultantTimeDetail(Model mrTemp, String service, String organization, String rateListCode) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, srNo, applicableFrom = "";
        ServicePriceList_Mdl priceList;
        List<ServicePriceList_Mdl> lstServicePrice = new ArrayList<ServicePriceList_Mdl>();
        int status = 0, rtnStatus = 0;
        try {
            if (!DmShared.xNull(mrTemp.getDayCode()).equalsIgnoreCase("")) {
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
                spBegin = new DmSavePoint(cnTemp);

                mrTemp.setCode("");
                sqlQuery = "SELECT	cm.subCode  "
                        + "FROM consultantMast cm "
                        + "left join subgroup sg on cm.subCode = sg.subCode "
                        + "WHERE sg.name = :name ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", DmShared.xNull(mrTemp.getConsultant()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                }
                psTemp.close();
                rsTemp.close();

                if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getConsultant() + " consultant not found.");
                }

                srNo = DmServer.getCodeViaManualCode(mrTemp.getCode(), "consultantCode", "consultantTimeDetail", "ISNULL(MAX(sNo), 0) + 1 AS sNo", cnTemp);

                sqlQuery = "select  max(consultantCode) as consultantCode, \n"
                        + "		max(applicableFrom) as applicableFrom  \n"
                        + "             from patientservicepricelist  \n"
                        + "             where organizationCode = :organizationCode "
                        + "             and EntryFrom = :entryFrom and site_code = :siteCode \n"
                        + "             and applicableFrom <= cast(:vDate as date)  \n"
                        + "             and consultantCode = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
                psTemp.setDate("vDate", new Date(DmServer.getServerTimestamp().getTime()));
                psTemp.setString("organizationCode", organization);
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
                psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.CONSULTANT_MASTER.toInt()).trim());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    applicableFrom = (DmShared.dNull(rsTemp.getDate("applicableFrom")));
                } else {
                    applicableFrom = (DmShared.dNull(new Date(DmServer.getServerTimestamp().getTime())));

                }

                sqlQuery = "INSERT INTO consultanttimedetail (consultantCode, sNo, dayNameCode, fromTime, toTime, opd, "
                        + "             emergency, appointment, patient, graceMinutes, reVisit, applicableFrom, "
                        + "             online, onlinePatient, site_Code, appointmentAdvance, payClinic) "
                        + " VALUES (:consultantCode, :sNo, :dayNameCode, :fromTime, :toTime, :opd, :emergency, :appointment, "
                        + "        :patient, :graceMinutes, :reVisit, :applicableFrom, :online, :onlinePatient, :siteCode, :appointmentAdvance, :payClinic);";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("sNo", srNo);
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getCode()));
                psTemp.setString("dayNameCode", DmShared.xNull(mrTemp.getDayCode()));
                psTemp.setString("fromTime", DmServer.vNull(mrTemp.getFromTime(), "0.00"));
                psTemp.setString("toTime", DmServer.vNull(mrTemp.getToTime(), "0.00"));
                psTemp.setString("opd", DmServer.vNull(mrTemp.getOPD(), "0.00"));
                psTemp.setString("emergency", DmServer.vNull(mrTemp.getEmergency(), "0.00"));
                psTemp.setString("appointment", DmServer.vNull(mrTemp.getPreAppointment(), "0.00"));
                psTemp.setString("appointmentAdvance", DmServer.vNull(mrTemp.getAppointmentAdvance(), "0.00"));
                psTemp.setString("payClinic", DmServer.vNull(mrTemp.getPayClinic(), "0.00"));
                psTemp.setString("patient", DmServer.vNull(mrTemp.getNoOfPatient(), "0"));
                psTemp.setString("graceMinutes", DmServer.vNull(mrTemp.getGraceMin(), "0"));
                psTemp.setString("reVisit", DmServer.vNull(mrTemp.getReVisit(), "0.00"));
                psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
                psTemp.setString("online", DmServer.vNull(mrTemp.getOnlineCharge(), "0.00"));
                psTemp.setString("onlinePatient", DmServer.vNull(mrTemp.getOnlinePatient(), "0"));
                psTemp.executeUpdate();
                psTemp.close();

                priceList = getTimeWisePriceList(mrTemp, PriceListNature.ReVisit, DmServer.vNull(mrTemp.getReVisit(), "0.00"), mrTemp.getCode(), "01-Apr-2000", organization, service, rateListCode);
                lstServicePrice.add(priceList);

                if (DmShared.vNull(mrTemp.getEmergency()).doubleValue() > 0) {
                    priceList = getTimeWisePriceList(mrTemp, PriceListNature.Emergency, DmServer.vNull(mrTemp.getEmergency(), "0.00"), mrTemp.getCode(), "01-Apr-2000", organization, service, rateListCode);
                    lstServicePrice.add(priceList);
                }
                if (DmShared.vNull(mrTemp.getOPD()).doubleValue() > 0) {
                    priceList = getTimeWisePriceList(mrTemp, PriceListNature.OPD, DmServer.vNull(mrTemp.getOPD(), "0.00"), mrTemp.getCode(), "01-Apr-2000", organization, service, rateListCode);
                    lstServicePrice.add(priceList);
                }
                if (DmShared.vNull(mrTemp.getOnlineCharge()).doubleValue() > 0) {
                    priceList = getTimeWisePriceList(mrTemp, PriceListNature.Online, DmServer.vNull(mrTemp.getOnlineCharge(), "0.00"), mrTemp.getCode(), "01-Apr-2000", organization, service, rateListCode);
                    lstServicePrice.add(priceList);
                }
                if (DmShared.vNull(mrTemp.getAppointmentAdvance()).doubleValue() > 0) {
                    priceList = getTimeWisePriceList(mrTemp, PriceListNature.AppointmentAdvance, DmServer.vNull(mrTemp.getAppointmentAdvance(), "0.00"), mrTemp.getCode(), "01-Apr-2000", organization, service, rateListCode);
                    lstServicePrice.add(priceList);
                }
                if (DmShared.vNull(mrTemp.getPreAppointment()).doubleValue() > 0) {
                    priceList = getTimeWisePriceList(mrTemp, PriceListNature.PreAppointment, DmServer.vNull(mrTemp.getPreAppointment(), "0.00"), mrTemp.getCode(), "01-Apr-2000", organization, service, rateListCode);
                    lstServicePrice.add(priceList);
                }
                if (DmShared.vNull(mrTemp.getPayClinic()).doubleValue() > 0) {
                    priceList = getTimeWisePriceList(mrTemp, PriceListNature.PayClinic, DmServer.vNull(mrTemp.getPayClinic(), "0.00"), mrTemp.getCode(), "01-Apr-2000", organization, service, rateListCode);
                    lstServicePrice.add(priceList);
                }

                sqlQuery = "";

                STServer.savePriceList(applicableFrom, PriceListEntryFrom.CONSULTANT_MASTER, mrTemp.getCode(), rateListCode, DmServer.getSsnSiteCode(rss), lstServicePrice, cnTemp);
                spBegin.commit();
                cnTemp.close();
                rtnStatus = DmImportStatus.SUCCESS.getId();
            }
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

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }

            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        Connection cnTemp = null;
        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue, organization = "", service, sqlQuery, rateListCode = "";
        Object objValue;
        String[] csvFileFormat;
        DmImportStatus importStatus = null;
        int status = 0, rtnStatus = 0;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        try {
            this.rss = rss;

            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            /*====================================< Get default Empanelment >====================================*/
            sqlQuery = "Select  om.subCode, dbo.getTaggedRateList(om.subCode, :vrDate) as rateListCode "
                    + "         from organizationMast as om "
                    + "         Left Join SubGroup as sg on om.SubCode = sg.SubCode  "
                    + "         where om.shortName = 'CP' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("vrDate", DmServer.convertDate(DmServer.getServerDateTime()[0]));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                organization = (DmShared.xNull(rsTemp.getString("subCode")));
                rateListCode = (DmShared.xNull(rsTemp.getString("rateListCode")));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(organization).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Define organization with 'CP' short name.");
            }

            if (DmShared.xNull(rateListCode).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Attach rateList to organization with 'CP' short name.");
            }

            service = DmServer.getCodeViaManualCode("CF", "shortName", "patientservicemast", "code", cnTemp);
            if (DmShared.xNull(service).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Define 'Consultant Fees' with 'CF' short name.");
            }
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
                        if (headerName.trim().equalsIgnoreCase("consultantName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 0, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setConsultant(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("dayName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            }
                            mrTemp.setDayName(fieldValue);
                            mrTemp.setDayCode(fieldValue.equalsIgnoreCase("All Day") ? "0"
                                    : (fieldValue.equalsIgnoreCase("Sunday") ? "1"
                                    : (fieldValue.equalsIgnoreCase("Monday") ? "2"
                                    : (fieldValue.equalsIgnoreCase("Tuesday") ? "3"
                                    : (fieldValue.equalsIgnoreCase("Wednesday") ? "4"
                                    : (fieldValue.equalsIgnoreCase("Thursday") ? "5"
                                    : (fieldValue.equalsIgnoreCase("Friday") ? "6"
                                    : (fieldValue.equalsIgnoreCase("Saturday") ? "7" : ""))))))));
                        } else if (headerName.trim().equalsIgnoreCase("graceMinut")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 4, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGraceMin(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("fromTime")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, true, 5, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFromTime(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("toTime")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, true, 5, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setToTime(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("noOfPatient")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 4, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNoOfPatient(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("OPD")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOPD(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Emergency")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmergency(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("preAppointment")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPreAppointment(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("appointmentAdvance")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAppointmentAdvance(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("payClinic")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPayClinic(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("reVisit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReVisit(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("onlineCharge")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOnlineCharge(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("onlinePatient")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 4, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOnlinePatient(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveConsultantTimeDetail(mrTemp, service, organization, rateListCode);
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception e) {
            }
        }
        return importStatus;
    }

    private ServicePriceList_Mdl getTimeWisePriceList(Model mrTemp, PriceListNature nature, String natureValue, String consultant, String applicableDate, String organization, String serviceCode, String rateListCode) {
        ServicePriceList_Mdl priceList = new ServicePriceList_Mdl();
        priceList.setConsultantCode(consultant);
        priceList.setDate(applicableDate);
        priceList.setEntryFromNature("1");
        priceList.setDayNameCode(DmShared.xNull(mrTemp.getDayCode()));
        priceList.setOrganizationCode(organization);
        priceList.setPriceListEntryFrom(PriceListEntryFrom.CONSULTANT_MASTER);
        priceList.setPriceListNature(nature);
        priceList.setRate(natureValue);
        priceList.setRateListCode(rateListCode);
        priceList.setServiceCode(DmShared.xNull(serviceCode));
        priceList.setSiteCode(DmServer.getSsnSiteCode(rss));
        priceList.setTime(DmServer.vNull(mrTemp.getFromTime(), "0.00"));
        priceList.setTimeTo(DmServer.vNull(mrTemp.getToTime(), "0.00"));
        return priceList;
    }
}
