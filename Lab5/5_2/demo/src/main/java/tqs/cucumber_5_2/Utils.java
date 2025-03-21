package tqs.cucumber_5_2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {

    public static LocalDate isoTextToLocalDate(String isoDate) {
        try {
            return LocalDate.parse(isoDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid ISO date: " + isoDate, e);
        }
    }

    public static LocalDate localDateFromDateParts(String year, String month, String day) {
        return LocalDate.of(
            Integer.parseInt(year),
            Integer.parseInt(month),
            Integer.parseInt(day)
        );
    }
}
