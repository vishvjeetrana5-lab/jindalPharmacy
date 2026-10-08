package dataman.dmBase.dmBaseJasper.server;

import net.sf.jasperreports.engine.JRAbstractRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

public class DmWaterMark extends JRAbstractRenderer {

    private String watermarkText = "Watermark";
    private double rotate = -50;
    private int waterMarkWidth = 595, waterMarkHeight = 700, leftMargin = 0, fontSize = 110;

    public DmWaterMark(String watermarkText, double rotate, int leftMargin, int waterMarkWidth, int waterMarkHeight, int fontSize) {
        this.watermarkText = watermarkText;
        this.rotate = rotate;
        this.waterMarkWidth = waterMarkWidth;
        this.waterMarkHeight = waterMarkHeight;
        this.fontSize = fontSize;
        this.leftMargin = leftMargin;
    }

    @Override
    public byte getType() {
        return RenderableTypeEnum.SVG.getValue();
    }

    @Override
    public byte getImageType() {
        return ImageTypeEnum.UNKNOWN.getValue();
    }

    @Override
    public Dimension2D getDimension() throws JRException {
//        return new Dimension(waterMarkWidth - 2 * 40, waterMarkHeight);
        return new Dimension(waterMarkWidth, waterMarkHeight);
    }

    @Override
    public byte[] getImageData() throws JRException {
        return new byte[0];
    }

//    @Override
//    public void render(Graphics2D g2, Rectangle2D rectangle) throws JRException {
//        AffineTransform originalTransform = g2.getTransform();
//
//        g2.translate(rectangle.getX() + 100, rectangle.getMaxY());
//        g2.rotate(rotate * Math.PI / 180);
//
//        Font font = new Font("Tahoma", Font.BOLD, 110);
//        Shape shape = font.createGlyphVector(g2.getFontRenderContext(), watermarkText).getOutline();
//        g2.setColor(new Color(204, 204, 204));
//        g2.fill(shape);
//        g2.draw(shape);
//        g2.setFont(font);
//
//        g2.setTransform(originalTransform);
//    }
    @Override
    public void render(Graphics2D g2, Rectangle2D rectangle) throws JRException {
        AffineTransform originalTransform = g2.getTransform();

        g2.translate(rectangle.getX() + leftMargin, rectangle.getMaxY());
        g2.rotate(rotate * Math.PI / 180);

        Font font = new Font("Tahoma", Font.BOLD, fontSize);
        Shape shape = font.createGlyphVector(g2.getFontRenderContext(), watermarkText).getOutline();
        g2.setColor(new Color(204, 204, 204));
        g2.fill(shape);
        g2.draw(shape);
        g2.setFont(font);

        g2.setTransform(originalTransform);
    }

}
