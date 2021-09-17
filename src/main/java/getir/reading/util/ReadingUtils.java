package getir.reading.util;

import getir.reading.constants.ReadingAppConstants;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class ReadingUtils {
    static DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern(ReadingAppConstants.DATE_TIME_FORMATTER);

    public static String getCurrentTimeString() {
        Date currDate = new Date();
        LocalDateTime localDateTime = currDate.toInstant()
                .atZone(ZoneId.of(ReadingAppConstants.TIMEZONE_INDIA))
                .toLocalDateTime();
        return localDateTime.format(formatter);
    }

    public static Timestamp getCurrentTimestamp() {
        ZonedDateTime currTime = ZonedDateTime
                .now(ZoneId.of(ReadingAppConstants.TIMEZONE_INDIA));
        return Timestamp.valueOf(currTime.toLocalDateTime());
    }

    public static String getFormattedTimestamp(Timestamp timestamp) {
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        return dateTime.format(formatter);
    }

    public static Timestamp convertStringToTimestamp(String timeString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ReadingAppConstants.DATE_TIME_FORMATTER);
        Date parsedDate = dateFormat.parse(timeString);
        return new Timestamp(parsedDate.getTime());
    }

    public static Timestamp startTimestamp(int month) throws ParseException {
        String thisYear = new SimpleDateFormat("yyyy").format(new Date());
        String thisMonth = String.format("%02d", month);
        String thisDay = "01";
        String thisTime = "00:00:00";
        String newTimeString = thisYear + "-" + thisMonth + "-" + thisDay + " " + thisTime;
        return convertStringToTimestamp(newTimeString);
    }

    public static Timestamp endTimestamp(int month) {
        String thisYear = new SimpleDateFormat("yyyy").format(new Date());
        String thisMonth = String.format("%02d", month);
        String thisDay = "01";
        String thisTime = "23:59:59";
        String date = thisYear + "-" + thisMonth + "-" + thisDay + " " + thisTime;
        LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter
                        .ofPattern(ReadingAppConstants.DATE_TIME_FORMATTER))
                .with(TemporalAdjusters.lastDayOfMonth());

        return Timestamp.valueOf(localDateTime);
    }
}
