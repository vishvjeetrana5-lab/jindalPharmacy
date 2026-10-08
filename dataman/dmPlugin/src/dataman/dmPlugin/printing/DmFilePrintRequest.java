package dataman.dmPlugin.printing;

import dataman.dmBase.self.shared.DmEnum;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;


public class DmFilePrintRequest implements Runnable {

    private String filePath;
    private String printerName;
    private int noOfCopies;
    private String reportUrl;
    private String userAgent;
    private DmEnum.DmPrinterImageType printingType;

    public DmFilePrintRequest(String reportUrl, String userAgent, DmEnum.DmPrinterImageType printingType, String printerName, int noOfCopies) {
        this.reportUrl = reportUrl;
        this.userAgent = userAgent;
        this.printerName = printerName;
        this.noOfCopies = noOfCopies;
        this.printingType = printingType;
    }

    public String getReportUrl() {
        return filePath;
    }

    public String getPrinterName() {
        return printerName;
    }

    public int getNoOfCopies() {
        return noOfCopies;
    }

    public DmEnum.DmPrinterImageType getPrintingType() {
        return printingType;
    }

    private String downloadReport(String urlToDownload, String userAgent) {
        String localFilePath = "";
        try {

            HttpURLConnection http = null;
            localFilePath = File.createTempFile("tempDm", ".pdf").getCanonicalPath();
            InputStream ins = null;
            FileOutputStream fos = new FileOutputStream(localFilePath);

            try {
                URL url = new URL(urlToDownload);

                System.out.println("trying to call url : " + urlToDownload);

                http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setRequestProperty("user-agent", userAgent);

                System.out.println("trying to read from client stream...");

                int rCode = http.getResponseCode();

                ins = http.getInputStream();

                byte[] BUFFER = new byte[1024 * 1024];
                int readLen = ins.read(BUFFER);
                while (readLen != -1) {
                    fos.write(BUFFER, 0, readLen);
                    readLen = ins.read(BUFFER);
                }

                System.out.println("client response status is " + rCode);

                if (rCode == HttpServletResponse.SC_OK) {
                    System.out.println("Report has downloaded...");
                } else {
                    System.out.println("Unable to fetch report...");
                }
            } catch (Exception ex) {
                throw ex;
            } finally {
                if (fos != null) {
                    fos.close();
                }
                if (ins != null) {
                    ins.close();
                }
                if (http != null) {
                    http.disconnect();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return localFilePath;
    }

    @Override
    public void run() {
        try {

            System.out.println("Trying to download Report : " + reportUrl);

            this.filePath = downloadReport(reportUrl, userAgent);

            System.out.println("Trying to print on Printer : " + printerName);

            PrintRequestAttributeSet atts = new HashPrintRequestAttributeSet();

            atts.add(new Copies(1));

            PrintService printService = getSpecifiedPrinterService(printerName, atts);

            File fileToPrint = new File(filePath);
            try {
                if (printService != null) {
                    PDDocument pdfDocument = PDDocument.load(fileToPrint);

                    PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);

                    initFilePrint(printService, atts, pdfDocument, pdfRenderer);
                }
            } finally {
                if (fileToPrint != null) {
                    if ((fileToPrint.exists()) && (fileToPrint.isFile())) {
                        boolean deleted = fileToPrint.delete();
                        System.out.println((deleted) ? "File : " + filePath + " is deleted successfully..." : "Unable to delete File : " + filePath);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initFilePrint(PrintService printService, PrintRequestAttributeSet printAttributes, final PDDocument pdfDocument, final PDFRenderer pdfRenderer) throws Exception {
        try {

            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(printService);

            job.setPageable(new Pageable() {
                @Override
                public int getNumberOfPages() {
                    return pdfDocument.getNumberOfPages();
                }

                @Override
                public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
                    PDPage pdfPage = pdfDocument.getPage(pageIndex);
                    double pageWidth = pdfPage.getMediaBox().getWidth();
                    double pageHeight = pdfPage.getMediaBox().getHeight();

                    PageFormat pageFormat = job.defaultPage();
                    Paper paper = pageFormat.getPaper();
                    paper.setSize(pageWidth, pageHeight);
                    paper.setImageableArea(0d, 0d, pageWidth, pageHeight);

                    pageFormat.setPaper(paper);

                    if (pdfPage.getRotation() == 0d) {
                        pageFormat.setOrientation(PageFormat.PORTRAIT);
                    } else {
                        pageFormat.setOrientation(PageFormat.LANDSCAPE);
                    }

                    return pageFormat;
                }

                @Override
                public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
                    return new Printable() {
                        @Override
                        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                            int pageExists = Printable.NO_SUCH_PAGE;
                            try {
                                GraphicsConfiguration conf = ((Graphics2D) graphics).getDeviceConfiguration();

                                AffineTransform trans = conf.getDefaultTransform();
                                double dpi = trans.transform(new Point2D.Float(72, 0), null).getX();

                                if (pageIndex < pdfDocument.getNumberOfPages()) {
                                    pageExists = Printable.PAGE_EXISTS;
                                }

                                if (pageExists == Printable.PAGE_EXISTS) {
                                    Graphics2D graphics2D = (Graphics2D) graphics;
                                    graphics2D.setBackground(Color.white);

                                    graphics2D.translate((int) pageFormat.getImageableX(),
                                            (int) pageFormat.getImageableY());

                                    BufferedImage pdfImage = pdfRenderer.renderImageWithDPI(pageIndex, (float) dpi, ((printingType != DmEnum.DmPrinterImageType.RGB) ? ImageType.BINARY : ImageType.RGB));

                                    graphics.drawImage(pdfImage, (int) pageFormat.getImageableX(), (int) pageFormat.getImageableY(), (int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight(), null);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return pageExists;
                        }
                    };
                }
            });

            job.print(printAttributes);

        } finally {
            if (pdfDocument != null) {
                pdfDocument.close();
            }
        }
    }

    private PrintService getSpecifiedPrinterService(String printerName, PrintRequestAttributeSet printAttributes) throws Exception {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, printAttributes);

        PrintService printablePrintService = null;
        for (int i = 0; i < services.length; i++) {
            if (services[i].getName().equalsIgnoreCase(printerName)) {
                printablePrintService = services[i];
                break;
            }
        }

        return printablePrintService;
    }

}
