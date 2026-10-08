package dataman.dmBase.self.server;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.DmJRBaseExpressionChunk;
import net.sf.jasperreports.engine.base.JRBaseExpressionChunk;
import net.sf.jasperreports.engine.base.JRBaseTextElement;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.util.JRElementsVisitor;

public class DmJasperCompiler implements JRVisitor, DmIJasperCompiled {

    private File reportFileJRXML;

    private Map<String, DmJasperStyle> styles;

    private Map<String, JasperReport> subreports;

    private JasperReport mainReport;

    private boolean compileWithHierarchy = false;

    public DmJasperCompiler(File reportFileJRXML, boolean withHierarchy, DmJasperStyle... styleList) {
        this.compileWithHierarchy = withHierarchy;
        styles = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        subreports = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        this.reportFileJRXML = reportFileJRXML;
        parseStyles(styleList);
    }

    private void parseStyles(DmJasperStyle... lstStyles) {
        if (lstStyles != null) {
            for (int i = 0; i < lstStyles.length; i++) {
                if (!styles.containsKey(lstStyles[i].getName())) {
                    styles.put(lstStyles[i].getName(), lstStyles[i]);
                }
            }
        }
    }

    @Override
    public JasperReport getReport() {
        return mainReport;
    }

    @Override
    public Map<String, JasperReport> getSubReports() {
        return subreports;
    }

    @Override
    public void visitBreak(JRBreak breakElement) {
    }

    @Override
    public void visitChart(JRChart chart) {
    }

    @Override
    public void visitCrosstab(JRCrosstab crosstab) {
    }

    @Override
    public void visitElementGroup(JRElementGroup elementGroup) {
    }

    @Override
    public void visitEllipse(JREllipse ellipse) {
    }

    @Override
    public void visitFrame(JRFrame frame) {
    }

    @Override
    public void visitImage(JRImage image) {
    }

    @Override
    public void visitLine(JRLine line) {
    }

    @Override
    public void visitRectangle(JRRectangle rectangle) {
    }

    @Override
    public void visitStaticText(JRStaticText staticText) {
    }

    @Override
    public void visitSubreport(JRSubreport subreport) {
        try {
            if (compileWithHierarchy) {
                compileSubReport(subreport.getExpression());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void visitTextField(JRTextField textField) {
        if (textField.getStyle() != null) {
            if (styles.containsKey(textField.getStyle().getName())) {
                DmJasperStyle style = styles.get(textField.getStyle().getName());
                if (!DmBaseShared.xNull(style.getFontName()).equals("")) {
                    textField.setFontName(textField.getStyle().getFontName());
                }
                if (style.getFontSize() != null) {
                    textField.setFontSize(textField.getStyle().getFontsize());
                }
                if (style.getBackColor() != null) {
                    textField.setBackcolor(textField.getStyle().getBackcolor());
                }
                if (style.getForeColor() != null) {
                    textField.setForecolor(textField.getStyle().getForecolor());
                }
                if (style.getBold() != null) {
                    textField.setBold(textField.getStyle().isBold());
                }
                if (style.getItalic() != null) {
                    textField.setItalic(textField.getStyle().isItalic());
                }
                if (style.getStrikeThrough() != null) {
                    textField.setStrikeThrough(textField.getStyle().isStrikeThrough());
                }
                if (style.getUnderline() != null) {
                    textField.setUnderline(textField.getStyle().isUnderline());
                }
            }
        }
//        System.out.println("inside visitTextField");
    }

    @Override
    public void visitComponentElement(JRComponentElement componentElement) {
//        System.out.println("inside visitComponentElement");
    }

    @Override
    public void visitGenericElement(JRGenericElement element) {
//        System.out.println("inside visitGenericElement");
    }

    public void compileReport() throws Exception {
        mainReport = compileJasper(reportFileJRXML);
    }

    private void compileSubReport(JRExpression subReportExpression) throws Exception {
        String subreportName = null;

        if (subReportExpression != null) {
            if (subReportExpression.getChunks().length > 0) {
                for (int i = 0; i < subReportExpression.getChunks().length; i++) {
                    if (subReportExpression.getChunks()[i] instanceof JRBaseExpressionChunk) {
                        DmJRBaseExpressionChunk chunk = new DmJRBaseExpressionChunk();
                        if (subReportExpression.getChunks()[i].getType() == JRExpressionChunk.TYPE_PARAMETER) {
                            subreportName = subReportExpression.getChunks()[i].getText();
                            chunk.setChunk(subReportExpression.getChunks()[i].getType(), subreportName);
                        } else {
                            chunk.setChunk(subReportExpression.getChunks()[i].getType(), null);
                        }
                        subReportExpression.getChunks()[i] = chunk;
                    }
                }
            }
        }

        if (subreportName != null) {
//            System.out.println("Subreport Name found...: " + subreportName);
            File subReportFile = new File(reportFileJRXML.getParentFile(), subreportName + ".jrxml");
            if (subReportFile.exists()) {
                JasperReport compiledSubReport = compileJasper(subReportFile);
                subreports.put(subreportName, compiledSubReport);
            }
        }
    }

    private JasperReport compileJasper(File reportFilePath) throws Exception {
        JasperReport jasperReport = JasperCompileManager.compileReport(reportFilePath.getAbsolutePath());
        JRStyle[] tmpStyles = jasperReport.getStyles();
        if (tmpStyles != null) {
            for (int i = 0; i < tmpStyles.length; i++) {
                if (styles.containsKey(tmpStyles[i].getName())) {
                    DmJasperStyle style = styles.get(tmpStyles[i].getName());
                    if (style.getBackColor() != null) {
                        tmpStyles[i].setBackcolor(style.getBackColor());
                    }
                    if (style.getForeColor() != null) {
                        tmpStyles[i].setForecolor(style.getForeColor());
                    }
                    if (style.getBold() != null) {
                        tmpStyles[i].setBold(style.getBold());
                    }
                    if (style.getFontName() != null) {
                        tmpStyles[i].setFontName(DmBaseShared.xNull(style.getFontName()));
                    }
                    if (style.getFontSize() != null) {
                        tmpStyles[i].setFontSize(style.getFontSize());
                    }
                    if (style.getItalic() != null) {
                        tmpStyles[i].setItalic(style.getItalic());
                    }
                    if (style.getStrikeThrough() != null) {
                        tmpStyles[i].setStrikeThrough(style.getStrikeThrough());
                    }
                    if (style.getUnderline() != null) {
                        tmpStyles[i].setUnderline(style.getUnderline());
                    }
                }
            }
        }
        JRElementsVisitor.visitReport(jasperReport, this);
        return jasperReport;
    }
}
