package agency.akcom.mmg.sherlock.ui.server.avazu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agency.akcom.mmg.sherlock.ui.server.avazu.client.AvazuClient;
import agency.akcom.mmg.sherlock.ui.server.avazu.client.AvazuClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Auth;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.AuthRequest;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Campaigns;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Campaigns.CampaignDatum;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.CampaignsRequest;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Report;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Report.ReportDatum;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.ReportRequest;
import agency.akcom.mmg.sherlock.ui.server.configConnection.SecretIdConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvazuUtils {

	private final AvazuClientBuilder avazuClientBuilder;
	private final AvazuClient avazuClient;
	AuthRequest authRequest;
	
	public AvazuUtils(SecretIdConnection credentials){
		avazuClientBuilder  = new AvazuClientBuilder();
		avazuClient = avazuClientBuilder.getAvazuClient();
		authRequest = new AuthRequest();
		authRequest.setClient_id(credentials.getClientId());
		authRequest.setClient_secret(credentials.getClientSecret());
	}
	
	//For test
	public AvazuUtils(AuthRequest authRequest) {
		avazuClientBuilder  = new AvazuClientBuilder();
		avazuClient = avazuClientBuilder.getAvazuClient();
		this.authRequest = authRequest;
	}
	
	/**
	 * @return "true" if credentials are valid, false - credentials invalid
	 */
	public boolean checkingValidCredentials() {
		Auth auth = avazuClient.getAuth(authRequest);
		if(auth.getAccess_token() != null) {
			return true;
		} else {
			return false;
		}
	}

	public List<ReportDatum> getFullReportDatum(String command, String startdate, String enddate,
			String groupby) {

		Auth auth = avazuClient.getAuth(authRequest);

		int page = 1;
		int lastPage = 0;
		List<ReportDatum> datums = null;

		do {
			ReportRequest reportRequest = new ReportRequest(auth.getAccess_token(), command, startdate, enddate,
					groupby, page);
			Report report = avazuClient.getReport(reportRequest);
			log.info("Page " + page + ": " + report.toString());

			if (lastPage == 0) {
				// first loop - do initializations
				datums = new ArrayList<ReportDatum>(report.getTotalcount());
				lastPage = report.getTotalcount() / report.getPagemaxcount() + 1;
			}

			datums.addAll(report.getData());

		} while (++page <= lastPage);

		return datums;
	}

	public List<CampaignDatum> getFullCampaignsDatum() {

		Auth auth = avazuClient.getAuth(authRequest);

		// set number of records per page very big to avoid to use paging
		CampaignsRequest campaignsRequest = new CampaignsRequest(auth.getAccess_token(), "get", 1, Integer.MAX_VALUE);
		Campaigns campaigns = avazuClient.getCampaigns(campaignsRequest);
		log.info(campaigns.toString());

		return campaigns.getData();
	}

	public Map<String, String> getCampaignsWithBidTypes() {
		List<CampaignDatum> datums = getFullCampaignsDatum();
		Map<String, String> campaignsWithBidTypes = new HashMap<>(datums.size());
		for (CampaignDatum datum : datums) {
			// TODO maybe should be enhanced to correctly process CPA type
			campaignsWithBidTypes.put(datum.getId(), (datum.getBidtype() == 4 ? "cpc" : "cpm"));
		}

		return campaignsWithBidTypes;
	}

}
