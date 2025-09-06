package at.fhtw.tourplanner.report;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.report.util.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.io.IOException;

@RequiredArgsConstructor
public class SingleTourReport implements PdfReport {

    private static final int LOG_TABLE_COLUMNS = 6;

    private final Tour tour;

    @Override
    public byte[] generateReport() throws DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = PdfDocumentBuilder.createDocument(outputStream);
            document.open();

            PdfDocumentBuilder.addHeader(document, "Tour Bericht");
            PdfDocumentBuilder.addSubHeader(document, "Alle Details zu Tour & Einträgen");
            addTourDetails(document, tour);
            addLogEntries(document, tour.getLogs());

            document.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addTourDetails(Document document, Tour tour) throws DocumentException {
        document.add(new Paragraph("Details", FontUtils.getLabel()));

        Font textFont = FontUtils.getText();
        document.add(new Paragraph("Name der Tour: " + tour.getName(), textFont));
        document.add(new Paragraph("Beschreibung: " + tour.getDescription(), textFont));
        document.add(new Paragraph("Transportmittel: " + tour.getTransportType(), textFont));
        document.add(new Paragraph("Distanz: " + DistanceConverter.metersToKilometers(tour.getDistanceInMeters()) + " km", textFont));
        document.add(new Paragraph("Dauer: " + DurationFormatter.format(tour.getDurationInSeconds()), textFont));

        document.add(Chunk.NEWLINE);
    }

    private void addLogEntries(Document document, List<TourLog> logs) throws DocumentException {
        document.add(new Paragraph("Alle Einträge", FontUtils.getLabel()));

        PdfPTable table = new PdfPTable(LOG_TABLE_COLUMNS);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        PdfTableUtils.addHeaderCell(table, "Datum", FontUtils.getLabel());
        PdfTableUtils.addHeaderCell(table, "Kommentar", FontUtils.getLabel());
        PdfTableUtils.addHeaderCell(table, "Schwierigkeit", FontUtils.getLabel());
        PdfTableUtils.addHeaderCell(table, "Bewertung", FontUtils.getLabel());
        PdfTableUtils.addHeaderCell(table, "Distanz", FontUtils.getLabel());
        PdfTableUtils.addHeaderCell(table, "Dauer", FontUtils.getLabel());

        logs.forEach(log -> {
            table.addCell(log.getDate().toString());
            table.addCell(log.getComment());
            table.addCell(String.valueOf(log.getDifficulty()));
            table.addCell(String.valueOf(log.getRating()));
            table.addCell(DistanceConverter.metersToKilometers(log.getDistanceInMeters()) + " km");
            table.addCell(DurationFormatter.format(log.getDurationInSeconds()));
        });

        document.add(table);
    }
}
