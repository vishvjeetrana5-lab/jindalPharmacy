/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmPlugin.resources;

import java.net.URL;

/**
 *
 * @author abhishek
 */
public class DmPlugInResources {

    public enum DmPluginResourceType {
        Images("images");

        private String rType;

        @Override
        public String toString() {
            return rType.trim();
        }

        private DmPluginResourceType(String resourceType) {
            this.rType = resourceType.trim();
        }
    }

    public static URL getResourceURL(DmPluginResourceType resourceType, String resourceName) {

        return DmPlugInResources.class.getResource(resourceType.toString() + "/" + resourceName.trim());
    }
}
