package dataman.dmBase.dmDynamicReportBuilder.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class RptFindExtension {

    private int pageWidth;
    private int pageHeight;
    private int topMargin;
    private int bottomMargin;
    private int leftMargin;
    private int rightMargin;
    private int columnWidth;
    private String orientation;

    public RptFindExtension() {
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
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

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public File generateExtendedFindReport() throws Exception {
        File reportFile = java.io.File.createTempFile("rpt", ".jrxml");
        StringBuilder sb = new StringBuilder();
        FileWriter fileWriter = null;
        try {

            fileWriter = new FileWriter(reportFile.getPath(), true);
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            sb.append("<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"Find_Footer\" language=\"groovy\" pageWidth=\"" + pageWidth + "\" pageHeight=\"" + pageHeight + "\" orientation=\"" + orientation + "\" whenNoDataType=\"AllSectionsNoDetail\" columnWidth=\"" + columnWidth + "\" leftMargin=\"" + leftMargin + "\" rightMargin=\"" + rightMargin + "\" topMargin=\"" + topMargin + "\" bottomMargin=\"" + bottomMargin + "\" scriptletClass=\"dataman.dmBase.dmJasper.server.DmJasper\" uuid=\"b9af47b1-a48a-42b7-adf5-f90029b833de\">");
            sb.append("<property name=\"ireport.zoom\" value=\"1.1289478601075549\"/>");
            sb.append("<property name=\"ireport.x\" value=\"0\"/>");
            sb.append("<property name=\"ireport.y\" value=\"0\"/>");
            sb.append("<parameter name=\"dmFooter\" class=\"java.lang.String\"/>");
            sb.append("<queryString >");
            sb.append("      <![CDATA[]]>");
            sb.append("</queryString>");
            sb.append("<summary>");
            sb.append("	<band height=\"20\">");
            sb.append("		<textField>");
            sb.append("<reportElement positionType=\"Float\" x=\"0\" y=\"0\" width=\"" + columnWidth + "\" height=\"20\" uuid=\"44e8166c-fc31-4aa3-8087-6a8d18526cf7\"/>");
            sb.append("<box>"
                    + "					<pen lineWidth=\"1.0\"/>"
                    + "					<topPen lineWidth=\"1.0\"/>"
                    + "					<leftPen lineWidth=\"0.0\"/>"
                    + "					<bottomPen lineWidth=\"0.0\"/>"
                    + "					<rightPen lineWidth=\"0.0\"/>"
                    + "				</box>");
            sb.append("<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">");
            sb.append("<font fontName=\"Tahoma\" size=\"12\" isBold=\"true\"/>");
            sb.append("</textElement>");
            sb.append("<textFieldExpression><![CDATA[$P{dmFooter}]]></textFieldExpression>");
            sb.append("</textField>");
            sb.append("</band>");
            sb.append("</summary>");
            sb.append("</jasperReport>");

            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(sb.toString());
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reportFile;
    }

}
