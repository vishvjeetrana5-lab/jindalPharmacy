package dataman.dmToolkitR.paymentGateway.server;

import java.sql.Connection;
import java.util.Date;
import java.util.Timer;


public class EazyPayConfig {

    public EazyPayConfig() {
    }

    private static Timer pollingTimer = null;

    public static void startPollingTimer(Connection connection, int pollingTimeInMin) throws Exception {
         System.out.println("Polling Timer Called");
        if (pollingTimer == null) {
            pollingTimer = new Timer();
            pollingTimer.schedule(new EazyPayPoller(connection), new Date(), (pollingTimeInMin * 60 * 1000));
        }
    }

//    public static void stopPollingTimer() throws Exception {
//        if (pollingTimer != null) {
//            pollingTimer.cancel();
//            pollingTimer.purge();
//            pollingTimer = null;
//        }
//    }

}
