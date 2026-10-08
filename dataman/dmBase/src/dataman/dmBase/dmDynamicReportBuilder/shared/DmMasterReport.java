package dataman.dmBase.dmDynamicReportBuilder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.DmHelper;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class DmMasterReport implements Serializable, IsSerializable {

//<editor-fold defaultstate="collapsed" desc="No Data Enum">
    public enum WhenNoDataEnum implements Serializable, IsSerializable {

        NO_PAGES((byte) 0), BLANK_PAGE((byte) 1), ALL_SECTIONS_NO_DETAIL((byte) 2), NO_DATA_SECTION((byte) 3);

        private byte value;

        public byte getValue() {
            return this.value;
        }

        private WhenNoDataEnum(byte wtype) {
            this.value = wtype;
        }
    }
//</editor-fold>

    private static final long serialVersionUID = 1L;
    private String title;
    private boolean isExpression = false;
    private String reportName;
    private String reportSubTitle;
    private int headerHeight, footerVariablesHeight, detailsHeight, leftMargin, rightMargin, topMargin, bottomMargin;
    private int colsPerPage, colsSpace;
    private boolean printColumnNames;
    private boolean ignorePagination;
    private boolean useFullPageWidth;
    private DmMasterReportStyle titleStyle = new DmMasterReportStyle("title");
    private DmMasterReportStyle subTitleStyle = new DmMasterReportStyle("subtitle");
    private boolean printBackgroundOnOddRows;
    private DmMasterReportStyle oddRowsBackgroundStyle = new DmMasterReportStyle("oddbackrowsstyle");
    private int titleHeight, subTitleHeight;
    private WhenNoDataEnum whenNoDataSetting = WhenNoDataEnum.ALL_SECTIONS_NO_DETAIL;
    private DmMasterReportStyle currencyStyle = new DmMasterReportStyle("currency");
    private DmMasterReportStyle numberStyle = new DmMasterReportStyle("number");
    private DmMasterReportStyle headerStyle = new DmMasterReportStyle("header");
    private DmMasterReportStyle columnDetailStyle = new DmMasterReportStyle("detail");

    private DmReportProperty_Mdl reportProperty = new DmReportProperty_Mdl();

    private String resultFileName = null;

    public DmMasterReportStyle getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(DmMasterReportStyle headerStyle) {
        this.headerStyle = headerStyle;
    }

    public DmMasterReportStyle getCurrencyStyle() {
        return currencyStyle;
    }

    public void setCurrencyStyle(DmMasterReportStyle currencyStyle) {
        this.currencyStyle = currencyStyle;
    }

    public DmMasterReportStyle getNumberStyle() {
        return numberStyle;
    }

    public void setNumberStyle(DmMasterReportStyle numberStyle) {
        this.numberStyle = numberStyle;
    }

    private DmMasterReportPage page = DmMasterReportPage.Page_A4_Portrait();

    private LinkedHashMap<String, DmMasterReportColumn> reportColumns = new LinkedHashMap<>();

    private DmHelper helper;
    private UserPermission userPermission;

    public UserPermission getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(UserPermission userPermission) {
        this.userPermission = userPermission;
    }

    public DmHelper getHelper() {
        return helper;
    }

    public void setHelper(DmHelper helper) {
        this.helper = helper;
    }

    public DmMasterReport() {
        this.titleStyle.setHorizontalAlign(DmMasterReportStyle.HorizontalAlignment.RIGHT);
        this.titleStyle.setFont(new DmMasterReportFont(DmMasterReportStyle.Fonts.TAHOMA, DmMasterReportStyle.PDFFontEncoding.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true, 12, true, false, false));
        this.columnDetailStyle.setFont(new DmMasterReportFont(DmMasterReportStyle.Fonts.TAHOMA, DmMasterReportStyle.PDFFontEncoding.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true, 10, false, false, false));
        this.currencyStyle.setFont(new DmMasterReportFont(DmMasterReportStyle.Fonts.TAHOMA, DmMasterReportStyle.PDFFontEncoding.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true, 10, false, false, false));
        this.numberStyle.setFont(new DmMasterReportFont(DmMasterReportStyle.Fonts.TAHOMA, DmMasterReportStyle.PDFFontEncoding.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true, 10, false, false, false));
        this.headerStyle.setFont(new DmMasterReportFont(DmMasterReportStyle.Fonts.TAHOMA, DmMasterReportStyle.PDFFontEncoding.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true, 10, false, false, false));
        this.headerStyle.setTransparent(false);
        this.headerStyle.setTransparency(DmMasterReportStyle.Transparency.OPAQUE);
        this.headerStyle.setBackgroundColor(DmMasterReportStyle.Colors.LIGHTGRAY);
        DmMasterReportBorder topBorder = new DmMasterReportBorder();
        topBorder.setLineStyle(DmMasterReportBorder.LineStyle.SOLID);
        topBorder.setBorderWidth(1f);
        this.headerStyle.setBorderTop(topBorder);

        this.reportProperty.setExportType(DmEnum.DmExportType.PDF);
        this.reportProperty.setNoOfPrint(1);
    }

    public DmMasterReportStyle getColumnDetailStyle() {
        return columnDetailStyle;
    }

    public void setColumnDetailStyle(DmMasterReportStyle columnDetailStyle) {
        this.columnDetailStyle = columnDetailStyle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIsExpression() {
        return isExpression;
    }

    public void setIsExpression(boolean isExpression) {
        this.isExpression = isExpression;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportSubTitle() {
        return reportSubTitle;
    }

    public void setReportSubTitle(String reportSubTitle) {
        this.reportSubTitle = reportSubTitle;
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public int getFooterVariablesHeight() {
        return footerVariablesHeight;
    }

    public void setFooterVariablesHeight(int footerVariablesHeight) {
        this.footerVariablesHeight = footerVariablesHeight;
    }

    public int getDetailsHeight() {
        return detailsHeight;
    }

    public void setDetailsHeight(int detailsHeight) {
        this.detailsHeight = detailsHeight;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public int getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public int getColsPerPage() {
        return colsPerPage;
    }

    public void setColsPerPage(int colsPerPage) {
        this.colsPerPage = colsPerPage;
    }

    public int getColsSpace() {
        return colsSpace;
    }

    public void setColsSpace(int colsSpace) {
        this.colsSpace = colsSpace;
    }

    public boolean isPrintColumnNames() {
        return printColumnNames;
    }

    public void setPrintColumnNames(boolean printColumnNames) {
        this.printColumnNames = printColumnNames;
    }

    public boolean isIgnorePagination() {
        return ignorePagination;
    }

    public void setIgnorePagination(boolean ignorePagination) {
        this.ignorePagination = ignorePagination;
    }

    public boolean isUseFullPageWidth() {
        return useFullPageWidth;
    }

    public void setUseFullPageWidth(boolean useFullPageWidth) {
        this.useFullPageWidth = useFullPageWidth;
    }

    public DmMasterReportStyle getTitleStyle() {
        return titleStyle;
    }

    public void setTitleStyle(DmMasterReportStyle titleStyle) {
        this.titleStyle = titleStyle;
    }

    public DmMasterReportStyle getSubTitleStyle() {
        return subTitleStyle;
    }

    public void setSubTitleStyle(DmMasterReportStyle subTitleStyle) {
        this.subTitleStyle = subTitleStyle;
    }

    public boolean isPrintBackgroundOnOddRows() {
        return printBackgroundOnOddRows;
    }

    public void setPrintBackgroundOnOddRows(boolean printBackgroundOnOddRows) {
        this.printBackgroundOnOddRows = printBackgroundOnOddRows;
    }

    public DmMasterReportStyle getOddRowsBackgroundStyle() {
        return oddRowsBackgroundStyle;
    }

    public void setOddRowsBackgroundStyle(DmMasterReportStyle oddRowsBackgroundStyle) {
        this.oddRowsBackgroundStyle = oddRowsBackgroundStyle;
    }

    public int getTitleHeight() {
        return titleHeight;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }

    public int getSubTitleHeight() {
        return subTitleHeight;
    }

    public void setSubTitleHeight(int subTitleHeight) {
        this.subTitleHeight = subTitleHeight;
    }

    public WhenNoDataEnum getWhenNoDataSetting() {
        return whenNoDataSetting;
    }

    public void setWhenNoDataSetting(WhenNoDataEnum whenNoDataSetting) {
        this.whenNoDataSetting = whenNoDataSetting;
    }

    public LinkedHashMap<String, DmMasterReportColumn> getReportColumns() {
        return this.reportColumns;
    }

    public DmMasterReportPage getPage() {
        return page;
    }

    public void setPage(DmMasterReportPage page) {
        this.page = page;
    }

    public String getResultFileName() {
        return resultFileName;
    }

    public void setResultFileName(String resultFileName) {
        this.resultFileName = resultFileName;
    }

    public DmReportProperty_Mdl getReportProperty() {
        return this.reportProperty;
    }
}
