package agency.akcom.mmg.sherlock.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

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
	AuthRequest authRequest;
	AvazuUtils avazuUtils;

	@Before
	public void setup() {
		log.info("TEST SETUP");
		AvazuClientBuilder avazuClientBuilder = new AvazuClientBuilder();
		avazuClient = avazuClientBuilder.getAvazuClient();
		authRequest = new AuthRequest();
		authRequest.setClient_id("100304");
		authRequest.setClient_secret("02b7da4f561ca3be4db4ddd4aa4571cf");
		avazuUtils = new AvazuUtils(authRequest);
	}

	@Test
	@Ignore
	public void testReport() throws Exception {
		log.info(authRequest.toString());

		Auth auth = avazuClient.getAuth(authRequest);
		assertNotNull(auth);
		log.info(auth.toString());

		ReportRequest reportRequest = new ReportRequest(auth.getAccess_token(), "creative", "2018-08-29", "2018-08-29",
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
	public void testFullReportSumms() throws Exception {
		log.info("--------------------------");

		long impressions = 0;
		long clicks = 0;
		long conversions = 0;
		double spend = 0f;

		for (ReportDatum datum : avazuUtils.getFullReportDatum("creative", "2018-08-27", "2018-08-27", null)) {
			//log.info("{}", datum);
			impressions += datum.getImpressions();
			clicks += datum.getClicks();
			conversions += datum.getConversions();
			spend += datum.getSpend();
		}
		log.info("impressions: " + impressions);
		log.info("clicks: " + clicks);
		log.info("conversions: " + conversions);
		log.info("spend: " + spend);

		assertEquals(56436, impressions);
		assertEquals(19768, clicks);
		assertEquals(0, conversions);
		assertEquals(24.33f, spend, 0.01f);
	}

	@Test
	@Ignore
	public void testCampaign() throws Exception {
		log.info(authRequest.toString());

		Auth auth = avazuClient.getAuth(authRequest);
		assertNotNull(auth);
		log.info(auth.toString());

		CampaignsRequest campaignsRequest = new CampaignsRequest(auth.getAccess_token(), "get", 1, Integer.MAX_VALUE);
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

		List<CampaignDatum> datums = avazuUtils.getFullCampaignsDatum();
		assertNotNull(datums);

		// for (CampaignDatum datum : datums) {
		// log.info("{}", datum);
		// }
		log.info("Number of campains " + datums.size());

		Map<String, String> campaignsWithBidTypes = avazuUtils.getCampaignsWithBidTypes();
		// for (Entry<String, String> entry : campaignsWithBidTypes.entrySet()) {
		// log.info("{}", entry);
		// }
	}

	@Test
	@Ignore
	public void testAvazuUtilsReport() throws Exception {

		List<ReportDatum> datums = avazuUtils.getFullReportDatum("creative", "2018-05-31", "2018-05-31", "site");
		assertNotNull(datums);

		// for (Datum datum : datums) {
		// log.info("{}", datum);
		// }

	}

}
