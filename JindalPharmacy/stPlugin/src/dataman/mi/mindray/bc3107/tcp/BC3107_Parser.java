package dataman.mi.mindray.bc3107.tcp;

import aarogya.projectMenu.mi.jvm.MiPersistenceModel;
import aarogya.projectMenu.mi.shared.MIConfigEnum;
import aarogya.projectMenu.mi.tcp.AbstractTCPMachineConfig;
import dataman.dmPlugin.CFunctions;
import dataman.machineInterface.reader.hl7.HL7Message;
import dataman.machineInterface.reader.hl7.HL7Segment;
import dataman.machineInterface.reader.hl7.HL7SegmentList;
import dataman.miToolkit.MachineInterfaceConfiguration;
import dataman.miToolkit.MiToolkit;
import dataman.miToolkit.record.AbstractHL7Record;
import java.util.ArrayList;
import java.util.List;
import dataman.miToolkit.record.IMachineParserHL7;
import dataman.stPlugin.STFunctions;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BC3107_Parser extends AbstractTCPMachineConfig implements IMachineParserHL7 {

    public static final String timeStampFormat = "yyyyMMddHHmmss";

    private String portToConnect = "3107";

    private bc3107MachineImplementation mainProcessor;

    private String machineManualCode;

    private static List<HL7Message> listDummyHl7Records;

    protected MIConfigEnum miConfigEnum;

    public BC3107_Parser(MIConfigEnum configEnum) {
        miConfigEnum = configEnum;
    }

    @Override
    public void parseMessage(HL7Message messageToParse) throws Exception {
        List<AbstractHL7Record> listResultRecords = new ArrayList<>();
        AbstractHL7Record segmentPatientIdentification = new AbstractHL7Record(messageToParse.findSegment("PID"), "pid");
//        AbstractHL7Record segmentPatientVisit = new AbstractHL7Record(messageToParse.findSegment("PV1"), "pv1");
//        AbstractHL7Record segmentObservationRequest = new AbstractHL7Record(messageToParse.findSegment("OBR"), "obr");
        HL7SegmentList listSegments = messageToParse.getSegments();
        for (int i = 4; i < listSegments.size(); i++) {
            HL7Segment segment = listSegments.get(i);
            if (segment.getName().equalsIgnoreCase("obx")) {
                listResultRecords.add(new AbstractHL7Record(segment, "obx"));
            }
        }

        transmitMessageToSave(segmentPatientIdentification, listResultRecords);
    }

    public static void clearDummyRecords() {
        if (listDummyHl7Records != null) {
            listDummyHl7Records.clear();
        }
    }

    public static void fillDummyRecords() {
        try {

            clearDummyRecords();

            if (listDummyHl7Records == null) {
                listDummyHl7Records = new ArrayList<>();
            }

            for (int i = 0; i < 1000; i++) {
                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("MSH|^~\\&|||||20101206164344||ORU^R01|1|P|2.3.1||||||UNICODE").append((char) 13).
                        append("PID|1||ChartNo" + i + "^^^^MR||LastName" + i + "^FirstName" + i + "||20040506070809|Male").append((char) 13).
                        append("PV1|1|Neike|Hema^^BN1|||||||||||||||||ChargeType").append((char) 13).
                        append("OBR|1||TestSampleID1|00001^Automated Count^99MRC||20000102030405|20010203040506|||Sender|||Cold|20020304050607||||||||20030405060708||HM||||Auditer||||Tester").append((char) 13).
                        append("OBX|1|IS|08001^Take Mode^99MRC||O||||||F").append((char) 13).
                        append("OBX|2|IS|08002^Blood Mode^99MRC||W||||||F").append((char) 13).
                        append("OBX|3|IS|08003^Test Mode^99MRC||CBC||||||F").append((char) 13).
                        append("OBX|4|IS|01002^Ref Group^99MRC||Common||||||F").append((char) 13).
                        append("OBX|5|NM|30525-0^Age^LN||Age|yr|||||F").append((char) 13).
                        append("OBX|6|ST|01001^Remark^99MRC||Remark||||||F").append((char) 13).
                        append("OBX|7|NM|6690-2^WBC^LN||111.11|1019/L|111.11-111.11|N|||F").append((char) 13).
                        append("OBX|8|NM|731-0^LYM#^LN||111.11|1019/L|111.11-111.11|N|||F").append((char) 13).
                        append("OBX|9|NM|736-9^LYM%^LN||111.1|%|111.1-111.1|N|||F").append((char) 13).
                        append("OBX|10|NM|789-8^RBC^LN||11.11|10112/L|11.11-11.11|N|||F").append((char) 13).
                        append("OBX|11|NM|718-7^HGB^LN||111.1|g/L|111.1-111.1|N|||F").append((char) 13).
                        append("OBX|12|NM|787-2^MCV^LN||111.1|%|111.1-111.1|N|||F").append((char) 13).
                        append("OBX|13|NM|785-6^MCH^LN||111.1|pg|111.1-111.1|N|||F").append((char) 13).
                        append("OBX|14|NM|786-4^MCHC^LN||1111|g/L|1111-1111|N|||F").append((char) 13).
                        append("OBX|15|NM|788-0^RDW-CV^LN||111.1|%|111.1-111.1|N|||F").append((char) 13).
                        append("OBX|16|NM|21000-5^RDW-SD^LN||111.1|fL|111.1-111.1|N|||F").append((char) 13).
                        append("OBX|17|NM|4544-3^HCT^LN||111.1|%|111.1-111.1|N|||F").append((char) 13).
                        append("OBX|18|NM|777-3^PLT^LN||1111|1019/L|1111-1111|N|||F").append((char) 13).
                        append("OBX|19|NM|32623-1^MPV^LN||11.1|fL|11.1-11.1|N|||F").append((char) 13).
                        append("OBX|20|NM|32207-3^PDW^LN||11.1||11.1-11.1|N|||F").append((char) 13).
                        append("OBX|21|NM|10002^PCT^99MRC||1.111|%|1.111-1.111|N|||F").append((char) 13).
                        append("OBX|22|NM|10027^MID#^99MRC||111.11|1019/L|111.11-111.11|N|||F").append((char) 13).
                        append("OBX|23|NM|10029^MID%^99MRC||111.1|%|111.1-111.1|N|||F").append((char) 13).
                        append("OBX|24|NM|10028^GRAN#^99MRC||111.11|1019/L|111.11-111.11|N|||F").append((char) 13).
                        append("OBX|25|NM|10030^GRAN%^99MRC||111.1|%|111.1-111.1|N|||F").append((char) 13).
                        append("OBX|26|NM|10014^PLCR^99MRC||111.1|%|111.1-111.1|N|||F").append((char) 13).
                        append("OBX|27|IS|12045^Multiple alerts^99MRC||T||||||F").append((char) 13).
                        append("OBX|28|IS|12046^Lym left region alert^99MRC||T||||||F").append((char) 13).
                        append("OBX|29|IS|||T||||||F").append((char) 13).
                        append("OBX|30|IS|12048^Mid gran region alert^99MRC||T||||||F").append((char) 13).
                        append("OBX|31|IS|12049^Gran right region alert^99MRC||T||||||F").append((char) 13).
                        append("OBX|32|IS|12050^Plt rbc boundary blur^99MRC||T||||||F").append((char) 13).
                        append("OBX|33|IS|12051^Micro plt over aboundce^99MRC||T||||||F").append((char) 13).
                        append("OBX|34|IS|12052^Macro plt over aboundce^99MRC||T||||||F").append((char) 13).
                        append("OBX|35|NM|15004^WBC Histogram. Meta Length^99MRC||1||||||F").append((char) 13).
                        append("OBX|36|NM|15010^WBC Lym left line.^99MRC||1||||||F").append((char) 13).
                        append("OBX|37|NM|15011^WBC Lym Mid line.^99MRC||2||||||F").append((char) 13).
                        append("OBX|38|NM|15012^WBC Mid Gran line.^99MRC||3||||||F").append((char) 13).
                        append("OBX|39|NM|15013^WBC Gran right line^99MRC||4||||||F").append((char) 13).
                        append("OBX|40|ED|15000^WBC Histogram. Binary^99MRC||^Application^Octer-stream^Base64^AAAAAAAAAAAAAAAAAAAAAA==||||||F").append((char) 13).
                        append("OBX|41|NM|15051^RBC Histogram. Left Line^99MRC||5||||||F").append((char) 13).
                        append("OBX|42|NM|15052^RBC Histogram. Right Line^99MRC||6||||||F").append((char) 13).
                        append("OBX|43|NM|15053^RBC Histogram. Binary Meta Length^99MRC||2||||||F").append((char) 13).
                        append("OBX|44|ED|15050^RBC Histogram. Binary^99MRC||^Application^Octer-stream^Base64^AAAAAAAAAAAAAAAAAAAAAA==||||||F").append((char) 13).
                        append("OBX|45|NM|15111^PLT Histogram. Left Line^99MRC||7||||||F").append((char) 13).
                        append("OBX|46|NM|15112^PLT Histogram. Right Line^99MRC||8||||||F").append((char) 13).
                        append("OBX|47|NM|15113^PLT Histogram. Binary Meta Length^99MRC||4||||||F").append((char) 13).
                        append("OBX|48|ED|15100^PLT Histogram. Binary^99MRC||^Application^Octer-stream^Base64^AAAAAAAAAAAAAAAAAAAAAA==||||||F").append((char) 13);

                HL7Message message = new HL7Message(stringBuilder.toString());

                listDummyHl7Records.add(message);
            }

            transmitDummyRecordsToSave();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void transmitDummyRecordsToSave() {
        try {
            for (int i = 0; i < listDummyHl7Records.size(); i++) {
                HL7Message messageToParse = listDummyHl7Records.get(i);

                List<AbstractHL7Record> listResultRecords = new ArrayList<>();
                AbstractHL7Record segmentPatientIdentification = new AbstractHL7Record(messageToParse.findSegment("PID"), "pid");
                HL7SegmentList listSegments = messageToParse.getSegments();
                for (int s = 4; s < listSegments.size(); s++) {
                    HL7Segment segment = listSegments.get(s);
                    if (segment.getName().equalsIgnoreCase("obx")) {
                        listResultRecords.add(new AbstractHL7Record(segment, "obx"));
                    }
                }

                List<MiPersistenceModel> listPersistanceModels = new ArrayList<>();
                String labRecId = segmentPatientIdentification.getFieldValue(3, 0, 0, 0);
                String localComputerIp = CFunctions.getLocalMachineIp();
                String localComputerName = CFunctions.getLocalMachineName();
                String machineCode = MIConfigEnum.MINDRAY_BC_3107_TCP.getConfigurationName();
                String currentTimeStamp = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
                for (int s = 0; s < listResultRecords.size(); s++) {
                    AbstractHL7Record resultRecord = listResultRecords.get(s);
                    MiPersistenceModel model = new MiPersistenceModel();
                    model.labRecordId = MiToolkit.nullToEmpty(labRecId).trim();
                    model.localComputerIp = MiToolkit.nullToEmpty(localComputerIp).trim();
                    model.localComputerName = MiToolkit.nullToEmpty(localComputerName).trim();
                    model.machineCode = MiToolkit.nullToEmpty(machineCode).trim();
                    model.machineConfigurationName = MIConfigEnum.MINDRAY_BC_3107_TCP.getConfigurationName();
                    model.machineDataDate = currentTimeStamp;
                    model.subTestName = resultRecord.getFieldValue(3, 0, 1, 0);
                    model.subTestValue = resultRecord.getFieldValue(5, 0, 0, 0);
                    model.subTestUnit = resultRecord.getFieldValue(6, 0, 0, 0);
                    model.machineName = "Mac1";

                    listPersistanceModels.add(model);
                }

                STFunctions.transmitPersistanceModelsToServer(listPersistanceModels);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void transmitMessageToSave(AbstractHL7Record segmentPatientIdentification, List<AbstractHL7Record> listResultSegments) {
        try {
            List<MiPersistenceModel> listPersistanceModels = new ArrayList<>();
            String labRecId = segmentPatientIdentification.getFieldValue(3, 0, 0, 0);
            String localComputerIp = CFunctions.getLocalMachineIp();
            String localComputerName = CFunctions.getLocalMachineName();
            String machineCode = getMachineCode();
            String currentTimeStamp = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
            for (int i = 0; i < listResultSegments.size(); i++) {
                AbstractHL7Record resultRecord = listResultSegments.get(i);
                MiPersistenceModel model = new MiPersistenceModel();
                model.labRecordId = MiToolkit.nullToEmpty(labRecId).trim();
                model.localComputerIp = MiToolkit.nullToEmpty(localComputerIp).trim();
                model.localComputerName = MiToolkit.nullToEmpty(localComputerName).trim();
                model.machineCode = MiToolkit.nullToEmpty(machineCode).trim();
                model.machineConfigurationName = MIConfigEnum.MINDRAY_BC_3107_TCP.getConfigurationName();
                model.machineDataDate = currentTimeStamp;
                model.subTestName = resultRecord.getFieldValue(3, 0, 1, 0);
                model.subTestValue = resultRecord.getFieldValue(5, 0, 0, 0);
                model.subTestUnit = resultRecord.getFieldValue(6, 0, 0, 0);
                model.machineName = MiToolkit.nullToEmpty(machineManualCode).trim();

                listPersistanceModels.add(model);
            }

            STFunctions.transmitPersistanceModelsToServer(listPersistanceModels);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getTransmissionMode() {
        return miConfigEnum.getTransmissionMode();
    }

    @Override
    public String getProtocol() {
        return miConfigEnum.getProtocol();
    }

    @Override
    public final String getCommunicationMode() {
        return miConfigEnum.getCommunicationMode();
    }

    @Override
    public String getProtocolVersion() {
        return MachineInterfaceConfiguration.CommunicationProtocolId.HL7_v2_3_1_BC3600.toString();
    }

    @Override
    public String getMachineName() {
        return miConfigEnum.getMachineCompany();
    }

    @Override
    public String getMachineCode() {
        return miConfigEnum.getConfigurationName();
    }

    @Override
    public String getMachineModel() {
        return miConfigEnum.getMachineModel();
    }

    @Override
    public List<String> getMachineObservableTestList() {
        List<String> listTests = new ArrayList<>();
        return listTests;
    }

    @Override
    public void setMachineManualCode(String manualMachineCode) {
        this.machineManualCode = MiToolkit.nullToEmpty(manualMachineCode).trim();
    }

    @Override
    public void start() throws Exception {
        try {
            mainProcessor = new bc3107MachineImplementation(portToConnect, new bc3107MachineImplementation.ICallback() {
                @Override
                public void processHL7Message(HL7Message hl7Message) {
                    try {
                        parseMessage(hl7Message);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            mainProcessor.startMachineInterface();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void stop() {
        try {
            if (mainProcessor != null) {
                mainProcessor.stopMachineInterface();
            }
            mainProcessor = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setCommunicationPort(String portToConnect) {
        this.portToConnect = MiToolkit.nullToEmpty(portToConnect);
    }

    @Override
    public String getCommunicationPort() {
        return this.portToConnect;
    }

    @Override
    public String getMachineManualCode() {
        return this.machineManualCode;
    }

}
