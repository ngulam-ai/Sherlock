package agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.client;

import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model.Auth;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model.AuthRequest;
import feign.Headers;
import feign.RequestLine;

public interface PerfectAudienceClient {
	
	@RequestLine("POST /auth")
	@Headers("Content-Type: application/json")
	Auth getAuth(AuthRequest authRequest);
	
//	@RequestLine("GET /v3/orders/{id}")
//	@Headers("Authorization: Token token={token}")
//	ReportOrderInfo orderDetails(@Param("token") String token, @Param("id") String order_id);
//	
//	@RequestLine("GET /v3/orders/{id}/stats/publishers.json")
//	@Headers("Authorization: Token token={token}")
//	ReportPublisher reportPublisher(@Param("token") String token, @Param("id") String order_id);

}
