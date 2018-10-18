package agency.akcom.mmg.sherlock.ui.server.task;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import agency.akcom.mmg.sherlock.ui.server.pocket.PocketUtils;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrdersStats;

public class PocketMathImportTask {

	public static void main(String... args) {
		String date = "20170326";
		String startDate = getFromDate(date);
		String endDate = getToDate(date);
		ReportOrdersStats stats = PocketUtils.getReport(startDate, endDate);
		System.out.println(stats);
	}

	public static String getFromDate() {
		ZonedDateTime yesterday = ZonedDateTime.now().with(ChronoField.NANO_OF_DAY, 0).minusDays(1);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String date = dateTimeFormatter.format(yesterday);
		return date;
	}

	public static String getToDate() {
		ZonedDateTime today = ZonedDateTime.now().with(ChronoField.NANO_OF_DAY, 0);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String date = dateTimeFormatter.format(today);
		return date;
	}

	// for test by date
	public static String getFromDate(String date) {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd")
				.parseDefaulting(ChronoField.NANO_OF_DAY, 0).toFormatter().withZone(ZoneId.systemDefault());

		ZonedDateTime yesterday = ZonedDateTime.parse(date, formatter).with(ChronoField.NANO_OF_DAY, 0);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String d = dateTimeFormatter.format(yesterday);
		return d;
	}

	public static String getToDate(String date) {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd")
				.parseDefaulting(ChronoField.NANO_OF_DAY, 0).toFormatter().withZone(ZoneId.systemDefault());

		ZonedDateTime today = ZonedDateTime.parse(date, formatter).plusDays(1).with(ChronoField.NANO_OF_DAY, 0);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String d = dateTimeFormatter.format(today);
		return d;
	}
}
