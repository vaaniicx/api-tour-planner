package at.fhtw.tourplanner.report.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfTableUtils {

    private PdfTableUtils() {}

    public static void addHeaderCell(PdfPTable table, String title, Font font) {
        addHeaderCell(table, title, font, BaseColor.LIGHT_GRAY);
    }

    public static void addHeaderCell(PdfPTable table, String title, Font font, BaseColor backgroundColor) {
        PdfPCell headerCell = new PdfPCell(new Phrase(title, font));
        headerCell.setBackgroundColor(backgroundColor);
        table.addCell(headerCell);
    }
}
