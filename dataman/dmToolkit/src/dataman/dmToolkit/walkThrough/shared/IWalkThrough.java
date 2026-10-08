package dataman.dmToolkit.walkThrough.shared;

import java.sql.Connection;

public interface IWalkThrough {

    public void changeStatus(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception;
}
