package at.fhtw.tourplanner.report.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

public class PdfDocumentBuilder {

    private static final int PAGE_MARGIN = 50;

    private PdfDocumentBuilder() {}

    public static Document createDocument(ByteArrayOutputStream outputStream) {
        try {
            Document document = new Document(PageSize.A4, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
            PdfWriter.getInstance(document, outputStream);
            return document;
        } catch (DocumentException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void addHeader(Document document, String title) throws DocumentException {
        Paragraph header = new Paragraph(title, FontUtils.getTitle());
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
    }

    public static void addSubHeader(Document document, String title) throws DocumentException {
        Paragraph subHeader = new Paragraph(title, FontUtils.getSubtitle());
        subHeader.setAlignment(Element.ALIGN_CENTER);
        subHeader.setSpacingAfter(48);
        document.add(subHeader);
    }
}
