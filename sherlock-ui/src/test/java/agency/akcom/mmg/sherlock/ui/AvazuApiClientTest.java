package agency.akcom.mmg.sherlock.ui;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import agency.akcom.mmg.sherlock.ui.server.avazu.AvazuUtils;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(JUnit4.class)
public class AvazuApiClientTest {

	private AvazuClient avazuClient;

	@Before
	public void setup() {
		log.info("TEST SETUP");
		AvazuClientBuilder avazuClientBuilder = new AvazuClientBuilder();
		avazuClient = avazuClientBuilder.getAvazuClient();
	}

	@Test
	@Ignore
	public void testReport() throws Exception {
		AuthRequest authRequest = new AuthRequest();
		log.info(authRequest.toString());

		Auth auth = avazuClient.getAuth(authRequest);
		assertNotNull(auth);
		log.info(auth.toString());

		ReportRequest reportRequest = new ReportRequest(auth.getAccess_token(), "creative", "2018-05-30", "2018-05-30",
				"site", 1);
		log.info(reportRequest.toString());

		Report report = avazuClient.getReport(reportRequest);
		assertNotNull(report);
		log.info(report.toString());

		// for (Datum datum : report.getData()) {
		// log.info("{}", datum);
		// }

	}

	@Test
	@Ignore
	public void testCampaign() throws Exception {
		AuthRequest authRequest = new AuthRequest();
		log.info(authRequest.toString());

		Auth auth = avazuClient.getAuth(authRequest);
		assertNotNull(auth);
		log.info(auth.toString());

		CampaignsRequest campaignsRequest = new CampaignsRequest(auth.getAccess_token(), "get", 1, Integer.MAX_VALUE,
				0);
		log.info(campaignsRequest.toString());

		Campaigns campaigns = avazuClient.getCampaigns(campaignsRequest);
		assertNotNull(campaigns);
		log.info(campaigns.toString());

		for (CampaignDatum datum : campaigns.getData()) {
			log.info("{}", datum);
		}
		log.info("Number of campains " + campaigns.getData().size());
	}

	@Test
	public void testAvazuUtilsCampaigns() throws Exception {

		List<CampaignDatum> datums = AvazuUtils.getFullCampaignsDatum();
		assertNotNull(datums);

		//for (CampaignDatum datum : datums) {
		//	log.info("{}", datum);
		//}
		log.info("Number of campains " + datums.size());

		Map<String, String> campaignsWithBidTypes = AvazuUtils.getCampaignsWithBidTypes();
		//for (Entry<String, String> entry : campaignsWithBidTypes.entrySet()) {
		//	log.info("{}", entry);			
		//}
	}

	@Test
	@Ignore
	public void testAvazuUtilsReport() throws Exception {

		List<ReportDatum> datums = AvazuUtils.getFullReportDatum("creative", "2018-05-31", "2018-05-31", "site");
		assertNotNull(datums);

		// for (Datum datum : datums) {
		// log.info("{}", datum);
		// }

	}

}
