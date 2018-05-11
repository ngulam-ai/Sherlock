package agency.akcom.mmg.sherlock.ui;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import agency.akcom.mmg.sherlock.ui.server.avazu.client.AvazuClient;
import agency.akcom.mmg.sherlock.ui.server.avazu.client.AvazuClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Auth;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.AuthRequest;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Report;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.ReportRequest;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Report.Datum;
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
		
		Auth auth = avazuClient.getAuth(authRequest);
		assertNotNull(auth);
		log.info(auth.toString());

		ReportRequest reportRequest = new ReportRequest(auth.getAccess_token(), "campaign", "2018-04-02", "2018-04-02");
		log.info(reportRequest.toString());

		Report report = avazuClient.getReport(reportRequest);
		assertNotNull(report);
		log.info(report.toString());
		
		for (Datum datum : report.getData()) {
			log.info("{}", datum);
		}
		

	}

}
