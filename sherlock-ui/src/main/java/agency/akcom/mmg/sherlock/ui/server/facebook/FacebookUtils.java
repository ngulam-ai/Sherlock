package agency.akcom.mmg.sherlock.ui.server.facebook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import agency.akcom.mmg.sherlock.ui.server.facebook.client.FacebookClient;
import agency.akcom.mmg.sherlock.ui.server.facebook.client.FacebookClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport;
import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport.AccountId;
import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport.Data;
import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport.ReportStat;
import net.sourceforge.htmlunit.corejs.javascript.commonjs.module.provider.StrongCachingModuleScriptProvider;
import org.json.JSONObject;

import agency.akcom.mmg.sherlock.ui.server.pocket.client.PocketClient;
import agency.akcom.mmg.sherlock.ui.server.pocket.client.PocketClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportPublisher;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderInfo;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderStats;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderStats.Order;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.ReportDatum;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FacebookUtils {


	static List<ReportDatum> reportList = null;
	
	static FacebookClientBuilder POCKET_CLIENT_BUILDER = new FacebookClientBuilder();
	static FacebookClient pocketClient = POCKET_CLIENT_BUILDER.getFacebookClient();
	private static String token= "EAANmRf7YVrgBAC8gAhgU3H1xKLONwt1tlrjpEwyaJm1btgqJI8mDTi5c290CoEqxB3nTmDTSVE15hMde3C4Ns1lyGZB1rZCkA3HtUiLJbXyuvtCuI4Yakyg4S3P9sguaJDvk9RfdsvGekVKmNuQW3Bz8zuQXsB7SjXHAPiRVldeR3bTxMHZC0eZAtXKNAlkIhcxMVK4PTc5ZCtLvXfmJcw6MJjjEY9e8SybWhkubwF1EmthBb5OtL";
	private static String startDate;
	private static String endDate;

	public static void getReport(){
		String accountId = getAccountId().getId(); //TODO to make for array;
		ReportStat reportStat = pocketClient.getReportStat(accountId, token);
	}

	public static Data getAccountId(){
		AccountId accountId = pocketClient.getAccountId(token);
		Data data = accountId.getData().get(0);
		return data;
	}
	
//	public static boolean checkingValidCredentials(String token) {
//		try {
//			pocketClient.reportTesting(token);
//			return true;
//		} catch (FeignException ex) {
//			if(ex.status() == 403) {
//				return false;
//			} else {
//				log.error(ex.getMessage());
//				return false;
//			}
//		}
//	}
//
//	public static List<ReportDatum> getReport(String token_, String startDate_, String endDate_) {
//		token = token_;
//		startDate = startDate_;
//		endDate = endDate_;
//		//First report with 1 page
//		ReportOrderStats stats = getReportOrders(startDate, endDate, 1);// TODO check to null (Maybe throw Exception, to restart task);
//
//		reportList = new ArrayList<>(stats.getOrders().size());
//
//		for (Order order : stats.getOrders()) {
//			ReportDatum reportDatum = new ReportDatum();
//
//			ReportOrderInfo reportOrders = getReportOrders(order.getId());
//			List<ReportPublisher> reportPublisher = getReportPublisher(order.getId(), 1);
//			for(ReportPublisher rep : reportPublisher) {
//				rep.setReportDatum(reportDatum);
//			}
//
//			reportDatum.setOrder(order);
//			if (reportOrders != null) {
//				reportDatum.setInfoOrder(reportOrders);
//			} else {
//				log.error("Report info for orderId:" + order.getId() + " failed!");
//			}
//
//			reportDatum.setReportPublisher(reportPublisher);
//			reportList.add(reportDatum);
//		}
//		return reportList;
//	}
//
//	public static ReportOrderInfo getReportOrders(String id) {
//		ReportOrderInfo report = pocketClient.orderDetails(token, id);
//
//		int maxAttempts = 8; //last attempt (8) has max backoff 1.06 min
//
//		for (int i = 0; i < maxAttempts; i++) {
//			if (POCKET_CLIENT_BUILDER.getStatus() == 200) {
//				return report;
//			} else {
//				backoff(i);
//				report = pocketClient.orderDetails(token, id);
//			}
//		}
//		return null;
//	}
//
//	public static List<ReportPublisher> getReportPublisher(String id, int page) {
//		List<ReportPublisher> report = pocketClient.reportPublisher(token, id, startDate, endDate, page);
//
//		int maxAttempts = 8; //last attempt (8) has max backoff 1.06 min
//
//		for (int i = 0; i < maxAttempts; i++) {
//			if (POCKET_CLIENT_BUILDER.getStatus() == 200) {
//
//				JSONObject pagination = new JSONObject(
//						POCKET_CLIENT_BUILDER.getHeaders().get("x-pagination").iterator().next());
//				boolean lastPage = (boolean) pagination.get("last_page");
//
//				if (lastPage == false) {
//					List<ReportPublisher> nextReport = pocketClient.reportPublisher(token, id, startDate, endDate, ++page);
//					if (nextReport != null) {
//						report.addAll(nextReport);
//					}
//				}
//				return report;
//			} else {
//				backoff(i);
//				report = pocketClient.reportPublisher(token, id, startDate, endDate, page);
//			}
//		}
//		return null;
//	}
//
//	public static ReportOrderStats getReportOrders(String startDate, String endDate, int page) {
//		ReportOrderStats stats = pocketClient.reportOrdersStats(token, startDate, endDate, page);
//
//		int maxAttempts = 8; //last attempt (8) has max backoff 1.06 min
//
//		for (int i = 0; i < maxAttempts; i++) {
//			if (POCKET_CLIENT_BUILDER.getStatus() == 200) {
//				JSONObject pagination = new JSONObject(
//						POCKET_CLIENT_BUILDER.getHeaders().get("x-pagination").iterator().next());
//
//				boolean lastPage = (boolean) pagination.get("last_page");
//
//				if (lastPage == false) {
//					ReportOrderStats nextStats = getReportOrders(startDate, endDate, ++page);
//					if (nextStats != null) {
//						stats.getOrders().addAll(nextStats.getOrders());
//					}
//				}
//				return stats;
//			} else {
//				backoff(i);
//				stats = pocketClient.reportOrdersStats(token, startDate, endDate, page);
//			}
//		}
//		log.error("PocketMath: Exceeded max attempts for getting Orders stats");
//		return null;
//	}
//
//	public static void backoff(int i) {
//		try {
//			Thread.sleep(1000*i*i);
//		} catch (InterruptedException e) {
//			log.error(e.getLocalizedMessage() + ", StackTrace : " + e.getStackTrace().toString());
//		}
//	}
}
