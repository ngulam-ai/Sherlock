package agency.akcom.mmg.sherlock.ui;

import agency.akcom.mmg.sherlock.ui.server.facebook.FacebookUtils;
import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport;
import agency.akcom.mmg.sherlock.ui.server.facebook.model.FacebookReport.Data;

import java.util.Map;

public class FacebookApiClientTest {
    public static void main(String[] args) {
        FacebookUtils facebookUtils = new FacebookUtils();
        facebookUtils.getReport();

    }
}
