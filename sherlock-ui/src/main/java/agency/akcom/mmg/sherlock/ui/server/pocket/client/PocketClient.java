package agency.akcom.mmg.sherlock.ui.server.pocket.client;

import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrders;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrdersStats;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface PocketClient {
	@RequestLine("GET /v3/stats/orders?from={start_date}&to={end_date}&page={page}&per=500")
	@Headers("Authorization: Token token=f569c162f3c4c9142d8813355928b272aec227b4801cfe0273b7e6120a4886ac")
	ReportOrdersStats reportOrdersStats(@Param("start_date") String startDate, @Param("end_date") String endDate, @Param("page") int page);
	
	@RequestLine("GET /v3/orders/{id}")
	@Headers("Authorization: Token token=f569c162f3c4c9142d8813355928b272aec227b4801cfe0273b7e6120a4886ac")
	ReportOrders orderDetails(@Param("id") String order_id);
}
