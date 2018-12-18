package agency.akcom.mmg.sherlock.ui.server.facebook.client;

import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderStats;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface FacebookClient {
	
	@RequestLine("GET /v3.2/")
	@Headers("Authorization: Token token={token}")
	ReportOrderStats reportOrdersStats(@Param("token") String token, @Param("start_date") String startDate,
			@Param("end_date") String endDate, @Param("page") int page);

}
