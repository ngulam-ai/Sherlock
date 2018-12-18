package agency.akcom.mmg.sherlock.ui.server.facebook.client;

import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport;
import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport.AccountId;
import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport.ReportStat;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface FacebookClient {
	
	@RequestLine("GET /v3.2/me/adaccounts?access_token={token}")
//	@Headers("Authorization: Token token={token}")
	AccountId getAccountId(@Param("token") String token);

	@RequestLine("GET /v3.2/{account_id}/insights?level=campaign&" +
			"fields=ad_id,impressions,ad_name,frequency&" +
			"access_token={token}")
	ReportStat getReportStat(@Param("account_id") String account_id, @Param("token") String token);
}
