package dataman.dmBase.dmScheduler.server;

import java.util.concurrent.Semaphore;

public class DmObjectLock<T> {

    private Semaphore sem = new Semaphore(1, true);
    private T obj;

    public DmObjectLock(T objToLock) {
        this.obj = objToLock;
    }

    public boolean lockObject() {
        boolean bool = false;
        try {
            this.sem.acquire();
//            System.out.println("Object lock has been taken...");
            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        }
        return bool;
    }

    public boolean releaseObject() {
        boolean bool = false;
        try {
            this.sem.release();
//            System.out.println("Object lock has been released...");
            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        }
        return bool;
    }

    public synchronized T getObject() {
        return this.obj;
    }
}
