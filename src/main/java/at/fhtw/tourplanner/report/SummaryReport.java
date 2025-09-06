package at.fhtw.tourplanner.report;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.report.util.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class SummaryReport implements PdfReport {

    private static final int LOG_TABLE_COLUMNS = 4;

    private final List<Tour> tours;

    @Override
    public byte[] generateReport() throws DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = PdfDocumentBuilder.createDocument(outputStream);
            document.open();

            PdfDocumentBuilder.addHeader(document, "Tour Zusammenfassung");
            PdfDocumentBuilder.addSubHeader(document, "Alle statistischen Daten zu Tour & Einträgen");
            addStatisticalAnalysis(document, tours);

            document.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addStatisticalAnalysis(Document document, List<Tour> tours) throws DocumentException {
        document.add(new Paragraph("Alle Touren", FontUtils.getLabel()));

        PdfPTable table = new PdfPTable(LOG_TABLE_COLUMNS);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        PdfTableUtils.addHeaderCell(table, "Name der Tour", FontUtils.getLabel());
        PdfTableUtils.addHeaderCell(table, "Durchschnittliche Zeit", FontUtils.getLabel());
        PdfTableUtils.addHeaderCell(table, "Durchschnittliche Distanz", FontUtils.getLabel());
        PdfTableUtils.addHeaderCell(table, "Durchschnittliche Bewertung", FontUtils.getLabel());

        tours.forEach(tour -> {
            table.addCell(tour.getName());
            table.addCell(DurationFormatter.format(tour.getAverageDuration()));
            table.addCell(DistanceConverter.metersToKilometers(tour.getAverageDistance()) + " km");
            table.addCell(String.valueOf(tour.getAverageRating()));
        });

        document.add(table);
    }
}
