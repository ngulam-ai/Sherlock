package agency.akcom.mmg.sherlock.ui.server.pocket;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import agency.akcom.mmg.sherlock.ui.server.pocket.client.PocketClient;
import agency.akcom.mmg.sherlock.ui.server.pocket.client.PocketClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportPublisher;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderInfo;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderStats;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderStats.Order;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.ReportDatum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PocketUtils {

	 /*
	 * 1. Get orders by date; 2. Get info this orders curl -i -H
	 * "Authorization: Token token=f569c162f3c4c9142d8813355928b272aec227b4801cfe0273b7e6120a4886ac"
	 * https://api.pocketmath.com/v3/campaigns
	 */

	static List<ReportDatum> reportList = null;
	
	static PocketClientBuilder POCKET_CLIENT_BUILDER = new PocketClientBuilder();
	static PocketClient pocketClient = POCKET_CLIENT_BUILDER.getPocketClient();
	private static String token;
	private static String startDate;
	private static String endDate;

	public static List<ReportDatum> getReport(String token_, String startDate_, String endDate_) {
		token = token_;
		startDate = startDate_;
		endDate = endDate_;
		//First report with 1 page
		ReportOrderStats stats = getReportOrders(startDate, endDate, 1);// TODO check to null (Maybe throw Exception, to restart task);
		
		reportList = new ArrayList<>(stats.getOrders().size());

		for (Order order : stats.getOrders()) {
			ReportDatum reportDatum = new ReportDatum();
			
			ReportOrderInfo reportOrders = getReportOrders(order.getId());
			List<ReportPublisher> reportPublisher = getReportPublisher(order.getId(), 1);
			for(ReportPublisher rep : reportPublisher) {
				rep.setReportDatum(reportDatum);
			}
			
			reportDatum.setOrder(order);
			if (reportOrders != null) {
				reportDatum.setInfoOrder(reportOrders);
			} else {
				log.error("Report info for orderId:" + order.getId() + " failed!");
			}
			
			reportDatum.setReportPublisher(reportPublisher);
			reportList.add(reportDatum);
		}
		return reportList; 
	}
	
	public static ReportOrderInfo getReportOrders(String id) {
		ReportOrderInfo report = pocketClient.orderDetails(token, id);
		
		int maxAttempts = 8; //last attempt (8) has max backoff 1.06 min

		for (int i = 0; i < maxAttempts; i++) {
			if (POCKET_CLIENT_BUILDER.getStatus() == 200) {
				return report;
			} else {
				backoff(i);
				report = pocketClient.orderDetails(token, id);
			}
		}
		return null;
	}
	
	public static List<ReportPublisher> getReportPublisher(String id, int page) {
		List<ReportPublisher> report = pocketClient.reportPublisher(token, id, startDate, endDate, page);
		
		int maxAttempts = 8; //last attempt (8) has max backoff 1.06 min
		
		for (int i = 0; i < maxAttempts; i++) {
			if (POCKET_CLIENT_BUILDER.getStatus() == 200) {
				
				JSONObject pagination = new JSONObject(
						POCKET_CLIENT_BUILDER.getHeaders().get("x-pagination").iterator().next());
				boolean lastPage = (boolean) pagination.get("last_page");

				if (lastPage == false) {
					List<ReportPublisher> nextReport = pocketClient.reportPublisher(token, id, startDate, endDate, ++page);
					if (nextReport != null) {
						report.addAll(nextReport);
					}
				}
				return report;
			} else {
				backoff(i);
				report = pocketClient.reportPublisher(token, id, startDate, endDate, page);
			}
		}
		return null;
	}

	public static ReportOrderStats getReportOrders(String startDate, String endDate, int page) {
		ReportOrderStats stats = pocketClient.reportOrdersStats(token, startDate, endDate, page);
		
		int maxAttempts = 8; //last attempt (8) has max backoff 1.06 min

		for (int i = 0; i < maxAttempts; i++) {
			if (POCKET_CLIENT_BUILDER.getStatus() == 200) {
				JSONObject pagination = new JSONObject(
						POCKET_CLIENT_BUILDER.getHeaders().get("x-pagination").iterator().next());
				
				boolean lastPage = (boolean) pagination.get("last_page");

				if (lastPage == false) {
					ReportOrderStats nextStats = getReportOrders(startDate, endDate, ++page);
					if (nextStats != null) {
						stats.getOrders().addAll(nextStats.getOrders());
					}
				}
				return stats;
			} else {
				backoff(i);
				stats = pocketClient.reportOrdersStats(token, startDate, endDate, page);
			}
		}
		log.error("PocketMath: Exceeded max attempts for getting Orders stats");
		return null;
	}
	
	public static void backoff(int i) {
		try {
			Thread.sleep(1000*i*i);
		} catch (InterruptedException e) {
			log.error(e.getLocalizedMessage() + ", StackTrace : " + e.getStackTrace().toString());
		}
	}
}
