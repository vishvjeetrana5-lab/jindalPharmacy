package pacsetup.commons;

import java.util.Properties;

public class SystemProperties {

    private Properties systemProperties;

    private enum SystemPropertiesKey {
        OS_Name("os.name"),
        OS_Version("os.version"),
        Java_Version("java.runtime.version"),
        Java_Vendor("java.vm.vendor"),
        User_Name("user.name"),
        User_Directory("user.dir"),
        User_Home("user.home");

        private String aValue;

        @Override
        public String toString() {
            return aValue.toLowerCase().trim();
        }

        private SystemPropertiesKey(String value) {
            this.aValue = value.toLowerCase().trim();
        }
    }

    public SystemProperties(Properties systemProperties) {
        this.systemProperties = systemProperties;
    }

    public Properties getProperties() {
        return this.systemProperties;
    }

    public String getOS_Name() {
        return getProperty(SystemPropertiesKey.OS_Name.toString());
    }

    public String getOS_Version() {
        return getProperty(SystemPropertiesKey.OS_Version.toString());
    }

    public String getJava_Version() {
        return getProperty(SystemPropertiesKey.Java_Version.toString());
    }

    public String getJava_Vendor() {
        return getProperty(SystemPropertiesKey.Java_Vendor.toString());
    }

    public String getUser_Name() {
        return getProperty(SystemPropertiesKey.User_Name.toString());
    }

    public String getUser_Directory() {
        return getProperty(SystemPropertiesKey.User_Directory.toString());
    }

    public String getUser_Home() {
        return getProperty(SystemPropertiesKey.User_Home.toString());
    }

    public String getProperty(String propertyKey) {
        return this.systemProperties.getProperty(propertyKey.toLowerCase().trim(), "");
    }
}
