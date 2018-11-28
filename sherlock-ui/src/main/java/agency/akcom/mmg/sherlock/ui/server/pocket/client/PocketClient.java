package agency.akcom.mmg.sherlock.ui.server.pocket.client;

import java.util.List;

import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportPublisher;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderInfo;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderStats;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface PocketClient {

	@RequestLine("GET /v3/stats/orders?from={start_date}&to={end_date}&page={page}&per=500")
	@Headers("Authorization: Token token={token}")
	ReportOrderStats reportOrdersStats(@Param("token") String token, @Param("start_date") String startDate,
			@Param("end_date") String endDate, @Param("page") int page);

	@RequestLine("GET /v3/orders/{id}")
	@Headers("Authorization: Token token={token}")
	ReportOrderInfo orderDetails(@Param("token") String token, @Param("id") String order_id);

	/**
	 * @param String token, String order_id, String startDate, String endDate, int page
	 */
	@RequestLine("GET /v3/orders/{id}/stats/publishers?from={start_date}&to={end_date}&page={page}&per=500")
	@Headers("Authorization: Token token={token}")
	List<ReportPublisher> reportPublisher(@Param("token") String token, @Param("id") String order_id,
			@Param("start_date") String startDate, @Param("end_date") String endDate, @Param("page") int page);

}
