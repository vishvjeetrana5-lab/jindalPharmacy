package dataman.dmBase.dmPrepareStatement.server;

import java.sql.Connection;
import java.sql.Savepoint;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class DmSavePoint {

    Savepoint spBegin = null;
    Connection cnTemp;
//    private static Semaphore semaphore;

    private DmSavePoint() throws Exception {
//        if (semaphore == null) {
//            semaphore = new Semaphore(1, true);
//        }
//        if (!semaphore.tryAcquire(10, TimeUnit.SECONDS)) {
//            throw new Exception("Shared savepoint resource has been timed out...");
//        }
    }

    public DmSavePoint(Connection cnTemp) throws Exception {
        this();
        this.cnTemp = cnTemp;
//        this.cnTemp.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        this.cnTemp.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        this.cnTemp.setAutoCommit(false);
        spBegin = this.cnTemp.setSavepoint();
    }

    public void commit() throws Exception {
        try {
            cnTemp.commit();
            cnTemp.close();
            spBegin = null;
            cnTemp = null;
        } finally {
            releaseLock();
        }
    }

    public void rollBack() throws Exception {
        try {
            cnTemp.rollback(spBegin);
            cnTemp.close();
            spBegin = null;
            cnTemp = null;
        } finally {
            releaseLock();
        }
    }

    private void releaseLock() throws Exception {
//        if (semaphore != null) {
//            semaphore.release();
//        }
    }
}
