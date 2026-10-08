package dataman.stPlugin;

import dataman.dmPlugin.getmac.GetSavePOSdetails;
import dataman.dmPlugin.routing.DmPluginBaseRouter;
import dataman.mi.routing.MiConfigService;
import dataman.radiology.RadiologyDocCommands;

public class STPlugin_Router extends DmPluginBaseRouter {

    @Override
    public void addSTPlugRoutingMappings() {
        addRoute("/machineConfigQuery", MiConfigService.class);
        addRoute("/radiologyReports", RadiologyDocCommands.class);
        addRoute("/getMac", GetSavePOSdetails.class);
    }

}
