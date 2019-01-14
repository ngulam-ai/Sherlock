package agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.client;

import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model.PerfectReport.*;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model.AuthRequest;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface PerfectAudienceClient {
	
	@RequestLine("POST /auth")
	@Headers("Content-Type: application/json")
	Auth getAuth(AuthRequest authRequest);

	//TODO to check yesterday? Does it return realy yesterday interval?

	@RequestLine("GET /reports/campaign_report?interval=yesterday")
	@Headers("Authorization: {token}")
	CampaignReport campaignReport (@Param("token") String token);

	@RequestLine("GET /reports/ad_report?interval=yesterday&campaign_id={camp_id}")
	@Headers("Authorization: {token}")
	AdReport adReport (@Param("token") String token, @Param("camp_id") String camp_id);

}
