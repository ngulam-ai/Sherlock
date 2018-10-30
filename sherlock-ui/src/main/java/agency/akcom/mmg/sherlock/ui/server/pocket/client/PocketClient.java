package agency.akcom.mmg.sherlock.ui.server.pocket.client;

import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderInfo;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderStats;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportPublisher;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface PocketClient {
	static final String TOKEN = "Authorization: Token token=f569c162f3c4c9142d8813355928b272aec227b4801cfe0273b7e6120a4886ac";
	
	@RequestLine("GET /v3/stats/orders?from={start_date}&to={end_date}&page={page}&per=500")
	@Headers(TOKEN)
	ReportOrderStats reportOrdersStats(@Param("start_date") String startDate, @Param("end_date") String endDate, @Param("page") int page);
	
	@RequestLine("GET /v3/orders/{id}")
	@Headers(TOKEN)
	ReportOrderInfo orderDetails(@Param("id") String order_id);
	
	@RequestLine("GET /v3/orders/{id}/stats/publishers.json")
	@Headers(TOKEN)
	ReportPublisher reportPublisher(@Param("id") String order_id);
}
