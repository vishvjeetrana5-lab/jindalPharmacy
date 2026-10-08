package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ST_SharedEnum implements Serializable, IsSerializable {

    public enum BloodComponentStatus implements Serializable, IsSerializable {

        RequisitionInitiated(1, "Req. Initiated"), RequisitionAccepted(2, "Req. Accepted"),
        BloodComponentIssued(3, "Component Issued"), BloodComponetReceived(4, "Component Received"),
        RequisitionRejected(5, "Req. Rejected"), BloodComponentRejected(6, "Component Rejected");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        BloodComponentStatus(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static BloodComponentStatus getViaKey(int id) {
            BloodComponentStatus algo = null;
            if (id == RequisitionInitiated.getId()) {
                algo = RequisitionInitiated;
            } else if (id == RequisitionAccepted.getId()) {
                algo = RequisitionAccepted;
            } else if (id == BloodComponentIssued.getId()) {
                algo = BloodComponentIssued;
            } else if (id == BloodComponetReceived.getId()) {
                algo = BloodComponetReceived;
            } else if (id == RequisitionRejected.getId()) {
                algo = RequisitionRejected;
            } else if (id == BloodComponentRejected.getId()) {
                algo = BloodComponentRejected;
            }
            return algo;
        }

        public static BloodComponentStatus getViaText(String text) {
            BloodComponentStatus algo = null;
            if (text.equalsIgnoreCase(RequisitionInitiated.getValue())) {
                algo = RequisitionInitiated;
            } else if (text.equalsIgnoreCase(RequisitionAccepted.getValue())) {
                algo = RequisitionAccepted;
            } else if (text.equalsIgnoreCase(BloodComponentIssued.getValue())) {
                algo = BloodComponentIssued;
            } else if (text.equalsIgnoreCase(BloodComponetReceived.getValue())) {
                algo = BloodComponetReceived;
            } else if (text.equalsIgnoreCase(RequisitionRejected.getValue())) {
                algo = RequisitionRejected;
            } else if (text.equalsIgnoreCase(BloodComponentRejected.getValue())) {
                algo = BloodComponentRejected;
            }
            return algo;
        }
    }

}
