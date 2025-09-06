package at.fhtw.tourplanner.report;

import com.itextpdf.text.DocumentException;

public interface PdfReport {

    byte[] generateReport() throws DocumentException;
}
