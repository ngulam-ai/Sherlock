package agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience;

import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.client.PerfectAudienceClient;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.client.PerfectAudienceClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model.AuthRequest;

public class PerfectAudienceUtils {
	
	static PerfectAudienceClientBuilder audienceClientBuilder = new PerfectAudienceClientBuilder();
	static PerfectAudienceClient audienceClient = audienceClientBuilder.getPerfectAudienceClient();
	
	public static void main(String[] args) {
		audienceClient.getAuth(new AuthRequest());
	}
}
