package dataman.dmBase.dmBaseJasper.server;

import com.google.gwt.user.client.Window;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;

public class QrCode extends DmBaseJasper {

    String charset = "UTF-8";
    private int qrWidth = 50, qrHeight = 50;
    private double rotate = 0;

    public QrCode(String qrText, String path, String charset, int qrWidth, int qrHeight) throws WriterException, IOException {

        this.charset = charset;
        this.qrWidth = qrWidth;
        this.qrHeight = qrHeight;

//        int size = 125;
        int size = 250;
        String fileType = "png";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        File qrFile = new File(path);
        createQRImage(qrFile, qrText, size, fileType, path);
        qrPath = path;
    }

    private String createQRImage(File qrFile, String qrCodeText, int size, String fileType, String path)
            throws WriterException, IOException {
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        ImageIO.write(image, fileType, qrFile);
        return path;
    }

}
