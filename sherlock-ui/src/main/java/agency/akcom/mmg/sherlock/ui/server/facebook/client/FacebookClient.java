package agency.akcom.mmg.sherlock.ui.server.facebook.client;

import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport;
import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport.AccountId;
import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport.ReportStat;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface FacebookClient {
	
	@RequestLine("GET /v3.2/me/adaccounts?access_token={token}")
	AccountId getAccountId(@Param("token") String token);

	//DSP_KEY name dsp
	//MODEL_KEY model - cpm
	//SOURCE_KEY Publisher name (source)
	//MEDIUM_KEY - Display
	//CONTENT_KEY Creative.name
	//CAMPAIGN_NAME_KEY
	//CAMPAIGN_ID_KEY
	//DATE_KEY

	//IMPRESSIONS_KEY
	//CLICKS_KEY
	//CONVERSION_KEY
	//SPEND_KEY

	@RequestLine("GET /v3.2/{account_id}/insights?" +
			"date_preset=lifetime&" +
			"fields=adset_id,impressions,ad_name,frequency," +
			"spend&" +
			"access_token={token}")
	ReportStat getReportStat(@Param("account_id") String account_id, @Param("token") String token);

	@RequestLine("GET /v3.2/{account_id}/adsets?" +
//			"level=ad&" +
//			"fields=name,configured_status,effective_status&" +
			"access_token={token}")
	ReportStat getAdSet(@Param("account_id") String account_id, @Param("token") String token);

	//120330000032299701

	@RequestLine("POST /v3.2/{account_id}/insights?" +
			"fields=impressions,spend,ad_id,adset_id" +
			"&level=ad&" +
//			"fields=name&" +
			"access_token={token}")
	ReportStat getAds(@Param("token") String token, @Param("account_id") String account_id);
}
