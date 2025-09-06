package at.fhtw.tourplanner.report.util;

import com.itextpdf.text.Font;

public class FontUtils {

    public static final Font.FontFamily DEFAULT_FONT_FAMILY = Font.FontFamily.HELVETICA;

    private FontUtils() {}

    public static Font getTitle() {
        return new Font(DEFAULT_FONT_FAMILY, 18, Font.BOLD);
    }

    public static Font getSubtitle() {
        return new Font(DEFAULT_FONT_FAMILY, 14, Font.NORMAL);
    }

    public static Font getLabel() {
        return new Font(DEFAULT_FONT_FAMILY, 12, Font.BOLD);
    }

    public static Font getText() {
        return new Font(DEFAULT_FONT_FAMILY, 12, Font.NORMAL);
    }
}
