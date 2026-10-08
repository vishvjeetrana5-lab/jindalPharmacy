package dataman.dmToolkit.razorPay.shared.pmtIntegration.shared;

public class RazorPayEnum {

    public enum PMT_INTEGRATION_STATUS {
        PROCESS(1),
        PENDING(2),
        SUCCESS(3),
        FAILED(0);

        private final int id;

        PMT_INTEGRATION_STATUS(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
        
    }

    public enum Error_Codes_Status {
        P2P_DEVICE_RECEIVED("1"),
        P2P_DEVICE_SENT("2"),
        P2P_STATUS_QUEUED("3"),
        P2P_DEVICE_TXN_DONE("4"),
        P2P_DEVICE_CANCELED("5"),
        P2P_STATUS_IN_CANCELED_FROM_EXTERNAL_SYSTEM("6");

        private final String id;

        Error_Codes_Status(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public static Error_Codes_Status getValueOf(String id) {
            Error_Codes_Status rValue = null;
            Error_Codes_Status[] errCodes = values();
            for (Error_Codes_Status errCode : errCodes) {
                if (errCode.getId().equalsIgnoreCase(id)) {
                    rValue = errCode;
                    break;
                }
            }
            return rValue;
        }
    }
}
