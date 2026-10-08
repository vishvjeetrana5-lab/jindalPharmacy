package dataman.dmPlugin.routing;

import dataman.dmPlugin.getmac.GetSavePOSdetails;
import dataman.dmPlugin.localSettings.LocalSettings;
import dataman.dmPlugin.printing.DmPrintCommands;
import dataman.dmPlugin.querySession.QueryToClient;
import dataman.dmPlugin.server.router.RouterNanoHTTPD;

public abstract class DmPluginBaseRouter extends RouterNanoHTTPD {

    public static int webPortNumber;

    public DmPluginBaseRouter() {
        super(webPortNumber);
        addMappings();
    }

    @Override
    public void addMappings() {
        super.addMappings(); //To change body of generated methods, choose Tools | Templates.
        addRoute("/queryClient", QueryToClient.class);
        addRoute("/localSettings", LocalSettings.class);
        addRoute("/printCommands", DmPrintCommands.class);
        addRoute("/getMac", GetSavePOSdetails.class);
        
        addSTPlugRoutingMappings();
    }
    
    public abstract void addSTPlugRoutingMappings();

}
