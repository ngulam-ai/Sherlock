package agency.akcom.mmg.sherlock.ui.server.pocket;

import java.util.List;

import org.json.JSONObject;

import agency.akcom.mmg.sherlock.ui.server.pocket.client.PocketClient;
import agency.akcom.mmg.sherlock.ui.server.pocket.client.PocketClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrders;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrdersStats;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrdersStats.Order;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.ReportDatum;

public class PocketUtils {

	private static final PocketClientBuilder POCKET_CLIENT_BUILDER = new PocketClientBuilder();
	private static final PocketClient pocketClient = POCKET_CLIENT_BUILDER.getPocketClient();

	/*
	 * 1. Get orders by date; 2. Get info this orders curl -i -H
	 * "Authorization: Token token=f569c162f3c4c9142d8813355928b272aec227b4801cfe0273b7e6120a4886ac"
	 * https://api.pocketmath.com/v3/campaigns
	 */

	List<ReportDatum> result = null;

	public static ReportOrdersStats getReport(String startDate, String endDate) {
		ReportOrdersStats stats = getReportOrdersStats(startDate, endDate, 1);// TODO check to null;

		for (Order order : stats.getOrders()) {
			ReportOrders reportOrders = getReportOrders(order.getId());// TODO check to null;
			ReportDatum rep = new ReportDatum();
			rep.setOrder(order);
			if (reportOrders != null) {
				rep.setInfoOrder(reportOrders);
			}
			System.out.println(reportOrders.getCampaign_id());
		}
		return stats;
	}

	public static ReportOrders getReportOrders(String id) {
		ReportOrders report = pocketClient.orderDetails(id);
		int maxTry = 5;

		for (int i = 0; i < maxTry; i++) {
			if (POCKET_CLIENT_BUILDER.getStatus() == 200) {
				return report;
			} else {
				report = pocketClient.orderDetails(id);
			}
		}
		return null;
	}

	public static ReportOrdersStats getReportOrdersStats(String startDate, String endDate, int page) {
		ReportOrdersStats stats = pocketClient.reportOrdersStats(startDate, endDate, page);
		int maxTry = 5;

		for (int i = 0; i < maxTry; i++) {
			if (POCKET_CLIENT_BUILDER.getStatus() == 200) {
				JSONObject pagination = new JSONObject(
						POCKET_CLIENT_BUILDER.getHeaders().get("x-pagination").iterator().next());
				boolean lastPage = (boolean) pagination.get("last_page");

				if (lastPage == false) {
					ReportOrdersStats nextStats = getReportOrdersStats(startDate, endDate, ++page);
					if (nextStats != null) {
						stats.getOrders().addAll(nextStats.getOrders());
					}
				}
				return stats;
			} else {
				stats = pocketClient.reportOrdersStats(startDate, endDate, page);
			}
		}
		// TODO logging exception;
		return null;
	}
}
