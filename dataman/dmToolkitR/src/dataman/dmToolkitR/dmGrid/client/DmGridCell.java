package dataman.dmToolkitR.dmGrid.client;

public class DmGridCell {

    public class Separator {

        public static final String CELL_SEPARATOR = "|";
        public static final String CELL_FIELD_VALUE_SEPARATOR = "~";
    }

    public class Field {

        public static final String NAME = "[{<name>}]";
        public static final String VALUE = "[{<value>}]";
        public static final String CSSCLASSES = "[{<cssClasses>}]";
        public static final String ALIAS = "[{<alias>}]";
        public static final String DATATYPE = "[{<dataType>}]";
        public static final String STYLE = "[{<style>}]";
    }

    public class Hide {

        public static final String IN_MOBILE = " hidden-xs ";
        public static final String IN_MOBILE_TABLET = " hidden-xs hidden-sm ";
    }

    private String name;
    private String value;
    private String cssClasses;
    private String alias;
    private String dataType;
    private String style;

    public DmGridCell() {
    }

    public DmGridCell(String name, String value) {
        this(name, value, "");
    }

    public DmGridCell(String name, String value, String cssClasses) {
        this(name, value, cssClasses, "");
    }

    public DmGridCell(String name, String value, String cssClasses, String alias) {
        this(name, value, cssClasses, alias, "");
    }

    public DmGridCell(String name, String value, String cssClasses, String alias, String dataType) {
        this(name, value, cssClasses, alias, dataType, "");
    }

    public DmGridCell(String name, String value, String cssClasses, String alias, String dataType, String style) {
        this.name = name;
        this.value = value;
        this.cssClasses = cssClasses;
        this.alias = alias;
        this.dataType = dataType;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCssClasses() {
        return cssClasses;
    }

    public void setCssClasses(String cssClasses) {
        this.cssClasses = cssClasses;
    }

}
