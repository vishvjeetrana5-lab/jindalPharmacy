/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.mi.mindray.bs240.tcp;

import dataman.machineInterface.reader.hl7.HL7Field;
import dataman.machineInterface.reader.hl7.HL7Message;
import dataman.machineInterface.reader.hl7.HL7Segment;
import dataman.machineInterface.reader.hl7.HL7Seps;
import dataman.machineInterface.reader.hl7.MSHSegment;
import dataman.miToolkit.MachineInterfaceConfiguration;
import dataman.miToolkit.MiToolkit;
import dataman.miToolkit.abstracts.AbstractMachineInterface;

/**
 *
 * @author abhishek
 */
public class bs240MachineImplementation extends AbstractMachineInterface {

    public interface ICallback {

        public void processHL7Message(HL7Message hl7Message);
    }

    private ICallback callBack;

    private String portToConnect;

    public bs240MachineImplementation(String portToConnect, ICallback callback) {
        this.portToConnect = MiToolkit.nullToEmpty(portToConnect);
        this.callBack = callback;
    }

    @Override
    public MachineInterfaceConfiguration getMachineInterfaceConfiguration() {
        MachineInterfaceConfiguration configuration = new MachineInterfaceConfiguration();
        configuration.getConfigurationSettings().put(MachineInterfaceConfiguration.ConfigurationConstants.CommunicationPort.toString(), portToConnect);
        configuration.getConfigurationSettings().put(MachineInterfaceConfiguration.ConfigurationConstants.CommunicationMode.toString(), MachineInterfaceConfiguration.CommunicationMode.TCPServer.toString());
        configuration.getConfigurationSettings().put(MachineInterfaceConfiguration.ConfigurationConstants.ProtocolId.toString(), MachineInterfaceConfiguration.CommunicationProtocolId.HL7_v2_3_1_BC3600.toString());
        return configuration;
    }

    @Override
    public void handleHL7Message(HL7Message hl7Message) {
        try {
            if (hl7Message != null) {
                if (callBack != null) {
                    callBack.processHL7Message(hl7Message);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public HL7Message getResponseHL7Message(HL7Message receivedHL7Message) throws Exception {

        MSHSegment mshSegment = new MSHSegment(receivedHL7Message.getMSHSegment());
        StringBuilder sb = new StringBuilder();
        sb.append("ACK").append(HL7Seps.DEFAULT_COMPONENT_SEPARATOR).append(receivedHL7Message.getMSHSegment().getField(9).get(0, 1, 0));
        HL7Field field = new HL7Field(sb.toString(), HL7Seps.DEFAULT);
        mshSegment.setField(9, field);
        String msa2Value = receivedHL7Message.getMSHSegment().getField(10).get(0, 0, 0);

        HL7Segment msaSegment = new HL7Segment("MSA");
        msaSegment.setField(1, "AA");
        msaSegment.setField(2, msa2Value);

        HL7Message responseHl7Message = new HL7Message(mshSegment);

        responseHl7Message.getSegments().add(msaSegment);

        return responseHl7Message;
    }

}
