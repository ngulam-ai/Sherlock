import static org.junit.Assert.assertNotNull;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import agency.akcom.mmg.sherlock.ui.server.avazu.clients.AvazuClient;
import agency.akcom.mmg.sherlock.ui.server.avazu.clients.AvazuClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.Auth;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.AuthRequest;
import agency.akcom.mmg.sherlock.ui.server.avazu.models.ReportRequest;
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
	public void test() throws Exception {
		AuthRequest authRequest = new AuthRequest();
		log.info(authRequest.toString());

		Auth auth = avazuClient.getAuth(authRequest.getClient_id(), authRequest.getClient_secret(),
				authRequest.getGrant_type());
		assertNotNull(auth);
		log.info(auth.toString());

		ReportRequest reportRequest = new ReportRequest(auth.getAccess_token(), "creative", "2018-04-01", "2018-04-30");
		log.info(reportRequest.toString());

		JSONObject reportJson = avazuClient.getReportAsJSON(reportRequest);
		assertNotNull(reportJson);
		log.info(reportJson.toString());

	}

}
