/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.mi.beckman_coulter.access2;

import aarogya.projectMenu.mi.jvm.MiPersistenceModel;
import aarogya.projectMenu.mi.shared.MIConfigEnum;
import dataman.dmPlugin.CFunctions;
import dataman.machineInterface.reader.astm.ASTMMessage;
import dataman.machineInterface.reader.astm.ASTMSegment;
import dataman.machineInterface.reader.astm.ASTMSeps;
import dataman.miToolkit.MiToolkit;
import dataman.miToolkit.abstracts.AbstractMachineParser_SerialAstm;
import dataman.miToolkit.record.AbstractASTMRecord;
import dataman.miToolkit.transport.serialPort.AbstractSerialPortASTMClient;
import dataman.stPlugin.STFunctions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author abhishek
 */
public class Access2_Parser extends AbstractMachineParser_SerialAstm {

    public Access2_Parser(MIConfigEnum miConfig) {
        super(miConfig);        
    }
    
//    public Access2_Parser(String miConfigString) throws Exception {
//        super(miConfigString);        
//    }

    @Override
    public String getProtocolVersion() {
        return "E1394-97";
    }
    
    @Override
    public void parseMessage(ASTMMessage astmMessage) throws Exception {
        if (astmMessage.getSegments().size() > 1) {
            List<AbstractASTMRecord> listResults = new ArrayList<>();
            AbstractASTMRecord patientRecord = new AbstractASTMRecord(astmMessage.getSegments().get(1), "p");
//            AbstractASTMRecord testOrderRecord = new AbstractASTMRecord(astmMessage.getSegments().get(2), "o");
            for (int i = 2; i < astmMessage.getSegments().size(); i++) {
                ASTMSegment segment = astmMessage.getSegments().get(i);
                if (segment.getName().equalsIgnoreCase("r")) {
                    AbstractASTMRecord record = new AbstractASTMRecord(segment, "r");
                    listResults.add(record);
                }
            }
            transmitMessageToSave(patientRecord, listResults);
        }

    }

    private void transmitMessageToSave(AbstractASTMRecord segmentPatientIdentification, List<AbstractASTMRecord> listResultSegments) {
        try {
            List<MiPersistenceModel> listPersistanceModels = new ArrayList<>();
            String labRecId = segmentPatientIdentification.getFieldValue(2, 0, 0, 0);
            String localComputerIp = CFunctions.getLocalMachineIp();
            String localComputerName = CFunctions.getLocalMachineName();
            String machineCode = getMachineCode();
            String currentTimeStamp = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
            for (int i = 0; i < listResultSegments.size(); i++) {
                AbstractASTMRecord resultRecord = listResultSegments.get(i);
                MiPersistenceModel model = new MiPersistenceModel();
                model.labRecordId = MiToolkit.nullToEmpty(labRecId).trim();
                model.localComputerIp = MiToolkit.nullToEmpty(localComputerIp).trim();
                model.localComputerName = MiToolkit.nullToEmpty(localComputerName).trim();
                model.machineCode = MiToolkit.nullToEmpty(machineCode).trim();
                model.machineConfigurationName = MIConfigEnum.BECKMAN_COULTER_ACCESS2_SERIAL.getConfigurationName();
                model.machineDataDate = currentTimeStamp;
                model.subTestName = resultRecord.getFieldValue(2, 0, 3, 0);
                model.subTestValue = resultRecord.getFieldValue(3, 0, 0, 0);
                model.subTestUnit = resultRecord.getFieldValue(4, 0, 0, 0);

                listPersistanceModels.add(model);
            }

            STFunctions.transmitPersistanceModelsToServer(listPersistanceModels);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ASTMSeps getMachineBasedSeperators() throws Exception {
        return new ASTMSeps('|', '^', '\\', '&');
    }
    
    

}
