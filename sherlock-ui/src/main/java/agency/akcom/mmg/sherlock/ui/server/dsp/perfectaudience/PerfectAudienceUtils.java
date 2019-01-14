package agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience;

import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.client.PerfectAudienceClient;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.client.PerfectAudienceClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model.AuthRequest;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model.PerfectReport.*;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model.ReportDatum;

import java.util.ArrayList;
import java.util.List;

public class PerfectAudienceUtils {
	//docs https://support.perfectaudience.com/952506-Reporting-API
	static PerfectAudienceClientBuilder audienceClientBuilder = new PerfectAudienceClientBuilder();
	static PerfectAudienceClient audienceClient = audienceClientBuilder.getPerfectAudienceClient();

	public static List<ReportDatum> getReport(String email, String pass){

		AuthRequest authRequest = new AuthRequest(email, pass);
		String token = audienceClient.getAuth(authRequest).getToken();

		CampaignReport campaignReport = audienceClient.campaignReport(token);
		if (campaignReport.getCampaignsReportList() == null) {
			return null;
		}

		List<ReportDatum> reportList = new ArrayList<>();

		for (CampaignStats camp : campaignReport.getCampaignsReportList()){
			AdReport adReport = audienceClient.adReport(token, camp.getCampaign_id());

			ReportDatum report = new ReportDatum();
			report.setAdStatsList(adReport.getAdStats());
			report.setCampaignStats(camp);
		}

		return reportList;
	}


	public static void main(String[] args) {
			String email = "dchubiryaev@akolchin.com";
			String password = "436333612perfect";

		List<ReportDatum> reportData = getReport(email, password);

	}
}
