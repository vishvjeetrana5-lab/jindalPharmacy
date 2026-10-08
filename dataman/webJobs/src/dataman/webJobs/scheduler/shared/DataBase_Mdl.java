package dataman.webJobs.scheduler.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class DataBase_Mdl extends KeyField implements Serializable,IsSerializable{

    private String databasename;

    public DataBase_Mdl() {
    }

    public String getDatabasename() {
        return databasename;
    }

    public void setDatabasename(String databasename) {
        this.databasename = databasename;
    }

}
